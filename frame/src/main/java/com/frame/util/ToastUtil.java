package com.frame.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;

/**
 * 吐司工具类(便于后期更换)
 */
public class ToastUtil {

    public static void showShortToast(String content) {
        if (TextUtils.isEmpty(content))
            ToastUtils.showShort("额,好像什么也没有说");
        else
            ToastUtils.showShort(content);
    }

    public static void showLongToast(String content) {
        if (TextUtils.isEmpty(content))
            ToastUtils.showLong("额,好像什么也没有说");
        else
            ToastUtils.showLong(content);
    }
}