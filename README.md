
목차
=================

- [목차](#목차)
  - [Version History](#version-history)
  - [시작하기 전에](#시작하기-전에)
  - [시작하기](#시작하기)
  - [어플리케이션 설정](#어플리케이션-설정)
    - [ExelBid SDK 추가하기](#exelbid-sdk-추가하기)
    - [Google Library 추가하기](#google-library-추가하기)
    - [프로가드 설정](#프로가드-설정)
    - [AndroidManifest 설정](#androidmanifest-설정)
    - [~~AppKey 설정~~](#appkey-설정)
    - [광고 클릭시 브라우저 앱 선택 설정](#광고-클릭시-브라우저-앱-선택-설정)
    - [Permission 설정](#permission-설정)
    - [Android 9 Api Level 28+ 사용 설정시 적용 사항](#android-9-api-level-28-사용-설정시-적용-사항)
  - [광고 적용하기](#광고-적용하기)
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
  - [Mediation](#mediation)
  - [Ads.txt App-ads.txt 적용하기](#adstxt-app-adstxt-적용하기)


## Version History

**Version 1.8.1**
  * 네트워크 모듈 안정화 작업
  * UID2 설정 함수 추가
  * 기타 OS 업데이트에 따른 최적화

**Version 1.7.9**
  * 전면 네이티브 상품 연동 추가
  * 전면 타이머 기능 추가
  * 공통함수 setRewarded 설정 추가

**Version 1.7.4**
  * 미디에이션 기능 버그 수정
  * 노출 트래킹 업데이트(추가 매크로 반영)
  * 앱러빈 커스텀 네트워크 어댑터 적용

**Version 1.7.3**
  * 전면 비디오 노출 체크 오류 수정
  * 미디에이션 기능 업데이트 및 샘플 추가

**Version 1.7.0**
  * Apache HTTP( org.apache.http.legacy) 클라이언트 사용 제거, Android 기본 지원 HttpURLConnection으로 대체 적용

**~~Version 1.6.8~~**
  * Bug Fix(Custom Adapter 노출 관련)

**~~Version 1.6.7~~**
  * _JCenter 종료에 따른 이관 작업 (to MavenCental)_
  * _Native 응답 Data 제공 함수 추가 적용(click data 등)_

**Version 1.6.2**
  * _~~Adfit Native Mediation 기능 구현 적용~~_
  * _~~Mediation 가이드 별도 적용~~_

**Version 1.6.1**
  * _Bug Fix - WebView UserAgnet 조회 관련_

**Version 1.6.0**
  * _SDK 최적화를 위해서 TargetSdkVersion 29, MinSdkVersion 16 설정 및 빌드 API 수준 안내_

**Version 1.5.4**
  * _X509TrustManager 관련 보안 이슈 수정(Image Downloader 최신 업데이트 버전 적용)_

**Version 1.5.3**
  * _로그 처리등 내부 기능 업데이트_

**Version 1.5.2**
  * _인앱 브라우저 호출 버그 수정_
  * _AndroidManifest Activity 설정시 ExBrowser -> ExelbidBrowser로  명칭 변경_

**Version 1.5.1**
  * _광고 클릭시 랜딩 프로세스 성능 개선_
  * _인앱 브라우저 딥링크 처리 케이스 추가 적용_

**Version 1.4.8**
  * _광고 클릭시 인앱 브라우저(ExelbidBrowser) 랜딩 케이스 구현_

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
2.  Inventory -> App -> + Create New App을 선택합니다.<br/>
![new app](./img/sdk-1.png)

3. 앱정보를 등록한 후, unit을 생성 합니다.

## 어플리케이션 설정

### ExelBid SDK 추가하기
* Android Studio
  1. repositories 적용
	```java
    {
      // jcenter()
      mavenCentral()
    }
	```
	2. 모듈의 build.gradle파일에 dependencies에 아래 항목을 추가합니다.
	```java
    dependencies {
        	implementation 'com.onnuridmc.exelbid:exelbid:1.8.1'
	}
    ```
### 빌드 API 수준
* Google Play의 타겟 API 수준 요구사항 및 Android Supported Library 28 이후 지원 중단에 따른 AndroidX Migrate 적용
* 참조 1 : [Google Play의 타겟 API 수준 요구사항](https://developer.android.com/distribute/best-practices/develop/target-sdk.html) 
* 참조 2 : [Play Console의 대상 API 레벨 요구사항](https://support.google.com/googleplay/android-developer/answer/113469#targetsdk)

    API 레벨 요구사항 | 시작일
    :-----|:-------------
    Android 8.0(API 레벨 26)  | * 2018년 8월 1일: 새로운 앱에 필요<br/> * 2018년 11월 1일: 앱 업데이트에 필요
    Android 9(API 레벨 28) | * 2019년 8월 1일: 새로운 앱에 필요<br/> * 2019년 11월 1일: 앱 업데이트에 필요
    Android 10(API 레벨 29) | * 2020년 8월 1일: 새로운 앱에 필요<br/> * 2020년 11월 1일: 앱 업데이트에 필요

* v1.6.0 미만 버전 - v1.5.4 이하 - (참조)
```xml
    defaultConfig {
        minSdkVersion 9
        targetSdkVersion 28
    }
```    
* v1.6.0 이상 버전 - AndroidX Migrate 적용 - (참조)
```xml
    defaultConfig {
        minSdkVersion 16
        targetSdkVersion 29
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

>com.onnuridmc.exelbid.common.ExelbidBrowser를 추가합니다. 이 Activity는 인앱 브라우저(WebView)를 구현한 것으로 특정 DeepLink 광고 클릭시에만 사용 됩니다.<br/>
MetaData와 com.onnuridmc.exelbid.common.ExelBidActivity를 AndroidManifest.xml의 <applicatrion> 태그 안에 추가합니다. 이 Activity는 전면광고를 표시하는데 사용됩니다.<br/>

```xml
<!-- 필수 -->
<activity android:name="com.onnuridmc.exelbid.common.ExelbidBrowser"
          android:configChanges="keyboardHidden|orientation|screenSize">
</activity>
<!-- 필수(전면 광고시) -->
<activity android:name="com.onnuridmc.exelbid.common.ExelBidActivity"
          android:configChanges="keyboardHidden|orientation|screenSize">
</activity>
```

### ~~AppKey 설정~~
>~~홈페이지에 등록된 어플리케이션의 아이디를 설정한다. (필수)~~

* ~~Manifest 에 meta-data 등록~~
```xml
<meta-data android:name="com.exelbid.appkey"
           android:value="{홈페이지에 등록한 어플리케이션의 아이디}"/>
```

*	 ~~동적 설정 - 앱 실행시 최초 한번만 설정~~
```java
ExelBid.setAppKey(String) // 홈페이지에 등록한 어플리케이션의 아이디
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
    - ``boolean removeTargetBrowser(Context context, int index)`` : 전달된 index에 저장된 브라우저를 삭제한다. 삭제 시 true 반환

### Permission 설정

* 필수권한
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

// 구글 정책(2022.03.15 발표)에 따라 대상 API 수준을 32(Android 13)로 업데이트하는 앱은 다음과 같이 매니페스트 파일에서 Google Play 서비스 일반 권한을 선언해야 합니다.(정책 적용 2022년 말 예정)
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
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

* Apache HTTP 클라이언트 지원 중단에 대한 설정 필요 (Ver 1.7.0 이상에서는 설정 필요 없음)
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
    -	``onFailed`` : 광고 요청 실패시 호출 됩니다.
    -	``onShow`` : 광고가 사용자에게 노출 되었을 경우에 호출 됩니다.
    -	``onClick`` : 사용자가 광고를 클릭할 경우에 호출됩니다.
    -	``onLoaded`` :  서버로부터 광고를 가져 왔을 경우에 호출 됩니다.

2.	광고가 노출될 영역에 대한 정보를 바인딩 합니다.
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

3.	네이티브 광고 요청시 어플리케이션에서 필수로 요청할 항목들을 설정합니다.

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

1. VIDEO 적용
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

2. 네이티브 광고 요청
    ```java
    loadAd()
    ```

6.	네이티브 광고 데이터 가져오기
	```java
    mNativeAd.getNativeAdData()
    ```

7.	광고 로딩 확인
	```java
    //광고를 노출시킬 준비가 되어있는지 체크한다.
	(boolean) isReady() 
    /**
     * 광고를 노출시킬 준비가 되어있는지, 광고 로드후 현재 까지의 시간이 특정 시간이 지났는지를 체크한다.
     * adCachTimeSecond 광고 로드후 현재 까지의 시간
     */
	(boolean) isReady(int adCachTimeSecond) 
	```

8.	네이티브 광고 노출
    ```java
    show() //네이티브 광고가 올바르게 로딩 된 경우에 Binder에 등록된 정보에 광고 데이터를 바인딩 합니다.
    ```

### 전면 네이티브 광고
1. 광고 노출 될 전면 Activity의 레이아웃 id를 이용하여 바인더를 생성한다.

	- ``NativeViewBinder.Builder(int layoutId)`` : 전면 네이티브의 layout을 설정합니다.
  
  
2. ExelBidNative 생성시 파라미터로 마지막에 전면 여부(boolean) true로 생성한다.
    ```java
    ExelBidNative mNativeAd = new ExelBidNative(this, mUnitId, new OnAdNativeListener() {
          @Override
          public void onFailed(ExelBidError error) {}
          @Override
          public void onShow() {}
          @Override
          public void onClick() {}
          @Override
          public void onLoaded() {}
      }
      , true // 전면 여부 설정
    );
    ```
3. 전면 네이티브 광고 노출(show)시 전달 된 레이아웃으로 구성된 새로운 전면(Activity) 화면에 광고가 노출된다.


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
## Mediation
Exelbid에서 '광고 없음'이 응답되는 경우, 간단한 설정 만으로 타사 SDK에 자동 광고 요청하여 Fill Rate를 증가시킬 수 있습니다.

가이드 별도 참조 - [``mediation 가이드``](MEDIATION.md#Kakao-Adfit-추가하기)

## Ads.txt App-ads.txt 적용하기
Exelbid에서도 웹지면에서의 App.txt 그리고 앱지면을 위한 App-ads.txt를 적극 권장하며 지원하고 있습니다.<br/>
Ads.txt, App-ads.txt에 대해서, 그리고 Exelbid에서 적용하는 방법은 아래에 링크 페이지를 참고해 주세요.

[1. Ads.txt란 무엇인가?](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/ads.txt.md)

[2. App-ads.txt 알아보기](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/app-ads.txt.md)

[3. Exelbid에서 ads.txt, app_ads.txt 적용하기](https://github.com/onnuridmc/Exelbid_Ads.txt/blob/master/README.md)

>이 문서는 온누리DMC Exelbid 제휴 당사자에 한해 제공되는 자료로 가이드 라인을 포함한 모든 자료의 지적재산권은 주식회사 온누리DMC가 보유합니다.<br>
Copyright © OnnuriDmc Corp. All rights reserved.
