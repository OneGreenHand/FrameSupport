package com.frame.util;

import com.frame.FrameApplication;

/**
 * date：2018/7/27 11:36
 * author: wenxy
 * description: 此工具类用来保存一些APP需要保存的信息
 */
public class InfoUtil {
    private static String TOKEN;
    // private static UserInfoEntity userInfo;
    /*************************登录和退出相关操作开始*******************************/
    /**
     * 获取token
     */
    public static String getTOKEN() {
        if (TOKEN == null || TOKEN.trim().equals("")) {
            TOKEN = SharedPreferencesUtil.getString(FrameApplication.mContext, "TOKEN", null);
        }
        return TOKEN;
    }

    /**
     * 设置token
     */
    public static void setTOKEN(String TOKEN) {
        SharedPreferencesUtil.putString(FrameApplication.mContext, "TOKEN", TOKEN);
        InfoUtil.TOKEN = TOKEN;
    }

    /**
     * 判断用户是否登录
     * 目前是根据token来判断
     */
    public static boolean isLogin() {
        if (getTOKEN() != null && !getTOKEN().trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 退出登录
     */
    public static void logout() {
        SharedPreferencesUtil.putString(FrameApplication.mContext, "TOKEN", null);
        TOKEN = null;
    }
//    /**
//     * 得到用户信息
//     */
//    public static UserInfoEntity getUserInfo() {
//        if (userInfo != null) {
//            return userInfo;
//        } else {
//            String infoJson = SharedPreferencesUtil.getString(AppContext.mContext, "userInfo", null);
//            if (infoJson == null) {
//                return null;
//            } else {
//                UserInfoEntity bean = GsonUtil.getBean(infoJson, UserInfoEntity.class);
//                return bean;
//            }
//        }
//    }
//
//    /**
//     * 储存用户信息
//     */
//    public static void setUserInfo(UserInfoEntity userInfo) {
//        Util.userInfo = userInfo;
//        Gson gson = new Gson();
//        String infoJson = gson.toJson(userInfo);
//        SharedPreferencesUtil.putString(AppContext.mContext, "userInfo", infoJson);
//    }
    /*************************登录和退出相关操作结束*******************************/

}
