package com.onnuridmc.sample.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.onnuridmc.sample.AppConstants;

/**
 * AdMob 전면 미디에이션 검증 화면.
 *
 * <p>{@link #loadAd(String)}·{@link #showInterstitial()} 의 코드는 미디에이션과 무관한
 * <b>표준 AdMob 전면 연동 코드</b>다. 전면 광고는 배너와 달리 뷰가 아니므로
 * 정적 {@code InterstitialAd.load()}로 로드하고, 성공 콜백에서 받은 인스턴스를
 * 보관했다가 원하는 시점에 {@code show()}로 표시한다.
 */
public class SampleAdmobInterstitial extends SampleAdmobMediationBase {

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
        return "AdMob 전면 미디에이션";
    }

    @Override
    protected String getMediationUnitId() {
        return AppConstants.UNIT_ID_ADMOB_INTERSTITIAL;
    }

    @Override
    protected String getTestUnitId() {
        return AppConstants.UNIT_ID_ADMOB_INTERSTITIAL_TEST;
    }

    @Override
    protected void loadAd(@NonNull String adUnitId) {
        mInterstitialAd = null;
        mBtnShow.setEnabled(false);

        // [AdMob 기본 연동 1] 전면 광고 로드
        // 미디에이션 유닛이면 이 호출 안에서 워터폴이 진행된다.
        // ExelBid이 낙찰되더라도 아래 콜백 구조는 완전히 동일하다.
        InterstitialAd.load(this, adUnitId, new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // [AdMob 기본 연동 2] 로드된 인스턴스 보관
                        // 전면 광고는 1회용이다. 표시하거나 실패하면 다시 로드해야 한다.
                        mInterstitialAd = interstitialAd;
                        mBtnShow.setEnabled(true);
                        setStatus("전면 로드 성공 — '광고 표시'를 누르세요");
                        showWaterfall(interstitialAd.getResponseInfo());
                        bindFullScreenCallback(interstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        mInterstitialAd = null;
                        mBtnShow.setEnabled(false);
                        setStatus("전면 로드 실패 (" + adError.getCode() + ") " + adError.getMessage());
                        showWaterfall(adError.getResponseInfo());
                    }
                });
    }

    // [AdMob 기본 연동 3] 전체 화면 이벤트 콜백 등록
    // 표시/노출/클릭/닫힘/표시 실패를 받는다. 닫힘·실패 시 인스턴스를 버리고
    // 다음 표시를 위해 새로 로드하는 것이 표준 패턴이다.
    private void bindFullScreenCallback(@NonNull InterstitialAd interstitialAd) {
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

            @Override
            public void onAdShowedFullScreenContent() {
                appendDiagnostics("[표시] 전면 광고가 화면에 표시되었습니다.");
            }

            @Override
            public void onAdImpression() {
                appendDiagnostics("[노출] AdMob이 전면 노출을 집계했습니다.");
            }

            @Override
            public void onAdClicked() {
                appendDiagnostics("[클릭] AdMob이 전면 클릭을 집계했습니다.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                appendDiagnostics("[종료] 전면 광고가 닫혔습니다.");
                mInterstitialAd = null;
                runOnUiThread(() -> mBtnShow.setEnabled(false));
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                setStatus("전면 표시 실패 (" + adError.getCode() + ") " + adError.getMessage());
                mInterstitialAd = null;
                runOnUiThread(() -> mBtnShow.setEnabled(false));
            }
        });
    }

    // [AdMob 기본 연동 4] 전면 광고 표시
    // 로드 완료 후 앱이 원하는 시점(화면 전환 등)에 호출한다.
    // ExelBid이 낙찰된 경우 이 호출로 ExelBid 전면 광고 화면이 열린다.
    private void showInterstitial() {
        if (mInterstitialAd == null) {
            setStatus("표시할 광고가 없습니다. 먼저 로드하세요.");
            return;
        }
        mInterstitialAd.show(this);
    }
}
