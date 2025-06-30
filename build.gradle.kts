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
        mavenCentral()
        maven {
            url = uri("https://artifact.bytedance.com/repository/pangle")
        }
        maven {
            url = uri("https://repository.tnkad.net:8443/repository/public/")
        }
    }
} 