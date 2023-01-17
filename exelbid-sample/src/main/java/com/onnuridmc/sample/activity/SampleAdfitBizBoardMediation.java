package com.onnuridmc.sample.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.kakao.adfit.ads.na.AdFitAdInfoIconPosition;
import com.kakao.adfit.ads.na.AdFitBizBoardAdTemplateLayout;
import com.kakao.adfit.ads.na.AdFitNativeAdBinder;
import com.kakao.adfit.ads.na.AdFitNativeAdLoader;
import com.kakao.adfit.ads.na.AdFitNativeAdRequest;
import com.kakao.adfit.ads.na.AdFitVideoAutoPlayPolicy;
import com.onnuridmc.exelbid.ExelBid;
import com.onnuridmc.exelbid.ExelBidNative;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeListener;
import com.onnuridmc.exelbid.common.OnMediationOrderResultListener;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationOrderResult;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationType;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;

public class SampleAdfitBizBoardMediation extends SampleBase implements View.OnClickListener {

    private Button loadBtn;
    private Button showBtn;
    EditText mEdtAdUnit;

    // Exelbid
    private MediationOrderResult mediationOrderResult;
    private MediationType currentMediationType;

    private View exelbidNativeLayout;
    private ExelBidNative exelBidNative;

    // Kakao Adfit
    private AdFitBizBoardAdTemplateLayout bizBoardAdTemplateLayout;
    AdFitNativeAdLoader.AdLoadListener adFitAdLoadListener;
    AdFitNativeAdRequest adfitRequest;
    AdFitNativeAdLoader adFitNativeAdLoader;
    private AdFitNativeAdBinder adfitNativeAdBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediation_adfit_bizboard);

        TextView titleTv = findViewById(R.id.tv);
        titleTv.setText(TAG);
        loadBtn = findViewById(R.id.load_btn);
        showBtn = findViewById(R.id.show_btn);
        loadBtn.setOnClickListener(this);
        showBtn.setOnClickListener(this);
        showBtn.setEnabled(false);

        mEdtAdUnit = findViewById(R.id.unit_id);
        mEdtAdUnit.setText(PrefManager.getPref(this, PrefManager.KEY_NATIVE_AD, UNIT_ID_EXELBID_NATIVE));

        logText = findViewById(R.id.log_txt);
        logText.setMovementMethod(new ScrollingMovementMethod());
        logsb = new StringBuffer("------------- LOG -------------\n");

        /********************************************************************************
         * Exelbid 설정
         *******************************************************************************/
        initExelbid();

        /********************************************************************************
         * Exelbid 설정
         *******************************************************************************/
        initAdfit();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.load_btn) {
            hideAllView();
            showBtn.setEnabled(false);

            /**
             * MediationOrder 사용시 설정
             */
            ArrayList<MediationType> mediationUseList = new ArrayList(Arrays.asList(MediationType.ADMOB, MediationType.FAN));
            ExelBid.getMediationData(SampleAdfitBizBoardMediation.this, mEdtAdUnit.getText().toString(), mediationUseList,
                    new OnMediationOrderResultListener() {
                        @Override
                        public void onMediationOrderResult(MediationOrderResult mediationOrderResult) {
                            printLog("Mediation","onMediationOrderResult");
                            if(mediationOrderResult != null && mediationOrderResult.getSize() > 0) {
                                SampleAdfitBizBoardMediation.this.mediationOrderResult = mediationOrderResult;
                                loadMediation();
                            }
                        }

                        @Override
                        public void onMediationFail(int errorCode, String errorMsg) {
                            printLog("Mediation","onMediationFail :: " + errorMsg + "(" + errorCode + ")");
                        }
                    });
            printLog("Mediation","Request...");
        } else if(v.getId() == R.id.show_btn) {
            showMediation();
        }
    }

    @SuppressLint("MissingPermission")
    private void loadMediation() {

        if(mediationOrderResult == null) {
            return;
        }

        printLog("Mediation List size", "" + mediationOrderResult.getSize());
        currentMediationType = mediationOrderResult.poll();
        if (currentMediationType != null) {
            if (currentMediationType.equals(MediationType.EXELBID)) {
                mediationOrderResult = null;
                exelBidNative.loadAd();
            } else if (currentMediationType.equals(MediationType.ADFIT)) {
                adFitNativeAdLoader.loadAd(adfitRequest, adFitAdLoadListener);
            } else {
                printLog(currentMediationType.toString(), "Not Setting...");
                loadMediation();
                return;
            }
            printLog(currentMediationType.toString(), "Request...");
        }
    }

    private void showMediation() {
        if(currentMediationType != null) {
            hideAllView();
            printLog(currentMediationType.toString(),"Show");
            if (currentMediationType.equals(MediationType.EXELBID)) {
                if (exelbidNativeLayout != null) {
                    exelbidNativeLayout.setVisibility(View.VISIBLE);
                }
            } else if (currentMediationType.equals(MediationType.ADFIT)) {
                if(bizBoardAdTemplateLayout != null) {
                    bizBoardAdTemplateLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void hideAllView() {
        if(exelbidNativeLayout != null) {
            exelbidNativeLayout.setVisibility(View.GONE);
        }
        if(bizBoardAdTemplateLayout != null) {
            bizBoardAdTemplateLayout.setVisibility(View.GONE);
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (exelBidNative != null) {
            exelBidNative.onPause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (exelBidNative != null) {
            exelBidNative.onResume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (exelBidNative != null) {
            exelBidNative.onDestroy();
        }

        if(adfitNativeAdBinder != null) {
            adfitNativeAdBinder.unbind();
            adfitNativeAdBinder = null;
        }
        adFitNativeAdLoader = null;

        super.onDestroy();
    }

    private void initExelbid() {

        // 네이티브 요청 객체를 생성한다.
        exelBidNative = new ExelBidNative(this, mEdtAdUnit.getText().toString(), new OnAdNativeListener() {
            @Override
            public void onFailed(ExelBidError error, int statusCode) {
                printLog("Exelbid","onFailed " + error.toString());
                exelbidNativeLayout.setVisibility(View.GONE);
                loadMediation();
            }

            @Override
            public void onShow() {
                printLog("Exelbid","onShow");
            }

            @Override
            public void onClick() {
                printLog("Exelbid","onClick");
            }

            @Override
            public void onLoaded() {
                printLog("Exelbid","onLoaded");
                // Exelbid는 자동 노출 처리한다.
                exelbidNativeLayout.setVisibility(View.VISIBLE);
                exelBidNative.show();
            }
        });

        exelbidNativeLayout = findViewById(R.id.exelbid_native_container);
        exelBidNative.setNativeViewBinder(new NativeViewBinder.Builder(exelbidNativeLayout)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        exelBidNative.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
    }

    /**
     * Kakao Adfit 설정
     */
    private void initAdfit() {
        bizBoardAdTemplateLayout = findViewById(R.id.biz_board_ad_template_layout);
        adFitNativeAdLoader = AdFitNativeAdLoader.create(this, "test-id");
        adfitRequest = new AdFitNativeAdRequest.Builder()
                .setAdInfoIconPosition(AdFitAdInfoIconPosition.RIGHT_TOP) // 광고 정보 아이콘 위치 설정 (container view 내에서의 광고 아이콘 위치)
                .setVideoAutoPlayPolicy(AdFitVideoAutoPlayPolicy.WIFI_ONLY) // 비디오 광고 자동 재생 정책 설정
                .build();
        adFitAdLoadListener = new AdFitNativeAdLoader.AdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull AdFitNativeAdBinder binder) {
                printLog("ADFIT","Load");
                if (adFitNativeAdLoader == null) {
                    // [Activity]가 먼저 종료된 경우, 메모리 누수(Memory Leak) 및 오류를 방지를 위해 응답을 무시
                    return;
                }
                // 광고 노출
                adfitNativeAdBinder = binder;
                binder.bind(bizBoardAdTemplateLayout);

            }

            @Override
            public void onAdLoadError(int errorCode) {
                printLog("ADFIT","Fail errorCode : " + errorCode);
                bizBoardAdTemplateLayout.setVisibility(View.GONE);
                loadMediation();
            }
        };

    }
}