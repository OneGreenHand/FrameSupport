package com.frame.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.blankj.utilcode.util.SizeUtils;
import com.frame.base.BaseModel;
import com.frame.base.BaseRequestView;
import com.frame.bean.BaseBean;
import com.frame.support.R;
import com.frame.support.api.API;
import com.frame.util.ToastUtil;
import com.frame.view.LoadingDialog;

/**
 * 验证码倒计时工具类
 */
public class VerificationCode extends TextView implements LifecycleObserver, BaseRequestView<BaseBean> {
    private int conductColor;//倒计时显示的颜色
    private int endColor;//倒计时结束时显示的颜色
    private String endText;//结束时显示的问题
    private String companyText;//倒计时后面显示的单位
    private int durationTime;//持续时间
    private int intervalTime;//间隔时间
    private Context context;
    private VerificationCountDownTimer countDownTimer;
    protected LoadingDialog progressDialog;

    public VerificationCode(Context context) {
        super(context);
        this.context = context;
        initView(null);
    }

    public VerificationCode(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public VerificationCode(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    /**
     * 开始倒计时
     */
    public void start(String phone, CodeType codeType) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("手机号不能为空");
            return;
        }
        new BaseModel.Builder(this)
                .putParam("Mobile", phone)
                .putParam("VCType", codeType.getValue())
                .setLoadStyle(BaseModel.LoadStyle.DIALOG)
                .create().post(API.GET_DUAN_ZI);//这里填写真实请求地址
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
            setTextColor(getResources().getColor(conductColor));
        }

        @Override
        public void onFinish() {
            setEnabled(true);
            setText(endText);
            setTextColor(getResources().getColor(endColor));
        }

    }

    /**
     * 初始化
     */
    private void initView(AttributeSet attrs) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            if (activity != null)
                activity.getLifecycle().addObserver(this);
        }
        setMinWidth(SizeUtils.dp2px(91));
        setMinHeight(SizeUtils.dp2px(27));
        setGravity(Gravity.CENTER);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerificationCode);
            conductColor = array.getResourceId(R.styleable.VerificationCode_VcConductColor, R.color.frame_colorAccent);
            endColor = array.getResourceId(R.styleable.VerificationCode_VcEndColor, R.color.frame_colorAccent);
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
        progressDialog = new LoadingDialog(getContext());//初始化加载框
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void requestSuccess(BaseBean data, BaseModel.LoadMode loadMode, Object tag, int pageCount) {
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

    /**
     * 显示加载对话框
     */
    @Override
    public void showLoadingDialog(String msg, boolean isCancel) {
        String message = TextUtils.isEmpty(msg) ? "拼命加载中..." : msg;
        if (progressDialog == null)
            progressDialog = new LoadingDialog(context);
        progressDialog.setCancle(isCancel);
        progressDialog.setMsg(message);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
