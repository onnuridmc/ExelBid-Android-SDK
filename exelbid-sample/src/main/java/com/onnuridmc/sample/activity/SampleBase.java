package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SampleBase extends Activity {

    protected final String TAG = getClass().getSimpleName();
    protected static final String UNIT_ID_EXELBID_BANNER = "4e0675ecdde6e9a9eac083b681c5068b0648895b";
    protected static final String UNIT_ID_ADMOB_BANNER = "ca-app-pub-3940256099942544/6300978111";
    protected static final String UNIT_ID_FAN_BANNER = "931253551037936_931255047704453";
    protected static final String APP_ID_DT = "167844";
    protected static final String APP_ID_PANGLE = "8337910";
    protected static final String UNIT_ID_DT_BANNER = "1756531";
    protected static final String UNIT_ID_PANGLE_BANNER = "981107951";
    protected static final String UNIT_ID_MAX_BANNER = "922f5cfc74ac19d4";
    protected static final String UNIT_ID_TNK_BANNER = "TEST_BANNER_200";
    protected static final String UNIT_ID_EXELBID_INTERSTITTIAL = "e98ab99b125943bbba54ebf9b0e866ec";
    protected static final String UNIT_ID_ADMOB_INTERSTITTIAL = "ca-app-pub-3940256099942544/1033173712";
    protected static final String UNIT_ID_FAN_INTERSTITTIAL = "931253551037936_931260687703889";
    protected static final String UNIT_ID_DT_INTERSTITTIAL = "1756532";
    protected static final String UNIT_ID_PANGLE_INTERSTITTIAL = "981118568";
    protected static final String UNIT_ID_MAX_INTERSTITIAL = "f14b5913a547e04c";
    protected static final String UNIT_ID_TNK_INTERSTITIAL = "TEST_INTERSTITIAL_V";
    protected static final String UNIT_ID_EXELBID_NATIVE = "5ae66a9bc47b48319fb02a1e07f070a6";
    protected static final String UNIT_ID_ADMOB_NATIVE = "ca-app-pub-3940256099942544/2247696110";
    protected static final String UNIT_ID_FAN_NATIVE = "931253551037936_931260397703918";
    protected static final String UNIT_ID_PANGLE_NATIVE = "981118572";
    protected static final String UNIT_ID_MAX_NATIVE = "36af5b9c4d5af0ee"; //745c4f55a023525f
    protected static final String UNIT_ID_TNK_NATIVE = "TEST_NATIVE";

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