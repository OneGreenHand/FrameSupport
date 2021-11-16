package com.frame.base.activity;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.frame.base.BasePresenter;
import com.frame.base.BaseSwipeView;
import com.frame.bean.BaseBean;
import com.frame.widget.VpSwipeRefreshLayout;

/**
 *  带下拉刷新的Activity
 */
public abstract class BaseSwipeActivity<T extends ViewBinding,P extends BasePresenter, B extends BaseBean> extends BaseRequestActivity<T,P, B> implements VpSwipeRefreshLayout.OnRefreshListener, BaseSwipeView<B> {

    protected VpSwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void setContentView(View view) {
        mSwipeRefreshLayout = new VpSwipeRefreshLayout(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.addView(view);
        mSwipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        super.setContentView(mSwipeRefreshLayout);
    }

    @Override
    public void onRefresh() {
        onRefreshRequest();
    }

    protected abstract void onRefreshRequest();

    @Override
    public void resetRefreshView() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

}