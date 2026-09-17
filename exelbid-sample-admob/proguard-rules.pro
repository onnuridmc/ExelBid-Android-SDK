# AdMob 커스텀 이벤트(레거시 GMA) 샘플 R8/ProGuard 규칙

# Keep line numbers and source file names for debugging
-keepattributes SourceFile,LineNumberTable,InnerClasses,Signature,*Annotation*

# Generic Android optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# ExelBid SDK (AdMob 커스텀 이벤트 어댑터 포함)
-keep class com.onnuridmc.exelbid.** { *; }

# Google Mobile Ads SDK
-keep class com.google.android.gms.ads.** { *; }
