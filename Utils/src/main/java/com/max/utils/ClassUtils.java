package com.max.utils;

/**
 * Created by elwinxiao on 08/06/2017.
 */

public class ClassUtils {
    public static <T> T createInstaceMayNull(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T createInstaceMayException(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("can't create instance for class: " + clazz.getName());
        }
    }
}
