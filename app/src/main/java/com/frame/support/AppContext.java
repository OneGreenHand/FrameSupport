package com.frame.support;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.frame.FrameApplication;
import com.frame.config.AppConfig;
import com.frame.support.util.ChannelUtils;
import com.tencent.bugly.crashreport.CrashReport;


public class AppContext extends FrameApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);//初始化工具类
        LogUtils.getConfig().setLogSwitch(AppConfig.DEBUG);//设置log开关
        //initBugly();
    }

    /**
     * 不改变Oncreate()方法中的业务逻辑
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        Context context = getApplicationContext();
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);// 设置是否为上报进程
        strategy.setAppChannel(ChannelUtils.getChannel());  //设置渠道
        // CrashReport.setUserId("");//设置用户ID
        CrashReport.initCrashReport(context, "8706956f68", AppConfig.DEBUG, strategy); // 仅使用异常捕获功能时使用这个
    }

}