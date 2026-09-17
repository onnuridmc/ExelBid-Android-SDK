package com.onnuridmc.sample.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediaContent;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;

/**
 * AdMob 네이티브 미디에이션 검증 화면.
 *
 * <p>ExelBid 어댑터에서 가장 중요하게 확인해야 할 화면이다. 네이티브는 노출·클릭 집계를
 * ExelBid이 직접 수행하므로, 광고가 보이는 것만으로는 정상 동작을 판단할 수 없다.
 * 광고를 노출·클릭한 뒤 ExelBid 리포트에 실제로 집계되는지 확인해야 한다.
 *
 * <p>{@link #loadAd(String)}·{@link #render(NativeAd)} 의 코드는 미디에이션과 무관한
 * <b>표준 AdMob 네이티브 연동 코드</b>다. 네이티브는 완성된 광고 뷰가 아니라
 * 애셋(제목·본문·이미지 등)을 받아 앱이 직접 {@code NativeAdView}에 배치하고,
 * 마지막에 {@code setNativeAd()}를 호출해 노출·클릭 추적을 AdMob에 맡기는 구조다.
 */
public class SampleAdmobNative extends SampleAdmobMediationBase {

    @Nullable
    private NativeAd mNativeAd;

    @Override
    protected String getScreenTitle() {
        return "AdMob 네이티브 미디에이션";
    }

    @Override
    protected String getMediationUnitId() {
        return AppConstants.UNIT_ID_ADMOB_NATIVE;
    }

    @Override
    protected String getTestUnitId() {
        return AppConstants.UNIT_ID_ADMOB_NATIVE_TEST;
    }

    @Override
    protected void loadAd(@NonNull String adUnitId) {
        destroyNativeAd();
        mAdContainer.removeAllViews();

        // [AdMob 기본 연동 1] AdLoader 구성
        // NativeAdOptions 는 기본값을 쓴다. shouldReturnUrlsForImageAssets 가 false 이므로
        // 어댑터가 이미지를 내려받아 Drawable 로 제공해야 한다(매체 앱의 일반적인 설정).
        AdLoader adLoader = new AdLoader.Builder(this, adUnitId)
                // [AdMob 기본 연동 2] 네이티브 광고 수신 콜백
                // 받은 NativeAd 의 파기 책임은 앱에 있다. 새 광고를 받거나 화면을 떠날 때
                // 반드시 destroy() 를 호출해야 한다 (ExelBid 낙찰 시 어댑터 리소스도 함께 해제된다).
                .forNativeAd(nativeAd -> {
                    if (isDestroyed() || isFinishing()) {
                        nativeAd.destroy();
                        return;
                    }
                    destroyNativeAd();
                    mNativeAd = nativeAd;
                    setStatus("네이티브 로드 성공");
                    showWaterfall(nativeAd.getResponseInfo());
                    render(nativeAd);
                })
                // [AdMob 기본 연동 3] 실패/노출/클릭 이벤트 리스너
                .withAdListener(new AdListener() {

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        setStatus("네이티브 로드 실패 (" + adError.getCode() + ") " + adError.getMessage());
                        showWaterfall(adError.getResponseInfo());
                    }

                    @Override
                    public void onAdImpression() {
                        appendDiagnostics("[노출] AdMob이 네이티브 노출을 집계했습니다.\n"
                                + "ExelBid이 낙찰된 경우 ExelBid 리포트에도 노출이 잡혀야 합니다.");
                    }

                    @Override
                    public void onAdClicked() {
                        appendDiagnostics("[클릭] AdMob이 네이티브 클릭을 집계했습니다.\n"
                                + "ExelBid이 낙찰된 경우 ExelBid 리포트에도 클릭이 잡혀야 합니다.");
                    }
                })
                .build();

        // [AdMob 기본 연동 4] 광고 요청
        // 미디에이션 유닛이면 이 호출 안에서 워터폴이 진행되고,
        // ExelBid이 낙찰되면 어댑터가 애셋(제목·본문·이미지)을 채워 forNativeAd 로 전달한다.
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void render(@NonNull NativeAd nativeAd) {
        // [AdMob 기본 연동 5] NativeAdView 준비
        // 네이티브 애셋은 반드시 NativeAdView 계층 안에 배치해야 노출·클릭이 추적된다.
        NativeAdView adView = (NativeAdView) getLayoutInflater()
                .inflate(R.layout.admob_native_item, mAdContainer, false);

        // 미디에이션 네이티브는 MediaContent 가 없을 수 있다.
        // 내용 없는 MediaView 를 등록하면 AdMob 이 렌더링을 완료하지 못해
        // 노출이 집계되지 않을 수 있으므로, 콘텐츠가 있을 때만 등록한다.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        MediaContent mediaContent = nativeAd.getMediaContent();
        if (mediaContent != null) {
            adView.setMediaView(mediaView);
            mediaView.setMediaContent(mediaContent);
        } else {
            mediaView.setVisibility(View.GONE);
            appendDiagnostics("[주의] MediaContent 없음 - MediaView 미등록");
        }

        // [AdMob 기본 연동 6] 애셋 뷰 등록
        // 어떤 뷰가 어떤 애셋인지 AdMob에 알려준다. 등록된 뷰가 클릭 대상이 된다.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // [AdMob 기본 연동 7] 애셋 값 바인딩
        // headline 만 필수이고 나머지는 null 일 수 있으므로 반드시 null 체크 후 채운다.
        // (ExelBid 응답에는 별점·advertiser 가 없어 해당 뷰는 숨겨진다.)
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        setTextOrHide(adView.getBodyView(), nativeAd.getBody());
        setTextOrHide(adView.getAdvertiserView(), nativeAd.getAdvertiser());

        Button cta = (Button) adView.getCallToActionView();
        if (nativeAd.getCallToAction() == null) {
            cta.setVisibility(View.INVISIBLE);
        } else {
            cta.setVisibility(View.VISIBLE);
            cta.setText(nativeAd.getCallToAction());
        }

        ImageView icon = (ImageView) adView.getIconView();
        if (nativeAd.getIcon() == null) {
            icon.setVisibility(View.GONE);
        } else {
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(nativeAd.getIcon().getDrawable());
        }

        RatingBar stars = (RatingBar) adView.getStarRatingView();
        if (nativeAd.getStarRating() == null) {
            stars.setVisibility(View.INVISIBLE);
        } else {
            stars.setVisibility(View.VISIBLE);
            stars.setRating(nativeAd.getStarRating().floatValue());
        }

        // [AdMob 기본 연동 8] 화면에 붙인 뒤 setNativeAd 호출 — 이 호출로 노출·클릭 추적이 시작된다.
        // 뷰를 먼저 계층에 붙인 뒤 setNativeAd 를 호출한다.
        // GMA 는 setNativeAd 시점에 뷰어빌리티 관찰을 시작하므로,
        // 아직 윈도우에 붙지 않은 뷰에 대해 호출하면 노출이 집계되지 않을 수 있다.
        mAdContainer.removeAllViews();
        mAdContainer.addView(adView);

        adView.setNativeAd(nativeAd);
    }

    private static void setTextOrHide(@Nullable View view, @Nullable String text) {
        if (!(view instanceof TextView)) {
            return;
        }
        if (text == null || text.isEmpty()) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
            ((TextView) view).setText(text);
        }
    }

    private void destroyNativeAd() {
        if (mNativeAd != null) {
            mNativeAd.destroy();
            mNativeAd = null;
        }
    }

    @Override
    protected void onDestroy() {
        destroyNativeAd();
        super.onDestroy();
    }
}
