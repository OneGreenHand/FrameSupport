package com.frame.base.fragment;

import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.LogUtils;
import com.frame.R;
import com.frame.base.BasePresenter;
import com.frame.base.BaseRequestView;
import com.frame.bean.BaseBean;
import com.frame.loadingView.VaryViewHelperController;
import com.frame.util.CustomClickListener;
import com.frame.util.ToastUtil;

/**
 * 带网络请求的fragment
 */
public abstract class BaseRequestFragment<T extends ViewBinding, P extends BasePresenter, B extends BaseBean> extends BaseFragment<T> implements BaseRequestView<B> {
    protected P mPresenter;
    private VaryViewHelperController mVaryViewHelperController;

    @Override
    protected void initCommon() {
        View frameRootView = viewBinding.getRoot().findViewById(R.id.frame_root_view);
        if (frameRootView != null)
            mVaryViewHelperController = new VaryViewHelperController(frameRootView, getEmptyView());
        mPresenter = setPresenter();
    }

    protected abstract P setPresenter();

    @Override
    public void requestFail(B data, Object tag) {
        ToastUtil.showShortToast(data.msg);
    }

    protected abstract void reRequest();

    @Override
    public void requestError(Throwable e, Object tag) {
        LogUtils.e("okhttp", e.getMessage());
    }

    @Override
    public void showLoadingView() {
        if (mVaryViewHelperController != null)
            mVaryViewHelperController.showLoading();
    }

    @Override
    public void showEmptyView() {
        if (mVaryViewHelperController != null)
            mVaryViewHelperController.showEmpty(getEmptyViewMsg());
    }

    @Override
    public void showNetErrorView(String tips) {
        if (mVaryViewHelperController != null)
            mVaryViewHelperController.showNetworkError(new CustomClickListener() {
                @Override
                public void onSingleClick(View v) {
                    reRequest();
                }
            }, tips);
    }

    @Override
    public void refreshView() {
        if (mVaryViewHelperController != null)
            mVaryViewHelperController.restore();
    }

    @Override
    public void tokenOverdue() {  //登录过期
    }

    //设置空数据提示文本
    protected String getEmptyViewMsg() {
        return getResString(R.string.frame_no_data);
    }

    //设置空数据布局(重写即为替换)
    protected int getEmptyView() {
        return R.layout.frame_layout_no_data;
    }

}
