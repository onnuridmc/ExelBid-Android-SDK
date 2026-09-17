package com.onnuridmc.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onnuridmc.sample.activity.SampleAdfitBizBoardMediation;
import com.onnuridmc.sample.activity.SampleBannerMediation;
import com.onnuridmc.sample.activity.SampleInterstitialMediation;
import com.onnuridmc.sample.activity.SampleNativeMediation;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private InAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_main);
        mListView = findViewById(R.id.listview);

        mAdapter = new InAdapter(this);

        mAdapter.add(new Pair<String, Class<?>>("배너 Mediation", SampleBannerMediation.class));
        mAdapter.add(new Pair<String, Class<?>>("전면 Mediation", SampleInterstitialMediation.class));
        mAdapter.add(new Pair<String, Class<?>>("네이티브 Mediation", SampleNativeMediation.class));
        mAdapter.add(new Pair<String, Class<?>>("AdFit 비즈보드 Mediation", SampleAdfitBizBoardMediation.class));

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            Pair<String, Class<?>> item = mAdapter.getItem(position);
            if (item != null) {
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
