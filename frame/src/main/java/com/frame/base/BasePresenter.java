package com.frame.base;

/**
 * @dessribe 通用P层
 */
public abstract class BasePresenter<V extends BaseRequestView> {

    protected V mV;

    public BasePresenter(V v) {
        mV = v;
    }

    protected BaseModel.Builder createRequestBuilder(){
        return new BaseModel.Builder(mV);
    }
}
