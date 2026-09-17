package com.onnuridmc.sample.nextgen;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.ads.mobile.sdk.MobileAds;
import com.google.android.libraries.ads.mobile.sdk.common.AdSourceResponseInfo;
import com.google.android.libraries.ads.mobile.sdk.common.MediationAdError;
import com.google.android.libraries.ads.mobile.sdk.common.ResponseInfo;
import com.google.android.libraries.ads.mobile.sdk.initialization.AdapterStatus;
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig;
import com.onnuridmc.exelbid.mediation.admob.ExelBidCustomEvent;

import java.util.List;
import java.util.Map;

/**
 * GMA Next-Gen SDK(ads-mobile-sdk)에서 ExelBid 커스텀 이벤트가 동작하는지 검증하는 공통 화면.
 *
 * <p>레거시 검증 화면(exelbid-demo의 AdMobMediationBaseActivity)과 같은 사상으로,
 * 다음 세 가지를 화면에 표시한다.
 * <ol>
 *     <li>어댑터 초기화 현황 — Next-Gen SDK가 레거시 API 기반 ExelBid 어댑터를 인식하는가</li>
 *     <li>워터폴 시도 순서 — {@link ResponseInfo#getAdSourceResponses()}</li>
 *     <li>낙찰 광고 소스 — {@link ResponseInfo#getLoadedAdSourceResponseInfo()}</li>
 * </ol>
 *
 * <p><b>Next-Gen 차이점</b>: AdMob 앱 ID를 매니페스트가 아니라
 * {@link InitializationConfig}로 코드에서 전달하며, 초기화 전 로드는 예외를 던진다.
 */
public abstract class NextGenMediationBaseActivity extends AppCompatActivity {

    protected static final String TAG = "NextGenMediation";

    /** ExelBid 데모 AdMob 앱. 레거시 검증과 동일한 계정/유닛을 사용해 결과를 비교한다. */
    static final String ADMOB_APP_ID = "ca-app-pub-3021369012722157~1149498357";
    static final String UNIT_ID_BANNER = "ca-app-pub-3021369012722157/3787156557";
    static final String UNIT_ID_INTERSTITIAL = "ca-app-pub-3021369012722157/4975883638";
    static final String UNIT_ID_NATIVE = "ca-app-pub-3021369012722157/5189927040";

    /** 초기화는 프로세스당 1회. 화면 재진입 시 중복 초기화를 막는다. */
    private static volatile boolean sInitialized = false;

    private TextView mTvStatus;
    private TextView mTvDiagnostics;
    private String mAdapterStatusBlock = "";

    protected Button mBtnLoad;
    protected Button mBtnShow;
    protected FrameLayout mAdContainer;

    protected abstract String getScreenTitle();

    protected abstract String getUnitId();

