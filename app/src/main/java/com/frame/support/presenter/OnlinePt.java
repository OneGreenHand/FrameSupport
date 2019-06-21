package com.frame.support.presenter;

import com.frame.support.api.API;
import com.frame.support.bean.CityWeatherBean;
import com.frame.support.view.fragment.OnlineFragment;
import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;

public class OnlinePt extends BasePresenter<OnlineFragment> {

    public OnlinePt(OnlineFragment onlineFragment) {
        super(onlineFragment);
    }

    /**
     * 获取城市天气信息
     */
    public void getCityWeather() {
        createRequestBuilder()
                .setMsgType("获取天气信息中~")
                .setLoadStyle(BaseModel.LoadStyle.DIALOG)
                .create()
                .get(API.CITY_WEATHER, CityWeatherBean.class);
    }
}
