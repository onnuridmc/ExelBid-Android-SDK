package com.onnuridmc.sample.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaViewListener;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
import java.util.List;

public class SampleNativeMediation extends SampleBase implements View.OnClickListener {

    private Button loadBtn;
    private Button showBtn;
    EditText mEdtAdUnit;

    // Exelbid
    private MediationOrderResult mediationOrderResult;
    private MediationType currentMediationType;

    private View exelbidNativeLayout;
    private ExelBidNative exelBidNative;

    // AdMob
    private UnifiedNativeAd adMobNativeAd;
    FrameLayout adMobFrameLayout;
    AdLoader adMobAdLoader;

    //FaceBook
    private @Nullable
    NativeAdLayout fanNativeAdLayout;
    private @Nullable
    com.facebook.ads.NativeAd fanNativeAd;
    NativeAdListener fanNativeAdListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediation_native);

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
        // 네이티브 요청 객체를 생성한다.
        exelBidNative = new ExelBidNative(this, mEdtAdUnit.getText().toString(), new OnAdNativeListener() {
            @Override
            public void onFailed(ExelBidError error) {
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

        exelbidNativeLayout = findViewById(R.id.native_layout);
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


        /********************************************************************************
         * AdMob 설정
         *******************************************************************************/
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                printLog("ADMOB", "onInitializationComplete initializationStatus : " + initializationStatus.toString());
            }
        });
        setAdMob();


        /********************************************************************************
         * Fan 설정
         *******************************************************************************/
        fanNativeAdLayout = findViewById(R.id.native_ad_container);
        setFan();
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
            ExelBid.getMediationData(SampleNativeMediation.this, mEdtAdUnit.getText().toString(), mediationUseList,
                    new OnMediationOrderResultListener() {
                        @Override
                        public void onMediationOrderResult(MediationOrderResult mediationOrderResult) {
                            printLog("Mediation","onMediationOrderResult");
                            if(mediationOrderResult != null && mediationOrderResult.getSize() > 0) {
                                SampleNativeMediation.this.mediationOrderResult = mediationOrderResult;
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
                mediationOrderResult = null;
                exelBidNative.loadAd();
            } else if (currentMediationType.equals(MediationType.ADMOB)) {
                adMobAdLoader.loadAd(new AdRequest.Builder().build());
                printLog("ADMOB","Request...");
            } else if (currentMediationType.equals(MediationType.FAN)) {
                fanNativeAd = new com.facebook.ads.NativeAd(this, UNIT_ID_FAN_NATIVE);
                fanNativeAd.loadAd(fanNativeAd.buildLoadAdConfig().withAdListener(fanNativeAdListener).build());
                printLog("FAN","Request...");
            }
        }
    }

    private void showMediation() {
        if(currentMediationType != null) {
            if (currentMediationType.equals(MediationType.ADMOB)) {
                if(adMobFrameLayout != null) {
                    adMobFrameLayout.setVisibility(View.VISIBLE);
                    printLog("ADMOB","Show");
                }
                if(fanNativeAdLayout != null) {
                    fanNativeAdLayout.setVisibility(View.GONE);
                }
            } else if (currentMediationType.equals(MediationType.FAN)) {
                if(adMobFrameLayout != null) {
                    adMobFrameLayout.setVisibility(View.GONE);
                }
                if(fanNativeAdLayout != null) {
                    fanNativeAdLayout.setVisibility(View.VISIBLE);
                    printLog("FAN","Show");
                }
            }
        }
    }

    private void hideAllView() {
        if(exelbidNativeLayout != null) {
            exelbidNativeLayout.setVisibility(View.GONE);
        }
        if(adMobFrameLayout != null) {
            adMobFrameLayout.setVisibility(View.GONE);
        }
        if(fanNativeAdLayout != null) {
            fanNativeAdLayout.setVisibility(View.GONE);
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
        if (adMobNativeAd != null) {
            adMobNativeAd.destroy();
        }
        super.onDestroy();
    }

    /**
     * Admob 설정
     */
    private void setAdMob() {

        adMobFrameLayout = findViewById(R.id.admob_container);
        AdLoader.Builder builder = new AdLoader.Builder(this, UNIT_ID_ADMOB_NATIVE);
        builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = isDestroyed();
                        }
                        if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                            unifiedNativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (adMobNativeAd != null) {
                            adMobNativeAd.destroy();
                        }
                        adMobNativeAd = unifiedNativeAd;
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.admob_native_item, null);
                        // Set the media view.
                        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

                        // Set other ad assets.
                        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
                        adView.setBodyView(adView.findViewById(R.id.ad_body));
                        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
                        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
                        adView.setPriceView(adView.findViewById(R.id.ad_price));
                        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
                        adView.setStoreView(adView.findViewById(R.id.ad_store));
                        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

                        ((TextView) adView.getHeadlineView()).setText(adMobNativeAd.getHeadline());
                        adView.getMediaView().setMediaContent(adMobNativeAd.getMediaContent());
                        if (adMobNativeAd.getBody() == null) {
                            adView.getBodyView().setVisibility(View.INVISIBLE);
                        } else {
                            adView.getBodyView().setVisibility(View.VISIBLE);
                            ((TextView) adView.getBodyView()).setText(adMobNativeAd.getBody());
                        }
                        if (adMobNativeAd.getCallToAction() == null) {
                            adView.getCallToActionView().setVisibility(View.INVISIBLE);
                        } else {
                            adView.getCallToActionView().setVisibility(View.VISIBLE);
                            ((Button) adView.getCallToActionView()).setText(adMobNativeAd.getCallToAction());
                        }
                        if (adMobNativeAd.getIcon() == null) {
                            adView.getIconView().setVisibility(View.GONE);
                        } else {
                            ((ImageView) adView.getIconView()).setImageDrawable(
                                    adMobNativeAd.getIcon().getDrawable());
                            adView.getIconView().setVisibility(View.VISIBLE);
                        }
                        if (adMobNativeAd.getPrice() == null) {
                            adView.getPriceView().setVisibility(View.INVISIBLE);
                        } else {
                            adView.getPriceView().setVisibility(View.VISIBLE);
                            ((TextView) adView.getPriceView()).setText(adMobNativeAd.getPrice());
                        }
                        if (adMobNativeAd.getStore() == null) {
                            adView.getStoreView().setVisibility(View.INVISIBLE);
                        } else {
                            adView.getStoreView().setVisibility(View.VISIBLE);
                            ((TextView) adView.getStoreView()).setText(adMobNativeAd.getStore());
                        }
                        if (adMobNativeAd.getStarRating() == null) {
                            adView.getStarRatingView().setVisibility(View.INVISIBLE);
                        } else {
                            ((RatingBar) adView.getStarRatingView())
                                    .setRating(adMobNativeAd.getStarRating().floatValue());
                            adView.getStarRatingView().setVisibility(View.VISIBLE);
                        }
                        if (adMobNativeAd.getAdvertiser() == null) {
                            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
                        } else {
                            ((TextView) adView.getAdvertiserView()).setText(adMobNativeAd.getAdvertiser());
                            adView.getAdvertiserView().setVisibility(View.VISIBLE);
                        }
                        adView.setNativeAd(adMobNativeAd);
                        VideoController vc = adMobNativeAd.getVideoController();
                        if (vc.hasVideoContent()) {
                            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                                @Override
                                public void onVideoEnd() {
                                    super.onVideoEnd();
                                }
                            });
                        } else {
                        }
                        adMobFrameLayout.removeAllViews();
                        adMobFrameLayout.addView(adView);
                        printLog("ADMOB","Load");
                        showBtn.setEnabled(true);
                    }
                });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);

