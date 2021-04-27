package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.onnuridmc.sample.R;

import static com.mopub.common.logging.MoPubLog.LogLevel.DEBUG;

public class SampleMopubBanner extends Activity {

    public final String TAG = getClass().getSimpleName();

    MoPubView moPubView;
    String mopubAdUnitId = "79aa29aa59914ec6a1b8a01b140a133b";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopub);

        TextView titleTv = findViewById(R.id.tv);
        titleTv.setText(TAG);
        TextView unitIdTv = findViewById(R.id.unitid);
        unitIdTv.setText(mopubAdUnitId);
        Button loadBtn = findViewById(R.id.load_btn);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moPubView.loadAd();
            }
        });

        final SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(mopubAdUnitId);

        configBuilder.withLogLevel(DEBUG);
        MoPub.initializeSdk(this, configBuilder.build(), new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                // SDK initialization complete. You may now request ads.
            }
        });

        moPubView = findViewById(R.id.ad_view);
        moPubView.setAdUnitId(mopubAdUnitId);
        moPubView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(@NonNull MoPubView banner) {
                Log.e(TAG, "onBannerLoaded");
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                Log.e(TAG, "onBannerFailed : " + errorCode.toString());
            }

            @Override
            public void onBannerClicked(MoPubView banner) {
                Log.e(TAG, "onBannerClicked");
            }

            @Override
            public void onBannerExpanded(MoPubView banner) {

            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {

            }
        });

    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (moPubView != null) {
            moPubView.destroy();
        }
        super.onDestroy();
    }
}
