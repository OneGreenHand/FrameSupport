package com.frame;

import android.app.Application;
import android.content.Context;

/**
 * date：2018/7/27 11:38
 * description: 基础框架Application
 */
public class FrameApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
