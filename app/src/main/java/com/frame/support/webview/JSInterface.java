package com.frame.support.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.frame.support.util.ChannelUtils;
import com.frame.support.util.InstructionsUtils;
import com.frame.support.util.ShareUtils;
import com.frame.util.CommonUtil;
import com.frame.util.ToastUtil;

/**
 * Description:
 * -
 * Author：chasen
 * Date： 2018/11/27 18:11
 */
@SuppressLint("MissingPermission")
public class JSInterface {

    Context context;

    public JSInterface(Context context) {
        this.context = context;
    }
    //1.0.0版本方法

    @JavascriptInterface
    public String getVersion() {
        return AppUtils.getAppVersionName();
    }

    @JavascriptInterface
    public String getOSVersion() {
        return DeviceUtils.getSDKVersionName();
    }

    @JavascriptInterface
    public String getChannle() {
        return ChannelUtils.getChannel();
    }

    @JavascriptInterface
    public String getIMSI() {
        return PhoneUtils.getIMSI();
    }

    @JavascriptInterface
    public void shareWeChatUrl(String title, String desc, String url, int type, String callBack) {
        ShareUtils.shareWeChatUrl(title, desc, url, type, callBack);
    }

    @JavascriptInterface
    public void shareWeChatTxt(String content, int type, String callBack) {
        ShareUtils.shareWeChatTxt(content, type, callBack);
    }

    //1.1.8版本方法
    @JavascriptInterface
    public void showToast(String msg) {
        ToastUtil.showShortToast(msg);
    }

    @JavascriptInterface
    public void showAlertDialog(String title, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.show();
    }

    @JavascriptInterface
    public String getIMEI() {
        return PhoneUtils.getIMEI();
    }

    @JavascriptInterface
    public void doFinsih(int result) {
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).setResult(result);
            ((AppCompatActivity) context).finish();
        }
    }

    //打开外部浏览器
    @JavascriptInterface
    public void Browser(final String url) {
        CommonUtil.intentToBrowsable(context, url);
    }

    //下载app
    @JavascriptInterface
    public void InstallAPP(final String url) {
        InstructionsUtils.downloadApk(context, null, "", url, true);
    }
}
