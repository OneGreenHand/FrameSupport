package com.frame.support.service;


import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.frame.config.BaseConfig;
import com.frame.support.util.NotificationHelper;
import com.frame.util.ToastUtil;

/**
 * @data on 2019/5/27 14:01
 * @describe 下载服务
 */
public class DownloadService extends Service {
    private String fileUrl;//下载的文件地址
    private String fileName;//文件名
    private NotificationHelper notificationHelper;
    private boolean isDownloading = false;//是否在下载中

    public NotificationHelper getNotification() {
        if (notificationHelper == null)
            notificationHelper = new NotificationHelper(getApplicationContext());
        return notificationHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground(1, getNotification().getNotification());//服务前台化只能用startForeground()方法,不可用notificationManager.notify(1,notification);,不然报错
        else
            getNotification().showNotification();//创建通知栏
        Aria.download(this).register();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initServer(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化下载相关东西
     */
    private void initServer(Intent intent) {
        fileUrl = intent.getStringExtra("fileUrl");
        fileName = intent.getStringExtra("fileName");
        if (TextUtils.isEmpty(fileUrl) || TextUtils.isEmpty(fileName) || !FileUtils.createOrExistsDir(BaseConfig.FILE_FOLDER)) {
            error();
            return;
        }
        if (!isDownloading) {//没有在下载中才可以下载
            if (FileUtils.isFileExists(BaseConfig.FILE_FOLDER + fileName)) {  //如果更新之前存在apk就直接安装
                stopSelf();//手动停止服务(8.0同时通知栏会消失)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)//未没有绑定service,手动取消通知栏
                    getNotification().cancel();
                AppUtils.installApp(BaseConfig.FILE_FOLDER + fileName);
            } else {
                try {
                    Aria.download(this)
                            .load(fileUrl)
                            .setFilePath(BaseConfig.FILE_FOLDER + fileName, true)
                            .create();
                } catch (Exception e) {//有可能下载地址错误
                    error();
                    ToastUtil.showShortToast("下载失败");
                }
            }
        } else {
            ToastUtil.showShortToast("已在下载中");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Aria.download(this).unRegister();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Download.onPre
    protected void onTaskPre(DownloadTask task) {
        isDownloading = true;
    }

    @Download.onTaskCancel
    public void onTaskCancel(DownloadTask task) {
        error();
    }

    @Download.onTaskFail
    public void onTaskFail(DownloadTask task) {
        error();
    }

    @Download.onTaskComplete
    public void onTaskComplete(DownloadTask task) {
        getNotification().downloadComplete(fileName);//手动设置下载完成,并且设置通知栏点击事件(不然进度显示有问题)
        isDownloading = false;
        if (FileUtils.isFileExists(BaseConfig.FILE_FOLDER + fileName)) //如果本地存在文件，直接调用安装操作
            AppUtils.installApp(BaseConfig.FILE_FOLDER + fileName);
        stopSelf();
    }

    @Download.onTaskRunning
    public void onTaskRunning(DownloadTask task) {
        long len = task.getFileSize();
        if (len == 0) {
            getNotification().updateProgress(-1);
        } else {
            getNotification().updateProgress((int) (task.getCurrentProgress() * 100 / len));
        }
    }

    private void error() {
        getNotification().updateProgress(-1);
        isDownloading = false;
        // stopSelf();
    }
}
