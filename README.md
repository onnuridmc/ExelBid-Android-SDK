
# 목차
=================
- [Version History](#version-history)
- [어플리케이션 설정](sdk_setting_guide.md#어플리케이션-설정)
  - [ExelBid SDK 추가하기](sdk_setting_guide.md#exelbid-sdk-추가하기)
  - [Google Library 추가하기](sdk_setting_guide.md#google-library-추가하기)
  - [프로가드 설정](sdk_setting_guide.md#프로가드-설정)
  - [AndroidManifest 설정](sdk_setting_guide.md#androidmanifest-설정)
  - [Permission 설정](sdk_setting_guide.md#permission-설정)
  - [Android 9 Api Level 28+ 사용 설정시 적용 사항](sdk_setting_guide.md#android-9-api-level-28-사용-설정시-적용-사항)
- [광고 적용하기](./ad_guide.md)
- [Exelbid Mediation](./mediation_guide.md)
- [Motiv Partners](./mp_guide.md)

## Version History

**Version 2.0.2**
* 추가 안정성 업데이트

**Version 2.0.1**
* ExoPlayer Media3 버전으로 라이브러리 업데이트
* 기타 안정성 업데이트

**Version 1.9.9.1**
  * MPartners 기능 추가
<details>
<summary>히스토리 보기</summary>

**Version 1.9.8.1**
  * 프로가드 룰 업데이트

**~~Version 1.9.8~~**
  * 미디에이션 유닛 아이디 적용

**Version 1.9.7**
  * 멀티 지면 미디에이션 관련 업데이트
  * 미디에이션 타입 추가

**Version 1.9.6**
  * 미디에이션 및 인터페이스 관련 업데이트

**Version 1.9.5**
  * 미디에이션 레거시 상태 함수 관련 업데이트

**Version 1.9.4**
  * 미디에이션 레거시 업데이트
    
**Version 1.9.3**
  * Country 코드 조회 실패 시 오류 수정

**~~Version 1.9.2~~**
  * 배너 클릭 처리시 랜딩 URL 캐싱 오류 확인 수정

**Version 1.9.1**
  * 전면 비디오 사용시 콜백 전달 안되는 오류 수정

**Version 1.9.0**
  * Exelbid InApp Browser 에서 Deeplink 처리 오류 케이스 수정(targetSDK 버전에 의한 문제)

**Version 1.8.8**
  * 배너 자동 갱신 기능 수정 업데이트

**Version 1.8.6**
  * v1.8.5에서 광고 이미지 로딩 안되는 경우 문제 수정

**~~Version 1.8.5~~**
  * 네트워크 보안 정책 강화(https 미사용 부분 확인 사용 처리)
  * UID 2.0 설정 추가 업데이트

**Version 1.8.4**
  * UID 2.0 설정 업데이트(Token Update)
  * Bug Fix - 특정 버전(Android 5.0)에서의 오류 발생 수정

**Version 1.8.3**
  * UID2.0 설정 추가

**Version 1.8.1**
  * 네트워크 모듈 안정화 작업

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
</details>
<br>


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
        	implementation 'com.onnuridmc.exelbid:exelbid:2.0.2'
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

## [# 광고 적용하기](./ad_guide.md)
## [# Exelbid Mediation](./mediation_guide.md)
## [# Motiv Partners](./mp_guide.md)

<br>

>이 문서는 온누리DMC Exelbid 제휴 당사자에 한해 제공되는 자료로 가이드 라인을 포함한 모든 자료의 지적재산권은 주식회사 온누리DMC가 보유합니다.<br>
Copyright © OnnuriDmc Corp. All rights reserved.
