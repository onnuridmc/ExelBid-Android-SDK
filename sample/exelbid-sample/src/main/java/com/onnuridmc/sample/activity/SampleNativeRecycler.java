package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.onnuridmc.exelbid.ExelBidNativeManager;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeManagerListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

import java.util.ArrayList;

public class SampleNativeRecycler extends Activity implements View.OnClickListener {
    private final int ITEM_COUNT = 100;

    private RecyclerView mRecyclerView;

    private ExelBidNativeManager mNativeAd;

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

        final DemoRecyclerAdapter inAdapter = new DemoRecyclerAdapter();

        for(int i = 0 ; i < ITEM_COUNT ; i ++) {
            DemoData demo = new DemoData();
            demo.isAd = false;
            demo.text = "Content Item : " + i;
            mListData.add(demo);
        }

        mRecyclerView.setAdapter(inAdapter);

        mNativeAd = new ExelBidNativeManager(this, mUnitId, new OnAdNativeManagerListener() {

            @Override
            public void onFailed(String key, ExelBidError error) {

            }

            @Override
            public void onShow(String key) {

            }

            @Override
            public void onClick(String key) {

            }

            @Override
            public void onLoaded(String key) {
                findViewById(R.id.button).setEnabled(false);
                DemoData data = new DemoData();
                data.isAd = true;
                // 원하는 광고 포지션에 넣어준다.
                mListData.add(5, data);
                inAdapter.notifyDataSetChanged();
            }
        });

        //네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅한다.
        mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(R.layout.native_item_recycle_adview)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());


        mNativeAd.setYob("1990");
        mNativeAd.setGender(true);
        mNativeAd.addKeyword("level", "10");
        mNativeAd.setTestMode(AppConstants.TEST_MODE);

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {

            String unitID = mEdtAdUnit.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            mNativeAd.setAdUnitId(unitID);
            if(!unitID.equals(mUnitId)) {
                PrefManager.setPref(this, PrefManager.KEY_NATIVE_AD, unitID);
            }

            mUnitId = unitID;

            //광고를 요청한다.
            mNativeAd.loadAd();

            //키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtAdUnit.getWindowToken(), 0);
        }
    }

    private class DemoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int ITEM_TYPE_AD = 99;
        private final int ITEM_TYPE_NONE = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                 final int viewType) {
            if(viewType == ITEM_TYPE_AD) {
                //setNativeViewBinder에 설정한 정보로 layout을 생성합니다.
                return mNativeAd.onCreateViewHolder(parent, viewType);
            } else {
                final View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                return new DemoViewHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if(getItemViewType(position) == ITEM_TYPE_AD) {
                //setNativeViewBinder에 설정한 정보로 layout을 생성합니다.
                mNativeAd.onBindViewHolder(holder, position);
            } else {
                ((DemoViewHolder)holder).textView.setText(mListData.get(position).text);
            }
        }

        @Override
        public long getItemId(final int position) {
            return (long) position;
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(mListData.get(position).isAd) {
                return ITEM_TYPE_AD;
            }
            return ITEM_TYPE_NONE;
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
