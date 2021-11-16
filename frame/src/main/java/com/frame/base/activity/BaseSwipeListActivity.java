package com.frame.base.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.frame.R;
import com.frame.base.BasePresenter;
import com.frame.base.BaseQuickHolder;
import com.frame.base.BaseSwipeListView;
import com.frame.bean.BaseBean;
import com.frame.config.BaseConfig;

import java.util.List;

/**
 * 自带recyclerview的基类
 * Tips:布局中的recyclerView的id必须是frame_recycleView
 */
public abstract class BaseSwipeListActivity<T extends ViewBinding, P extends BasePresenter, B extends BaseBean, AB> extends BaseSwipeActivity<T, P, B> implements BaseSwipeListView<B> {

    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter<AB, BaseQuickHolder> mBaseAdapter;
    private int page = BaseConfig.ViewPage.START_INDEX;

    @Override
    protected void initCommon() {
        super.initCommon();
        mRecyclerView = findViewById(R.id.frame_recycleView);
        if (mRecyclerView == null)
            throw new RuntimeException("布局中必须有RecyclerView,并且RecyclerView中的ID为frame_recycleView");
        mRecyclerView.setLayoutManager(setLayoutManager());
        mBaseAdapter = setAdapter();
        getAdapter();
    }

    /**
     * 空布局时,Rv头部是否显示,结合{@link #UserAdapterEmpty()}使用
     */
    protected boolean isHeaderAndEmpty() {
        return false;
    }

    /**
     * 空布局时,Rv脚部是否显示,结合{@link #UserAdapterEmpty()}使用
     */
    protected boolean isFooterAndEmpty() {
        return false;
    }

    /**
     * 无数据时,是否使用Adapter设置空布局(不能和frame_root_view一起使用)
     */
    protected boolean UserAdapterEmpty() {
        return false;
    }

    private OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            loadMoreListRequest(page);
        }
    };

    //自动更新adapter状态
    protected void notifyAdapterStatus(List<AB> data, int pageIndex, int pageCount) {
        int dataSize = data == null ? 0 : data.size();
        if (pageIndex == BaseConfig.ViewPage.START_INDEX) {
            page = pageIndex;
            if (data == null || data.isEmpty()) {
                if (UserAdapterEmpty())
                    mBaseAdapter.setList(null);
            } else {
                if (dataSize >= pageCount) {
                    page++;
                    getLoadMoreModule().setOnLoadMoreListener(loadMoreListener);
                } else
                    getLoadMoreModule().setOnLoadMoreListener(null);
                mBaseAdapter.setList(data);
            }
        } else {
            if (data == null || data.isEmpty()) {
                getLoadMoreModule().loadMoreEnd(false);
            } else {
                page++;
                mBaseAdapter.addData(data);
                if (dataSize >= pageCount)
                    getLoadMoreModule().loadMoreComplete();
                else
                    getLoadMoreModule().loadMoreEnd(false);
            }
        }
    }

    private BaseLoadMoreModule getLoadMoreModule() {
        return mBaseAdapter.getLoadMoreModule();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getLoadMoreModule().setOnLoadMoreListener(null);
    }

    @Override
    public void loadMoreFailView() {
        getLoadMoreModule().loadMoreFail();
    }

    protected RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected abstract BaseQuickAdapter<AB, BaseQuickHolder> setAdapter();

    protected abstract void loadMoreListRequest(int page);

    /**
     * 设置适配器,若{@link #UserAdapterEmpty()}为true,切换布局只需setList(null)
     */
    private void getAdapter() {
        if (UserAdapterEmpty()) {
            View mEmptyView = LayoutInflater.from(mContext).inflate(getEmptyView(), (ViewGroup) mRecyclerView.getParent(), false);
            ((TextView) mEmptyView.findViewById(R.id.tv_view_pager_no_data_content)).setText(getEmptyViewMsg());
            mBaseAdapter.setHeaderWithEmptyEnable(isHeaderAndEmpty());
            mBaseAdapter.setFooterWithEmptyEnable(isFooterAndEmpty());
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