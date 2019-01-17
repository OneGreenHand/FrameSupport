package com.frame.support.api;


import com.frame.config.AppConfig;

public class API {
    public static final String BASEURL = AppConfig.config.getUrl();//接口统一请求地址
    public final static String CITY_WEATHER = "https://www.apiopen.top/weatherApi?city=深圳";//获取城市天气
    public final static String PERSONALIZED_SIGNATURE = "https://www.apiopen.top/femaleNameApi";//获取个性签名
    public final static String PERSONALIZED_SIGNATURE_ERROR = "https://www.apiopen.top/femaleNameApi?page=1000";//获取个性签名(没有数据的请求)

}
