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
import com.onnuridmc.exelbid.MPartnersAdView;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.OnBannerAdListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

public class SampleMotivPartnersBannerView extends Activity {
    MPartnersAdView mAdView;
    EditText mEdtChannelId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mp_bannerview);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }

        mEdtChannelId = (EditText) findViewById(R.id.channel_id_et);
        mAdView = (MPartnersAdView) findViewById(R.id.adview);

        mEdtChannelId.setText("exelbiddev");

        mAdView.setAdSize(320, 50);
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
                Toast.makeText(SampleMotivPartnersBannerView.this, errorCode.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }

    public void onClick(View v) {
        if(v.getId() == R.id.mp_banner_load) {
            String channelId = mEdtChannelId.getText().toString();
            if (TextUtils.isEmpty(channelId)) {
                return;
            }
            mAdView.setChannelId(channelId);
            mAdView.loadAd();
            //키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtChannelId.getWindowToken(), 0);
        } else if(v.getId() == R.id.mp_banner_show) {
            if(mAdView.getVisibility() == View.VISIBLE)
            {
                mAdView.setVisibility(View.GONE);
            } else if(mAdView.getVisibility() == View.GONE) {
                mAdView.setVisibility(View.VISIBLE);
            }
        }
    }


}
