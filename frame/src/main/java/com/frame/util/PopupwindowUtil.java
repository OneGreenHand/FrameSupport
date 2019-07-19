package com.frame.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.frame.R;
import com.frame.widget.Solve7PopupWindow;

/**
 * Popupwindow工具类，直接传入view然后调用显示即可
 */
public class PopupwindowUtil {

    /**
     * @param context  上下文
     * @param layout   布局
     * @param isMathch 宽度是否MATCH_PARENT
     * @param cancel   是否可以点击取消
     * @param isAlpha  是否背景半透明
     */
    public static Solve7PopupWindow getPopupwindow(Context context, View layout, boolean isMathch, boolean cancel, boolean isAlpha) {
        Solve7PopupWindow popupWindow = new Solve7PopupWindow();
        //设置PopupWindow的View
        popupWindow.setContentView(layout);
        //设置PopupWindow弹出窗体的宽
        if (isMathch) {
            popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        } else {
            popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        }
        //设置PopupWindow弹出窗体的高
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        //设置PopupWindow可触摸
        popupWindow.setTouchable(cancel);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //设置非PopupWindow区域是否可触摸
        popupWindow.setOutsideTouchable(cancel);
        //设置PopupWindow可触摸
        popupWindow.setTouchable(cancel);
        //设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(cancel);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (isAlpha) {
            //设置PopupWindow弹出后有阴影
            CommonUtil.setBackgroundAlpha((Activity) context, 0.8f);
            //设置PopupWindow消失后阴影也消失
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    CommonUtil.setBackgroundAlpha((Activity) context, 1.0f);
                }
            });
        }
        return popupWindow;
    }
}