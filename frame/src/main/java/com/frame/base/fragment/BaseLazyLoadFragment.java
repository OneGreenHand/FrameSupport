package com.frame.base.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BaseView;
import com.frame.bean.EventBean;
import com.frame.view.LoadingDialog;
import com.gyf.barlibrary.ImmersionOwner;
import com.gyf.barlibrary.ImmersionProxy;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;


/**
 * @Description: Fragment基类
 */
public abstract class BaseLazyLoadFragment extends BaseFragment implements BaseView, ImmersionOwner {
    protected Activity mActivity;
    protected LoadingDialog progressDialog;
    protected View rootView;
    protected RxPermissions rxPermissions;
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;


    public BaseLazyLoadFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionProxy.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        rootView = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, rootView);//绑定framgent
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
        initCommon();
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
        initData();
        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
        mImmersionProxy.onActivityCreated(savedInstanceState);
    }

    /**
     * 是否可以加载数据,可以加载数据的条件：1.视图已经初始化2.视图对用户可见3.没有被加载过
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    //当视图初始化并且对用户可见的时候去真正的加载数据
    protected abstract void lazyLoad();

    // 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
    protected void stopLoad() {
    }

    protected void initCommon() {
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

    public boolean isRegisterBus() {
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
     * 显示加载对话框
     */
    @Override
    public void showLoadingDialog(Object msgType, boolean isCancel) {
        String message = "玩命加载中...";
        if (msgType == null) {
            message = "请求中...";
        } else if (msgType instanceof String) {
            message = (String) msgType;
        } else if (msgType instanceof Integer) {
            message = getResString((int) msgType);
        }
        if (progressDialog == null) {
            progressDialog = new LoadingDialog(mActivity);
        }
        progressDialog.setCancle(isCancel);
        progressDialog.setMsg(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * ImmersionBar代理类(fragment中使用沉浸式通过实现ImmersionOwner接口来实现沉浸式)
     */
    private ImmersionProxy mImmersionProxy = new ImmersionProxy(this);

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mImmersionProxy.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mImmersionProxy.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImmersionProxy.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //页面销毁时隐藏dialog，否则重新打开页面时可能会报java.lang.IllegalArgumentException: View not attached to window manager
        dismissLoadingDialog();
        isInit = false;
        isLoad = false;
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        //取消请求
        //RxAPIManager.get().cancel(this);
        mImmersionProxy.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mImmersionProxy.onConfigurationChanged(newConfig);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mImmersionProxy.onHiddenChanged(hidden);
    }

    /**
     * 懒加载，在view初始化完成之前执行
     */
    @Override
    public void onLazyBeforeView() {
    }

    /**
     * 懒加载，在view初始化完成之后执行
     */
    @Override
    public void onLazyAfterView() {
    }

    /**
     * Fragment用户可见时候调用
     */
    @Override
    public void onVisible() {
    }

    /**
     * Fragment用户不可见时候调用
     */
    @Override
    public void onInvisible() {
    }

    /**
     * 初始化沉浸式代码
     */
    @Override
    public void initImmersionBar() {

    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     */
    @Override
    public boolean immersionBarEnabled() {
        return false;
    }
}
