package com.ogh.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.ogh.support.api.API;
import com.ogh.support.bean.DuanZiBean;
import com.ogh.support.view.activity.RefreshRequestActivity;

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