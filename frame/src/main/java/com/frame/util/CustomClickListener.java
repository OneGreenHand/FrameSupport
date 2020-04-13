package com.frame.util;

import android.view.View;

/**
 * @describe 通用点击事件(防止暴力点击)
 */
public abstract class CustomClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 1000;

    public CustomClickListener() {
    }

    public CustomClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) { // 单次点击事件
            onSingleClick(v);
            mLastClickTime = nowTime;
        }
    }

    public abstract void onSingleClick(View v);

}