package com.max.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by elwinxiao on 2015/12/12.
 */
public class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static void launchActivity(Context context, Class<?> activityClass, Bundle bundle) {
        launchActivity(context, activityClass, 0, bundle);
    }

    public static void launchActivity(Context context, Class<?> activityClass, int flags, Bundle bundle) {
        if (!(context instanceof Activity)) {
            flags |= Intent.FLAG_ACTIVITY_NEW_TASK;
        }

        Intent intent = new Intent(context, activityClass);
        intent.addFlags(flags);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }


    /**
     * 启动Activity
     *
     * @param activity activity
     * @param cls      activity类
     */
    public static void startActivity(final Context activity,
                                     final Class<?> cls) {
        startActivity(activity, null, activity.getPackageName(), cls.getName(), null);
    }


    /**
     * 启动Activity
     *
     * @param activity  activity
     * @param cls       activity类
     * @param enterAnim 入场动画
     * @param exitAnim  出场动画
     */
    public static void startActivity( final Activity activity,
                                      final Class<?> cls,
                                      final int enterAnim,
                                      final int exitAnim) {
        startActivity(activity, null, activity.getPackageName(), cls.getName(), null);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 启动Activity
     *
     * @param extras    extras
     * @param activity  activity
     * @param cls       activity类
     * @param enterAnim 入场动画
     * @param exitAnim  出场动画
     */
    public static void startActivity( final Activity activity,
                                      final Bundle extras,
                                      final Class<?> cls,
                                      final int enterAnim,
                                      final int exitAnim) {
        startActivity(activity, extras, activity.getPackageName(), cls.getName(), null);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 启动Activity
     * @param context
     * @param extras
     * @param cls
     */
    public static void startActivity( final Context context,
                                      final Bundle extras,
                                      final Class<?> cls) {
        startActivity(context, extras, context.getPackageName(), cls.getName(), null);
    }

    /**
     * 启动Activity
     *
     * @param extras   extras
     * @param context  context
     * @param cls      activity类
     * @param options  跳转动画
     */
    public static void startActivity( final Context context,
                                      final Bundle extras,
                                      final Class<?> cls,
                                      final Bundle options) {
        startActivity(context, extras, context.getPackageName(), cls.getName(), options);
    }


    /**
     * 启动Activity
     * @param context
     * @param extras
     * @param pkg
     * @param cls
     * @param options
     */
    private static void startActivity(final Context context,
                                      final Bundle extras,
                                      final String pkg,
                                      final String cls,
                                      final Bundle options) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) intent.putExtras(extras);
        intent.setComponent(new ComponentName(pkg, cls));
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (options == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent);
        } else {
            context.startActivity(intent, options);
        }
    }


    /**
     * 判断是否存在Activity
     *
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isActivityExists(Context context,final String packageName, final String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }



    /**
     * 获取设备当前运行的程序的调用栈的顶部Activity
     */
    public static ActivityManager.RunningTaskInfo getTasksTopActivity(Context ctx) {
        ActivityManager mActivityManager = ((ActivityManager) ctx.getSystemService(
                Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = mActivityManager.getRunningTasks(1);
        if(runningTaskInfo != null && runningTaskInfo.size() > 0) {
            return runningTaskInfo.get(0);
        }
        return null;
    }

    /**
     * 获取栈顶Activity
     *
     * @return 栈顶Activity
     */
    public static Activity getTopActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                activities = (HashMap) activitiesField.get(activityThread);
            } else {
                activities = (ArrayMap) activitiesField.get(activityThread);
            }
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取launcher activity
     *
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(Context context,final String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo aInfo : info) {
            if (aInfo.activityInfo.packageName.equals(packageName)) {
                return aInfo.activityInfo.name;
            }
        }
        return "no " + packageName;
    }



}
