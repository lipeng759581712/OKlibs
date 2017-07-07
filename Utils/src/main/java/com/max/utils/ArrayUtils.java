package com.max.utils;

/**
 * 数组工具类，提供了如空判断、非空判断以及获取数组大小等方法
 */
public class ArrayUtils {

    /**
     * 判断对象数组是否为空
     */
    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断long类型数组是否为空
     */
    public static boolean isEmpty(final long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断int类型数组是否为空
     */
    public static boolean isEmpty(final int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断short类型数组是否为空
     */
    public static boolean isEmpty(final short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断char类型数组是否为空
     */
    public static boolean isEmpty(final char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断byte类型数组是否为空
     */
    public static boolean isEmpty(final byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断double类型数组是否为空
     */
    public static boolean isEmpty(final double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断float类型数组是否为空
     */
    public static boolean isEmpty(final float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断boolean类型数组是否为空
     */
    public static boolean isEmpty(final boolean[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断对象数组是否为非空
     */
    public static <T> boolean isNotEmpty(final T[] array) {
         return (array != null && array.length != 0);
    }

    /**
     * 判断long类型数组是否为非空
     */
    public static boolean isNotEmpty(final long[] array) {
        return (array != null && array.length != 0);
    }

    /**
     * 判断int类型数组是否为非空
     */
    public static boolean isNotEmpty(final int[] array) {
        return (array != null && array.length != 0);
    }

    /**
     * 判断short类型数组是否为非空
     */
    public static boolean isNotEmpty(final short[] array) {
        return (array != null && array.length != 0);
    }

    /**
     * 判断char类型数组是否为非空
     */
    public static boolean isNotEmpty(final char[] array) {
        return (array != null && array.length != 0);
    }

    /**
     * 判断byte类型数组是否为非空
     */
    public static boolean isNotEmpty(final byte[] array) {
        return (array != null && array.length != 0);
    }

    /**
     * 判断double类型数组是否为非空
     */
    public static boolean isNotEmpty(final double[] array) {
        return (array != null && array.length != 0);
    }

    /**
     * 判断float类型数组是否为非空
     */
    public static boolean isNotEmpty(final float[] array) {
        return (array != null && array.length != 0);
    }

    /**
     * 判断boolean类型数组是否为非空
     */
    public static boolean isNotEmpty(final boolean[] array) {
        return (array != null && array.length != 0);
    }

    /**
     * 获取对象数组大小，当输入为null时，返回-1
     */
    public static<T> int sizeOf(T[] array) {
        return array == null ? -1 : array.length;
    }

    public static int sizeOf(byte[] array) {
        return array == null ? -1 : array.length;
    }

    /**
     * 将数组里转成String，方便打印，会一一调用元素的toString()
     */
    public static String toString(Object[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < array.length; ++i) {
            Object obj = array[i];
            builder.append(obj.toString());
            if (i < array.length - 1) builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

    public static <T> T firstItem(T[] array) {
        if (isEmpty(array)) return null;
        return array[0];
    }

    public static Class<byte[]> getByteArrayClass() {
        Class<byte[]> bytesClass = getArrayClass(new byte[1]);
        return bytesClass;
    }

    public static <T> Class<T> getArrayClass(T bs) {
        return (Class<T>) bs.getClass();
    }
}
