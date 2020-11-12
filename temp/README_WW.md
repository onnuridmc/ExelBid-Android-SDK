
목차
=================

* [Version History](#version-history)
* [시작하기 전에](#시작하기-전에)
* [시작하기](#시작하기)
* [어플리케이션 설정](#어플리케이션-설정)
  * [ExelBid SDK 추가하기](#exelbid-sdk-추가하기)
  * [Google Library 추가하기](#google-library-추가하기)
  * [프로가드 설정](#프로가드-설정)
  * [AndroidManifest 설정](#androidmanifest-설정)
  * [AppKey 설정](#appkey-설정)
  * [광고 클릭시 브라우저 앱 선택 설정](#광고-클릭시-브라우저-앱-선택-설정)
  * [Permission 설정](#permission-설정)
  * [Android 9 Api Level 28+ 사용 설정시 적용 사항](#Android-9-Api-Level-28+-사용-설정시-적용-사항)
* [광고 적용하기](#광고-적용하기)
  * [인스턴스 공통 메소드](#인스턴스-공통-메소드)
  * [위치 정보 사용 설정](#위치-정보-사용-설정)
  * [네이티브](#네이티브)
* [Ads.txt App-ads.txt 적용하기](#Ads.txt-App\-ads.txt-적용하기)



## Version History

**Version 1.4.7**
  * _광고 클릭시 브라우저 설정 기능 업데이트_

**Version 1.4.6**
  * _광고 클릭시 브라우저 설정 기능 추가_

**Version 1.4.4**
  * _Kakao Adfit 버전 3.0.8업데이트_

**Version 1.4.3**
  * _Android 6.0이하에서 전면 클릭시 오류 케이스 수정_

**Version 1.4.2**
  * _노출 트래킹 시 Scrren on/off상태 확인 기능 제거(오류 확인)_
  
**Version 1.4.1**
  * _다이얼로그 배너 광고 미디에이션 기능 추가 - Kakao Adfit Mediation 기능 추가_
  * _배너 광고 노출 트래킹 최적화 - Scrren 상태 확인 처리_

**Version 1.4.0**
  * _Build gradle 3.2.1 적용_
  * _미디에이션 기능 추가 - Kakao Adfit Mediation 기능 추가_

**Version 1.3.6**
  * _다이얼로그 광고 노출시 광고 없는 경우도 다이얼로그 창 노출 가능하게 적용_
  * _Native, 전면 광고등에서 광고 로드후 노출(show)까지의 시간 확인 기능 추가. (전면 광고 - 3.광고확인 등)_

**Version 1.3.5**
  * _다이얼로그 광고 show시 data 로드 오류 수정_
  * _다이얼로그 광고 Sample추가_

**Version 1.3.3**
  * _Manifest 불필요한 속성(android:allowBackup, android:supportsRtl) 제거_
  * _ExelbidNativeManager 사용 광고 요청시 네트워크 상황에 따라 리스트 순서 설정 안되는 버그 수정_

**Version 1.3.2**
  * _Request시 device정보 parameter 중 Null인 경우에 오류 발생 경우 수정_

**Version 1.3.1**
  * _네이티브 어댑터 사용시 클릭 버그 수정 적용(v1.3.0발생)_

**Version 1.3.0**
  * _AdNativeRecyclerAdapter 사용시 최초 화면 광고 없을시, 다음 인덱스 못가져오는 오류 수정_
  * _배너형 광고 노출 확인 시점 명확하게 수정(실제 뷰가 노출되는 시점)_
  * _Tracking(Impression, click) 클라이언트 처리_

**Version 1.2.2**
  * _AdNativeRecyclerAdapter 에서 화면 갱신 바로 안되는 오류 수정_

**Version 1.2.1**
  * _SDK minSdkVersion 9로 적용._

**Version 1.2.0**
  * _인스턴스 공통 메소드에 [setCoppa](#인스턴스-공통-메소드) 함수 추가._
  * _방송통신위원회 시행령 '온라인 맞춤형 광고 개인정보보호 가이드라인' 에 따라서 네이티브 adInfoImageId 관련  추가 안내_

## 시작하기 전에
1. Exelbid에서는 광고 요청에 대한 응답 후 노출까지의 시간(노출 캐시 시간)을 30분 이내로 권장합니다.(IAB 권장) <br>
광고 응답 이후 노출 시간 차이가 해당 시간보다 길어지면 광고 캠페인에 따라서 노출이 무효 처리 될 수 있습니다.


## 시작하기

1. 계정을 생성합니다
2. Inventory -> New App을 선택합니다.<br/>
![new app](./img/sdk-1.png)

3. 앱정보를 등록한 후, unit을 생성 합니다.

## 어플리케이션 설정

### ExelBid SDK 추가하기
* Android Studio
	1. 모듈의 build.gradle파일에 dependencies에 아래 항목을 추가합니다.
	```java
    dependencies {
        	implementation 'com.onnuridmc.exelbid:exelbid:1.4.7'
	}
    ```

### Google Library 추가하기
> ExelBid Android SDK가 제대로 작동하려면 Google Play Service 4.0 이상의 라이브러리가 필요합니다. 광고 식별자 수집에 대한 Google Play 콘텐츠 가이드라인을 준수하기 위한 것입니다.

1. ``AndroidManifest.xml``파일에 <application> 태그 안에 아래 코드를 추가합니다.
```xml
<meta-data
android:name="com.google.android.gms.version"
android:value="@integer/google_play_services_version" />
```

2. Google Play Service jar를 dependencies에 추가합니다.
poject structure -> dependencies -> add -> library dependency 에서 com.google.android.gms:play-services or com.google.android.gms:play-services-ads를 추가합니다
![import](./img/sdk-3.png)
_* eclipse를 사용하는 경우에는 Google Play Service 라이브러리 프로젝트를 추가합니다._


<br/>

### 프로가드 설정

```java
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}
-keep class com.google.android.gms.common.api.GoogleApiClient { public *; }
-keep class com.google.android.gms.common.api.GoogleApiClient$* {public *;}
-keep class com.google.android.gms.location.LocationServices {public *;}
-keep class com.google.android.gms.location.FusedLocationProviderApi {public *;}

-keepattributes SourceFile,LineNumberTable,InnerClasses
-keep class com.onnuridmc.exelbid.** { *; }
```

<br>

### AndroidManifest 설정

### 광고 클릭시 브라우저 앱 선택 설정
>광고 클릭시 랜딩이 이루어지는 브라우저 앱을 설정한다.<br/>
최초 앱 실행시(MainActivity등) 한번 설정하며, 등록 순서가 호출 순서이다
```java
ExelBid.addTargetBrowser("com.android.chrome"); // 크롬 브라우저
ExelBid.addTargetBrowser("com.sec.android.app.sbrowser"); // 삼성 브라우저
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
    - ``boolean removeTargetBrowser(Context context, int index)`` : 전달된 index에 저장된 브라우저를 삭제한다. 삭제 시 true 반환

### Permission 설정

* 필수권한
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

* 권장권한
```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
```

### Android 9 Api Level 28+ 사용 설정시 적용 사항
* Api Level 28+ 부터 적용된 보안 정책 
<br>Android P를 대상으로 하는 앱이 암호화되지 않은 연결을 허용하지 않는것을 기본으로 합니다.
<br>http:// -> https:// 전환을 필요로 한니다. 
<br>Exelbid에서는 광고 요청등의 Api에 https를 사용하지만 Exelbid에 연결된 많은 광고주 플랫폼사들의 광고 소재 리소스(image, js등)의 원할한 활용을 위해 http사용 허가 설정이 필요합니다.

```xml
// 1번 혹은 2번 형태로 적용 가능

1. res/xml 에 networkSecurityConfig 파일 구성후 AndroidManifest.xml에 application 속성에 
   android:networkSecurityConfig="@xml/network_security_config" 설정
    
    //res/network_security_config.xml (networkSecurityConfig 파일)
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <base-config cleartextTrafficPermitted="true" />
    </network-security-config>


    // AndroidManifest.xml
    <application
        .
        .
        android:networkSecurityConfig="@xml/network_security_config">

2.  AndroidManifest.xml에 application 속성에 
    android:usesCleartextTraffic="true" 직접 설정 
      <application
        .
        .
        android:usesCleartextTraffic="true">      
```

* Apache HTTP 클라이언트 지원 중단에 대한 설정 필요
<br>Exelbid SDK에서 사용하는 Network 모듈은 Apache HTTP를 사용하고 있습니다. 
<br>따라서 Android 9 이상을 대상으로 하는 앱이 Apache HTTP 클라이언트를 계속 사용하려면 다음을 AndroidManifest.xml에 추가해야 합니다.
    ```xml
      <application
        .
        .
      >
        <uses-library
            android:name="org.apache.http.legacy"
            android:required="false"/>
    ```


## 광고 적용하기

### 인스턴스 공통 메소드

>광고의 효율을 높이기 위해 나이, 성별을 설정하는 것이 좋습니다.

*	setYob(String) : 태어난 연도 4자리(2016)
*	setGender(boolean) : 성별 (true : 남자, false : 여자)
*	addKeyword(String, String) : Custom 메타 데이터 (Key, Value)
*	setTestMode(boolean) : 광고의 테스트를 위해 설정하는 값입니다. 통계에 적용 되지 않으며 항상 광고가 노출되게 됩니다.
*	setAdUnitId(String) : 광고 아이디를 셋팅 합니다.
* setCoppa(boolean) : 선택사항으로 미국 아동 온라인 사생활 보호법에 따라 13세 미만의 사용자를 설정하면 개인 정보를 제한하여 광고 입찰 처리됩니다. (IP, Device ID, Geo 정보등)

### 위치 정보 사용 설정
>위치 정보는 기본적으로 사용으로 설정 되어 있으며, 사용 안함 설정은 두가지 방법이 있습니다. 둘중 하나만 true 설정 하셔도 위치 정보 사용하지 않습니다. 
1. Manifest 에 meta-data 등록
    ```xml
    <meta-data android:name="com.exelbid.LOCATION_DISABLE" android:value="false"/>
    ```
2. 동적 설정 – 앱 실행 시 최초 한번만 설정
    ```java
    ExelBid.setLocationDisable(true);
    ```


### 네이티브
1.	네이티브 광고 인스턴스를 생성합니다.
    ```java
    ExelBidNative mNativeAd = new ExelBidNative(this, mUnitId, new OnAdNativeListener() {
        @Override
        public void onFailed(ExelBidError error) {
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
    -	``onFailed`` : 광고 요청 실패시 호출 됩니다. (응답할 광고가 없을 경우 포함)
    -	``onShow`` : 광고가 사용자에게 노출 되었을 경우에 호출 됩니다.
    -	``onClick`` : 사용자가 광고를 클릭할 경우에 호출됩니다.
    -	``onLoaded`` :  서버로부터 광고를 가져 왔을 경우에 호출 됩니다.

2.	광고가 노출될 영역에 대한 정보를 바인딩 합니다.
    ```java
    mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(mNativeRootLayout)
              .mainImageId(R.id.native_main_image)
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

3.	네이티브 광고 요청시 어플리케이션에서 필수로 요청할 항목들을 설정합니다.
    ```java
    setRequiredAsset(NativeAsset[])
    ```

	- ``TITLE`` : 제목
	- ``CTATEXT`` : 버튼에 표시될 텍스트
	- ``ICON`` : 아이콘
	- ``MAINIMAGE`` : 이미지
	- ``DESC`` : 상세설명
	- ``RATING`` : 별점

4.	네이티브 광고 이미지를 조작합니다.
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

5.	네이티브 광고 요청
    ```java
    loadAd()
    ```

6.	네이티브 광고 데이터 가져오기
	```java
    mNativeAd.getNativeAdData()
    ```

3.	광고 로딩 확인
	```java
    //광고를 노출시킬 준비가 되어있는지 체크한다.
	(boolean) isReady() 
    /**
     * 광고를 노출시킬 준비가 되어있는지, 광고 로드후 현재 까지의 시간이 특정 시간이 지났는지를 체크한다.
     * adCachTimeSecond 광고 로드후 현재 까지의 시간
     */
	(boolean) isReady(int adCachTimeSecond) 
	```

6.	네이티브 광고 노출
    ```java
    show() //네이티브 광고가 올바르게 로딩 된 경우에 Binder에 등록된 정보에 광고 데이터를 바인딩 합니다.
    ```

## Ads.txt App-ads.txt 적용하기
Exelbid에서도 웹지면에서의 App.txt 그리고 앱지면을 위한 App-ads.txt를 적극 권장하며 지원하고 있습니다.<br/>
Ads.txt, App-ads.txt에 대해서, 그리고 Exelbid에서 적용하는 방법은 아래에 링크 페이지를 참고해 주세요.

[1. Ads.txt란 무엇인가?](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/ads.txt.md)

[2. App-ads.txt 알아보기](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/app-ads.txt.md)

[3. Exelbid에서 ads.txt, app_ads.txt 적용하기](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/README.md)


>이 문서는 온누리DMC Exelbid 제휴 당사자에 한해 제공되는 자료로 가이드 라인을 포함한 모든 자료의 지적재산권은 주식회사 온누리DMC가 보유합니다.<br>
Copyright © OnnuriDmc Corp. All rights reserved.