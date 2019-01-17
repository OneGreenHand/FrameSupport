package com.frame.config;

import android.os.Environment;

import com.blankj.utilcode.util.SDCardUtils;

import java.io.File;

/**
 * date：2018/7/27 11:41
 * author: wenxy
 * description: 基础配置类
 */
public class BaseConfig {
    //文件操作路径
    public static final String SDCARD_PATH = SDCardUtils.getSDCardPaths().get(0);    //sd卡路径
    public static final String DATA_PATH = Environment.getDataDirectory().getAbsolutePath();//内存路径
    public static final String APP_FOLDER = SDCARD_PATH + File.separator + "OneGreenHand" + File.separator;    //客户端文件夹路径
    public static final String PHOTO_FOLDER = SDCARD_PATH + File.separator + "OneGreenHand" + File.separator + "photo" + File.separator;    //客户端照片文件路径
    public static final String FILE_FOLDER = SDCARD_PATH + File.separator + "OneGreenHand" + File.separator + "file" + File.separator;    //客户端文件路径
    //SharedPreferences,统一以sp_开头命名
    public static final String SP_NAME = "SP_OneGreenHand"; //客户端sp名字
    public static final String SP_CONF_APP_UNIQUEID = "SP_OneGreenHand_Uniqueid"; //应用生成的app id标识
}
