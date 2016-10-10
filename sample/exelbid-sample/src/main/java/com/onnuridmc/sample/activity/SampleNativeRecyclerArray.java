package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.onnuridmc.exelbid.ExelBidNativeManager;
import com.onnuridmc.exelbid.common.AdNativeData;
import com.onnuridmc.exelbid.common.ExelBidClientPositioning;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeManagerListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;
import com.onnuridmc.sample.view.NativeBannerView;

import java.util.ArrayList;

public class SampleNativeRecyclerArray extends Activity implements View.OnClickListener{
    private final int ITEM_COUNT = 100;

    private RecyclerView mRecyclerView;

    private ExelBidNativeManager mNativeAd;

    private ArrayList<DemoData> mListData = new ArrayList<>();

    private int[] POSITION_LIST = {0, 3, 6, 10, 13, 15, 20};

    private String mUnitId;
    private EditText mEdtAdUnit;

    private DemoRecyclerAdapter mAdapter;

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

        mAdapter = new DemoRecyclerAdapter();

        for(int i = 0 ; i < ITEM_COUNT ; i ++) {
            DemoData demo = new DemoData();
            demo.isAd = false;
            demo.text = "Content Item : " + i;
            mListData.add(demo);
        }

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
//                Log.d("dooully","[RecyclerListener] onViewRecycled");
//                if(holder instanceof NativeBannerView.NativeBannerViewHolder) {
//                    ((NativeBannerView.NativeBannerViewHolder)holder).getNativeBannerView().stop();
//                }
            }
        });

        mNativeAd = new ExelBidNativeManager(this, AppConstants.UNIT_ID_NATIVE, new OnAdNativeManagerListener() {
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
                AdNativeData data = mNativeAd.getAdNativeData(key);
                if(data != null) {
                    DemoData demoData = new DemoData();
                    demoData.isAd = true;
                    demoData.adData = data;

                    Integer position = mNativeAd.getPositionByNativeData(data);
                    if(position != null) {
                        if(position >= mListData.size()) {
                            position = mListData.size() -1;
                        }
                        mListData.add(position, demoData);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        //네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅한다.
        mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(R.layout.native_item_recycle_adview)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .build());

        mNativeAd.setYob("1990");
        mNativeAd.setGender(true);
        mNativeAd.addKeyword("level", "10");
        mNativeAd.setTestMode(AppConstants.TEST_MODE);

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

        mNativeAd.setPositionning(new ExelBidClientPositioning()
                .addFixedPositions(POSITION_LIST));

    }

    private void requestNativeAd() {
        for(int i = 0 ; i < POSITION_LIST.length ; i ++) {
            final String key = String.valueOf(i);
            mNativeAd.loadAd(key);
        }
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
            requestNativeAd();
        }
    }

    private class DemoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int ITEM_TYPE_AD = 99;
        private final int ITEM_TYPE_BANNER = 98;
        private final int ITEM_TYPE_NONE = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                          final int viewType) {
            Log.i("dooully","[RecyclerView] onCreateViewHolder : " + viewType);
            if (viewType == ITEM_TYPE_AD) {
                //setNativeViewBinder에 설정한 정보로 layout을 생성합니다.
                return mNativeAd.onCreateViewHolder(parent, viewType);
            } else if (viewType == ITEM_TYPE_BANNER) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.native_item_recycle_banner, parent, false);
                return new NativeBannerView.NativeBannerViewHolder(itemView);
            } else {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                return new DemoViewHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            Log.i("dooully","[RecyclerView] onBindViewHolder : " + position);

            DemoData demoData = mListData.get(position);

            if(getItemViewType(position) == ITEM_TYPE_AD) {
                //setNativeViewBinder에 설정한 정보로 layout을 생성합니다.
                mNativeAd.onBindViewHolder(holder, demoData.adData, position);
            } else if (getItemViewType(position) == ITEM_TYPE_BANNER){
                NativeBannerView nativeBannerView = ((NativeBannerView.NativeBannerViewHolder) holder).getNativeBannerView();
                mNativeAd.bindViewByAdNativeData(demoData.adData, new NativeViewBinder.Builder(nativeBannerView)
                        .callToActionButtonId(R.id.button)
                        .titleTextViewId(R.id.textView1)
                        .textTextViewId(R.id.textView2)
                        .iconImageId(R.id.imageView)
                        .build());
            } else {
                ((DemoViewHolder)holder).textView.setText(demoData.text);
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            Log.i("dooully","[RecyclerView] onAttachedToRecyclerView");
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            Log.i("dooully","[RecyclerView] onDetachedFromRecyclerView");
        }


        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            Log.i("dooully","[RecyclerView] onFailedToRecycleView");
            return super.onFailedToRecycleView(holder);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
//            Log.i("dooully","[RecyclerView] onViewAttachedToWindow");
            if(holder instanceof NativeBannerView.NativeBannerViewHolder) {
                ((NativeBannerView.NativeBannerViewHolder)holder).getNativeBannerView().start();
            }
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
//            Log.i("dooully","[RecyclerView] onViewDetachedFromWindow");
            if(holder instanceof NativeBannerView.NativeBannerViewHolder) {
                ((NativeBannerView.NativeBannerViewHolder)holder).getNativeBannerView().stop();
            }
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
//            Log.i("dooully","[RecyclerView] onViewRecycled");
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
                if(position % 2 == 0) {
                    return ITEM_TYPE_BANNER;
                } else {
                    return ITEM_TYPE_AD;
                }
            }
            return ITEM_TYPE_NONE;
        }
    }

    private class DemoData {
        public boolean isAd = false;
        public AdNativeData adData;
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
