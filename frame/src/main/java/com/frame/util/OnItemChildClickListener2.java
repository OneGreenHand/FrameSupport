package com.frame.util;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import org.jetbrains.annotations.NotNull;

/**
 * Adapter子Item点击事件(防暴力点击)
 */
public abstract class OnItemChildClickListener2 implements OnItemChildClickListener {
    private long mLastClickTime;
    private long timeInterval = 1000;

    public OnItemChildClickListener2() {
    }

    public OnItemChildClickListener2(long interval) {
        this.timeInterval = interval;
    }

    public abstract void onSingleClick(BaseQuickAdapter adapter, View view, int position);

    @Override
    public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) { // 单次点击事件
            onSingleClick(adapter, view, position);
            mLastClickTime = nowTime;
        }
    }
}