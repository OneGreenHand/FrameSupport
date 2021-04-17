package com.ogh.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.frame.base.BaseModel;
import com.frame.base.BaseRequestView;
import com.frame.bean.BaseBean;
import com.ogh.support.R;
import com.ogh.support.api.API;
import com.frame.util.ToastUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * 验证码倒计时工具类
 */
public class VerificationCode extends TextView implements LifecycleObserver, BaseRequestView<BaseBean> {
    private int conductColor = Color.RED;//倒计时显示的颜色
    private int endColor = Color.RED;//倒计时结束时显示的颜色
    private String endText = "重新获取";//结束时显示的文字
    private String companyText = " s";//倒计时后面显示的单位
    private int durationTime = 60000;//持续时间
    private int intervalTime = 1000;//间隔时间
    private VerificationCountDownTimer countDownTimer;

    public VerificationCode(Context context) {
        super(context);
        initView(context, null);
    }

    public VerificationCode(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public VerificationCode(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 开始倒计时
     */
    public void start(String phone, CodeType codeType) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("手机号不能为空");
        } else if (phone.startsWith("0") || phone.length() != 11) {
            ToastUtil.showShortToast("手机号格式不正确");
        } else {
            new BaseModel.Builder(this)
                    .putParam("Mobile", phone)
                    .putParam("VCType", codeType.getValue())
                    .create().post(API.GET_DUAN_ZI);//这里填写真实请求地址
        }
    }

    public enum CodeType {
        LOGIN(1),//登录
        REGISTER(2);//注册
        private int mValue;

        CodeType(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    private class VerificationCountDownTimer extends CountDownTimer {

        public VerificationCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setEnabled(false);
            setText((millisUntilFinished / 1000) + companyText);
            setTextColor(conductColor);
        }

        @Override
        public void onFinish() {
            setEnabled(true);
            setText(endText);
            setTextColor(endColor);
        }
    }

    /**
     * 初始化
     */
    private void initView(Context context, AttributeSet attrs) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            if (!activity.isFinishing() && !activity.isDestroyed())//注册绑定生命周期
                activity.getLifecycle().addObserver(this);
        }
        setMinWidth(SizeUtils.dp2px(95));
        setMinHeight(SizeUtils.dp2px(30));
        setGravity(Gravity.CENTER);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerificationCode);
            conductColor = array.getColor(R.styleable.VerificationCode_VcConductColor, Color.RED);
            endColor = array.getColor(R.styleable.VerificationCode_VcEndColor, Color.RED);
            endText = array.getString(R.styleable.VerificationCode_VcEndText);
            companyText = array.getString(R.styleable.VerificationCode_VcCompany);
            durationTime = array.getInt(R.styleable.VerificationCode_VcDuration, 60000);
            intervalTime = array.getInt(R.styleable.VerificationCode_VcIntervalTime, 1000);
            if (TextUtils.isEmpty(companyText))
                companyText = " s";
            if (TextUtils.isEmpty(endText))
                endText = "重新获取";
            array.recycle();
        }
        countDownTimer = new VerificationCountDownTimer(durationTime, intervalTime);//初始化倒计时
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void requestSuccess(BaseBean data, Object tag, int pageIndex, int pageCount) {
        if (countDownTimer == null)
            countDownTimer = new VerificationCountDownTimer(durationTime, intervalTime);
        ToastUtil.showShortToast("验证码已发送");
        countDownTimer.start();//开始倒计时
    }

    @Override
    public void requestFail(BaseBean data, Object tag) {
        ToastUtil.showShortToast(data.msg);
    }

    @Override
    public void requestError(Throwable e, Object tag) {
        ToastUtil.showShortToast("请求出错");
    }

    @Override
    public void showLoadingView() {
    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void showNetErrorView(String tips) {
    }

    @Override
    public void refreshView() {
    }

    @Override
    public void tokenOverdue() {
    }

    @Override
    public void showLoadingDialog(String msg, boolean isCancel) {
    }

    @Override
    public void dismissLoadingDialog() {
    }
}