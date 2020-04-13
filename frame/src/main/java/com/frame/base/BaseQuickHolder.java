package com.frame.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.frame.util.GlideImageUtil;

public class BaseQuickHolder extends BaseViewHolder {

    public BaseQuickHolder(View view) {
        super(view);
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
     * 设置图片
     */
    public BaseQuickHolder setImageByUrl(int viewId, String url, Context mContext) {
        ImageView iv = getView(viewId);
        GlideImageUtil.showImage(mContext,url,iv);
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