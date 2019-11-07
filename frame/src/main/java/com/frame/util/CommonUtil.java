package com.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.frame.FrameApplication;
import com.frame.R;
import com.frame.view.TipDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 通用工具类
 */
public class CommonUtil {

    public static Context getContext() {
        return FrameApplication.mContext;
    }

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
                PermissionPageUtils.start(context, false);
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
            dialog.setContent("未开启系统通知栏权限,将影响部分功能的正常使用,请前往开启");
            dialog.setOnSureClick(view -> {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= 26) { // android 8.0引导
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
                } else if (Build.VERSION.SDK_INT >= 21) {  // android 5.0-7.0
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

    private static long lastClickTime = 0;//上次点击的时间

    /**
     * 判断控件点击事件的触发事件间隔，防止用户多次点击
     *
     * @return true重复点击 false为不是
     */
    public static boolean isFastDoubleClick() {
        long currentTime = System.currentTimeMillis();
        lastClickTime = currentTime;
        return currentTime - lastClickTime > 1000;
    }

    /**
     * 设置文本大小及颜色
     * String s = "<font color='#999999'>温馨提示: </font>" + "<font color='#FF8727'>本商品无质量问题不能退换</font>";
     */
    public static Spanned setHtmlColor(String string) {
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            spanned = Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT);
        else
            spanned = Html.fromHtml(string);
        return spanned;
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
     * 版本号比较(versionName对比方式)
     *
     * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2))
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
    public static String settingphone(String phone) {
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
     * 截取最后四位(银行卡)
     */
    public static String getLastFourNum(String numner) {
        if (TextUtils.isEmpty(numner)) return "";
        if (numner.length() < 5)
            return numner;
        else
            return numner.substring(numner.length() - 4, numner.length());
    }

    /**
     * 将银行卡中间位数设置为*号
     */
    public static String getHideBankCardNum(String bankCardNum) {
        try {
            if (TextUtils.isEmpty(bankCardNum)) return "未绑定银行卡";
            int length = bankCardNum.length();
            if (length < 8) //如果小于9位直接返回
                return bankCardNum;
            else {
                String startNum = bankCardNum.substring(0, 4);
                String endNum = bankCardNum.substring(length - 4, length);
                String str = "";
                for (int i = 0; i < bankCardNum.substring(4, bankCardNum.length() - 4).length(); i++)
                    str = str + "*";
                bankCardNum = startNum + str + endNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bankCardNum;
    }

    /**
     * 邮箱用****号隐藏前面的字母
     */
    public static String settingemail(String email) {
        if (TextUtils.isEmpty(email)) return "";
        return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }

    /**
     * 判断是否含有特殊字符
     *
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
        if (m.find()) return true;
        return false;
    }

    // 使用正则表达式来判断字符串中是否包含字母
    public static boolean judgeContainsStr(String str) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(str);
        return m.matches();
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
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            mContext.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showShortToast("浏览器打开失败");
        }
    }

    /**
     * 跳转QQ
     */
    public static void ContactQQ(Context context, String qq) {
        String url;
        if (AppUtils.isAppInstalled("com.tencent.mobileqq") || AppUtils.isAppInstalled("com.tencent.tim") || AppUtils.isAppInstalled("com.tencent.qqlite"))
            url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "";
        else
            url = "http://wpa.qq.com/msgrd?v=3&uin=" + qq + "&site=qq&menu=yes";
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    //密码8-20，不能为中文且必须包含字符，不能为纯数字
    public static boolean checkPassword(String edInput) {
        if (TextUtils.isEmpty(edInput)) {
            ToastUtil.showShortToast("密码不能为空");
            return false;
        } else if (edInput.length() < 8 || edInput.length() > 20) {
            ToastUtil.showShortToast("密码长度为8-20位");
            return false;
        } else {
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(edInput);
            if (!m.find()) {
                p = Pattern.compile("[0-9]*");
                Matcher m2 = p.matcher(edInput);
                if (m2.matches()) {
                    ToastUtil.showShortToast("密码不能为纯数字");
                    return false;
                } else {
                    return true;
                }
            } else {
                ToastUtil.showShortToast("密码不能包含中文");
                return false;
            }
        }
    }

    //检测GPS是否打开
    public static boolean checkGpsIsOpen(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //跳转GPS设置界面
    public static void IntentToOpenGps(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void IntentToOpenGps(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        fragment.startActivityForResult(intent, requestCode);
    }

    //设置消息小图标
    public static void setMsgCountIcon(int count, TextView tv) {
        if (tv == null)
            return;
        if (count == 0) {
            tv.setVisibility(View.INVISIBLE);
        } else if (count < 10) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(count);
            tv.setBackgroundResource(R.drawable.shape_msg_circular);
        } else if (count >= 10) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(count);
            tv.setBackgroundResource(R.drawable.shape_msg_square);
        } else if (count > 99) {
            tv.setVisibility(View.VISIBLE);
            tv.setText("99+");
            tv.setBackgroundResource(R.drawable.shape_msg_square);
        }
    }

    /**
     * 主要用于后台控制跳转本地
     * intentUrl举例：TaskActivity?ID=1&NAME=小明  意思就是跳转到TaskDetailActivity，然后带了ID和NAME，两个参数
     */
    public static void goLocationActivity(Context context, String intentUrl) {
        if (TextUtils.isEmpty(intentUrl))
            return;
        try {
//            if (needlogin) {//如果需要登录
//             Intent intent = new Intent();
//             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//             intent.setClass(context, getActivityClassName("LoginActivity"));
//             context.startActivity(intent);
//            } else {
            Intent intent = new Intent(context, getActivityClassName(intentUrl.contains("?") ? intentUrl.split("\\?")[0] : intentUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intentUrl.contains("?")) {//说明带参数
                for (String kv : intentUrl.split("\\?")[1].split("\\&")) {//拿到？后面的，然后对&分割处理
                    String k = kv.split("=")[0];//拿到参数名
                    String v = kv.split("=")[1];//拿到参数
                    intent.putExtra(k, v);
                }
            }
            context.startActivity(intent);
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找本地是否有这个class
     */
    public static Class getActivityClassName(String className) {
        try {
            return Class.forName(AppUtils.getAppPackageName() + ".view.activity." + className);//TODO 这里需要改成自己对应activity位置
        } catch (Exception e) {
            return null;
        }
    }
}