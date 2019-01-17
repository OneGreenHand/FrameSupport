package com.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.frame.FrameApplication;
import com.frame.util.AppManager;
import com.frame.util.InfoUtil;


/**
 * 跳转工具类
 */
public class IntentUtil {
    /**
     * @param context  上下文
     * @param activity 目标activity
     * @param bundle   携带的数据
     * @param ifLogin  跳转前是否需要登录
     * @description: Activity跳转
     */
    public static void goActivity(Context context, Class<?> activity, Bundle bundle, boolean ifLogin, boolean ifAgainCycle) {
        Intent intent = new Intent();
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ifLogin && !InfoUtil.isLogin()) {
            if (AppManager.getAppManager().contains(activity)) {//如果该Activity实例存在于任务栈中
                if (ifAgainCycle) {//就结束该Activity实例(重新走生命周期)
                    AppManager.getAppManager().finishActivity(activity);
                } else {//就复用该Activity实例(不会重新走生命周期)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                }
            }
            //intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
        } else {
            if (AppManager.getAppManager().contains(activity)) {//如果该Activity实例存在于任务栈中
                if (ifAgainCycle) {//就结束该Activity实例(重新走生命周期)
                    AppManager.getAppManager().finishActivity(activity);
                } else {//就复用该Activity实例(不会重新走生命周期)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                }
            }
            intent.setClass(context, activity);
            if (bundle == null) {
                bundle = new Bundle();
            }
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    /**
     * @param context     上下文
     * @param activity    目标的activity
     * @param bundle      携带的数据
     * @param requestCode 请求码
     * @param ifLogin     跳转前是否需要登录
     * @description: Activity跳转, 带返回结果
     */
    public static void goActivityForResult(Context context, Class<?> activity, Bundle bundle, int requestCode, boolean ifLogin, boolean ifAgainCycle) {
        Intent intent = new Intent();
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ifLogin && !InfoUtil.isLogin()) {
            if (AppManager.getAppManager().contains(activity)) {//如果该Activity实例存在于任务栈中
                if (ifAgainCycle) {//就结束该Activity实例(重新走生命周期)
                    AppManager.getAppManager().finishActivity(activity);
                } else {//就复用该Activity实例(不会重新走生命周期)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                }
            }
            //intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
        } else {
            if (AppManager.getAppManager().contains(activity)) {//如果该Activity实例存在于任务栈中
                if (ifAgainCycle) {//就结束该Activity实例(重新走生命周期)
                    AppManager.getAppManager().finishActivity(activity);
                } else {//就复用该Activity实例(不会重新走生命周期)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                }
            }
            intent.setClass(context, activity);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * @param fragment    上下文
     * @param activity    目标的activity
     * @param bundle      携带的数据
     * @param requestCode 请求码
     * @param ifLogin     跳转前是否需要登录
     * @description: fragment跳转, 带返回结果
     */
    public static void goFragmentForResult(Fragment fragment, Class<?> activity, Bundle bundle, int requestCode, boolean ifLogin, boolean ifAgainCycle) {
        Intent intent = new Intent();
        Context mContext = FrameApplication.mContext;
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ifLogin && !InfoUtil.isLogin()) {
            if (AppManager.getAppManager().contains(activity)) {//如果该Activity实例存在于任务栈中
                if (ifAgainCycle) {//就结束该Activity实例(重新走生命周期)
                    AppManager.getAppManager().finishActivity(activity);
                } else {//就复用该Activity实例(不会重新走生命周期)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                }
            }
            //intent.setClass(fragment.getActivity(), LoginActivity.class);
            mContext.startActivity(intent);
        } else {
            if (AppManager.getAppManager().contains(activity)) {//如果该Activity实例存在于任务栈中
                if (ifAgainCycle) {//就结束该Activity实例(重新走生命周期)
                    AppManager.getAppManager().finishActivity(activity);
                } else {//就复用该Activity实例(不会重新走生命周期)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                }
            }
            intent.setClass(mContext, activity);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            fragment.startActivityForResult(intent, requestCode);
        }
    }
}
