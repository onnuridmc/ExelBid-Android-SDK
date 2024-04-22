package com.onnuridmc.sample.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAd;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAdInteractionCallback;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAdInteractionListener;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAdLoadListener;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerRequest;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerSize;
import com.bytedance.sdk.openadsdk.api.init.PAGConfig;
import com.bytedance.sdk.openadsdk.api.init.PAGSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.fyber.inneractive.sdk.external.InneractiveAdManager;
import com.fyber.inneractive.sdk.external.InneractiveAdRequest;
import com.fyber.inneractive.sdk.external.InneractiveAdSpot;
import com.fyber.inneractive.sdk.external.InneractiveAdSpotManager;
import com.fyber.inneractive.sdk.external.InneractiveAdViewEventsListener;
import com.fyber.inneractive.sdk.external.InneractiveAdViewUnitController;
import com.fyber.inneractive.sdk.external.InneractiveErrorCode;
import com.fyber.inneractive.sdk.external.InneractiveUnitController;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.kakao.adfit.ads.ba.BannerAdView;
import com.onnuridmc.exelbid.ExelBid;
import com.onnuridmc.exelbid.ExelBidAdView;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnBannerAdListener;
import com.onnuridmc.exelbid.common.OnMediationOrderResultListener;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationOrderResult;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationType;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;
import com.tnkfactory.ad.AdItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SampleBannerMediation extends SampleBase implements View.OnClickListener {

    private MediationOrderResult mediationOrderResult;
    private MediationType currentMediationType;
    EditText mEdtAdUnit;
    // Exelbid
    private ExelBidAdView exelbidAdView;

    // AdMob
    private com.google.android.gms.ads.AdView admobView;

    //FaceBook
    private com.facebook.ads.AdView fanView;
    LinearLayout fanAdView;
    com.facebook.ads.AdListener fanAdListener;

    // Kakao adfit
    private BannerAdView adfitAdView;

    // DigitalTurbine
    private ViewGroup dtView;
    private InneractiveAdSpot dtAdSpot;
    private InneractiveAdRequest dtAdRequest;
    private InneractiveAdViewUnitController dtAdController;

    // pangle
    private ViewGroup pangleView;
    private PAGBannerAd pagAd;
    private PAGBannerRequest pagRequest;
    private PAGBannerAdLoadListener pagAdListener;
    private PAGBannerAdInteractionListener pagInteractListener;

    // Applovin
    private MaxAdView maxAdView;

    // Tnk
    private com.tnkfactory.ad.BannerAdView tnkAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediation_banner);

        TextView titleTv = findViewById(R.id.tv);
        titleTv.setText(TAG);
        findViewById(R.id.load_btn).setOnClickListener(this);

        mEdtAdUnit = findViewById(R.id.unit_id);
        mEdtAdUnit.setText(PrefManager.getPref(this, PrefManager.KEY_BANNER_AD, "2680f2fc8eee4dbb397589d1b12bc80979ec0ea9"));

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
            public void onAdFailed(ExelBidError errorCode, int statusCode) {
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
        fanAdView = findViewById(R.id.fan_view);
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
                fanAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        /********************************************************************************
         * kakao adfit 설정
         *******************************************************************************/
        adfitAdView = findViewById(R.id.adfit_view);
        adfitAdView.setClientId("test-id");
        adfitAdView.setAdListener(new com.kakao.adfit.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                printLog("Adfit",": onAdLoaded ");
                adfitAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailed(int i) {
                printLog("Adfit",": onAdFailed");
                loadMediation();
            }

            @Override
            public void onAdClicked() {

            }
        });

        /********************************************************************************
         * DigitalTurbine 설정
         *******************************************************************************/
        InneractiveAdManager.initialize(getApplicationContext(), APP_ID_DT);
        InneractiveAdManager.setUserId("userId");

        dtView = findViewById(R.id.dt_view);
        dtAdRequest = new InneractiveAdRequest(UNIT_ID_DT_BANNER);
        dtAdSpot = InneractiveAdSpotManager.get().createSpot();
        dtAdController = new InneractiveAdViewUnitController();
        dtAdSpot.addUnitController(dtAdController);
        dtAdSpot.setRequestListener(new InneractiveAdSpot.RequestListener() {
            @Override
            public void onInneractiveSuccessfulAdRequest(InneractiveAdSpot inneractiveAdSpot) {
                printLog("DT",": onSuccessfulAdRequest ");
                if (dtAdSpot.isReady()) {
                    dtAdController.bindView(dtView);
                    dtView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onInneractiveFailedAdRequest(InneractiveAdSpot inneractiveAdSpot, InneractiveErrorCode inneractiveErrorCode) {
                printLog("DT","onFailedAdRequest : " + inneractiveErrorCode.toString());
                loadMediation();
            }
        });
        dtAdController.setEventsListener(new InneractiveAdViewEventsListener() {
            @Override
            public void onAdImpression(InneractiveAdSpot inneractiveAdSpot) {
                printLog("DT","onAdImpression");
            }

            @Override
            public void onAdClicked(InneractiveAdSpot inneractiveAdSpot) {
                printLog("DT","onAdClicked");
            }

            @Override
            public void onAdWillCloseInternalBrowser(InneractiveAdSpot inneractiveAdSpot) {

            }

            @Override
            public void onAdWillOpenExternalApp(InneractiveAdSpot inneractiveAdSpot) {

            }

            @Override
            public void onAdEnteredErrorState(InneractiveAdSpot inneractiveAdSpot, InneractiveUnitController.AdDisplayError adDisplayError) {

            }

            @Override
            public void onAdExpanded(InneractiveAdSpot inneractiveAdSpot) {

            }

            @Override
            public void onAdResized(InneractiveAdSpot inneractiveAdSpot) {

            }

            @Override
            public void onAdCollapsed(InneractiveAdSpot inneractiveAdSpot) {

            }
        });

        /********************************************************************************
         * PANGLE 설정
         *******************************************************************************/
        pangleView = findViewById(R.id.pangle_view);
        PAGConfig pagInitConfig =  new PAGConfig.Builder()
                .appId(APP_ID_PANGLE)
                .debugLog(true)
                .build();
        PAGSdk.init(this, pagInitConfig, new PAGSdk.PAGInitCallback() {
            @Override
            public void success() {
                printLog("pangle", "pangle init success: ");
            }

            @Override
            public void fail(int code, String msg) {
                printLog("pangle", "pangle init fail: " + code);
            }
        });

        PAGBannerSize bannerSize = PAGBannerSize.BANNER_W_320_H_50;
        pagRequest = new PAGBannerRequest(bannerSize);
        pagAd = new PAGBannerAd() {
            @Override
            public void setAdInteractionListener(PAGBannerAdInteractionListener pagBannerAdInteractionListener) {

            }

            @Override
            public void setAdInteractionCallback(PAGBannerAdInteractionCallback pagBannerAdInteractionCallback) {

            }

            @Override
            public View getBannerView() {
                return null;
            }

            @Override
            public void destroy() {

            }

            @Override
            public void win(Double aDouble) {

            }

            @Override
            public void loss(Double aDouble, String s, String s1) {

            }

            @Override
            public Map<String, Object> getMediaExtraInfo() {
                return null;
            }
        };

        pagAdListener = new PAGBannerAdLoadListener() {
            @Override
            public void onError(int i, String s) {
                printLog("PANGLE","onError");
                loadMediation();
            }

            @Override
            public void onAdLoaded(PAGBannerAd pagBannerAd) {
                printLog("PANGLE","onAdLoaded");
                if (pagBannerAd != null) {
                    pagAd = pagBannerAd;
                    pagAd.setAdInteractionListener(pagInteractListener);
                    pangleView.addView(pagAd.getBannerView());
                    pangleView.setVisibility(View.VISIBLE);
                }
            }
        };

        pagInteractListener = new PAGBannerAdInteractionListener() {
            @Override
            public void onAdShowed() {
                printLog("PANGLE","onAdShowed");
            }

            @Override
            public void onAdClicked() {
                printLog("PANGLE","onAdClicked");
            }

            @Override
            public void onAdDismissed() {
                printLog("PANGLE","onAdDismissed");
            }
        };

        /********************************************************************************
         *  APPLOVIN 설정
         *******************************************************************************/
        // AppLovin SDK Initialization
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                printLog("APPLOVIN", "SDK Initialized");
            }
        });

        // Initialize MaxAdView and set the listener
        maxAdView = findViewById(R.id.max_view); // Ensure you have a MaxAdView in your layout
        maxAdView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdLoaded(@NonNull MaxAd maxAd) {
                printLog("APPLOVIN","onAdLoaded");
                maxAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdDisplayed(@NonNull MaxAd maxAd) {
                printLog("APPLOVIN","onAdDisplayed");
            }

            @Override
            public void onAdHidden(@NonNull MaxAd maxAd) {
                printLog("APPLOVIN","onAdDisplayed");
            }

            @Override
            public void onAdClicked(@NonNull MaxAd maxAd) {
                printLog("APPLOVIN","onAdClicked");
            }

            @Override
            public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
                printLog("APPLOVIN","onAdLoadFailed");
                loadMediation();
            }

            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
                printLog("APPLOVIN","onAdDisplayFailed");
            }

            @Override
            public void onAdExpanded(@NonNull MaxAd maxAd) {
                printLog("APPLOVIN","onAdExpanded");
            }

            @Override
            public void onAdCollapsed(@NonNull MaxAd maxAd) {
                printLog("APPLOVIN","onAdCollapsed");
            }
        });

        /********************************************************************************
         * Tnk 설정
         *******************************************************************************/
        tnkAdView = findViewById(R.id.tnk_view);
        tnkAdView.setListener(new com.tnkfactory.ad.AdListener() {
            @Override
            public void onClick(AdItem adItem) {
                super.onClick(adItem);
                printLog("TNK","onClick");
            }

            @Override
            public void onShow(AdItem adItem) {
                super.onShow(adItem);
                printLog("TNK","onShow");
            }

            @Override
            public void onError(AdItem adItem, com.tnkfactory.ad.AdError adError) {
                super.onError(adItem, adError);
                printLog("TNK","onError");
                loadMediation();
            }

            @Override
            public void onLoad(AdItem adItem) {
                super.onLoad(adItem);
                printLog("TNK","onLoad");
                tnkAdView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.load_btn) {
            hideAllView();
            mediationOrderResult = null;
            // 1. 연동된 타사 광고 목록 설정
            ArrayList<MediationType> mediationUseList =
                    new ArrayList(Arrays.asList(
                            MediationType.EXELBID,
                            MediationType.ADMOB,
                            MediationType.FAN,
                            MediationType.ADFIT,
                            MediationType.DT,
                            MediationType.PANGLE,
                            MediationType.APPLOVIN,
                            MediationType.TNK
                    ));
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
                fanAdView.addView(fanView);
                fanView.loadAd(fanView.buildLoadAdConfig().withAdListener(fanAdListener).build());
                printLog("FAN","Request...");
            } else if (currentMediationType.equals(MediationType.ADFIT)) {
                adfitAdView.loadAd();
                printLog("Adfit","Request...");
            } else if (currentMediationType.equals(MediationType.DT)) {
                if (dtAdSpot.isReady()) {
                    dtAdController.unbindView(dtView);
                }
                dtAdSpot.requestAd(dtAdRequest);
                printLog("DT","Request...");
            } else if (currentMediationType.equals(MediationType.PANGLE)) {
                if(pagAd != null) {
                    pangleView.removeView(pagAd.getBannerView());
                }
                pagAd.loadAd(UNIT_ID_PANGLE_BANNER, pagRequest, pagAdListener);
                printLog("PANGLE","Request...");
            } else if (currentMediationType.equals(MediationType.APPLOVIN)) {
                maxAdView.loadAd();
                printLog("APPLOVIN","Request...");
            } else if (currentMediationType.equals(MediationType.TNK)) {
                tnkAdView.load();
                printLog("TNK","Request...");
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
        if(fanAdView != null) {
            fanAdView.setVisibility(View.GONE);
        }
        if(adfitAdView != null) {
            adfitAdView.setVisibility(View.GONE);
        }
        if(dtView != null) {
            dtView.setVisibility(View.GONE);
        }
        if(pangleView != null) {
            pangleView.setVisibility(View.GONE);
        }
        if(maxAdView != null) {
            maxAdView.setVisibility(View.GONE);
        }
        if(tnkAdView != null) {
            tnkAdView.setVisibility(View.GONE);
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
        if (adfitAdView != null) {
            adfitAdView.pause();
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
        if (adfitAdView != null) {
            adfitAdView.resume();
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
        if (adfitAdView != null) {
            adfitAdView.destroy();
            adfitAdView = null;
        }
        if(dtAdSpot != null) {
            dtAdSpot.destroy();
            dtAdSpot = null;
        }
        if(pagAd != null){
            pagAd.destroy();
        }
        if(maxAdView != null){
            maxAdView.destroy();
        }
        if(tnkAdView != null){
            tnkAdView = null;
        }
        super.onDestroy();
    }
}