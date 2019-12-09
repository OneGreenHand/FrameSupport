package com.frame.base.fragment;

import android.view.ViewGroup;

import com.frame.adapter.BaseQuickAdapter;
import com.frame.base.BasePresenter;
import com.frame.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @dessribe 自带recycler view的基类（解决需要多个adapter切换问题）
 * Tips:布局中的recyclerView的id必须是frame_recycleView
 */
public abstract class BaseSwipeListLazyLoadMultFragment<P extends BasePresenter, B extends BaseBean, AB> extends BaseSwipeListLazyLoadFragment<P, B, AB> {

    /**
     * 手动设置数据
     * 如果数据为空会设置空布局
     * 无需关注切换布局问题
     */
    public void setEmptyData(List data) {
        if (data == null || data.isEmpty()) {
            ViewGroup parent = (ViewGroup) mEmptyView.getParent();
            if (parent != null)//切换adapter这里不处理会出问题
                parent.removeAllViews();
            mBaseAdapter.setNewData(new ArrayList<>());
            mBaseAdapter.setHeaderAndEmpty(isHeaderAndEmpty());
            mBaseAdapter.setEmptyView(mEmptyView);
        } else {
            mBaseAdapter.setNewData(data);
        }
    }

    //必须要在notifyAdapterStatus(data, loadMode, pageCount)之前调用
    public void changeAdapter(BaseQuickAdapter adapter) {
        mBaseAdapter = adapter;
        mRecyclerView.setAdapter(mBaseAdapter);
    }

}