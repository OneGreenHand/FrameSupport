package com.frame.base.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.R;
import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.frame.base.BaseQuickHolder;
import com.frame.bean.BaseBean;
import com.frame.adapter.BaseQuickAdapter;
import com.frame.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @dessribe 自带recycler view的基类
 * Tips:布局中的recyclerView的id必须是frame_recycleView
 */
public abstract class BaseSwipeListFragment<P extends BasePresenter, B extends BaseBean, AB> extends BaseSwipeFragment<P, B> {

    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter<AB, BaseQuickHolder> mBaseAdapter;
    private int page = AppConfig.ViewPage.START_INDEX;
    protected View mEmptyView;

    @Override
    protected void initCommon() {
        super.initCommon();
        mRecyclerView = rootView.findViewById(R.id.frame_recycleView);
        if (mRecyclerView == null)
            throw new RuntimeException("布局中必须有RecyclerView，并且RecyclerView中的ID为frame_recycleView");
        mRecyclerView.setLayoutManager(setLayoutManager());
        mBaseAdapter = setAdapter();
        mRecyclerView.setAdapter(mBaseAdapter);
        mEmptyView = LayoutInflater.from(mActivity).inflate(getEmptyView(), (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) mEmptyView.findViewById(R.id.tv_view_pager_no_data_content)).setText(getEmptyViewMsg());
    }

    /**
     * 手动设置数据
     * 如果数据为空会设置空布局
     * 无需关注切换布局问题
     */
    public void setEmptyData(List<AB> data) {
        if (data == null || data.isEmpty()) {
            mBaseAdapter.setNewData(new ArrayList<>());
            mBaseAdapter.setHeaderAndEmpty(isHeaderAndEmpty());
            mBaseAdapter.setEmptyView(mEmptyView);
        } else {
            mBaseAdapter.setNewData(data);
        }
    }

    //自动更新adapter状态
    public void notifyAdapterStatus(List<AB> data, BaseModel.LoadMode loadMode, int pageCount) {
        if (loadMode == BaseModel.LoadMode.LOAD_MODE) {//加载更多
            if (data == null) {
                mBaseAdapter.loadMoreEnd(false);//不显示加载更多
            } else {
                page++;
                mBaseAdapter.addData(data);
                if (data.size() < pageCount)
                    mBaseAdapter.loadMoreEnd(false);
                else
                    mBaseAdapter.loadMoreComplete();
            }
        } else {
            if (data == null || data.isEmpty()) {
                mBaseAdapter.setNewData(new ArrayList<>());
                mBaseAdapter.setHeaderAndEmpty(isHeaderAndEmpty());
                mBaseAdapter.setEmptyView(mEmptyView);
                return;
            }
            page = 1;
            if (data.size() == pageCount) {
                mBaseAdapter.setOnLoadMoreListener(mRequestLoadMoreListener, mRecyclerView);
                page++;
            } else {
                mBaseAdapter.setOnLoadMoreListener(null, mRecyclerView);
            }
            mBaseAdapter.setNewData(data);
        }
    }

    private BaseQuickAdapter.RequestLoadMoreListener mRequestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            if (page > 1) {
                loadMoreListRequest(page);
            } else {
                mBaseAdapter.setEnableLoadMore(false);
            }
        }
    };

    @Override
    public void onRefresh() {
        mBaseAdapter.loadMoreEnd(false);
        super.onRefresh();
    }

    //重置刷新
    @Override
    public void resetRefreshView() {
        super.resetRefreshView();
        if (!mBaseAdapter.isLoadMoreEnable())
            mBaseAdapter.setEnableLoadMore(true);
    }

    @Override
    public void loadMoreFailView() {
        super.loadMoreFailView();
        mBaseAdapter.loadMoreFail();
    }

    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    public abstract BaseQuickAdapter<AB, BaseQuickHolder> setAdapter();

    //加载更多时要发送的请求
    public abstract void loadMoreListRequest(int page);
}
