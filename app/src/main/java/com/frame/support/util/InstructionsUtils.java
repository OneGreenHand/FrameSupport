package com.frame.support.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.frame.support.R;
import com.frame.support.service.DownloadService;
import com.frame.util.CommonUtil;
import com.frame.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author luo
 * @fileName IntentUtil
 * @data on 2019/5/30 18:56
 */
public class InstructionsUtils {

    /**
     * @param type     操作类别： 0、跳转本地 1、打开外部浏览器 2、通知栏下载APK 3、打开指定QQ 4、打开APP
     * @param url      跳转URL：根据type变动，可能是本地activity或者浏览器之类的
     * @param packName 下载专用：如果包名不为空，就检查是否安装，已安装就不下载
     */
    public static void JumpIntention(Context context, int type, String packName, String url) {
        switch (type) {
            case 0:
                CommonUtil.goLocationActivity(context, url);
                break;
            case 1:
                CommonUtil.intentToBrowsable(context, url);
                break;
            case 2:
                downloadApk(context, packName, "", url, true);
                break;
            case 3:
                CommonUtil.ContactQQ(context, url);
                break;
            case 4:
                checkInstall(context, true, packName, "", "", false);
                break;
        }
    }

    /**
     * @param type     操作类别： 0、跳转本地 1、打开外部浏览器 2、通知栏下载APK 3、打开指定QQ 4、微信分享 5、打开APP
     * @param url      跳转URL：根据type变动，可能是本地activity或者浏览器之类的
     * @param packName 下载专用：如果包名不为空，就检查是否安装，已安装就不下载
     */
    public static void JumpIntention(Activity activity, int type, String packName, String url) {
        switch (type) {
            case 0:
                CommonUtil.goLocationActivity(activity, url);
                break;
            case 1:
                CommonUtil.intentToBrowsable(activity, url);
                break;
            case 2:
                downloadApk(activity, packName, "", url, true);
                break;
            case 3:
                CommonUtil.ContactQQ(activity, url);
                break;
            case 4:
                checkInstall(activity, true, packName, "", "", false);
                break;
        }
    }

    /**
     * 通过service下载apk
     *
     * @param packNmae       包名，如果不为空就检查是否安装了
     * @param title          下载时显示的标题
     * @param url            下载地址
     * @param isShowProgress 是否通知栏显示下载进度(必须要打开了通知栏)，默认为true
     */
    public static void downloadApk(Context context, String packNmae, String title, String url, boolean isShowProgress) {
        if (TextUtils.isEmpty(packNmae)) {
            if (TextUtils.isEmpty(url)) {
                ToastUtil.showShortToast("下载地址不能为空，请稍后重试~");
                return;
            }
            if (hasPermission(context))
                download(context, title, url, isShowProgress);
            else
                ToastUtil.showShortToast("暂无相关储存权限，无法下载该应用！");
        } else {//检查是否安装
            checkInstall(context, false, packNmae, title, url, isShowProgress);
        }
    }

    /**
     * 通过service下载apk
     *
     * @param packNmae       包名，如果不为空就检查是否安装了
     * @param title          下载时显示的标题
     * @param url            下载地址
     * @param isShowProgress 是否通知栏显示下载进度(必须要打开了通知栏)，默认为true
     */
    @SuppressLint("CheckResult")
    public static void downloadApk(Activity activity, String packNmae, String title, String url, boolean isShowProgress) {
        if (TextUtils.isEmpty(packNmae)) {
            if (TextUtils.isEmpty(url)) {
                ToastUtil.showShortToast("下载地址不能为空，请稍后重试~");
                return;
            }
            if (activity instanceof FragmentActivity) {
                RxPermissions rxPermissions = new RxPermissions((FragmentActivity) activity);
                rxPermissions.requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission.granted) {//权限申请成功
                                download(activity, title, url, isShowProgress);
                            } else if (permission.shouldShowRequestPermissionRationale) {//拒绝申请权限
                                ToastUtil.showShortToast("由于您拒绝了存储权限申请，该功能无法使用！");
                            } else {//不在提醒申请权限
                                ToastUtil.showShortToast("由于您拒绝了存储权限申请，该功能无法使用！");
                            }
                        });
            } else {
                ToastUtil.showShortToast("暂无相关储存权限，无法下载该应用！");
            }
        } else {//检查是否安装
            checkInstall(activity, false, packNmae, title, url, isShowProgress);
        }
    }

    /**
     * 是否已经安装了这款APP
     */
    private static boolean isInstall(String packageName) {
        boolean isExistence = false;
        if (TextUtils.isEmpty(packageName))
            return isExistence;
        isExistence = AppUtils.isAppInstalled(packageName);
        return isExistence;
    }

    /**
     * 是否已经安装了这款APP
     *
     * @param context
     * @param isOpen         是否为打开app操作，false为下载,true为打开APP
     * @param packageName    包名
     * @param title          下载显示标题
     * @param url            下载的地址
     * @param isShowProgress 下载是否显示进度，默认为true
     */
    @SuppressLint("CheckResult")
    public static void checkInstall(Context context, boolean isOpen, String packageName, String title, String url, boolean isShowProgress) {
        Observable.just(1)//判断是否安装了该应用，为耗时操作
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer integer) throws Exception {
                        return isInstall(packageName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean s) throws Exception {
                        if (isOpen) {//需要打开app
                            if (!s) //未安装
                                ToastUtil.showShortToast("应用未安装！");
                            else
                                AppUtils.launchApp(packageName);
                        } else {//下载
                            if (!s) //未安装
                                download(context, title, url, isShowProgress);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showShortToast("出现异常，请稍后重试！");
                    }
                });
    }

    /**
     * 下载
     *
     * @param title          下载要显示的标题
     * @param url            下载的地址
     * @param isShowProgress 是否通知栏显示进度
     */
    private static void download(Context context, String title, String url, boolean isShowProgress) {
        String fileName = "";
        if (url.contains("/"))
            fileName = url.substring(url.lastIndexOf("/")).replace("/", "");
        else
            fileName = context.getResources().getString(R.string.app_name) + System.currentTimeMillis() + ".apk";
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("fileUrl", url);
        intent.putExtra("fileName", fileName);
        intent.putExtra("title", title);
        intent.putExtra("isShowProgress", isShowProgress);
        context.startService(intent);
    }

    /**
     * 检测是否有读写权限(上下文不为activity会使用)
     */
    private static boolean hasPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission_readStorage = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", AppUtils.getAppPackageName()));
        boolean permission_writeStorage = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", AppUtils.getAppPackageName()));
        return permission_readStorage && permission_writeStorage;
    }

}