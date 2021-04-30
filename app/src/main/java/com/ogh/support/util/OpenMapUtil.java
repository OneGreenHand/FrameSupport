package com.ogh.support.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * description: 跳转三方地图工具类
 */
public class OpenMapUtil {

    /***
     * 是否安装百度地图
     */
    public static boolean isInstallBaiduMap() {
        try {
            if (!new File("/data/data/" + "com.baidu.BaiduMap").exists())
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 是否安装高德地图
     */
    public static boolean isInstallGaodeMap() {
        try {
            if (!new File("/data/data/" + "com.autonavi.minimap").exists())
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /***
     * 是否安装腾讯地图
     */
    public static boolean isInstallTencentMap() {
        try {
            if (!new File("/data/data/" + "com.tencent.map").exists())
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 打开腾讯地图
     * 参考http://lbs.qq.com/uri_v1/guide-mobile-navAndRoute.html
     *
     * @param context
     * @param type      路线规划方式参数: bus 或 drive 或 walk 或 bike
     * @param from      起点名称: 鼓楼
     * @param fromcoord 起点坐标: 39.907380,116.388501
     * @param to        终点名称: 奥林匹克森林公园
     * @param tocoord   终点坐标: 40.010024,116.392239
     * @param referer   请填写开发者key
     */
    public static void openTencentMap(Context context, String type, String from, String fromcoord, String to, String tocoord, String referer) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("qqmap://map/routeplan?")
                    .append("type=" + type + "&")
                    .append("from=")
                    .append(from + "&")
                    .append("fromcoord=")
                    .append(fromcoord + "&")
                    .append("to=")
                    .append(to + "&")
                    .append("tocoord=")
                    .append(tocoord + "&")
                    .append("referer=myapp");
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开高德地图
     * 参考https://lbs.amap.com/api/amap-mobile/guide/android/route
     *
     * @param context
     * @param slat    起点纬度。如果不填写此参数则自动将用户当前位置设为起点纬度。
     * @param slon    起点经度。如果不填写此参数则自动将用户当前位置设为起点经度。
     * @param sname   起点名称
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称
     * @param dev     起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param t       t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）（骑行仅在V788以上版本支持）
     */
    public static void openGaoDeMap(Context context, String slat, String slon, String sname, String dlat, String dlon, String dname, String dev, String t) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("androidamap://route/plan/?")
                    .append("slat=")
                    .append(slat + "&")
                    .append("slon=")
                    .append(slon + "&")
                    .append("sname=")
                    .append(sname + "&")
                    .append("dlat=")
                    .append(dlat + "&")
                    .append("dlon=")
                    .append(dlon + "&")
                    .append("dname=")
                    .append(dname + "&")
                    .append("dev=" + dev + "&")
                    .append("t=" + t);
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(sb.toString()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开百度地图
     * http://lbsyun.baidu.com/index.php?title=uri/api/android
     *
     * @param context
     * @param sName   起点名称: 天安门
     * @param sLatlng 起点经纬度: 39.98871,116.43234
     * @param eName   终点名称: 对外经贸大学
     * @param eLatlng 终点经纬度: 39.98871,116.43234
     * @param mode    导航模式: transit 或 driving 或 walking 或 riding
     */
    public static void openBaiduMap(Context context, String sName, String sLatlng, String eName, String eLatlng, String mode) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("baidumap://map/direction?")
                    .append("origin=")
                    .append("name:" + sName + "|")
                    .append("latlng:" + sLatlng + "&")
                    .append("destination=")
                    .append("name:" + eName + "|")
                    .append("latlng:" + eLatlng + "&")
                    .append("mode=" + mode + "&")
                    .append("src=andr.baidu.openAPIdemo");
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(sb.toString()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
