package com.frame.support.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.frame.FrameApplication;
import com.frame.support.util.ChannelUtils;
import com.frame.support.util.InstructionsUtils;
import com.frame.support.util.WeChatShareUtils;
import com.frame.util.CommonUtil;
import com.frame.util.ToastUtil;
import com.frame.view.TipDialog;

@SuppressLint("MissingPermission")
public class JSInterface {

    Context context;

    public JSInterface(Context context) {
        this.context = context;
    }

    // 某种操作
    @JavascriptInterface
    public void openModule(int type, String url) {
        if (context instanceof AppCompatActivity)
            InstructionsUtils.JumpIntention(((AppCompatActivity) context), type, "", url);
        else
            InstructionsUtils.JumpIntention(context, type, "", url);
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

    //获取渠道号
    @JavascriptInterface
    public String getChannel() {
        return ChannelUtils.getChannel();
    }

    //获取IMSI码(需要权限)
    @JavascriptInterface
    public String getIMSI() {
        if (!hasPhonePermission(FrameApplication.mContext)) {
            return "";
        } else {
            return PhoneUtils.getIMSI();
        }
    }

    //获取设备码(需要权限)
    @JavascriptInterface
    public String getDeviceId() {
        if (!hasPhonePermission(FrameApplication.mContext)) {
            return "";
        } else {
            return PhoneUtils.getDeviceId();
        }
    }

    //获取IMEI码(需要权限)
    @JavascriptInterface
    public String getIMEI() {
        if (!hasPhonePermission(FrameApplication.mContext)) {
            return "";
        } else {
            return PhoneUtils.getIMEI();
        }
    }

    //获取设备厂商
    @JavascriptInterface
    public String getManufacturer() {
        return DeviceUtils.getManufacturer();
    }

    //分享微信链接
    @JavascriptInterface
    public void shareWeChatUrl(String title, String desc, String url, int type, String callBack) {
        WeChatShareUtils.shareWeChatUrl(title, desc, url, type, callBack);
    }

    //分享微信文本
    @JavascriptInterface
    public void shareWeChatTxt(String content, int type, String callBack) {
        WeChatShareUtils.shareWeChatTxt(content, type, callBack);
    }

    //吐司
    @JavascriptInterface
    public void showToast(String msg) {
        ToastUtil.showShortToast(msg);
    }

    //弹框
    @JavascriptInterface
    public void showDialog(String title, String msg) {
        TipDialog dialog = new TipDialog(context);
        dialog.setTitle(title);
        dialog.setContent(msg);
        dialog.show();
    }

    //结束当前页面
    @JavascriptInterface
    public void doFinsih() {
        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).finish();
    }

    //打开app
    @JavascriptInterface
    public void openAPP(String packageName) {
        InstructionsUtils.checkInstall(null, true, packageName, "", "", false);
    }

    //下载app
    @JavascriptInterface
    public void installAPP(String url, String packNmae) {
        if (context instanceof AppCompatActivity)
            InstructionsUtils.downloadApk((AppCompatActivity) context, packNmae, "", url, true);
        else
            InstructionsUtils.downloadApk(context, packNmae, "", url, true);
    }

    //打开外部浏览器
    @JavascriptInterface
    public void openBrowser(String url) {
        CommonUtil.intentToBrowsable(context, url);
    }

    /**
     * 检测是否有电话权限
     */
    private boolean hasPhonePermission(Context context) {
        return (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", AppUtils.getAppPackageName()));
    }
}