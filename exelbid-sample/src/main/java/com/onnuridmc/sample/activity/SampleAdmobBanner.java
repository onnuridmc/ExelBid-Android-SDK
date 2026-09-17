package com.onnuridmc.sample.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.onnuridmc.sample.AppConstants;

/**
 * AdMob 배너 미디에이션 검증 화면.
 *
 * <p>{@link #loadAd(String)} 안의 코드는 미디에이션과 무관한 <b>표준 AdMob 배너 연동 코드</b>다.
 * 유닛에 미디에이션 그룹이 설정되어 있으면 loadAd() 안에서 워터폴이 돌고,
 * ExelBid이 낙찰되더라도 앱이 받는 콜백은 완전히 동일하다.
 */
public class SampleAdmobBanner extends SampleAdmobMediationBase {

    @Nullable
    private AdView mAdView;

    @Override
    protected String getScreenTitle() {
        return "AdMob 배너 미디에이션";
    }

    @Override
    protected String getMediationUnitId() {
        return AppConstants.UNIT_ID_ADMOB_BANNER;
    }

    @Override
    protected String getTestUnitId() {
        return AppConstants.UNIT_ID_ADMOB_BANNER_TEST;
    }

    @Override
    protected void loadAd(@NonNull String adUnitId) {
        // AdView는 광고 유닛 ID를 한 번만 설정할 수 있다. 재요청 때마다 새로 만든다.
        destroyAdView();

        // [AdMob 기본 연동 1] 배너 뷰 생성
        mAdView = new AdView(this);

        // [AdMob 기본 연동 2] AdMob 광고 유닛 ID 설정
        // 미디에이션 동작 여부는 코드가 아니라 이 유닛의 대시보드 설정이 결정한다.
        mAdView.setAdUnitId(adUnitId);

        // [AdMob 기본 연동 3] 배너 크기 설정 (BANNER = 320x50)
        mAdView.setAdSize(AdSize.BANNER);

        // [AdMob 기본 연동 4] 이벤트 리스너 등록
        // ExelBid이 낙찰된 경우에도 로드/실패/노출/클릭 콜백은 모두 이 리스너로 들어온다.
        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                setStatus("배너 로드 성공");
                showWaterfall(mAdView == null ? null : mAdView.getResponseInfo());
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                setStatus("배너 로드 실패 (" + adError.getCode() + ") " + adError.getMessage());
                // 실패해도 워터폴 기록은 남는다. 어느 어댑터까지 시도했는지 확인할 수 있다.
                showWaterfall(adError.getResponseInfo());
            }

            @Override
            public void onAdImpression() {
                appendDiagnostics("[노출] AdMob이 배너 노출을 집계했습니다.");
            }

            @Override
            public void onAdClicked() {
                appendDiagnostics("[클릭] AdMob이 배너 클릭을 집계했습니다.");
            }
        });

        // [AdMob 기본 연동 5] 배너 뷰를 화면 계층에 추가
        mAdContainer.addView(mAdView);

        // [AdMob 기본 연동 6] 광고 요청
        // 미디에이션 유닛이면 이 호출 안에서 워터폴이 진행되고,
        // ExelBid 순서가 오면 GMA가 ExelBidCustomEvent를 리플렉션으로 호출한다.
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    private void destroyAdView() {
        if (mAdView != null) {
            mAdContainer.removeView(mAdView);
            mAdView.destroy();
            mAdView = null;
        }
    }

    // [AdMob 기본 연동 7] 배너 생명주기 전달
    // AdView는 Activity 생명주기를 직접 알 수 없으므로 resume/pause/destroy를 전달해야 한다.
    // 갱신 주기 관리와 리소스 해제에 필요하며, 미디에이션 여부와 무관한 표준 절차다.

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        destroyAdView();
        super.onDestroy();
    }
}
