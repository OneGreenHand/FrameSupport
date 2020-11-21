package com.ogh.support.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.frame.base.activity.BaseSwipeActivity;
import com.frame.bean.BaseBean;
import com.ogh.support.bean.DuanZiBean;
import com.ogh.support.databinding.LayoutHeadFootExampleBinding;
import com.ogh.support.presenter.HeadFootExamplePt;
import com.ogh.support.view.adapter.ExampleAdapter;

/**
 * 添加头部和脚部的示例(带下拉刷新上拉加载)
 */
public class HeadFootExampleActivity extends BaseSwipeActivity<LayoutHeadFootExampleBinding,HeadFootExamplePt, BaseBean> {
    ExampleAdapter adapter;

    @Override
    protected void onRefreshRequest() {
        mPresenter.getDuanZiList();
    }

    @Override
    protected HeadFootExamplePt setPresenter() {
        return new HeadFootExamplePt(this);
    }

    @Override
    protected void reRequest() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        viewBinding.titlebar.setTitle("添加头部和脚部的示例");
        viewBinding.frameRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ExampleAdapter();
        //  adapter.setHeaderWithEmptyEnable(true);//空布局时,头部显示出来
        // adapter.setFooterWithEmptyEnable(true);//空布局时,脚部显示出来
        initHeadFootView();
        viewBinding.frameRecycleView.setAdapter(adapter);
        adapter.setEmptyView(getEmptyView());//设置空布局(引用资源文件必须放在setAdapter()后才有效)
        mPresenter.getDuanZiList();
    }

    /**
     * 初始化头部和脚部
     */
    private void initHeadFootView() {
        TextView head = new TextView(mContext);
        head.setText("我是adapter的头部");
        head.setTextSize(18);
        head.setTextColor(Color.parseColor("#3BC68C"));
        head.setGravity(Gravity.CENTER);
        TextView foot = new TextView(mContext);
        foot.setText("我是adapter的脚部");
        foot.setTextSize(18);
        foot.setTextColor(Color.parseColor("#3BC68C"));
        foot.setGravity(Gravity.CENTER);
        adapter.addHeaderView(head);
        adapter.addFooterView(foot);
    }

    @Override
    public void requestSuccess(BaseBean data, Object tag, int pageIndex, int pageCount) {
        DuanZiBean duanZiBean = (DuanZiBean) data;
        if (duanZiBean == null)
            return;
        adapter.setList(duanZiBean.result);
    }
}
