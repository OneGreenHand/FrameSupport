package com.frame.base;

/**
 * @author lucas
 * @package com.goume.heyding.base.mvp
 * @date 2018/7/6
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
