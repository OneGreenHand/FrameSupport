package com.frame.support.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.frame.config.BaseConfig;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信分享回调
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    IWXAPI iwxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(this, BaseConfig.WEIXIN_APP_ID);
        boolean handleIntent = iwxapi.handleIntent(getIntent(), this);
        //下面代码是判断微信分享后返回WXEnteryActivity的，如果handleIntent==false,说明没有调用IWXAPIEventHandler，则需要在这里销毁这个透明的Activity;
        if (handleIntent == false)
            finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
//         if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {//分享
//            switch (baseResp.errCode) {//微信调整后现在没办法知道是否分享成功了
//                case BaseResp.ErrCode.ERR_OK:
//                    result = "分享成功";
//                    break;
//                case BaseResp.ErrCode.ERR_USER_CANCEL:
//                    result = "分享取消";
//                    break;
//                case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                    result = "分享被拒绝";
//                    break;
//                default:
//                    result = "未知原因";
//                    break;
//            }
//        }
//        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登录
//            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
//            resp.code
//            resp.errCode
//        }
        finish();
    }
}
