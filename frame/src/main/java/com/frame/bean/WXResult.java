package com.frame.bean;

import java.io.Serializable;

/**
 * @author OneGreenHand
 * @package com.frame.bean
 * @fileName WEXModel
 * @data on 2019/3/15 9:17
 * @describe
 */
public class WXResult extends BaseBean implements Serializable {

    public  DataBean data;

    public  class DataBean implements Serializable {
        public String appId;
        public String partnerId;
        public String prepayId;
        public String packageValue;
        public String nonceStr;
        public String timeStamp;
        public String sign;
    }
}