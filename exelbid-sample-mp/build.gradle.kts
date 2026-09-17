plugins {
    id("com.android.application")
}

android {
    compileSdk = 34
    namespace = "com.onnuridmc.sample.mp"

    defaultConfig {
        applicationId = "com.onnuridmc.sample.mp"
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

    // Motiv Partners 연동도 ExelBid SDK 하나로 충분하다 (MPartnersAdView/MPartnersNative)
    implementation("com.onnuridmc.exelbid:exelbid:2.1.0")
}
