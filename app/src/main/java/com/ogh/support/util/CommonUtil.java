package com.ogh.support.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.frame.util.ToastUtil;
import com.ogh.support.view.dialog.TipDialog;
import com.ogh.support.widget.HtmlParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 通用工具类
 */
public class CommonUtil {

    /**
     * 请求权限弹框
     */
    public static void getPermissions(Context context, String tips) {
        TipDialog dialog = new TipDialog(context);
        dialog.setContent(TextUtils.isEmpty(tips) ? "为了提供更好的服务,请授权APP必要的权限!单击【确定】按钮前往设置中心授权权限" : tips);
        dialog.setCancel(false);
        dialog.setOnSureClick(new TipDialog.SureCalk() {
            @Override
            public void OnClick(View view) {
                PermissionUtils.launchAppDetailsSettings();
            }
        });
        dialog.show();
    }

    /**
     * 请求打开通知权限
     */
    public static boolean notificationAuthority(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        boolean isOpened = manager.areNotificationsEnabled();//API 19以上也可以用这个判断
        if (!isOpened) {//没有授予通知权限
            TipDialog dialog = new TipDialog(context);
            dialog.setContent("系统通知未开启,将不能及时收到消息通知,请前往开启");
            dialog.setOnSureClick(view -> {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // android 8.0引导
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  // android 5.0-7.0
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", context.getPackageName());
                    intent.putExtra("app_uid", context.getApplicationInfo().uid);
                } else {   // 其他
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
            dialog.show();
        }
        return isOpened;
    }

    /**
     * 设置支持html标签
     * String s = "<font color='#999999'>温馨提示</font>";
     */
    public static Spanned setHtmlColor(String string) {
        if (TextUtils.isEmpty(string))
            return Html.fromHtml("");
        else {
            Spanned spanned;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                spanned = Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT);
            else
                spanned = Html.fromHtml(string);
            return spanned;
        }
    }

    /**
     * 设置支持html标签（额外支持文字大小、删除线,数值不能带有单位,默认就是sp）
     * String s = "<font color='#999999' size='20'>温馨提示</font>";
     */
    public static Spanned setHtmlColor2(String string) {
        return new HtmlParser().buildSpannedText(string);
    }

    /**
     * 版本号比较(versionName对比方式)
     *
     * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2) || version1.equals(version2))
            return 0;
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length); // 获取最小长度值
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0)
            index++;
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {// 如果位数不一致，比较多余位数
                if (Integer.parseInt(version1Array[i]) > 0)
                    return 1;
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0)
                    return -1;
            }
            return 0;
        } else
            return diff > 0 ? 1 : -1;
    }

    /**
     * 手机号用****号隐藏中间数字
     */
    public static String setTingPhone(String phone) {
        if (TextUtils.isEmpty(phone)) return "";
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 第一个汉字用*代替
     */
    public static String getFirstHindName(String name) {
        if (TextUtils.isEmpty(name)) return "";
        return name.replaceFirst(name.substring(0, 1), "*");
    }

    /**
     * 邮箱用****号隐藏前面的字母
     */
    public static String setTingEmail(String email) {
        if (TextUtils.isEmpty(email)) return "";
        return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1)
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        else
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        activity.getWindow().setAttributes(lp);
    }

    //跳转到浏览器
    public static void intentToBrowsable(Context mContext, String url) {
        if (TextUtils.isEmpty(url))
            return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShortToast("浏览器打开失败");
        }
    }

    /**
     * 跳转QQ
     */
    public static void ContactQQ(Context context, String qq) {
        String url;
        String q = TextUtils.isEmpty(qq) ? "40012345" : qq;
        if (AppUtils.isAppInstalled("com.tencent.mobileqq") || AppUtils.isAppInstalled("com.tencent.tim"))
            url = "mqqwpa://im/chat?chat_type=wpa&uin=" + q + "";
        else
            url = "http://wpa.qq.com/msgrd?v=3&uin=" + q + "&site=qq&menu=yes";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //检测GPS是否打开
    public static boolean checkGpsIsOpen(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //跳转GPS设置界面
    public static void IntentToOpenGps(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, requestCode);
    }

}