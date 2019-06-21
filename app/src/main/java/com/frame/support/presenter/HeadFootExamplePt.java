package com.frame.support.presenter;

import com.frame.support.api.API;
import com.frame.support.bean.PersonalizedSignatureBean;
import com.frame.support.view.activity.HeadFootExampleActivity;
import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;

public class HeadFootExamplePt extends BasePresenter<HeadFootExampleActivity> {
    public HeadFootExamplePt(HeadFootExampleActivity headFootExampleActivity) {
        super(headFootExampleActivity);
    }

    /**
     * 获取个性签名
     */
    public void getPersonalizedSignature(int page) {
        createRequestBuilder()
                .setLoadStyle(BaseModel.LoadStyle.DIALOG)
                .putParam("page", page)
                .create()
                .post(API.PERSONALIZED_SIGNATURE, PersonalizedSignatureBean.class);
    }
}
