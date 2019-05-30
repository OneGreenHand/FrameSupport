package com.frame.support.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.frame.support.service.DownloadService;
import com.frame.util.CommonUtil;
import com.frame.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author luo
 * @package com.frame.support.util
 * @fileName IntentUtil
 * @data on 2019/5/30 18:56
 */
public class IntentUtils {
    /**
     * @param type     0、跳转本地 1、打开外部浏览器 2、通知栏下载APK 3、打开指定QQ
     * @param url      可能是本地activity或者浏览器之类的
     * @param context  非activity使用，下载只能只判断权限
     * @param activity Activity专用，下载功能拥有权限请求功能
     */
    public static void JumpIntention(Context context, Activity activity, int type, String url) {
        switch (type) {
            case 0:
                if (context == null)
                    CommonUtil.goLocationActivity(activity, url);
                else
                    CommonUtil.goLocationActivity(context, url);
                break;
            case 1:
                if (context == null)
                    CommonUtil.intentToBrowsable(activity, url);
                else
                    CommonUtil.intentToBrowsable(context, url);
                break;
            case 2:
                downloadApk(context, activity, "", url);
                break;
            case 3:
                if (context == null)
                    CommonUtil.ContactQQ(activity, url);
                else
                    CommonUtil.ContactQQ(context, url);
                break;
        }
    }

    /**
     * 用service下载apk
     */
    @SuppressLint("CheckResult")
    public static void downloadApk(Context context, Activity activity, String title, String url) {
        if (TextUtils.isEmpty(url))
            return;
        if (activity == null) {
            if (hasPermission(context))
                download(context, title, url);
            else
                ToastUtil.showShortToast("暂无相关储存权限，无法下载该应用！");
        } else {
            RxPermissions rxPermissions = new RxPermissions((FragmentActivity) activity);
            rxPermissions.requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(permission -> {
                        if (permission.granted) {//权限申请成功
                            download(activity, title, url);
                        } else if (permission.shouldShowRequestPermissionRationale) {//拒绝申请权限
                            ToastUtil.showShortToast("您拒绝了存储权限申请，该功能无法使用！");
                        } else {//不在提醒申请权限
                            ToastUtil.showShortToast("您拒绝了存储权限申请，该功能无法使用！");
                        }
                    });
        }

    }

    private static void download(Context context, String title, String url) {
        String fileName = "";
        if (url.contains("/"))
            fileName = url.substring(url.lastIndexOf("/")).replace("/", "");
        else
            fileName = "聚赚" + System.currentTimeMillis() + ".apk";
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("fileUrl", url);
        intent.putExtra("fileName", fileName);
        intent.putExtra("title", title);
        context.startService(intent);
        ToastUtil.showShortToast("正在为您后台下载应用中...");
    }

    /**
     * 检测是否有读写权限
     */
    public static boolean hasPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission_readStorage = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", "com.mcht.redpacket"));
        boolean permission_writeStorage = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.mcht.redpacket"));
        return permission_readStorage && permission_writeStorage;
    }

}
