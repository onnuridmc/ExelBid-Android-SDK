package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.onnuridmc.sample.dialog.AdNativeRoundDialog2;
import com.onnuridmc.sample.utils.PrefManager;

/**
 * 다이얼로그 형태의 광고를 노출할경우 사용
 */
public class SampleDialog extends Activity {

    private String mInterstitialUnitId;
    private EditText mEdtInterstitial;
    private AdInterstitialDialog2 mInterstitialDialog;

    private String mNativeUnitId;
    private EditText mEdtNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }


        mInterstitialUnitId = PrefManager.getInterstialAd(this, PrefManager.KEY_DIALOG_INTERSTIAL_AD, AppConstants.UNIT_ID_INTERSTITIAL);
        mEdtInterstitial = (EditText)findViewById(R.id.dialog_edtInterstitial);
        mEdtInterstitial.setText(mInterstitialUnitId);
        initInterstitial();

        mNativeUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_DIALOG_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);
        mEdtNative = (EditText)findViewById(R.id.dialog_edtNative);
        mEdtNative.setText(mNativeUnitId);
        initNative1();
        initNative2();
        initNative3();
    }

    private void initInterstitial() {

        final Button btnInterstitialLoad = (Button)findViewById(R.id.dialog_btnInterstitialLoad);
        final Button btnInterstitialShow = (Button)findViewById(R.id.dialog_btnInterstitialShow);
        final CheckBox isTestCheckBoxInterstitial = (CheckBox) findViewById(R.id.interstitial_test_check);
        final Spinner sampleSpinner = (Spinner) findViewById(R.id.spinner_sample);

        btnInterstitialLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mInterstitialDialog.setTestMode(isTestCheckBoxInterstitial.isChecked());

                mInterstitialDialog.loadAd();

                //키보드 숨기기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtInterstitial.getWindowToken(), 0);

            }
        });
        btnInterstitialShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // mInterstitialDialog.isReady(60 * 30) 와 같이 사용하여 로드 시간 체크를 할 수 있다
                if(mInterstitialDialog.isReady()) {
                    mInterstitialDialog.show();
                }

                // 30분이 지났는지 여부 체크
                if(mInterstitialDialog.isReady(60 * 30)) {
                    mInterstitialDialog.show();
                }
            }
        });
        btnInterstitialShow.setEnabled(false);

        mInterstitialDialog = new AdInterstitialDialog2(SampleDialog.this, mInterstitialUnitId);

        //전면 다이얼로그
        ArrayAdapter sampleAdapter = ArrayAdapter.createFromResource(this, R.array.selected, android.R.layout.simple_spinner_item);
        sampleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sampleSpinner.setAdapter(sampleAdapter);
        sampleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mInterstitialDialog.setSample(position);
                findViewById(R.id.dialog_btnInterstitialShow).setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mInterstitialDialog.setInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialLoaded() {
                btnInterstitialShow.setEnabled(true);
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
                btnInterstitialShow.setEnabled(false);
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
    }

    private void initNative1() {

        final Button btnNativeLoad = (Button)findViewById(R.id.dialog_btnNativeLoad);
        final Button btnNativeShow = (Button)findViewById(R.id.dialog_btnNativeShow);
        final CheckBox isTestCheckBoxNative = (CheckBox) findViewById(R.id.native_test_check);

        //네이티브 다이얼로그
        final AdNativeDialog nativeDialog = new AdNativeDialog(SampleDialog.this, mNativeUnitId);

        btnNativeLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ExelbidNativeDialog를 상속 받아서 네이티브 다이얼로그 디자인을 변경할수 있다.
                String unitID = mEdtNative.getText().toString();
                if(TextUtils.isEmpty(unitID)) {
                    return;
                }
                if(!unitID.equals(mNativeUnitId)) {
                    PrefManager.setPref(SampleDialog.this, PrefManager.KEY_DIALOG_NATIVE_AD, unitID);
                }

                mNativeUnitId = unitID;
                nativeDialog.setAdUnitId(mNativeUnitId);
                nativeDialog.setTestMode(isTestCheckBoxNative.isChecked());

                nativeDialog.loadAd();

                //키보드 숨기기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtNative.getWindowToken(), 0);
            }
        });
        btnNativeShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nativeDialog.isReady(60 * 30) 와 같이 사용하여 로드 시간 체크를 할 수 있다
                if(nativeDialog.isReady(60 * 30)) {
                    nativeDialog.show();
                }
            }
        });
        btnNativeShow.setEnabled(false);
        nativeDialog.setAdNativeListener(new OnAdNativeListener() {

            @Override
            public void onFailed(ExelBidError error) {
                btnNativeShow.setEnabled(false);
            }

            @Override
            public void onShow() {

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLoaded() {
                btnNativeShow.setEnabled(true);
            }
        });
        nativeDialog.setTitle("종료");
        nativeDialog.setOnButton1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nativeDialog.dismiss();
                finish();
            }
        });
        nativeDialog.setOnButton2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nativeDialog.dismiss();
            }
        });
        nativeDialog.setYob("1990");
        nativeDialog.setGender(true);
    }

    private void initNative2() {

        final Button btnRoundNativeLoad = (Button)findViewById(R.id.dialog_btnRoundNativeLoad);
        final Button btnRoundNativeShow = (Button)findViewById(R.id.dialog_btnRoundNativeShow);
        final CheckBox isTestCheckBoxRoundNative = (CheckBox) findViewById(R.id.native_round_test_check);
        //네이티브 라운드 다이얼로그
        final AdNativeRoundDialog mNativeRoundDialog = new AdNativeRoundDialog(SampleDialog.this, mNativeUnitId);

        btnRoundNativeLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mNativeRoundDialog.setTestMode(isTestCheckBoxRoundNative.isChecked());

                mNativeRoundDialog.loadAd();
                //키보드 숨기기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtNative.getWindowToken(), 0);
            }
        });
        btnRoundNativeShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mNativeRoundDialog.isReady(60 * 30) 와 같이 사용하여 로드 시간 체크를 할 수 있다
                if(mNativeRoundDialog.isReady(60 * 30)) {
                    mNativeRoundDialog.show();
                }
            }
        });

        btnRoundNativeShow.setEnabled(false);

        mNativeRoundDialog.setAdNativeListener(new OnAdNativeListener() {
            @Override
            public void onFailed(ExelBidError error) {
                btnRoundNativeShow.setEnabled(false);
            }

            @Override
            public void onShow() {

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLoaded() {
                btnRoundNativeShow.setEnabled(true);
            }
        });
        mNativeRoundDialog.setYob("1990");
        mNativeRoundDialog.setGender(true);

    }

    private void initNative3() {

        final Button btnRoundNativeLoad = (Button)findViewById(R.id.dialog_btnRoundNativeLoad2);
        final Button btnRoundNativeShow = (Button)findViewById(R.id.dialog_btnRoundNativeShow2);
        final CheckBox isTestCheckBoxRoundNative = (CheckBox) findViewById(R.id.native_round_test_check2);


        //네이티브 라운드 다이얼로그
        final AdNativeRoundDialog2 nativeRoundDialog = new AdNativeRoundDialog2(SampleDialog.this, mNativeUnitId);

        btnRoundNativeLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ExelbidNativeDialog를 상속 받아서 네이티브 다이얼로그 디자인을 변경할수 있다.
                String unitID = mEdtNative.getText().toString();
                if(TextUtils.isEmpty(unitID)) {
                    return;
                }
                if(!unitID.equals(mNativeUnitId)) {
                    PrefManager.setPref(SampleDialog.this, PrefManager.KEY_DIALOG_NATIVE_AD, unitID);
                }

                mNativeUnitId = unitID;
                nativeRoundDialog.setAdUnitId(mNativeUnitId);
                nativeRoundDialog.setTestMode(isTestCheckBoxRoundNative.isChecked());

                nativeRoundDialog.loadAd();
                //키보드 숨기기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtNative.getWindowToken(), 0);
            }
        });
        btnRoundNativeShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mNativeRoundDialog.isReady(60 * 30) 와 같이 사용하여 로드 시간 체크를 할 수 있다
                if(nativeRoundDialog.isReady(60 * 30)) {
                    nativeRoundDialog.show();
                }
            }
        });

        btnRoundNativeShow.setEnabled(false);

        nativeRoundDialog.setAdNativeListener(new OnAdNativeListener() {
            @Override
            public void onFailed(ExelBidError error) {
                btnRoundNativeShow.setEnabled(false);
            }

            @Override
            public void onShow() {

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLoaded() {
                btnRoundNativeShow.setEnabled(true);
            }
        });
        nativeRoundDialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nativeRoundDialog.dismiss();
                finish();
            }
        });
        nativeRoundDialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nativeRoundDialog.dismiss();
            }
        });
        nativeRoundDialog.setYob("1990");
        nativeRoundDialog.setGender(true);

    }

    @Override
    protected void onDestroy() {
        mInterstitialDialog.destory();
        super.onDestroy();
    }

}
