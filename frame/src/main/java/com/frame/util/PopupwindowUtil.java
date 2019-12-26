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
     * @param context 上下文
     * @param layout  布局
     * @param isMatch 宽度是否MATCH_PARENT
     * @param cancel  是否可以点击取消
     * @param isAlpha 是否背景半透明
     */
    public static Solve7PopupWindow getPopupWindow(Context context, View layout, boolean isMatch, boolean cancel, boolean isAlpha) {
        Solve7PopupWindow popupWindow = new Solve7PopupWindow();
        popupWindow.setContentView(layout);
        popupWindow.setWidth(isMatch ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);  //设置PopupWindow弹出窗体动画效果
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchable(cancel);
        popupWindow.setOutsideTouchable(cancel);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (isAlpha) {
            CommonUtil.setBackgroundAlpha((Activity) context, 0.8f);  //显示阴影
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//阴影消失
                @Override
                public void onDismiss() {
                    CommonUtil.setBackgroundAlpha((Activity) context, 1.0f);
                }
            });
        }
        return popupWindow;
    }
}