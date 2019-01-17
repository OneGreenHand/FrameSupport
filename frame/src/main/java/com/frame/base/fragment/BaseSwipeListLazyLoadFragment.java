package com.frame.base.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.R;
import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.frame.base.BaseQuickHolder;
import com.frame.bean.BaseBean;
import com.frame.adapter.BaseQuickAdapter;
import com.frame.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @dessribe 自带recycler view的基类
 * Tips:布局中的recyclerView的id必须是frame_recycleView
 */
public abstract class BaseSwipeListLazyLoadFragment<P extends BasePresenter, B extends BaseBean, AB> extends BaseSwipeLazyLoadFragment<P, B> {

    public RecyclerView mRecyclerView;
    private BaseQuickAdapter<AB, BaseQuickHolder> mBaseAdapter;
    protected int page = AppConfig.ViewPage.START_INDEX;
    private View mEmptyView;

    @Override
    protected void initCommon() {
        super.initCommon();
        mRecyclerView = rootView.findViewById(R.id.frame_recycleView);
        if (mRecyclerView == null) {
            throw new RuntimeException("布局中必须有RecyclerView，并且RecyclerView中的ID为frame_recycleView");
        }
        mRecyclerView.setLayoutManager(setLayoutManager());
        mBaseAdapter = setAdapter();
        mRecyclerView.setAdapter(mBaseAdapter);
        mEmptyView = LayoutInflater.from(mActivity).inflate(getEmptyView() == -1 ? R.layout.frame_view_pager_no_data : getEmptyView(), (ViewGroup) mRecyclerView.getParent(), false);
    }

    //自动更新adapter状态
    public void notifyAdapterStatus(List<AB> data, BaseModel.LoadMode loadMode,int pageCount) {
        if (loadMode == BaseModel.LoadMode.LOAD_MODE) {
            if (data == null) {
                mBaseAdapter.loadMoreEnd(false);
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

    //移除adapter中的数据
    public void notifyAdapterRemove(int position) {
        mBaseAdapter.remove(position);
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
