package com.frame.config;

import android.text.TextUtils;

import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.SPStaticUtils;

import java.io.File;

/**
 * 应用程序配置类: 用于保存用户相关信息及设置
 */
public class AppConfig {

    //true打开调试，false关闭调试
    public static boolean DEBUG = true;

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

    public static final class FilePath {
        public static final String PHOTO_FOLDER = PathUtils.getExternalAppPicturesPath(); //  /storage/emulated/0/Android/data/package/files/Pictures.
        public static final String FILE_FOLDER = PathUtils.getExternalAppDownloadPath(); //   /storage/emulated/0/Android/data/package/files/Download.
    }

    public static final class ViewPage {
        public static final int START_INDEX = 1; //起始页下标
        public static final int PAGE_COUNT = 10;  //每页的数据量
    }
}
