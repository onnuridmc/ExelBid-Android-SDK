package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.onnuridmc.exelbid.ExelBidNative;
import com.onnuridmc.exelbid.ExelBidNativeMulti;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeMultiListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.model.DummyData;
import com.onnuridmc.sample.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 여러개의 네이티브 광고를 요청하는 샘플입니다.
 */
public class SampleNativeMulti extends Activity {

    private ExelBidNativeMulti mNativeMulti;

    private static final String NATIVE_UNIT_ID_A = "4b0c32679086ecd5f83f75744f811b0b2145e825";
    private static final String NATIVE_UNIT_ID_B = "b3720be2da2e10438b63af6f4e0ace4881992597";

    private View mNativeAdRootLayoutA;
    private View mNativeAdRootLayoutB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_multi);

        mNativeAdRootLayoutA = findViewById(R.id.native_layout_a);
        mNativeAdRootLayoutB = findViewById(R.id.native_layout_b);

        mNativeMulti = new ExelBidNativeMulti(this);

        mNativeMulti.setOnNativeMultiListener(new OnAdNativeMultiListener() {
            @Override
            public void onFailed(String adUnitId, ExelBidError error) {

            }

            @Override
            public void onShow(String adUnitId) {

            }

            @Override
            public void onClick(String adUnitId) {

            }

            @Override
            public void onLoaded(String adUnitId) {
                if(NATIVE_UNIT_ID_A.equals(adUnitId)) {
                    mNativeAdRootLayoutA.setVisibility(View.VISIBLE);
                    mNativeMulti.show(adUnitId);
                } else if (NATIVE_UNIT_ID_B.equals(adUnitId)) {
                    mNativeAdRootLayoutB.setVisibility(View.VISIBLE);
                    mNativeMulti.show(adUnitId);
                }
            }
        });

        mNativeMulti.addNativeViewBinder(NATIVE_UNIT_ID_A, new NativeViewBinder.Builder(mNativeAdRootLayoutA)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .build());

        mNativeMulti.addNativeViewBinder(NATIVE_UNIT_ID_B, new NativeViewBinder.Builder(mNativeAdRootLayoutB)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .build());

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mNativeMulti.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

        mNativeMulti.setAge(30);
        mNativeMulti.setGender(true);
        mNativeMulti.addKeyword("level", "10");
        mNativeMulti.setTestMode(AppConstants.TEST_MODE);

        mNativeAdRootLayoutA.setVisibility(View.GONE);
        mNativeAdRootLayoutB.setVisibility(View.GONE);

        mNativeMulti.loadAdAll();
//        mNativeMulti.loadAd(NATIVE_UNIT_ID_A);
    }


}
