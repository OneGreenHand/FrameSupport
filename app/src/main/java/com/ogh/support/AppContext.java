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

}