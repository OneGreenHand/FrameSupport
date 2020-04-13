package com.frame.support.api;


import com.frame.config.AppConfig;

public class API {
    private static final String BASEURL = AppConfig.getUrl();//接口统一请求地址
    public final static String GET_DUAN_ZI = BASEURL + "getJoke";//获取段子
    //https://api.apiopen.top/getJoke?page=2&count=2&type=text
}
