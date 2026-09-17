package com.onnuridmc.sample.model;

import com.onnuridmc.exelbid.common.AdNativeData;

/**
 * Created by Administrator on 2016-07-19.
 */
public class DummyData {
    private boolean isAd = false;

    // 광고가 아닌 경우의 데이터
    private String mData;

    // 광고용 데이터
    private AdNativeData mAdData;

    public DummyData(String data) {
        isAd = false;
        mData = data;
    }

    public DummyData(AdNativeData adData) {
        isAd = true;
        mAdData = adData;
    }

    public boolean isAd() {
        return isAd;
    }

    public AdNativeData getAdData() {
        return mAdData;
    }

    @Override
    public String toString() {
        return mData;
    }

    public AdNativeData getAdNativeData() {
        return mAdData;
    }
}
