package com.frame.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.frame.FrameApplication;
import com.frame.config.AppConfig;
import com.frame.config.BaseConfig;
import com.frame.support.util.ChannelUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class AppContext extends FrameApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);//初始化工具类
        //initBugly();
        initFolder();
    }

    /**
     * 不改变Oncreate()方法中的业务逻辑
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        // 安装tinker插件(热更新相关)
        // Beta.installTinker();
    }


    /**
     * 初始化崩溃捕获
     */
    @SuppressLint("MissingPermission")
    private void initFolder() {
        // 创建app目录
        File dir = new File(BaseConfig.APP_FOLDER);
        if (!dir.exists()) {
            dir.mkdir();
        }
        //创建file目录
        File fileDir = new File(BaseConfig.FILE_FOLDER);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        strategy.setAppChannel(ChannelUtils.getChannel());  //设置渠道
        // 初始化Bugly
        CrashReport.initCrashReport(context, "f2b3e1f187", AppConfig.DEBUG, strategy);
    }

    /**
     * 增加上报进程控制（防止多进程情况下进行多次上报，造成不必要的资源浪费）
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
