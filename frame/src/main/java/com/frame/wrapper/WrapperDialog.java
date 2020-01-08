package com.frame.wrapper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;


/**
 * @date 2018/7/9
 * 主要用于dialog统一点击事件
 */
public class WrapperDialog extends Dialog {
    private int[] mIds;
    private View.OnClickListener mListener;

    public WrapperDialog( Context context) {
        super(context);
    }

    public WrapperDialog( Context context, int themeResId) {
        super(context, themeResId);
    }

    protected WrapperDialog( Context context, boolean cancelable,  OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public WrapperDialog setOnClickListener(int[] ids, View.OnClickListener listener) {
        mIds = ids;
        mListener = listener;
        if (mListener != null && mIds != null && mIds.length != 0) {
            for (int id : mIds) {
                findViewById(id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onClick(view);
                    }
                });
            }
        }
        return this;
    }

}