package com.frame.config;

import com.blankj.utilcode.util.AppUtils;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 */
public class AppConfig {

    public static Config config = Config.product;
    /**
     * debug为true打开调试信息，false则关闭调试信息
     */
    public static boolean DEBUG = config.isDebug();
    /**
     * App全局用到的包名
     */
    public static final String PACKAGE_NAME = AppUtils.getAppPackageName();
    //分页参数
    public static final class ViewPage{
        //起始页下标
        public static final int START_INDEX = 1;
        //每页的数据量
        public static final int PAGE_COUNT = 20;
    }
}
