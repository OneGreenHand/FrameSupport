package com.frame.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.frame.R;


/**
 * Dialog工具类，直接传入view,然后调用show方法即可
 */
public class DialogUtil {

    public static Dialog getDialog(Context context, int view) {
        return getDialog(context, view, Gravity.CENTER);
    }

    public static Dialog getDialog(Context context, int view, int location) {
        return getDialog(context, view, location, true);
    }

    public static Dialog getDialog(Context context, int view, int location, boolean cancel) {
        Dialog dialog=new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setCancelable(cancel);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams p = window.getAttributes();
            p.width = ViewGroup.LayoutParams.MATCH_PARENT;
            p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(p);
            window.setGravity(location);
        }
        return dialog;
    }

}