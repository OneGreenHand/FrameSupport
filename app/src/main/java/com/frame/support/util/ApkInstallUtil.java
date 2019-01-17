package com.frame.support.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;

import com.frame.config.AppConfig;

import java.io.File;

/**
 * description: APK安装工具类(已弃用)
 */
public class ApkInstallUtil {

    /**
     * 判断是否是8.0,8.0需要处理未知应用来源权限问题,否则直接安装
     */
    public static Intent checkIsAndroid(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = context.getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk(context, file);
                return null;
            } else {
                return startInstallPermissionSettingActivity((Activity) context);
            }
        } else {
            installApk(context, file);
            return null;
        }
    }
    /**
     * 判断是否为7.0版本APK安装
     */
    private static void installApk(Context context, File file) {
        if (context == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //添加详情见博客：https://www.cnblogs.com/rioder/archive/2011/11/02/2233584.html
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, AppConfig.PACKAGE_NAME+".provider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
    /**
     * 前往设置允许未知来源安装
     */
    private static Intent startInstallPermissionSettingActivity(Activity activity) {
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        return intent;
    }
}
