package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.onnuridmc.exelbid.common.AdNativeRecyclerAdapter;
import com.onnuridmc.exelbid.common.ExelBidServerPositioning;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

import java.util.ArrayList;

public class SampleNativeRecyclerAuto extends Activity implements View.OnClickListener {
    private final int ITEM_COUNT = 100;

    private RecyclerView mRecyclerView;
    private AdNativeRecyclerAdapter mAdapter;
    private DemoRecyclerAdapter inAdapter;


    private ArrayList<DemoData> mListData = new ArrayList<>();

    private String mUnitId;
    private EditText mEdtAdUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_recycler);

        mEdtAdUnit = (EditText)findViewById(R.id.editText);
        mUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);
        mEdtAdUnit.setText(mUnitId);

        findViewById(R.id.button).setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        inAdapter = new DemoRecyclerAdapter();

        for(int i = 0 ; i < ITEM_COUNT ; i ++) {
            DemoData demo = new DemoData();
            demo.isAd = false;
            demo.text = "Content Item : " + i;
            mListData.add(demo);
        }

        mAdapter = new AdNativeRecyclerAdapter(this, mUnitId, inAdapter);
        mAdapter.setNativeViewBinder(new NativeViewBinder.Builder(R.layout.native_item_recycle_adview)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());


        mAdapter.setYob("1990");
        mAdapter.setGender(true);
        mAdapter.addKeyword("level", "10");
        mAdapter.setTestMode(AppConstants.TEST_MODE);

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mAdapter.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
        mAdapter.setPositionning(new ExelBidServerPositioning());

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {
            mListData.clear();

            for(int i = 0 ; i < ITEM_COUNT ; i ++) {
                DemoData demo = new DemoData();
                demo.isAd = false;
                demo.text = "Content Item : " + i;
                mListData.add(demo);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    private class DemoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                          final int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new DemoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((DemoViewHolder)holder).textView.setText(mListData.get(position).text);
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }
    }

    private class DemoData {
        public boolean isAd = false;
        public String text;
    }

    /**
     * A view holder for R.layout.simple_list_item_1
     */
    private static class DemoViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        DemoViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
