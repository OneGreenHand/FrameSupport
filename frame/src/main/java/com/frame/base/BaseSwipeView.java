package com.frame.base;

import com.frame.bean.BaseBean;

public interface BaseSwipeView<B extends BaseBean> extends BaseRequestView<B> {
    void resetRefreshView();
}