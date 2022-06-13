package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SampleBase extends Activity {

    protected final String TAG = getClass().getSimpleName();
    protected static final String UNIT_ID_EXELBID_BANNER = "4e0675ecdde6e9a9eac083b681c5068b0648895b";
    protected static final String UNIT_ID_ADMOB_BANNER = "ca-app-pub-9668530497062401/8249133291";
    protected static final String UNIT_ID_FAN_BANNER = "931253551037936_931255047704453";
    protected static final String UNIT_ID_EXELBID_INTERSTITTIAL = "e98ab99b125943bbba54ebf9b0e866ec";
    protected static final String UNIT_ID_ADMOB_INTERSTITTIAL = "ca-app-pub-9668530497062401/1054867070";
    protected static final String UNIT_ID_FAN_INTERSTITTIAL = "931253551037936_931260687703889";
    protected static final String UNIT_ID_EXELBID_NATIVE = "5ae66a9bc47b48319fb02a1e07f070a6";
    protected static final String UNIT_ID_ADMOB_NATIVE = "ca-app-pub-9668530497062401/8785648902";
    protected static final String UNIT_ID_FAN_NATIVE = "931253551037936_931260397703918";

    // Log text
    protected TextView logText;
    protected StringBuffer logsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void printLog(String log) {
        logsb.append("- ").append(log).append("\n");
        logText.setText(logsb.toString());
        Log.e(TAG, log);
    }

    protected void printLog(String head, String log) {
        logsb.append("- [").append(head).append("] ").append(log).append("\n");
        String text = logsb.toString();
        logText.setText(text);
        Log.e(TAG, text);
    }
}