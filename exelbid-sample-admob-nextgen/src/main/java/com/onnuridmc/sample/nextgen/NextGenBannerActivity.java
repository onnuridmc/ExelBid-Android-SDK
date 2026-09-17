package com.onnuridmc.sample.nextgen;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.ads.mobile.sdk.banner.AdSize;
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd;
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest;
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;

/**
 * Next-Gen SDK 배너 미디에이션 검증.
 *
 * <p>레거시와 달리 뷰(AdView)가 아니라 광고 객체({@link BannerAd})를 정적 load 로 받고,
 * {@code getView(activity)} 로 뷰를 꺼내 계층에 붙인다.
 */
public class NextGenBannerActivity extends NextGenMediationBaseActivity {

    @Nullable
    private BannerAd mBannerAd;

    @Override
    protected String getScreenTitle() {
        return "NextGen 배너 미디에이션";
    }

    @Override
    protected String getUnitId() {
        return UNIT_ID_BANNER;
    }

    @Override
    protected void loadAd() {
        destroyAd();

        BannerAdRequest request = new BannerAdRequest.Builder(getUnitId(), AdSize.BANNER).build();
        BannerAd.load(request, new AdLoadCallback<BannerAd>() {
            @Override
            public void onAdLoaded(@NonNull BannerAd bannerAd) {
                mBannerAd = bannerAd;
                setStatus("배너 로드 성공");
                showWaterfall(bannerAd.getResponseInfo());
                bindEventCallback(bannerAd);
                runOnUiThread(() -> {
                    View adView = bannerAd.getView(NextGenBannerActivity.this);
                    mAdContainer.removeAllViews();
                    mAdContainer.addView(adView);
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                setStatus("배너 로드 실패 - " + adError.getMessage());
                showWaterfall(adError.getResponseInfo());
            }
        });
    }

    private void bindEventCallback(@NonNull BannerAd bannerAd) {
        bannerAd.setAdEventCallback(new BannerAdEventCallback() {
            @Override
            public void onAdImpression() {
                appendDiagnostics("[노출] Next-Gen SDK가 배너 노출을 집계했습니다.");
            }

            @Override
            public void onAdClicked() {
                appendDiagnostics("[클릭] Next-Gen SDK가 배너 클릭을 집계했습니다.");
            }
        });
    }

    private void destroyAd() {
        if (mBannerAd != null) {
            mAdContainer.removeAllViews();
            mBannerAd.destroy();
            mBannerAd = null;
        }
    }

    @Override
    protected void onDestroy() {
        destroyAd();
        super.onDestroy();
    }
}
