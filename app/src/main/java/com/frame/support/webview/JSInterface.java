package com.frame.support.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.frame.FrameApplication;
import com.frame.support.util.ChannelUtils;
import com.frame.util.ToastUtil;
import com.frame.view.TipDialog;


/**
 * 备注: 在这里面的方法都是子线程
 */
@SuppressLint({"MissingPermission", "CheckResult"})
public class JSInterface {

    Context context;
    // 将请求成功的数据返回到主线程进行数据更新
    Handler mHandler = new Handler(FrameApplication.getContext().getMainLooper());

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

    //获取唯一设备 ID
    @JavascriptInterface
    public String getDeviceId() {
        return DeviceUtils.getUniqueDeviceId();
    }

    //吐司
    @JavascriptInterface
    public void showToast(String msg) {
        mHandler.post(() -> ToastUtil.showShortToast(msg));
    }

    //弹框
    @JavascriptInterface
    public void showDialog(String title, String msg) {
        mHandler.post(() -> {
            TipDialog dialog = new TipDialog(context);
            dialog.setTitle(title);
            dialog.setContent(msg);
            dialog.show();
        });
    }

    //结束当前页面
    @JavascriptInterface
    public void doFinish() {
        if (context instanceof Activity)
            ((Activity) context).finish();
    }

    /**
     * 检测是否有电话权限
     */
    private boolean hasPhonePermission(Context context) {
        return (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", AppUtils.getAppPackageName()));
    }
}