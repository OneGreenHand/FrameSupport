package com.frame.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.frame.support.api.API;
import com.frame.support.bean.DuanZiBean;
import com.frame.support.view.activity.RefreshRequestActivity;

public class RefreshRequestPt extends BasePresenter<RefreshRequestActivity> {
    public RefreshRequestPt(RefreshRequestActivity refreshRequestActivity) {
        super(refreshRequestActivity);
    }

    /**
     * 获取段子
     */
    public void getDuanZiList(int page) {
        createRequestBuilder()
                .setLoadStyle(page == 1 ? BaseModel.LoadStyle.DIALOG : BaseModel.LoadStyle.NONE)
                .setPageIndex(page)
                .create()
                .get(API.GET_DUAN_ZI + "?page=" + page + "&count=10&type=text", DuanZiBean.class);
    }
}