package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnAdNativeListener;
import com.onnuridmc.exelbid.common.OnInterstitialAdListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.dialog.AdInterstitialDialog;
import com.onnuridmc.sample.dialog.AdNativeDialog;
import com.onnuridmc.sample.dialog.AdNativeRoundDialog;
import com.onnuridmc.sample.utils.PrefManager;

/**
 * 다이얼로그 형태의 광고를 노출할경우 사용
 */
public class SampleDialog extends Activity implements View.OnClickListener{

    private EditText mEdtInterstitial;
    private EditText mEdtNative;
    private EditText mEdtRoundNative;

    private String mNativeUnitId;
    private String mInterstitialUnitId;

    private AdNativeDialog mNativeDialog;
    private AdNativeRoundDialog mNativeRoundDialog;
    private AdInterstitialDialog mInterstitialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog);

        mEdtInterstitial = (EditText)findViewById(R.id.dialog_edtInterstitial);
        mEdtNative = (EditText)findViewById(R.id.dialog_edtNative);

        mInterstitialUnitId = PrefManager.getInterstialAd(this, PrefManager.KEY_DIALOG_INTERSTIAL_AD, AppConstants.UNIT_ID_INTERSTITIAL);
        mNativeUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_DIALOG_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);


        mEdtInterstitial.setText(mInterstitialUnitId);
        mEdtNative.setText(mNativeUnitId);


        //전면 다이얼로그
        mInterstitialDialog = new AdInterstitialDialog(SampleDialog.this, mInterstitialUnitId);
        mInterstitialDialog.setInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialLoaded() {
                findViewById(R.id.dialog_btnInterstitialShow).setEnabled(true);
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

            }
        });
        mInterstitialDialog.setOnButton1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialDialog.dismiss();
                finish();
            }
        });
        mInterstitialDialog.setOnButton2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialDialog.dismiss();
            }
        });
        mInterstitialDialog.setTitle("종료");
        mInterstitialDialog.setYob("1990");
        mInterstitialDialog.setGender(true);
        mInterstitialDialog.setTestMode(AppConstants.TEST_MODE);

        //네이티브 다이얼로그
        mNativeDialog = new AdNativeDialog(SampleDialog.this, mNativeUnitId);
        mNativeDialog.setAdNativeListener(new OnAdNativeListener() {

            @Override
            public void onFailed(ExelBidError error) {

            }

            @Override
            public void onShow() {

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLoaded() {
                findViewById(R.id.dialog_btnNativeShow).setEnabled(true);
            }
        });
        mNativeDialog.setTitle("종료");
        mNativeDialog.setOnButton1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNativeDialog.dismiss();
                finish();
            }
        });
        mNativeDialog.setOnButton2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNativeDialog.dismiss();
            }
        });
        mNativeDialog.setYob("1990");
        mNativeDialog.setGender(true);
        mNativeDialog.setTestMode(AppConstants.TEST_MODE);


        //네이티브 라운드 다이얼로그
        mNativeRoundDialog = new AdNativeRoundDialog(SampleDialog.this, mNativeUnitId);
        mNativeRoundDialog.setAdNativeListener(new OnAdNativeListener() {
            @Override
            public void onFailed(ExelBidError error) {

            }

            @Override
            public void onShow() {

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLoaded() {
                findViewById(R.id.dialog_btnRoundNativeShow).setEnabled(true);
            }
        });
        mNativeRoundDialog.setYob("1990");
        mNativeRoundDialog.setGender(true);
        mNativeRoundDialog.setTestMode(AppConstants.TEST_MODE);

        findViewById(R.id.dialog_btnInterstitialShow).setEnabled(false);
        findViewById(R.id.dialog_btnRoundNativeShow).setEnabled(false);
        findViewById(R.id.dialog_btnNativeShow).setEnabled(false);

    }

    @Override
    protected void onDestroy() {
        mInterstitialDialog.destory();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dialog_btnInterstitialLoad) {
            // 전면 다이얼로그 광고
            // EspressoInterstitialDialog 상속받아서 다이얼로그 디자인을 변경할수 있다.
            String unitID = mEdtInterstitial.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            if(!unitID.equals(mInterstitialUnitId)) {
                PrefManager.setPref(SampleDialog.this, PrefManager.KEY_DIALOG_INTERSTIAL_AD, unitID);
            }

            mInterstitialUnitId = unitID;
            mInterstitialDialog.setAdUnitId(mInterstitialUnitId);

            mInterstitialDialog.loadAd();
        } else if (v.getId() == R.id.dialog_btnNativeLoad) {
            //EspressoNativeDialog를 상속 받아서 네이티브 다이얼로그 디자인을 변경할수 있다.
            String unitID = mEdtNative.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            if(!unitID.equals(mNativeUnitId)) {
                PrefManager.setPref(SampleDialog.this, PrefManager.KEY_DIALOG_NATIVE_AD, unitID);
            }

            mNativeUnitId = unitID;
            mNativeDialog.setAdUnitId(mNativeUnitId);

            mNativeDialog.loadAd();
        } else if (v.getId() == R.id.dialog_btnRoundNativeLoad) {
            //EspressoNativeDialog를 상속 받아서 네이티브 다이얼로그 디자인을 변경할수 있다.
            String unitID = mEdtNative.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            if(!unitID.equals(mNativeUnitId)) {
                PrefManager.setPref(SampleDialog.this, PrefManager.KEY_DIALOG_NATIVE_AD, unitID);
            }

            mNativeUnitId = unitID;
            mNativeRoundDialog.setAdUnitId(mNativeUnitId);

            mNativeRoundDialog.loadAd();

        } else if (v.getId() == R.id.dialog_btnInterstitialShow) {
            if(mInterstitialDialog.isReady()) {
                mInterstitialDialog.show();
            }
        } else if (v.getId() == R.id.dialog_btnNativeShow) {
            if(mNativeDialog.isReady()) {
                mNativeDialog.show();
            }
        } else if (v.getId() == R.id.dialog_btnRoundNativeShow) {
            if(mNativeRoundDialog.isReady()) {
                mNativeRoundDialog.show();
            }
        }
    }
}
