package com.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.FrameApplication;
import com.frame.R;
import com.frame.view.CommonPromptDialog;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 工具类
 */
public class CommonUtil {

    private static CommonPromptDialog dialog;
    private static long lastClickTime;

    private CommonUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Context getContext() {
        return FrameApplication.mContext;
    }

    /**
     * 请求权限弹框
     */
    public static void getPermissions(final Context context, String tips) {
        if (dialog == null)
            dialog = new CommonPromptDialog(context);
        if (tips == null || tips.trim().isEmpty())
            dialog.setContentText("为了给您提供更好的服务，请设置相应权限哦！如若需要，请单击【确定】按钮前往设置中心进行权限授权。");
        else
            dialog.setContentText(tips);
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
            dialog.setContentText("检测到你未开启系统通知栏权限，将影响部分功能正常使用，是否前往开启？");
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
        if (0 < timeD && timeD < 800)
            return true;
        lastClickTime = time;
        return false;
    }

    /**
     * 设置文本大小及颜色
     */
    public static Spanned setHtmlColor(String string) {
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT);
        } else {
            spanned = Html.fromHtml(string);
        }
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
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
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
        } else
            return diff > 0 ? 1 : -1;
    }

    /**
     * 手机号用****号隐藏中间数字
     */
    public static String settingphone(String phone) {
        if (phone == null || phone.isEmpty())
            return "";
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;
    }

    /**
     * 第一个汉字用*代替
     */
    public static String getFirstHindName(String name) {
        if (name == null || name.isEmpty())
            return "";
        String hind_name = name.replaceFirst(name.substring(0, 1), "*");
        return hind_name;
    }

    /**
     * 截取最后四位(银行卡)
     */
    public static String getLastFourNum(String numner) {
        if (numner == null || numner.isEmpty())
            return "";
        if (numner.length() < 5)
            return numner;
        else {
            String hind_name = numner.substring(numner.length() - 4, numner.length());
            return hind_name;
        }
    }

    /**
     * 邮箱用****号隐藏前面的字母
     */
    public static String settingemail(String email) {
        if (email == null || email.isEmpty())
            return "";
        String emails = email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        return emails;
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
        if (m.find())
            return true;
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
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        mContext.startActivity(intent);
    }

    /**
     * 跳转QQ
     */
    public static void ContactQQ(Context context, String qq) {
        String url = "";
        if (isAvilible(context, "com.tencent.mobileqq") || isAvilible(context, "com.tencent.tim") || isAvilible(context, "com.tencent.qqlite")) {
            url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "";
        } else {
            url = "http://wpa.qq.com/msgrd?v=3&uin=" + qq + "&site=qq&menu=yes";
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    /**
     * 检查手机上是否安装了指定的软件
     */
    public static boolean isAvilible(Context context, String packageName) {
        if (packageName == null || packageName.isEmpty())
            return false;
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos == null || packageInfos.isEmpty())
            return false;
        for (int i = 0; i < packageInfos.size(); i++) {
            if (packageInfos.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    //密码8-20，不能为中文且必须包含字符，不能为纯数字
    public static boolean checkPassword(Context mContext, EditText edInput) {
        String txt = edInput.getText().toString().trim();
        if (txt.isEmpty()) {
            ToastUtil.showCenterToast(mContext, "密码输入不能为空");
            return false;
        } else if (txt.length() < 8) {
            ToastUtil.showCenterToast(mContext, "密码长度为8-20位");
            return false;
        } else {
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(txt);
            if (!m.find()) {
                p = Pattern.compile("[0-9]*");
                Matcher m2 = p.matcher(txt);
                if (m2.matches()) {
                    ToastUtil.showCenterToast(mContext, "密码不能为纯数字");
                    return false;
                } else {
                    return true;
                }
            } else {
                ToastUtil.showCenterToast(mContext, "密码不能包含中文");
                return false;
            }
        }
    }

    //检测GPS是否打开
    public static boolean checkGPSIsOpen(Context mContext) {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    //跳转GPS设置界面
    public static void fragmentIntentToOpenGPS(Context context, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        Object obj = context;
        if (obj instanceof FragmentActivity) {
            ((FragmentActivity) obj).startActivityForResult(intent, requestCode);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).startActivityForResult(intent, requestCode);
        }
    }

    //设置消息小图标
    public static void setMsgCountIcon(String count, TextView tv) {
        if (!count.isEmpty()) {
            if (Integer.parseInt(count) == 0) {
                tv.setVisibility(View.INVISIBLE);
            } else if (Integer.parseInt(count) < 10) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(count);
                tv.setBackgroundResource(R.drawable.shape_msg_circular);
            } else if (Integer.parseInt(count) >= 10) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(count);
                tv.setBackgroundResource(R.drawable.shape_msg_square);
            } else if (Integer.parseInt(count) >= 100) {
                tv.setVisibility(View.VISIBLE);
                tv.setText("99+");
                tv.setBackgroundResource(R.drawable.shape_msg_square);
            }
        } else {
            tv.setVisibility(View.INVISIBLE);
        }
    }
}