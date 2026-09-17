// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val agpVersion = "8.2.2"
    val kotlinVersion = "1.9.22"
    
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // 서드파티 광고 SDK 전용 저장소(kakao/tnk/pangle)는 장애 격리를 위해
        // exelbid-sample-mediation 모듈에만 선언한다.
    }
} 