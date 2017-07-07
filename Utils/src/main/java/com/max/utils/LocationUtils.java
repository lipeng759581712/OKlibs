package com.max.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * 手机定位相关的工具类
 *
 * Created by maxpengli on 2017/7/7.
 */

public class LocationUtils {
    /**
     * 判断GPS是否打开
     * ACCESS_FINE_LOCATION权限
     *
     * @param context
     * @return
     */
    public static boolean isGPSEnabled(Context context) {
        //获取手机所有连接LOCATION_SERVICE对象
        LocationManager locationManager = ((LocationManager) context.getSystemService(Context
                .LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
