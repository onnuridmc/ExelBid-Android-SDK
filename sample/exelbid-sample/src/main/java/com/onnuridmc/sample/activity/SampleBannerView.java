package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.ExelBidAdView;
import com.onnuridmc.exelbid.common.OnBannerAdListener;
import com.onnuridmc.exelbid.lib.utils.ExelLog;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

public class SampleBannerView extends Activity {
    ExelBidAdView mAdView;
    EditText mEdtAdUnit;

    private String mUnitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bannerview);

        mEdtAdUnit = (EditText) findViewById(R.id.banner_adUnit);
        mAdView = (ExelBidAdView) findViewById(R.id.adview);

        mUnitId = PrefManager.getPref(this, PrefManager.KEY_BANNER_AD, AppConstants.UNIT_ID_BANNER);
        mEdtAdUnit.setText(mUnitId);

        //TEST MODE 여부
        mAdView.setTestMode(AppConstants.TEST_MODE);

        mAdView.setYob("1990");
        //남자면 true 여자면 false
        mAdView.setGender(true);
        mAdView.addKeyword("level", "10");
//        mAdView.setAutoreflashDisable();

        mAdView.setAdListener(new OnBannerAdListener() {

            @Override
            public void onAdLoaded() {
                ExelLog.e("dooully", " onAdLoaded ");
            }

            @Override
            public void onAdClicked() {
                ExelLog.e("dooully", " onAdClicked ");
            }

            @Override
            public void onAdFailed(ExelBidError errorCode) {
                Toast.makeText(SampleBannerView.this, errorCode.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }

    public void onClick(View v) {
        String unitID = mEdtAdUnit.getText().toString();
        if(TextUtils.isEmpty(unitID)) {
            return;
        }
        mAdView.setAdUnitId(unitID);
        if(!unitID.equals(mUnitId)) {
            PrefManager.setPref(this, PrefManager.KEY_BANNER_AD, unitID);
        }

        mUnitId = unitID;
        mAdView.loadAd();
    }


}
