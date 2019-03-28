package com.frame.util;


/**
 * @author OneGreenHand
 * @package com.ogh.module.common.util
 * @fileName UserUtils
 * @data on 2019/2/28 15:35
 * @describe 用户操作工具类
 */
public class UserUtils {
    private static String TOKEN;
    // private static UserInfoEntity userInfo;
    /*************************登录和退出相关操作开始*******************************/
    /**
     * 获取token
     */
    public static String getTOKEN() {
        if (TOKEN == null || TOKEN.trim().equals(""))
            TOKEN = SharedPreferencesUtil.getString(CommonUtil.getContext(), "TOKEN", null);
        return TOKEN;
    }

    /**
     * 设置token
     */
    public static void setTOKEN(String TOKEN) {
        SharedPreferencesUtil.putString(CommonUtil.getContext(), "TOKEN", TOKEN);
        UserUtils.TOKEN = TOKEN;
    }

    /**
     * 判断用户是否登录
     * 目前是根据token来判断
     */
    public static boolean isLogin() {
        if (getTOKEN() != null && !getTOKEN().trim().equals(""))
            return true;
         else
            return false;
    }

    /**
     * 退出登录
     */
    public static void logout() {
        SharedPreferencesUtil.putString(CommonUtil.getContext(), "TOKEN", null);
        TOKEN = null;
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
