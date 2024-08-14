
## Motiv Partners 적용하기

- [시작하기 전에](#시작하기-전에)
- [시작하기](#시작하기)
- [인스턴스 공통 메소드](#인스턴스-공통-메소드)
- [배너광고](#배너광고)
- [네이티브](#네이티브)


### 시작하기 전에
  > Motiv Partners 연동 가이드 입니다. Exelbid SDK를 사용하며 광고 연동 프로세스는 대동소이합니다.


### 시작하기

  - 운영팀에게 채널 아이디를 발급받습니다.
  - Motiv partners 광고는 Exelbid SDK를 통해서 적용 할 수 있습니다.
  지원 버전은 v1.9.9 이상에서 지원됩니다.
    ```java
    dependencies {
            implementation 'com.onnuridmc.exelbid:exelbid:1.9.9'
    }
    ```
          
### 인스턴스 공통 메소드

* ``setChannelId(String)`` : 채널 아이디를 셋팅 합니다.

### 배너광고

>띠 배너 형태의 광고를 사용합니다.

[<b>배너 샘플 소스 링크<b>](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleMotivPartnersBannerView.java)

1.  배너 광고 인스턴스를 원하는 layout위치에다가 생성합니다.
    ```xml
    <com.onnuridmc.exelbid.MPartnersAdView
        android:id="@+id/adview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </com.onnuridmc.exelbid.MPartnersAdView>
    ```

2. Activity에서 해당 인스턴스를 바인딩 합니다.
    ```java
    MPartnersAdView mAdView = (ExelBidAdView) findViewById(R.id.adview);
    ```
3. 광고 영역의 사이즈를 등록합니다.
    ```java
    setAdSize(Int, Int) // width, height를 dp기준으로 설정 mAdView.setAdSize(320, 50)
    ```

4. 채널 아이디를 배너 인스턴스에 셋팅합니다.
    ```java
    setChannelId(String)
    ```

5. 광고를 요청합니다.
    ```java
    loadAd()
    ```

6. 광고 이벤트 등록
  * ``setAdListener(OnBannerAdListener)``
    - ``onAdLoaded()`` : 광고가 로딩된 시점에 호출 됩니다.
    - ``onAdFailed(ExelBidError)`` : 서버로부터 광고를 가져오지 못한 경우에 호출 됩니다.
    - ``onAdClicked()`` : 광고 클릭시 호출 됩니다.

7. Activity 종료시 destroy를 호출해야 합니다.
    ```java
    destroy()
    ```

### 네이티브
[<b>네이티브 샘플 소스 링크<b>](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleMotivPartnersNative.java)
1. 네이티브 광고 인스턴스를 생성합니다.
    ```java
    MPartnersNative mNativeAd = new MPartnersNative(this, mUnitId, new OnAdNativeListener() {
        @Override
        public void onFailed(ExelBidError error, int statusCode) {
        }
        @Override
        public void onShow() {
        }
        @Override
        public void onClick() {
        }
        @Override
        public void onLoaded() {
        }
    });
    ```
    -	``onFailed`` : 광고 요청 실패시 호출 됩니다.
    -	``onShow`` : 광고가 사용자에게 노출 되었을 경우에 호출 됩니다.
    -	``onClick`` : 사용자가 광고를 클릭할 경우에 호출됩니다.
    -	``onLoaded`` :  서버로부터 광고를 가져 왔을 경우에 호출 됩니다.

2. 광고가 레이아웃 설정 후, 레이아웃 정보를 NativeViewBinder을 이용하여 바인딩 합니다.
    ```xml
    -- 광고 레이아웃 예시
    <LinearLayout
        android:id="@+id/native_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true" >

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <ImageView
                android:id="@+id/native_icon_image"
                android:layout_width="64dp"
                android:layout_height="64dp" />

            <TextView
                android:id="@+id/native_title"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:layout_marginLeft="15dp"
                android:layout_weight="1"
                android:textColor="@android:color/darker_gray"
                android:textStyle="bold"/>
        </LinearLayout>

        <TextView
            android:id="@+id/native_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:textColor="@android:color/darker_gray"/>
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <ImageView
                android:id="@+id/native_main_image"
                android:layout_width="match_parent"
                android:layout_height="250dp"
                android:layout_gravity="center_horizontal"
                android:contentDescription="native_main_image" />

        </RelativeLayout>

        <Button
            android:id="@+id/native_cta"
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
    ```
    ```java
    mNativeRootLayout = findViewById(R.id.native_layout);
    mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(mNativeRootLayout)
              .mainImageId(R.id.native_main_image)
              .callToActionButtonId(R.id.native_cta)
              .titleTextViewId(R.id.native_title)
              .textTextViewId(R.id.native_text)
              .iconImageId(R.id.native_icon_image)
              .build());
    ```

	- ``NativeViewBinder.Builder(View view)`` : 네이티브 광고가 노출 되어야 하는 최상위 View를 설정합니다.
		광고요청시 설정되는 항목으로는 제목, 상세설명, 메인이미지, 아이콘, 액션 버튼의 텍스트가 있으며,
        어플리케이션에서 사용할 항목만 NativeViewBinder에 설정하면 됩니다.
	- ``mainImageId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 메인 이미지가 노출될 ImageView의 id를 설정합니다.
	- ``callToActionButtonId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 ActionButton id를 설정합니다. 해당 Button에 텍스트가 설정 됩니다.
	- ``titleTextViewId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 제목이 설정 될 TextView의 id를 설정합니다.
	- ``textTextViewId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 설명이 설정 될 TextView의 id를 설정합니다.
	- ``iconImageId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 아이콘이 노출될 ImageView의 id를 설정합니다.
	- ``build();`` : 설정한 항목으로 NativeViewBinder객체를 생성합니다.

3. 네이티브 광고 요청시 어플리케이션에서 필수로 요청할 항목들을 설정합니다.

  - 기본적으로 NativeViewBinder를 통해 설정된 asset항목들은 옵션으로 요청 되어 지며, 아래 setRequiredAsset를 통해 지정될 경우 필수로 지정된다.
    ```java
    setRequiredAsset(NativeAsset[])
    ```
	- ``TITLE`` : 제목
	- ``CTATEXT`` : 버튼에 표시될 텍스트
	- ``ICON`` : 아이콘
	- ``MAINIMAGE`` : 이미지
	- ``DESC`` : 상세설명

    ```java
    // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 
    mNativeAd.setRequiredAsset(
        new NativeAsset[] {
        NativeAsset.TITLE, NativeAsset.CTATEXT, 
        NativeAsset.ICON, NativeAsset.MAINIMAGE}
        );
    ```        

4. 네이티브 광고 요청
    ```java
    loadAd()
    ```

5. 광고 로딩 확인
	```java
    //광고를 노출시킬 준비가 되어있는지 체크한다.
	(boolean) isReady() 

9. 네이티브 광고 노출
    ```java
    show() //네이티브 광고가 올바르게 로딩 된 경우에 Binder에 등록된 정보에 광고 데이터를 바인딩 합니다.
    ```
