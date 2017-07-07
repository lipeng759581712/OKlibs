package com.max.utils;

/**
 * Created by elwinxiao on 2015/12/15.
 */
public class ResourceUtils {

    public static int normalizeColor(int color) {
        if ((color & 0xff000000) == 0) color |= 0xff000000;
        return color;
    }

}
