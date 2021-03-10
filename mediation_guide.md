# Mediation Guide

Exelbid Android SDK를 이용한 광고 연동시 No Ads(No Fill) 의 경우, 앱에서 연동하고 있는 타사 광고의 최적환 된 호출 순서를 응답한다.
### ※ 아래 내용을 기반으로 한 샘플 프로젝트는 요청시 제공

## ExelBid-Android-SDK 광고 기본 연동
 ExelBid-Android-SDK 연동 가이드를 통한 기본 연동 
    - [ExelBid-Android-SDK 연동 가이드 참조](https://github.com/onnuridmc/ExelBid-Android-SDK)

## 미디에이션 설정
1. 연동된 타사 광고 목록 설정
    연동 되어진 미디에이션(타사 sdk 연동)리스트를 전달한다.
    - Exelbid에서는 SDK에 최적화 연동 가능한 라이브러리를 ***Enum***(***MediationType***)으로 제공한다
    - ***ArrayList*** 형태로 설정한다.

2. 타사 광고 최적화 순서를 받을 리스너 설정 
    * Exelbid에서 ***No Ad*** 시에 ***onAdFail*** 대신  ***OnMediationOrderResultListener***의  ***onMediationOrderResult*** 통해서 타사 광고 최적화 호출 순서 정보 객체(***MediationOrderResult***)를 응답 받는다.
3. 타사 광고 목록과 리스너를 Exelbid 광고 객체에 설정한다.
    * setMediationOrderListener('목록 리스트', '타사 광고 최적화 순서 목록 리스너')
4. 타사 광고 순서 응답시 해당 순서에 따른 타사 광고 호출
    ```java
    // 1. 연동된 타사 광고 목록 설정
    ArrayList<MediationType> mediationUseList =
                new ArrayList(Arrays.asList(MediationType.ADMOB, MediationType.MOPUB, MediationType.FAN));
    // 2. 타사 광고 최적화 순서를 받을 리스너 설정
        OnMediationOrderResultListener onMediationOrderResultListener = new OnMediationOrderResultListener() {
    
            @Override
            public void onMediationOrderResult(MediationOrderResult mediationOrderResult) {
                mMediationOrderResult = mediationOrderResult;
                // 4. 타사 광고 호출 로직 구현
                loadMediation();
            }
        };
        // 3. 타사 광고 목록과 리스너를 Exelbid 광고 객체에 설정한다.
        exelbidAdView.setMediationOrderListener(mediationUseList, onMediationOrderResultListener);
    ```
5. 샘플(SampleBannerMediation.java 배너 기본 예제) 설명
    * ***OnMediationOrderResultListener***를 통해서 응답받은 ***MediationOrderResult***객체는 ***poll()*** 함수 호출시마다 순서대로 MediationType 객체를 반환한다.
    * 타사 SDK의 종류와 형식(배너, 전면, 네이티브) 에 따라서 광고 요청 로직을 적용한다
    * loadMediation
    
        ```java
        // MediationOrderResult가 응답 되었고 각각의 SDK 광고 요청 실패시 호출
        private void loadMediation() {
            if(mMediationOrderResult == null) {
                return;
            }
            MediationType mediationType = mMediationOrderResult.poll();
            currentMediationType = mediationType;
    
            // 타사 SDK의 종류와 형식(배너, 전면, 네이티브) 에 따라서 광고 요청 로직을 적용한다
            if(currentMediationType != null) {
                if (currentMediationType.equals(MediationType.ADMOB)) {
                    admobView.loadAd(new AdRequest.Builder().build());
    
                } else if (currentMediationType.equals(MediationType.MOPUB)) {
                    moPubView.loadAd();
    
                } else if (currentMediationType.equals(MediationType.FAN)) {
                    fanView = new com.facebook.ads.AdView(this, UNIT_ID_FAN_BANNER, AdSize.BANNER_HEIGHT_50);
                    fanContainer.addView(fanView);
                    fanView.loadAd(fanView.buildLoadAdConfig().withAdListener(fanAdListener).build());
                }
            }
        } 
        ```
6. MediationOrderResult
    * ***int getSize()*** - 응답된 타사 광록 목록의 개수를 반환
    * ***MediationType poll()*** - 응답된 타사 광록 목록에서 최우선 순위의 ***MediationType***을 반환 후 목록에서 삭제
    * ***reset()*** - ***OnMediationOrderResultListener***를 통해서 응답 받은 목록 개수와 순서로 ***MediationOrderResult***를 초기화 한다.


### 전면, 네이티브 형태는 위의 배너와 같은 방식으로 처리한다.
### 외에 타사 연동은 각각의 해당 가이드를 참조해 설정한다.
* Mopub - [https://developers.mopub.com/publishers/android/integrate/](https://developers.mopub.com/publishers/android/integrate/)
* AdMob - [https://developers.google.com/admob/android/quick-start?hl=ko](https://developers.google.com/admob/android/quick-start?hl=ko)
* FaceBook - [https://developers.facebook.com/docs/audience-network/guides/ad-formats](https://developers.facebook.com/docs/audience-network/guides/ad-formats)
