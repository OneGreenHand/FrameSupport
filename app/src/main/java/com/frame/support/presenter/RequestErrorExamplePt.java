package com.frame.support.presenter;

import com.frame.support.api.API;
import com.frame.support.bean.PersonalizedSignatureBean;
import com.frame.support.view.activity.NoDataExampleActivity;
import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;

public class RequestErrorExamplePt extends BasePresenter<NoDataExampleActivity> {
    public RequestErrorExamplePt(NoDataExampleActivity requestErrorExampleActivity) {
        super(requestErrorExampleActivity);
    }

    /**
     * 获取个性签名
     */
    public  void getPersonalizedSignature( ){
        createRequestBuilder()
                .setLoadStyle(BaseModel.LoadStyle.DIALOG_VIEW)
                .create()
                .get(API.PERSONALIZED_SIGNATURE_ERROR,PersonalizedSignatureBean.class);
    }
}
