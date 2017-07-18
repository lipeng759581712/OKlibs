package com.max.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * 设备工具，提供了获取设备屏幕显示的各种指标（如高度、宽度等）、尺寸单位之间的转换以获取设备版本型号等方法
 */
public class DeviceUtils {



    /**
     * 获取设备版本
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备型号
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }


    /**
     * 查询设备IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephoneManager.getDeviceId();
    }

}
