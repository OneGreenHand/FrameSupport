package com.frame.util;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.frame.R;

public class ToastUtil {

    private static Toast toastCenter = null;

    public static void showCenterToast(Context context, String content) {
        //加载Toast布局
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        //为控件设置属性
        TextView tv = view.findViewById(R.id.content);
        tv.setText(content);
        //Toast的初始化
        if (toastCenter != null) {
            toastCenter.cancel();
        }
        toastCenter = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastCenter.setGravity(Gravity.BOTTOM, 0, height / 5);
        toastCenter.setDuration(Toast.LENGTH_SHORT);
        toastCenter.setView(view);
        toastCenter.show();
    }
}