package com.frame.bean;

import java.io.Serializable;

/**
 * @data on 2019/3/15 9:17
 * @describe 微信回调结果，一般都是这些参数
 */
public class WXResult extends BaseBean implements Serializable {

    public DataBean data;

    public class DataBean implements Serializable {
        public String appId;
        public String partnerId;
        public String prepayId;
        public String packageValue;
        public String nonceStr;
        public String timeStamp;
        public String sign;
    }
}