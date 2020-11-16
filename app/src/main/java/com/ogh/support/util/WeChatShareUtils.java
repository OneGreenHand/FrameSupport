package com.ogh.support.util;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.ImageUtils;
import com.frame.FrameApplication;
import com.ogh.support.AppContext;
import com.frame.util.ToastUtil;
import com.ogh.support.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * 微信分享工具类
 */
public class WeChatShareUtils {

    /**
     * 分享微信文本
     *
     * @param type 0：分享到好友  1：分享到朋友圈
     */
    public static void shareWeChatTxt(String content, int type) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = content;
        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = textObject;
        mediaMessage.description = content;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "";
        req.message = mediaMessage;
        req.scene = type;
        String appId = AppContext.getContext().getResources().getString(R.string.wx_appid);
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(FrameApplication.getContext(), appId);
        if (!iwxapi.isWXAppInstalled()) {
            ToastUtil.showShortToast("未安装微信,请安装后再尝试分享");
            return;
        }
        iwxapi.registerApp(appId);
        iwxapi.sendReq(req);
    }

    /**
     * 发起app网页分享
     *
     * @param type 0：分享到好友  1：分享到朋友圈
     */
    public static void shareWeChatUrl(String title, String desc, String url, int type) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject object = new WXWebpageObject();
        object.webpageUrl = url;
        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = object;
        mediaMessage.title = title;//网页标题
        mediaMessage.description = desc;//网页描述
        Bitmap bitmap = ImageUtils.getBitmap(R.mipmap.ic_launcher);//图片
        mediaMessage.thumbData = ImageUtils.bitmap2Bytes(bitmap);
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "";
        req.message = mediaMessage;
        req.scene = type;
        String appId = AppContext.getContext().getResources().getString(R.string.wx_appid);
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(FrameApplication.getContext(), appId);
        if (!iwxapi.isWXAppInstalled()) {
            ToastUtil.showShortToast("未安装微信,请安装后再尝试分享");
            return;
        }
        iwxapi.registerApp(appId);
        iwxapi.sendReq(req);
    }

    /**
     * 发起图片分享
     *
     * @param type 0：分享到好友  1：分享到朋友圈
     */
    public static void shareWeChatImg(Bitmap bitmap, int type) {
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        bitmap.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp);//设置缩略图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = type;
        String appId = AppContext.getContext().getResources().getString(R.string.wx_appid);
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(FrameApplication.getContext(), appId);
        if (!iwxapi.isWXAppInstalled()) {
            ToastUtil.showShortToast("未安装微信,请安装后再尝试分享");
            return;
        }
        iwxapi.registerApp(appId);
        iwxapi.sendReq(req);
    }

    private static byte[] bmpToByteArray(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}