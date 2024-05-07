# Mediation Guide

Exelbid Android SDK를 이용한 광고 연동시 Mediation 연동의 경우, 각 앱에서 연동하고 있는 광고 SDK들의 최적화 된 호출 순서를 응답한다.(Exelbid 포함)

## 시작하기

1. 계정을 생성합니다
2.  Inventory -> App -> + Create New App을 선택합니다.<br/>
![new app](./img/sdk-1.png)

3. 앱정보를 등록한 후, unit을 생성 합니다. (unit id 발급)
4. 해당 App->unit을 기준으로 미디에이션 설정
    - mediation->설정하기<br/><br/>
5. 어플리케이션 설정 (ExelBid SDK 추가하기)
    * Android Studio
        1. repositories 적용
            ```java
            {
            mavenCentral()
            }
            ```
        2. 모듈의 build.gradle파일에 dependencies에 아래 항목을 추가합니다.
            * SDK 버전은 [기본 가이드](https://github.com/onnuridmc/ExelBid-Android-SDK#exelbid-sdk-%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0)에서 최신 버전 확인 적용합니다.
            ```java
            dependencies {
                    implementation 'com.onnuridmc.exelbid:exelbid:1.9.8'
            }
6. ```ExelBid.getMediationData``` 을 호출하여 최적화 순서 호출

## 미디에이션 설정
1. 연동된 미디에이션(광고 SDK) 목록 설정

    연동된 미디에이션(sdk 연동)리스트를 전달한다.
    - Exelbid에서는 SDK에서 미디에이션 최적화를 위해서 연동 가능한 광고 SDK들을 ***Enum***(***MediationType***)으로 제공한다
    - ***ArrayList*** 형태로 설정한다.
    - 해당 값을 설정하지 않는 경우, 대시보드에 설정된 미디에이션 타입이 디폴트로 적용됩니다.

2. 미디에이션 최적화 순서를 받을 리스너 설정 
    * ***ExelBid.getMediationData*** 에 등록한 ***OnMediationOrderResultListener***의  ***onMediationOrderResult*** 통해서 광고 SDK들의 최적화 호출 순서 정보 객체(***MediationOrderResult***)를 응답 받는다.

3. 연동된 광고 SDK 목록과 리스너를 이용하여 Exelbid 광고 객체에 설정한다.
    * ExelBid.getMediationData('Context', 'Exelbid Mediation unit id', '연동된 광고 SDK 목록, '리스너')

4. 미디에이션 순서 응답시 해당 순서에 따른 광고SDK 호출
5. 응답 결과가 없을시 리스너의 ***onMediationFail***가 호출된다.
    - Error code
        <table>
            <tbody>
                <tr>
                    <td>
                        <code>0</code>
                    </td>
                    <td>
                        정상 응답
                    </td>
                </tr>
                <tr>
                    <td>
                        <code>8020</code>
                    </td>
                    <td>
                        설정된 리스트가 없다.(국가 정보 포함)
                    </td>
                </tr>
                <tr>
                    <td>
                        <code>8010</code>
                    </td>
                    <td>
                        UNIT ID 오류. 빈 값이거나 형식 오류
                    </td>
                </tr>
                <tr>
                    <td>
                        <code>9xxx</code>
                    </td>
                    <td>
                        네트워크 오류, 응답 Json 파싱 오류 등
                    </td>
                </tr>
                <tr>
                    <td>
                        <code>8888</code>
                    </td>
                    <td>
                        기타 에러
                    </td>
                </tr>
            </tbody>
        </table>

    ```java
           // 1. 연동된 미디에이션(광고 SDK) 목록 설정
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
            // 2. 미디에이션 최적화 순서를 받을 리스너 설정 (new OnMediationOrderResultListener)
            // 3. 연동된 광고 SDK 목록과 리스너를 이용하여 Exelbid 광고 객체에 설정한다.
            ExelBid.getMediationData(SampleBannerMediation.this, UNIT_ID_EXELBID_BANNER, mediationUseList
                    , new OnMediationOrderResultListener() {

                        @Override
                        public void onMediationOrderResult(MediationOrderResult mediationOrderResult) {
                            printLog("Mediation","onMediationOrderResult");

                            if(mediationOrderResult != null && mediationOrderResult.getSize() > 0) {
                                SampleBannerMediation.this.mediationOrderResult = mediationOrderResult;
                                // 4. 미디에이션 순서 응답시 해당 순서에 따른 광고 SDK 호출
                                loadMediation();
                            }
                        }

                        @Override
                        public void onMediationFail(int errorCode, String errorMsg) {
                            // 5. 응답 결과가 없을시, 혹은 에러 발생시 호출된다.
                            printLog("Mediation","onMediationFail :: " + errorMsg + "(" + errorCode + ")");
                        }
                    });
    ```
5. 샘플([SampleBannerMediation.java](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleBannerMediation.java) 배너 기본 예제) 설명
    * ***OnMediationOrderResultListener***를 통해서 응답받은 ***MediationOrderResult***객체는 ***poll()*** 함수 호출 시마다 순서대로 ```Pair<MediationType, String>``` 객체를 반환한다.
    * 광고 SDK의 종류와 형식(배너, 전면, 네이티브) 에 따라서 광고 요청 로직을 적용한다
    * loadMediation
    
        ```java
        // MediationOrderResult가 응답 되었고 각각의 SDK 광고 요청 실패시 호출
        private void loadMediation() {
            if(mMediationOrderResult == null) {
                return;
            }
            Pair<MediationType, String> currentMediationPair = mediationOrderResult.poll();
            if(currentMediationPair == null) {
                return;
            }
            currentMediationType = currentMediationPair.first;
            currentMediationUnitId = currentMediationPair.second;
    
            // 광고 SDK의 종류와 형식(배너, 전면, 네이티브) 에 따라서 광고 요청 로직을 적용한다
             if (currentMediationType.equals(MediationType.EXELBID)) {
                exelbidAdView.setAdUnitId(currentMediationUnitId);
                exelbidAdView.loadAd();
            } else if (currentMediationType.equals(MediationType.ADMOB)) {
                if(admobView.getAdUnitId() == null){
                    admobView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
                    admobView.setAdUnitId(currentMediationUnitId);
                }
                admobView.loadAd(new AdRequest.Builder().build());
            } else if (currentMediationType.equals(MediationType.FAN)) {
                if(fanView == null || !fanView.getPlacementId().equals(currentMediationUnitId)) {
                    fanView = new com.facebook.ads.AdView(this, currentMediationUnitId, AdSize.BANNER_HEIGHT_50);
                    fanAdView.addView(fanView);
                }
                fanView.loadAd(fanView.buildLoadAdConfig().withAdListener(fanAdListener).build());
            } else if (currentMediationType.equals(MediationType.ADFIT)) {
                adfitAdView.setClientId(currentMediationUnitId);
                adfitAdView.loadAd();
            } else if (currentMediationType.equals(MediationType.DT)) {
                if (dtAdSpot.isReady()) {
                    dtAdController.unbindView(dtView);
                }
                dtAdSpot.requestAd(new InneractiveAdRequest(currentMediationUnitId));
            } else if (currentMediationType.equals(MediationType.PANGLE)) {
                if(pagAd != null) {
                    pangleView.removeView(pagAd.getBannerView());
                }
                pagAd.loadAd(currentMediationUnitId, pagRequest, pagAdListener);
            } else if (currentMediationType.equals(MediationType.APPLOVIN)) {
                if(maxAdView == null || !maxAdView.getAdUnitId().equals(currentMediationUnitId)) {
                    maxAdView = new MaxAdView(currentMediationUnitId, this);
                    maxAdView.setListener(maxAdListener);
                    adContainer.addView(maxAdView);
                }
                maxAdView.loadAd();
            } else if (currentMediationType.equals(MediationType.TNK)) {
                if(tnkAdView == null || !tnkAdView.getPlacementId().equals(currentMediationUnitId)) {
                    tnkAdView = new com.tnkfactory.ad.BannerAdView(this, currentMediationUnitId);
                    tnkAdView.setListener(tnkAdListener);
                    adContainer.addView(tnkAdView);
                }
                tnkAdView.load();
            }
        }
        ```
6. MediationOrderResult
    * ***int getSize()*** - 응답된 미디에이션 광고 목록의 개수를 반환
    * ***Pair<MediationType, String> poll()*** - 응답된 미디에이션 광고 목록에서 최우선 순위의 미디에이션 데이터 ```Pair<MediationType, String>```을 반환 후 목록에서 삭제
      - ***MediationType*** : 현재 순서의 미디에이션 타입
      - ***String*** : 현재 순서의 광고 유닛 아이디 (미디에이션 타입에 해당하는 광고 SDK의 지면 아이디)
    * ***reset()*** - ```OnMediationOrderResultListener```를 통해서 응답 받은 목록 개수와 순서로 ```MediationOrderResult```를 초기화


### 위의 배너 샘플과 같은 방식으로 광고 타입 별 샘플을 참고하여 적용합니다.
- 배너 광고 미디에이션 샘플 : [SampleBannerMediation.java](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleBannerMediation.java)
- 전면 광고 미디에이션 샘플 : [SampleInterstitialMediation.java](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleInterstitialMediation.java)

    - 전면 비디오 광고 : 전면 광고와 동일하게 처리합니다. 전면 광고 미디에이션 샘플을 참고해주세요. 광고 SDK 별 비디오 설정은 각 SDK 가이드를 참고해주세요.

- 네이티브 광고 미디에이션 샘플 : [SampleNativeMediation.java](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleNativeMediation.java)

    - 네이티브 동영상 광고 : 네이티브 광고와 동일하게 적용합니다. 네이티브 광고 미디에이션 샘플을 참고해주세요. 광고 SDK 별 비디오 설정은 각 SDK 가이드를 참고해주세요.

### ExelBid-Android-SDK 연동 가이드를 통한 기본 연동  - [ExelBid-Android-SDK 연동 가이드 참조](https://github.com/onnuridmc/ExelBid-Android-SDK)
### 외에 Exelbid 및 타사 광고 SDK 연동은 각각의 해당 가이드를 참조해 설정한다.
* AdMob - [https://developers.google.com/admob/android/quick-start?hl=ko](https://developers.google.com/admob/android/quick-start?hl=ko)
* FaceBook - [https://developers.facebook.com/docs/audience-network/guides/ad-formats](https://developers.facebook.com/docs/audience-network/guides/ad-formats)
* Kakao-Adfit - [https://github.com/adfit/adfit-android-sdk/blob/master/docs/GUIDE.md](https://github.com/adfit/adfit-android-sdk/blob/master/docs/GUIDE.md)
* DigitalTurbine - [https://developer.digitalturbine.com/hc/en-us/articles/360010822437-Integrating-the-Android-SDK](https://developer.digitalturbine.com/hc/en-us/articles/360010822437-Integrating-the-Android-SDK)
* Pangle - [https://www.pangleglobal.com/kr/integration/integrate-pangle-sdk-for-android](https://www.pangleglobal.com/kr/integration/integrate-pangle-sdk-for-android)
* Applovin - [https://dash.applovin.com/documentation/mediation/android/getting-started/integration](https://dash.applovin.com/documentation/mediation/android/getting-started/integration)
* Tnk - [https://github.com/tnkfactory/android-sdk/blob/master/Android_Guide.md](https://github.com/tnkfactory/android-sdk/blob/master/Android_Guide.md)

