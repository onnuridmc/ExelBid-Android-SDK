package com.onnuridmc.sample.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.onnuridmc.exelbid.common.ExelBidInterstitialDialog;
import com.onnuridmc.exelbid.common.OnInterstitialAdListener;
import com.onnuridmc.sample.R;

/**
 * Created by Administrator on 2016-08-19.
 */
public class AdInterstitialDialog2 extends ExelBidInterstitialDialog {

    private Button mButton1;
    private Button mButton2;
    private TextView mTitle;

    public AdInterstitialDialog2(Context context, String adUnitId) {
        super(context, adUnitId);
    }

    public AdInterstitialDialog2(Context context, String adUnitId, OnInterstitialAdListener listener) {
        super(context, adUnitId, listener);
    }

    public AdInterstitialDialog2(Context context, int theme, String adUnitId, OnInterstitialAdListener listener) {
        super(context, theme, adUnitId, listener);
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.dialog_interstitial_layout);

        mButton1 = (Button) findViewById(R.id.dialog_button1);
        mButton2 = (Button) findViewById(R.id.dialog_button2);
        mTitle = (TextView) findViewById(R.id.dialog_title);

//        mButton1.setVisibility(View.GONE);
//        mButton2.setVisibility(View.GONE);
//        mTitle.setVisibility(View.GONE);
    }

    @Override
    public ViewGroup getAdBindLayout() {
        //전면 광고가 노출될 뷰를 정의
        return (ViewGroup) findViewById(R.id.dialog_bodylayout);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getText(titleId));
    }

    public void setOnButton1ClickListener(View.OnClickListener listener) {
        mButton1.setOnClickListener(listener);
    }

    public void setOnButton2ClickListener(View.OnClickListener listener) {
        mButton2.setOnClickListener(listener);
    }

    public void setSample(int position) {
        int layout = 0;
        switch (position)
        {
            case 0:
                layout = R.layout.dialog_interstitial_layout2;
                break;
            case 1:
                layout = R.layout.dialog_interstitial_layout3;
                break;
            case 2:
                layout = R.layout.dialog_interstitial_layout4;
                break;
            case 3:
                layout = R.layout.dialog_interstitial_layout5;
                break;
            case 4:
                layout = R.layout.dialog_interstitial_layout6;
                break;
            case 5:
                layout = R.layout.dialog_interstitial_layout7;
                break;
            case 6:
                layout = R.layout.dialog_interstitial_layout8;
                break;
            case 7:
                layout = R.layout.dialog_interstitial_layout2;
                break;
            default:
                layout = R.layout.dialog_interstitial_layout;
                break;
        }
        setContentView(layout);

        mButton1 = (Button) findViewById(R.id.dialog_button1);
        mButton2 = (Button) findViewById(R.id.dialog_button2);

    }
}
