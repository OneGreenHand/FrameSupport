package com.frame.util;


import android.text.TextUtils;

import com.frame.FrameApplication;

/**
 * @author OneGreenHand
 * @package com.ogh.module.common.util
 * @fileName UserUtils
 * @data on 2019/2/28 15:35
 * @describe 用户操作工具类
 */
public class UserUtils {
    // private static UserInfoEntity userInfo;
    /*************************登录和退出相关操作开始*******************************/
    /**
     * 获取token
     */
    public static String getToken() {
        return SharedPreferencesUtil.getString(FrameApplication.mContext, "TOKEN", "");
    }

    /**
     * 设置token
     */
    public static void setToken(String token) {
        SharedPreferencesUtil.putString(FrameApplication.mContext, "TOKEN", token);
    }

    /**
     * 判断用户是否登录
     * 目前是根据token来判断
     */
    public static boolean isLogin() {
        String token = getToken();
        if (TextUtils.isEmpty(token) || token.trim().equals(""))
            return false;
        else
            return true;
    }

    /**
     * 退出登录
     */
    public static void logout() {
        SharedPreferencesUtil.putString(CommonUtil.getContext(), "TOKEN", "");
    }
//    /**
//     * 得到用户信息
//     */
//    public static UserInfoEntity getUserInfo() {
//        if (userInfo != null) {
//            return userInfo;
//        } else {
//            String infoJson = SharedPreferencesUtil.getString(Utils.getContext(), "userInfo", null);
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
//        SharedPreferencesUtil.putString(Utils.getContext(), "userInfo", infoJson);
//    }
    /*************************登录和退出相关操作结束*******************************/
}
