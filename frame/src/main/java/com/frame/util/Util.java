package com.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


import com.frame.FrameApplication;
import com.frame.view.CommonPromptDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 工具类
 */
public class Util {

    private static CommonPromptDialog dialog;
    private static long lastClickTime;
    private static String TOKEN;
    // private static UserInfoEntity userInfo;
    /*************************登录和退出相关操作开始*******************************/
    /**
     * 获取token
     */
    public static String getTOKEN() {
        if (TOKEN == null || TOKEN.trim().equals("")) {
            TOKEN = SharedPreferencesUtil.getString(FrameApplication.mContext, "TOKEN", null);
        }
        return TOKEN;
    }

    /**
     * 设置token
     */
    public static void setTOKEN(String TOKEN) {
        SharedPreferencesUtil.putString(FrameApplication.mContext, "TOKEN", TOKEN);
        Util.TOKEN = TOKEN;
    }

    /**
     * 判断用户是否登录
     * 目前是根据token来判断
     */
    public static boolean isLogin() {
        if (getTOKEN() != null && !getTOKEN().trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 退出登录
     */
    public static void logout() {
        SharedPreferencesUtil.putString(FrameApplication.mContext, "TOKEN", null);
        TOKEN = null;
    }
//    /**
//     * 得到用户信息
//     */
//    public static UserInfoEntity getUserInfo() {
//        if (userInfo != null) {
//            return userInfo;
//        } else {
//            String infoJson = SharedPreferencesUtil.getString(AppContext.mContext, "userInfo", null);
//            if (infoJson == null) {
//                return null;
//            } else {
//                UserInfoEntity bean = GsonUtil.getBean(infoJson, UserInfoEntity.class);
//                return bean;
//            }
//        }
//    }
//
//    /**
//     * 储存用户信息
//     */
//    public static void setUserInfo(UserInfoEntity userInfo) {
//        Util.userInfo = userInfo;
//        Gson gson = new Gson();
//        String infoJson = gson.toJson(userInfo);
//        SharedPreferencesUtil.putString(AppContext.mContext, "userInfo", infoJson);
//    }
    /*************************登录和退出相关操作结束*******************************/


    /**
     * 请求权限弹框
     */
    public static void getPermissions(final Context context, String tips) {
        if (dialog == null)
            dialog = new CommonPromptDialog(context);
        if (tips == null || tips.trim().isEmpty()) {
            dialog.setContentText("应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。");
        } else {
            dialog.setContentText(tips);
        }
        dialog.setCancel(false);
        dialog.show();
        dialog.setOnSureClick(new CommonPromptDialog.SureCalk() {
            @Override
            public void OnClick(View view) {
                PermissionPageUtils.start(context, false);
            }
        });
    }

    /**
     * 请求打开通知权限
     */
    public static void notificationAuthority(final Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        boolean isOpened = manager.areNotificationsEnabled();//API 19以上也可以用这个判断
        if (!isOpened) {//没有授予通知权限
            CommonPromptDialog dialog = new CommonPromptDialog(context);
            dialog.setContentText( "检测到你未开启系统通知栏权限，将影响部分功能正常使用，是否前往开启？");
            dialog.show();
            dialog.setOnSureClick(new CommonPromptDialog.SureCalk() {
                @Override
                public void OnClick(View view) {
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= 26) {
                        // android 8.0引导
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
                    } else if (Build.VERSION.SDK_INT >= 21) {
                        // android 5.0-7.0
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("app_package", context.getPackageName());
                        intent.putExtra("app_uid", context.getApplicationInfo().uid);
                    } else {
                        // 其他
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }


    /**
     * 判断控件点击事件的触发事件间隔，防止用户多次点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    /**
     * 版本号比较
     * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.d("HomePageActivity", "version1Array==" + version1Array.length);
        Log.d("HomePageActivity", "version2Array==" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.d("HomePageActivity", "verTag2=2222=" + version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
    /**
     * 手机号用****号隐藏中间数字
     */
    public static String settingphone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return "";
        }
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;
    }

    /**
     * 第一个汉字用*代替
     */
    public static String getFirstHindName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        String hind_name = name.replaceFirst(name.substring(0, 1), "*");
        return hind_name;
    }

    /**
     * 截取最后四位(银行卡)
     */
    public static String getLastFourNum(String numner) {
        if (numner == null || numner.isEmpty()) {
            return "";
        }
        if (numner.length() < 5) {
            return numner;
        } else {
            String hind_name = numner.substring(numner.length() - 4, numner.length());
            return hind_name;
        }
    }


    /**
     * 邮箱用****号隐藏前面的字母
     */
    public static String settingemail(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }
        String emails = email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        return emails;
    }

    /**
     * 判断是否含有特殊字符
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    //是否包含中文 true为包含，false为不包含
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 设置页面的透明度
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }

}