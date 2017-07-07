package com.max.log;

import android.util.Log;

/**
 * Created by maxpengli on 4/27/16.
 * 由于从其它地方引入了代码，使用的是LogUtil，统一转成ALog的实现
 * 除录制SDK及插件外，其他的都使用ALog.
 */
public class LogUtil {

    public static void v(String tag, String msg) {
        ALog.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        ALog.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        ALog.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        ALog.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        ALog.e(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        ALog.v(tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void d(String tag, String msg, Throwable tr) {
        ALog.d(tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void i(String tag, String msg, Throwable tr) {
        ALog.i(tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void w(String tag, String msg, Throwable tr) {
        ALog.w(tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void e(String tag, String msg, Throwable tr) {
        ALog.e(tag, msg + '\n' + getStackTraceString(tr));
    }

    private static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }
}
