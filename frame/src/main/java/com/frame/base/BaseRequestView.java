package com.frame.base;

import com.frame.bean.BaseBean;

public interface BaseRequestView<B extends BaseBean> extends BaseView {
    void requestSuccess(B data, Object tag, int pageIndex, int pageCount);

    void requestFail(B data, Object tag);

    void requestError(Throwable e, Object tag);

    void showLoadingView();

    void showEmptyView();

    void showNetErrorView(String tips);

    void refreshView();

    void tokenOverdue();
}