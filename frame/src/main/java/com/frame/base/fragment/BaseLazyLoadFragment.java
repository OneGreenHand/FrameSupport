package com.frame.base.fragment;

import android.os.Bundle;


/**
 * @Description: Fragment基类
 */
public abstract class BaseLazyLoadFragment extends BaseFragment {
    /**
     * 视图是否已经初初始化
     */
    private boolean isInit = false;
    private boolean isLoad = false;

    public BaseLazyLoadFragment() {
        super();
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isNeedLoad()) {
            if (!isLoad) {
                isCanLoadData();
            }
        } else {
            isCanLoadData();
        }
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     * 3.没有被加载过
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {

    }

    protected boolean isNeedLoad() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isInit = false;
        isLoad = false;
    }
}
