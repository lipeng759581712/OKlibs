package com.max.thread;

import android.os.Handler;
import android.os.Looper;

/**
 * 单例
 */
public class MainLooperHandler extends Handler {
    private static MainLooperHandler instance = new MainLooperHandler();
    public static MainLooperHandler getInstance() {
        return instance;
    }

    private MainLooperHandler() {
        super(Looper.getMainLooper());
    }

    public void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public void runOnUiThread(Runnable runnable, long delayMillis) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            postDelayed(runnable, delayMillis);
        }
    }

}
