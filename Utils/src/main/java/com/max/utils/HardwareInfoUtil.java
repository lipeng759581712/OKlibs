package com.max.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

/**
 * 用来获取手机的硬件信息
 *
 * @author wiizhang
 */
public class HardwareInfoUtil {

    private static final String TAG = "HardwareInfoUtil";

    private static int sCoreNum = 0;

    private static long sCpuFrequence = 0;

    private static long sTotalMemo = 0;
    /*
     * 单个进程的对内存级别
     */
    public static final int MEMORY_LEVEL_HIGH = 0;
    public static final int MEMORY_LEVEL_NORMAL = 1;
    public static final int MEMORY_LEVEL_LOW = 2;

    /**
     * 获取cpu核心数
     *
     * @return
     */
    public static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        if (sCoreNum == 0) {
            try {
                // Get directory containing CPU info
                File dir = new File("/sys/devices/system/cpu/");
                // Filter to only list the devices we care about
                File[] files = dir.listFiles(new CpuFilter());
                // Return the number of cores (virtual CPU devices)
                sCoreNum = files == null ? 0 : files.length;
            } catch (Exception e) {
                Log.e(TAG, "getNumCores exception occured,e=", e);
                sCoreNum = 1;
            }
        }
        return sCoreNum;

    }

    /**
     * 获取cpu的核心频率
     *
     * @return
     */
    public static long getCpuFrequence() {
        if (sCpuFrequence == 0) {
            try {
                FileInputStream fileInputStream = new FileInputStream(new File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line = reader.readLine();
                sCpuFrequence = Long.parseLong(line);
//                ALog.i(TAG, "sCpuFrequence:" + sCpuFrequence);
            } catch (IOException e) {
                Log.e(TAG, "getCpuFrequence IOException occured,e=", e);
                sCpuFrequence = -1;
            } catch (ClassCastException e) {
                Log.e(TAG, "getCpuFrequence ClassCastException occured,e=", e);
                sCpuFrequence = -1;
            } catch (Exception e) {
                Log.e(TAG, "getCpuFrequence Exception occured,e=", e);
                sCpuFrequence = -1;
            }
        }
        return sCpuFrequence;
    }

    public static long getTotalMemory() {
        if (sTotalMemo == 0) {
            String str1 = "/proc/meminfo";// 系统内存信息文件
            String str2;
            String[] arrayOfString;
            long initial_memory = -1;
            FileReader localFileReader = null;
            try {
                localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

                if (str2 != null) {
                    arrayOfString = str2.split("\\s+");
                    //initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
                    final long sizeInKB = Long.parseLong(arrayOfString[1]);
                    initial_memory = sizeInKB * 1024;
//                    ALog.i(TAG, "initial_memory:" + initial_memory);
                }
                localBufferedReader.close();
            } catch (IOException e) {
                Log.e(TAG, "getTotalMemory Exception occured,e=", e);
            } finally {
                if (localFileReader != null) {
                    try {
                        localFileReader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "close localFileReader Exception occured,e=", e);
                    }
                }
            }
            sTotalMemo = initial_memory;
        }
        return sTotalMemo;
    }

    /**
     * 获取机型信息
     */
    public static String getPerformanceDetail(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append("此设备性能信息:");
        builder.append("CPU频率: ");
        builder.append(Formatter.formatFileSize(context, getCpuFrequence() * 1024));
        builder.append(" ,CPU核数: ");
        builder.append(getNumCores());
        builder.append(" ,总内存大小: ");
        builder.append(Formatter.formatFileSize(context, getTotalMemory()));
        return builder.toString();
    }

    public static long getAvailMemory(Context context) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return mi.availMem;
    }

    /**
     * 获取本机单个进程的可用内存级别
     *
     * @return
     */
    public static int getMemoryClassLevel(Context context) {
        int memorgsize = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cameraSizeLevel = MEMORY_LEVEL_HIGH;
        if (memorgsize < 36) {
            cameraSizeLevel = MEMORY_LEVEL_LOW;
        } else if (memorgsize < 42) {
            cameraSizeLevel = MEMORY_LEVEL_NORMAL;
        }
        return cameraSizeLevel;
    }

    /**
     * 获取当前进程CPU使用率
     */
    public static float getProcessCpuRate() {
        long totalCpuTime1 = getTotalCpuTime();
        long processCpuTime1 = getProcessCpuTime();
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            return 0;
        }

        long totalCpuTime2 = getTotalCpuTime();
        long processCpuTime2 = getProcessCpuTime();

        if (totalCpuTime2 - totalCpuTime1 == 0) {
            return 0;
        }

        return 100 * 1.0f * (processCpuTime2 - processCpuTime1)
                / (totalCpuTime2 - totalCpuTime1);
    }

    public static long getTotalCpuTime() {
        final String totalCpuPath = "/proc/stat";
        try {
            RandomAccessFile file = new RandomAccessFile(totalCpuPath, "r");
            String[] totalCPUInfo = file.readLine().split(" ");
            return Long.parseLong(totalCPUInfo[2]) + Long.parseLong(totalCPUInfo[3])
                    + Long.parseLong(totalCPUInfo[4]) + Long.parseLong(totalCPUInfo[5])
                    + Long.parseLong(totalCPUInfo[6]) + Long.parseLong(totalCPUInfo[7])
                    + Long.parseLong(totalCPUInfo[8]);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getProcessCpuTime() {
        final int pid = android.os.Process.myPid();
        final String processCpuPath = "/proc/" + pid + "/stat";

        try {
            RandomAccessFile file = new RandomAccessFile(processCpuPath, "r");
            String line = "";
            StringBuffer sb = new StringBuffer();
            sb.setLength(0);
            while ((line = file.readLine()) != null) {
                sb.append(line + "\n");
            }
            String[] processCPUInfo = sb.toString().split(" ");
            return Long.parseLong(processCPUInfo[13]) + Long.parseLong(processCPUInfo[14])
                    + Long.parseLong(processCPUInfo[15]) + Long.parseLong(processCPUInfo[16]);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
