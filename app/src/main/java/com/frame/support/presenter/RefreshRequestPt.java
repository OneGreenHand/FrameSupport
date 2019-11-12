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
                .setLoadStyle(page == 1 ? BaseModel.LoadStyle.DIALOG_VIEW : BaseModel.LoadStyle.NONE)
                .setLoadMode(page == 1 ? BaseModel.LoadMode.FIRST : BaseModel.LoadMode.LOAD_MODE)
                .create()
                .get(API.GET_DUAN_ZI + "?page=" + page + "&count=15&type=text", DuanZiBean.class);
    }


}
