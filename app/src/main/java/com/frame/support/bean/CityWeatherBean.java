package com.frame.support.bean;

import com.frame.bean.BaseBean;

import java.util.List;

/**
 * @describe 天气实体类
 */
public class CityWeatherBean extends BaseBean {

    /**
     * data : {"yesterday":{"date":"19日星期三","high":"高温 23℃","fx":"无持续风向","low":"低温 17℃","fl":"<![CDATA[<3级]]>","type":"多云"},"city":"深圳","aqi":"54","forecast":[{"date":"20日星期四","high":"高温 25℃","fengli":"<![CDATA[<3级]]>","low":"低温 19℃","fengxiang":"无持续风向","type":"多云"},{"date":"21日星期五","high":"高温 25℃","fengli":"<![CDATA[<3级]]>","low":"低温 19℃","fengxiang":"无持续风向","type":"阴"},{"date":"22日星期六","high":"高温 25℃","fengli":"<![CDATA[<3级]]>","low":"低温 20℃","fengxiang":"无持续风向","type":"阴"},{"date":"23日星期天","high":"高温 23℃","fengli":"<![CDATA[3-4级]]>","low":"低温 17℃","fengxiang":"东北风","type":"小雨"},{"date":"24日星期一","high":"高温 23℃","fengli":"<![CDATA[3-4级]]>","low":"低温 18℃","fengxiang":"东北风","type":"小雨"}],"ganmao":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。","wendu":"24"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * yesterday : {"date":"19日星期三","high":"高温 23℃","fx":"无持续风向","low":"低温 17℃","fl":"<![CDATA[<3级]]>","type":"多云"}
         * city : 深圳
         * aqi : 54
         * forecast : [{"date":"20日星期四","high":"高温 25℃","fengli":"<![CDATA[<3级]]>","low":"低温 19℃","fengxiang":"无持续风向","type":"多云"},{"date":"21日星期五","high":"高温 25℃","fengli":"<![CDATA[<3级]]>","low":"低温 19℃","fengxiang":"无持续风向","type":"阴"},{"date":"22日星期六","high":"高温 25℃","fengli":"<![CDATA[<3级]]>","low":"低温 20℃","fengxiang":"无持续风向","type":"阴"},{"date":"23日星期天","high":"高温 23℃","fengli":"<![CDATA[3-4级]]>","low":"低温 17℃","fengxiang":"东北风","type":"小雨"},{"date":"24日星期一","high":"高温 23℃","fengli":"<![CDATA[3-4级]]>","low":"低温 18℃","fengxiang":"东北风","type":"小雨"}]
         * ganmao : 各项气象条件适宜，无明显降温过程，发生感冒机率较低。
         * wendu : 24
         */

        public YesterdayBean yesterday;
        public String city;
        public String aqi;
        public String ganmao;
        public String wendu;
        public List<ForecastBean> forecast;

        public static class YesterdayBean {
            /**
             * date : 19日星期三
             * high : 高温 23℃
             * fx : 无持续风向
             * low : 低温 17℃
             * fl : <![CDATA[<3级]]>
             * type : 多云
             */
            public String date;
            public String high;
            public String fx;
            public String low;
            public String fl;
            public String type;

        }

        public static class ForecastBean {
            /**
             * date : 20日星期四
             * high : 高温 25℃
             * fengli : <![CDATA[<3级]]>
             * low : 低温 19℃
             * fengxiang : 无持续风向
             * type : 多云
             */

            public String date;
            public String high;
            public String fengli;
            public String low;
            public String fengxiang;
            public String type;

        }
    }
}
