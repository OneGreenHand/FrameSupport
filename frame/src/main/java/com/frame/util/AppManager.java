package com.frame.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * @date 2016年07月28日09:45:00
 * @description: Activity管理类
 */
public class AppManager {

    private static Stack<Activity> activityStack = new Stack<>();
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null)
                    instance = new AppManager();
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null)
            activityStack = new Stack<Activity>();
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(activityStack.lastElement());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        ArrayList<Activity> delList = new ArrayList<Activity>();
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls))
                delList.add(activity);
        }
        for (Activity activity : delList)
            finishActivity(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i))
                activityStack.get(i).finish();
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 将指定Activity移除堆栈
     */
    public void removeActivityFromStack(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 搜索栈中是否存在当前类名Activity
     */
    public boolean contains(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls))
                return true;
        }
        return false;
    }

    /**
     * 清空除某个类外的所有类(一般用于保留mainactivity)
     */
    public void clearAllActivityExceptMain(Class<?> cls) {
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**
     * 搜索栈中类名Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls))
                return activity;
        }
        return null;
    }

}
