package com.frame.support.bean;

import com.frame.bean.BaseBean;

import java.util.List;

/**
 * @describe 段子实体类
 */
public class DuanZiBean extends BaseBean {


    /**
     * message : 成功!
     * result : [{"sid":"29915476","text":"公牛和母牛前阵子吵架分手了，分手后母牛和大象好上了，最近母牛觉得还是公牛对自己好，所以回到了公牛的身边，一番激情后，母牛躺着公牛身边问:你觉得我有什么改变？公牛想了想说:你牛逼大了。","type":"text","thumbnail":null,"video":null,"images":null,"up":"146","down":"11","forward":"0","comment":"7","uid":"23179427","name":"百思用户23179427","header":"http://wimg.spriteapp.cn/profile","top_comments_content":"我像只鱼儿在你的荷塘","top_comments_voiceuri":"","top_comments_uid":"23178669","top_comments_name":"ghy","top_comments_header":"http://thirdwx.qlogo.cn/mmopen/vi_32/kGtQqrbXrBCIyH2KQ13tokNGQj8tNkxMOeezx5MzHicxXM2eVLy92DeyzkTmONQlhTEicNYD5NKFFJDicJHFvOxFw/132","passtime":"2019-11-09 13:52:01"},{"sid":"29916844","text":"A：干IT太苦了，数据是越存越多，预算是越来越少，好基友是越来越多，女朋友是越来越少。想换一行怎么办？B：敲一下回车。","type":"text","thumbnail":null,"video":null,"images":null,"up":"107","down":"9","forward":"0","comment":"4","uid":"17264596","name":"百撕bu得骑姐","header":"http://wimg.spriteapp.cn/profile/large/2016/01/26/56a7823fbac90_mini.jpg","top_comments_content":null,"top_comments_voiceuri":null,"top_comments_uid":null,"top_comments_name":null,"top_comments_header":null,"passtime":"2019-11-09 12:52:02"}]
     */

    public String message;
    public List<ResultBean> result;

    @Override
    public boolean isEmpty() {
        return result == null || result.isEmpty();
    }

    public static class ResultBean {
        /**
         * sid : 29915476
         * text : 公牛和母牛前阵子吵架分手了，分手后母牛和大象好上了，最近母牛觉得还是公牛对自己好，所以回到了公牛的身边，一番激情后，母牛躺着公牛身边问:你觉得我有什么改变？公牛想了想说:你牛逼大了。
         * type : text
         * thumbnail : null
         * video : null
         * images : null
         * up : 146
         * down : 11
         * forward : 0
         * comment : 7
         * uid : 23179427
         * name : 百思用户23179427
         * header : http://wimg.spriteapp.cn/profile
         * top_comments_content : 我像只鱼儿在你的荷塘
         * top_comments_voiceuri :
         * top_comments_uid : 23178669
         * top_comments_name : ghy
         * top_comments_header : http://thirdwx.qlogo.cn/mmopen/vi_32/kGtQqrbXrBCIyH2KQ13tokNGQj8tNkxMOeezx5MzHicxXM2eVLy92DeyzkTmONQlhTEicNYD5NKFFJDicJHFvOxFw/132
         * passtime : 2019-11-09 13:52:01
         */
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
