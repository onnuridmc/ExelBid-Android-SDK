package com.onnuridmc.sample.nextgen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * GMA Next-Gen SDK 검증 데모 진입 화면.
 */
public class NextGenMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ExelBid × GMA Next-Gen 검증");

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        root.setPadding(pad, pad, pad, pad);

        addButton(root, "배너 미디에이션", NextGenBannerActivity.class);
        addButton(root, "전면 미디에이션", NextGenInterstitialActivity.class);
        addButton(root, "네이티브 미디에이션", NextGenNativeActivity.class);

        setContentView(root);
    }

    private void addButton(LinearLayout parent, String label, Class<?> activity) {
        Button button = new Button(this);
        button.setText(label);
        button.setOnClickListener(v -> startActivity(new Intent(this, activity)));
        parent.addView(button);
    }
}
