package com.frame.support.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.frame.base.BaseModel;
import com.frame.base.activity.BaseSwipeActivity;
import com.frame.bean.BaseBean;
import com.frame.support.R;
import com.frame.support.bean.DuanZiBean;
import com.frame.support.presenter.HeadFootExamplePt;
import com.frame.support.view.adapter.ExampleAdapter;
import com.frame.support.widget.TitleBarLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 *  添加头部和脚部的示例(带下拉刷新上拉加载)
 */
public class HeadFootExampleActivity extends BaseSwipeActivity<HeadFootExamplePt, BaseBean> {
    @BindView(R.id.titlebar)
    TitleBarLayout titlebar;
    @BindView(R.id.frame_recycleView)
    RecyclerView recycleview;
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
        titlebar.setTitle("添加头部和脚部的示例");
        recycleview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ExampleAdapter();
        //  adapter.setHeaderWithEmptyEnable(true);//空布局时,头部显示出来
        // adapter.setFooterWithEmptyEnable(true);//空布局时,脚部显示出来
        adapter.setEmptyView(LayoutInflater.from(mContext).inflate(getEmptyView(), recycleview, false));//设置空布局
        initHeadFootView();
        recycleview.setAdapter(adapter);
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
    protected int getLayoutID() {
        return R.layout.layout_head_foot_example;
    }

    @Override
    public void requestSuccess(BaseBean data,  Object tag,int pageIndex, int pageCount){
        DuanZiBean duanZiBean = (DuanZiBean) data;
        if (duanZiBean == null)
            return;
        adapter.setList(duanZiBean.result);
    }
}
