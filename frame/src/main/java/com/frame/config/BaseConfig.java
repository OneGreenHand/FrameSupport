package com.frame.config;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;

/**
 * 应用程序配置类: 用于保存用户相关信息及设置
 */
public class BaseConfig {

    //true打开调试，false关闭调试
    public static boolean DEBUG = false;

    //通用请求地址
    public static String getUrl() {
        if (DEBUG) {
            return "https://api.apiopen.top/";
        } else {
            String urls = SPStaticUtils.getString("baseUrl");
            return TextUtils.isEmpty(urls) ? "http://api.jiefuk.com:8019/" : urls;
        }
    }

    public static void setUrl(String url) {
        if (!DEBUG)
            SPStaticUtils.put("baseUrl", url);
    }

    public static final class ViewPage {
        public static final int START_INDEX = 1; //起始页下标
        public static final int PAGE_COUNT = 10;  //每页的数据量
    }
}
