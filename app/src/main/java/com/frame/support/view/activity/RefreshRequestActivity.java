package com.frame.support.view.activity;

import android.os.Bundle;

import com.frame.adapter.BaseQuickAdapter;
import com.frame.support.R;
import com.frame.support.bean.PersonalizedSignatureBean;
import com.frame.support.presenter.RefreshRequestPt;
import com.frame.base.BaseModel;
import com.frame.base.BaseQuickHolder;
import com.frame.base.activity.BaseSwipeListActivity;
import com.frame.support.view.adapter.ExampleAdapter;

/**
 * @describe 上拉刷新和下拉加载
 */
public class RefreshRequestActivity extends BaseSwipeListActivity<RefreshRequestPt, PersonalizedSignatureBean, PersonalizedSignatureBean.DataBean> {

    @Override
    public BaseQuickAdapter<PersonalizedSignatureBean.DataBean, BaseQuickHolder> setAdapter() {
        return new ExampleAdapter();
    }

    @Override
    public void loadMoreListRequest(int page) {
        mPresenter.getPersonalizedSignature(page);
    }

    @Override
    protected void onRefreshRequest() {
        mPresenter.getPersonalizedSignature(1);
    }

    @Override
    protected RefreshRequestPt setPresenter() {
        return new RefreshRequestPt(this);
    }

    @Override
    protected void reRequest() {
        mPresenter.getPersonalizedSignature(1);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initTitleBar("下拉刷新上拉加载示例");
    }

    @Override
    protected void initData() {
        mPresenter.getPersonalizedSignature(1);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.layout_head_foot_example;
    }

    @Override
    public void requestSuccess(PersonalizedSignatureBean data, BaseModel.LoadMode loadMode, Object tag, int pageCount) {
        notifyAdapterStatus(data.data, loadMode, pageCount);
    }

}
