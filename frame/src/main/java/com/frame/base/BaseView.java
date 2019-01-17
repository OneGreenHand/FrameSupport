package com.frame.base;


/**
 * @author lucas
 * @package com.goume.heyding.base
 * @date 2018/7/6
 * @dessribe 通用V层
 */
public interface BaseView {
    void showLoadingDialog(Object msgType,boolean isCancel);
    void dismissLoadingDialog();
}
