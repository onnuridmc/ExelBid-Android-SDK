package com.onnuridmc.sample.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeListener;
import com.onnuridmc.exelbid.common.ExelBidNativeDialog;
import com.onnuridmc.sample.R;

/**
 * Created by Administrator on 2016-08-19.
 */

public class AdNativeDialog extends ExelBidNativeDialog {

    private Button mButton1;
    private Button mButton2;
    private TextView mTitle;

    public AdNativeDialog(Context context, String adUnitId) {
        super(context, adUnitId);
    }

    public AdNativeDialog(Context context, String adUnitId, OnAdNativeListener listener) {
        super(context,  adUnitId, listener);
    }

    public AdNativeDialog(Context context, int theme, String adUnitId, OnAdNativeListener listener) {
        super(context, theme, adUnitId, listener);
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.dialog_native_layout);

        mButton1 = (Button) findViewById(R.id.dialog_button1);
        mButton2 = (Button) findViewById(R.id.dialog_button2);
        mTitle = (TextView) findViewById(R.id.dialog_title);

    }

    @Override
    protected NativeViewBinder getNativeViewBinder() {
        //네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅합니다.
        // Builder의 생성자에 바인딩 될 뷰와 각각의 항목을 넘기면 bindNativeAdData가 호출 될때
        // 광고가 바인딩 됩니다.
        // 바인딩 되지 않아도 되는 항목이 있을시 builder에 id를 셋팅하지 않으면 됩니다.
        return new NativeViewBinder.Builder(findViewById(R.id.dialog_native_layout))
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .build();
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
