package com.frame.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.frame.R;
import com.frame.adapter.BaseViewHolder;
import com.frame.widget.RecycleViewDivider;

public class BaseQuickHolder extends BaseViewHolder {

    public BaseQuickHolder(View view) {
        super(view);
    }

    public BaseQuickHolder setGlideImg(@IdRes int id, String url) {
        ImageView view = getView(id);
        Glide.with(view.getContext())
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.img_showing).error(R.drawable.img_show_error))
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
     * 设置本地图片
     */
    public BaseQuickHolder setLocalImage(int viewId, int url, int width, int height, Context mContext) {
        ImageView iv = getView(viewId);
        Glide.with(mContext)
                .load(url)//加载地址
                .apply(new RequestOptions().override(width, height).placeholder(R.drawable.img_showing).error(R.drawable.img_show_error))
                .into(iv);
        return this;
    }

    /**
     * 设置图片
     */
    public BaseQuickHolder setImageByUrl(int viewId, String url, Context mContext) {
        ImageView iv = getView(viewId);
        Glide.with(mContext)
                .load(url)//加载地址
                .apply(new RequestOptions().placeholder(R.drawable.img_showing).error(R.drawable.img_show_error))
                .into(iv);
        return this;
    }

//    /**
//     * 设置圆角图片(并不兼容)
//     */
//    public BaseQuickHolder setFilletImageByUrl(int viewId, String url, int radius, int margin, RoundedCornersTransformation.CornerType cornerType, Context mContext) {
//        ImageView iv = getView(viewId);
//        Glide.with(mContext)
//                .load(url)//加载地址
//                .apply(RequestOptions.bitmapTransform(new MultiTransformation(
//                        new CenterCrop(),
//                        new RoundedCornersTransformation(radius, margin, cornerType))).placeholder(R.drawable.img_showing).error(R.drawable.img_show_error))
//                .into(iv);
//        return this;
//    }

    /**
     * 设置圆形图片(并不兼容)
     */
    public BaseQuickHolder setRoundImageByUrl(int viewId, String url, final Context mContext) {
        final ImageView iv = getView(viewId);
        Glide.with(mContext)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().error(R.drawable.img_show_error))
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
    public BaseQuickHolder setLayoutManager(int viewId, RecyclerView.Adapter adapter, Context mContext, int direction, String color, int height, int width, int hdCount, int ftCount) {
        RecyclerView rv = getView(viewId);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        if (direction == 0)
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        else
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        rv.setLayoutManager(linearLayoutManager);
        rv.addItemDecoration(RecycleViewDivider.builder()
                .color(Color.parseColor(color))// 设颜色
                .height(height)// 设线高px，用于画水平线
                .width(width)
                .headerCount(hdCount)
                .footerCount(ftCount)
                .build());
        rv.setAdapter(adapter);
        return this;
    }

}