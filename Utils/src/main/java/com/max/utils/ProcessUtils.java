package com.max.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ProcessUtils {

    private static volatile String sProcessName;
    public static final String UNKOWN_PROCESS = "unknow";

    public static String currentProcessName(Context context) {
        if (sProcessName != null) {
            return sProcessName;
        }
        synchronized (ProcessUtils.class) {
            if (sProcessName != null) {
                return sProcessName;
            }
            return sProcessName = obtainProcessName(context);
        }
    }

    public static boolean isMainProcess(Context context) {
        final String processName = currentProcessName(context);
        return processName != null && processName.equals(context.getApplicationInfo().processName);
    }

    private static String obtainProcessName(Context context) {
        final int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> listTaskInfo = am.getRunningAppProcesses();
        if (listTaskInfo != null && listTaskInfo.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo proc : listTaskInfo) {
                if (proc != null && proc.pid == pid) {
                    return proc.processName;
                }
            }
        }
        return UNKOWN_PROCESS;
    }

    private ProcessUtils() {}
}
