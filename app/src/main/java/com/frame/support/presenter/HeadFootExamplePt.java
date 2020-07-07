package com.frame.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.frame.support.api.API;
import com.frame.support.bean.DuanZiBean;
import com.frame.support.view.activity.HeadFootExampleActivity;

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
                .create()
                .get(API.GET_DUAN_ZI + "?page=1&count=10&type=text", DuanZiBean.class);
    }
}
