package com.frame.support.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.frame.support.R;
import com.frame.support.service.DownloadService;
import com.frame.util.CommonUtil;
import com.frame.util.ToastUtil;

public class InstructionsUtils {

    /**
     * @param type 操作类别： 0、跳转本地 1、打开外部浏览器 2、通知栏下载APK或打开app 3、打开指定QQ
     * @param url  跳转URL：根据type变动，可能是本地activity或者浏览器之类的
     */
    public static void JumpIntention(Context context, int type, String url) {
        switch (type) {
            case 0:
                CommonUtil.goLocationActivity(context, url);
                break;
            case 1:
                CommonUtil.intentToBrowsable(context, url);
                break;
            case 2:
                checkInstallOrDown(context, url);
                break;
            case 3:
                CommonUtil.ContactQQ(context, url);
                break;
        }
    }

    /**
     * 通过service下载apk
     *
     * @param url 下载地址
     */
    private static void download(Context context, String url) {
        if (TextUtils.isEmpty(url))
            return;
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        String fileName;
                        if (url.contains("/"))
                            fileName = url.substring(url.lastIndexOf("/")).replace("/", "");
                        else
                            fileName = context.getResources().getString(R.string.app_name) + System.currentTimeMillis() + ".apk";
                        Intent intent = new Intent(context, DownloadService.class);
                        intent.putExtra("fileUrl", url);
                        intent.putExtra("fileName", fileName);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            context.startForegroundService(intent);
                        } else {
                            context.startService(intent);
                        }
                    }

                    @Override
                    public void onDenied() {
                        ToastUtil.showShortToast("权限被拒绝,下载失败");
                    }
                })
                .request();
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
        } else {
            download(context, url);
        }
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
                download(context, downUrl);
            else//包名不为空,并且已经安装就打开应用
                AppUtils.launchApp(packageName);
        }
    }
}