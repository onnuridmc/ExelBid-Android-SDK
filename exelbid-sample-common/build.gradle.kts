plugins {
    id("com.android.library")
}

android {
    compileSdk = 34
    namespace = "com.onnuridmc.sample"

    defaultConfig {
        minSdk = 24
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.11.0")

    // ExelBid SDK — 공용 다이얼로그가 ExelBidInterstitialDialog/ExelBidNativeDialog를 상속
    implementation("com.onnuridmc.exelbid:exelbid:2.1.0")
}
