package com.max.utils;

/**
 * 时间监测工具，提供了统计持续时间的方法
 *
 * @author Created by fclarke on 9/1/15.
 */
public class TimeWatch {

    private long mStartTime;

    public TimeWatch (){
        reset();
    }

    /**
     * 记录开始时间点
     */
    public void reset() {
        mStartTime = System.currentTimeMillis();
    }

    /**
     * 获取持续时间
     */
    public long stop() {
        return System.currentTimeMillis() - mStartTime;
    }
}
