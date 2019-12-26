package com.frame.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * description: 所有关于时间计算的工具类
 */
public class DateUtil {

    /**
     * 获取当前系统时间
     */
    public static String getDate(String pattern) {
        return new SimpleDateFormat(TextUtils.isEmpty(pattern) ? "yyyy-MM-dd HH:mm:ss" : pattern).format(Calendar.getInstance().getTime());
    }

    /**
     * 设置时间
     */
    public static Calendar setCalendar(int year, int month, int date) {
        Calendar cl = Calendar.getInstance();
        cl.set(year, month - 1, date);
        return cl;
    }

    /**
     * 获取当前时间的前一天时间
     */
    public static Calendar getBeforeDay(Calendar cl) {
        int day = cl.get(Calendar.DATE);  //使用set方法直接进行设置
        cl.set(Calendar.DATE, day - 1);
        return cl;
    }

    /**
     * 获取当前时间的后一天时间
     */
    public static Calendar getAfterDay(Calendar cl) {
        int day = cl.get(Calendar.DATE);  //使用set方法直接设置时间值
        cl.set(Calendar.DATE, day + 1);
        return cl;
    }

    /**
     * 获取当前时间的后30天时间
     */
    public static Calendar getAfter30Day(Calendar cl) {
        int day = cl.get(Calendar.DATE);  //使用set方法直接设置时间值
        cl.set(Calendar.DATE, day + 30);
        return cl;
    }

    /**
     * 得到几天前的时间
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

}