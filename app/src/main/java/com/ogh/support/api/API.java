package com.ogh.support.api;


import com.frame.config.BaseConfig;

public class API {
    private static final String BASE_URL = BaseConfig.getUrl();//接口统一请求地址
    public final static String GET_DUAN_ZI = BASE_URL + "getJoke";//获取段子  //https://api.apiopen.top/getJoke?page=2&count=2&type=text
}
