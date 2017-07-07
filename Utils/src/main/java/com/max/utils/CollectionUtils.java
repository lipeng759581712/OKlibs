package com.max.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 容器工具，提供了空判断、非空判断、获取容器大小、获取容器中的元素等方法
 *
 * @author Created by elwinxiao on 2015/3/30.
 */
public class CollectionUtils {

	/**
	 * 获取Collection对象的大小
	 *
	 * @return 传入Collection对象为null时返回-1，否则返回Collection对象的大小
	 */
	public static<E> int sizeOf(Collection<E> collection) {
		return collection == null ? -1 : collection.size();
	}

	/**
	 * 获取Collection对象的大小
	 *
	 * @return 传入Collection对象为null时返回0，否则返回Collection对象的大小
	 */
	public static<E> int sizeOfSafe(Collection<E> collection) {
		return collection == null ? 0 : collection.size();
	}

	/**
	 * 获取对象数组的大小
	 */
	public static<T> int sizeOf(T[] array) {
		return ArrayUtils.sizeOf(array);
	}

	/**
	 * 判断Collection对象是否为空
	 */
	public static <E> boolean isEmpty(Collection<E> collection) {
		return (collection == null || collection.size() == 0);
	}

	/**
	 * 判断Map对象是否为空
	 */
	public static boolean isEmpty(Map<?,?> map){
		return map == null || map.isEmpty();
	}

	/**
	 * 判断Collection对象是否为非空
	 */
	public static boolean isNotEmpty(Collection<?> collection){
		return collection != null && collection.size() > 0;
	}

	/**
	 * 判断Map对象是否为空
	 */
	public static boolean isNotEmpty(Map<?,?> map){
		return map != null && !map.isEmpty();
	}

	/**
	 * 判断对象数组是否为非空
	 */
	@Deprecated
	public static boolean isNotEmpty(Object[] array){
		return ArrayUtils.isNotEmpty(array);
	}

	/**
	 * 获取List对象中指定位置的元素
	 */
	public static <T> T getItem(List<T> list, int position){
		if(list == null || position > list.size()){
			return null;
		}
		return list.get(position);
	}

	/**
	 * 判断对象数组是否为空
	 */
	public static boolean isEmpty(Object[] objs)
	{
		return objs == null || objs.length == 0;
	}

	/**
	 * 获取Collection对象中指定位置的元素
	 */
	public static <T> T get(Collection<T> c, int position)
	{
		if (isEmpty(c))
			return null;

		if (position < 0 || position >= c.size())
			return null;

		if (c instanceof List)
			return (T) ((List<T>) c).get(position);

		List<? extends T> a = new ArrayList<T>(c);
		return a.get(position);
	}

	/**
	 * 将List对象转化为ArrayList对象
	 */
	public static <T> ArrayList<T> convertToArrayList(List<T> list) {
		if (list instanceof ArrayList) {
			return (ArrayList<T>) list;
		}

		ArrayList<T> arrayList = new ArrayList();
		for (T item: list) {
			arrayList.add(item);
		}

		return arrayList;
	}

	/**
	 * 将数组里转成String，方便打印，会一一调用元素的toString()
	 */
	public static String toString(Object[] array) {
		return ArrayUtils.toString(array);
	}

	public static String toString(Collection<?> collection) {
		if (collection == null) {
			return null;
		}
		if (collection.size() == 0) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		builder.append("[");

		int i = 0;
		for (Object obj: collection) {
			builder.append(obj.toString());
			if (i < collection.size() - 1) builder.append(", ");
			++i;
		}

		builder.append("]");
		return builder.toString();
	}


	public static <T> T firstItem(T[] array) {
		return ArrayUtils.firstItem(array);
	}

	public static <T> T firstItem(Collection<T> collection) {
		if (isEmpty(collection)) return null;

		Iterator<T> iter = collection.iterator();
		if (iter == null) return null;

		return iter.next();
	}


	private static<E> void walk(Collection<E> collection, ItemWorker<E> itemWorker) {
		int index = 0;
		Iterator<E> iterator = collection.iterator();
		while (iterator.hasNext()) {
			if (itemWorker.onItem(iterator.next(), index++, collection.size())){
				break;
			}
		}
	}

	private static<E> void walkList(List<E> list, ItemWorker<E> itemWorker) {
		for (int i = 0; i < list.size(); ++i) {
			itemWorker.onItem(list.get(i), i, list.size());
		}
	}

	private interface ItemWorker<E> {
		boolean onItem(E item, int index, int size);
	}

}
