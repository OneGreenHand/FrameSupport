package com.ogh.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.ogh.support.api.API;
import com.ogh.support.bean.DuanZiBean;
import com.ogh.support.view.activity.HeadFootExampleActivity;

public class HeadFootExamplePt extends BasePresenter<HeadFootExampleActivity> {
    public HeadFootExamplePt(HeadFootExampleActivity headFootExampleActivity) {
        super(headFootExampleActivity);
    }

    /**
     * 获取段子
     */
    public void getDuanZiList() {
        createRequestBuilder()
                .setLoadStyle(BaseModel.LoadStyle.DIALOG)
                .putParam("type", "text")
                .create()
                .post(API.GET_DUAN_ZI, DuanZiBean.class);
    }
}
