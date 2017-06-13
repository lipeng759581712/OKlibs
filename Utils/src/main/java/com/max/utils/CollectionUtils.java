package com.max.androidutilsmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by elwinxiao on 2015/3/30.
 */
public class CollectionUtils {

	public static<E> int sizeOf(Collection<E> collection) {
		return collection == null ? -1 : collection.size();
	}

	public static<E> int safeSizeOf(Collection<E> collection) {
		return collection == null ? 0 : collection.size();
	}

	public static<T> int sizeOf(T[] array) {
		return array == null ? -1 : array.length;
	}

    public static<T> boolean isEmptyArray(T[] array) {
        return (array == null || array.length == 0);
    }

    public static<E> boolean isEmptyList(Collection<E> collection) {
        return (collection == null || collection.size() == 0);
    }

	public static boolean isEmpty(Map<?,?> map){
		return map == null || map.isEmpty();
	}


	public static boolean isNotEmpty(Collection<?> collection){
		return collection != null && collection.size() > 0;
	}

	public static boolean isNotEmpty(Map<?,?> map){
		return map != null && !map.isEmpty();
	}

	public static boolean isNotEmpty(Object[] array){
		return array != null && array.length > 0;
	}

	public static <T> T getItem(List<T> list, int position){
		if(list == null || position > list.size()){
			return null;
		}
		return list.get(position);
	}
	public static boolean isEmpty(Collection<?> c)
	{
		return c == null || c.size() == 0;
	}

	public static boolean isEmpty(Object[] objs)
	{
		return objs == null || objs.length == 0;
	}

	public static <T> T get(Collection<T> c, int index)
	{
		if (isEmpty(c))
			return null;

		if (index < 0 || index >= c.size())
			return null;

		if (c instanceof List)
			return (T) ((List<T>) c).get(index);

		List<? extends T> a = new ArrayList<T>(c);
		return a.get(index);
	}

	public  static  <T> ArrayList<T> convertToArrayList(List<T> list) {
		if (list instanceof ArrayList) {
			return (ArrayList<T>) list;
		}

		ArrayList<T> arrayList = new ArrayList();
		for (T item: list) {
			arrayList.add(item);
		}

		return arrayList;
	}

}
