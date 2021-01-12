package com.ogh.support.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;

public class ChannelUtils {

    public static String getChannel() {
//        String channel = WalleChannelReader.get(FrameApplication.getContext(), "CHANNEL");//通过Walle获取到的渠道号,需要手动注入渠道信息,不然为空
//        return TextUtils.isEmpty(channel) ? "MeiZu" : channel;
        return getMate("channel");
    }

    /**
     * 动态获取(AndroidManifest文件中channel字段)
     */
    private static String getMate(String key) {
        String result = "MeiZu";
        try {
            ApplicationInfo appInfo = Utils.getApp().getPackageManager().getApplicationInfo(AppUtils.getAppPackageName(), PackageManager.GET_META_DATA);
            result = appInfo.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
