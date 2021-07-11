package com.myclothershopapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "referral";
    private static final String NOW_TIME = "nowtime";
    private static final String IS_SHOP_CLOSE = "shopclose";
    private static final String IS_PERCENTAGE_AVAILABLE = "percentage";
    private static final String PERCENTAGE_VALUE = "percentage_value";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean getShopClose() {
        return pref.getBoolean(IS_SHOP_CLOSE, false);
    }
    public void setIsShopClose(boolean isShopClose) {
        editor.putBoolean(IS_SHOP_CLOSE, isShopClose);
        editor.commit();

    }
    public boolean getDiscountAvailable() {
        return pref.getBoolean(IS_PERCENTAGE_AVAILABLE, false);
    }
    public void setDiscountAvailable(boolean discountAvailable) {
        editor.putBoolean(IS_PERCENTAGE_AVAILABLE, discountAvailable);
        editor.commit();

    }

    public String getPercentageValue() {
        return pref.getString(PERCENTAGE_VALUE, "");
    }

    public void setPercentageValue(String percentageValue) {
        editor.putString(PERCENTAGE_VALUE, percentageValue);
        editor.commit();
    }


    public long getNowTime() {
        return pref.getLong(NOW_TIME, -1);

    }

    public void setDate(long timestamp) {
        editor.putLong(NOW_TIME, timestamp);
        editor.commit();

    }
}