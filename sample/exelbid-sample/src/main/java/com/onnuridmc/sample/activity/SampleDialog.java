package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnAdNativeListener;
import com.onnuridmc.exelbid.common.OnInterstitialAdListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.dialog.AdInterstitialDialog2;
import com.onnuridmc.sample.dialog.AdNativeDialog;
import com.onnuridmc.sample.dialog.AdNativeRoundDialog;
import com.onnuridmc.sample.utils.PrefManager;

/**
 * 다이얼로그 형태의 광고를 노출할경우 사용
 */
public class SampleDialog extends Activity implements View.OnClickListener {

    private EditText mEdtInterstitial;
    private EditText mEdtNative;
    private CheckBox mIsTestCheckBoxInterstitial;

    private String mNativeUnitId;
    private String mInterstitialUnitId;

    private AdNativeDialog mNativeDialog;
    private AdNativeRoundDialog mNativeRoundDialog;
    private AdInterstitialDialog2 mInterstitialDialog;
    private CheckBox mIsTestCheckBoxNative;
    private CheckBox mIsTestCheckBoxNativeDialog;

    private Spinner mSampleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }

        mEdtInterstitial = (EditText)findViewById(R.id.dialog_edtInterstitial);
        mEdtNative = (EditText)findViewById(R.id.dialog_edtNative);
        mIsTestCheckBoxInterstitial = (CheckBox) findViewById(R.id.interstitial_test_check);
        mIsTestCheckBoxNative = (CheckBox) findViewById(R.id.native_test_check);
        mIsTestCheckBoxNativeDialog = (CheckBox) findViewById(R.id.native_round_test_check);

        mInterstitialUnitId = PrefManager.getInterstialAd(this, PrefManager.KEY_DIALOG_INTERSTIAL_AD, AppConstants.UNIT_ID_INTERSTITIAL);
        mNativeUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_DIALOG_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);


        mEdtInterstitial.setText(mInterstitialUnitId);
        mEdtNative.setText(mNativeUnitId);


        //전면 다이얼로그
        mSampleSpinner = (Spinner) findViewById(R.id.spinner_sample);
        ArrayAdapter sampleAdapter = ArrayAdapter.createFromResource(this, R.array.selected, android.R.layout.simple_spinner_item);
        sampleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSampleSpinner.setAdapter(sampleAdapter);
        mSampleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mInterstitialDialog.setSample(position);
                findViewById(R.id.dialog_btnInterstitialShow).setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mInterstitialDialog = new AdInterstitialDialog2(SampleDialog.this, mInterstitialUnitId);
        mInterstitialDialog.setInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialLoaded() {
                findViewById(R.id.dialog_btnInterstitialShow).setEnabled(true);
                mInterstitialDialog.setTitle("종료하시겠습니까?");
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
                findViewById(R.id.dialog_btnInterstitialShow).setEnabled(false);
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
        mInterstitialDialog.setYob("1990");
        mInterstitialDialog.setGender(true);

        //네이티브 다이얼로그
        mNativeDialog = new AdNativeDialog(SampleDialog.this, mNativeUnitId);
        mNativeDialog.setAdNativeListener(new OnAdNativeListener() {

            @Override
            public void onFailed(ExelBidError error) {
                findViewById(R.id.dialog_btnNativeShow).setEnabled(false);
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


        //네이티브 라운드 다이얼로그
        mNativeRoundDialog = new AdNativeRoundDialog(SampleDialog.this, mNativeUnitId);
        mNativeRoundDialog.setAdNativeListener(new OnAdNativeListener() {
            @Override
            public void onFailed(ExelBidError error) {
                findViewById(R.id.dialog_btnRoundNativeShow).setEnabled(false);
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
            // ExelbidInterstitialDialog 상속받아서 다이얼로그 디자인을 변경할수 있다.
            String unitID = mEdtInterstitial.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            if(!unitID.equals(mInterstitialUnitId)) {
                PrefManager.setPref(SampleDialog.this, PrefManager.KEY_DIALOG_INTERSTIAL_AD, unitID);
            }

            mInterstitialUnitId = unitID;
            mInterstitialDialog.setAdUnitId(mInterstitialUnitId);
            mInterstitialDialog.setTestMode(mIsTestCheckBoxInterstitial.isChecked());

            mInterstitialDialog.loadAd();

            //키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtInterstitial.getWindowToken(), 0);
        } else if (v.getId() == R.id.dialog_btnNativeLoad) {
            //ExelbidNativeDialog를 상속 받아서 네이티브 다이얼로그 디자인을 변경할수 있다.
            String unitID = mEdtNative.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            if(!unitID.equals(mNativeUnitId)) {
                PrefManager.setPref(SampleDialog.this, PrefManager.KEY_DIALOG_NATIVE_AD, unitID);
            }

            mNativeUnitId = unitID;
            mNativeDialog.setAdUnitId(mNativeUnitId);
            mNativeDialog.setTestMode(mIsTestCheckBoxNative.isChecked());

            mNativeDialog.loadAd();

            //키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtNative.getWindowToken(), 0);
        } else if (v.getId() == R.id.dialog_btnRoundNativeLoad) {
            // ExelbidNativeDialog를 상속 받아서 네이티브 다이얼로그 디자인을 변경할수 있다.
            String unitID = mEdtNative.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            if(!unitID.equals(mNativeUnitId)) {
                PrefManager.setPref(SampleDialog.this, PrefManager.KEY_DIALOG_NATIVE_AD, unitID);
            }

            mNativeUnitId = unitID;
            mNativeRoundDialog.setAdUnitId(mNativeUnitId);
            mNativeRoundDialog.setTestMode(mIsTestCheckBoxNativeDialog.isChecked());

            mNativeRoundDialog.loadAd();
            //키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtNative.getWindowToken(), 0);

        } else if (v.getId() == R.id.dialog_btnInterstitialShow) {

            // mInterstitialDialog.isReady(60 * 30) 와 같이 사용하여 로드 시간 체크를 할 수 있다
            if(mInterstitialDialog.isReady()) {
                mInterstitialDialog.show();
            }
        } else if (v.getId() == R.id.dialog_btnNativeShow) {
            // mNativeDialog.isReady(60 * 30) 와 같이 사용하여 로드 시간 체크를 할 수 있다
            if(mNativeDialog.isReady()) {
                mNativeDialog.show();
            }
        } else if (v.getId() == R.id.dialog_btnRoundNativeShow) {
            // mNativeRoundDialog.isReady(60 * 30) 와 같이 사용하여 로드 시간 체크를 할 수 있다
            if(mNativeRoundDialog.isReady()) {
                mNativeRoundDialog.show();
            }
        }
    }
}
