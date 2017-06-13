
package com.max.androidutilsmodule;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * 有些逻辑并不需要单独使用一个HandlerThread，但又有一些临时的任务需要开启线程进行处理，此时就可以将任务丢到这个线程来处理
 * 
 * @author hugozhong
 */
public class CommonTaskThread extends HandlerThread {

    public CommonTaskThread(String name) {
        super(name, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        start();
    }

    private Handler handler;

    private static CommonTaskThread thread = new CommonTaskThread("CommonTaskThread");

    public static CommonTaskThread getInstance() {
        return thread;
    }

    /**
     * 将Runnable内部的代码丢到CommonTaskThread里执行
     * 
     * @param runnable
     */
    public synchronized void post(Runnable runnable) {
        if (handler == null) {
            handler = new Handler(getLooper());
        }
        handler.post(runnable);
    }

    public synchronized void postDelay(Runnable runnable, long delay) {
        if (handler == null) {
            handler = new Handler(getLooper());
        }
        handler.postDelayed(runnable, delay);
    }

}
