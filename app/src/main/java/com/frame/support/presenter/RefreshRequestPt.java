package com.frame.support.presenter;

import com.frame.support.api.API;
import com.frame.support.bean.PersonalizedSignatureBean;
import com.frame.support.view.activity.RefreshRequestActivity;
import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;

public class RefreshRequestPt extends BasePresenter<RefreshRequestActivity> {
    public RefreshRequestPt(RefreshRequestActivity refreshRequestActivity) {
        super(refreshRequestActivity);
    }

    /**
     * 获取个性签名
     */
    public void getPersonalizedSignature(int page) {
        createRequestBuilder()
                .setLoadStyle(page == 1 ? BaseModel.LoadStyle.DIALOG_VIEW : BaseModel.LoadStyle.NONE)
                .setLoadMode(page == 1 ? BaseModel.LoadMode.FIRST : BaseModel.LoadMode.LOAD_MODE)
                .putParam("page", page)
                .create()
                .post(API.PERSONALIZED_SIGNATURE, PersonalizedSignatureBean.class);
    }
}
