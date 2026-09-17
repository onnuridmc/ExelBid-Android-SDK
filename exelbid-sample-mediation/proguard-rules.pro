# Add project specific ProGuard rules here.
# Optimizations and keep rules for ExelBid SDK sample app

# Keep line numbers and source file names for debugging
-keepattributes SourceFile,LineNumberTable,InnerClasses,Signature,*Annotation*

# Generic Android optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# ExelBid SDK
-keep class com.onnuridmc.exelbid.** { *; }

# Google Ads SDK
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient { public *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info { public *; }
-keep class com.google.android.gms.common.api.GoogleApiClient { public *; }
-keep class com.google.android.gms.common.api.GoogleApiClient$* { public *; }
-keep class com.google.android.gms.location.LocationServices { public *; }
-keep class com.google.android.gms.location.FusedLocationProviderApi { public *; }

# Facebook Audience Network
-keep class com.facebook.ads.** { *; }
-keeppackagenames com.facebook.*
-keep class com.facebook.** { *; }

# Kakao AdFit
-keep class com.kakao.adfit.** { *; }

# AppLovin
-keep class com.applovin.** { *; }
-keepclassmembers class com.applovin.** { *; }

# Pangle
-keep class com.bytedance.sdk.** { *; }
-keep class com.pgl.** { *; }

# TNK Factory
-keep class com.tnkfactory.** { *; }

# Digital Turbine (Fyber)
-keep class com.fyber.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# ExoPlayer
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**

# WebView JavaScript Interface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Serializable classes
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
