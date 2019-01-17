package com.frame.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.frame.config.BaseConfig;


/**
 * @Description: SharedPreferences工具类
 */
public class SharedPreferencesUtil {

    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(BaseConfig.SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String value) {
        return getSharedPreferences(context).getString(key, value);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean value) {
        return getSharedPreferences(context).getBoolean(key, value);
    }

    public static void putLong(Context context, String key, Long value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static Long getLong(Context context, String key, Long value) {
        return getSharedPreferences(context).getLong(key, value);
    }

    public static void putInteger(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInteger(Context context, String key, int value) {
        return getSharedPreferences(context).getInt(key, value);
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
