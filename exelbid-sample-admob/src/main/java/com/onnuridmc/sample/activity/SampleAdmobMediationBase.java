package com.onnuridmc.sample.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdapterResponseInfo;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.onnuridmc.exelbid.mediation.admob.ExelBidCustomEvent;
import com.onnuridmc.sample.admob.R;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * AdMob 미디에이션 워터폴에서 ExelBid 커스텀 이벤트가 실제로 호출되는지 확인하는 화면의 공통 뼈대.
 *
 * <p>확인 순서는 다음과 같다.
 * <ol>
 *     <li>AdMob이 ExelBid 어댑터 클래스를 찾았는가 — 초기화 상태로 확인</li>
 *     <li>워터폴에서 ExelBid이 호출되었는가 — {@link AdapterResponseInfo} 목록으로 확인</li>
 *     <li>ExelBid이 광고를 채웠는가 — 최종 낙찰 어댑터로 확인</li>
 * </ol>
 *
 * <p><b>주의</b>: Google 테스트 광고 유닛은 미디에이션을 타지 않고 항상 Google 테스트 광고를
 * 반환한다. ExelBid 커스텀 이벤트를 검증하려면 미디에이션 그룹이 설정된 실제 유닛을 써야 한다.
 */
abstract class SampleAdmobMediationBase extends AppCompatActivity {

    private static final String TAG = "AdMobMediation";

    /** 패키지 + 대문자로 시작하는 클래스명 형태. credentials 값 중 클래스명을 골라내는 데 쓴다. */
    private static final Pattern CLASS_NAME_PATTERN =
            Pattern.compile("([a-zA-Z_$][\\w$]*\\.)+[A-Z][\\w$]*");

    private TextView mTvStatus;
    private TextView mTvDiagnostics;
    private TextView mTvUnitId;
    private RadioButton mRbMediation;

    protected Button mBtnLoad;
    protected Button mBtnShow;
    protected FrameLayout mAdContainer;

    /**
     * 어댑터 등록 현황 블록. 요청마다 새로 찍히는 로그와 달리 세션 내내 유지되어야 하므로
     * 따로 보관했다가 진단창을 비울 때 다시 채워 넣는다.
     */
    private String mAdapterStatusBlock = "";

    /** 미디에이션 그룹이 설정된 실제 AdMob 광고 유닛. */
    protected abstract String getMediationUnitId();

    /** 비교용 Google 테스트 유닛. 미디에이션을 타지 않는다. */
    protected abstract String getTestUnitId();

    protected abstract String getScreenTitle();

