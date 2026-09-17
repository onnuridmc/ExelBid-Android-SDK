// 서드파티 광고 SDK 전용 저장소 — 이 모듈에만 선언해 저장소 장애가
// 다른 샘플 모듈 빌드에 번지지 않도록 격리한다.
repositories {
    google()
    mavenCentral()
    maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    maven { url = uri("https://repository.tnkad.net:8443/repository/public/") }
    maven { url = uri("https://artifact.bytedance.com/repository/pangle") }
}

plugins {
    id("com.android.application")
}

android {
    compileSdk = 34
    namespace = "com.onnuridmc.sample.mediation"

    defaultConfig {
        applicationId = "com.onnuridmc.sample.mediation"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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

val adsVersion = "23.4.0"

dependencies {
    implementation(project(":exelbid-sample-common"))

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ExelBid SDK
    implementation("com.onnuridmc.exelbid:exelbid:2.1.0")

    // Pangle 네이티브 아이콘 로딩에 사용
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // ExelBid 미디에이션 대상 광고 네트워크 SDK 7종
    implementation("com.google.android.gms:play-services-ads:$adsVersion")
    implementation("com.facebook.android:audience-network-sdk:6.20.0")
    implementation("com.kakao.adfit:ads-base:3.12.9")
    implementation("com.fyber:marketplace-sdk:8.3.7")
    implementation("com.pangle.global:ads-sdk:5.8.0.5")
    implementation("com.applovin:applovin-sdk:12.6.0")
    // 7.21.9는 TNK 저장소에 존재하지 않음(7.21.x 라인은 7.21.4까지)
    implementation("com.tnkfactory:pub:7.21.4")
}
