package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onnuridmc.exelbid.ExelBidAdView;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnBannerAdListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

public class SampleBannerView extends Activity {
    ExelBidAdView mAdView;
    EditText mEdtAdUnit;
    CheckBox mIsTestCheckBox;

    private String mUnitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bannerview);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }

        mEdtAdUnit = (EditText) findViewById(R.id.banner_adUnit);
        mAdView = (ExelBidAdView) findViewById(R.id.adview);
        mIsTestCheckBox = (CheckBox) findViewById(R.id.test_check);
        mIsTestCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //TEST MODE 여부
                mAdView.setTestMode(isChecked);
            }
        });

        mUnitId = PrefManager.getPref(this, PrefManager.KEY_BANNER_AD, AppConstants.UNIT_ID_BANNER);
        mEdtAdUnit.setText(mUnitId);

        //TEST MODE 여부
        mAdView.setTestMode(mIsTestCheckBox.isChecked());
//        mAdView.setVisibility(View.GONE);

        mAdView.setYob("1990");
        //남자면 true 여자면 false
        mAdView.setGender(true);
        mAdView.addKeyword("level", "10");
        mAdView.setCoppa(true);
//        mAdView.setAutoreflashDisable();

        mAdView.setAdListener(new OnBannerAdListener() {

            @Override
            public void onAdLoaded() {
                Log.e(getClass().getName(), "onAdLoaded ");
            }

            @Override
            public void onAdClicked() {
                Log.e(getClass().getName(), "onAdClicked ");
            }

            @Override
            public void onAdFailed(ExelBidError errorCode, int statusCode) {
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
        if(v.getId() == R.id.banner_load) {
            String unitID = mEdtAdUnit.getText().toString();
            if (TextUtils.isEmpty(unitID)) {
                return;
            }
            mAdView.setAdUnitId(unitID);
            if (!unitID.equals(mUnitId)) {
                PrefManager.setPref(this, PrefManager.KEY_BANNER_AD, unitID);
            }

            mUnitId = unitID;
            mAdView.loadAd();


            //키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtAdUnit.getWindowToken(), 0);
        } else if(v.getId() == R.id.banner_show) {
            if(mAdView.getVisibility() == View.VISIBLE)
            {
                mAdView.setVisibility(View.GONE);
            } else if(mAdView.getVisibility() == View.GONE) {
                mAdView.setVisibility(View.VISIBLE);
            }
        }
    }


}
