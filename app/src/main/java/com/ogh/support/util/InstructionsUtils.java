package com.ogh.support.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.frame.util.ToastUtil;
import com.ogh.support.R;
import com.ogh.support.service.DownloadService;

public class InstructionsUtils {

    /**
     * @param type   操作类别:  0、跳转本地 1、打开外部浏览器 2、通知栏下载APK或打开app 3、打开指定QQ 4、分享文本
     * @param action 跳转意图: 根据type变动，可能是本地activity或者浏览器之类的
     */
    public static void JumpIntention(Context context, int type, String action) {
        switch (type) {
            case 0:
                goLocationActivity(context, action);
                break;
            case 1:
                CommonUtil.intentToBrowsable(context, action);
                break;
            case 2:
                checkInstallOrDown(context, action);
                break;
            case 3:
                CommonUtil.ContactQQ(context, action);
                break;
            case 4:
                if (!TextUtils.isEmpty(action)) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, action);
                    context.startActivity(Intent.createChooser(intent, "分享至"));
                }
                break;
        }
    }

    /**
     * 申请权限
     */
    private static void requestPermission(Context context, String url) {
        if (TextUtils.isEmpty(url))
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//android 10不需要权限
            download(context, url);
        } else {
            PermissionUtils.permission(PermissionConstants.STORAGE)
                    .callback(new PermissionUtils.SimpleCallback() {
                        @Override
                        public void onGranted() {
                            download(context, url);
                        }

                        @Override
                        public void onDenied() {
                            ToastUtil.showShortToast("权限被拒绝,下载失败");
                        }
                    })
                    .request();
        }
    }

    /**
     * 通过service下载apk
     *
     * @param url 下载地址
     */
    public static void download(Context context, String url) {
        String fileName;
        if (url.contains("/"))
            fileName = url.substring(url.lastIndexOf("/")).replace("/", "");
        else
            fileName = context.getResources().getString(R.string.app_name) + System.currentTimeMillis() + ".apk";
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("fileUrl", url);
        intent.putExtra("fileName", fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    /**
     * 未安装则下载,已安装则打开
     *
     * @param url 里面可能包含包名和下载地址
     */
    public static void checkInstallOrDown(Context context, String url) {
        if (TextUtils.isEmpty(url))
            return;
        if (url.contains("&")) {//说明含有包名(先检查是否安装，再决定是否下载)
            String packageName = url.split("&")[0];
            String downUrl = url.split("&")[1];
            new CheckInstallTask(context, packageName, downUrl).execute();
        } else
            requestPermission(context, url);
    }

    /**
     * 检测是否安装(异步任务)
     */
    private static class CheckInstallTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String packageName;
        private String downUrl;

        private CheckInstallTask(Context context, String packageName, String downUrl) {
            this.context = context;
            this.packageName = packageName;
            this.downUrl = downUrl;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(packageName))
                return false;
            return AppUtils.isAppInstalled(packageName);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if (!s) //包名为空或者未安装就帮他下载
                requestPermission(context, downUrl);
            else//包名不为空,并且已经安装就打开应用
                AppUtils.launchApp(packageName);
        }
    }

    /**
     * 主要用于后台控制跳转本地
     * intentUrl举例: TaskActivity?ID=1&NAME=小明  意思就是跳转到TaskDetailActivity，然后带了ID和NAME，两个参数
     */
    private static void goLocationActivity(Context context, String intentUrl) {
        if (TextUtils.isEmpty(intentUrl))
            return;
        try {
//            if (needlogin) {//如果需要登录
//             Intent intent = new Intent();
//             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//             intent.setClass(context, getActivityClassName("LoginActivity"));
//             context.startActivity(intent);
//            } else {
            Intent intent = new Intent(context, getActivityClassName(intentUrl.contains("@") ? intentUrl.split("@")[0] : intentUrl));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intentUrl.contains("@")) {//说明带参数
                for (String kv : intentUrl.split("@")[1].split("\\$")) {//拿到@后面的，然后对$分割处理
                    String k = kv.substring(0, kv.indexOf("="));//拿到参数名
                    String v = kv.substring(kv.indexOf("=")).replaceFirst("=", "");//拿到参数
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
    private static Class getActivityClassName(String className) {
        try {
            return Class.forName("com.ogh.support.view.activity." + className);//TODO 这里需要改成自己对应activity位置
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}