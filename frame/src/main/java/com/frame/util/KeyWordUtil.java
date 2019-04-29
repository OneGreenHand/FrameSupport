package com.frame.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字变色工具类
 */
public class KeyWordUtil {

    /**
     * 关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @return 结果SpannableString
     */
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        try {
            keyword = escapeExprSpecialWord(keyword);
            text = escapeExprSpecialWord(text);

            List<String> keywordTemp = Arrays.asList(keyword.split(""));
            int i = 0;
            Iterator<String> it = keywordTemp.iterator();
            while (it.hasNext()) {
                if (text.contains(it.next()) && !TextUtils.isEmpty(keyword)) {
                    i++;
                    Pattern p = Pattern.compile(keywordTemp.get(i));
                    Matcher m = p.matcher(s);
                    while (m.find()) {
                        int start = m.start();
                        int end = m.end();
                        s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        } catch (Exception e) {

        }
        return s;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return keyword
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}