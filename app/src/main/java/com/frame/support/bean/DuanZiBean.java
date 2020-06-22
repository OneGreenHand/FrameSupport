package com.frame.support.bean;

import com.frame.bean.BaseBean;

import java.util.List;

/**
 * @describe 段子实体类
 */
public class DuanZiBean extends BaseBean {

    public String message;
    public List<ResultBean> result;

    @Override
    public boolean isEmpty() {
        return result == null || result.isEmpty();
    }

    public static class ResultBean {
        public String sid;
        public String text;
        public String type;
        public String thumbnail;
        public String video;
        public String images;
        public String up;
        public String down;
        public String forward;
        public String comment;
        public String uid;
        public String name;
        public String header;
        public String top_comments_content;
        public String top_comments_voiceuri;
        public String top_comments_uid;
        public String top_comments_name;
        public String top_comments_header;
        public String passtime;
    }
}
