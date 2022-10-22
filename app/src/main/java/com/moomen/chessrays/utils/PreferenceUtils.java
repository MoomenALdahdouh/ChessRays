package com.moomen.chessrays.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    public PreferenceUtils() {
    }

    public static boolean saveName(String name, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_NAME, name);
        editor.apply();
        return true;
    }

    public static String getName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_NAME, null);
    }

    public static boolean saveID(String userId, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_ID, userId);
        editor.apply();
        return true;
    }

    public static String getID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_ID, null);
    }

    public static boolean saveUid(String userUid, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_UID, userUid);
        editor.apply();
        return true;
    }

    public static String getUid(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_UID, null);
    }

    public static boolean saveAccountType(String accountType, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.ACCOUNT_TYPE, accountType);
        editor.apply();
        return true;
    }

    public static String getAccountType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.ACCOUNT_TYPE, null);
    }

}
