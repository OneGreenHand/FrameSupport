package com.frame.config;

import com.blankj.utilcode.util.SDCardUtils;

import java.io.File;

/**
 * description: 基础配置类
 */
public class BaseConfig {
    //文件操作路径
    public static final String SDCARD_PATH = SDCardUtils.getSDCardPathByEnvironment();    //sd卡路径
    public static final String APP_FOLDER = SDCARD_PATH + File.separator + "FrameSupport" + File.separator;    //客户端文件夹路径
    public static final String PHOTO_FOLDER = APP_FOLDER + "photos" + File.separator;    //客户端照片文件路径
    public static final String FILE_FOLDER = APP_FOLDER + "files" + File.separator;    //客户端文件路径
    //微信支付AppId
    public static final String WEIXIN_APP_ID = "wx87c75a5bc154737c";
}
