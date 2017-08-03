package com.max.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * 字节工具，提供了字节数组与其他类型数据的转换、字节数组文件读写等方法
 *
 * @author Created by elwinxiao on 2015/4/20.
 */
public class ByteUtils {

    /**
     * 将字节数组转化为long类型数据
     */
    public static long byte2long(byte[] b) throws IOException {
        ByteArrayInputStream baos = new ByteArrayInputStream(b);
        DataInputStream dos = new DataInputStream(baos);
        long result = dos.readLong();
        dos.close();
        return result;
    }

    /**
     * 将字节数组写入文件
     */
    public static void writeBytesToFile(File file, byte[] data) throws IOException {
        OutputStream os = new FileOutputStream(file);
        os.write(data);
    }

    /**
     * 从文件中读取字节数组
     */
    public static byte[] readBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }

        return os.toByteArray();
    }

    /**
     * 按指定字节序将整数转换为指定长度的字节数组
     *
     * @param number 待转换的整数
     * @param len 输出字节数组的长度
     * @param big_endian 是否网络字节序。true表示按网络字节序(Big Endian)，false表示按Little Endian字节序
     * @return 返回字节数组
     */
    public static byte[] numberToBytes(long number, int len, boolean big_endian)
    {
        byte[] buffer = new byte[len];
        int start = big_endian ? (len - 1) : 0;
        int end = big_endian ? -1 : len;
        int inc = big_endian ? -1 : 1;

        for (int i = start; i != end; i += inc)
        {
            buffer[i] = (byte) (number & 0xff);
            number >>>= 8;
        }

        return buffer;
    }

    /**
     * 创建指定长度的字节数组，数组元素全部初始化为0
     */
    public static byte[] createZeroBytes(int length)
    {
        if (length <= 0)
            throw new IllegalArgumentException("length must be gt 0");

        byte[] bytes = new byte[length];
        Arrays.fill(bytes, (byte) 0);
        return bytes;
    }

    /**
     * 从指定字节数组中查找某子字节数组的第一次出现的位置
     *
     * @param src 指定数组
     * @param start 起始查询位置
     * @param sub 待查询数组
     * @return 如果没找到，返回-1，否则返回索引
     */
    public static int indexOf(byte[] src, int start, byte[] sub)
    {

        if (src == null || sub == null)
        {
            throw new NullPointerException("source or target array is null!");
        }

        int index = -1;
        int len = src.length;
        int tlen = sub.length;

        if (start >= len || len - start < tlen)
        {
            return -1;
        }

        while (start <= len - tlen)
        {
            int i = 0;
            for (; i < tlen; i++)
            {
                if (src[start + i] != sub[i])
                {
                    break;
                }
            }

            if (i == tlen)
            {
                index = start;
                break;
            }

            start++;
        }

        return index;
    }

    /**
     * 从字节数组中解析出整数
     *
     * @param buf 字节数组
     * @param big_Endian 是否大字节序解析
     * @return 相应的整数
     */
    public static int parseInteger(byte[] buf, boolean big_Endian)
    {
        return (int) parseNumber(buf, 4, big_Endian);
    }

    /**
     * 从字节数组中解析出短整数
     *
     * @param buf 字节数组
     * @param big_Endian 是否大字节序解析
     * @return 相应的短整数
     */
    public static int parseShort(byte[] buf, boolean big_Endian)
    {
        return (int) parseNumber(buf, 2, big_Endian);
    }

    /**
     * 从字节数组中解析出长整数
     *
     * @param buf 字节数组
     * @param len 整数组成的字节数
     * @param big_Endian 是否大字节序解析
     * @return 相应的长整数
     */
    public static long parseNumber(byte[] buf, int len, boolean big_Endian)
    {
        if (buf == null || buf.length == 0)
        {
            throw new IllegalArgumentException("byte array is null or empty!");
        }

        int mlen = Math.min(len, buf.length);
        long r = 0;
        if (big_Endian)
            for (int i = 0; i < mlen; i++)
            {
                r <<= 8;
                r |= (buf[i] & 0xff);
            }
        else
            for (int i = mlen - 1; i >= 0; i--)
            {
                r <<= 8;
                r |= (buf[i] & 0xff);
            }
        return r;
    }

    /**
     * 判断字节数组是否以另一字节数组开头
     * @param all 原字节数组
     * @param sub 子字节数组
     * @return 返回判断结果
     */
    public static boolean startWiths(byte[] all, byte[] sub)
    {
        if (all == null || sub == null || all.length < sub.length)
            return false;

        for (int i = 0; i < sub.length; i++)
        {
            if (all[i] != sub[i])
                return false;
        }

        return true;
    }

    /**
     * 判断字节数组是否以另一字节数组结尾
     * @param all 原字节数组
     * @param sub 子字节数组
     * @return 返回判断结果
     */
    public static boolean endWiths(byte[] all, byte[] sub)
    {
        if (all == null || sub == null || all.length < sub.length)
            return false;
        int allLen = all.length;
        int subLen = sub.length;

        for (int i = 1; i < (subLen+1); i++)
        {
            if (all[allLen - i] != sub[subLen - i])
                return false;
        }

        return true;
    }

    /**
     * 判断字节数组是否以另一字节数组结尾
     * @param all 原字节数组
     * @param length 原字节数组最大长度
     * @param sub 子字节数组
     * @return 返回判断结果
     */
    public static boolean endWiths(byte[] all, int length, byte[] sub)
    {
        if (all == null || sub == null || length < sub.length)
            return false;

        int allLen = Math.min(all.length, length);
        int subLen = sub.length;

        for (int i = 1; i < (subLen+1); i++)
        {
            if (all[allLen - i] != sub[subLen - i])
                return false;
        }

        return true;
    }
}
