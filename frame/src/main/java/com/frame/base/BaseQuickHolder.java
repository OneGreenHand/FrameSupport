package com.frame.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.frame.R;
import com.frame.adapter.BaseViewHolder;

public class BaseQuickHolder extends BaseViewHolder {

    public BaseQuickHolder(View view) {
        super(view);
    }

    public BaseQuickHolder setGlideImg(@IdRes int id, String url) {
        ImageView view = getView(id);
        Glide.with(view.getContext())
                .load(url)
                .apply(new RequestOptions().placeholder(R.mipmap.img_showing).error(R.mipmap.img_show_error))
                .into(view);
        return this;
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
        Glide.with(mContext)
                .load(url)//加载地址
                .apply(new RequestOptions().placeholder(R.mipmap.img_showing).error(R.mipmap.img_show_error))
                .into(iv);
        return this;
    }

    /**
     * 设置圆形图片(并不兼容)
     */
    public BaseQuickHolder setRoundImageByUrl(int viewId, String url, final Context mContext) {
        final ImageView iv = getView(viewId);
        Glide.with(mContext)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().error(R.mipmap.img_show_error))
                .into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(circularBitmapDrawable);
                    }
                });
        return this;
    }

    /**
     * 设置布局管理器
     * direction （0 水平 1 垂直）
     */
    public BaseQuickHolder setLayoutManager(int viewId, RecyclerView.Adapter adapter, Context mContext, int direction) {
        RecyclerView rv = getView(viewId);
        rv.setLayoutManager(new LinearLayoutManager(mContext, direction == 1 ? OrientationHelper.HORIZONTAL : OrientationHelper.VERTICAL, false));
        rv.setAdapter(adapter);
        return this;
    }

}