package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.onnuridmc.exelbid.ExelBidNative;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeListener;
import com.onnuridmc.exelbid.lib.utils.ExelLog;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

/**
 * 네이티브 광고를 하나씩 요청하는 샘플입니다.
 */
public class SampleNative extends Activity implements View.OnClickListener{

    private static final String TAG = "SampleNativeSingle";

    private ExelBidNative mNativeAd;

    private View mNativeRootLayout;

    private String mUnitId;
    private EditText mEdtAdUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native);

        mEdtAdUnit = (EditText)findViewById(R.id.editText);
        mUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);
        mEdtAdUnit.setText(mUnitId);

        // 네이티브 요청 객체를 생성한다.
        mNativeAd = new ExelBidNative(this, mUnitId, new OnAdNativeListener() {

            @Override
            public void onFailed(ExelBidError error) {
                ExelLog.d(TAG, "onFailed" + error.toString());
            }

            @Override
            public void onShow() {
                ExelLog.d(TAG, "onShow");
            }

            @Override
            public void onClick() {
                ExelLog.d(TAG, "onClick");
            }

            @Override
            public void onLoaded() {
                ExelLog.d(TAG, "onLoaded");
                mNativeRootLayout.setVisibility(View.VISIBLE);
                mNativeAd.show();
            }
        });

        //네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅합니다.
        // Builder의 생성자에 바인딩 될 뷰와 각각의 항목을 넘기면 bindNativeAdData가 호출 될때
        // 광고가 바인딩 됩니다.
        // 바인딩 되지 않아도 되는 항목이 있을시 builder에 id를 셋팅하지 않으면 됩니다.

        mNativeRootLayout = findViewById(R.id.native_layout);
        mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(mNativeRootLayout)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .build());

        findViewById(R.id.button).setOnClickListener(this);

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

        mNativeAd.setYob("1990");
        mNativeAd.setGender(true);
        mNativeAd.addKeyword("level", "10");
        mNativeAd.setTestMode(AppConstants.TEST_MODE);

        mNativeRootLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {

            String unitID = mEdtAdUnit.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }

            mNativeAd.setAdUnitId(unitID);

            if(!unitID.equals(mUnitId)) {
                PrefManager.setPref(this, PrefManager.KEY_NATIVE_AD, unitID);
            }

            mUnitId = unitID;

            //광고를 요청한다.
            mNativeAd.loadAd();
        }
    }
}
