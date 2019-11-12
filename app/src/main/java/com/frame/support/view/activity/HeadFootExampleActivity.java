package com.frame.support.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frame.base.BaseModel;
import com.frame.base.activity.BaseSwipeActivity;
import com.frame.bean.BaseBean;
import com.frame.support.R;
import com.frame.support.bean.DuanZiBean;
import com.frame.support.presenter.HeadFootExamplePt;
import com.frame.support.view.adapter.ExampleAdapter;

import butterknife.BindView;

/**
 * @describe 添加头部和脚部的示例(带下拉刷新上拉加载)
 */
public class HeadFootExampleActivity extends BaseSwipeActivity<HeadFootExamplePt, BaseBean> {

    @BindView(R.id.frame_recycleView)
    RecyclerView recycleview;
    ExampleAdapter adapter;

    @Override
    protected void onRefreshRequest() {
        mPresenter.getDuanZiList(1);
    }

    @Override
    protected HeadFootExamplePt setPresenter() {
        return new HeadFootExamplePt(this);
    }

    @Override
    protected void reRequest() {
        mPresenter.getDuanZiList(1);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initTitleBar("添加头部和脚部的示例");
        recycleview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ExampleAdapter();
        initHeadFootView();
        recycleview.setAdapter(adapter);
    }

    /**
     * 初始化头部和脚部
     */
    private void initHeadFootView() {
        TextView head = new TextView(mContext);
        head.setText("我是添加的头部");
        head.setTextSize(20);
        head.setTextColor(Color.parseColor("#3BC68C"));
        head.setGravity(Gravity.CENTER);
        TextView foot = new TextView(mContext);
        foot.setText("我是添加的脚部");
        foot.setTextSize(20);
        foot.setTextColor(Color.parseColor("#3BC68C"));
        foot.setGravity(Gravity.CENTER);
        adapter.addHeaderView(head);
        adapter.addFooterView(foot);
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
        adapter.setNewData(duanZiBean.result);
    }
}
