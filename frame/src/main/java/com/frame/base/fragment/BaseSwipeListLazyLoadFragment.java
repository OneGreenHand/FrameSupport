package com.frame.base.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
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
public abstract class BaseSwipeListLazyLoadFragment<P extends BasePresenter, B extends BaseBean, AB> extends BaseSwipeLazyLoadFragment<P, B> {

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
                getLoadMoreModule().loadMoreEnd(false);
            } else {
                page++;
                mBaseAdapter.addData(data);
                if (dataSize < pageCount) {
                    getLoadMoreModule().loadMoreEnd(false);
                } else
                    getLoadMoreModule().loadMoreComplete();
            }
        } else {
            if (data == null || data.isEmpty()) {
                if (UserAdapterEmpty())
                    mBaseAdapter.setNewData(null);
                return;
            }
            page = 1;
            if (dataSize >= pageCount) {
                getLoadMoreModule().setOnLoadMoreListener(() -> {
                    if (page > 1)
                        loadMoreListRequest(page);
                    else
                        getLoadMoreModule().setEnableLoadMore(false);
                });
                page++;
            } else {
                getLoadMoreModule().setOnLoadMoreListener(null);
            }
            mBaseAdapter.setNewData(data);
        }
    }

    private BaseLoadMoreModule getLoadMoreModule() {
        return mBaseAdapter.getLoadMoreModule();
    }

    @Override
    public void onRefresh() {
        getLoadMoreModule().loadMoreEnd(false);
        super.onRefresh();
    }

    @Override
    public void resetRefreshView() {
        super.resetRefreshView();
        if (!getLoadMoreModule().isEnableLoadMore())
            getLoadMoreModule().setEnableLoadMore(true);
    }

    @Override
    public void loadMoreFailView() {
        getLoadMoreModule().loadMoreFail();
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
            mBaseAdapter.setHeaderWithEmptyEnable(isHeaderAndEmpty());
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

}