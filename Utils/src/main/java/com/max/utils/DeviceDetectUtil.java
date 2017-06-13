package com.max.androidutilsmodule;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 四核2G内存，1280x720以上的分辨率,GPU型号为PowerVR时，版本大于等于30
 * Created by jikylin on 2015/9/22.
 */
public class DeviceDetectUtil {
    private static final String TAG = "DeviceDetectUtil";
    private static long sTotalMemo = 0;
    public final static long RESOLUTION_LONG = 960;
    public final static long FIT_MEMORY = 1500;

    public static String[] FREQ_BASE_PATH = {"/sys/class/kgsl","/sys/devices/platform/galcore/gpu/gpu0/gpufreq",};
    public static String[] FREQ_MAX_PATH = {"/kgsl-3d0/max_gpuclk","/kgsl-3d0/devfreq/max_freq","/scaling_max_freq"};

    public static long getTotalMemory() {
        if (sTotalMemo == 0) {
            String str1 = "/proc/meminfo";// 系统内存信息文件
            String str2;
            String[] arrayOfString;
            long initial_memory = -1;
            InputStreamReader localFileReader = null;
            try {
                localFileReader = new InputStreamReader(new FileInputStream(str1), "UTF-8");
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

                if (str2 != null) {
                    arrayOfString = str2.split("\\s+");
                    initial_memory = Integer.valueOf(arrayOfString[1]).intValue()/1024;// 获得系统总内存，单位是KB
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
            sTotalMemo = initial_memory;// KB转换为MB或者G，内存大小规格化
        }
        return sTotalMemo;
    }

    public static int getResolutionLongSize(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels>dm.heightPixels?dm.widthPixels:dm.heightPixels;
    }



    /**
     * 1280x720以上的分辨率
     * @param context
     * @return
     */
    public static boolean isResolutionFit(Context context){
        boolean isFitResolution = getResolutionLongSize(context)-RESOLUTION_LONG>=0;
        Log.i(TAG, "isFitResolution:" + isFitResolution);
        return isFitResolution;
    }

    public static boolean isDeviceEnable(Context context){
        return isResolutionFit(context);
    }

    /**
     * 获取CPU信息
     * @return
     */
    public static String cpu_info;
    public static String getCpuInfo() {
        if (!TextUtils.isEmpty(cpu_info)) {
            return cpu_info;
        }else{
            return "";
        }


//      TODO:暂时关闭
        /*if (!TextUtils.isEmpty(cpu_info)) {
            return cpu_info;
        }

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String cpuInfo = "";
        String cpuFile = "/proc/cpuinfo";

        try {
            fileReader = new FileReader(cpuFile);
            bufferedReader = new BufferedReader(fileReader);
            while ((cpuInfo = bufferedReader.readLine()) != null) {
                if (cpuInfo.contains("Hardware")) {
                    cpu_info =  cpuInfo.split(":")[1].trim();
                    return cpu_info;
                }
            }
        } catch (FileNotFoundException e) {
            LogUtil.e(TAG, "getCpuInfo FileNotFoundException e:" + e);
        } catch (NullPointerException e) {
            LogUtil.e(TAG, "getCpuInfo NullPointerException e:" + e);
        } catch (IOException e) {
            LogUtil.e(TAG, "getCpuInfo readLine IOException e:" + e);
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }

                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                LogUtil.e(TAG, "getCpuInfo file close IOException e:" + e);
            }
        }*/
//        return "";
    }

    /**
     * 获取GPU信息
     * @return
     */
    public static String gpu_info;
    public static String getGpuInfo() {
        if (!TextUtils.isEmpty(gpu_info)) {
            return gpu_info;
        }else{
            return "";
        }

//        TODO:暂时关闭
        /*
        if(!TextUtils.isEmpty(gpu_info)){
            return gpu_info;
        }
        try {
            StringBuilder gpuInfo = new StringBuilder();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                // get a hold of the display and initialize
                final EGLDisplay dpy = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
                final int[] vers = new int[2];
                EGL14.eglInitialize(dpy, vers, 0, vers, 1);

                // find a suitable opengl config. since we do not render, we are not that strict about the exact attributes
                final int[] configAttr = {
                        EGL14.EGL_COLOR_BUFFER_TYPE, EGL14.EGL_RGB_BUFFER,
                        EGL14.EGL_LEVEL, 0,
                        EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                        EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                        EGL14.EGL_NONE
                };

                final EGLConfig[] configs = new EGLConfig[1];
                final int[] numConfig = new int[1];
                EGL14.eglChooseConfig(dpy, configAttr, 0, configs, 0, 1, numConfig, 0);
                if (numConfig[0] == 0) {
                   LogUtil.w("getOpenGLESInformation", "no config found! PANIC!");
                }

                // we need a surface for our context, even if we do not render anything so let's create a little offset surface
                final int[] surfAttr = {
                        EGL14.EGL_WIDTH, 64,
                        EGL14.EGL_HEIGHT, 64,
                        EGL14.EGL_NONE
                };
                final EGLConfig config = configs[0];
                final EGLSurface surf = EGL14.eglCreatePbufferSurface(dpy, config, surfAttr, 0);

                // finally let's create our context
                final int[] ctxAttrib = { EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE };
                final EGLContext ctx = EGL14.eglCreateContext(dpy, config, EGL14.EGL_NO_CONTEXT, ctxAttrib, 0);

                // set up everything, make the context our current context
                EGL14.eglMakeCurrent(dpy, surf, surf, ctx);

                // get gpu render
                String gpu_render = glGetString(GL_RENDERER);
                gpuInfo.append(gpu_render);

                // free and destroy everything
                EGL14.eglMakeCurrent(dpy, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE,EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroySurface(dpy, surf);
                EGL14.eglDestroyContext(dpy, ctx);
                EGL14.eglTerminate(dpy);
            }else{
                EGL10 egl10 = (EGL10) javax.microedition.khronos.egl.EGLContext.getEGL();
                if(egl10 != null) {
                    GL10 gl10 = (GL10) egl10.eglGetCurrentContext().getGL();
                    //get gpu render
                    if(gl10 != null) {
                        String gpu_render = gl10.glGetString(GL10.GL_RENDERER);
                        gpuInfo.append(gpu_render);
                    }
                }
            }
            //get gpu freq
            String gpu_freq = getGpuFreq();
            if (!TextUtils.isEmpty(gpu_freq)) {
                gpuInfo.append(" @");
                gpuInfo.append(gpu_freq);
            }
            gpu_info = gpuInfo.toString();
            return gpu_info;
        }catch (NullPointerException e){
            LogUtil.e(TAG, "getGpuInfo NullPointerException :" + e);
            return null;
        }catch (Exception e){
            LogUtil.e(TAG, "getGpuInfo Exception:" + e);
            return null;
        }*/
    }


    public static String getGpuFreq() {
        InputStreamReader fileReader = null;
        BufferedReader bufferedReader = null;
        String gpu_freq = "";
        try {
            String filePath = getGpuFreqPath();
            if (TextUtils.isEmpty(filePath)) {
                return "";
            }
            fileReader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
            bufferedReader = new BufferedReader(fileReader, 512);

            String line = "";
            StringBuilder output = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line);
            }
            int freq = Integer.parseInt(output.toString()) / 1000000;
            gpu_freq = Integer.toString(freq) + "MHz";
        } catch (IOException e) {
            Log.e("readLine", "IOException:" + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("BufferedReader", "IllegalArgumentException:" + e.getMessage());
        }finally {
            try {
                if(fileReader != null){
                    fileReader.close();
                }

                if(bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (IOException e){
                Log.e(TAG,"getGpuFreq file close IOException e:"+e.toString());
            }
        }
        return gpu_freq;
    }

    public static String getGpuFreqPath(){
        String gpuBasePath = "";
        String gpuFreqMaxPath = "";

        for(final String s:FREQ_BASE_PATH){
            if(fileExists(s)){
                gpuBasePath = s;
            }
        }

        for(final String s:FREQ_MAX_PATH){
            if(fileExists(gpuBasePath + s)){
                gpuFreqMaxPath = gpuBasePath + s;
            }
        }
        return gpuFreqMaxPath;
    }

    public static boolean fileExists(String fileName){
        if(TextUtils.isEmpty(fileName) || TextUtils.equals("_", fileName)){
            return false;
        }
        return new File(fileName).exists();
    }
}
