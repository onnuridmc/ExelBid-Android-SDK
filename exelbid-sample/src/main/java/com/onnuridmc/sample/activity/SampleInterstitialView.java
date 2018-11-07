package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.onnuridmc.exelbid.ExelBidInterstitial;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnInterstitialAdListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

public class SampleInterstitialView extends Activity {

    private ExelBidInterstitial mInterstitialAd;

    private String mUnitId;

    private EditText mEdtAdUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_interstitialview);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }

        mEdtAdUnit = (EditText) findViewById(R.id.editText);

        CheckBox isTestCheckBox = (CheckBox) findViewById(R.id.test_check);
        isTestCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //TEST MODE 여부
                mInterstitialAd.setTestMode(isChecked);
            }
        });
        mUnitId = PrefManager.getInterstialAd(this, PrefManager.KEY_INTERSTIAL_AD, AppConstants.UNIT_ID_INTERSTITIAL);

        mEdtAdUnit.setText(mUnitId);

        mInterstitialAd = new ExelBidInterstitial(this, mUnitId);
        mInterstitialAd.setInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialLoaded() {
                findViewById(R.id.interstitial_show).setEnabled(true);
            }

            @Override
            public void onInterstitialShow() {

            }

            @Override
            public void onInterstitialDismiss() {

            }

            @Override
            public void onInterstitialClicked() {

            }

            @Override
            public void onInterstitialFailed(ExelBidError errorCode) {
                findViewById(R.id.interstitial_show).setEnabled(false);
            }
        });


        mInterstitialAd.setYob("1990");
        mInterstitialAd.setGender(true);
        mInterstitialAd.addKeyword("level", "10");
        mInterstitialAd.setTestMode(isTestCheckBox.isChecked());

        findViewById(R.id.interstitial_show).setEnabled(false);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.interstitial_load) {

            String unitID = mEdtAdUnit.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            mInterstitialAd.setAdUnitId(unitID);
            if(!unitID.equals(mUnitId)) {
                PrefManager.setPref(this, PrefManager.KEY_INTERSTIAL_AD, unitID);
            }

            mUnitId = unitID;

            if(mInterstitialAd != null) {
                mInterstitialAd.load();

                //키보드 숨기기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtAdUnit.getWindowToken(), 0);
            }
        } else if(v.getId() == R.id.interstitial_show) {
            if(mInterstitialAd != null) {

                // mInterstitialAd.isReady(60 * 30) 와 같이 사용하여 로드 시간 체크를 할 수 있다
                if(mInterstitialAd.isReady())
                    mInterstitialAd.show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mInterstitialAd != null) {
            mInterstitialAd.destroy();
        }
        super.onDestroy();
    }
}
