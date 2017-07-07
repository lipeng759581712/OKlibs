package com.max.utils;


/**
 * Created by elwinxiao on 2015/12/9.
 */
public class FormatUtils {

    /**
     * 获取表示数量的字符串
     */
    public static String humanNumber(long number) {
        if (number < 10000) {
            return String.format("%d", number);
        } else if (number < 1000000) {
            if(number % 10000 <= 500) {
                return String.format("%.0f万", number / 10000.0f);
            }
            else {
                return String.format("%.1f万", number / 10000.0f);
            }
        } else if (number < 100000000) {
            return String.format("%.0f万", number / 10000.0f);
        } else {
            if(number % 100000000 <= 5000000) {
                return String.format("%.0f亿", number / 100000000.0f);
            }
            else {
                return String.format("%.1f亿", number / 100000000.0f);
            }
        }
    }

    /**
     * 将long类型数据转化为（0xXX）形式的字符串
     */
    public static String richNumber(int value) {
        return value + "(0x" + Integer.toHexString(value) + ")";
    }
    public static String richNumber(long value) {
        return value + "(0x" + Long.toHexString(value) + ")";
    }

    public static String formatDurationTime(long durationSec) {
        final int minInSec = 60;
        final int hourInSec = minInSec * 60;

        int hour = (int) (durationSec / hourInSec);
        durationSec %= hourInSec;
        int minute = (int) (durationSec / minInSec);
        durationSec = durationSec % minInSec;

        String timeString = "";
        if (hour > 0) {
            timeString = hour + ":";
        }

        timeString += String.format("%02d", minute);
        timeString += String.format(":%02d", durationSec);
        return timeString;
    }

}
