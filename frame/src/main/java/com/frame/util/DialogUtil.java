package com.frame.util;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.frame.R;
import com.frame.wrapper.WrapperDialog;


/**
 * Dialog工具类，直接传入view,然后调用show方法即可
 */
public class DialogUtil {

    public static WrapperDialog getDialog(Context context, int view) {
        return getDialog(context, view, Gravity.CENTER);
    }

    public static WrapperDialog getDialog(Context context, int view, int location) {
        return getDialog(context, view, location, true);
    }

    public static WrapperDialog getDialog(Context context, int view, int location, boolean cancel) {
        WrapperDialog dialog = new WrapperDialog(context, R.style.ActionSheetDialogStyle);
        dialog.setCancelable(cancel);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setGravity(location);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        return dialog;
    }

}