package com.frame.support.api;


import com.frame.config.AppConfig;

public class API {
    public static final String BASEURL = AppConfig.getUrl();//接口统一请求地址
    public final static String CITY_WEATHER = BASEURL + "weatherApi?city=深圳";//获取城市天气
    public final static String PERSONALIZED_SIGNATURE = BASEURL + "femaleNameApi";//获取个性签名
    public final static String PERSONALIZED_SIGNATURE_ERROR = BASEURL + "femaleNameApi?page=1000";//获取个性签名(没有数据的请求)

}
