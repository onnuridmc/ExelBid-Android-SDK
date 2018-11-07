package com.onnuridmc.sample.dialog;

import android.content.Context;
import android.view.View;

import com.onnuridmc.exelbid.common.ExelBidNativeDialog;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.sample.R;

/**
 * Created by Administrator on 2016-08-19.
 */

public class AdNativeRoundDialog2 extends ExelBidNativeDialog {

    private View mCancel;
    private View mOk;

    public AdNativeRoundDialog2(Context context, String adUnitId) {
        super(context, adUnitId);
    }

    @Override
    protected void onCreate() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_round_native_layout2);
        mOk = findViewById(R.id.btn_ok);
        mCancel = findViewById(R.id.btn_cancel);

        setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
        //기본값 셋팅

//        // 광고 이미지를 조작할 일이 있을경우에 셋팅합니다.
//        setNativeImageController(new NativeImageControllor() {
//            @Override
//            public Bitmap mainImageDisplay(Bitmap bitmap, int width, int height) {
//
//
//                return Utils.getRoundedCornerBitmap(getContext(), bitmap, 20, width, height, true, true, true, true);
//            }
//
//            @Override
//            public Bitmap iconImageDisplay(Bitmap bitmap, int width, int height) {
//                return Utils.getRoundedCornerBitmap(getContext(), bitmap, 10, width, height, false, false, false, false);
//            }
//        });

    }

    @Override
    protected NativeViewBinder getNativeViewBinder() {
        //네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅합니다.
        // Builder의 생성자에 바인딩 될 뷰와 각각의 항목을 넘기면 bindNativeAdData가 호출 될때
        // 광고가 바인딩 됩니다.
        // 바인딩 되지 않아도 되는 항목이 있을시 builder에 id를 셋팅하지 않으면 됩니다.
        return new NativeViewBinder.Builder(findViewById(R.id.ad_cardLayout))
                .mainImageId(R.id.ad_image)
                .callToActionButtonId(R.id.ad_cta)
                .titleTextViewId(R.id.ad_title)
                .textTextViewId(R.id.ad_subtitle)
                .iconImageId(R.id.ad_icon)
                .adInfoImageId(R.id.ad_bg)
                .build();
    }

    public void setOnOkClickListener(View.OnClickListener listener) {
        mOk.setOnClickListener(listener);
    }

    public void setOnCancelClickListener(View.OnClickListener listener) {
        mCancel.setOnClickListener(listener);
    }

}
