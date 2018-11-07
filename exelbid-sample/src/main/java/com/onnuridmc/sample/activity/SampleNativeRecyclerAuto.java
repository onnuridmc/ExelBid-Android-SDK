package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

/**
 *  SampleNativeRecyclerAuto
 *  AdNativeRecyclerAdapter를 사용하여 RecyclerView 가운데 네이티브 광고 여러개를 자동으로 로드하여 바이딩하는 예제
 *  1. AdNativeRecyclerAdapter mAdapter 생성 (unitId 와 사용하고자 하는 원래 Adapter를 전달)
 *  2. mNativeAdMgr.setNativeViewBinder 이용하여 네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅한다.
 *  3. mNativeAdMgr.setRequiredAsset 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
 *  4. mNativeAdMgr 에 추가 Arg 세팅 (ex. setYob, setGender 등)
 *  5. mAdapter.setPositionning(new ExelBidServerPositioning()); 사용시 manage.exelbid.com에서 설정항 fixed, repeat_interval 값을 사용한다
 *     또는 mAdapter.setPositionning(new ExelBidClientPositioning().addFixedPositions(new int{0, 3, 6, 10, 13, 15, 20}).setRepeatInterval(5));
 *     와 같이 설정하면 서버에 설정 값 무시하고 클라이언트에서 설정한대로 사용할 수 있다
 */
public class SampleNativeRecyclerAuto extends Activity implements View.OnClickListener {
    private final int ITEM_COUNT = 20;

    private RecyclerView mRecyclerView;
    private AdNativeRecyclerAdapter mAdapter;
    private DemoRecyclerAdapter inAdapter;

    private String mUnitId;
    private EditText mEdtAdUnit;
    private CheckBox mIsTestCheckBox;

    private int[] FIXED_POSITION = {0, 2, 5};


    private ArrayList<DemoData> mListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_recycler);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }
        mIsTestCheckBox = (CheckBox) findViewById(R.id.test_check);
        mIsTestCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAdapter.setTestMode(isChecked);
            }
        });

        mEdtAdUnit = (EditText)findViewById(R.id.editText);
        mUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);
        mEdtAdUnit.setText(mUnitId);

        Button btn = (Button) findViewById(R.id.button);
        btn.setText("Add");
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
        mAdapter.setTestMode(mIsTestCheckBox.isChecked());

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mAdapter.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
        mAdapter.setPositionning(new ExelBidServerPositioning());

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {
//            mListData.clear();

            int size = mListData.size();
            for(int i = 0 ; i < ITEM_COUNT ; i ++) {
                DemoData demo = new DemoData();
                demo.isAd = false;
                demo.text = "Content Item : " + (size + i);
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
