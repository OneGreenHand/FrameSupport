package com.frame.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.frame.FrameApplication;

public class ToastUtil {

    /**
     * 系统短吐司(因为用别人的有时候会不出现toash)
     */
    public static void showShortToast(String content) {
        if (TextUtils.isEmpty(content))
            Toast.makeText(FrameApplication.mContext, "额，好像什么也没有说~", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(FrameApplication.mContext, content, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String content) {
        if (TextUtils.isEmpty(content))
            Toast.makeText(FrameApplication.mContext, "额，好像什么也没有说~", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(FrameApplication.mContext, content, Toast.LENGTH_LONG).show();
    }
}