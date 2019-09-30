package com.frame.support.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.frame.base.BaseModel;
import com.frame.base.BaseRequestView;
import com.frame.bean.BaseBean;
import com.frame.support.R;
import com.frame.support.api.API;
import com.frame.util.ToastUtil;
import com.frame.view.LoadingDialog;

/**
 * @author OneGreenHand
 * @package com.goume.fzk.main.widget
 * @fileName VerificationCodeLayout
 * @data on 2019/4/10 18:55
 * @describe 验证码倒计时工具类
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
    public void start(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("手机号不能为空");
            return;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed())//如果activity已经被销毁就不显示
                    return;
            } else {
                if (activity.isFinishing())
                    return;
            }
        }
        new BaseModel.Builder(this)
                .putParam("Mobile", phone)
                .setLoadStyle(BaseModel.LoadStyle.DIALOG)
                .create().post(API.CITY_WEATHER);//这里填写真实请求地址
    }

    public enum CodeType {
        LOGIN(1),//登录
        REGISTER(2),//注册
        UPDATE_PWD(3),//修改密码验证码
        INFO_CHANGE(4),//信息变更验证码
        UPDATE_PAY_PWD(5),//修改支付密码
        REAL_NAME_AUTHENTICATION(6),//实名认证
        UPDATE_INVITER(7),//修改邀请人
        REGISTER_SALESMAN(8),//注册业务员
        SMS_GROUP_SENDING(9);//短信群发通知
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
        ToastUtil.showShortToast("验证码发送成功");
        countDownTimer.start();//开始倒计时
    }

    @Override
    public void requestFail(BaseBean data, Object tag) {
        ToastUtil.showShortToast(data.msg);
//        if (data.code == 1003) {//需要前往H5注册页面
//            TipsDialog dialog = new TipsDialog(context);
//            dialog.setLayoutType(1);
//            dialog.setCancelTitle("残忍拒绝");
//            dialog.setSureTitle("前往注册");
//            dialog.setContent("该手机号暂未注册，是否前往注册页面？");
//            dialog.setOnClickListener(new int[]{R.id.cancel_tv, R.id.sure_tv}, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v.getId() == R.id.cancel_tv) {
//                        dialog.dismiss();
//                    } else if (v.getId() == R.id.sure_tv) {
//                        AgreementWebActivity.openWebActivity(context, "", AppConfig.Web.REGISTER);
//                        dialog.dismiss();
//                    }
//                }
//            });
//            dialog.show();
//        }
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
    public void showNetErrorView() {

    }

    @Override
    public void showServerErrorView(String tips) {

    }

    @Override
    public void refreshView() {

    }

    @Override
    public void tokenOverdue() {

    }

    @Override
    public void showLoadingDialog(Object msgType, boolean isCancel) {
        String message = "玩命加载中...";
        if (msgType == null) {
            message = "请求中...";
        } else if (msgType instanceof String) {
            message = (String) msgType;
        } else if (msgType instanceof Integer) {
            getContext().getResources().getString(((int) msgType));
        }
        if (progressDialog == null) {
            progressDialog = new LoadingDialog(getContext());
        }
        progressDialog.setCancle(isCancel);
        progressDialog.setMsg(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
