package com.frame.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.frame.R;
import com.frame.util.CustomClickListener;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import butterknife.ButterKnife;

/**
 * 通用dialog
 */
public abstract class BaseDialog extends Dialog implements LifecycleObserver {

    private int animResId = 0;//动画样式
    private int gravity = 0;
    private boolean isCancelable = true;//点击外部是否可以取消弹框
    public Context mContext;

    public BaseDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);//默认为dialog样式
        this.mContext = context;
        initCommon(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        initCommon(context);
    }

    public BaseDialog(Context context, int themeResId, int gravity) {
        super(context, themeResId);
        this.gravity = gravity;
        this.mContext = context;
        initCommon(context);
    }

    public BaseDialog(Context context, int themeResId, int animResId, int gravity) {
        super(context, themeResId);
        this.gravity = gravity;
        this.animResId = animResId;
        this.mContext = context;
        initCommon(context);
    }

    public BaseDialog(Context context, int themeResId, int gravity, boolean isCancelable) {
        super(context, themeResId);
        this.gravity = gravity;
        this.isCancelable = isCancelable;
        this.mContext = context;
        initCommon(context);
    }

    /**
     * 在initCommon()方法前调用
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    protected void initCommon(Context context) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            if (!activity.isFinishing() && !activity.isDestroyed())//注册绑定生命周期
                activity.getLifecycle().addObserver(this);
        }
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCancelable);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        //必须在setContentView()之后
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = getWidth();
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
        window.setGravity(gravity == 0 ? Gravity.CENTER : gravity);
        //设置动画（默认dialog样式）
        window.setWindowAnimations(animResId == 0 ? R.style.ActionSheetDialogAnimation : animResId);
        initView(context);
    }

    @Override
    public void show() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (activity.isFinishing() || activity.isDestroyed())
                return;
        }
        super.show();
    }
    protected int getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }
    protected void initView(Context context) {
    }

    protected abstract @LayoutRes
    int getLayoutID();

    public BaseDialog setOnClickListener(int[] ids, CustomClickListener listener) {
        if (listener != null && ids != null && ids.length != 0) {
            for (int id : ids)
                findViewById(id).setOnClickListener(listener);
        }
        return this;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (isShowing())
            dismiss();
    }
}