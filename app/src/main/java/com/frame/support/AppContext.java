package com.frame.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.frame.FrameApplication;
import com.frame.config.BaseConfig;
import com.tencent.bugly.beta.Beta;

import java.io.File;

public class AppContext extends FrameApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);//初始化工具类
   //     Bugly.init(getApplicationContext(), "f2b3e1f187", false);//初始化bugly
        initFolder();
    }

    /**
     * 不改变Oncreate()方法中的业务逻辑
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        // 安装tinker插件
        Beta.installTinker();
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
//        //创建photo目录
//        File photosDir = new File(BaseConfig.PHOTO_FOLDER);
//        if (!photosDir.exists()) {
//            photosDir.mkdir();
//        }
        //创建file目录
        File fileDir = new File(BaseConfig.FILE_FOLDER);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }
}
