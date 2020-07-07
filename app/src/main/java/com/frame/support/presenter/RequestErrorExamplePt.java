package com.frame.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.frame.support.api.API;
import com.frame.support.bean.DuanZiBean;
import com.frame.support.view.activity.NoDataExampleActivity;

public class RequestErrorExamplePt extends BasePresenter<NoDataExampleActivity> {
    public RequestErrorExamplePt(NoDataExampleActivity requestErrorExampleActivity) {
        super(requestErrorExampleActivity);
    }

    /**
     * 获取段子
     */
    public  void getPersonalizedSignature( ){
        createRequestBuilder()
                .setLoadStyle(BaseModel.LoadStyle.DIALOG_VIEW)
                .create()
                .get(API.GET_DUAN_ZI+"?page=999&count=10&type=text", DuanZiBean.class);
    }
}
