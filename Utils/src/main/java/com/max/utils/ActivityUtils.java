package com.max.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

/**
 * Created by elwinxiao on 2015/12/12.
 */
public class ActivityUtils {

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
}