//        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))build());
        adMobAdLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                String error = String.format("ADMOB Fail : domain: %s, code: %d, message: %s",
                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                Log.e(TAG, error);
                adMobFrameLayout.setVisibility(View.GONE);
                loadMediation();
            }
        }).build();
    }


    /**
     * FaceBook 설정
     */
    private void setFan() {

        fanNativeAd = new com.facebook.ads.NativeAd(this, UNIT_ID_FAN_NATIVE);
        fanNativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                printLog("FAN","Fail : " + adError.getErrorMessage());
                fanNativeAdLayout.setVisibility(View.GONE);
                loadMediation();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                printLog("FAN","Load");
                if (fanNativeAd == null || fanNativeAd != ad) {
                    // Race condition, load() called again before last ad was displayed
                    printLog("FAN","onAdLoaded Error");
                    loadMediation();
                    return;
                }

                if (fanNativeAdLayout == null) {
                    printLog("FAN","onAdLoaded Error");
                    loadMediation();
                    return;
                }

                // Unregister last ad
                fanNativeAd.unregisterView();

                if (!fanNativeAd.isAdLoaded() || fanNativeAd.isAdInvalidated()) {
                    printLog("FAN","Ad is not loaded or invalidated.");
                    loadMediation();
                    return;
                }

                LinearLayout adChoicesContainer = fanNativeAdLayout.findViewById(R.id.ad_choices_container);
                if (adChoicesContainer != null) {
                    AdOptionsView adOptionsView = new AdOptionsView(SampleNativeMediation.this, fanNativeAd, fanNativeAdLayout);
                    adChoicesContainer.removeAllViews();
                    adChoicesContainer.addView(adOptionsView, 0);
                }

                Log.d(TAG, "Aspect ratio of ad: " + fanNativeAd.getAspectRatio());

                // Create native UI using the ad metadata.
                com.facebook.ads.MediaView nativeAdIcon = fanNativeAdLayout.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = fanNativeAdLayout.findViewById(R.id.native_ad_title);
                TextView nativeAdBody = fanNativeAdLayout.findViewById(R.id.native_ad_body);
                TextView sponsoredLabel = fanNativeAdLayout.findViewById(R.id.native_ad_sponsored_label);
                TextView nativeAdSocialContext = fanNativeAdLayout.findViewById(R.id.native_ad_social_context);
                Button nativeAdCallToAction = fanNativeAdLayout.findViewById(R.id.native_ad_call_to_action);

                com.facebook.ads.MediaView nativeAdMedia = fanNativeAdLayout.findViewById(R.id.native_ad_media);
                nativeAdMedia.setListener(new MediaViewListener() {
                    @Override
                    public void onVolumeChange(com.facebook.ads.MediaView mediaView, float volume) {
                        Log.i(TAG, "FAN MediaViewEvent: Volume " + volume);
                    }

                    @Override
                    public void onPause(com.facebook.ads.MediaView mediaView) {
                        Log.i(TAG, "FAN MediaViewEvent: Paused");
                    }

                    @Override
                    public void onPlay(com.facebook.ads.MediaView mediaView) {
                        Log.i(TAG, "FAN MediaViewEvent: Play");
                    }

                    @Override
                    public void onFullscreenBackground(com.facebook.ads.MediaView mediaView) {
                        Log.i(TAG, "FAN MediaViewEvent: FullscreenBackground");
                    }

                    @Override
                    public void onFullscreenForeground(com.facebook.ads.MediaView mediaView) {
                        Log.i(TAG, "FAN MediaViewEvent: FullscreenForeground");
                    }

                    @Override
                    public void onExitFullscreen(com.facebook.ads.MediaView mediaView) {
                        Log.i(TAG, "FAN MediaViewEvent: ExitFullscreen");
                    }

                    @Override
                    public void onEnterFullscreen(com.facebook.ads.MediaView mediaView) {
                        Log.i(TAG, "FAN MediaViewEvent: EnterFullscreen");
                    }

                    @Override
                    public void onComplete(com.facebook.ads.MediaView mediaView) {
                        Log.i(TAG, "FAN MediaViewEvent: Completed");
                    }
                });

                // Setting the Text
                nativeAdSocialContext.setText(fanNativeAd.getAdSocialContext());
                nativeAdCallToAction.setText(fanNativeAd.getAdCallToAction());
                nativeAdCallToAction.setVisibility(fanNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                nativeAdTitle.setText(fanNativeAd.getAdvertiserName());
                nativeAdBody.setText(fanNativeAd.getAdBodyText());
                sponsoredLabel.setText("Sponsored");

                // You can use the following to specify the clickable areas.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdIcon);
                clickableViews.add(nativeAdMedia);
                clickableViews.add(nativeAdCallToAction);
                fanNativeAd.registerViewForInteraction(fanNativeAdLayout, nativeAdMedia, nativeAdIcon, clickableViews);

                // Optional: tag views
                NativeAdBase.NativeComponentTag.tagView(nativeAdIcon, NativeAdBase.NativeComponentTag.AD_ICON);
                NativeAdBase.NativeComponentTag.tagView(nativeAdTitle, NativeAdBase.NativeComponentTag.AD_TITLE);
                NativeAdBase.NativeComponentTag.tagView(nativeAdBody, NativeAdBase.NativeComponentTag.AD_BODY);
                NativeAdBase.NativeComponentTag.tagView(nativeAdSocialContext, NativeAdBase.NativeComponentTag.AD_SOCIAL_CONTEXT);
                NativeAdBase.NativeComponentTag.tagView(nativeAdCallToAction, NativeAdBase.NativeComponentTag.AD_CALL_TO_ACTION);

                fanNativeAd.setOnTouchListener(
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    int i = view.getId();
                                    if (i == R.id.native_ad_call_to_action) {
                                        Log.d(TAG, "Call to action button clicked");
                                    } else if (i == R.id.native_ad_media) {
                                        Log.d(TAG, "Main image clicked");
                                    } else {
                                        Log.d(TAG, "Other ad component clicked");
                                    }
                                }
                                return false;
                            }
                        });
                printLog("FAN","onAdLoaded");
                showBtn.setEnabled(true);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
    }
}