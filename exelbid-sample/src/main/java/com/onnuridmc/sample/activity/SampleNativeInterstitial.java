package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.onnuridmc.exelbid.ExelBidNative;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnInterstitialAdListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

/**
 * 전면 네이티브 광고 샘플입니다.
 */
public class SampleNativeInterstitial extends Activity implements View.OnClickListener{

    private static final String TAG = "SampleNativeInterstitial";

    private ExelBidNative mNativeAd;


    private String mUnitId;
    private EditText mEdtAdUnit;
    private CheckBox mIsTestCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_instl);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }
        mIsTestCheckBox = (CheckBox) findViewById(R.id.test_check);

        mEdtAdUnit = (EditText)findViewById(R.id.editText);
        mUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);
        mEdtAdUnit.setText(mUnitId);

        // 네이티브 요청 객체를 생성한다.
        mNativeAd = new ExelBidNative(this, mUnitId, new OnInterstitialAdListener() {

            @Override
            public void onInterstitialLoaded() {
                Log.d(TAG, "onLoaded");
                mNativeAd.show();
            }

            @Override
            public void onInterstitialShow() {

            }

            @Override
            public void onInterstitialDismiss() {

            }

            @Override
            public void onInterstitialClicked() {
                Log.d(TAG, "onClick");
            }

            @Override
            public void onInterstitialFailed(ExelBidError errorCode, int statusCode) {
                Log.d(TAG, "onFailed" + errorCode.toString());
            }
        });
        mNativeAd.setTimer(10);

        // 전면 네이티브 구성시 레이아웃의 ID를 넘긴다
        mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(R.layout.sample_native_instl_layout)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());

        findViewById(R.id.button).setOnClickListener(this);

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

        mNativeAd.setYob("1990");
        mNativeAd.setGender(true);
        mNativeAd.addKeyword("level", "10");
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
