package com.ogh.support.bean;

/**
 * @describe 微信回调结果，一般都是这些参数
 * TODO  这里要根据后台返回的字段名和返回体，进行修改
 */
public class WXResult {

    public String appId;
    public String partnerId;
    public String prepayId;
    public String packageValue;
    public String nonceStr;
    public String timeStamp;
    public String sign;
}