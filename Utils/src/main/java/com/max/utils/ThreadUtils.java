package com.max.utils;


import android.os.Handler;
import android.os.Looper;

/**
 * Created by elwinxiao on 2015/12/12.
 */
public class ThreadUtils {

    static public void runOnMainThread (Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    static public void runDelayedOnMainThread(Runnable runnable, long delayMillis){
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delayMillis);
    }
}
