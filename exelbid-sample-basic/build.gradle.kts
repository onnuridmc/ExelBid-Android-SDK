plugins {
    id("com.android.application")
}

android {
    compileSdk = 34
    namespace = "com.onnuridmc.sample.basic"

    defaultConfig {
        applicationId = "com.onnuridmc.sample.basic"
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
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ExelBid 기본 연동은 이 의존성 하나로 충분하다
    implementation("com.onnuridmc.exelbid:exelbid:2.1.0")

    // ExelBid 비디오 광고(네이티브 비디오/VAST) 재생용 —
    // SDK가 compileOnly로 참조하므로 비디오 광고를 쓰는 앱이 직접 제공해야 한다
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")
    implementation("androidx.media3:media3-common:1.2.0")
}
