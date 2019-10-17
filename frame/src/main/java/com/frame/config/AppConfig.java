package com.frame.config;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 */
public class AppConfig {

    //  debug为true打开调试信息，false则关闭调试信息
    public static boolean DEBUG = true;
    //通用请求地址
    private static String Url = "https://www.apiopen.top/";

    public static String getUrl() {
        return DEBUG ? "https://www.apiopen.top/" : Url;
    }

    public static void setUrl(String url) {
        if (!DEBUG)
            Url = url;
    }

    //分页参数
    public static final class ViewPage {
        //起始页下标
        public static final int START_INDEX = 1;
        //每页的数据量
        public static final int PAGE_COUNT = 15;
    }
}
