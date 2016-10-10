package com.onnuridmc.sample;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.onnuridmc.sample.activity.SampleBannerView;
import com.onnuridmc.sample.activity.SampleDialog;
import com.onnuridmc.sample.activity.SampleInterstitialView;
import com.onnuridmc.sample.activity.SampleNativeArray;
import com.onnuridmc.sample.activity.SampleNativeBanner;
import com.onnuridmc.sample.activity.SampleNativeRecycler;
import com.onnuridmc.sample.activity.SampleNativeRecyclerArray;
import com.onnuridmc.sample.activity.SampleNative;
import com.onnuridmc.sample.activity.SampleNativeListView;

public class MainActivity extends ListActivity {

    private InAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new InAdapter(this);

        mAdapter.add(new Pair<String, Class<?>>("배너광고", SampleBannerView.class));
        mAdapter.add(new Pair<String, Class<?>>("전면광고", SampleInterstitialView.class));
        mAdapter.add(new Pair<String, Class<?>>("다이얼로그광고", SampleDialog.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브", SampleNative.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Banner", SampleNativeBanner.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Array", SampleNativeArray.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 ListView", SampleNativeListView.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Recycler", SampleNativeRecycler.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Recycler Array", SampleNativeRecyclerArray.class));

        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
                Pair<String, Class<?>> item = mAdapter.getItem(position);
                startActivity(new Intent(MainActivity.this, item.second));
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
