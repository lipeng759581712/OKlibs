package com.max.utils;

import com.tencent.gpframework.log.ALog;

import java.lang.reflect.Field;

/**
 * 反射工具，目前提供了一个反射查找对象中指定字段值的方法
 *
 * @author Created by elwinxiao on 2015/7/23.
 */
public class ReflectUtils {

    /**
     * 通过反射查找某个对象中指定字段的值，如果无法找到，则返回默认值
     *
     * @param object       查找对象
     * @param fieldName    字段名
     * @param defaultValue 默认值
     */
    public static <T, F> F extractField(T object, String fieldName, F defaultValue) {
        F value = defaultValue;

        try {
            Field field = object.getClass().getField(fieldName);
            field.setAccessible(true);
            Object value1 = (F) field.get(object);
            if (value1 == null) {
                ALog.e("ReflectUtils", "extract field[" + fieldName + "] failed: actual value is null, fieldType=" + field.getType());
            } else if (defaultValue == null || defaultValue.getClass().getCanonicalName().equals(value1.getClass().getCanonicalName())) {
                value = (F)value1;
            } else {
                ALog.e("ReflectUtils", "extract field[" + fieldName + "] failed: " + "error type, expectType=" + value.getClass().getSimpleName() + ", actualType=" +  value1.getClass().getSimpleName());
            }
        } catch (NoSuchFieldException e) {
            ALog.e("ReflectUtils", "extract field[" + fieldName + "] failed: " + e);
        } catch (IllegalAccessException e) {
            ALog.e("ReflectUtils", "extract field[" + fieldName + "] failed: " + e);
        } catch (ClassCastException e) {
            ALog.e("ReflectUtils", "extract field[" + fieldName + "] failed: " + e);
        }

        return value;
    }
}
