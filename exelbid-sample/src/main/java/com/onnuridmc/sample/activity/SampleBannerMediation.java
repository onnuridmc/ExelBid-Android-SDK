package com.onnuridmc.sample.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onnuridmc.exelbid.ExelBid;
import com.onnuridmc.exelbid.ExelBidAdView;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnBannerAdListener;
import com.onnuridmc.exelbid.common.OnMediationOrderResultListener;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationOrderResult;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationType;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;

public class SampleBannerMediation extends SampleBase implements View.OnClickListener {

    EditText mEdtAdUnit;
    // Exelbid
    private ExelBidAdView exelbidAdView;
    private MediationOrderResult mediationOrderResult;
    private MediationType currentMediationType;

    // AdMob
    private com.google.android.gms.ads.AdView admobView;

    //FaceBook
    private com.facebook.ads.AdView fanView;
    LinearLayout fanContainer;
    com.facebook.ads.AdListener fanAdListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediation_banner);

        TextView titleTv = findViewById(R.id.tv);
        titleTv.setText(TAG);
        findViewById(R.id.load_btn).setOnClickListener(this);

        mEdtAdUnit = findViewById(R.id.unit_id);
        mEdtAdUnit.setText(PrefManager.getPref(this, PrefManager.KEY_BANNER_AD, UNIT_ID_EXELBID_BANNER));

        logText = findViewById(R.id.log_txt);
        logText.setMovementMethod(new ScrollingMovementMethod());
        logsb = new StringBuffer("------------- LOG -------------\n");

        /********************************************************************************
         * Exelbid 설정
         *******************************************************************************/
        exelbidAdView = findViewById(R.id.exelbid_view);
        exelbidAdView.setAdUnitId(mEdtAdUnit.getText().toString());
        exelbidAdView.setAdListener(new OnBannerAdListener() {
            @Override
            public void onAdLoaded() {
                printLog("Exelbid","Load");
                exelbidAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailed(ExelBidError errorCode) {
                printLog("Exelbid","Fail : " + errorCode.getErrorMessage());
                exelbidAdView.setVisibility(View.GONE);
                loadMediation();
            }

            @Override
            public void onAdClicked() {
            }
        });


        /********************************************************************************
         * AdMob 설정
         *******************************************************************************/
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                printLog("ADMOB", "onInitializationComplete initializationStatus : " + initializationStatus.toString());
            }
        });
        admobView = findViewById(R.id.admob_view);
        admobView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                printLog("ADMOB","onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                printLog("ADMOB","onAdFailedToLoad : " + loadAdError.toString());
                loadMediation();
            }

            @Override
            public void onAdOpened() {
                printLog("ADMOB","onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                printLog("ADMOB","onAdLoaded");
                admobView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked() {
                printLog("ADMOB","onAdClicked");
            }

            @Override
            public void onAdImpression() {
                printLog("ADMOB","onAdImpression");
            }
        });


        /********************************************************************************
         * FaceBook 설정
         *******************************************************************************/
        fanContainer = findViewById(R.id.fan_container);
        fanAdListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                printLog("FAN","Error : " + adError.getErrorMessage());
                loadMediation();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                printLog("FAN",": Loaded " + ad.toString());
                fanContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.load_btn) {
            hideAllView();

            mediationOrderResult = null;
            // 1. 연동된 타사 광고 목록 설정
            ArrayList<MediationType> mediationUseList =
                    new ArrayList(Arrays.asList(MediationType.EXELBID, MediationType.ADMOB, MediationType.FAN));
            // 2. 타사 광고 최적화 순서를 받을 리스너 설정
            // 3. 타사 광고 목록과 리스너를 Exelbid 광고 객체에 설정한다.
            ExelBid.getMediationData(SampleBannerMediation.this, mEdtAdUnit.getText().toString(), mediationUseList
                    , new OnMediationOrderResultListener() {

                        @Override
                        public void onMediationOrderResult(MediationOrderResult mediationOrderResult) {
                            printLog("Mediation","onMediationOrderResult");

                            if(mediationOrderResult != null && mediationOrderResult.getSize() > 0) {
                                SampleBannerMediation.this.mediationOrderResult = mediationOrderResult;
                                // 4. 타사 광고 호출 로직 구현
                                loadMediation();
                            }
                        }

                        @Override
                        public void onMediationFail(int errorCode, String errorMsg) {
                            printLog("Mediation","onMediationFail :: " + errorMsg + "(" + errorCode + ")");
                        }
                    });
            printLog("Mediation","Request...");
        }
    }

    /**
     * MediationOrderResult가 응답 되었고 각각의 SDK 광고 요청 실패시 호출
     */
    @SuppressLint("MissingPermission")
    private void loadMediation() {

        if(mediationOrderResult == null) {
            return;
        }

        currentMediationType = mediationOrderResult.poll();
        if(currentMediationType != null) {
            // 타사 SDK의 종류와 형식(배너, 전면, 네이티브) 에 따라서 광고 요청 로직을 적용한다
            if (currentMediationType.equals(MediationType.EXELBID)) {
                exelbidAdView.loadAd();
                printLog("Exelbid","Request...");
            } else if (currentMediationType.equals(MediationType.ADMOB)) {
//                MobileAds.setRequestConfiguration(
//                        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
//                                .build());
                admobView.loadAd(new AdRequest.Builder().build());
                printLog("ADMOB","Request...");
            } else if (currentMediationType.equals(MediationType.FAN)) {
                fanView = new com.facebook.ads.AdView(this, UNIT_ID_FAN_BANNER, AdSize.BANNER_HEIGHT_50);
                fanContainer.addView(fanView);
                fanView.loadAd(fanView.buildLoadAdConfig().withAdListener(fanAdListener).build());
                printLog("FAN","Request...");
            }
        }
    }

    private void hideAllView() {
        if(exelbidAdView != null) {
            exelbidAdView.setVisibility(View.GONE);
        }
        if(admobView != null) {
            admobView.setVisibility(View.GONE);
        }
        if(fanContainer != null) {
            fanContainer.setVisibility(View.GONE);
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (exelbidAdView != null) {
            exelbidAdView.onPause();
        }
        if (admobView != null) {
            admobView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (exelbidAdView != null) {
            exelbidAdView.onResume();
        }
        if (admobView != null) {
            admobView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (exelbidAdView != null) {
            exelbidAdView.onDestroy();
        }
        if (admobView != null) {
            admobView.destroy();
        }
        super.onDestroy();
    }
}