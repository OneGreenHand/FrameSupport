package com.frame.base;

import com.frame.bean.BaseBean;

public interface BaseSwipeListView<B extends BaseBean> extends BaseSwipeView<B> {
    void loadMoreFailView();
}