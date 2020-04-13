package com.frame.base;


/**
 * @dessribe 通用V层
 */
public interface BaseView {
    void showLoadingDialog(String msg,boolean isCancel);
    void dismissLoadingDialog();
}