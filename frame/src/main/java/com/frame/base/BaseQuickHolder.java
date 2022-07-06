package com.frame.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.frame.util.CustomClickListener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.disposables.Disposable;

public class BaseQuickHolder extends BaseViewHolder {

    public BaseQuickHolder(View view) {
        super(view);
    }

    private Disposable mDisposable;

    /**
     * 设置倒计时
     */
    public void addDisposable(Disposable disposable) {
        mDisposable = disposable;
    }

    /**
     * 获取计时
     */
    public Disposable getDisposable() {
        return mDisposable;
    }

    /**
     * 销毁倒计时
     */
    public void disDisposable() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    /**
     * 获取图片控件
     */
    public ImageView getImgView(int viewId) {
        return getView(viewId);
    }

    /**
     * 设置能不能点击
     */
    public BaseQuickHolder setEnabled(int viewId, boolean isEnabled) {
        View v = getView(viewId);
        v.setEnabled(isEnabled);
        return this;
    }

    /**
     * 点击事件(已经做了暴力点击处理)
     */
    public BaseQuickHolder setOnClickListener(int viewId, CustomClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置布局管理器
     * direction （0 水平 1 垂直）
     */
    public BaseQuickHolder setLayoutManager(int viewId, RecyclerView.Adapter adapter, Context mContext, int direction) {
        RecyclerView rv = getView(viewId);
        rv.setLayoutManager(new LinearLayoutManager(mContext, direction == 1 ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL, false));
        rv.setAdapter(adapter);
        return this;
    }
}