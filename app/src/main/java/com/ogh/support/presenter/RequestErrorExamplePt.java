package com.ogh.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.ogh.support.api.API;
import com.ogh.support.bean.DuanZiBean;
import com.ogh.support.view.activity.NoDataExampleActivity;

public class RequestErrorExamplePt extends BasePresenter<NoDataExampleActivity> {
    public RequestErrorExamplePt(NoDataExampleActivity requestErrorExampleActivity) {
        super(requestErrorExampleActivity);
    }

    /**
     * 获取段子
     */
    public void getPersonalizedSignature() {
        createRequestBuilder()
                .setLoadStyle(BaseModel.LoadStyle.DIALOG_VIEW)
                .create()
                .get(API.GET_DUAN_ZI + "?page=999&count=10&type=text", DuanZiBean.class);
    }
}
