package com.ogh.support.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.frame.FrameApplication;
import com.meituan.android.walle.WalleChannelReader;

/**
 * 获取渠道号(根据AndroidManifest文件中channel字段)
 */
public class ChannelUtils {

    public static String getChannel() {
        String channerl = WalleChannelReader.get(FrameApplication.getContext(), "CHANNEL");//通过walle获取到的渠道号，需要手动注入渠道信息
        return TextUtils.isEmpty(channerl) ? "AA000" : channerl;
        //  return getMate("channel");
    }

    /**
     * 动态获取
     */
    private static String getMate(String key) {
        String result = "AA000";
        try {
            ApplicationInfo appInfo = Utils.getApp().getPackageManager().getApplicationInfo(AppUtils.getAppPackageName(), PackageManager.GET_META_DATA);
            result = appInfo.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
