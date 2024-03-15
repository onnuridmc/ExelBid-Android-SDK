package com.onnuridmc.sample.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.api.init.PAGConfig;
import com.bytedance.sdk.openadsdk.api.init.PAGSdk;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdInteractionCallback;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdInteractionListener;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdLoadListener;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialRequest;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAdListener;
import com.fyber.inneractive.sdk.external.InneractiveAdManager;
import com.fyber.inneractive.sdk.external.InneractiveAdRequest;
import com.fyber.inneractive.sdk.external.InneractiveAdSpot;
import com.fyber.inneractive.sdk.external.InneractiveAdSpotManager;
import com.fyber.inneractive.sdk.external.InneractiveErrorCode;
import com.fyber.inneractive.sdk.external.InneractiveFullscreenAdEventsListener;
import com.fyber.inneractive.sdk.external.InneractiveFullscreenUnitController;
import com.fyber.inneractive.sdk.external.InneractiveFullscreenVideoContentController;
import com.fyber.inneractive.sdk.external.InneractiveUnitController;
import com.fyber.inneractive.sdk.external.VideoContentListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.onnuridmc.exelbid.ExelBid;
import com.onnuridmc.exelbid.ExelBidInterstitial;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnInterstitialAdListener;
import com.onnuridmc.exelbid.common.OnMediationOrderResultListener;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationOrderResult;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationType;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

public class SampleInterstitialMediation extends SampleBase implements View.OnClickListener {

    private Button loadBtn;
    private Button showBtn;
    EditText mEdtAdUnit;

    // Exelbid
    private ExelBidInterstitial exelbidInterstitial;
    private MediationOrderResult mediationOrderResult;
    private MediationType currentMediationType;

    // AdMob
    private InterstitialAd adMobInterstitialAd;
    private InterstitialAdLoadCallback adMobAdListener;
    private FullScreenContentCallback admobFullScreenCallback;

    //FaceBook
    private com.facebook.ads.InterstitialAd fanInterstitialAd;
    private com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig fanLoadAdConfig;

    // DigitalTurbine
    private InneractiveAdSpot dtAdSpot;
    private InneractiveAdRequest dtAdRequest;
    private InneractiveFullscreenUnitController dtAdController;

