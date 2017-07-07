package com.max.utils;

import java.util.Arrays;

/**
 * Created by elwinxiao on 2015/12/9.
 */
public class HexUtils {
    private static final char[] toDigit = ("0123456789ABCDEF").toCharArray();

    private static final int[] fromDigit = {
            0,  1,  2,  3,  4,  5,  6,  7,  8,  9, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15
    };

    /**
     * 将采用Base16编码方式的字符串解码成字节数组
     */
    public static byte[] fromHex(String s) {
        return fromHex(s, Integer.MAX_VALUE);
    }

    /**
     * 将采用Base16编码方式的字符串解码成字节数组，limit用于限制解码后的字节数组长度
     */
    public static byte[] fromHex(String s, int limit) {
        // Note that a zero-length hex string is valid.
        int length = s.length();
        if ((length % 2) != 0)
            return null;

        byte[] b = new byte[length/2];
        int j = 0;

        for (int i = 0; i < length; i += 2) {
            int c1 = s.charAt(i);
            int c2 = s.charAt(i+1);
            int value1;
            int value2;

            if (c1 < '0' || c1 > 'f' || (value1 = fromDigit[c1 - '0']) < 0)
                return null;

            if (c2 < '0' || c2 > 'f' || (value2 = fromDigit[c2 - '0']) < 0)
                return null;

            b[j++] = (byte) (((value1 & 0xF) << 4) | (value2 & 0xF));
            if (j >= limit)
                break;
        }

        if (j != b.length) {
            byte[] b2 = new byte[j];
            System.arraycopy(b, 0, b2, 0, j);
            b = b2;
        }

        return b;
    }

    /**
     * 将字节数组编码成采用Base16编码方式的字符串
     */
    public static String toHex(byte[] b) {
        if (b == null) return null;

        char[] chars = new char[2*b.length];
        int j = 0;

        for (int i = 0; i < b.length; ++i) {
            byte bits = b[i];

            chars[j++] = toDigit[((bits >>> 4) & 0xF)];
            chars[j++] = toDigit[(bits & 0xF)];
        }

        return new String(chars);
    }

    public static String toPrefixHex(byte[] array) {
        return toPrefixHex(array, 0, array.length);
    }

    /**
     * 字节数组转化为十六进制字符串，加前辍“0x”
     */
    public static String toPrefixHex(byte[] array, int start, int end) {
        if (array == null || start >= array.length) {
            return null;
        }

        int actualEnd = Math.min(end, array.length);
        byte[] effectArray = Arrays.copyOfRange(array, start, actualEnd);

        String output = "0x" + HexUtils.toHex(effectArray);
        return output;
    }
}
