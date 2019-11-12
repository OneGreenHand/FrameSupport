package com.frame.support.view.activity;

import android.os.Bundle;

import com.frame.adapter.BaseQuickAdapter;
import com.frame.base.BaseModel;
import com.frame.base.BaseQuickHolder;
import com.frame.base.activity.BaseSwipeListActivity;
import com.frame.bean.BaseBean;
import com.frame.support.R;
import com.frame.support.bean.DuanZiBean;
import com.frame.support.presenter.RefreshRequestPt;
import com.frame.support.view.adapter.ExampleAdapter;

/**
 * @describe 上拉刷新和下拉加载
 */
public class RefreshRequestActivity extends BaseSwipeListActivity<RefreshRequestPt, BaseBean, DuanZiBean.ResultBean> {

    @Override
    public BaseQuickAdapter<DuanZiBean.ResultBean, BaseQuickHolder> setAdapter() {
        return new ExampleAdapter();
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
        mPresenter.getDuanZiList(1);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initTitleBar("下拉刷新上拉加载示例");
    }

    @Override
    protected void initData() {
        mPresenter.getDuanZiList(1);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.layout_head_foot_example;
    }

    @Override
    public void requestSuccess(BaseBean data, BaseModel.LoadMode loadMode, Object tag, int pageCount) {
        DuanZiBean duanZiBean = (DuanZiBean) data;
        if (duanZiBean == null || duanZiBean.result == null || duanZiBean.result.isEmpty())
            return;
        notifyAdapterStatus(duanZiBean.result, loadMode, pageCount);
    }

}
