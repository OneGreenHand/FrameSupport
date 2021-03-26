package com.ogh.support.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;


/**
 * 价格相关转换工具
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
    public static double getAccurateNumber(double num, int digit) {
        if (digit < 0)
            return 0;
        return new BigDecimal(num).setScale(digit, BigDecimal.ROUND_FLOOR).doubleValue();
    }

    /**
     * 四舍五入计算
     * 性能较差
     * digit 小数点后保留几位
     */
    public static double rounding(double num, int digit) {
        return new BigDecimal(num).setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 千位分隔符 10000会变成10,000.00
     * digit 每隔几位分割
     */
    public static String QianWeiFenGe(double num, int digit) {
        // ① 把串倒过来
        StringBuffer tmp = new StringBuffer().append(num).reverse();
        // ② 替换这样的串：连续split位数字的串，其右边还有个数字，在串的右边添加逗号
        String retNum = Pattern.compile("(\\d{" + digit + "})(?=\\d)").matcher(tmp.toString()).replaceAll("$1,");
        // ③ 替换完后，再把串倒回去返回
        return new StringBuffer().append(retNum).reverse().toString();
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

    public static String subZeroAndDot(double s) {
        String money = String.valueOf(s);
        if (TextUtils.isEmpty(money) || money.trim().isEmpty()) {
            return "0";
        } else if (money.indexOf(".") > 0) {
            money = money.replaceAll("0+?$", "");//去掉多余的0
            money = money.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return money;
    }

    /**
     * 比例转换，例如100:1
     */
    public static String priceConversion(String price, int bili) {
        String num = "";
        if (TextUtils.isEmpty(price) || price.trim().isEmpty())
            return "0";
        else
            num = subZeroAndDot(CalculUtil.div(Double.parseDouble(price), bili , 2));
        return num;
    }

    /**
     * 只保留一位小数，不四舍五入
     */
    public static String splitOnePoint(String s, int pointCount) {
        if (TextUtils.isEmpty(s) || s.trim().isEmpty()) {
            s = "0.0";
        } else {
            if (s.contains(".")) {
                if (s.split("\\.")[1].length() > pointCount) {
                    s = s.split("\\.")[0] + "." + s.split("\\.")[1].substring(0, pointCount);
                    s = subZeroAndDot(s);
                }
            }
        }
        return s;
    }

    // m 转化为 km(四舍五入，保留小数点一位)
    public static String KmConversion(int m) {
        String distance = m + "";
        if (distance.length() <= 3)
            distance = m + "m";
        else
            distance = rounding(((m / 100) * 0.1), 1) + "km";
        return distance;
    }

    // 值转化为W(不四舍五入保留两位小数,最多99亿)
    public static String PopularityConversion(int popularity) {
        String distance = popularity + "";
        if (distance.length() <= 4)
            distance = popularity + "";
        else {
            if (popularity % 10000 == 0) {//整数
                distance = popularity / 10000 + "w";
            } else {
                distance = subZeroAndDot(splitOnePoint((((float) popularity / 1000) * 0.1) + "", 2)) + "w";//这里要去除多余的0
            }
        }
        return distance;
    }

    public static Spannable setLeftPriceSp(String company, int font1, int font2, int color, String text) {//￥99(单色)
        Spannable sp = new SpannableString(company + text);
        sp.setSpan(new AbsoluteSizeSpan(font1, true), 0, company.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(font2, true), company.length(), company.length() + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color), 0, company.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static Spannable setLeftPriceSp(String company, int font1, int font2, int color1, int color2, String text) {//￥99(双色)
        Spannable sp = new SpannableString(company + text);
        sp.setSpan(new AbsoluteSizeSpan(font1, true), 0, company.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(font2, true), company.length(), company.length() + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color1), 0, company.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color2), company.length(), company.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static Spannable setRightPriceSp(String text, String company, int font1, int font2, int color) {//99金币(单色)
        Spannable sp = new SpannableString(text + company);
        sp.setSpan(new AbsoluteSizeSpan(font1, true), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(font2, true), text.length(), company.length() + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color), 0, company.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static Spannable setRightPriceSp(String text, int font1, int font2, int color1, int color2, String company) {//99金币(双色)
        Spannable sp = new SpannableString(text + company);
        sp.setSpan(new AbsoluteSizeSpan(font1, true), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(font2, true), text.length(), company.length() + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color1), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color2), text.length(), company.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }
}