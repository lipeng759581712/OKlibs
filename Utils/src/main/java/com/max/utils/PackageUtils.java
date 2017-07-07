package com.max.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elwinxiao on 2015/12/15.
 */
public class PackageUtils {

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<PackageInfo> getAllAppInfos(Context context){
        if (context == null){
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> apps = packageManager.getInstalledPackages(0);
        return apps;
    }

    public static List<PackageInfo> getNoSystemAppInfos(Context context){
        if (context == null){
            return null;
        }
        List<PackageInfo> allApps = getAllAppInfos(context);
        List<PackageInfo> noSystemApps = new ArrayList<PackageInfo>();
        for (PackageInfo app : allApps){
            if (app != null && (app.applicationInfo.flags & app.applicationInfo.FLAG_SYSTEM)<=0){
                noSystemApps.add(app);
            }
        }
        return noSystemApps;
    }
}
