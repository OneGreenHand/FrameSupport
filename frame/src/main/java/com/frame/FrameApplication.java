package com.frame;

import android.app.Application;
import android.content.Context;

/**
 * description: 基础框架Application
 */
public class FrameApplication extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}