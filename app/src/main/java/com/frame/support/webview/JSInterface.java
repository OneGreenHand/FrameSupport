package com.frame.support.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.frame.support.util.ChannelUtils;
import com.frame.util.ToastUtil;
import com.frame.view.TipDialog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 备注：在这里面的方法都是子线程
 */
@SuppressLint({"MissingPermission", "CheckResult"})
public class JSInterface {

    Context context;

    public JSInterface(Context context) {
        this.context = context;
    }

    //获取渠道号
    @JavascriptInterface
    public String getChannel() {
        return ChannelUtils.getChannel();
    }

    // 获取 App 版本号
    @JavascriptInterface
    public String getVersion() {
        return AppUtils.getAppVersionName();
    }

    //获取设备系统版本号
    @JavascriptInterface
    public String getOSVersion() {
        return DeviceUtils.getSDKVersionName();
    }

    //获取设备厂商
    @JavascriptInterface
    public String getManufacturer() {
        return DeviceUtils.getManufacturer();
    }

    //获取AndroidID
    @JavascriptInterface
    public String getAndroidID() {
        return DeviceUtils.getAndroidID();
    }

    //获取IMSI码(需要权限)
    @JavascriptInterface
    public String getIMSI() {
        return hasPhonePermission(context) ? PhoneUtils.getIMSI() : "";
    }

    //获取设备码(需要权限)
    @JavascriptInterface
    public String getDeviceId() {
        return hasPhonePermission(context) ? PhoneUtils.getDeviceId() : "";
    }

    //获取IMEI码(需要权限)
    @JavascriptInterface
    public String getIMEI() {
        return hasPhonePermission(context) ? PhoneUtils.getIMEI() : "";
    }


    //吐司
    @JavascriptInterface
    public void showToast(String msg) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        ToastUtil.showShortToast(msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e("Observable", "Js发生错误");
                    }
                });
    }

    //弹框
    @JavascriptInterface
    public void showDialog(String title, String msg) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        TipDialog dialog = new TipDialog(context);
                        dialog.setTitle(title);
                        dialog.setContent(msg);
                        dialog.show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e("Observable", "Js发生错误");
                    }
                });
    }

    //结束当前页面
    @JavascriptInterface
    public void doFinish() {
        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).finish();
    }

    /**
     * 检测是否有电话权限
     */
    private boolean hasPhonePermission(Context context) {
        return (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", AppUtils.getAppPackageName()));
    }
}