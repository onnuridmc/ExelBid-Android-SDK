package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.onnuridmc.exelbid.ExelBidNativeManager;
import com.onnuridmc.exelbid.common.AdNativeData;
import com.onnuridmc.exelbid.common.ExelBidError;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.exelbid.common.OnAdNativeManagerListener;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.utils.PrefManager;

/**
 * 네이티브 광고를 여러개 요청 하는 샘플입니다.
 *  SampleNativeArray
 *  ListView 가운데 네이티브 광고 여러개를 로드 하여 바이딩하는 예제
 *  1. ExelBidNativeManager mNativeAdMgr 생성 (unitId 와 OnAdNativeManagerListener를 인수로 전달)
 *  2. mNativeAdMgr.setNativeViewBinder 이용하여 네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅한다.
 *  3. mNativeAdMgr.setRequiredAsset 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
 *  4. mNativeAdMgr 에 추가 Arg 세팅 (ex. setYob, setGender 등)
 *  5. mNativeAdMgr.loadAd(key) 여러개의 광고개수만큼 요청
 *  6. OnAdNativeManagerListener 을 통한 결과 Callback에서 성공시 기존 데이터 리스트에 key값을 가지고 광고 설정
 *  7. getView에서 광고 설정된 포지션 일 경우에
 *      key값을 가지고 mNativeAdMgr.getAdNativeData(key) 하여 AdNativeData를 가져온다
 *      mNativeAdMgr.getView(adNativeData, convertView); 호출
 */
public class SampleNativeArray extends Activity implements View.OnClickListener {

    private static final String TAG = "SampleNativeArray";

    private ListView mListView;
    private ExelBidNativeManager mNativeAdMgr;
    CheckBox mIsTestCheckBox;

    private String mUnitId;
    private EditText mEdtAdUnit;

    private InAdapter mAdapter;

    private int  insertPosition = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_array);

        String title = getIntent().getStringExtra(getString(R.string.str_title));
        if(!title.isEmpty()) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }
        mIsTestCheckBox = (CheckBox) findViewById(R.id.test_check);

        mEdtAdUnit = (EditText) findViewById(R.id.editText);
        mUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);
        mEdtAdUnit.setText(mUnitId);

        mAdapter = new InAdapter(this, android.R.layout.simple_list_item_1);

        mListView = (ListView) findViewById(R.id.listview);

        for(int i = 0 ; i < 100 ; i ++) {
            mAdapter.add(new DemoData(false, "Content Item : " + i));
        }

        mListView.setAdapter(mAdapter);

        // 네이티브 요청 객체를 생성한다.
        mNativeAdMgr = new ExelBidNativeManager(this, mUnitId, new OnAdNativeManagerListener() {
            @Override
            public void onFailed(String key, ExelBidError error) {
                Log.d(TAG, "onFailed : " + key + "  " + error.toString());
            }

            @Override
            public void onShow(String key) {
                Log.d(TAG, "onShow : " + key);
            }

            @Override
            public void onClick(String key) {
                Log.d(TAG, "onClick : " + key);
            }

            @Override
            public void onLoaded(String key) {
                Log.d(TAG, "onLoaded : " + key);
                // 이 데이터로 Custom하게 사용할수 있습니다.
                insertPosition += 3;
                mAdapter.insert(new DemoData(true, key), insertPosition);

                //Adapter가 아닐경우에는 해당 뷰를 바로 설정 한다.
                if(false) {
                    AdNativeData adNativeData = mNativeAdMgr.getAdNativeData(key);
//                    mNativeAdMgr.bindViewByAdNativeData(adNativeData,
//                            new NativeViewBinder.Builder(rootView)
//                            .mainImageId(R.id.native_main_image)
//                            .callToActionButtonId(R.id.native_cta)
//                            .titleTextViewId(R.id.native_title)
//                            .textTextViewId(R.id.native_text)
//                            .iconImageId(R.id.native_icon_image)
//                            .build());
                }
            }
        });

        // 네이티브 요청시 필수로 존재해야 하는 값을 셋팅한다. 해당 조건 셋팅으로 인해서 광고가 존재하지 않을 확률이 높아집니다.
        mNativeAdMgr.setRequiredAsset(new NativeAsset[]{NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

        mNativeAdMgr.setYob("1990");
        mNativeAdMgr.setGender(true);
        mNativeAdMgr.addKeyword("level", "10");

        mNativeAdMgr.setNativeViewBinder(new NativeViewBinder.Builder(R.layout.native_item_adview)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build());

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {

            String unitID = mEdtAdUnit.getText().toString();
            if (TextUtils.isEmpty(unitID)) {
                return;
            }

            mNativeAdMgr.setTestMode(mIsTestCheckBox.isChecked());
            mNativeAdMgr.setAdUnitId(unitID);

            if (!unitID.equals(mUnitId)) {
                PrefManager.setPref(this, PrefManager.KEY_NATIVE_AD, unitID);
            }

            mUnitId = unitID;


            //키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEdtAdUnit.getWindowToken(), 0);

            //광고를 요청한다.
            for(int i = 0 ; i < 10 ; i ++ ) {
                mNativeAdMgr.loadAd(String.valueOf(i));
            }
        } else if (v.getId() == R.id.button2) {

            mAdapter.clear();
            for(int i = 0 ; i < 100 ; i ++) {
                mAdapter.add(new DemoData(false, "reload Item : " + i));
            }
            mAdapter.notifyDataSetChanged();

            insertPosition = 0;
            mNativeAdMgr.clear();
            //광고를 요청한다.
            for(int i = 0 ; i < 10 ; i ++ ) {
                mNativeAdMgr.loadAd(String.valueOf(i));
            }
        }
    }

    private class DemoData {
        boolean isAd;
        String text;
        DemoData(boolean isAd, String text){
            this.text = text;
            this.isAd = isAd;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private class InAdapter extends ArrayAdapter<DemoData> {
        InAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(getItemViewType(position) == 1) {
                DemoData demoData = getItem(position);
                // 요청할때 사용했던 key로 adNativeData를 가져온다.
                AdNativeData adNativeData = mNativeAdMgr.getAdNativeData(demoData.text);
                return mNativeAdMgr.getView(adNativeData, convertView);
            } else {
                return super.getView(position, convertView, parent);
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            DemoData demoData = getItem(position);
            return getItem(position).isAd ? 1 : 0;
        }
    }

}
