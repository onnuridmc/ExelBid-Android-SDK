repositories {
    google()
    mavenCentral()
    maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    maven { url = uri("https://repository.tnkad.net:8443/repository/public/") }
    maven { url = uri("https://artifact.bytedance.com/repository/pangle") }
    flatDir {
        dirs("../libs")
    }
}

plugins {
    id("com.android.application")
}

android {
    compileSdk = 34
    namespace = "com.onnuridmc.sample"

    defaultConfig {
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = findProperty("version")?.toString() ?: "1.0"
        multiDexEnabled = true
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src/main/java")
            resources.srcDirs("src/main/java")
            aidl.srcDirs("src/main")
            renderscript.srcDirs("src/main")
            res.srcDirs("res")
            assets.srcDirs("assets")
        }
    }
}

val exoplayerVersion = "1.2.0"
val adsVersion = "23.4.0"

dependencies {
    // Core Android libraries
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")
    
    // ExelBid SDK
    implementation("com.onnuridmc.exelbid:exelbid:2.0.1")
    
    // Media Player
    implementation("androidx.media3:media3-exoplayer:${exoplayerVersion}")
    implementation("androidx.media3:media3-ui:${exoplayerVersion}")
    implementation("androidx.media3:media3-common:${exoplayerVersion}")
    
    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Ad Networks
    implementation("com.google.android.gms:play-services-ads:$adsVersion")
    implementation("com.facebook.android:audience-network-sdk:6.20.0")
    implementation("com.kakao.adfit:ads-base:3.12.9")
    implementation("com.fyber:marketplace-sdk:8.3.7")
    implementation("com.pangle.global:ads-sdk:5.8.0.5")
    implementation("com.applovin:applovin-sdk:12.6.0")
    implementation("com.tnkfactory:pub:7.21.9")
    
    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}