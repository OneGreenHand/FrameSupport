package com.frame.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.frame.FrameApplication;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.Serializable;
import java.util.Map;


/**
 * 跳转工具类(使用请放在主module)
 */
public class IntentUtil {

    /**
     * 跳转activity,通过bundle方式传入数据
     */
    public static void goActivity(Context context, Class<?> activity, Bundle bundle, boolean ifLogin) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在Activity上下文之外启动Activity
        if (ifLogin && !UserUtil.isLogin()) {
            //intent.setClass(context, LoginActivity.class);
            //  context.startActivity(intent);
        } else {
            intent.setClass(context, activity);
            if (bundle == null)
                bundle = new Bundle();
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转activity并回调,通过bundle方式传入数据
     */
    public static void goActivityForResult(Activity context, Class<?> activity, Bundle bundle, int requestCode, boolean ifLogin) {
        Intent intent = new Intent();
        if (ifLogin && !UserUtil.isLogin()) {
            //intent.setClass(context, LoginActivity.class);
            //  context.startActivity(intent);
        } else {
            intent.setClass(context, activity);
            if (bundle != null)
                intent.putExtras(bundle);
            context.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * fragment跳转activity,通过bundle方式传入数据
     */
    public static void goFragmentForResult(Fragment fragment, Class<?> activity, Bundle bundle, int requestCode, boolean ifLogin) {
        Intent intent = new Intent();
        Context mContext = FrameApplication.mContext;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ifLogin && !UserUtil.isLogin()) {
            //intent.setClass(fragment.getActivity(), LoginActivity.class);
            //  mContext.startActivity(intent);
        } else {
            intent.setClass(mContext, activity);
            if (bundle != null)
                intent.putExtras(bundle);
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 跳转activity,通过map方式传入数据,主要配合CommonUtil中goLocationActivity方法使用
     * 携带的数据(目前只支持 String、Boolean、Integer、Double、Long、Float、Bundle、Parcelable、Serializable)
     */
    public static void goActivity2(Context context, Class<?> activity, Map<String, Object> param, boolean ifLogin) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ifLogin && !UserUtil.isLogin()) {
            //   intent.setClass(context, LoginActivity.class);
            //    context.startActivity(intent);
        } else {
            intent.setClass(context, activity);
            if (param != null && param.size() > 0) {
                for (Map.Entry<String, Object> me : param.entrySet()) {
                    Object value = me.getValue();
                    String key = me.getKey();
                    if (value instanceof String) {
                        intent.putExtra(key, (String) value);
                    } else if (value instanceof Boolean) {
                        intent.putExtra(key, (boolean) value);
                    } else if (value instanceof Integer) {
                        intent.putExtra(key, (int) value);
                    } else if (value instanceof Double) {
                        intent.putExtra(key, (double) value);
                    } else if (value instanceof Long) {
                        intent.putExtra(key, (long) value);
                    } else if (value instanceof Float) {
                        intent.putExtra(key, (float) value);
                    } else if (value instanceof Bundle) {
                        intent.putExtra(key, (Bundle) value);
                    } else if (value instanceof Parcelable) {
                        intent.putExtra(key, (Parcelable) value);
                    } else if (value instanceof Serializable) {
                        intent.putExtra(key, (Serializable) value);
                    }
                }
            }
            context.startActivity(intent);
        }
    }

    /**
     * 跳转activity并回调,通过map方式传入数据,主要配合CommonUtil中goLocationActivity方法使用
     * 携带的数据(目前只支持 String、Boolean、Integer、Double、Long、Float、Bundle、Parcelable、Serializable)
     */
    public static void goActivityForResult2(Activity context, Class<?> activity, Map<String, Object> param, int requestCode, boolean ifLogin) {
        Intent intent = new Intent();
        if (ifLogin && !UserUtil.isLogin()) {
            //     intent.setClass(context, LoginActivity.class);
            //  context.startActivity(intent);
        } else {
            intent.setClass(context, activity);
            if (param != null && param.size() > 0) {
                for (Map.Entry<String, Object> me : param.entrySet()) {
                    Object value = me.getValue();
                    String key = me.getKey();
                    if (value instanceof String) {
                        intent.putExtra(key, (String) value);
                    } else if (value instanceof Boolean) {
                        intent.putExtra(key, (boolean) value);
                    } else if (value instanceof Integer) {
                        intent.putExtra(key, (int) value);
                    } else if (value instanceof Double) {
                        intent.putExtra(key, (double) value);
                    } else if (value instanceof Long) {
                        intent.putExtra(key, (long) value);
                    } else if (value instanceof Float) {
                        intent.putExtra(key, (float) value);
                    } else if (value instanceof Bundle) {
                        intent.putExtra(key, (Bundle) value);
                    } else if (value instanceof Parcelable) {
                        intent.putExtra(key, (Parcelable) value);
                    } else if (value instanceof Serializable) {
                        intent.putExtra(key, (Serializable) value);
                    }
                }
            }
            context.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * fragment跳转activity,通过map方式传入数据,主要配合CommonUtil中goLocationActivity方法使用
     * 携带的数据(目前只支持 String、Boolean、Integer、Double、Long、Float、Bundle、Parcelable、Serializable)
     */
    public static void goFragmentForResult2(Fragment fragment, Class<?> activity, Map<String, Object> param, int requestCode, boolean ifLogin) {
        Intent intent = new Intent();
        Context mContext = FrameApplication.mContext;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ifLogin && !UserUtil.isLogin()) {
            //    intent.setClass(fragment.getActivity(), LoginActivity.class);
            //    mContext.startActivity(intent);
        } else {
            intent.setClass(mContext, activity);
            if (param != null && param.size() > 0) {
                for (Map.Entry<String, Object> me : param.entrySet()) {
                    Object value = me.getValue();
                    String key = me.getKey();
                    if (value instanceof String) {
                        intent.putExtra(key, (String) value);
                    } else if (value instanceof Boolean) {
                        intent.putExtra(key, (boolean) value);
                    } else if (value instanceof Integer) {
                        intent.putExtra(key, (int) value);
                    } else if (value instanceof Double) {
                        intent.putExtra(key, (double) value);
                    } else if (value instanceof Long) {
                        intent.putExtra(key, (long) value);
                    } else if (value instanceof Float) {
                        intent.putExtra(key, (float) value);
                    } else if (value instanceof Bundle) {
                        intent.putExtra(key, (Bundle) value);
                    } else if (value instanceof Parcelable) {
                        intent.putExtra(key, (Parcelable) value);
                    } else if (value instanceof Serializable) {
                        intent.putExtra(key, (Serializable) value);
                    }
                }
            }
            fragment.startActivityForResult(intent, requestCode);
        }
    }
}
