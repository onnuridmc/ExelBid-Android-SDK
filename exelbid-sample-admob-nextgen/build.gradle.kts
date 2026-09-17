plugins {
    id("com.android.application")
}

android {
    namespace = "com.onnuridmc.sample.nextgen"
    // ads-mobile-sdk 1.4.0 요구사항. AGP 8.2.2 공식 지원(34) 초과라
    // 루트 gradle.properties의 android.suppressUnsupportedCompileSdk=35 로 경고를 억제한다.
    compileSdk = 35

    defaultConfig {
        applicationId = "com.onnuridmc.sample.nextgen"
        minSdk = 24          // ads-mobile-sdk 요구 최소 버전
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        abortOnError = false
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")

    // ExelBid SDK — AdMob 커스텀 이벤트 어댑터 포함 (2.1.0+)
    implementation("com.onnuridmc.exelbid:exelbid:2.1.0")

    // GMA Next-Gen SDK. 레거시 play-services-ads 와 공존 불가하므로 이 모듈에는
    // 다른 광고 SDK를 추가하지 않는다. ExelBid SDK 는 play-services-ads 를
    // compileOnly 로만 참조하므로 전이 의존성 충돌이 없다.
    implementation("com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk:1.4.0")
}
