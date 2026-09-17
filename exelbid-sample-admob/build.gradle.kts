plugins {
    id("com.android.application")
}

android {
    compileSdk = 34
    namespace = "com.onnuridmc.sample.admob"

    defaultConfig {
        applicationId = "com.onnuridmc.sample.admob"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    implementation(project(":exelbid-sample-common"))

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ExelBid SDK — 2.1.0부터 AdMob 커스텀 이벤트 어댑터(ExelBidCustomEvent) 포함
    implementation("com.onnuridmc.exelbid:exelbid:2.1.0")

    // GMA 레거시 SDK. Next-Gen(ads-mobile-sdk)과 한 앱에 공존 불가 —
    // Next-Gen 샘플은 exelbid-sample-admob-nextgen 모듈 참조
    implementation("com.google.android.gms:play-services-ads:23.4.0")
}