    // Pangle
    private PAGInterstitialAd pagAd;
    private PAGInterstitialRequest pagRequest;
    private PAGInterstitialAdLoadListener pagAdListener;
    private PAGInterstitialAdInteractionListener pagInteractListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediation_interstitial);

        TextView titleTv = findViewById(R.id.tv);
        titleTv.setText(TAG);
        loadBtn = findViewById(R.id.load_btn);
        showBtn = findViewById(R.id.show_btn);
        loadBtn.setOnClickListener(this);
        showBtn.setOnClickListener(this);
        showBtn.setEnabled(false);

        mEdtAdUnit = findViewById(R.id.unit_id);
        mEdtAdUnit.setText(PrefManager.getPref(this, PrefManager.KEY_INTERSTIAL_AD, UNIT_ID_EXELBID_INTERSTITTIAL));

        logText = findViewById(R.id.log_txt);
        logText.setMovementMethod(new ScrollingMovementMethod());
        logsb = new StringBuffer("------------- LOG -------------\n");

        /********************************************************************************
         * Exelbid 설정
         *******************************************************************************/
        exelbidInterstitial = new ExelBidInterstitial(this, mEdtAdUnit.getText().toString());
        exelbidInterstitial.setInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialLoaded() {
                printLog("Exelbid","Loaded");
                // Exelbid는 여기서 노출
                exelbidInterstitial.show();
            }

            @Override
            public void onInterstitialShow() {
            }

            @Override
            public void onInterstitialDismiss() {
            }

            @Override
            public void onInterstitialClicked() {
            }

            @Override
            public void onInterstitialFailed(ExelBidError errorCode, int statusCode) {
                printLog("Exelbid","Fail : " + errorCode.getErrorMessage());
                loadMediation();
            }
        });

        /********************************************************************************
         * AdMob 설정
         *******************************************************************************/
        /********************************************************************************
         * AdMob 설정
         *******************************************************************************/
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                printLog("ADMOB","onInitializationComplete initializationStatus : " + initializationStatus.toString());
            }
        });

        adMobAdListener = new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                printLog("ADMOB","onAdFailedToLoad : " + loadAdError.toString());
                adMobInterstitialAd = null;
                loadMediation();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                adMobInterstitialAd = interstitialAd;
                if (adMobInterstitialAd == null) {
                    printLog("ADMOB","onAdLoaded - Ad did not load");
                    loadMediation();
                } else {
                    printLog("ADMOB","onAdLoaded");
                    showBtn.setEnabled(true);
                }
            }
        };

        admobFullScreenCallback = new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                printLog("ADMOB", "onAdClicked");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                printLog("ADMOB", "onAdDismissedFullScreenContent");
                adMobInterstitialAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                printLog("ADMOB", "onAdFailedToShowFullScreenContent");
                adMobInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                printLog("ADMOB", "onAdImpression");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                printLog("ADMOB", "onAdShowedFullScreenContent");
            }
        };

        /********************************************************************************
         * FAN 설정
         *******************************************************************************/
        fanInterstitialAd = new com.facebook.ads.InterstitialAd(this, UNIT_ID_FAN_INTERSTITTIAL);
        fanLoadAdConfig = fanInterstitialAd
                .buildLoadAdConfig()
                .withAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        fanInterstitialAd.destroy();
                        fanInterstitialAd = null;
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        printLog("FAN","Fail : " + adError.getErrorMessage());
                        loadMediation();
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        if (fanInterstitialAd == null
                                || ad != fanInterstitialAd
                                || !fanInterstitialAd.isAdLoaded()
                                || fanInterstitialAd.isAdInvalidated()) {
                            // Ad not ready to show.
                            printLog("FAN","Fail - Ad not loaded. Click load to request an ad.");
                            loadMediation();
                        } else {
                            // Ad was loaded, show it!
                            printLog("FAN","Loaded");
                            showBtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                    }
                })
                .withCacheFlags(EnumSet.of(CacheFlag.VIDEO))
                .build();

        /********************************************************************************
         * DigitalTurbine 설정
         *******************************************************************************/
        InneractiveAdManager.initialize(getApplicationContext(), APP_ID_DT);
        InneractiveAdManager.setUserId("userId");
        InneractiveAdManager.setMuteVideo(true);

        dtAdRequest = new InneractiveAdRequest(UNIT_ID_DT_INTERSTITTIAL);
        dtAdSpot = InneractiveAdSpotManager.get().createSpot();
        dtAdSpot.setRequestListener(new InneractiveAdSpot.RequestListener() {
            @Override
            public void onInneractiveSuccessfulAdRequest(InneractiveAdSpot inneractiveAdSpot) {
                printLog("DT",": onSuccessfulAdRequest ");
                showBtn.setEnabled(true);
            }
            @Override
            public void onInneractiveFailedAdRequest(InneractiveAdSpot inneractiveAdSpot, InneractiveErrorCode inneractiveErrorCode) {
                printLog("DT","onFailedAdRequest : " + inneractiveErrorCode.toString());
                showBtn.setEnabled(false);
                loadMediation();
            }
        });

        // 컨트롤러 설정
        dtAdController = new InneractiveFullscreenUnitController();
        dtAdSpot.addUnitController(dtAdController);
        InneractiveFullscreenVideoContentController videoController = new InneractiveFullscreenVideoContentController();
        dtAdController.addContentController(videoController);
        dtAdController.setEventsListener(new InneractiveFullscreenAdEventsListener() {
            @Override
            public void onAdImpression(InneractiveAdSpot inneractiveAdSpot) {
                printLog("DT","onAdImpression");
            }

            @Override
            public void onAdClicked(InneractiveAdSpot inneractiveAdSpot) {
                printLog("DT","onAdClicked");
            }

            @Override
            public void onAdWillOpenExternalApp(InneractiveAdSpot inneractiveAdSpot) {

            }

            @Override
            public void onAdEnteredErrorState(InneractiveAdSpot inneractiveAdSpot, InneractiveUnitController.AdDisplayError adDisplayError) {

            }

            @Override
            public void onAdWillCloseInternalBrowser(InneractiveAdSpot inneractiveAdSpot) {

            }

            @Override
            public void onAdDismissed(InneractiveAdSpot inneractiveAdSpot) {
                printLog("DT","onAdDismissed");
            }
        });
        videoController.setEventsListener(new VideoContentListener() {
            @Override
            public void onProgress(int i, int i1) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onPlayerError() {

            }
        });

        /********************************************************************************
         * Pangle 설정
         *******************************************************************************/
        PAGConfig pagInitConfig = new PAGConfig.Builder()
                .appId(APP_ID_PANGLE)
                .debugLog(true)
                .build();

        PAGSdk.init(this, pagInitConfig, new PAGSdk.PAGInitCallback() {
            @Override
            public void success() {
                //load pangle ads after this method is triggered.
                printLog("PANGLE", "pangle init success: ");
            }

            @Override
            public void fail(int code, String msg) {
                printLog("PANGLE", "pangle init fail: " + code);
            }
        });

        pagRequest = new PAGInterstitialRequest();
        pagAd = new PAGInterstitialAd() {
            @Override
            public void setAdInteractionListener(PAGInterstitialAdInteractionListener pagInterstitialAdInteractionListener) {

            }

            @Override
            public void setAdInteractionCallback(PAGInterstitialAdInteractionCallback pagInterstitialAdInteractionCallback) {

            }

            @Override
            public void show(@Nullable Activity activity) {

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

        pagAdListener = new PAGInterstitialAdLoadListener() {
            @Override
            public void onError(int i, String s) {
                printLog("PANGLE","onError");
                showBtn.setEnabled(false);
                loadMediation();
            }

            @Override
            public void onAdLoaded(PAGInterstitialAd pagInterstitialAd) {
                printLog("PANGLE","onAdLoaded");
                if (pagInterstitialAd != null) {
                    pagAd = pagInterstitialAd;
                    pagAd.setAdInteractionListener(pagInteractListener);
                    showBtn.setEnabled(true);
                }
            }
        };

        pagInteractListener = new PAGInterstitialAdInteractionListener() {
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
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.load_btn) {
            showBtn.setEnabled(false);
            mediationOrderResult = null;

            ArrayList<MediationType> mediationUseList = new ArrayList(Arrays.asList(MediationType.EXELBID, MediationType.ADMOB, MediationType.FAN, MediationType.DT, MediationType.PANGLE));
            ExelBid.getMediationData(SampleInterstitialMediation.this, mEdtAdUnit.getText().toString(), mediationUseList,
                    new OnMediationOrderResultListener() {
                        @Override
                        public void onMediationOrderResult(MediationOrderResult mediationOrderResult) {
                            printLog("Mediation","onMediationOrderResult");
                            if(mediationOrderResult != null && mediationOrderResult.getSize() > 0) {
                                SampleInterstitialMediation.this.mediationOrderResult = mediationOrderResult;
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

        currentMediationType = mediationOrderResult.poll();
        if(currentMediationType != null) {
            if (currentMediationType.equals(MediationType.EXELBID)) {
                exelbidInterstitial.load();
                printLog("Exelbid","Request...");
            } else if (currentMediationType.equals(MediationType.ADMOB)) {
                AdRequest adRequest = new AdRequest.Builder().build();
                adMobInterstitialAd.load(SampleInterstitialMediation.this, UNIT_ID_ADMOB_INTERSTITTIAL, adRequest, adMobAdListener);
                printLog("ADMOB","Request...");
            } else if (currentMediationType.equals(MediationType.FAN)) {
                fanInterstitialAd.loadAd(fanLoadAdConfig);
                printLog("FAN","Request...");
            } else if (currentMediationType.equals(MediationType.DT)) {
                dtAdSpot.requestAd(dtAdRequest);
                printLog("DT","Request...");
            }  else if (currentMediationType.equals(MediationType.PANGLE)) {
                pagAd.loadAd(UNIT_ID_PANGLE_INTERSTITTIAL, pagRequest, pagAdListener);
                printLog("PANGLE","Request...");
            }
        }
    }

    private void showMediation() {
        if(currentMediationType != null) {
            if (currentMediationType.equals(MediationType.ADMOB)) {
                if (adMobInterstitialAd != null) {
                    adMobInterstitialAd.setFullScreenContentCallback(admobFullScreenCallback);
                    printLog("ADMOB","Show");
                    adMobInterstitialAd.show(SampleInterstitialMediation.this);
                }
            } else if (currentMediationType.equals(MediationType.FAN)) {
                printLog("FAN","Show");
                if(fanInterstitialAd != null) {
                    fanInterstitialAd.show();
                }
            } else if (currentMediationType.equals(MediationType.DT)) {
                printLog("DT","Show");
                if (dtAdSpot != null && dtAdSpot.isReady()) {
                    dtAdController.show(this);
                }
            }  else if (currentMediationType.equals(MediationType.PANGLE)) {
                printLog("pangle","Show");
                if(pagAd != null) {
                    pagAd.show(this);
                }
            }
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if(exelbidInterstitial != null) {
            exelbidInterstitial.destroy();
        }
        if(adMobInterstitialAd != null) {
            adMobInterstitialAd = null;
        }
        if(dtAdSpot != null) {
            dtAdSpot.destroy();
            dtAdSpot = null;
        }
        if(pagAd != null) {
            pagAd = null;
        }
        super.onDestroy();
    }
}