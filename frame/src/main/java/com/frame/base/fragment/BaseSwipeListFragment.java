package com.frame.base.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.R;
import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.frame.base.BaseQuickHolder;
import com.frame.bean.BaseBean;
import com.frame.config.AppConfig;

import java.util.List;

/**
 * @dessribe 自带recycler view的基类
 * Tips:布局中的recyclerView的id必须是frame_recycleView
 */
public abstract class BaseSwipeListFragment<P extends BasePresenter, B extends BaseBean, AB> extends BaseSwipeFragment<P, B> {

    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter<AB, BaseQuickHolder> mBaseAdapter;
    private int page = AppConfig.ViewPage.START_INDEX;

    @Override
    protected void initCommon() {
        super.initCommon();
        mRecyclerView = rootView.findViewById(R.id.frame_recycleView);
        if (mRecyclerView == null)
            throw new RuntimeException("布局中必须有RecyclerView,并且RecyclerView中的ID为frame_recycleView");
        mRecyclerView.setLayoutManager(setLayoutManager());
        mBaseAdapter = setAdapter();
        getAdapter();
    }

    //自动更新adapter状态(正常情况使用)
    protected void notifyAdapterStatus(List<AB> data, BaseModel.LoadMode loadMode, int pageCount) {
        notifyAdapterStatus(data, data.size(), loadMode, pageCount);
    }

    //分组布局使用
    protected void notifyAdapterStatus(List<AB> data, int dataSize, BaseModel.LoadMode loadMode, int pageCount) {
        if (loadMode == BaseModel.LoadMode.LOAD_MODE) {
            if (data == null) {
                mBaseAdapter.loadMoreEnd(false);
            } else {
                page++;
                mBaseAdapter.addData(data);
                if (dataSize < pageCount) {
                    mBaseAdapter.loadMoreEnd(false);
                } else
                    mBaseAdapter.loadMoreComplete();
            }
        } else {
            if (data == null || data.isEmpty()) {
                if (UserAdapterEmpty())
                    mBaseAdapter.setNewData(null);
                return;
            }
            page = 1;
            if (dataSize >= pageCount) {
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

    @Override
    public void resetRefreshView() {
        super.resetRefreshView();
        if (!mBaseAdapter.isLoadMoreEnable())
            mBaseAdapter.setEnableLoadMore(true);
    }

    @Override
    public void loadMoreFailView() {
        mBaseAdapter.loadMoreFail();
    }

    protected RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    protected abstract BaseQuickAdapter<AB, BaseQuickHolder> setAdapter();

    protected abstract void loadMoreListRequest(int page);

    /**
     * 设置适配器,若{@link BaseFragment#UserAdapterEmpty()}为true,切换布局只需setNewData(null)
     */
    private void getAdapter() {
        if (UserAdapterEmpty()) {//必须在setAdapter之前调用
            View mEmptyView = LayoutInflater.from(mActivity).inflate(getEmptyView(), (ViewGroup) mRecyclerView.getParent(), false);
            ((TextView) mEmptyView.findViewById(R.id.tv_view_pager_no_data_content)).setText(getEmptyViewMsg());
            mBaseAdapter.setHeaderAndEmpty(isHeaderAndEmpty());
            mBaseAdapter.setEmptyView(mEmptyView);
        }
        mRecyclerView.setAdapter(mBaseAdapter);
    }

    /**
     * 手动切换适配器
     * 请在notifyAdapterStatus(data,loadMode,pageCount)之前调用
     */
    protected void changeAdapter(BaseQuickAdapter adapter) {
        mBaseAdapter = adapter;
        getAdapter();
    }

    /**
     * 手动设置数据,与{@link BaseFragment#UserAdapterEmpty()}配套使用
     */
    protected void setEmptyData(List data) {
        setChangeEmptyData(data, false);
    }

    /**
     * @param removeView 移除父布局,切换Adapter情况下使用
     */
    protected void setChangeEmptyData(List data, boolean removeView) {
        if (data == null || data.isEmpty()) {
            if (removeView) {
                ViewGroup parent = (ViewGroup) mRecyclerView.getParent();
                if (parent != null)//切换adapter这里不处理会出问题
                    parent.removeAllViews();
            }
            mBaseAdapter.setNewData(null);
        } else {
            mBaseAdapter.setNewData(data);
        }
    }
}
