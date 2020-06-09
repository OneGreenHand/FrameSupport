package com.frame.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.frame.FrameApplication;
import com.frame.bean.PayResult;
import com.frame.bean.WXResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * @describe 支付工具类
 */
public class PayHelper {
    private static PayHelper mPayHelper = null;

    public static PayHelper getInstance() {
        if (mPayHelper == null) {
            synchronized (PayHelper.class) {
                if (mPayHelper == null)
                    mPayHelper = new PayHelper();
            }
        }
        return mPayHelper;
    }

    private PayHelper() {
    }

    /********************************微信支付**************************************/
    private IWXAPI wxapi = null;

    public void WexPay(WXResult result) {
        if (wxapi == null) {
            wxapi = WXAPIFactory.createWXAPI(FrameApplication.getContext(),result.appId);
            if (!wxapi.isWXAppInstalled()) {
                ToastUtil.showShortToast("未安装微信客户端");
                return;
            }
            wxapi.registerApp(result.appId);// 将该app注册到微信
        }
        PayReq req = new PayReq();
        if (result != null) {//TODO  这里要根据后台返回的字段名和返回体，进行修改
            req.appId = result.appId;
            req.partnerId = result.partnerId;
            req.prepayId = result.prepayId;
            req.nonceStr = result.nonceStr;
            req.timeStamp = result.timeStamp;
            req.packageValue = "Sign=WXPay";
            req.sign = result.sign;
            wxapi.sendReq(req);
        }
    }

    /********************************支付宝支付**************************************/
    private final int SDK_PAY_FLAG = 1;

    public void AliPay(Activity activity, final String orderInfo, IPayListener mIPayListener) {
        this.mIPayListener = mIPayListener;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (mIPayListener != null)
                            mIPayListener.onSuccess(resultInfo);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (mIPayListener != null)
                            mIPayListener.onFail(resultInfo);
                    }
                    break;
            }
        }
    };
    //支付宝支付结果回调
    private IPayListener mIPayListener;

    public void setIPayListener(IPayListener mIPayListener) {
        this.mIPayListener = mIPayListener;
    }

    public interface IPayListener {
        void onSuccess(String resultInfo);

        void onFail(String resultInfo);
    }
}
