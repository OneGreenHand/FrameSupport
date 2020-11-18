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
                .putParam("type", "text")
                .putParam("page", page)
                .putParam("count", "10")
                .create()
                .post(API.GET_DUAN_ZI, DuanZiBean.class);
    }
}