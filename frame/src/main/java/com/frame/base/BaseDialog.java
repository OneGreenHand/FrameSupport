package com.frame.base;

import android.app.Activity;
import android.app.Dialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Build;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.frame.R;

import butterknife.ButterKnife;

/**
 * 通用dialog
 */
public abstract class BaseDialog extends Dialog implements LifecycleObserver {

    public View mRootView;
    private int animResId = 0;//动画样式
    private int gravity = 0;
    private boolean isCancelable = true;//点击外部是否可以取消弹框
    private View.OnClickListener mListener;
    private int[] mIds;

    protected int marginLeftAndRightDp = 0;
    public Context bContext;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);//默认为dialog样式
        this.bContext = context;
        initCommon(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.bContext = context;
        initCommon(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId, int gravity) {
        super(context, themeResId);
        this.gravity = gravity;
        this.bContext = context;
        initCommon(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId, int animResId, int gravity) {
        super(context, themeResId);
        this.gravity = gravity;
        this.animResId = animResId;
        this.bContext = context;
        initCommon(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId, int gravity, boolean isCancelable) {
        super(context, themeResId);
        this.gravity = gravity;
        this.isCancelable = isCancelable;
        this.bContext = context;
        initCommon(context);
    }

    /**
     * 在initCommon（）方法前调用
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    protected void initCommon(Context context) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getLifecycle().addObserver(this);
        }
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCancelable);
        if (isUseShadow()) {//是否周围圆角显示
            setContentView(R.layout.dialog_shadow_bg);
            CardView viewById = (CardView) findViewById(R.id.v_root);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewById.getLayoutParams();
            int dp2px = SizeUtils.dp2px(marginLeftAndRightDp);
            layoutParams.leftMargin = dp2px;
            layoutParams.rightMargin = dp2px;
            LayoutInflater.from(viewById.getContext()).inflate(getLayoutID(), viewById);
        } else {
            setContentView(getLayoutID());
        }
        ButterKnife.bind(this);
        //居中显示
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
        window.setGravity(gravity == 0 ? Gravity.CENTER : gravity);
        //设置动画（默认dialog样式）
        window.setWindowAnimations(animResId == 0 ? R.style.ActionSheetDialogAnimation : animResId);
        initView(context);
    }

    protected boolean isUseShadow() {
        return false;
    }

    // Tips:调用改方法后 BindView将会失效
    protected void reSetContentView(Context context, @LayoutRes int res) {
        mRootView = LayoutInflater.from(context).inflate(res, null);
        setContentView(mRootView, new LinearLayout.LayoutParams(getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void show() {
        if (bContext instanceof Activity) {
            Activity activity = (Activity) bContext;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed())//如果activity已经被销毁就不显示
                    return;
            } else {
                if (activity.isFinishing())
                    return;
            }
        }
        super.show();
    }

    protected int getWidth() {
        return LinearLayout.LayoutParams.MATCH_PARENT;
    }

    protected void initView(Context context) {
    }

    protected abstract @LayoutRes
    int getLayoutID();

    public BaseDialog setOnClickListener(int[] ids, View.OnClickListener listener) {
        mIds = ids;
        mListener = listener;
        if (mListener != null && mIds != null && mIds.length != 0) {
            for (int id : mIds) {
                findViewById(id).setOnClickListener(mListener);
            }
        }
        return this;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (isShowing())
            dismiss();
    }
}
