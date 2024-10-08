
## 광고 적용하기

- [시작하기 전에](#시작하기-전에)
- [시작하기](#시작하기)
- [인스턴스 공통 메소드](#인스턴스-공통-메소드)
- [배너광고](#배너광고)
- [전면 광고](#전면-광고)
- [전면 광고 - 비디오](#전면-광고---비디오)
- [네이티브](#네이티브)
- [전면 네이티브 광고](#전면-네이티브-광고)
- [전면 타이머 기능](#전면-타이머-기능)
- [다이얼로그 공통 메소드](#다이얼로그-공통-메소드)
- [다이얼로그 광고 (전면)](#다이얼로그-광고-전면)
- [다이얼로그 광고 (네이티브)](#다이얼로그-광고-네이티브)
- [광고 클릭시 브라우저 앱 선택 설정](#광고-클릭시-브라우저-앱-선택-설정)
- [Ads.txt App-ads.txt 적용하기](#adstxt-app-adstxt-적용하기)



### 시작하기 전에
  > Exelbid에서는 광고 요청에 대한 응답 후 노출까지의 시간(노출 캐시 시간)을 30분 이내로 권장합니다.(IAB 권장) <br>
광고 응답 이후 노출 시간 차이가 해당 시간보다 길어지면 광고 캠페인에 따라서 노출이 무효 처리 될 수 있습니다.


### 시작하기

  1. 계정을 생성합니다
  2.  Inventory -> App -> + Create New App을 선택합니다.<br/>
  ![new app](./img/sdk-1.png)

  3. 앱정보를 등록한 후, unit을 생성 합니다.

          
  ### 인스턴스 공통 메소드

>광고의 효율을 높이기 위해 나이, 성별을 설정하는 것이 좋습니다.

*	``setYob(String)`` : 태어난 연도 4자리(2016)
*	``setGender(boolean)`` : 성별 (true : 남자, false : 여자)
*	``addKeyword(String, String)`` : Custom 메타 데이터 (Key, Value)
*	``setTestMode(boolean)`` : 광고의 테스트를 위해 설정하는 값입니다. 통계에 적용 되지 않으며 항상 광고가 노출되게 됩니다.
*	``setAdUnitId(String)`` : 광고 아이디를 셋팅 합니다.
* ``setCoppa(boolean)`` : 선택사항으로 미국 아동 온라인 사생활 보호법에 따라 13세 미만의 사용자를 설정하면 개인 정보를 제한하여 광고 입찰 처리됩니다. (IP, Device ID, Geo 정보등)
*	``setRewarded(boolean);`` : 지면의 리워드 여부를 설정한다.

	      
### 배너광고

>띠 배너 형태의 광고를 사용합니다.

1.  배너 광고 인스턴스를 원하는 layout위치에다가 생성합니다.
    ```xml
    <com.onnuridmc.exelbid.ExelBidAdView
        android:id="@+id/adview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </com.onnuridmc.exelbid.ExelBidAdView>
    ```

2. Activity에서 해당 인스턴스를 바인딩 합니다.
    ```java
    ExelBidAdView mAdView = (ExelBidAdView) findViewById(R.id.adview);
    ```

3. 사이트로부터 발급받은 유닛 아이디를 확인합니다
  ![unit id](./img/sdk-4.png)

4. 유닛아이디를 배너 인스턴스에 셋팅합니다.
    ```java
    setAdUnitId(String)
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

7. 기본적으로 띠배너의 광고의 경우 유닛에 설정한 리플래쉬 시간에 따라 자동으로 갱신 됩니다. 해당 기능을 사용하지 않게 하기 위해서는 리플래쉬 기능을 해제해 주어야 합니다.
    ```java
    setAutoreflashDisable()
    ```

8. Activity 종료시 destroy를 호출해야 합니다.
    ```java
    destroy()
    ```

### 전면 광고
1.	전면 광고 인스턴스를 생성합니다.
    ```java
    ExelBidInterstitial mInterstitialAd = new ExelBidInterstitial(this, "홈페이지에서 발급받은 전면광고 유닛 아이디");
    ```

2.	전면 광고 요청
	```java
	loadAd()
	```
3.	광고 로딩 확인
	```java
    //전면 광고를 노출시킬 준비가 되어있는지 체크한다.
	(boolean) isReady() 
    /**
     * 전면 광고를 노출시킬 준비가 되어있는지, 광고 로드후 현재 까지의 시간이 특정 시간이 지났는지를 체크한다.
     * adCachTimeSecond 광고 로드후 현재 까지의 시간
     */
	(boolean) isReady(int adCachTimeSecond) 
	```

4.	전면 광고 노출
	```java
    // 전면 광고를 노출합니다.
	show() 
    ```

5.	광고 이벤트 등록
	*	``setInterstitialAdListener(OnInterstitialAdListener)``
		-	``onInterstitialLoaded`` : 광고가 로딩된 시점에 호출 됩니다.
		-	``onInterstitialShow`` : 전면 광고가 사용자에게 노출된 시점에 호출됩니다.
		-	``onInterstitialDismiss`` : 사용자가 전면광고를 닫았을 때 호출 됩니다.
		-	``onInterstitialClicked`` : 광고 클릭시 호출 됩니다.
		-	``onInterstitialFailed`` : 서버로부터 광고를 가져오지 못한 경우에 호출 됩니다.

6.	Activity 종료시 destroy를 호출합니다.
	```java
	destroy()
	```

### 전면 광고 - 비디오 
1. build.gradle 에 비디오 컨포넌트 라이브러리 종속성 추가 (비디오 광고 공통)

    - Exelbid에서는 비디오 플레이어를 ExoPlayer2 기반으로 동작 적용된다.
    - 미 적용시 Exception발생 혹은 광고 노출 되지 않음
      ```java
      def exoplayer_version = '2.13.3'
      dependencies {
          implementation "com.google.android.exoplayer:exoplayer-core:$exoplayer_version"
          implementation "com.google.android.exoplayer:exoplayer-ui:$exoplayer_version"
      }
      ```
2. minSdkVersion 24 미만 버전 (비디오 광고 공통)

    - minSdkVersion 24 미만 버전에서는 gradle.properties 에 아래와 같이 적용 필요 (Gradle 플러그인 버그로 인한)
      ```java
      android.enableDexingArtifactTransform=false
      ```
3. 기존 전면 노출시 사용되는 ExelbidActivity 대신 비디오 노출을 처리하는 VideoPlayerActivity를 manifest에 등록

    - AndroidManifest 설정
      ```xml
      <activity android:name="com.onnuridmc.exelbid.lib.vast.VideoPlayerActivity"
                android:configChanges="keyboardHidden|orientation|screenSize">
      </activity>
      ```    

### 네이티브
1. 네이티브 광고 인스턴스를 생성합니다.
    ```java
    ExelBidNative mNativeAd = new ExelBidNative(this, mUnitId, new OnAdNativeListener() {
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

2. 광고가 노출될 영역에 대한 정보를 바인딩 합니다.
    ```java
    mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(mNativeRootLayout)
              .mainImageId(R.id.native_main_image)
              .mediaViewId(R.id.native_video)
              .callToActionButtonId(R.id.native_cta)
              .titleTextViewId(R.id.native_title)
              .textTextViewId(R.id.native_text)
              .iconImageId(R.id.native_icon_image)
              .adInfoImageId(R.id.native_privacy_information_icon_image)
              .build());
    ```

	- ``NativeViewBinder.Builder(View view)`` : 네이티브 광고가 노출 되어야 하는 View를 설정합니다.
		광고요청시 설정되는 항목으로는 제목, 상세설명, 메인이미지, 아이콘, 별점, 액션 버튼의 텍스트가 있으며,
        어플리케이션에서 사용할 항목만 NativeViewBinder에 설정하면 됩니다.
	- ``mainImageId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 메인 이미지가 노출될 ImageView의 id를 설정합니다.
	- ``mediaViewId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 메인 동영상이 노출될 NativeVideoView의 id를 설정합니다.
	- ``callToActionButtonId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 ActionButton id를 설정합니다. 해당 Button에 텍스트가 설정 됩니다.
	- ``titleTextViewId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 제목이 설정 될 TextView의 id를 설정합니다.
	- ``textTextViewId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 설명이 설정 될 TextView의 id를 설정합니다.
	- ``iconImageId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 아이콘이 노출될 ImageView의 id를 설정합니다.
	- ``ratingBarId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고의 별점이 표시될 RatingBar의 id를 설정합니다.
	- ``adInfoImageId(int resourceId)`` : 생성자에 설정한 View에 포함되어 있는 광고 정보 표시 아이콘이 노출될 ImageView의 id를 설정합니다.
  해당 ImageView의 속성에 android:src를 설정하지 않아도 기본 Info 아이콘이 바인딩 됩니다.
  _**2017/07 방송통신위원회에서 시행되는 '온라인 맞춤형 광고 개인정보보호 가이드라인' 에 따라서 필수 적용 되어야 합니다.
  광고주측에서 제공하는 해당 광고의 타입(맞춤형 광고 여부)에 따라 정보 표시 아이콘(Opt-out)의 노출이 결정됩니다.
  ※ 광고 정보 표시 아이콘이 노출될 ImageView의 사이즈는 NxN(권장 20x20)으로 설정 되어야 합니다.**_
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
	- ``RATING`` : 별점
	- ``VIDEO`` : 메인 동영상

    - MAINIMAGE, VIDEO가 동시에 요청될 시에 MAINIMAGE가 필수, VIDEO는 옵션으로 적용된다.
      ```java
      // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 
        mNativeAd.setRequiredAsset(
          new NativeAsset[] {
            NativeAsset.VIDEO, NativeAsset.TITLE, NativeAsset.CTATEXT, 
            NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC}
          );
      ```        

4. 네이티브 광고 이미지를 조작합니다.
	광고 메인 이미지와 아이콘 Bitmap을 수정해야 하는 일이 있을경우에 해당 Controllor를 등록합니다.
    ```java
    setNativeImageController(new NativeImageControllor() {
        @Override
        public Bitmap mainImageDisplay(Bitmap bitmap, int width, int height) {
            return bitmap;
        }

        @Override
        public Bitmap iconImageDisplay(Bitmap bitmap, int width, int height) {
            return bitmap;
        }
    });)
    ```
    - ``Bitmap mainImageDisplay(Bitmap bitmap, int width, int height)`` :
        메인 이미지가 이미지뷰에 바인딩 되기전에 호출 됩니다. bitmap은 다운받은 메인이미지이며 이미지뷰의 width, height값이 넘어 옵니다.
    - ``Bitmap iconImageDisplay(Bitmap bitmap, int width, int height)`` :
      아이콘 이미지가 이미지뷰에 바인딩 되기전에 호출 됩니다. bitmap은 다운받은 IconImage이며 ImageView의 width, height 값이 넘어 옵니다.

5. VIDEO 적용
   1) build.gradle 에 비디오 컨포넌트 라이브러리 종속성 추가 (비디오 광고 공통)

       - Exelbid에서는 비디오 플레이어를 ExoPlayer2 기반으로 동작 적용된다.
       - mediaViewId로 NativeVideoView 설정시 com.google.android.exoplayer~ 라이브러리 종속성 설정이 없다면 Exception 발생
          ```java
          def exoplayer_version = '2.13.3'
          dependencies {
              implementation "com.google.android.exoplayer:exoplayer-core:$exoplayer_version"
              implementation "com.google.android.exoplayer:exoplayer-ui:$exoplayer_version"
          }
          ```
   2) minSdkVersion 24 미만 버전 (비디오 광고 공통)

       - minSdkVersion 24 미만 버전에서는 gradle.properties 에 아래와 같이 적용 필요 (Gradle 플러그인 버그로 인한)
          ```java
          android.enableDexingArtifactTransform=false
          ```
   3) 레이아웃 구성시 **com.onnuridmc.exelbid.lib.vast.NativeVideoView**로 비디오뷰를 추가 적용한다.

      ```xml
      ...
      <TextView
          android:id="@+id/native_text" ... />
      <RelativeLayout
          ...>

          <ImageView
              android:id="@+id/native_main_image"
              ... />

          <com.onnuridmc.exelbid.lib.vast.NativeVideoView
              android:id="@+id/native_video"
              android:layout_width="match_parent"
              android:layout_height="250dp"
              android:layout_gravity="center_horizontal"
              />

      </RelativeLayout>
      ...
       ```

6. 네이티브 광고 요청
    ```java
    loadAd()
    ```

7. 네이티브 광고 데이터 가져오기
    ```java
    mNativeAd.getNativeAdData()
    ```

8. 광고 로딩 확인
	```java
    //광고를 노출시킬 준비가 되어있는지 체크한다.
	(boolean) isReady() 
    /**
     * 광고를 노출시킬 준비가 되어있는지, 광고 로드후 현재 까지의 시간이 특정 시간이 지났는지를 체크한다.
     * adCachTimeSecond 광고 로드후 현재 까지의 시간
     */
	(boolean) isReady(int adCachTimeSecond) 
	```

9. 네이티브 광고 노출
    ```java
    show() //네이티브 광고가 올바르게 로딩 된 경우에 Binder에 등록된 정보에 광고 데이터를 바인딩 합니다.
    ```

### 전면 네이티브 광고 
  
1. ExelBidNative 생성시 전면 리스너(OnInterstitialAdListener)를 바인딩한다.
    ```java
    ExelBidNative mNativeAd = new ExelBidNative(this, mUnitId, new OnInterstitialAdListener() {

          @Override
          public void onInterstitialLoaded() {}

          @Override
          public void onInterstitialShow() {}

          @Override
          public void onInterstitialDismiss() {}

          @Override
          public void onInterstitialClicked() {}

          @Override
          public void onInterstitialFailed(ExelBidError error, int statusCode) {}
      }
    );
    ```
    
2. 사용자 정의 광고 레이아웃 id를 설정한다. 해당 사용자 정의 레이아웃 파일은 광고 응답 후, show() 호출시 새로운 Activity에서 사용되어진다.

    ```java
    int layoutid = R.layout.sample_native_instl_layout;
    ```
3.	2의 layoutid와 함께 광고가 노출될 영역에 대한 정보를 바인딩 합니다.<br/>
  *- 각 asset 설정은 기본 네이티브 연동과 동일, 네이티브 - 2 참조*
    ```java
    mNativeAd.setNativeViewBinder(layoutid)
              .mainImageId(R.id.native_main_image)
              .mediaViewId(R.id.native_video)
              .callToActionButtonId(R.id.native_cta)
              .titleTextViewId(R.id.native_title)
              .textTextViewId(R.id.native_text)
              .iconImageId(R.id.native_icon_image)
              .adInfoImageId(R.id.native_privacy_information_icon_image)
              .build());
    ```
 
4. 네이티브 광고 요청시 어플리케이션에서 필수로 요청할 항목들을 설정합니다. <br/>
    *- 네이티브 - 3 참조*

5. 전면 네이티브 광고 노출(show)시 전달 된 레이아웃으로 구성된 새로운 전면(Activity) 화면에 광고가 노출된다.


### 전면 타이머 기능
광고의 전환 성과 향상을 위해 일정 시간 노출을 보장하는 타이머 기능을 제공한다.
1. 전면 형식의 광고(배너, 비디오, 네이티브)의 경우에만 적용 가능하다. (ExelBidNative, ExelBidInterstitial) 
2. 적용 
  *	``setTimer(int sec)`` : 타이머가 동작할 시간(초)를 설정한다.
    ```java
    exelbidNative.setTimer(5);
    exelbidInterstitial.setTimer(5);
    ```

### 다이얼로그 공통 메소드
- ``loadAd()`` : 광고를 가져옵니다.
- ``show()`` : 다이얼로그를 노출합니다.
- ``(boolean) isReady()`` : 광고를 노출할 준비가 되었는지 체크합니다.
- ``(boolean) isReady(int adCachTimeSecond)`` : 광고를 노출시킬 준비가 되어있는지, 광고 로드후 현재 까지의 시간(adCachTimeSecond)이 특정 시간이 지났는지를 체크한다.


<a name="다이얼로그-광고-전면"></a>
### 다이얼로그 광고 (전면)
>ExelBidInterstitialDialog를 상속받은 클래스를 생성해야 합니다.
어플리케이션에서 Dialog의 UI를 설정해야 합니다.

1.	다이얼로그의 레이아웃을 설정합니다.
    ```java
    @Override
    protected void onCreate() {
        setContentView(R.layout.dialog_interstitial_layout);
        ...
    }
    ```

2.	광고가 들어갈 영역을 설정한다.
	setContentView에 설정한 레이아웃의 항목 중 광고가 들어갈 영역의 View를 리턴 시킵니다.
    ```java
    (ViewGroup) getAdBindLayout();
    ```
	xml에 광고가 노출될 레이아웃 설정
	```xml
    <FrameLayout
        android:id="@+id/dialog_native_layout"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:background="#FFFFFF"
        android:layout_weight="1"
        >
    </FrameLayout>
    ```
	ExelBidinterstitialDialog를 상속받은 클래스에서 getAdBindLayout을 설정
    ```java
    @Override
    public ViewGroup getAdBindLayout() {
           return (ViewGroup) findViewById(R.id.dialog_bodylayout);
    }
    ```

3.	Activit 종료시 destory를 호출합니다.
    ```java
    destroy()
    ```

<a name="다이얼로그-광고-네이티브"></a>
### 다이얼로그 광고 (네이티브)
>ExelBidNativeDialog를 상속받은 클래스를 생성해야 합니다.
어플리케이션에서 Dialog의 UI를 설정해야 합니다.

1.	다이얼로그의 레이아웃을 설정합니다.
    ```java
    @Override
    protected void onCreate() {
        setContentView(R.layout.dialog_native_layout);
        ...
    }
    ```

2.	(NativeViewBinder) getNativeViewBinder()
	네이티브 광고 데이터가 바인딩 될 뷰의 정보를 설정합니다.
    ```java
    @Override
    protected NativeViewBinder getNativeViewBinder() {
            return new NativeViewBinder.Builder(findViewById(R.id.dialog_native_layout))
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build();
    }
    ```

### 광고 클릭시 브라우저 앱 선택 설정
>광고 클릭시 랜딩이 이루어지는 브라우저 앱을 설정한다.<br/>
최초 앱 실행시(MainActivity등) 한번 설정하며, 등록 순서가 호출 순서이다<br/>
※ Ver 1.4.7 이후 부터는 설정값을 파일(Preference)에 저장

```java
ExelBid.addTargetBrowser(context, "com.android.chrome"); // 크롬 브라우저
ExelBid.addTargetBrowser(context, "com.sec.android.app.sbrowser"); // 삼성 브라우저
```
* 위와 같시 설정후 광고 클릭시 
1. 크롬 브라우저로 선택 호출
2. 크롬 브라우저 앱이 없을 경우 두번째 삼성 브라우저 선택 호출
3. 삼성 브라우저 앱이 없을 경우 기본 브라우저 호출

* 브라우저 설정 및 관리 함수<br/>
    - ``boolean clearTargetBrowser(Context context)`` : 브라우저 설정을 초기화(설정 없음) 한다.
    - ``boolean addTargetBrowser(Context context, String packagename)`` : 기존 설정된 리스트에 추가 설정한다. 기존에 해당 브라우저가 존재하면 false 반환.
    - ``boolean addTargetBrowser(Context context, String packagename, int index)`` : 기존 설정된 리스트 특정 위치(index)에 추가 설정한다. 존에 해당 브라우저가 존재하거나, index가 범위 밖이면 false 반환.
    - ``boolean addTargetBrowserList(Context context, ArrayList<String> packageList)`` : 기존 설정된 리스트에 packageList를 순서대로 추가 설정한다.(중복 제외)
    - ``boolean setTargetBrowser(Context context, String packagename)`` : 초기화 후 전달된 브라우저(packagename)를 설정한다.
    - ``boolean setTargetBrowserList(Context context, ArrayList<String> packageList)`` : 초기화 후 전달된 브라우저 패키지명 리스트를 설정한다.(중복 제외)
    - ``ArrayList<String> getTargetBrowserList(Context context)`` : 저장된 브라우저 패키지명 리스트를 가져온다.
    - ``String getTargetBrowser(Context context, int index)`` : 해당 index에 저장된 브라우저 패키지명을 가져온다.
    - ``boolean removeTargetBrowser(Context context, String packagename)`` : 전달된 packagename 브라우저를 삭제한다. 삭제 시 true 반환
    - ``boolean removeTargetBrowser(Context context, int index)`` : 전달된 index에 저장된 브라우저를 삭제한다. 삭제 시 true 반환<br><br>
        
* 패키지 공개 설정
    - Android 11 이상을 타겟팅시 아래와 같이 Manifest에 적용된 앱 패키지를 공개 처리한다. 
    - 해당 설정은 구글 개발자 가이드 ([Android 11의 패키지 공개 상태 관리](https://developer.android.com/about/versions/11/privacy/package-visibility)) 에 따릅니다.
    
      ```xml
      <manifest package="com.example.game">
          <queries>
              <package android:name="com.android.chrome" />
              <package android:name="com.sec.android.app.sbrowse" />
              ...
          </queries>
          ...
      </manifest>
      ```

### UID2.0

>광고 타켓팅을 위해 필요한 아이디입니다. (v1.8.4 이상)
* 운영팀에 따로 문의 바랍니다.


### Ads.txt App-ads.txt 적용하기
Exelbid에서도 웹지면에서의 App.txt 그리고 앱지면을 위한 App-ads.txt를 적극 권장하며 지원하고 있습니다.<br/>
Ads.txt, App-ads.txt에 대해서, 그리고 Exelbid에서 적용하는 방법은 아래에 링크 페이지를 참고해 주세요.

[1. Ads.txt란 무엇인가?](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/ads.txt.md)

[2. App-ads.txt 알아보기](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/app-ads.txt.md)

[3. Exelbid에서 ads.txt, app_ads.txt 적용하기](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/README.md)