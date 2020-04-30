package com.frame.wrapper;

import android.app.Dialog;
import android.content.Context;

import com.frame.util.CustomClickListener;


/**
 * @date 2018/7/9
 * 主要用于dialog统一点击事件
 */
public class WrapperDialog extends Dialog {

    public WrapperDialog(Context context) {
        super(context);
    }

    public WrapperDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected WrapperDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public WrapperDialog setOnClickListener(int[] ids, CustomClickListener listener) {
        if (listener != null && ids != null && ids.length != 0) {
            for (int id : ids)
                findViewById(id).setOnClickListener(listener);
        }
        return this;
    }

}