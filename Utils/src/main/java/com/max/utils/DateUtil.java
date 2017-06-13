package com.max.androidutilsmodule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * 日期工具类
 *
 * @author hugozhong
 * @version 1.0
 * @date 2013-9-27
 */
public class DateUtil {

    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;

    private static StringBuilder sFormatBuilder = new StringBuilder();
    private static Formatter sFormatter = new Formatter(sFormatBuilder,
            Locale.getDefault());

    /**
     * <pre>
     * 规则：
     * 1.一个小时以内的，以当前时间，减去真实发表时间=37分钟前，45分钟前，51分钟前
     * 2.超过1小时
     * 小于24小时的，以当前时间，减去真实发表时间=2小时前，5小时前，16小时前
     * 3.超过24小时，小于48小时的，统一显示=昨天
     * 4超过48小时的，直接显示发表的日期=8月18日
     * </pre>
     *
     * @param timemillis
     */
    public static String getFeedDisplay(long timemillis) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
        long timemillisInterval = System.currentTimeMillis() - timemillis;
        if (timemillisInterval < 0) {
            return sDateFormat.format(new Date(timemillis));
        } else if (timemillisInterval < ONE_MINUTE) {
            return "刚刚";
        } else if (timemillisInterval < ONE_HOUR) {
            return (timemillisInterval / 60000) + "分钟前";
        } else if (timemillisInterval < ONE_DAY) {
            return (timemillisInterval / 3600000) + "小时前";
        } else {
            Calendar dayBeforeYesterday = Calendar.getInstance();
            dayBeforeYesterday.add(Calendar.DAY_OF_YEAR, -2); // 前天
            dayBeforeYesterday.set(Calendar.HOUR_OF_DAY, 0);
            dayBeforeYesterday.set(Calendar.MINUTE, 0);
            dayBeforeYesterday.set(Calendar.SECOND, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timemillis); // feed时间
            if (calendar.before(dayBeforeYesterday)) {// 前天之前的Feed时间
                return sDateFormat.format(new Date(timemillis));
            } else {
                return "昨天";
            }
        }

    }

    public static int getDayInterval(Calendar calendar, Calendar anotherCalendar) {

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        long timeInMillis = calendar.getTimeInMillis();
        long anotherTimeInMillis = anotherCalendar.getTimeInMillis();

        if (timeInMillis < anotherTimeInMillis) {
            beginCalendar.setTimeInMillis(timeInMillis);
            endCalendar.setTimeInMillis(anotherTimeInMillis);

        } else {
            beginCalendar.setTimeInMillis(anotherTimeInMillis);
            endCalendar.setTimeInMillis(timeInMillis);
        }

        int day = 0;
        while (beginCalendar.before(endCalendar)) {
            // 如果开始日期和结束日期在同年、当前年的同一天时结束循环
            if (beginCalendar.get(Calendar.YEAR) == endCalendar
                    .get(Calendar.YEAR)
                    && beginCalendar.get(Calendar.DAY_OF_YEAR) == endCalendar
                    .get(Calendar.DAY_OF_YEAR)) {
                break;
            } else {
                beginCalendar.add(Calendar.DAY_OF_YEAR, 1);
                day++;
            }
        }

        return day;
    }

    /**
     * 判断两个时间戳是否同一天
     */
    public static boolean isSameDay(long time, long anotherTime) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);

        Calendar last = Calendar.getInstance();
        last.setTimeInMillis(anotherTime);

        if ((current.get(Calendar.YEAR) == last.get(Calendar.YEAR))
                && (current.get(Calendar.DAY_OF_YEAR) == last
                .get(Calendar.DAY_OF_YEAR))) {
            return true;
        }

        return false;
    }

    /**
     * 判断两个时间戳是否同一年
     */
    public static boolean isSameYear(long time, long anotherTime) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);

        Calendar last = Calendar.getInstance();
        last.setTimeInMillis(anotherTime);

        if (current.get(Calendar.YEAR) == last.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    public static String getYear(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    /**
     * 获取时间 分;秒 mm:ss
     *
     * @return
     */
    public static String getTimeShort(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String dateString = formatter.format(time);
        return dateString;
    }

    /**
     * 获取时间 时;分;秒 hh:mm
     *
     * @return
     */
    public static String getTimeHM(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(time * 1000L);
        return dateString;
    }

    public static String getDateShort(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getDateFull(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd       HH:mm");
        String dateString = formatter.format(time * 1000L);
        return dateString;
    }

    public static String getDate(long time) {
        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数  
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天
        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(time * 1000L);
        if (dateString == null) {
            return "未知";
        }
        try {
            Date date = formatter.parse(dateString);
            current.setTime(date);
            if (current.after(today)) {
                return "今天" + getTimeHM(time);
            } else if (current.before(today) && current.after(yesterday)) {
                return "昨天" + getTimeHM(time);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    /**
     * 视频时间显示格式 00：00：00
     *
     * @param duration
     * @return
     */
    public static String getVideoDuration(int duration) {

        if (duration <= 0) {
            return "未知";
        }

        int seconds = duration % 60;
        int minutes = (duration / 60) % 60;
        int hours = duration / 3600;

        sFormatBuilder.setLength(0);
        return sFormatter.format("%02d:%02d:%02d", hours, minutes, seconds)
                .toString();

    }

    /**
     * 视频时间显示格式 00：00
     *
     * @param duration
     * @return
     */
    public static String getVideoRecordDuration(int duration, int n) {

        if (duration < 0) {
            return "未知";
        }

        int seconds = duration % 60;
        int minutes = (duration / 60) % 60;
        int hours = duration / 3600;
        sFormatBuilder.setLength(0);
        if (n == 2) {
            return sFormatter.format("%02d:%02d", minutes, seconds)
                    .toString();
        } else if (n == 3) {
            return sFormatter.format("%02d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return "未知";
        }

    }

    public static String getVideoRecordDuration(int duration) {

        if (duration < 0) {
            return "未知";
        }
        int hours = duration / 3600;
        if (hours > 0) {
            return getVideoRecordDuration(duration, 3);
        } else {
            return getVideoRecordDuration(duration, 2);
        }
    }


}
