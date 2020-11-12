# Mediation - Native 추가 지원 버전
- Exelbid에서 '광고 없음'이 응답되는 경우, 간단한 설정 만으로 타사 SDK에 자동 광고 요청하여 Fill Rate를 증가시킬 수 있습니다.

## Kakao Adfit 추가하기
- ~~Exelbid SDK v 1.4.0 버전부터 지원~~<br>
- ~~Exelbid SDK v 1.4.4버전, adfit 버전 3.0.8 적용 필수(Adfit 정책상 구버전 지원 중단됨(2019년 9월 이후 예정)~~<br>
- Exelbid SDK v 1.6.2버전, adfit 버전 3.5.2 적용 필수 (Adfit Native Mediation 추가 적용)<br>
- Exelbid 운영팀에 사용 제안(아이디 발급등 처리 필요)<br>
- 디스플레이 배너 광고, 네이티브 기본(단독) 형태 (다이얼로그 광고 포함) 에서만 사용 가능
1. minSdkVersion 14 적용 
    - Adfit mediation 기능은 <a href="https://developer.android.com/about/versions/android-4.0">Android 4.0(Ice Cream Sandwich, API Level 14)</a> 이상 기기에서 동작합니다.

2. build.gradle 파일에 Maven repository를 추가합니다.
    ```java
    repositories {
        google()
        jcenter()
        maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' }
    }
    ```
3. build.gradle 파일 dependencies에 Adfit SDK Library를 추가
    ```java
    dependencies {
        implementation "com.google.android.gms:play-services-ads-identifier:17.0.0"
        implementation "com.kakao.adfit:ads-base:3.5.2"
    }
    ```
    
4. Android Gradle 플러그인 업데이트 권장
    - 플러그인 버전 4.0.0+, Gradle 버전 6.1.1+ 이상 사용 권장 <a href="https://developer.android.com/studio/releases/gradle-plugin?hl=ko#updating-plugin">(Android Gradle 플러그인 출시 노트)</a>

## Kakao Adfit 배너 미디에이션 적용하기
> 배너의 경우 위 설정만으로 동작한다.

## Kakao Adfit 네이티브 미디에이션 적용하기
> 현재 Exelbid [기본 네이티브 연동방식 - README.md->네이티브](README.md#네이티브) 에서만 적용 가능하며 해당 내용에 따라 선작업 되어 야 합니다.

    - 기본 네이티브 광고 뷰 레이아웃 외에 Adfit Native 레이아웃을 구성합니다.
    - 두개의 레이아웃은 광고 응답결과에 따라 Visible 속성이 하나는 View.VISIBLE, 나머지 하나는 View.GONE으로 토글 됩니다.

### Adfit Native 레이아웃 구성

| 번호 | 설명                     | UI 클래스                | AdFitNativeAdLayout |
|-----|-------------------------|------------------------|---------------------|
| -   | 광고 영역                 | AdFitNativeAdView       | containerView      |
| 1   | 제목 텍스트                | TextView               | titleView           |
| 2   | 본문 텍스트                | TextView               | bodyView            |
| 3   | 행동유도버튼                | Button                 | callToActionButton |
| 4   | 광고주(브랜드) 이름 텍스트    | TextView                | profileNameView    |
| 5   | 광고주(브랜드) 아이콘 이미지   | ImageView              | profileIconView     |
| 6   | 미디어 소재(이미지, 비디오 등) | AdFitMediaView         | mediaView           |
| 7   | 광고 정보 아이콘 이미지       | -                      | -                   |

- 네이티브 광고는 위의 요소들로 구성됩니다.
- 각 요소들은 위 표를 참고하여 대응하는 UI 클래스를 통해 표시되도록 구현해야 합니다.
- "제목 텍스트"와 "미디어 소재" 요소는 필수입니다.
- "광고 정보 아이콘 이미지"는 "광고 영역" 내에 `AdFitNativeAdRequest`에 설정한 위치에 표시됩니다.
- 사용자가 광고임을 명확히 인지할 수 있도록 "광고", "AD", "Sponsored" 등의 텍스트를 별도로 표시해주셔야 합니다.
- "광고 영역"은 `AdFitNativeAdView(FrameLayout)`, "미디어 소재"는 `AdFitMediaView`를 사용하셔야 합니다.  

### 네이티브 레이아웃 샘플(with Adfit Layout) layout.xml
* [**sample_mediation_layout.xml**](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/res/layout/act_native_mediation.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/native_ad_root_layout">

    <!-- default Layout -->
    <LinearLayout
        android:id="@+id/native_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:background="#FFFFFF"
        android:orientation="vertical"
        android:padding="10dp"
        >

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <ImageView
                android:id="@+id/native_icon_image"
                .../>

            <TextView
                android:id="@+id/native_title"
                .../>

            <ImageView
                android:id="@+id/native_privacy_information_icon_image"
                .../>/>
        </LinearLayout>

        <TextView
            android:id="@+id/native_text"
                .../>/>
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <ImageView
                android:id="@+id/native_main_image"
                .../> />

        </RelativeLayout>

        <Button
            android:id="@+id/native_cta"
                .../>/>

    </LinearLayout>

    <!-- Adfit Layout -->
    <com.kakao.adfit.ads.na.AdFitNativeAdView
        android:id="@+id/adfit_native_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:background="#FFFFFF"
        android:padding="10dp"
        >

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:background="#FFFFFF"
            android:orientation="vertical"
            android:padding="10dp"
            >
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

                <ImageView
                    android:id="@+id/native_icon_image2"
                    android:layout_width="64dp"
                    android:layout_height="64dp"
                    />

                <TextView
                    android:id="@+id/native_title2"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:layout_marginLeft="15dp"
                    android:layout_weight="1"
                    android:textColor="@android:color/darker_gray"
                    android:textStyle="bold"/>
            </LinearLayout>

            <TextView
                android:id="@+id/native_text2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:textColor="@android:color/darker_gray"/>

            <RelativeLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

                <com.kakao.adfit.ads.na.AdFitMediaView
                    android:id="@+id/native_main_image2"
                    android:layout_width="match_parent"
                    android:layout_height="250dp"
                    android:layout_gravity="center_horizontal"
                    android:contentDescription="native_main_image" />

            </RelativeLayout>

            <Button
                android:id="@+id/native_cta2"
                android:layout_width="wrap_content"
                android:layout_height="35dp"
                android:layout_gravity="right"
                android:layout_marginTop="10dp"
                android:clickable="true"
                android:paddingBottom="10dp"
                android:text="learn_more"
                android:textColor="@android:color/black"
                android:textSize="12sp"
                android:textStyle="bold"/>
        </LinearLayout>
    </com.kakao.adfit.ads.na.AdFitNativeAdView>
</RelativeLayout>
```

### Adfit 네이티브 광고 추가 연동
* `AdFitAdInfoIconPosition`으로 광고 정보 아이콘 위치를 설정할 수 있습니다.<br/>
  기본값은 `RIGHT_TOP`(오른쪽 상단)입니다.<br>

  | AdFitAdInfoIconPosition | 위치              |
  |-------------------------|------------------|
  | LEFT_TOP                | 왼쪽 상단          |
  | RIGHT_TOP               | 오른쪽 상단 (기본값) |
  | LEFT_BOTTOM             | 왼쪽 하단          |
  | RIGHT_BOTTOM            | 오른쪽 상단         |
<br>

* ExelBidNative의 `addMediationViewBinder` 메소드<br>
    기본 네이티브 광고에 미디에이션(Adfit Native) 속성을 추가한다.
```java    
    /**
     * @param mediationType 미디에이션 타입
     *                      ex) MediationType.ADFIT
     * @param mediationRootView 광고 로드에 따라 VISIBLE, GONE이 된다. 해당 미디에이션의 Root View를 전달한다.
     * @param objs 미디에이션 광고의 데이터가 바이딩을 담당할 Object를 전달한다.
     *               ex) Adfit의 AdFitNativeAdLayout, AdFitAdInfoIconPosition
     */
    public void addMediationViewBinder(MediationType mediationType, View mediationRootView, Object ... objs)
```    

* [**SampleNativeMediation.java**](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleNativeMediation.java)
```java
...............

        /********* 기본 광고 설정 *********/
        View defaultLayout = findViewById(R.id.default_native_layout);
        mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(defaultLayout)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());

        /********* Adfit 미디에이션 추가시 적용 *********/

        // Mediation Root View
        AdFitNativeAdView adFitNativeAdView = findViewById(R.id.adfit_native_layout);

        // 광고 SDK에 넘겨줄 Mediation[AdFitNativeAdLayout] 정보 구성
        AdFitNativeAdLayout adfitAdLayout = new AdFitNativeAdLayout.Builder(adFitNativeAdView)
                .setTitleView(findViewById(R.id.native_title2)) // 광고 타이틀 문구 (필수)
                .setBodyView(findViewById(R.id.native_text2)) // 광고 본문 텍스트
                .setProfileIconView( findViewById(R.id.native_icon_image2)) // 광고주(브랜드) 이름
//                        .setProfileNameView(findViewById(R.id.native_icon_image2)) // // 광고주 아이콘 (브랜드 로고)
                .setMediaView(findViewById(R.id.native_main_image2)) // 광고 이미지 소재 또는 비디오 소재 (필수)
                .setCallToActionButton(findViewById(R.id.native_cta2)) // 행동 유도 버튼 (ex. 알아보기, 바로가기 등)
                .build();

        /**
         * MediationType.ADFIT 미디에이션 타입
         * adFitNativeAdView 광고 로드에 따라 VISIBLE, GONE이 된다. 해당 미디에이션의 Root View를 전달한다.
         * adfitAdLayout 미디에이션 광고의 데이터가 바이딩을 담당할 Object를 전달한다.
         *               ex) Adfit의 AdFitNativeAdLayout
         * AdFitAdInfoIconPosition.RIGHT_TOP 광고 정보 아이콘 위치를 설정
         */
        mNativeAd.addMediationViewBinder(MediationType.ADFIT, adFitNativeAdView, adfitAdLayout, AdFitAdInfoIconPosition.RIGHT_TOP);

...............        
```




