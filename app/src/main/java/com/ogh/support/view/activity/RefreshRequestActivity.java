package com.ogh.support.view.activity;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.base.BaseQuickHolder;
import com.frame.base.activity.BaseSwipeListActivity;
import com.frame.bean.BaseBean;
import com.ogh.support.bean.DuanZiBean;
import com.ogh.support.databinding.LayoutHeadFootExampleBinding;
import com.ogh.support.presenter.RefreshRequestPt;
import com.ogh.support.view.adapter.ExampleAdapter;

/**
 *  上拉刷新和下拉加载
 */
public class RefreshRequestActivity extends BaseSwipeListActivity<LayoutHeadFootExampleBinding,RefreshRequestPt, BaseBean, DuanZiBean.ResultBean> {

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
    protected void init(Bundle savedInstanceState) {
        viewBinding.titlebar.setTitle("下拉刷新上拉加载示例");
        mPresenter.getDuanZiList(1);
    }

    @Override
    public void requestSuccess(BaseBean data, Object tag, int pageIndex, int pageCount) {
        DuanZiBean duanZiBean = (DuanZiBean) data;
        if (duanZiBean == null)
            return;
        notifyAdapterStatus(duanZiBean.result, pageIndex, pageCount);
    }

}
