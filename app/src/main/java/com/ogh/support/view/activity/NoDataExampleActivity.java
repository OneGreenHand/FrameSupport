package com.ogh.support.view.activity;

import android.os.Bundle;

import com.frame.base.activity.BaseSwipeActivity;
import com.frame.bean.BaseBean;
import com.ogh.support.R;
import com.ogh.support.bean.DuanZiBean;
import com.ogh.support.presenter.RequestErrorExamplePt;
import com.ogh.support.widget.TitleBarLayout;
import com.frame.util.ToastUtil;

import butterknife.BindView;

/**
 * 无数据显示示例
 */
public class NoDataExampleActivity extends BaseSwipeActivity<RequestErrorExamplePt, BaseBean> {
    @BindView(R.id.titlebar)
    TitleBarLayout titlebar;

    @Override
    protected void onRefreshRequest() {
        ToastUtil.showShortToast("刷新了");
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
        titlebar.setTitle("无数据显示示例");
        mPresenter.getPersonalizedSignature();
    }

    @Override
    public String getEmptyViewMsg() {
        return "为了看出效果,这里请求了第999页数据,所以返回的数据为空";
    }

    @Override
    protected int getLayoutID() {
        return R.layout.layout_head_foot_example;
    }

    @Override
    public void requestSuccess(BaseBean data, Object tag,int pageIndex, int pageCount) {
        //请求完成后返回的数据是空的，在实体bean中isEmpty()为true，basemodel会自动处理切换为空布局显示
        DuanZiBean duanZiBean = (DuanZiBean) data;
        ToastUtil.showShortToast(duanZiBean.message);
    }

}