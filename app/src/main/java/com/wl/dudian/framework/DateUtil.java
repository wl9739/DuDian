package com.wl.dudian.framework;

import java.util.Calendar;

/**
 *
 * @author zfeiyu
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
}
