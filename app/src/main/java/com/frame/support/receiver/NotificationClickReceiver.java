package com.frame.support.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.frame.config.BaseConfig;

/**
 * @author luo
 * @package com.frame.support.receiver
 * @fileName NotificationClickReceiver
 * @data on 2019/5/27 16:39
 */
public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String fileName = intent.getStringExtra("FILE_NAME");
        if (intent.getAction().equals("NOTIFICATION_CLICKED")) {
            if (TextUtils.isEmpty(fileName))
                return;
            if (FileUtils.isFileExists(BaseConfig.FILE_FOLDER + fileName)) //如果本地存在文件，直接调用安装操作
                AppUtils.installApp(BaseConfig.FILE_FOLDER + fileName);
        }

    }
}
