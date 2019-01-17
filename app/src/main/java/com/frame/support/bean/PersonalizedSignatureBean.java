package com.frame.support.bean;

import com.frame.bean.BaseBean;

import java.util.List;

/**
 * @describe 个性签名实体类
 */
public class PersonalizedSignatureBean extends BaseBean {

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public List<DataBean> data;

    public static class DataBean {
        public String femalename;
    }
}
