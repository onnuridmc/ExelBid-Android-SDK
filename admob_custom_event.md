# AdMob에서의 Exelbid Mediation (Custom Event)

Google AdMob 미디에이션 워터폴에 Exelbid를 광고 소스로 추가하는 방법입니다.

Exelbid SDK에 AdMob 커스텀 이벤트 어댑터가 포함되어 있으므로 **매체에서 어댑터를 직접 구현할 필요가 없습니다.**
SDK를 추가하고 AdMob 대시보드에 클래스명을 등록하면 됩니다.

## 목차
- [지원 범위](#지원-범위)
- [동작 방식](#동작-방식)
- [1. SDK 추가](#1-sdk-추가)
- [2. AndroidManifest 설정](#2-androidmanifest-설정)
- [3. AdMob 대시보드 설정](#3-admob-대시보드-설정)
- [4. 연동 확인](#4-연동-확인)
- [5. 문제 해결](#5-문제-해결)
- [GMA Next-Gen SDK 지원](#gma-next-gen-sdk-지원)
- [알아두실 사항](#알아두실-사항)

---

## 지원 범위

| 항목 | 내용 |
|---|---|
| Exelbid SDK | **2.1.0 이상** |
| Google Mobile Ads SDK (레거시, `play-services-ads`) | **22.6.0 이상** |
| GMA Next-Gen SDK (`ads-mobile-sdk`) | **1.4.0 검증 완료** — [상세](#gma-next-gen-sdk-지원) |
| 지원 광고 형식 | 배너, 전면광고, 네이티브 광고 고급형 |
| 미지원 광고 형식 | 보상형, 보상형 전면, 앱 오프닝 |
| 미디에이션 방식 | 폭포식(워터폴). 입찰(비딩)은 지원하지 않습니다 |

미지원 형식으로 요청이 들어오면 어댑터가 즉시 실패를 반환하여 AdMob이 워터폴의 다음 소스로 넘어갑니다.

### minSdk 요구사항

Google Mobile Ads SDK는 버전에 따라 최소 Android 버전이 다릅니다. **매체 앱의 `minSdk`가 아래 값보다 낮으면 매니페스트 병합 단계에서 빌드가 실패합니다.**

| Google Mobile Ads SDK | 요구 minSdk |
|---|---|
| 22.x | 19 |
| 23.x | 21 |
| **24.0.0 이상** | **23** |

Exelbid SDK 자체의 `minSdk`는 **21**입니다.
`minSdk 21`인 앱에서 GMA 24 이상을 쓰려면 앱의 `minSdk`를 23으로 올려야 합니다.

### 검증 범위

- 어댑터는 GMA **25.4.0** 기준으로 컴파일하되, **22.6.0부터 존재하는 API만** 사용합니다.
- 실기기 동작 검증은 GMA(레거시) **22.6.0** 환경(Android 14, R8 적용 release 빌드)에서 수행했습니다.
- **GMA Next-Gen SDK 1.4.0** 환경에서도 배너/전면/네이티브 3형식의 낙찰·렌더·노출 집계를 실기기로 검증했습니다.

---

## 동작 방식

```
매체 앱  AdView.loadAd() / InterstitialAd.load() / AdLoader.loadAd()
   │
   ▼
Google Mobile Ads SDK  ──▶  AdMob 서버
   │                         미디에이션 그룹의 eCPM 순서로 워터폴 구성
   ▼
워터폴 순서대로 시도
   ├─ AdMob Network ................ No Fill
   ├─ Exelbid (Custom Event)  ◀── 대시보드에 등록한 클래스명으로 어댑터를 찾아 호출
   │     │
   │     ├─ Parameter(Exelbid 광고 유닛 ID)로 Exelbid 서버에 광고 요청
   │     ├─ 광고 있음 → AdMob에 전달하여 노출
   │     └─ 광고 없음 → 실패 반환, 워터폴 다음 소스로 진행
   └─ 그 외 광고 소스 ...
```

Google Mobile Ads SDK는 대시보드에 입력된 **클래스명 문자열로 어댑터를 찾습니다.**
따라서 해당 클래스가 앱에 포함되어 있어야 하며, 난독화로 이름이 바뀌어서도 안 됩니다.
두 조건 모두 Exelbid SDK를 추가하면 자동으로 충족됩니다.

---

## 1. SDK 추가

### repositories

```gradle
repositories {
    mavenCentral()
}
```

### dependencies

```gradle
dependencies {
    // Exelbid SDK (AdMob 커스텀 이벤트 어댑터 포함)
    implementation 'com.onnuridmc.exelbid:exelbid:2.1.0'

    // Google Mobile Ads SDK — 매체에서 직접 추가해야 합니다
    implementation 'com.google.android.gms:play-services-ads:22.6.0'

    // 비디오 전면 광고를 사용하는 경우 필요합니다 (아래 설명 참고)
    implementation 'androidx.media3:media3-exoplayer:1.2.0'
    implementation 'androidx.media3:media3-ui:1.2.0'
    implementation 'androidx.media3:media3-common:1.2.0'
}
```

> Google Mobile Ads SDK는 Exelbid SDK에 포함되지 않습니다(`compileOnly` 참조).
> AdMob을 사용하지 않는 앱에는 GMA 라이브러리가 딸려오지 않으며, 어댑터 클래스 몇 개(수 KB)만 포함됩니다.

### 비디오 전면 광고와 media3

Exelbid 전면 광고 응답이 **VAST(비디오) 소재**인 경우 `androidx.media3`가 필요합니다.
의존성이 없으면 해당 요청은 **무조건 실패 처리**되며, 매체에서는 원인을 알 수 없는 No Fill로 보입니다.

이미지 전면만 사용한다면 추가하지 않아도 됩니다.

---

## 2. AndroidManifest 설정

### AdMob 앱 ID

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-0000000000000000~0000000000" />
```

**광고 단위 ID와 같은 계정의 앱 ID여야 합니다.** 계정이 다르면 광고가 전혀 응답하지 않습니다.

### Exelbid Activity 선언 (필수)

Exelbid SDK는 AAR에 Activity를 선언해 두지 않습니다. **매체 앱에서 직접 선언해야 하며, 누락 시 전면 광고 표시 시점에 `ActivityNotFoundException`이 발생합니다.**

```xml
<!-- 필수: 딥링크 광고 클릭 시 사용하는 인앱 브라우저 -->
<activity
    android:name="com.onnuridmc.exelbid.common.ExelbidBrowser"
    android:configChanges="keyboardHidden|orientation|screenSize" />

<!-- 필수(전면 광고 사용 시) -->
<activity
    android:name="com.onnuridmc.exelbid.common.ExelBidActivity"
    android:configChanges="keyboardHidden|orientation|screenSize" />

<!-- 비디오 전면 광고 사용 시 -->
<activity
    android:name="com.onnuridmc.exelbid.lib.vast.VideoPlayerActivity"
    android:configChanges="keyboardHidden|orientation|screenSize" />
```

Permission 등 나머지 기본 설정은 [기본 가이드](./README.md)를 참고하시기 바랍니다.

### 프로가드

어댑터 관련 규칙은 **추가로 작성하실 필요가 없습니다.** 어댑터는 클래스명으로 리플렉션 로딩되므로 난독화에서 제외되어야 하는데, 해당 규칙이 Exelbid SDK의 consumer ProGuard 설정에 포함되어 배포됩니다.

> **참고**: Exelbid SDK의 consumer 규칙에는 어댑터 keep 외에 `-ignorewarnings`, `-keeppackagenames`,
> `-keepattributes SourceFile,LineNumberTable` 같은 전역 옵션도 함께 들어 있습니다.
> 매체 앱의 R8 경고가 억제되고 패키지명 난독화가 비활성화되는 영향이 있습니다.

### 광고 호출 코드

미디에이션 연동을 위해 **광고 호출 코드는 수정할 필요가 없습니다.** 기존 AdMob 코드를 그대로 사용하시면 됩니다.

```java
AdView adView = new AdView(this);
adView.setAdUnitId("ca-app-pub-0000000000000000/0000000000");
adView.setAdSize(AdSize.BANNER);
adView.loadAd(new AdRequest.Builder().build());
```

### 샘플 코드

이 저장소의 `exelbid-sample-admob` 모듈에 형식별 동작 샘플이 있습니다.
(GMA Next-Gen SDK용 샘플은 별도 모듈 `exelbid-sample-admob-nextgen` —
레거시와 Next-Gen SDK는 한 앱에 공존할 수 없어 모듈이 분리되어 있습니다.)
각 샘플의 광고 호출 코드에는 `[AdMob 기본 연동 N]` 주석으로 표준 연동 단계가 표시되어 있어,
**미디에이션 적용 시에도 코드가 표준 AdMob 그대로임**을 코드 위에서 확인하실 수 있습니다.

샘플 앱(`./gradlew :exelbid-sample-admob:assembleDebug`)을 빌드해 실행하면
메인 목록의 **"AdMob 배너/전면/네이티브 (ExelBid 어댑터)"** 메뉴로 진입합니다.
각 화면에서 어댑터 등록 여부, 워터폴 시도 순서, 낙찰 어댑터를 바로 확인할 수 있습니다.

#### 배너 — [`SampleAdmobBanner`](exelbid-sample-admob/src/main/java/com/onnuridmc/sample/activity/SampleAdmobBanner.java)

| 단계 | 내용 |
|---|---|
| 1~3 | `AdView` 생성 → 유닛 ID 설정 → 크기 설정 |
| 4 | `AdListener` 등록 — Exelbid이 낙찰돼도 로드/노출/클릭 콜백은 동일하게 수신 |
| 5~6 | 뷰 계층에 추가 → `loadAd()` — 이 호출 안에서 워터폴이 진행됨 |
| 7 | 생명주기 전달 (`resume`/`pause`/`destroy`) |

#### 전면 — [`SampleAdmobInterstitial`](exelbid-sample-admob/src/main/java/com/onnuridmc/sample/activity/SampleAdmobInterstitial.java)

| 단계 | 내용 |
|---|---|
| 1~2 | `InterstitialAd.load()` → 성공 콜백에서 인스턴스 보관 (전면은 1회용) |
| 3 | `FullScreenContentCallback` 등록 — 표시/노출/클릭/닫힘 이벤트 수신 |
| 4 | 원하는 시점에 `show()` — Exelbid 낙찰 시 이 호출로 Exelbid 전면 화면이 열림 |

#### 네이티브 — [`SampleAdmobNative`](exelbid-sample-admob/src/main/java/com/onnuridmc/sample/activity/SampleAdmobNative.java)

| 단계 | 내용 |
|---|---|
| 1~4 | `AdLoader` 구성 → `forNativeAd` 수신 콜백 → 리스너 → `loadAd()` |
| 5~6 | `NativeAdView` 준비 → 애셋 뷰 등록 (등록된 뷰가 클릭 대상) |
| 7 | 애셋 값 바인딩 — headline만 필수, 나머지는 null 체크 (Exelbid 응답에는 별점·advertiser 없음) |
| 8 | 뷰를 화면에 붙인 뒤 `setNativeAd()` — 이 호출로 노출·클릭 추적 시작 |

#### 공통 진단 화면 — [`SampleAdmobMediationBase`](exelbid-sample-admob/src/main/java/com/onnuridmc/sample/activity/SampleAdmobMediationBase.java)

`MobileAds.initialize()`(기본 연동 0단계) 외의 코드는 모두 **연동 검증용 진단 코드**이며 매체 앱에는 필요하지 않습니다.

- **어댑터 초기화 현황** — `ExelBidCustomEvent : READY` 가 보이면 어댑터가 정상 등록된 것
- **워터폴 시도 순서** — `ResponseInfo.getAdapterResponses()`로 Exelbid이 호출됐는지, 몇 ms에 성공했는지 표시
- **낙찰 어댑터** — Exelbid이 광고를 채웠는지 최종 확인
- **광고 검사기 버튼** — `MobileAds.openAdInspector()` 실행

> **매체 적용 시**: 샘플의 AdMob 앱 ID(`AndroidManifest.xml`)와 광고 유닛 ID(`AppConstants.java`)는
> Exelbid 데모 계정의 값입니다. 매체 앱에서는 자신의 AdMob 앱 ID·유닛 ID로 교체해야 합니다.

---

## 3. AdMob 대시보드 설정

### 3-1. 커스텀 이벤트 광고 소스와 매핑 생성

**미디에이션 → 폭포식 구조 소스 → 광고 소스 설정 → 맞춤 이벤트 추가 → 매핑 추가**

| 입력 항목 | 값 |
|---|---|
| 매핑 이름 | 임의의 식별용 이름 (예: `Exelbid_Banner`) |
| **Class Name** | `com.onnuridmc.exelbid.mediation.admob.ExelBidCustomEvent` |
| **Parameter** | 연동할 **Exelbid 광고 유닛 ID** |
| 네트워크 eCPM | 선택 사항이며 참고용 표시값입니다 |

> **Class Name은 세 광고 형식 모두 동일합니다.**
> 어댑터 하나가 배너·전면·네이티브를 모두 처리하며, 형식 구분은 Google Mobile Ads SDK가
> 자동으로 수행합니다.
>
> **Parameter는 광고 형식마다 다릅니다.** 각 AdMob 광고 단위에 대응하는
> Exelbid 광고 유닛 ID를 정확히 입력해 주십시오. 배너 매핑에 네이티브 유닛 ID를 넣으면
> 광고가 응답하지 않습니다.

광고 형식별로 매핑을 각각 생성하시기 바랍니다.

| AdMob 광고 단위 | 매핑 이름 예시 | Parameter |
|---|---|---|
| 배너 광고 단위 | `Exelbid_Banner` | Exelbid 배너 유닛 ID |
| 전면 광고 단위 | `Exelbid_Interstitial` | Exelbid 전면 유닛 ID |
| 네이티브 광고 단위 | `Exelbid_Native` | Exelbid 네이티브 유닛 ID |

**배너는 크기도 맞춰 주십시오.** Exelbid 배너 크기는 Exelbid 광고 유닛 설정으로 결정되며
어댑터가 AdMob 요청 크기를 서버에 강제하지 않습니다. AdMob 광고 단위 크기와 다른 크기의
Exelbid 유닛을 매핑하면 광고가 잘리거나 여백이 생길 수 있습니다.
적응형(Adaptive)·스마트 배너는 크기 정합을 보장하지 않습니다.

### 3-2. 미디에이션 그룹에 추가

**미디에이션 → 미디에이션 그룹 만들기**

1. **광고 형식**과 **플랫폼(Android)** 을 선택합니다.
2. **타겟팅**에서 대상 광고 단위를 선택합니다.
3. **폭포식 구조 광고 소스**에서 **광고 소스 추가 → 맞춤 이벤트**를 선택합니다.
4. **라벨**(예: `Exelbid`)과 **eCPM**을 입력합니다.
5. 3-1에서 생성한 매핑을 선택하고 저장합니다.

> **미디에이션 그룹은 광고 형식별로 각각 만들어야 합니다.**
> 배너 그룹은 배너 요청만 처리하므로, 전면과 네이티브는 별도 그룹이 필요합니다.
>
> **eCPM 값이 워터폴 순서를 결정합니다.** 값이 높을수록 먼저 호출됩니다.
> 연동 테스트 중에는 값을 높게 설정하여 Exelbid가 1순위로 호출되도록 하면 확인이 쉽습니다.

설정 반영에는 수 분이 소요될 수 있으며, 앱을 완전히 종료한 후 다시 실행해야 적용됩니다.

---

## 4. 연동 확인

### 광고 검사기 (권장)

실제 워터폴 구성과 각 광고 소스의 응답을 Google이 직접 보여주는 도구입니다. 가장 확실한 확인 수단입니다.

```java
MobileAds.openAdInspector(this, error -> {
    if (error != null) {
        Log.e("Mediation", "광고 검사기 오류: " + error.getMessage());
    }
});
```

### 워터폴 호출 순서 확인

광고 로드 후 `ResponseInfo`로 실제 시도된 광고 소스를 확인할 수 있습니다.

```java
// import com.google.android.gms.ads.ResponseInfo;
// import com.google.android.gms.ads.AdapterResponseInfo;

ResponseInfo responseInfo = adView.getResponseInfo();
if (responseInfo == null) {
    return;
}

// 낙찰된 광고 소스
Log.d("Mediation", "낙찰: " + responseInfo.getMediationAdapterClassName());

// 시도된 광고 소스 목록
for (AdapterResponseInfo response : responseInfo.getAdapterResponses()) {
    Log.d("Mediation", response.getAdapterClassName()
            + " (" + response.getLatencyMillis() + "ms) "
            + (response.getAdError() == null ? "성공" : response.getAdError().getMessage()));
}
```

> 커스텀 이벤트의 경우 `getAdapterClassName()`은 Google 래퍼 클래스인
> `com.google.ads.mediation.customevent.CustomEventAdapter`를 반환합니다.
> 대시보드에 등록한 실제 클래스명과 Parameter는 `response.getCredentials()` 번들에서 확인하실 수 있습니다.

---

## 5. 문제 해결

| 증상 | 확인 사항 |
|---|---|
| 전면 광고 표시 시 `ActivityNotFoundException` | **`ExelBidActivity` 매니페스트 선언 누락** |
| 워터폴에 Exelbid가 나타나지 않음 | 대시보드 **Class Name 오타**. 미디에이션 그룹 미설정. 해당 광고 형식의 그룹 없음. 타겟팅에서 제외됨. eCPM이 낮아 순위에서 밀림 |
| 워터폴에 있으나 계속 실패 | **Parameter(Exelbid 유닛 ID)** 오류. Exelbid 재고 없음(No Fill) |
| 광고가 전혀 응답하지 않음 | AdMob **앱 ID와 광고 단위 ID의 계정 불일치** |
| 비디오 전면만 실패 | `androidx.media3` 의존성 누락 |
| 빌드 시 매니페스트 병합 실패 | GMA 24 이상 사용 시 앱 `minSdk`가 23 미만 |
| 설정했는데 반영되지 않음 | 반영에 수 분 소요. 앱을 완전히 종료 후 재실행 |

### Google 테스트 광고 단위로는 확인할 수 없습니다

`ca-app-pub-3940256099942544/...` 형식의 Google 테스트 광고 단위는 **미디에이션을 거치지 않고**
항상 Google 테스트 광고를 반환합니다. 연동 확인은 반드시 **미디에이션 그룹이 설정된 실제 광고 단위**로
진행하시기 바랍니다.

---

## GMA Next-Gen SDK 지원

Google은 차세대 [GMA Next-Gen SDK](https://developers.google.com/admob/android/next-gen/quick-start?hl=ko)
(`com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk`)를 출시하면서 기존
`play-services-ads`를 **레거시**로 분류했습니다 (Google 개발자 문서의 플랫폼 선택에서
"Android" = Next-Gen, "Android(레거시)" = 기존 SDK).

**Next-Gen SDK를 사용하는 앱에서도 Exelbid 어댑터는 수정 없이 그대로 동작합니다.**
Next-Gen SDK가 미디에이션 어댑터 API(`com.google.android.gms.ads.mediation` 패키지)를
자체 포함하여 어댑터 호환성을 유지하기 때문입니다.
AdMob 대시보드 설정(Class Name / Parameter)도 레거시와 완전히 동일합니다.

### 레거시 대비 연동 차이점

본 가이드의 1~2장은 레거시 SDK 기준입니다. Next-Gen 앱에서는 아래 항목만 다릅니다.

| 항목 | 레거시 (`play-services-ads`) | Next-Gen (`ads-mobile-sdk`) |
|---|---|---|
| AdMob 앱 ID | AndroidManifest `meta-data` | 코드에서 `InitializationConfig.Builder(앱ID)` 로 전달 |
| SDK 초기화 | 권장 (미호출 시 자동) | **필수** — 초기화 전 로드 시 예외 발생. 백그라운드 스레드 호출 권장 |
| 요구 minSdk | GMA 버전에 따라 19~23 | **24** |
| `play-services-ads` exclude | 해당 없음 | 일반 어댑터는 전역 exclude 필요하지만, **Exelbid SDK는 `compileOnly` 참조라 exclude 설정이 필요 없습니다** |

**Exelbid 관련 설정(SDK 의존성 추가, Exelbid Activity 2종 선언, 프로가드)은 레거시와 동일하게 적용하시면 됩니다.**

```java
// Next-Gen 초기화 예시 - 앱 ID를 코드로 전달한다
new Thread(() -> MobileAds.initialize(
        context,
        new InitializationConfig.Builder("ca-app-pub-XXXX~YYYY").build(),
        initializationStatus -> { /* 초기화 완료 */ })).start();
```

### 샘플 코드

이 저장소의 [`exelbid-sample-admob-nextgen`](exelbid-sample-admob-nextgen) 모듈이
Next-Gen SDK 연동 샘플입니다. 초기화(앱 ID 코드 전달)부터 배너/전면/네이티브 3형식의
로드·표시와 워터폴 진단 화면까지 포함합니다.

### 검증 정보

Next-Gen SDK **1.4.0** + Exelbid SDK **2.1.0** 조합으로 실기기(Android 14, release 빌드)에서 확인:

- 어댑터 초기화: 초기화 상태 맵에 `ExelBidCustomEvent : COMPLETE`
- 배너 / 전면 / 네이티브: 워터폴 낙찰, 렌더링, 노출 집계 정상
- Ad Inspector: Custom Event **Fill** 확인

> **주의**: 레거시 SDK와 Next-Gen SDK는 한 앱에 공존할 수 없습니다(중복 클래스 충돌).
> 다른 미디에이션 어댑터를 함께 쓰는 경우 해당 어댑터가 끌고 오는 `play-services-ads`를
> Google의 [마이그레이션 가이드](https://developers.google.com/admob/android/next-gen/migration?hl=ko)에
> 따라 exclude 처리해야 합니다.

---

## 알아두실 사항

### 워터폴 방식이며 입찰은 지원하지 않습니다

AdMob 맞춤 이벤트는 **수동으로 입력한 eCPM 값**으로 워터폴 순서가 결정됩니다.
실시간 입찰(비딩)에 참여하지 않으며, AdMob의 광고 소스 자동 최적화도 적용되지 않습니다.

따라서 실제 Exelbid의 eCPM 수준이 달라지면 대시보드의 eCPM 값을 주기적으로
조정해 주셔야 기대하는 수익 순위가 유지됩니다.

### 전면 광고는 로드 시점의 Context로 표시됩니다

어댑터는 광고를 **로드한 시점의 Context**로 전면 광고를 띄웁니다.
로드한 화면과 표시하는 화면이 다를 수 있는 구조(스플래시에서 미리 로드 후 다른 화면에서 표시 등)에서는
표시 직전에 로드하시는 것을 권장합니다.

### 배너 노출 집계 시점

배너는 AdMob에 **로드 성공 직후** 노출을 보고합니다(Google 레퍼런스 어댑터와 동일한 방식).
Exelbid 리포트의 집계 기준과 차이가 생길 수 있습니다.

### 네이티브 광고

- **이미지 선다운로드**: 어댑터가 아이콘과 메인 이미지를 미리 받아 전달하므로, 매체에서는
  일반적인 AdMob 네이티브 광고와 동일하게 처리하시면 됩니다. 다만 이미지 크기와 네트워크 상태에 따라
  **로드 완료가 지연**될 수 있습니다. 미디에이션 타임아웃 설정 시 이를 감안해 주십시오.
- **필수 애셋**: Exelbid 요청에 제목·행동유도문구·메인이미지를 필수로 지정합니다.
  이 중 하나라도 없는 소재는 응답에서 제외되므로 네이티브 응답률에 영향이 있습니다.
- **아이콘은 선택 항목**이라 광고에 따라 없을 수 있습니다. 렌더링 시 null 확인이 필요합니다.
- **별점은 제공하지 않습니다.** Exelbid 응답에 평점 데이터가 없어 매핑하지 않습니다.
- **광고정보(AdChoices) 아이콘**은 서버가 관련 정보를 내려주는 경우에만 표시됩니다.

네이티브 렌더링 예제입니다.

```java
// import com.google.android.gms.ads.nativead.NativeAd;
// import com.google.android.gms.ads.nativead.NativeAdView;

NativeAdView adView = (NativeAdView) getLayoutInflater()
        .inflate(R.layout.my_native_ad, container, false);

adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
adView.setIconView(adView.findViewById(R.id.ad_app_icon));
// ... 나머지 애셋 뷰 등록

((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

// 아이콘은 없을 수 있으므로 반드시 확인합니다
ImageView iconView = (ImageView) adView.getIconView();
NativeAd.Image icon = nativeAd.getIcon();
if (icon != null && icon.getDrawable() != null) {
    iconView.setVisibility(View.VISIBLE);
    iconView.setImageDrawable(icon.getDrawable());
} else {
    iconView.setVisibility(View.GONE);
}

adView.setNativeAd(nativeAd);
```

### 배너 자동 갱신

미디에이션 환경에서는 AdMob이 갱신 주기를 관리합니다.
Exelbid SDK 자체 갱신은 어댑터에서 자동으로 비활성화되므로 별도 설정이 필요하지 않습니다.

### 아동 대상 설정

매체가 AdMob에 설정한 아동 대상(COPPA) 및 연령 제한 신호는 Exelbid 요청에 자동으로 전달됩니다.
별도 설정이 필요하지 않습니다.

---

## 참고

- [Exelbid SDK 기본 가이드](./README.md)
- [Google AdMob 광고 연동 가이드](https://developers.google.com/admob/android/quick-start?hl=ko)
- [Google 맞춤 이벤트 문서](https://developers.google.com/admob/android/custom-events/setup)
- [AdMob 맞춤 이벤트 설정 도움말](https://support.google.com/admob/answer/13407144)

문의 사항은 담당자에게 연락 주시기 바랍니다.
