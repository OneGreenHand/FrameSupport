package com.frame.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * @date 2016年07月28日09:45:00
 * @description: Activity管理类
 */
public class AppManager {

    private Stack<WeakReference<Activity>> mActivityStack = new Stack<>();//弱引用防止内存泄漏
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
        if (mActivityStack == null)
            mActivityStack = new Stack<>();
        if (activity != null)
            mActivityStack.add(new WeakReference<>(activity));
    }

    /**
     * 检查弱引用是否释放，若释放，则从栈中清理掉该元素
     */
    public void checkWeakReference() {
        if (mActivityStack != null) {
            Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> activityReference = iterator.next();
                Activity temp = activityReference.get();
                if (temp == null)
                    iterator.remove();
            }
        }
    }

    /**
     * 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        checkWeakReference();
        if (mActivityStack != null && !mActivityStack.isEmpty())
            return mActivityStack.lastElement().get();
        return null;
    }

    /**
     * 关闭当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = currentActivity();
        if (activity != null)
            finishActivity(activity);
    }

    /**
     * 关闭指定的Activity(单个)
     */
    public void finishActivity(Activity activity) {
        if (activity != null && mActivityStack != null) {
            Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> next = iterator.next();
                Activity temp = next.get();
                if (temp == null) {
                    iterator.remove();
                    continue;
                }
                if (temp == activity)
                    iterator.remove();
            }
            activity.finish();
        }
    }

    /**
     * 关闭指定类名的所有Activity
     */
    public void finishActivity(Class<?> cls) {
        if (mActivityStack != null) {
            Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> activityReference = iterator.next();
                Activity activity = activityReference.get();
                if (activity == null) {
                    iterator.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    iterator.remove();
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActivityStack != null) {
            for (WeakReference<Activity> activityReference : mActivityStack) {
                Activity activity = activityReference.get();
                if (activity != null)
                    activity.finish();
            }
            mActivityStack.clear();
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            finishAllActivity();  // 退出JVM,释放所占内存资源,0表示正常退出
            System.exit(0);  // 从系统中kill掉应用程序
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索栈中是否存在当前类名Activity
     */
    public boolean contains(Class<?> cls) {
        boolean isExistence = false;//是否存在
        Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> activityReference = iterator.next();
            Activity activity = activityReference.get();
            if (activity == null)
                continue;
            if (activity.getClass().equals(cls)) {
                isExistence = true;
                break;
            }
        }
        return isExistence;
    }

    /**
     * 清空除某个类外的所有类(一般用于保留MainActivity)
     */
    public void clearAllActivityExceptMain(Class<?> cls) {
        Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> activityReference = iterator.next();
            Activity activity = activityReference.get();
            if (activity == null) {
                iterator.remove();
                continue;
            }
            if (!activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }
}