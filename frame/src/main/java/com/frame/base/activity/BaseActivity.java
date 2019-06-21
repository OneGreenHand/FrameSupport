package com.frame.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.arialyy.aria.core.Aria;
import com.frame.R;
import com.frame.base.BaseView;
import com.frame.bean.EventBean;
import com.frame.util.AppManager;
import com.frame.view.LoadingDialog;
import com.frame.widget.RecycleViewDivider;
import com.gyf.immersionbar.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;


/**
 * Activity基类，所有的Activity均继承它
 */
public abstract class BaseActivity extends RxAppCompatActivity implements BaseView, View.OnClickListener {
    protected Activity mContext = this;
    protected LoadingDialog progressDialog;
    protected RxPermissions rxPermissions;
    private boolean isDestroyed = false;//是否真的被finish
    private InputMethodManager mInputMethodManager;

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);// 添加Activity到堆栈
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除标题
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        initCommon();
        init(savedInstanceState);//初始化
        //初始化沉浸式状态栏,所有子类都将继承这些相同的属性,请在设置界面之后设置
        if (isImmersionBarEnabled())
            initImmersionBar();
        if (rxPermissions == null)
            rxPermissions = new RxPermissions(this);
        if (isRegisterEventBus())
            EventBus.getDefault().register(this);
        if (isUserAria())
            Aria.download(this).register();
        initData();
    }

    /**
     * 方便之类重写方法，确定是否finsh操作
     */
    public boolean needFinsh() {
        return true;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_finish) {
            if (needFinsh())
                finish();
        }
    }

    protected void initCommon() {
    }

    protected abstract void init(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract int getLayoutID();

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(mContext).inflate(layoutResID, null);
        super.setContentView(view);
    }

    protected String getResString(int res) {
        return getResources().getString(res);
    }

    protected int getResInt(int res) {
        return getResources().getInteger(res);
    }

    /**
     * 是否需要使用下载工具类
     */
    protected boolean isUserAria() {
        return false;
    }

    /**
     * 是否需要开启沉浸式
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 默认设置的沉浸式状态栏配置(状态栏为粉红色，并且状态栏字体和导航栏图标变色，同时解决状态栏和布局重叠问题)
     */
    protected void initImmersionBar() {
        ImmersionBar.with(this).statusBarColor(R.color.frame_colorAccent).autoDarkModeEnable(true).fitsSystemWindows(true).init();
    }

    /**
     * 解决布局顶部和状态栏重叠问题(方案一)
     * 在标题栏的上方增加View标签，高度根据android版本来判断，如下述代码。
     * <View
     * android:layout_width="match_parent"
     * android:layout_height="25dp"
     * android:background="@color/c_F44444" 顶部为图片时无需设置这项/>
     * 指定高度为25dp（20~25dp最佳，根据需求定）
     */
    public void resetImmersionBar() {
        ImmersionBar.with(this).reset().init();
    }

    /**
     * 解决布局顶部和状态栏重叠问题(方案二)
     * 在标题栏的上方增加View标签，但是高度指定为0dp。
     * <View
     * android:id="@+id/status_bar_view"
     * android:layout_width="match_parent"
     * android:layout_height="0dp"
     * android:background="@color/c_F44444" 顶部为图片时无需设置这项/>
     *
     * @param view 标题栏上方的view，如上述代码示例
     */
    public void resetImmersionBar(View view) {
        ImmersionBar.with(this).reset().statusBarView(view).init();
    }

    /**
     * 设置其他颜色，主要用于和通用标题栏颜色不符的情况
     *
     * @param color 状态栏的颜色
     */
    public void resetImmersionBar(int color) {
        ImmersionBar.with(this).reset().statusBarColor(color).fitsSystemWindows(true).init();
    }

    /**
     * 是否需要注册EventBus
     */
    public boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(EventBean event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(EventBean event) {
        if (event != null) {
            receiveStickyEvent(event);
            EventBus.getDefault().removeStickyEvent(event);//手动移除，不然还是会接收到
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(EventBean event) {
    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(EventBean event) {
    }

    /**
     * 统一设置标题
     */
    public void initTitleBar(String title) {
        findViewById(R.id.img_finish).setOnClickListener(this);
        ((TextView) findViewById(R.id.app_title)).setText(title);
    }

    /**
     * RecyclerView加载线性布局配置
     */
    public void initLlManager(RecyclerView rv, int orientation, String color, int height, int width, int headerCount, int footerCount) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, orientation == 1 ? OrientationHelper.VERTICAL : OrientationHelper.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(RecycleViewDivider.builder()
                .color(Color.parseColor(color))// 设颜色
                .height(height)// 设线高px，用于画水平线
                .width(width)
                .headerCount(headerCount)
                .footerCount(footerCount)
                .build());

    }

    /**
     * RecyclerView加载表格布局配置
     */
    public void initGlManager(RecyclerView rv, int spanCount, int orientation, String color, int height, int width, int headerCount, int footerCount) {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, spanCount, orientation == 1 ? OrientationHelper.VERTICAL : OrientationHelper.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(RecycleViewDivider.builder()
                .color(Color.parseColor(color))// 设颜色
                .height(height)// 设线高px，用于画水平线
                .width(width)
                .headerCount(headerCount)
                .footerCount(footerCount)
                .build());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            if (mInputMethodManager == null) {
                mInputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                mInputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
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
    public void showLoadingDialog(Object msgType, boolean isCancel) {
        String message = "玩命加载中...";
        if (msgType == null || (msgType instanceof String && ((String) msgType).isEmpty())) {
            message = "请求中...";
        } else if (msgType instanceof String) {
            message = (String) msgType;
        } else if (msgType instanceof Integer) {
            message = getResString((int) msgType);
        }
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
        if (isDestroyed) {
            return;
        } else {
            mInputMethodManager = null;
            // 回收资源
            isDestroyed = true;
            if (isRegisterEventBus())
                EventBus.getDefault().unregister(this);
            // 结束Activity&从堆栈中移除
            AppManager.getAppManager().finishActivity(this);
            //RxAPIManager.get().cancel(this.getClass().getName());//取消网络请求
        }
    }
}
