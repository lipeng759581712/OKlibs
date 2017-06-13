package com.max.androidutilsmodule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by elwinxiao on 2015/4/18.
 */
public class TimeUtils {
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static boolean isSameDayOfMillis(long ms1, long ms2) {
        return compareByDay(ms1, ms2) == 0;
    }

    public static long compareByDay(long ms1, long ms2) {
        long day1 = toDay(ms1);
        long day2 = toDay(ms2);
        return day1 - day2;
    }

    public static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
//        return millis / MILLIS_IN_DAY;
    }

    public static String easyReadTime(long timestampMillsec, boolean withTime) {
        long delta = System.currentTimeMillis() - timestampMillsec;
        final long second = 1000; // a second
        final long minute = second * 60; // a minute
        final long hour = minute * 60; // an hour
        final long day = hour * 24; // a day
        final long week = day * 7; // a week

        if (delta < 0) {
            String easyReadTime = chinaFormatTime(timestampMillsec, withTime);
            return easyReadTime;
        } else if (delta < minute) {
            return "刚刚";
        } else if (delta < hour) {
            return (delta / minute) + "分钟前";
        } else if (delta < day) {
            return (delta / hour) + "小时前";
        } else if (delta < week) {
            return (delta / day) + "天前";
        } else {
            String easyReadTime = chinaFormatTime(timestampMillsec, withTime);
            return easyReadTime;
        }
    }

    public static String chinaFormatTime(long timestampMillsec, boolean withTime) {
        String formatString = "yyyy-MM-dd";
        if (withTime) {
            formatString += " HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatString, Locale.ROOT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String easyReadTime = dateFormat.format(new Date(timestampMillsec));
        return easyReadTime;
    }

    /**
     * 毫秒转换为*分*秒
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {

        int minutes = 0;
        int seconds = 0;
        seconds = (int)mss / 1000;
        if (seconds < 60){
            return seconds + "秒";
        }else {
            minutes = seconds / 60;
            seconds = seconds % 60;
        }

        return minutes + "分"+ seconds + "秒";
    }



}
