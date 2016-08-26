package com.wl.dudian.framework;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Qiushui
 * @since 0.0.2
 */
public class DateUtil {

    /**
     * 返回当前日期的上一天
     *
     * @param nowDate
     * @return
     */
    public static String getLastDay(String nowDate) {
        if (nowDate == null || TextUtils.isEmpty(nowDate)) {
            return "";
        }
        int theYear = Integer.parseInt(nowDate.substring(0, 4));
        int theMonth = Integer.parseInt(nowDate.substring(4, 6)) - 1;
        int theDay = Integer.parseInt(nowDate.substring(6, 8));
        Calendar calendar = Calendar.getInstance();
        calendar.set(theYear, theMonth, theDay);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day - 1);

        String _year = String.valueOf(calendar.get(Calendar.YEAR));
        String _month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if ((_month).length() < 2) {
            _month = "0" + _month;
        }
        String _day = String.valueOf(calendar.get(Calendar.DATE));
        if (_day.length() < 2) {
            _day = "0" + _day;
        }

        return _year + _month + _day;
    }

    /**
     * 根据当前日期字符串,返回星期.
     *
     * @param nowDate
     * @return
     */
    public static String getWeekday(String nowDate) {
        if (nowDate.length() != 8) {
            throw new IllegalStateException("Illegal date");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(nowDate.substring(0, 4)), Integer.parseInt(nowDate.substring(4, 6)) - 1, Integer.parseInt(nowDate.substring(6, 8)));
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 获取完整的日期字符串(07月01日  星期五)
     *
     * @param nowDate
     * @return
     */
    public static String getFullDateFormart(String nowDate) {
        return nowDate.substring(4, 6) + "月" + nowDate.substring(6, 8) + "日  " + DateUtil.getWeekday(nowDate);
    }

    /**
     * 获取评论时间
     * @param millionSecond
     * @return
     */
    public static String getDate(long millionSecond) {

        Date date = new Date(millionSecond);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        return simpleDateFormat.format(date);
    }
}
