package com.frame.base.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BasePresenter;
import com.frame.base.BaseSwipeView;
import com.frame.bean.BaseBean;
import com.frame.widget.VpSwipeRefreshLayout;

/**
 * @dessribe 带下拉刷新的基类
 */
public abstract class BaseSwipeActivity<P extends BasePresenter, B extends BaseBean> extends BaseRequestActivity<P, B> implements VpSwipeRefreshLayout.OnRefreshListener, BaseSwipeView<B> {

    public VpSwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void setContentView(int layoutResID) {
        View inflate = LayoutInflater.from(this).inflate(layoutResID, null);
        mSwipeRefreshLayout = new VpSwipeRefreshLayout(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.addView(inflate);
        mSwipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        super.setContentView(mSwipeRefreshLayout);
    }

    @Override
    public void onRefresh() {
        onRefreshRequest();
    }

    //当触发刷新时去请求数据
    protected abstract void onRefreshRequest();

    @Override
    public void resetRefreshView() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMoreFailView() {

    }
}
