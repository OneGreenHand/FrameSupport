package com.frame.util;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.frame.R;
import com.frame.wrapper.WrapperDialog;


/**
 * Dialog工具类，直接传入view,然后调用show方法即可
 */
public class DialogUtil {

    public static WrapperDialog getDialog(Context context, int view) {
        return getDialog(context, view, 4);
    }

    public static WrapperDialog getDialog(Context context, int view, int location) {
        return getDialog(context, view, location, true);
    }


    /**
     * @param cancel   是否可以取消
     * @param location 位置(0 TOP 1 BOTTOM  2 LEFT 3 RIGHT 4 CENTER 5 CENTER_VERTICAL 6 CENTER_HORIZONTAL)
     */
    public static WrapperDialog getDialog(Context context, int view, int location, boolean cancel) {
        WrapperDialog dialog;
        dialog = new WrapperDialog(context, R.style.ActionSheetDialogStyle);
        dialog.setCancelable(cancel);//弹出后会点击屏幕或物理返回键，dialog不消失
        dialog.setCanceledOnTouchOutside(cancel);// 弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        dialog.getWindow().setContentView(view);
        if (location == 0) {
            dialog.getWindow().setGravity(Gravity.TOP);
        } else if (location == 1) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        } else if (location == 2) {
            dialog.getWindow().setGravity(Gravity.LEFT);
        } else if (location == 3) {
            dialog.getWindow().setGravity(Gravity.RIGHT);
        } else if (location == 4) {
            dialog.getWindow().setGravity(Gravity.CENTER);
        } else if (location == 5) {
            dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        } else if (location == 6) {
            dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        }
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        return dialog;
    }

}
