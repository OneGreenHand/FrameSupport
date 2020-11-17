package com.ogh.support.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.ogh.support.config.AppConfig;

public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String fileName = intent.getStringExtra("FILE_NAME");
        if (TextUtils.isEmpty(fileName))
            return;
        if (FileUtils.isFileExists(AppConfig.FilePath.FILE_FOLDER + fileName)) //如果本地存在文件，直接调用安装操作
            AppUtils.installApp(AppConfig.FilePath.FILE_FOLDER + fileName);
    }
}
