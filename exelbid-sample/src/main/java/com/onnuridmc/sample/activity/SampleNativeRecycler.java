package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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

/**
 *  SampleNativeRecycler
 *  RecyclerView 가운데 네이티브 광고 하나 로드 하여 바이딩하는 예제
 *  1. ExelBidNativeManager mNativeAdMgr 생성 (unitId 와 OnAdNativeManagerListener를 인수로 전달)
 *  2. mNativeAdMgr.setNativeViewBinder 이용하여 네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅한다.
 *  3. mNativeAdMgr.setRequiredAsset 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
 *  4. mNativeAdMgr 에 추가 Arg 세팅 (ex. setYob, setGender 등)
 *  5.  mNativeAdMgr.loadAd() 광고 요청
 *  6. OnAdNativeManagerListener 을 통한 결과 Callback에서 성공시 기존 데이터 리스트에 광고 설정
 *  7. onCreateViewHolder에서 광고 설정된 포지션 일 경우에 mNativeAdMgr을 통한 onCreateViewHolder 호출 처리 하면 setNativeViewBinder를 통해 설정한  layout viewholder 설정
 *  8. onBindViewHolder에서 광고 설정된 포지션 일 경우에 mNativeAdMgr을 통한 onBindViewHolder 호출 처리 하면 setNativeViewBinder를 통해 설정한  layout viewholder 에 광고 데이터 바인딩
 */
import java.util.ArrayList;

public class SampleNativeRecycler extends Activity implements View.OnClickListener {
    private final int ITEM_COUNT = 100;

    private RecyclerView mRecyclerView;

    private ExelBidNativeManager mNativeAdMgr;

    private ArrayList<DemoData> mListData = new ArrayList<>();

    private String mUnitId;
    private EditText mEdtAdUnit;
    private CheckBox mIsTestCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_recycler);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }
        mIsTestCheckBox = (CheckBox) findViewById(R.id.test_check);

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

        mNativeAdMgr = new ExelBidNativeManager(this, mUnitId, new OnAdNativeManagerListener() {

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
                DemoData data = new DemoData();
                data.isAd = true;
                // 원하는 광고 포지션에 넣어준다.
                mListData.add(5, data);
                inAdapter.notifyDataSetChanged();
            }
        });

        //네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅한다.
        mNativeAdMgr.setNativeViewBinder(new NativeViewBinder.Builder(R.layout.native_item_recycle_adview)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());


        mNativeAdMgr.setYob("1990");
        mNativeAdMgr.setGender(true);
        mNativeAdMgr.addKeyword("level", "10");
        mNativeAdMgr.setTestMode(mIsTestCheckBox.isChecked());

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mNativeAdMgr.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {

            String unitID = mEdtAdUnit.getText().toString();
            if(TextUtils.isEmpty(unitID)) {
                return;
            }
            mNativeAdMgr.setAdUnitId(unitID);
            if(!unitID.equals(mUnitId)) {
                PrefManager.setPref(this, PrefManager.KEY_NATIVE_AD, unitID);
            }
            mNativeAdMgr.setTestMode(mIsTestCheckBox.isChecked());

            mUnitId = unitID;

            //광고를 요청한다.
            mNativeAdMgr.loadAd();

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
                return mNativeAdMgr.onCreateViewHolder(parent, viewType);
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
                mNativeAdMgr.onBindViewHolder(holder, position);
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
