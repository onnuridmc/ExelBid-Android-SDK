# Motiv Partners 샘플 R8/ProGuard 규칙

# Keep line numbers and source file names for debugging
-keepattributes SourceFile,LineNumberTable,InnerClasses,Signature,*Annotation*

# Generic Android optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# ExelBid SDK
-keep class com.onnuridmc.exelbid.** { *; }
