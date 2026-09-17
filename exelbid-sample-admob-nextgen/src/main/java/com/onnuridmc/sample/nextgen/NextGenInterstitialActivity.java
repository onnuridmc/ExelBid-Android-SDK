package com.onnuridmc.sample.nextgen;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest;
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd;
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback;

/**
 * Next-Gen SDK 전면 미디에이션 검증.
 */
public class NextGenInterstitialActivity extends NextGenMediationBaseActivity {

    @Nullable
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtnShow.setVisibility(View.VISIBLE);
        mBtnShow.setOnClickListener(v -> showInterstitial());
    }

    @Override
    protected String getScreenTitle() {
        return "NextGen 전면 미디에이션";
    }

    @Override
    protected String getUnitId() {
        return UNIT_ID_INTERSTITIAL;
    }

    @Override
    protected void loadAd() {
        destroyAd();
        runOnUiThread(() -> mBtnShow.setEnabled(false));

        InterstitialAd.load(new AdRequest.Builder(getUnitId()).build(),
                new AdLoadCallback<InterstitialAd>() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        setStatus("전면 로드 성공 — '광고 표시'를 누르세요");
                        showWaterfall(interstitialAd.getResponseInfo());
                        bindEventCallback(interstitialAd);
                        runOnUiThread(() -> mBtnShow.setEnabled(true));
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        setStatus("전면 로드 실패 - " + adError.getMessage());
                        showWaterfall(adError.getResponseInfo());
                    }
                });
    }

    private void bindEventCallback(@NonNull InterstitialAd interstitialAd) {
        interstitialAd.setAdEventCallback(new InterstitialAdEventCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                appendDiagnostics("[표시] 전면 광고가 화면에 표시되었습니다.");
            }

            @Override
            public void onAdImpression() {
                appendDiagnostics("[노출] Next-Gen SDK가 전면 노출을 집계했습니다.");
            }

            @Override
            public void onAdClicked() {
                appendDiagnostics("[클릭] Next-Gen SDK가 전면 클릭을 집계했습니다.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                appendDiagnostics("[종료] 전면 광고가 닫혔습니다.");
                destroyAd();
                runOnUiThread(() -> mBtnShow.setEnabled(false));
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull FullScreenContentError error) {
                setStatus("전면 표시 실패 - " + error.getMessage());
                destroyAd();
                runOnUiThread(() -> mBtnShow.setEnabled(false));
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd == null) {
            setStatus("표시할 광고가 없습니다. 먼저 로드하세요.");
            return;
        }
        mInterstitialAd.show(this);
    }

    private void destroyAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
            mInterstitialAd = null;
        }
    }

    @Override
    protected void onDestroy() {
        destroyAd();
        super.onDestroy();
    }
}
