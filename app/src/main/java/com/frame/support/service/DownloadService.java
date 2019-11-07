package com.frame.support.service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.frame.config.BaseConfig;
import com.frame.support.util.NotificationHelper;
import com.frame.util.ToastUtil;

/**
 * @author luo
 * @package com.frame.support.service
 * @fileName DownloadService
 * @data on 2019/5/27 14:01
 * @describe 下载服务
 */
public class DownloadService extends Service {
    private String fileUrl;//下载的文件地址
    private String fileName;//文件名
    private String title;
    private NotificationHelper notificationHelper;
    private boolean isDownloading = false;//是否在下载中
    private boolean isShowProgress = true;//是否显示下载进度

    public NotificationHelper getNotification() {
        if (notificationHelper == null)
            notificationHelper = new NotificationHelper(getApplicationContext());
        return notificationHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
        if (intent == null)
            return;
        fileUrl = intent.getStringExtra("fileUrl");
        fileName = intent.getStringExtra("fileName");
        title = intent.getStringExtra("title");
        isShowProgress = intent.getBooleanExtra("isShowProgress", true);
        if (TextUtils.isEmpty(fileUrl) || TextUtils.isEmpty(fileName))
            return;
        if (!FileUtils.createOrExistsDir(BaseConfig.FILE_FOLDER)) //判断目录是否存在，不存在则判断是否创建成功(这里不检查权限了，在外面检查)
            return;
        if (!isDownloading) {//没有在下载中才可以下载
            if (FileUtils.isFileExists(BaseConfig.FILE_FOLDER + fileName)) {  //如果更新之前存在apk就直接安装
                if (isShowProgress) {
                    AppUtils.installApp(BaseConfig.FILE_FOLDER + fileName);
                } else {//如果是静默下载，并且本地有安装包那就延时三秒安装
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AppUtils.installApp(BaseConfig.FILE_FOLDER + fileName);
                        }
                    }, 3000);
                }
            } else {
                if (isShowProgress)
                    ToastUtil.showShortToast("后台下载中");
                Aria.download(this)
                        .load(fileUrl)
                        .setFilePath(BaseConfig.FILE_FOLDER + fileName, true)
                        .start();
            }
        } else {
            if (isShowProgress)
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

    @Download.onTaskStart
    public void onTaskStart(DownloadTask task) {
        if (isShowProgress) {
            getNotification().showNotification(title);
        }
        isDownloading = true;
    }

    @Download.onTaskCancel
    public void onTaskCancel(DownloadTask task) {
        if (isShowProgress) {
            getNotification().updateProgress(-1);
        }
        isDownloading = false;
        stopSelf();
    }

    @Download.onTaskFail
    public void onTaskFail(DownloadTask task) {
        if (isShowProgress) {
            getNotification().updateProgress(-1);
        }
        isDownloading = false;
        stopSelf();
    }

    @Download.onTaskComplete
    public void onTaskComplete(DownloadTask task) {
        if (isShowProgress) {
            getNotification().downloadComplete(fileName);//手动设置下载完成，并且设置通知栏点击事件(这里进度显示会有问题)
        }
        isDownloading = false;
        if (FileUtils.isFileExists(BaseConfig.FILE_FOLDER + fileName)) //如果本地存在文件，直接调用安装操作
            AppUtils.installApp(BaseConfig.FILE_FOLDER + fileName);
        stopSelf();//手动停止服务
    }

    @Download.onTaskRunning
    public void onTaskRunning(DownloadTask task) {
        long len = task.getFileSize();
        if (isShowProgress) {
            if (len == 0) {
                getNotification().updateProgress(-1);
            } else {
                getNotification().updateProgress((int) (task.getCurrentProgress() * 100 / len));
            }
        }
    }
}
