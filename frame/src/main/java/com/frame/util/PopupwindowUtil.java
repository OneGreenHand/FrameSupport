package com.frame.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.frame.widget.Solve7PopupWindow;

/**
 * Popupwindow工具类，直接传入view然后调用显示即可
 */
public class PopupwindowUtil {

    /**
     * @param layout  布局
     * @param isMatch 宽度是否MATCH_PARENT
     * @param cancel  是否可以点击取消
     * @param isAlpha 是否背景半透明
     */
    public static Solve7PopupWindow getPopupWindow(Context context, View layout, boolean isMatch, boolean cancel, boolean isAlpha) {
        Solve7PopupWindow popupWindow = new Solve7PopupWindow();
        popupWindow.setContentView(layout);
        popupWindow.setWidth(isMatch ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(cancel);// 设置popupwindow外部可点击
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);//让pop覆盖在输入法上面
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//软键盘顶起当前界面,当前布局的高度会发生变化
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