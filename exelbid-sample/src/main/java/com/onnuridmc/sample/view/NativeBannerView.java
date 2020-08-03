package com.onnuridmc.sample.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onnuridmc.sample.R;

/**
 * Created by Administrator on 2016-10-04.
 */

public class NativeBannerView extends LinearLayout{

    private final static int DURATION = 1000;
    private final static int START_DELAY = 1000;

    private TextView mTitleView;
    private TextView mDescView;

    private ImageView mIcon;
    private Button mButton;

    private ObjectAnimator mAnimatorTitle;
    private ObjectAnimator mAnimatorDesc;

    private boolean isRunning = false;

    public NativeBannerView(Context context) {
        super(context);
        init();
    }

    public NativeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_native_banner, this);

        mIcon = (ImageView) findViewById(R.id.imageView);
        mTitleView = (TextView)findViewById(R.id.textView1);
        mDescView = (TextView)findViewById(R.id.textView2);
        mButton = (Button) findViewById(R.id.button);

    }

    public void stop () {
        if(mAnimatorTitle != null) {
            mAnimatorTitle.cancel();
            mAnimatorTitle = null;
        }

        if(mAnimatorDesc != null) {
            mAnimatorDesc.cancel();
            mAnimatorDesc = null;
        }

        mTitleView.setTranslationY(0);
        mDescView.setTranslationY(0);
        mTitleView.setVisibility(View.GONE);
        mDescView.setVisibility(View.GONE);

        isRunning = false;
    }

    public void start() {
        stop();
        isRunning = true;
        mTitleView.setVisibility(View.VISIBLE);
        mDescView.setVisibility(View.VISIBLE);
        startTitleAnimation(0);
    }

    private void startTitleAnimation(int delay) {
        if(getHeight() == 0)
            return;

        float offset = getHeight() / 2;

        if(mAnimatorTitle == null) {
            mAnimatorTitle = ObjectAnimator.ofFloat(mTitleView, "translationY", offset, 0);
            mAnimatorTitle.setDuration(DURATION);
            mAnimatorTitle.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mDescView.setVisibility(View.GONE);
                    mTitleView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startDescAnimation(START_DELAY);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        } else {
            mAnimatorTitle.setFloatValues(offset, 0);
        }

        mAnimatorTitle.setStartDelay(delay);
        mAnimatorTitle.start();
    }

    private void startDescAnimation(int delay) {
        float offset = getHeight() / 2;
        if(mAnimatorDesc == null) {
            mAnimatorDesc = ObjectAnimator.ofFloat(mDescView, "translationY", offset, 0);
            mAnimatorDesc.setDuration(DURATION);
            mAnimatorDesc.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mTitleView.setVisibility(View.GONE);
                    mDescView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startTitleAnimation(START_DELAY);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        } else {
            mAnimatorTitle.setFloatValues(offset, 0);
        }

        mAnimatorDesc.setStartDelay(delay);
        mAnimatorDesc.start();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed && isRunning) {
            start();
        }
    }

    public static class NativeBannerViewHolder extends RecyclerView.ViewHolder{
        private View mItemView;
        private NativeBannerView mNativeBannerView;

        public NativeBannerViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mNativeBannerView = (NativeBannerView)itemView.findViewById(R.id.native_banner);
        }

        public NativeBannerView getNativeBannerView() {
            return mNativeBannerView;
        }


    }
}
