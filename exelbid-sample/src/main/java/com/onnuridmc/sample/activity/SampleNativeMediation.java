package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.adfit.ads.na.AdFitAdInfoIconPosition;
import com.kakao.adfit.ads.na.AdFitMediaView;
import com.kakao.adfit.ads.na.AdFitNativeAdLayout;
import com.kakao.adfit.ads.na.AdFitNativeAdView;
import com.onnuridmc.exelbid.ExelBidNative;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeListener;
import com.onnuridmc.exelbid.lib.ads.mediation.MediationType;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

/**
 * 네이티브 광고를 하나씩 요청하는 샘플입니다.
 */
public class SampleNativeMediation extends Activity implements View.OnClickListener{

    private static final String TAG = "SampleNativeMediation";

    private ExelBidNative mNativeAd;

    private View mNativeRootLayout;

    private String mUnitId;
    private EditText mEdtAdUnit;
    private CheckBox mIsTestCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_mediation);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }
        mIsTestCheckBox = (CheckBox) findViewById(R.id.test_check);

        mEdtAdUnit = (EditText)findViewById(R.id.editText);
        mUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);
        mEdtAdUnit.setText(mUnitId);

        // 네이티브 요청 객체를 생성한다.
        mNativeAd = new ExelBidNative(this, mUnitId, new OnAdNativeListener() {

            @Override
            public void onFailed(ExelBidError error) {
                Log.d(TAG, "onFailed" + error.toString());
            }

            @Override
            public void onShow() {
                Log.d(TAG, "onShow");
            }

            @Override
            public void onClick() {
                Log.d(TAG, "onClick");
            }

            @Override
            public void onLoaded() {
                Log.d(TAG, "onLoaded");
                mNativeRootLayout.setVisibility(View.VISIBLE);
                mNativeAd.show();
            }
        });

        /**
         * 네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅합니다.
         * Builder의 생성자에 바인딩 될 뷰와 각각의 항목을 넘기면 bindNativeAdData가 호출 될때 광고가 바인딩 됩니다.
         * 바인딩 되지 않아도 되는 항목이 있을시 builder에 id를 셋팅하지 않으면 됩니다.
         **/

        mNativeRootLayout = findViewById(R.id.native_ad_root_layout);
        mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(findViewById(R.id.default_native_layout))
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());

        /********* Adfit 미디에이션 추가시 적용 *********/

        // Mediation Root View
        AdFitNativeAdView adFitNativeAdView = findViewById(R.id.adfit_native_layout);

        // 광고 SDK에 넘겨줄 Mediation[AdFitNativeAdLayout] 정보 구성
        AdFitNativeAdLayout adfitAdLayout = new AdFitNativeAdLayout.Builder(adFitNativeAdView)
                .setTitleView(findViewById(R.id.native_title2)) // 광고 타이틀 문구 (필수)
                .setBodyView(findViewById(R.id.native_text2)) // 광고 본문 텍스트
                .setProfileIconView( findViewById(R.id.native_icon_image2)) // 광고주(브랜드) 이름
//                        .setProfileNameView(findViewById(R.id.native_icon_image2)) // // 광고주 아이콘 (브랜드 로고)
                .setMediaView(findViewById(R.id.native_main_image2)) // 광고 이미지 소재 또는 비디오 소재 (필수)
                .setCallToActionButton(findViewById(R.id.native_cta2)) // 행동 유도 버튼 (ex. 알아보기, 바로가기 등)
                .build();

        /**
         * MediationType.ADFIT 미디에이션 타입
         * adFitNativeAdView 광고 로드에 따라 VISIBLE, GONE이 된다. 해당 미디에이션의 Root View를 전달한다.
         * adfitAdLayout 미디에이션 광고의 데이터가 바이딩을 담당할 Object를 전달한다.
         *               ex) Adfit의 AdFitNativeAdLayout
         * AdFitAdInfoIconPosition.RIGHT_TOP 광고 정보 아이콘 위치를 설정
         */
        mNativeAd.addMediationViewBinder(MediationType.ADFIT, adFitNativeAdView, adfitAdLayout, AdFitAdInfoIconPosition.RIGHT_TOP);

        /********* Adfit 미디에이션 추가시 적용 마지막 *********/

        findViewById(R.id.button).setOnClickListener(this);

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

        mNativeAd.setYob("1990");
        mNativeAd.setGender(true);
        mNativeAd.addKeyword("level", "10");

        mNativeRootLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {

            String unitID = mEdtAdUnit.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            mNativeAd.setTestMode(mIsTestCheckBox.isChecked());
            mNativeAd.setAdUnitId(unitID);

            if(!unitID.equals(mUnitId)) {
                PrefManager.setPref(this, PrefManager.KEY_NATIVE_AD, unitID);
            }

            mUnitId = unitID;

            //광고를 요청한다.
            mNativeAd.loadAd();

            //키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtAdUnit.getWindowToken(), 0);
        }
    }
}
