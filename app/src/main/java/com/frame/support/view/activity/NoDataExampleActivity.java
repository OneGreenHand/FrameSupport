package com.frame.support.view.activity;

import android.os.Bundle;

import com.frame.support.R;
import com.frame.support.presenter.RequestErrorExamplePt;
import com.frame.base.BaseModel;
import com.frame.base.activity.BaseSwipeActivity;
import com.frame.bean.BaseBean;
import com.frame.util.ToastUtil;

/**
 * @describe 无数据显示示例
 */
public class NoDataExampleActivity extends BaseSwipeActivity<RequestErrorExamplePt, BaseBean> {
    @Override
    protected void onRefreshRequest() {
        ToastUtil.showShortToast("下拉刷新");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected RequestErrorExamplePt setPresenter() {
        return new RequestErrorExamplePt(this);
    }

    @Override
    protected void reRequest() {
        ToastUtil.showShortToast("重新请求");
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initTitleBar("无数据显示示例");
    }

    @Override
    protected void initData() {
        mPresenter.getPersonalizedSignature();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.layout_head_foot_example;
    }

    @Override
    public void requestSuccess(BaseBean data, BaseModel.LoadMode loadMode, Object tag, int pageCount) {
        ToastUtil.showShortToast(data.msg);
    }
}
