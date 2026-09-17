package com.onnuridmc.sample.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.onnuridmc.exelbid.common.OnInterstitialAdListener;
import com.onnuridmc.exelbid.common.ExelBidInterstitialDialog;
import com.onnuridmc.sample.R;

/**
 * Created by Administrator on 2016-08-19.
 */
public class AdInterstitialDialog extends ExelBidInterstitialDialog {

    private Button mButton1;
    private Button mButton2;
    private TextView mTitle;

    public AdInterstitialDialog(Context context, String adUnitId) {
        super(context, adUnitId);
    }

    public AdInterstitialDialog(Context context, String adUnitId, OnInterstitialAdListener listener) {
        super(context, adUnitId, listener);
    }

    public AdInterstitialDialog(Context context, int theme, String adUnitId, OnInterstitialAdListener listener) {
        super(context, theme, adUnitId, listener);
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.dialog_interstitial_layout);

        mButton1 = (Button) findViewById(R.id.dialog_button1);
        mButton2 = (Button) findViewById(R.id.dialog_button2);
        mTitle = (TextView) findViewById(R.id.dialog_title);
    }

    @Override
    public ViewGroup getAdBindLayout() {
        //전면 광고가 노출될 뷰를 정의
        return (ViewGroup) findViewById(R.id.dialog_bodylayout);
    }

    @Override
    public void setTitle(CharSequence title) {
        findViewById(R.id.dialog_title_line).setVisibility(View.VISIBLE);
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
}
