# Video 소재 연동 Guide

Exelbid SDK로 비디오 광고를 노출하는 방법입니다. 비디오 광고는 두 가지 형태를 지원합니다.

1. **전면 비디오** — 기존 전면 배너와 같은 지면 형태
2. **네이티브 비디오** — 기존 네이티브 형식에서 MainImage Asset 대신 Video Asset을 활용

기본 연동이 선행되어야 합니다 — [기본 가이드](./README.md) 참조.

## 비디오 플레이어 라이브러리 추가 (비디오 광고 공통)

Exelbid SDK의 비디오 플레이어는 **androidx.media3(ExoPlayer)** 기반으로 동작합니다.
SDK가 media3를 포함하지 않으므로(compileOnly 참조) **앱에서 직접 추가해야 하며**,
미적용 시 Exception이 발생하거나 광고가 노출되지 않습니다.

```gradle
dependencies {
    implementation 'androidx.media3:media3-exoplayer:1.2.0'
    implementation 'androidx.media3:media3-ui:1.2.0'
    implementation 'androidx.media3:media3-common:1.2.0'
}
```

> 구버전 SDK(1.x)는 `com.google.android.exoplayer:exoplayer-*:2.x`(ExoPlayer2)를 사용했습니다.
> SDK 2.0.0 이상에서는 위와 같이 media3로 적용합니다.

## 전면 비디오

전면 비디오 노출은 기존 전면에서 사용하는 ExelBidActivity 대신 비디오 노출을 처리하는
`VideoPlayerActivity`가 담당합니다. manifest에 등록합니다.

```xml
<activity android:name="com.onnuridmc.exelbid.lib.vast.VideoPlayerActivity"
          android:configChanges="keyboardHidden|orientation|screenSize">
</activity>
```

광고 요청/노출 코드는 [일반 전면 광고](./ad_guide.md#전면-광고)와 동일합니다.

## 네이티브 비디오

메인 이미지 대신 비디오를 노출합니다.

1. 레이아웃 구성 시 `com.onnuridmc.exelbid.lib.vast.NativeVideoView`로 비디오뷰를 추가합니다.
   - [Sample NativeVideo Layout Link](exelbid-sample-basic/src/main/res/layout/act_native.xml)

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

2. `NativeViewBinder` 객체 생성 시 `mediaViewId`로 위 NativeVideoView의 리소스 ID를 추가합니다.
   - [SampleNativeVideo.java Link](exelbid-sample-basic/src/main/java/com/onnuridmc/sample/activity/SampleNativeVideo.java)
   - 주의) `mediaViewId`로 NativeVideoView 설정 시 media3 라이브러리 종속성이 없다면 Exception이 발생합니다.

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

3. 필요하다면 요청 시 Video Asset을 필수 지정합니다.

    ```java
            // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다.
            mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.VIDEO, NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
    ```

- Native 특성상 필수로 지정하지 않은(Optional) asset도 응답될 수 있습니다. 해당 경우 레이아웃에 준비되지 않은 asset은 무시됩니다.
