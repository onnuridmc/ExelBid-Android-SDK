package com.onnuridmc.sample.nextgen;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.ads.mobile.sdk.common.Image;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;
import com.google.android.libraries.ads.mobile.sdk.nativead.MediaView;
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd;
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoader;
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback;
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdRequest;
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView;

import java.util.Collections;

/**
 * Next-Gen SDK 네이티브 미디에이션 검증.
 *
 * <p>ExelBid 어댑터에서 가장 중요한 검증 화면. 어댑터가 레거시
 * {@code UnifiedNativeAdMapper}(+{@code formats.NativeAd$Image})로 매핑한 애셋을
 * Next-Gen SDK가 신규 {@code nativead.NativeAd} 로 노출하는지, 노출·클릭이
 * ExelBid 집계까지 이어지는지 확인한다.
 */
public class NextGenNativeActivity extends NextGenMediationBaseActivity {

    @Nullable
    private NativeAd mNativeAd;

    @Override
    protected String getScreenTitle() {
        return "NextGen 네이티브 미디에이션";
    }

    @Override
    protected String getUnitId() {
        return UNIT_ID_NATIVE;
    }

    @Override
    protected void loadAd() {
        destroyAd();
        runOnUiThread(() -> mAdContainer.removeAllViews());

        NativeAdRequest request = new NativeAdRequest.Builder(
                getUnitId(),
                Collections.singletonList(NativeAd.NativeAdType.NATIVE)).build();

        NativeAdLoader.load(request, new NativeAdLoaderCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                if (isDestroyed() || isFinishing()) {
                    nativeAd.destroy();
                    return;
                }
                destroyAd();
                mNativeAd = nativeAd;
                setStatus("네이티브 로드 성공");
                showWaterfall(nativeAd.getResponseInfo());
                bindEventCallback(nativeAd);
                runOnUiThread(() -> render(nativeAd));
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                setStatus("네이티브 로드 실패 - " + adError.getMessage());
                showWaterfall(adError.getResponseInfo());
            }
        });
    }

    private void bindEventCallback(@NonNull NativeAd nativeAd) {
        nativeAd.setAdEventCallback(new NativeAdEventCallback() {
            @Override
            public void onAdImpression() {
                appendDiagnostics("[노출] Next-Gen SDK가 네이티브 노출을 집계했습니다.\n"
                        + "ExelBid 리포트에도 노출이 잡혀야 합니다.");
            }

            @Override
            public void onAdClicked() {
                appendDiagnostics("[클릭] Next-Gen SDK가 네이티브 클릭을 집계했습니다.\n"
                        + "ExelBid 리포트에도 클릭이 잡혀야 합니다.");
            }
        });
    }

    private void render(@NonNull NativeAd nativeAd) {
        NativeAdView adView = (NativeAdView) getLayoutInflater()
                .inflate(R.layout.nextgen_native_item, mAdContainer, false);

        TextView headline = adView.findViewById(R.id.ad_headline);
        TextView body = adView.findViewById(R.id.ad_body);
        Button cta = adView.findViewById(R.id.ad_call_to_action);
        ImageView icon = adView.findViewById(R.id.ad_app_icon);
        MediaView mediaView = adView.findViewById(R.id.ad_media);

        adView.setHeadlineView(headline);
        adView.setBodyView(body);
        adView.setCallToActionView(cta);
        adView.setIconView(icon);

        headline.setText(nativeAd.getHeadline());
        setTextOrHide(body, nativeAd.getBody());
        setTextOrHide(cta, nativeAd.getCallToAction());

        Image iconImage = nativeAd.getIcon();
        if (iconImage == null || iconImage.getDrawable() == null) {
            icon.setVisibility(View.GONE);
            appendDiagnostics("[주의] 아이콘 Drawable 없음");
        } else {
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(iconImage.getDrawable());
        }

        // 뷰를 먼저 계층에 붙인 뒤 registerNativeAd 를 호출한다.
        // (레거시 setNativeAd 와 같은 이유 - 등록 시점에 뷰어빌리티 관찰이 시작된다)
        mAdContainer.removeAllViews();
        mAdContainer.addView(adView);
        adView.registerNativeAd(nativeAd, mediaView);
    }

    private static void setTextOrHide(@NonNull TextView view, @Nullable String text) {
        if (text == null || text.isEmpty()) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        }
    }

    private void destroyAd() {
        if (mNativeAd != null) {
            mNativeAd.destroy();
            mNativeAd = null;
        }
    }

    @Override
    protected void onDestroy() {
        destroyAd();
        super.onDestroy();
    }
}
