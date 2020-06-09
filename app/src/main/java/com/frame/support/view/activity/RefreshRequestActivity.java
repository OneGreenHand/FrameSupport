package com.frame.support.view.activity;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.base.BaseQuickHolder;
import com.frame.base.activity.BaseSwipeListActivity;
import com.frame.bean.BaseBean;
import com.frame.support.R;
import com.frame.support.bean.DuanZiBean;
import com.frame.support.presenter.RefreshRequestPt;
import com.frame.support.view.adapter.ExampleAdapter;
import com.frame.support.widget.TitleBarLayout;

import butterknife.BindView;

/**
 *  上拉刷新和下拉加载
 */
public class RefreshRequestActivity extends BaseSwipeListActivity<RefreshRequestPt, BaseBean, DuanZiBean.ResultBean> {
    @BindView(R.id.titlebar)
    TitleBarLayout titlebar;

    @Override
    public BaseQuickAdapter<DuanZiBean.ResultBean, BaseQuickHolder> setAdapter() {
        return new ExampleAdapter();
    }

    @Override
    protected boolean UserAdapterEmpty() {
        return true;
    }

    @Override
    public void loadMoreListRequest(int page) {
        mPresenter.getDuanZiList(page);
    }

    @Override
    protected void onRefreshRequest() {
        mPresenter.getDuanZiList(1);
    }

    @Override
    protected RefreshRequestPt setPresenter() {
        return new RefreshRequestPt(this);
    }

    @Override
    protected void reRequest() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titlebar.setTitle("下拉刷新上拉加载示例");
        mPresenter.getDuanZiList(1);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.layout_head_foot_example;
    }

    @Override
    public void requestSuccess(BaseBean data, Object tag, int pageIndex, int pageCount) {
        DuanZiBean duanZiBean = (DuanZiBean) data;
        if (duanZiBean == null)
            return;
        notifyAdapterStatus(duanZiBean.result, pageIndex, pageCount);
    }

}
