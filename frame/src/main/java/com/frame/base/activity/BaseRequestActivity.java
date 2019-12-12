package com.frame.base.activity;

import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.frame.R;
import com.frame.base.BasePresenter;
import com.frame.base.BaseRequestView;
import com.frame.bean.BaseBean;
import com.frame.loadingView.VaryViewHelperController;
import com.frame.util.ToastUtil;

/**
 * @dessribe 带网络请求的界面
 */
public abstract class BaseRequestActivity<P extends BasePresenter, B extends BaseBean> extends BaseActivity implements BaseRequestView<B> {

    protected P mPresenter;
    private VaryViewHelperController mVaryViewHelperController;

    @Override
    protected void initCommon() {
        super.initCommon();
        View frameRootView = findViewById(R.id.frame_root_view);
        if (frameRootView != null)
            mVaryViewHelperController = new VaryViewHelperController(frameRootView, getEmptyView());
        mPresenter = setPresenter();
    }

    protected abstract P setPresenter();

    @Override
    public void requestFail(B data, Object tag) {
        ToastUtil.showShortToast(data.msg);
    }

    //重新请求数据
    protected abstract void reRequest();

    @Override
    public void requestError(Throwable e, Object tag) {
        LogUtils.e("okhttp", e.getMessage());
    }

    @Override
    public void showLoadingView() {
        if (mVaryViewHelperController != null) {
            mVaryViewHelperController.showLoading();
        }
    }

    @Override
    public void showEmptyView() {
        if (mVaryViewHelperController != null) {
            mVaryViewHelperController.showEmpty(getEmptyViewMsg());
        }
    }

    @Override
    public void showNetErrorView(String tips) {
        if (mVaryViewHelperController != null)
            mVaryViewHelperController.showNetworkError(view -> reRequest(), tips);
    }

    @Override
    public void refreshView() {
        if (mVaryViewHelperController != null) {
            mVaryViewHelperController.restore();
        }
    }

    @Override
    public void tokenOverdue() {
        //登录过期，清除用户重新登录
    }

    //设置空数据提示文本
    public String getEmptyViewMsg() {
        return getResString(R.string.frame_no_data);
    }

    //设置空数据布局(重写即为替换)
    public int getEmptyView() {
        return R.layout.frame_view_pager_no_data;
    }
}
