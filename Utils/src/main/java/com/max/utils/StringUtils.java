/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.max.androidutilsmodule;


import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 字符串操作工具包
 * 
 * @author 白猫
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils

{
	private static String TAG = "StringUtils";
    /**
     * @return True iff the url is a network url.
     */
    public static boolean isNetworkUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return isHttpUrl(url) || isHttpsUrl(url);
    }

    /**
     * @return True iff the url is an http: url.
     */
    public static boolean isHttpUrl(String url) {
        return (null != url) &&
                (url.length() > 6) &&
                url.substring(0, 7).equalsIgnoreCase("http://");
    }

    /**
     * @return True iff the url is an https: url.
     */
    public static boolean isHttpsUrl(String url) {
        return (null != url) &&
                (url.length() > 7) &&
                url.substring(0, 8).equalsIgnoreCase("https://");
    }
    
    public static int compareVersion(String s1, String s2) {
		if (s1 == null && s2 == null)
			return 0;
		else if (s1 == null)
			return -1;
		else if (s2 == null)
			return 1;

		String[] arr1 = s1.split("[^a-zA-Z0-9]+"), arr2 = s2
				.split("[^a-zA-Z0-9]+");

		int i1, i2, i3;

		for (int ii = 0, max = Math.min(arr1.length, arr2.length); ii <= max; ii++) {
			if (ii == arr1.length)
				return ii == arr2.length ? 0 : -1;
			else if (ii == arr2.length)
				return 1;

			try {
				i1 = Integer.parseInt(arr1[ii]);
			} catch (Exception x) {
				i1 = Integer.MAX_VALUE;
			}

			try {
				i2 = Integer.parseInt(arr2[ii]);
			} catch (Exception x) {
				i2 = Integer.MAX_VALUE;
			}

			if (i1 != i2) {
				return i1 - i2;
			}

			i3 = arr1[ii].compareTo(arr2[ii]);

			if (i3 != 0)
				return i3;
		}

		return 0;
	}

	public static byte[] getUtf8(String str) {
		try {
			byte[] keyBytes = str.getBytes("UTF-8");
			return keyBytes;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("unsupport utf-8 encoding", e);
		}
	}

	public static String richNumber(long value) {
		return value + "(0x" + Long.toHexString(value) + ")";
	}

	/**
	 * 方法名称:transMapToString
	 * 传入参数:map
	 * 返回值:String 形如 username'chenziwen^password'1234
	 */
	public static String transMapToString(Map map) {
		String equal = "'";
		String space = "^";
		return transMapToString(map,equal,space);
	}


	public static String transMapToString(Map map, String equal, String space){
		if(map==null)return null;
		Map.Entry entry;
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
			entry = (Map.Entry) iterator.next();
			sb.append(entry.getKey().toString()).append(equal).append(null == entry.getValue() ? "" :
					entry.getValue().toString()).append(iterator.hasNext() ? space : "");
		}
		return sb.toString();
	}


	/**
	 * 方法名称:transStringToMap
	 * 传入参数:mapString 形如 username'chenziwen^password'1234
	 * 返回值:Map
	 */
	public static Map transStringToMap(String mapString) {
		Map map = new HashMap();
		StringTokenizer items;
		for (StringTokenizer entrys = new StringTokenizer(mapString, "^"); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
			items = new StringTokenizer(entrys.nextToken(), "'");
		return map;
	}


	public static int[] transStringToIntArray(String typeString)
	{
		if(TextUtils.isEmpty(typeString)){
			return new int[]{};
		}
		typeString.replace(" ", "");//去掉字符串中的空字符
		String[] str = typeString.split(",");//分割字符串
		int[] result = new int[str.length];
		try{
			for(int i= 0; i<str.length; i++){
				result[i] = Integer.valueOf(str[i].trim());
			}
		}catch (Exception e) {
			throw new RuntimeException("transStringToIntArray excption", e);
		}
		return result;
	}

	public static<T> T transStringToSafe(String str, T defaultValue){
		T transValue = defaultValue;
		try{
			if (str!=null){

				if (transValue instanceof Integer){
					transValue = (T) Integer.valueOf(str);
				}else if (transValue instanceof Float){
					transValue = (T) Float.valueOf(str);
				}
			}
		}catch (Exception e){
			transValue = defaultValue;
			Log.e(TAG,"transStringToSafe fail : "+e.getMessage());
		}
        return transValue;
	}

	public static String transStringArrayToStr(String[] strings){
		if (strings==null)return " ";
		StringBuffer sb = new StringBuffer();
		int length= strings.length;
		for (int i = 0; i < length; i++) {
			sb.append(strings[i]);
			if (i!=length-1){
				sb.append(",");
			}

		}
		return sb.toString();
	}



    
}
