package com.onnuridmc.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.onnuridmc.sample.activity.SampleBannerView;
import com.onnuridmc.sample.activity.SampleDialog;
import com.onnuridmc.sample.activity.SampleInterstitialView;
import com.onnuridmc.sample.activity.SampleNative;
import com.onnuridmc.sample.activity.SampleNativeArray;
import com.onnuridmc.sample.activity.SampleNativeBanner;
import com.onnuridmc.sample.activity.SampleNativeListView;
import com.onnuridmc.sample.activity.SampleNativeRecycler;
import com.onnuridmc.sample.activity.SampleNativeRecyclerArray;
import com.onnuridmc.sample.activity.SampleNativeRecyclerAuto;
import com.onnuridmc.sample.activity.SampleNativeVideo;

public class MainActivity extends Activity {

    private ListView mListView;
    private InAdapter mAdapter;

    // Sample app web views are debuggable.
    static {
        setWebDebugging();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setWebDebugging() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_main);
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mListView = (ListView) findViewById(R.id.listview);

        mAdapter = new InAdapter(this);

        mAdapter.add(new Pair<String, Class<?>>("배너광고", SampleBannerView.class));
        mAdapter.add(new Pair<String, Class<?>>("전면광고", SampleInterstitialView.class));
        mAdapter.add(new Pair<String, Class<?>>("다이얼로그광고", SampleDialog.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브", SampleNative.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 비디오", SampleNativeVideo.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Banner", SampleNativeBanner.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Array", SampleNativeArray.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 ListView", SampleNativeListView.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Recycler", SampleNativeRecycler.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Recycler Array", SampleNativeRecyclerArray.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Recycler Auto", SampleNativeRecyclerAuto.class));

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
                Pair<String, Class<?>> item = mAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, item.second);
                intent.putExtra(getString(R.string.str_title), item.first);
                startActivity(intent);
            }
        });
    }

    private class InAdapter extends ArrayAdapter<Pair<String, Class<?>>> {

        public InAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, android.R.layout.simple_list_item_1, null);
            }

            Pair<String, Class<?>> data = getItem(position);

            TextView txtView = (TextView) convertView.findViewById(android.R.id.text1);

            txtView.setText(data.first);

            return convertView;
        }

    }

}
