package com.onnuridmc.sample.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

@TargetApi(9)
public class PrefManager {
	public final static String KEY_INTERSTITIAL_AD = "inter_key";
	public final static String KEY_BANNER_AD = "banner_key";
	public final static String KEY_NATIVE_AD = "native_key";
	public final static String KEY_NATIVE_ADAPTER_AD = "native_adapter_key";
	public final static String KEY_NATIVE_MULTI_AD = "native_multi_key";
	public final static String KEY_DIALOG_NATIVE_AD = "dig_native_key";
	public final static String KEY_DIALOG_INTERSTIAL_AD = "dig_inter_key";


	public static String getInterstialAd(Context context, String key, String _default) {
		if(KEY_INTERSTITIAL_AD.equals(key)) {
			return getPref(context, KEY_INTERSTITIAL_AD, getPref(context, KEY_DIALOG_INTERSTIAL_AD, _default));
		} else {
			return getPref(context, KEY_DIALOG_INTERSTIAL_AD, getPref(context, KEY_INTERSTITIAL_AD, _default));
		}
	}

	public static String getNativeAd(Context context, String key, String _default) {
		String nativead = getPref(context, KEY_NATIVE_AD, null);
		String nativeadapter = getPref(context, KEY_NATIVE_ADAPTER_AD, null);
		String nativemulti = getPref(context, KEY_NATIVE_MULTI_AD, null);
		String nativenative = getPref(context, KEY_DIALOG_NATIVE_AD, null);

		String _setData = !TextUtils.isEmpty(nativead) ? nativead :
		                  !TextUtils.isEmpty(nativeadapter) ? nativeadapter :
						  !TextUtils.isEmpty(nativemulti) ? nativemulti :
		                  !TextUtils.isEmpty(nativenative) ? nativenative : _default;

		return getPref(context, key, _setData);
	}


	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void setPref(Context context, String key, String value){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = settings.edit();
		editor.putString(key, value);
		if(Build.VERSION.SDK_INT >= 9){
			editor.apply();
		}else{			
			editor.commit();
		}
	}
	
	public static String getPref(Context context, String key, String defValue){
		SharedPreferences settings =  PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key, defValue);
	}
	
}
