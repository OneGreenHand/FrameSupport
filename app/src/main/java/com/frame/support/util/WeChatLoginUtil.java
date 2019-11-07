package com.frame.support.util;

import android.content.Context;

import com.frame.config.BaseConfig;
import com.frame.util.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信登录工具类
 */
public class WeChatLoginUtil {
    public static void WeChatLogin(Context context) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, BaseConfig.WEIXIN_APP_ID);
        if (!iwxapi.isWXAppInstalled()) {
            ToastUtil.showShortToast("未安装微信,请安装后再尝试登录");
            return;
        }
        iwxapi.registerApp(BaseConfig.WEIXIN_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        iwxapi.sendReq(req);
    }
}