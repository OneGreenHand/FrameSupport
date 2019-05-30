package com.frame.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BasePresenter;
import com.frame.base.BaseSwipeView;
import com.frame.widget.VpSwipeRefreshLayout;
import com.frame.bean.BaseBean;


/**
 * @des 通用请求fragment(带下拉)
 */
public abstract class BaseSwipeFragment<P extends BasePresenter, B extends BaseBean> extends BaseRequestFragment<P, B> implements VpSwipeRefreshLayout.OnRefreshListener, BaseSwipeView<B> {
    public VpSwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mSwipeRefreshLayout = new VpSwipeRefreshLayout(mActivity);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.addView(rootView);
        mSwipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return mSwipeRefreshLayout;
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
