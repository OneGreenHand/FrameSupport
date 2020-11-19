package com.ogh.support;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.frame.FrameApplication;
import com.frame.config.BaseConfig;


public class AppContext extends FrameApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);//初始化工具类
        LogUtils.getConfig().setLogSwitch(BaseConfig.DEBUG);//设置log开关
    }

    /**
     * 不改变Oncreate()方法中的业务逻辑
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//   private void initBugly() {//初始化bugly
//        Context context = getApplicationContext();
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);// 设置是否为上报进程
//        strategy.setAppChannel(ChannelUtils.getChannel());  //设置渠道
//        // CrashReport.setUserId("");//设置用户ID
//        CrashReport.initCrashReport(context, "8706956f68", BaseConfig.DEBUG, strategy);
//    }

}