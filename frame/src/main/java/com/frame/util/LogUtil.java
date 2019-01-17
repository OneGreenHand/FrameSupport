package com.frame.util;

import android.util.Log;

import com.frame.config.AppConfig;


/**
 * @description: Logcat调试工具类
 */
public class LogUtil {

    public static void i(String tag, String message) {
        if (AppConfig.DEBUG) {
            Log.i(tag, message+"");
        }
    }

    public static void w(String tag, String message) {
        if (AppConfig.DEBUG) {
            Log.w(tag, message+"");
        }

    }

    public static void d(String tag, String message) {
        if (AppConfig.DEBUG) {
            Log.d(tag, message+"");
        }

    }

    public static void e(String tag, String message) {
        if (AppConfig.DEBUG) {
            Log.e(tag, message+"");
        }

    }

    public static void v(String tag, String message) {
        int maxLogSize = 3000;

        if (AppConfig.DEBUG) {
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.i(tag, message.substring(start, end));
            }
        }

    }

    /**
     * @description: 日志输出为txt文件
     */
//    public static void log2txt(String message) {
//        try {
//            FileWriter fileWriter = new FileWriter(BaseConfig.LOG_PATH, true);
//            PrintWriter printWriter = new PrintWriter(fileWriter);
//            printWriter.println(message);
//            printWriter.close();
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}