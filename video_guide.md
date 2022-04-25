# Video 소재 연동 Guide

- Exelbid Android SDK를 이용하여 비디오광고 노출을 위해서는 아래와 같은 준비가 필요.
- 비디오 광고 노출 방식은 현재 두가지 형태로 정의
    1. 전면 비디오 - 기존 전면 배너와 마찬가지의 지면 형태
    2. 네이티브 비디오 - 기존 네이티브 형식에 MainImage Asset 대시 Video Asset을 대신 활용

- Video 지원은 <b>Exelbid SDK v1.7.3</b> 이상 버전을 사용
## ExelBid-Android-SDK 광고 기본 연동
 ExelBid-Android-SDK 연동 가이드를 통한 기본 연동 
- [ExelBid-Android-SDK 연동 가이드 참조](https://github.com/onnuridmc/ExelBid-Android-SDK)
- 비디오 광고 소재 지원은 <b><I>Android Exelbid SDK v1.7.3</I></b> 부터 지원

## build.gradle 에 비디오 컨포넌트 라이브러리 종속성 추가 (비디오 광고 공통)
- Exelbid에서는 비디오 플레이어를 ExoPlayer2 기반으로 동작 적용된다.
- 미 적용시 Exception발생 혹은 광고 노출 되지 않음
```java
def exoplayer_version = '2.13.3'
dependencies {
    implementation "com.google.android.exoplayer:exoplayer-core:$exoplayer_version"
    implementation "com.google.android.exoplayer:exoplayer-ui:$exoplayer_version"
}
```

## minSdkVersion 24 미만 버전 (비디오 광고 공통)
- minSdkVersion 24 미만 버전에서는 gradle.properties 에 아래와 같이 적용 필요 (Gradle 플러그인 버그로 인한)
```java
    android.enableDexingArtifactTransform=false
```


## 전면 비디오
>전면 Video 광고 노출시 적용 - 기존 전면 노출시 사용되는 ExelbidActivity 대신 비디오 노출을 처리하는 VideoPlayerActivity를 manifest에 등록
### AndroidManifest 설정


```xml
<activity android:name="com.onnuridmc.exelbid.lib.vast.VideoPlayerActivity"
          android:configChanges="keyboardHidden|orientation|screenSize">
</activity>
```

## 네이티브 비디오
> 메인 이미지 대신 비디오를 노출 한다.

- 레이아웃 구성시 com.onnuridmc.exelbid.lib.vast.NativeVideoView로 비디오뷰를 추가 적용한다.
- [Sample NativeVideo Layout Link](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/res/layout/act_native.xml)
```xml
        ...
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <ImageView
                android:id="@+id/native_icon_image".../>

            <TextView
                android:id="@+id/native_title".../>

            <ImageView
                android:id="@+id/native_privacy_information_icon_image" .../>
        </LinearLayout>

        <TextView
            android:id="@+id/native_text" ... />
        <RelativeLayout
            ...>

            <ImageView
                android:id="@+id/native_main_image"
                ... />

            <com.onnuridmc.exelbid.lib.vast.NativeVideoView
                android:id="@+id/native_video"
                android:layout_width="match_parent"
                android:layout_height="250dp"
                android:layout_gravity="center_horizontal"
                />

        </RelativeLayout>
        ...
```
- NativeViewBinder 객체 생성시 mediaViewId를 통해 위의 NativeVideoView의 리소스 ID를 추가한다.
- [SampleNativeVideo.java Link](https://github.com/onnuridmc/ExelBid-Android-SDK/blob/master/exelbid-sample/src/main/java/com/onnuridmc/sample/activity/SampleNativeVideo.java)
- 주의) mediaViewId로 NativeVideoView 설정시 com.google.android.exoplayer~ 라이브러리 종속성 설정이 없다면 Exception 발생
```java
        mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(mNativeRootLayout)
                .mainImageId(R.id.native_main_image)
                .mediaViewId(R.id.native_video)     // 비디오뷰를 추가한다.
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());

```
- 필요하다면 요청시 Video Asset을 필수 지정한다.
```
        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 
        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.VIDEO, NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
```        
- Native 특성상 필수로 지정하지 않은(Optional) asset도 응답 될 수 있다. 해당 경우 레이아웃에 준비되지 않은 asset은 무시 됨