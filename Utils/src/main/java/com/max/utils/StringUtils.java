package com.max.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by elwinxiao on 2015/12/9.
 */
public class StringUtils {

    /**
     * 获取String对象UTF-8编码的字节数组
     */
    public static byte[] getUtf8Code(String str) {
        try {
            byte[] keyBytes = str.getBytes("UTF-8");
            return keyBytes;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupport utf-8 encoding", e);
        }
    }

    public static long stringToLong(String str) {
        long ret = 0;
        try {
            ret = Long.parseLong(str);
        } catch (NumberFormatException e) {
            Log.e("StringUtils", e.toString());
        }
        return ret;
    }

    public static boolean isValidLong(String str) {
        try {
            long result = Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断一个字符串是否为空（str为null或者长度为0，都会被认为是空）
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty() && !str.trim().isEmpty();
    }
}
