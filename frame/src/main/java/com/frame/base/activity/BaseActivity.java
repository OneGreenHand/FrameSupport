package com.frame.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.frame.R;
import com.frame.base.BaseView;
import com.frame.bean.EventBean;
import com.frame.view.LoadingDialog;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;


/**
 * Activity基类，所有的Activity均继承它
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    protected Activity mContext = this;
    protected LoadingDialog progressDialog;
    private boolean isDestroyed = false;//是否真的被finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        initCommon();
        init(savedInstanceState);//初始化
        //初始化沉浸式状态栏,所有子类都将继承这些相同的属性,请在设置界面之后设置
        if (isImmersionBarEnabled())
            initImmersionBar();
        if (isRegisterEventBus())
            EventBus.getDefault().register(this);
    }

    protected void initCommon() {
    }

    protected abstract void init(Bundle savedInstanceState);

    protected abstract int getLayoutID();

    protected String getResString(int res) {
        return getResources().getString(res);
    }

    protected int getResInt(int res) {
        return getResources().getInteger(res);
    }

    /**
     * 是否需要开启沉浸式
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 空布局时,Rv头部是否显示,结合{@link BaseActivity#UserAdapterEmpty()}使用
     */
    protected boolean isHeaderAndEmpty() {
        return false;
    }

    /**
     * 空布局时,Rv脚部是否显示,结合{@link BaseActivity#UserAdapterEmpty()}使用
     */
    protected boolean isFooterAndEmpty() {
        return false;
    }

    /**
     * 无数据时,是否使用Adapter设置空布局(不能和frame_root_view一起使用)
     */
    protected boolean UserAdapterEmpty() {
        return false;
    }

    /**
     * 默认设置状态栏配置(状态栏为粉红色、字体自动变色(须指定状态栏颜色),同时解决状态栏和布局重叠问题)
     */
    private void initImmersionBar() {
        initImmersionBar(R.color.frame_colorPrimary);
    }

    /**
     * 设置其他颜色,主要用于和通用标题栏颜色不符的情况
     */
    protected void initImmersionBar(int color) {
        ImmersionBar.with(this).statusBarColor(color).autoStatusBarDarkModeEnable(true, 0.2f).fitsSystemWindows(true).init();
    }

    /**
     * 是否需要注册EventBus
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(EventBean event) {
        if (event != null)
            receiveEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(EventBean event) {
        if (event != null) {
            receiveStickyEvent(event);
            EventBus.getDefault().removeStickyEvent(event);//手动移除,不然还是会接收到
        }
    }

    /**
     * 接收到分发的普通事件
     */
    protected void receiveEvent(EventBean event) {
    }

    /**
     * 接受到分发的粘性事件
     */
    protected void receiveStickyEvent(EventBean event) {
    }

    /**
     * 水平布局
     */
    protected void setLayoutManager(RecyclerView rv, int orientation) {
        rv.setLayoutManager(new LinearLayoutManager(mContext, orientation == 1 ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL, false));
    }

    /**
     * 表格布局
     */
    protected void setLayoutManager(RecyclerView rv, int spanCount, int orientation) {
        rv.setLayoutManager(new GridLayoutManager(mContext, spanCount, orientation == 1 ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL, false));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                KeyboardUtils.hideSoftInput(this);
            }
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismissLoadingDialog();
        }
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
        progressDialog.setCancle(isCancel);
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
            if (isRegisterEventBus())
                EventBus.getDefault().unregister(this);
        }
    }
}
