package com.frame.base.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arialyy.aria.core.Aria;
import com.frame.base.BaseView;
import com.frame.bean.EventBean;
import com.frame.view.LoadingDialog;
import com.frame.widget.RecycleViewDivider;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * @Description: Fragment基类
 */
public abstract class BaseFragment extends RxFragment implements BaseView {
    protected Activity mActivity;
    protected LoadingDialog progressDialog;
    protected View rootView;
    protected RxPermissions rxPermissions;

    public BaseFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        rootView = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, rootView);//绑定framgent
        initCommon();
        if (rxPermissions == null)
            rxPermissions = new RxPermissions(this);
        if (isRegisterEventBus())
            EventBus.getDefault().register(this);
        if (isUserAria())
            Aria.download(this).register();
        return rootView;
    }

    protected void initCommon() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
        initData();
    }

    protected abstract void init(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract int getLayoutID();

    protected String getResString(int res) {
        return getResources().getString(res);
    }

    protected int getResInt(int res) {
        return getResources().getInteger(res);
    }

    /**
     * 是否需要注册EventBus
     */
    public boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 是否需要使用下载工具类
     */
    protected boolean isUserAria() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(EventBean event) {
        if (event != null)
            receiveEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(EventBean event) {
        if (event != null)
            receiveStickyEvent(event);
        EventBus.getDefault().removeStickyEvent(event);//手动移除，不然还是会接收到
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
            progressDialog = new LoadingDialog(mActivity);
        progressDialog.setCancle(isCancel);
        progressDialog.setMsg(message);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * RecyclerView加载线性布局配置
     */
    public void initLlManager(RecyclerView rv, int orientation, String color, int height, int width, int headerCount, int footerCount) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, orientation == 1 ? OrientationHelper.VERTICAL : OrientationHelper.HORIZONTAL, false);
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
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, spanCount, orientation == 1 ? OrientationHelper.VERTICAL : OrientationHelper.HORIZONTAL, false);
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
    public void onDestroy() {
        super.onDestroy();
        //页面销毁时隐藏dialog，否则重新打开页面时可能会报java.lang.IllegalArgumentException: View not attached to window manager
        dismissLoadingDialog();
        if (isRegisterEventBus())
            EventBus.getDefault().unregister(this);
        //取消请求
        //RxAPIManager.get().cancel(this);
    }
}
