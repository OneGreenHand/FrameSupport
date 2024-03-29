package com.ogh.support.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.util.regex.Pattern;


/**
 * 数字相关转换工具
 */
public class PriceUtil {

    /**
     * 得到转化后的数值
     * 性能较好,精度低
     * digit 小数点后保留几位（四舍五入）
     */
    public static String getPerformanceNumber(double num, int digit) {
        if (digit < 0)
            return "0";
        return String.format("%." + digit + "f", num);
    }

    /**
     * 得到转化后的数值
     * 性能较差,精度高
     * digit 小数点后保留几位（价格为正:数值不变，价格为负:数值变小）
     */
    public static double getAccurateNumber(String num, int digit) {
        if (digit < 0)
            return 0;
        return new BigDecimal(num).setScale(digit, BigDecimal.ROUND_FLOOR).doubleValue();
    }

    /**
     * 四舍五入计算
     * 性能较差
     * digit 小数点后保留几位
     */
    public static double rounding(String num, int digit) {
        return new BigDecimal(num).setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 千位分隔符 10000会变成10,000
     * 小数点后的数字不会参与分割
     * digit 每隔几位分割
     */
    public static String QianWeiFenGe(String num, int digit) {
        if (TextUtils.isEmpty(num))
            return "0";
        String decimal = "";
        if (num.contains(".")) {//有小数点
            decimal = num.substring(num.indexOf("."));//小数点后的数字
            num = num.substring(0, num.indexOf("."));//小数点前的数字
        }
        try {
            // ① 把串倒过来
            StringBuffer tmp = new StringBuffer().append(num).reverse();
            // ② 替换这样的串：连续split位数字的串，其右边还有个数字，在串的右边添加逗号
            String retNum = Pattern.compile("(\\d{" + 3 + "})(?=\\d)").matcher(tmp.toString()).replaceAll("$1,");
            // ③ 替换完后，再把串倒回去返回
            String resultNum = new StringBuffer().append(retNum).reverse().toString();
            return !TextUtils.isEmpty(decimal) ? subZeroAndDot(resultNum + decimal) : resultNum;
        } catch (Exception e) {
            e.printStackTrace();
            return !TextUtils.isEmpty(decimal) ? subZeroAndDot(num + decimal) : num;
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     */
    public static String subZeroAndDot(String s) {
        if (TextUtils.isEmpty(s) || s.trim().isEmpty()) {
            return "0";
        } else if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static Spannable setPriceSp(String text1, String text2, int font, int color1, int color2) {//双色同字号
        Spannable sp = new SpannableString(text1 + text2);
        sp.setSpan(new AbsoluteSizeSpan(font, true), 0, text1.length() + text2.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color1), 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color2), text1.length(), text1.length() + text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static Spannable setPriceSp2(String text1, String text2, int font1, int font2, int color) {//单色不同字号
        Spannable sp = new SpannableString(text1 + text2);
        sp.setSpan(new AbsoluteSizeSpan(font1, true), 0, text1.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(font2, true), text1.length(), text1.length() + text2.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color), 0, text1.length() + text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static Spannable setPriceSp(String text1, String text2, int font1, int font2, int color1, int color2) {//双色不同字号
        Spannable sp = new SpannableString(text1 + text2);
        sp.setSpan(new AbsoluteSizeSpan(font1, true), 0, text1.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(font2, true), text1.length(), text1.length() + text2.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color1), 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color2), text1.length(), text1.length() + text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }
}