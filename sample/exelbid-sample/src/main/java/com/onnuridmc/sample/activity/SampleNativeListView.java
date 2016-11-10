package com.onnuridmc.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.onnuridmc.exelbid.common.AdNativeAdapter;
import com.onnuridmc.exelbid.common.ExelBidClientPositioning;
import com.onnuridmc.exelbid.common.ExelBidServerPositioning;
import com.onnuridmc.exelbid.common.NativeAsset;
import com.onnuridmc.exelbid.common.NativeViewBinder;
import com.onnuridmc.sample.AppConstants;
import com.onnuridmc.sample.R;
import com.onnuridmc.sample.model.DummyData;
import com.onnuridmc.sample.utils.PrefManager;

/**
 * Adapter를 사용할경우 조금더 편하게 사용할수 있도록 한 샘플
 * getView가 호출 될때 광고를 요청하여 알아서 데이터가 바인딩 된다.
 * 만약 데이터가 변경될 시에도 포지션이 설정된 값으로 유지 되므로 그럴경우에는 NativeManager로 사용하시기 바랍니다.
 */
public class SampleNativeListView extends Activity {

    private ListView mListView;
    private AdNativeAdapter mAdapter;

    private String mUnitId;
    private EditText mEdtAdUnit;

    private int[] FIXED_POSITION = {0, 2, 5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_native_adapter);

        mEdtAdUnit = (EditText)findViewById(R.id.editText);
        mEdtAdUnit.setEnabled(false);
        mUnitId = PrefManager.getNativeAd(this, PrefManager.KEY_NATIVE_AD, AppConstants.UNIT_ID_NATIVE);
        mEdtAdUnit.setText(mUnitId);

        mListView = (ListView) findViewById(R.id.listview);

        final ArrayAdapter<DummyData> inAdapter = new ArrayAdapter<DummyData>(this, android.R.layout.simple_list_item_1);

        for (int i = 0; i < 100; ++i) {
            inAdapter.add(new DummyData("Item " + i));
        }

        mAdapter = new AdNativeAdapter(this, mUnitId, inAdapter);

        //네이티브 광고 데이터가 바인딩 될 뷰의 정보를 셋팅한다.
        mAdapter.setNativeViewBinder(new NativeViewBinder.Builder(R.layout.native_item_adview)
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
        mAdapter.setRequiredAsset(new NativeAsset[]{NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});

        // 광고가 노출될 포지션을 셋팅한다.
//        mAdapter.setPositionning(new ExelBidClientPositioning()
//                .addFixedPositions(FIXED_POSITION)
//                .setRepeatInterval(10)
//        );
        mAdapter.setPositionning(new ExelBidServerPositioning());

        mListView.setAdapter(mAdapter);

    }

}
