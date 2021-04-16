package com.frame.util;

import android.graphics.Color;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;

/**
 * 吐司工具类(便于后期更换)
 */
public class ToastUtil {

    public static void showShortToast(String content) {
        if (!TextUtils.isEmpty(content))
            ToastUtils.make().setTextColor(Color.WHITE).setBgColor(Color.BLACK).show(content);
    }

    public static void showLongToast(String content) {
        if (!TextUtils.isEmpty(content))
            ToastUtils.make().setTextColor(Color.WHITE).setDurationIsLong(true).setBgColor(Color.BLACK).show(content);
    }
}