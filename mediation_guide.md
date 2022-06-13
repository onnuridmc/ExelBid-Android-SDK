# Mediation Guide

Exelbid Android SDK를 이용한 광고 연동시 Mediation 연동의 경우, 앱에서 연동하고 있는 타사 광고의 최적화 된 호출 순서를 응답한다.(Exelbid 포함)

## 시작하기

1. 계정을 생성합니다
2.  Inventory -> App -> + Create New App을 선택합니다.<br/>
![new app](./img/sdk-1.png)

3. 앱정보를 등록한 후, unit을 생성 합니다. (unit id 발급)
4. 해당 App->unit을 기준으로 미디에이션 설정
    - mediation->설정하기<br/><br/>


5. 어플리케이션 설정### ExelBid SDK 추가하기
    * Android Studio
        1. repositories 적용
            ```java
            {
            mavenCentral()
            }
            ```
        2. 모듈의 build.gradle파일에 dependencies에 아래 항목을 추가합니다.
            ```java
            dependencies {
                    implementation 'com.onnuridmc.exelbid:exelbid:1.7.3'
            }
6. ExelBid.getMediationData 을 호출하여 최적화 순서 호출
ExelBid.getMediationData
## 미디에이션 설정
1. 연동된 미디에이션(광고 SDK) 목록 설정
    연동 되어진 미디에이션(sdk 연동)리스트를 전달한다.
    - Exelbid에서는 SDK에서 미디에이션 최적화를 위해서 연동 가능한 광고 SDK들을 ***Enum***(***MediationType***)으로 제공한다
    - ***ArrayList*** 형태로 설정한다.

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
                    new ArrayList(Arrays.asList(MediationType.EXELBID, MediationType.ADMOB, MediationType.FAN));
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
    * ***OnMediationOrderResultListener***를 통해서 응답받은 ***MediationOrderResult***객체는 ***poll()*** 함수 호출시마다 순서대로 MediationType 객체를 반환한다.
    * 광고 SDK의 종류와 형식(배너, 전면, 네이티브) 에 따라서 광고 요청 로직을 적용한다
    * loadMediation
    
        ```java
        // MediationOrderResult가 응답 되었고 각각의 SDK 광고 요청 실패시 호출
        private void loadMediation() {
            if(mMediationOrderResult == null) {
                return;
            }
            MediationType mediationType = mMediationOrderResult.poll();
            currentMediationType = mediationType;
    
            // 광고 SDK의 종류와 형식(배너, 전면, 네이티브) 에 따라서 광고 요청 로직을 적용한다
            if (currentMediationType.equals(MediationType.EXELBID)) {
                exelbidAdView.loadAd();

            } else if(currentMediationType != null) {
                if (currentMediationType.equals(MediationType.ADMOB)) {
                    admobView.loadAd(new AdRequest.Builder().build());
    
                } else if (currentMediationType.equals(MediationType.FAN)) {
                    fanView = new com.facebook.ads.AdView(this, UNIT_ID_FAN_BANNER, AdSize.BANNER_HEIGHT_50);
                    fanContainer.addView(fanView);
                    fanView.loadAd(fanView.buildLoadAdConfig().withAdListener(fanAdListener).build());
                }
            }
        } 
        ```
6. MediationOrderResult
    * ***int getSize()*** - 응답된 미디에이션 광록 목록의 개수를 반환
    * ***MediationType poll()*** - 응답된 미디에이션 광록 목록에서 최우선 순위의 ***MediationType***을 반환 후 목록에서 삭제
    * ***reset()*** - ***OnMediationOrderResultListener***를 통해서 응답 받은 목록 개수와 순서로 ***MediationOrderResult***를 초기화 한다.


### 전면, 네이티브 형태는 위의 배너와 같은 방식으로 처리한다.
- 배너 광고 미디에이션 샘플 : [SampleBannerMediation.java](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleBannerMediation.java)
- 전면 광고 미디에이션 샘플 : [SampleInterstitialMediation.java](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleInterstitialMediation.java)
- 네이티브 광고 미디에이션 샘플 : [SampleNativeMediation.java](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleNativeMediation.java)

### ExelBid-Android-SDK 연동 가이드를 통한 기본 연동  - [ExelBid-Android-SDK 연동 가이드 참조](https://github.com/onnuridmc/ExelBid-Android-SDK)
### 외에 Exelbid 및 타사 광고 SDK 연동은 각각의 해당 가이드를 참조해 설정한다.
* AdMob - [https://developers.google.com/admob/android/quick-start?hl=ko](https://developers.google.com/admob/android/quick-start?hl=ko)
* FaceBook - [https://developers.facebook.com/docs/audience-network/guides/ad-formats](https://developers.facebook.com/docs/audience-network/guides/ad-formats)