    /** 실제 광고 요청. 초기화 완료 후에만 호출된다. */
    protected abstract void loadAd();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextgen_format);
        setTitle(getScreenTitle());

        ((TextView) findViewById(R.id.tv_title)).setText(getScreenTitle());
        ((TextView) findViewById(R.id.tv_class_name)).setText(ExelBidCustomEvent.class.getName());
        ((TextView) findViewById(R.id.tv_unit_id)).setText(getUnitId());
        mTvStatus = findViewById(R.id.tv_status);
        mTvDiagnostics = findViewById(R.id.tv_diagnostics);
        mBtnLoad = findViewById(R.id.btn_load);
        mBtnShow = findViewById(R.id.btn_show);
        mAdContainer = findViewById(R.id.fl_ad_container);

        mBtnLoad.setEnabled(false);
        mBtnLoad.setOnClickListener(v -> {
            clearDiagnostics();
            setStatus("광고 요청 중...");
            loadAd();
        });

        findViewById(R.id.btn_inspector).setOnClickListener(v ->
                MobileAds.openAdInspector(error -> {
                    if (error != null) {
                        setStatus("광고 검사기 오류: " + error.getMessage());
                    }
                }));

        initializeMobileAds();
    }

    /**
     * Next-Gen SDK 초기화.
     *
     * <p>레거시와 달리 (1) 앱 ID를 코드로 전달하고, (2) 초기화 전에 로드하면
     * 예외가 발생하며, (3) 메인 스레드에서 호출하면 ANR 경고가 있어
     * 백그라운드 스레드에서 호출한다.
     */
    private void initializeMobileAds() {
        if (sInitialized) {
            setStatus("초기화 완료 (이전 화면에서 수행됨)");
            mBtnLoad.setEnabled(true);
            return;
        }
        setStatus("Next-Gen SDK 초기화 중...");
        new Thread(() -> MobileAds.initialize(
                getApplicationContext(),
                new InitializationConfig.Builder(ADMOB_APP_ID).build(),
                initializationStatus -> {
                    sInitialized = true;
                    StringBuilder sb = new StringBuilder("[Next-Gen 어댑터 초기화 현황]\n");
                    appendAdapterStatus(sb, initializationStatus.getAdapterStatusMap());
                    mAdapterStatusBlock = sb.toString();
                    appendDiagnostics(mAdapterStatusBlock);
                    runOnUiThread(() -> {
                        setStatus("초기화 완료 — 광고를 로드하세요");
                        mBtnLoad.setEnabled(true);
                    });
                })).start();
    }

    private void appendAdapterStatus(@NonNull StringBuilder sb,
                                     @NonNull Map<String, AdapterStatus> statusMap) {
        if (statusMap.isEmpty()) {
            sb.append("  등록된 어댑터 없음\n");
            return;
        }
        boolean exelBidFound = false;
        for (Map.Entry<String, AdapterStatus> entry : statusMap.entrySet()) {
            boolean isExelBid = isExelBidAdapter(entry.getKey());
            exelBidFound |= isExelBid;
            sb.append(isExelBid ? "  >> " : "     ")
                    .append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue().getInitializationState())
                    .append('\n');
        }
        if (!exelBidFound) {
            sb.append("\n  ExelBid 어댑터가 목록에 없습니다.\n");
        }
    }

    /**
     * Next-Gen 워터폴 진단. 레거시의 {@code AdapterResponseInfo} 가
     * {@link AdSourceResponseInfo} 로 바뀌었고, 광고 소스 이름/인스턴스 정보가 추가됐다.
     */
    protected void showWaterfall(@Nullable ResponseInfo responseInfo) {
        if (responseInfo == null) {
            appendDiagnostics("[워터폴] ResponseInfo 없음");
            return;
        }

        StringBuilder sb = new StringBuilder("[워터폴 시도 순서]\n");
        List<AdSourceResponseInfo> responses = responseInfo.getAdSourceResponses();
        if (responses.isEmpty()) {
            sb.append("  시도 기록 없음\n");
        }

        int index = 1;
        for (AdSourceResponseInfo response : responses) {
            String className = response.getAdapterClassName();
            boolean isExelBid = isExelBidAdapter(className);
            MediationAdError error = response.getAdError();

            sb.append(isExelBid ? "  >> " : "     ")
                    .append(index++).append(". ")
                    .append(className)
                    .append(" (").append(response.getLatencyMillis()).append("ms) ")
                    .append(error == null ? "성공" : "실패 - " + error.getMessage())
                    .append('\n');
            if (response.getCredentials() != null) {
                for (String key : response.getCredentials().keySet()) {
                    Object value = response.getCredentials().get(key);
                    sb.append("        ").append(key).append('=').append(value).append('\n');
                }
            }
        }

        AdSourceResponseInfo winner = responseInfo.getLoadedAdSourceResponseInfo();
        String winnerClass = winner == null ? null : winner.getAdapterClassName();
        sb.append("\n[낙찰 광고 소스] ")
                .append(winnerClass == null || winnerClass.isEmpty() ? "AdMob 자체 광고" : winnerClass);
        if (isExelBidAdapter(winnerClass)) {
            sb.append("\n\nNext-Gen SDK에서 ExelBid 커스텀 이벤트가 광고를 채웠습니다.");
        }
        appendDiagnostics(sb.toString());
    }

    protected static boolean isExelBidAdapter(@Nullable String className) {
        return className != null && className.startsWith(ExelBidCustomEvent.class.getName());
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
        mTvDiagnostics.setText(mAdapterStatusBlock);
    }
}
