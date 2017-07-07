package com.max.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 显示工具类，提供了获取屏幕显示的各种指标（如高度、宽度等）、尺寸单位之间的转换方法
 */
public class DisplayUtils {
    /**
     * *******************************************************************************************************
     * ************************************* 新方法获取屏幕高宽度 开始 *****************************************
     * *******************************************************************************************************
     */

    /**
     * 获取APP应用区域
     * @param activity
     * @return
     */
    public static Rect getAppDisplayFrame(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect;
    }

    /**
     * 获取屏幕区域
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取设备屏幕的宽度（单位为px）
     */
    public static int getScreenWidthPx(Activity activity) {
        return getDisplayMetrics(activity).widthPixels;
    }

    /**
     * 获取设备屏幕的高度（单位为px）
     */
    public static int getScreenHeightPx(Activity activity) {
        return getDisplayMetrics(activity).heightPixels;
    }

    /**
     * 获取设备屏幕的宽度（单位为dp）
     */
    public static float getScreenWidthDp(Activity activity) {
        DisplayMetrics displayMetrics = getDisplayMetrics(activity);
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    /**
     * 获取设备屏幕的高度（单位为dp）
     */
    public static float getScreenHeightDp(Activity activity) {
        DisplayMetrics displayMetrics = getDisplayMetrics(activity);
        return displayMetrics.heightPixels / displayMetrics.density;
    }

    public static int getPhoneWidth(Activity activity) {
        if(isLandscape(activity)) {
            return getScreenHeightPx(activity);
        } else {
            return getScreenWidthPx(activity);
        }
    }

    /**
     * 获取设备屏幕的密度比例，如2.0
     */
    public static float getScreenDensity(Activity activity) {
        return getDisplayMetrics(activity).density;
    }

    public static boolean isLandscape(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration();
        int ori = mConfiguration.orientation;
        if(ori == mConfiguration.ORIENTATION_LANDSCAPE){
            return true;
        }else if(ori == mConfiguration.ORIENTATION_PORTRAIT){
            return false;
        }
        return false;
    }
    /**
     * *******************************************************************************************************
     * ************************************* 新方法获取屏幕高宽度 结束 *****************************************
     * *******************************************************************************************************
     * 下面的方法在魅族手机中出现宽度变高度的BUG！
     */


    /**
     * 获取设备屏幕的宽度（单位为px）
     */
    public static int getScreenWidthPx(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取设备屏幕的高度（单位为px）
     */
    public static int getScreenHeightPx(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 获取设备屏幕的宽度（单位为dp）
     */
    public static int getScreenWidthDp(Context context) {
        return (int) (getScreenWidthPx(context) / getScreenDensity(context));
    }

    /**
     * 获取设备屏幕的高度（单位为dp）
     */
    public static int getScreenHeightDp(Context context) {
        return (int) (getScreenHeightPx(context) / getScreenDensity(context));
    }

    /**
     * 获取设备屏幕的密度比例，如2.0
     */
    public static float getScreenDensity(Context context) {
        return getDisplayMetrics(context).density;
    }


    /**
     * 获取设备屏幕的伸缩密度
     */
    public static float getScreenScaleDensity(Context context) {
        return getDisplayMetrics(context).scaledDensity;
    }

    /**
     * 获取设备屏幕的密度大小，如320（对应屏幕密度比例为2.0）
     */
    public static int getScreenDensityDpi(Context context) {
        return getDisplayMetrics(context).densityDpi;
    }

    /**
     * 获取设备屏幕对角线尺寸, 3.0以上系统会减去StatusBar的高度
     */
    public static float getScreenSize(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        double diagonalPx = Math.sqrt(Math.pow(metrics.widthPixels, 2) + Math
                .pow(metrics.heightPixels, 2));
        float screenSize = (float) (diagonalPx / metrics.densityDpi);
        return screenSize;
    }

    /**
     * 获取设备屏幕的密度模式，如"ldpi"、"mdpi"、"hdpi"等
     */
    public static String getDensityMode(Context context) {
        int densityDpi = getScreenDensityDpi(context);
        switch (densityDpi) {
            case 120:
                return "ldpi";
            case 160:
                return "mdpi";
            case 240:
                return "hdpi";
            case 320:
                return "xdpi";
            case 480:
                return "xxdpi";
            case 640:
                return "xxxdpi";
            default:
                return "Unknown";
        }
    }

    /**
     * 获取屏幕的大小模式，如"xlarge"、"large"、"normal"等
     */
    public static String getScreenMode(Context context) {
        int screenWidthDp = getScreenWidthDp(context);
        int screenHeightDp = getScreenHeightDp(context);

        if (screenHeightDp >= 960 && screenWidthDp >= 720) {
            return "xlarge";
        } else if (screenHeightDp >= 640 && screenWidthDp >= 480) {
            return "large";
        } else if (screenHeightDp >= 470 && screenWidthDp >= 320) {
            return "normal";
        } else if (screenHeightDp >= 426 && screenWidthDp >= 320) {
            return "small";
        } else {
            return "unknown";
        }
    }

    /**
     * 获取当前显示指标
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取设备屏幕显示高度，不包括状态栏高度
     */
    public static int getRealScreenHeightPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        try {
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealHeight");
            return (Integer) method.invoke(display);
        } catch (Exception e) {
            e.printStackTrace();
            return getScreenHeightPx(context);
        }
    }

    /**
     * 根据设备分辨率从dp转成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = getScreenDensity(context);
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据设备分辨率从px转成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = getScreenDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据设备分辨率从dp转成px（float）
     */
    public static float dip2pxF(Context context, float dpValue) {
        final float scale = getScreenDensity(context);
        return (dpValue * scale + 0.5f);
    }

    /**
     * 根据设备分辨率从sp转成px（float）
     */
    public static float sp2pxF(Context context, float spValue) {
        final float scale = getScreenScaleDensity(context);
        return (spValue * scale + 0.5f);
    }

    /**
     * 640p下的标注转720p
     *
     * @param sourceSizePx 640p下的标注值（px）
     * @return 720p下的尺寸（dp）
     */
    public static float sizeConvert(float sourceSizePx) {
        final float base = 1.77777f;
        return sourceSizePx / base;
    }


    /**
     *获取还未绘制View的size
     * @param view
     * @return
     */
    public static Point getViewSize(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width,height);
        return new Point(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    /**
     * 获得状态栏的高度
     * @param context
     * @return
     * by Hankkin at:2015-10-07 21:16:43
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