    /** 실제 광고 요청. 선택된 유닛 ID가 전달된다. */
    protected abstract void loadAd(@NonNull String adUnitId);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admob_mediation_format);
        setTitle(getScreenTitle());

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvClassName = findViewById(R.id.tv_class_name);
        mTvUnitId = findViewById(R.id.tv_unit_id);
        mTvStatus = findViewById(R.id.tv_status);
        mTvDiagnostics = findViewById(R.id.tv_diagnostics);
        mRbMediation = findViewById(R.id.rb_mediation);
        mBtnLoad = findViewById(R.id.btn_load);
        mBtnShow = findViewById(R.id.btn_show);
        mAdContainer = findViewById(R.id.fl_ad_container);

        tvTitle.setText(getScreenTitle());
        // 문자열 리터럴 대신 클래스 참조를 쓴다. 패키지를 옮겨도 값이 자동으로 따라간다.
        tvClassName.setText(ExelBidCustomEvent.class.getName());

        ((RadioGroup) findViewById(R.id.rg_unit))
                .setOnCheckedChangeListener((group, checkedId) -> updateUnitIdLabel());
        updateUnitIdLabel();

        mBtnLoad.setOnClickListener(v -> {
            clearDiagnostics();
            setStatus("광고 요청 중...");
            loadAd(currentUnitId());
        });

        findViewById(R.id.btn_inspector).setOnClickListener(v -> openAdInspector());

        initializeMobileAds();
    }

    private String currentUnitId() {
        return mRbMediation.isChecked() ? getMediationUnitId() : getTestUnitId();
    }

    private void updateUnitIdLabel() {
        String suffix = mRbMediation.isChecked()
                ? ""
                : "\n(테스트 유닛이라 ExelBid 커스텀 이벤트는 호출되지 않습니다)";
        mTvUnitId.setText(currentUnitId() + suffix);
    }

    /**
     * AdMob SDK를 초기화하고 어댑터 등록 현황을 화면에 표시한다.
     *
     * <p>[AdMob 기본 연동 0] {@code MobileAds.initialize()} 는 미디에이션과 무관하게
     * 앱 시작 시 1회 호출하는 표준 절차다. 초기화 콜백의 상태 맵을 화면에 찍는 부분만
     * 이 샘플의 검증용 코드다.
     *
     * <p>ExelBid 커스텀 이벤트가 목록에 없다면 대시보드의 Class Name 이 틀렸거나
     * 어댑터 클래스가 앱에 포함되지 않은 것이다.
     */
    private void initializeMobileAds() {
        MobileAds.initialize(this, initializationStatus -> {
            StringBuilder sb = new StringBuilder("[AdMob 어댑터 초기화 현황]\n");
            appendAdapterStatus(sb, initializationStatus);
            mAdapterStatusBlock = sb.toString();
            appendDiagnostics(mAdapterStatusBlock);
        });
    }

    private void appendAdapterStatus(@NonNull StringBuilder sb, @NonNull InitializationStatus status) {
        Map<String, AdapterStatus> statusMap = status.getAdapterStatusMap();
        if (statusMap.isEmpty()) {
            sb.append("  등록된 어댑터 없음\n");
            return;
        }

        boolean exelBidFound = false;
        for (Map.Entry<String, AdapterStatus> entry : statusMap.entrySet()) {
            String className = entry.getKey();
            AdapterStatus adapterStatus = entry.getValue();
            boolean isExelBid = isExelBidAdapter(className);
            exelBidFound |= isExelBid;

            sb.append(isExelBid ? "  >> " : "     ")
                    .append(simpleName(className))
                    .append(" : ")
                    .append(adapterStatus.getInitializationState())
                    .append('\n');
        }

        if (!exelBidFound) {
            sb.append("\n  ExelBid 어댑터가 목록에 없습니다.\n")
                    .append("  AdMob 대시보드의 Class Name 을 확인하세요.\n");
        }
    }

    /**
     * 워터폴 진행 결과를 사람이 읽을 수 있는 형태로 출력한다.
     *
     * <p>{@link ResponseInfo#getAdapterResponses()} 에는 AdMob이 실제로 시도한 어댑터가
     * 순서대로 담긴다. 여기에 ExelBid이 보이지 않으면 대시보드 미디에이션 그룹 설정 문제이고,
     * 보이는데 실패했다면 어댑터 또는 ExelBid 응답 문제다.
     */
    protected void showWaterfall(@Nullable ResponseInfo responseInfo) {
        if (responseInfo == null) {
            appendDiagnostics("[워터폴] ResponseInfo 없음");
            return;
        }

        StringBuilder sb = new StringBuilder("[워터폴 시도 순서]\n");
        List<AdapterResponseInfo> responses = responseInfo.getAdapterResponses();
        if (responses.isEmpty()) {
            sb.append("  시도 기록 없음\n");
        }

        int index = 1;
        for (AdapterResponseInfo response : responses) {
            String wrapperName = response.getAdapterClassName();
            String customEventClass = findCustomEventClassName(response);
            String effectiveName = customEventClass != null ? customEventClass : wrapperName;
            boolean isExelBid = isExelBidAdapter(wrapperName) || isExelBidAdapter(customEventClass);
            AdError error = response.getAdError();

            sb.append(isExelBid ? "  >> " : "     ")
                    .append(index++).append(". ")
                    .append(simpleName(effectiveName))
                    .append(" (").append(response.getLatencyMillis()).append("ms) ")
                    .append(error == null
                            ? "성공"
                            : "실패 - " + error.getCode() + " " + error.getMessage())
                    .append('\n');

            if (customEventClass != null) {
                sb.append("        via ").append(shortName(wrapperName)).append('\n');
            }
            appendCredentials(sb, response);
        }

        String winner = responseInfo.getMediationAdapterClassName();
        sb.append("\n[낙찰 어댑터] ")
                .append(winner == null || winner.isEmpty() ? "AdMob 자체 광고" : simpleName(winner));

        if (isExelBidAdapter(winner)) {
            sb.append("\n\nExelBid 커스텀 이벤트가 광고를 채웠습니다.");
        }
        appendDiagnostics(sb.toString());
    }

    private void openAdInspector() {
        // 실제 워터폴 구성과 각 어댑터 응답을 AdMob이 직접 보여주는 도구.
        MobileAds.openAdInspector(this, error -> {
            if (error != null) {
                setStatus("광고 검사기 오류: " + error.getMessage());
            }
        });
    }

    /**
     * 커스텀 이벤트의 실제 클래스명을 찾는다.
     *
     * <p>{@link AdapterResponseInfo#getAdapterClassName()} 은 커스텀 이벤트일 때
     * Google 래퍼인 {@code com.google.ads.mediation.customevent.CustomEventAdapter} 를 돌려준다.
     * 대시보드에 등록한 실제 클래스명은 credentials 번들에 들어 있으므로 거기서 꺼낸다.
     * 키 이름이 GMA 버전에 따라 달라질 수 있어 값 중 클래스명 형태인 것을 찾는다.
     */
    @Nullable
    private static String findCustomEventClassName(@NonNull AdapterResponseInfo response) {
        Bundle credentials = response.getCredentials();
        if (credentials == null) {
            return null;
        }
        for (String key : credentials.keySet()) {
            Object value = credentials.get(key);
            if (value instanceof String && CLASS_NAME_PATTERN.matcher((String) value).matches()) {
                return (String) value;
            }
        }
        return null;
    }

    /**
     * credentials 내용을 출력한다. 대시보드에 입력한 Class Name 과 Parameter 가
     * 실제로 어댑터까지 전달됐는지 여기서 확인할 수 있다.
     */
    private static void appendCredentials(@NonNull StringBuilder sb, @NonNull AdapterResponseInfo response) {
        Bundle credentials = response.getCredentials();
        if (credentials == null || credentials.isEmpty()) {
            return;
        }
        for (String key : credentials.keySet()) {
            Object value = credentials.get(key);
            if (value == null) {
                continue;
            }
            String text = String.valueOf(value);
            if (text.length() > 60) {
                text = text.substring(0, 60) + "...";
            }
            sb.append("        ").append(key).append('=').append(text).append('\n');
        }
    }

    protected static boolean isExelBidAdapter(@Nullable String className) {
        return className != null && className.startsWith(ExelBidCustomEvent.class.getName());
    }

    private static String simpleName(@Nullable String className) {
        if (className == null || className.isEmpty()) {
            return "(없음)";
        }
        int dot = className.lastIndexOf('.');
        return dot < 0 ? className : className.substring(dot + 1) + "  [" + className + "]";
    }

    private static String shortName(@Nullable String className) {
        if (className == null || className.isEmpty()) {
            return "(없음)";
        }
        int dot = className.lastIndexOf('.');
        return dot < 0 ? className : className.substring(dot + 1);
    }

    protected void setStatus(@NonNull String message) {
        Log.d(TAG, message);
        runOnUiThread(() -> mTvStatus.setText(message));
    }

    protected void appendDiagnostics(@NonNull String block) {
        Log.d(TAG, block);
        runOnUiThread(() -> {
            CharSequence current = mTvDiagnostics.getText();
            mTvDiagnostics.setText(current.length() == 0 ? block : current + "\n\n" + block);
        });
    }

    private void clearDiagnostics() {
        // 어댑터 등록 현황은 남긴다. 요청 로그만 비운다.
        mTvDiagnostics.setText(mAdapterStatusBlock);
    }
}
