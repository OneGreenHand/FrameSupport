package com.frame.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.blankj.utilcode.util.ScreenUtils;
import com.frame.R;
import com.frame.util.CustomClickListener;


/**
 * 通用dialog
 */
public abstract class BaseDialog extends Dialog implements LifecycleObserver {

    public Context mContext;
    private int gravity;
    private boolean isCancelable = true;//点击外部是否可以取消弹框

    public BaseDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        initCommon(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        initCommon(context);
    }

    public BaseDialog(Context context, int gravity, boolean isCancelable) {
        super(context, R.style.ActionSheetDialogStyle);
        this.gravity = gravity;
        this.isCancelable = isCancelable;
        initCommon(context);
    }

    protected void initCommon(Context context) {
        this.mContext = context;
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            if (!activity.isFinishing() && !activity.isDestroyed())//注册绑定生命周期
                activity.getLifecycle().addObserver(this);
        }
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCancelable);
        setContentView(getLayoutID());
        Window window = getWindow(); //必须在setContentView()之后
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = getWidth();
            attributes.height = getHeight();
            attributes.gravity=gravity == 0 ? Gravity.CENTER : gravity;
            window.setAttributes(attributes);
        }
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
        return (int) (ScreenUtils.getScreenWidth() * 0.9);
    }

    protected int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected void initView(Context context) {
    }

    protected abstract int getLayoutID();

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