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
import com.tencent.bugly.Bugly;
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
     * 创建APP文件
     */
    @SuppressLint("MissingPermission")
    private void initFolder() {
        File dir = new File(BaseConfig.APP_FOLDER);  // 创建app目录
        if (!dir.exists()) {
            dir.mkdir();
        }
        File fileDir = new File(BaseConfig.FILE_FOLDER);  //创建file目录
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        Context context = getApplicationContext();
        String packageName = context.getPackageName();  // 获取当前包名
        String processName = getProcessName(android.os.Process.myPid());   // 获取当前进程名
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);// 设置是否为上报进程
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        strategy.setAppChannel(ChannelUtils.getChannel());  //设置渠道
        // CrashReport.setUserId("");//设置用户ID(用于定位具体用户)
        //  Bugly.init(context, "8706956f68", AppConfig.DEBUG, strategy);//使用热更新或者升级功能时使用这个
        CrashReport.initCrashReport(context, "8706956f68", AppConfig.DEBUG, strategy); // 仅使用异常捕获功能时使用这个
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
            if (!TextUtils.isEmpty(processName))
                processName = processName.trim();
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
