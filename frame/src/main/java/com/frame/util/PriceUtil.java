package com.frame.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * 价格相关转换工具
 */
public class PriceUtil {

    /**
     * 千位分隔符
     * digit 小数点后保留几位（价格相关慎用，会被四舍五入）
     */
    public static String qianWeiFenGe(double num, int digit) {
        if (digit < 0)
            return "小数位不能为空";
        StringBuffer sb = new StringBuffer();
        sb.append("#,##0.");
        for (int i = 0; i < digit; i++)
            sb.append("0");
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(num);
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
            num = subZeroAndDot(CalculUtil.div(price, bili + "", 2));
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
            distance = round2((float) ((m / 100) * 0.1), 1).toString() + "km";
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

    /**
     * 四舍五入精确到小数随你几位
     */
    public static BigDecimal round2(float aFloat, int digit) {
        return new BigDecimal(String.valueOf(aFloat)).setScale(digit, BigDecimal.ROUND_HALF_UP);
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