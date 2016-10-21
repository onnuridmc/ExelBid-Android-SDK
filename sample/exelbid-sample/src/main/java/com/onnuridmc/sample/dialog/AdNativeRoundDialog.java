package com.onnuridmc.sample.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.RatingBar;

import com.onnuridmc.exelbid.common.ExelBidNativeDialog;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeImageControllor;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeListener;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.Utils;

/**
 * Created by Administrator on 2016-08-19.
 */

public class AdNativeRoundDialog extends ExelBidNativeDialog {

    private View mClosedView;
    private View mAdTextBg;
    private RatingBar mRatingBar;

    public AdNativeRoundDialog(Context context, String adUnitId) {
        super(context, adUnitId);
    }

    public AdNativeRoundDialog(Context context, String adUnitId, OnAdNativeListener listener) {
        super(context,  adUnitId, listener);
    }

    public AdNativeRoundDialog(Context context, int theme, String adUnitId, OnAdNativeListener listener) {
        super(context, theme, adUnitId, listener);
    }

    @Override
    protected void onCreate() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_round_native_layout);
        mClosedView = findViewById(R.id.ad_ivCross);
        mClosedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mRatingBar = (RatingBar) findViewById(R.id.ad_ratingBar);
        LayerDrawable localLayerDrawable = (LayerDrawable)mRatingBar.getProgressDrawable();
        localLayerDrawable.getDrawable(2).setColorFilter(Color.parseColor("#FFD738"), PorterDuff.Mode.SRC_ATOP);
        localLayerDrawable.getDrawable(1).setColorFilter(Color.parseColor("#B7B7B7"), PorterDuff.Mode.SRC_ATOP);
        localLayerDrawable.getDrawable(0).setColorFilter(Color.parseColor("#B7B7B7"), PorterDuff.Mode.SRC_ATOP);


        mAdTextBg = findViewById(R.id.ad_bg);

        // rating을 required로 요청하면 fill이 적게 찰수 있다.
        setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
        //기본값 셋팅
        mRatingBar.setRating(4.0f);

        // 광고 이미지를 조작할 일이 있을경우에 셋팅합니다.
        setNativeImageController(new NativeImageControllor() {
            @Override
            public Bitmap mainImageDisplay(Bitmap bitmap, int width, int height) {

                Bitmap adbg = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.adbg);

                mAdTextBg.setBackgroundDrawable(new BitmapDrawable(getContext().getResources(), Utils.getRoundedAdBitmap(getContext(), adbg, 20, mAdTextBg.getWidth(), mAdTextBg.getHeight(), width, height)));
                mAdTextBg.setVisibility(View.VISIBLE);
                return Utils.getRoundedCornerBitmap(getContext(), bitmap, 20, width, height, false, false, true, true);
            }

            @Override
            public Bitmap iconImageDisplay(Bitmap bitmap, int width, int height) {
                return Utils.getRoundedCornerBitmap(getContext(), bitmap, 10, width, height, false, false, false, false);
            }
        });

    }

    @Override
    protected NativeViewBinder getNativeViewBinder() {
        //네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅합니다.
        // Builder의 생성자에 바인딩 될 뷰와 각각의 항목을 넘기면 bindNativeAdData가 호출 될때
        // 광고가 바인딩 됩니다.
        // 바인딩 되지 않아도 되는 항목이 있을시 builder에 id를 셋팅하지 않으면 됩니다.
        return new NativeViewBinder.Builder(findViewById(R.id.ad_cardLayout))
                .mainImageId(R.id.ad_image)
                .callToActionButtonId(R.id.ad_btCallToAction)
                .titleTextViewId(R.id.ad_title)
                .textTextViewId(R.id.ad_subtitle)
                .iconImageId(R.id.ad_icon)
                .ratingBarId(R.id.ad_ratingBar)
                .build();
    }

}
