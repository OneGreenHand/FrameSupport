package com.frame.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.frame.R;
import com.frame.base.BaseView;
import com.frame.view.LoadingDialog;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Activity基类，所有的Activity均继承它
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity implements BaseView {
    protected Activity mContext = this;
    protected LoadingDialog progressDialog;
    private boolean isDestroyed = false;//是否真的被finish
    protected T viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            try {
                Class<T> clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
                Method method = clazz.getMethod("inflate", LayoutInflater.class);
                viewBinding = (T) method.invoke(null, getLayoutInflater());
                setContentView(viewBinding.getRoot());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initCommon();
        init(savedInstanceState);//初始化
        if (isImmersionBarEnabled())   //初始化沉浸式状态栏,所有子类都将继承这些相同的属性,请在设置界面之后设置
            initImmersionBar();
        if (isRegisterBus())
            BusUtils.register(this);
    }

    protected void initCommon() {
    }

    protected abstract void init(Bundle savedInstanceState);

    protected String getResString(int res) {
        return getResources().getString(res);
    }

    /**
     * 是否需要注册BusUtils
     */
    protected boolean isRegisterBus() {
        return false;
    }

    /**
     * 是否需要开启沉浸式
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 默认设置状态栏配置(状态栏为主题色、字体自动变色(须指定状态栏颜色),同时解决状态栏和布局重叠问题)
     */
    private void initImmersionBar() {
        initImmersionBar(R.color.frame_white);
    }

    /**
     * 设置其他颜色,主要用于和通用标题栏颜色不符的情况
     */
    protected void initImmersionBar(int color) {
        ImmersionBar.with(this).statusBarColor(color).autoStatusBarDarkModeEnable(true, 0.2f).fitsSystemWindows(true).init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev))
                KeyboardUtils.hideSoftInput(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            dismissLoadingDialog();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示加载对话框
     */
    @Override
    public void showLoadingDialog(String msg, boolean isCancel) {
        String message = TextUtils.isEmpty(msg) ? getResString(R.string.frame_load_ing) : msg;
        if (progressDialog == null)
            progressDialog = new LoadingDialog(mContext);
        progressDialog.setCancel(isCancel);
        progressDialog.setMsg(message);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    @Override
    public void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing())
            destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
    }

    /**
     * 解决activity被finish后onDestroy()不立即执行问题
     */
    private void destroy() {
        if (!isDestroyed) { // 回收资源
            isDestroyed = true;
            dismissLoadingDialog();
            if (isRegisterBus())
                BusUtils.unregister(this);
        }
    }
}