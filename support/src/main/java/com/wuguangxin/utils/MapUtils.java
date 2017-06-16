package com.wuguangxin.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapUtils{
	private static int MAP_KEY = 0;
	private static int MAP_VALUE = 1;
	
	/**
	 * 根据value获取Map集合中的key
	 * @param map Map集合
	 * @param value value值
	 * @return
	 */
	public static String getKeyFromValue(Map<String, String> map, String value){
		if(!(map == null || map.isEmpty() || TextUtils.isEmpty(value))){
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			Entry<String, String> entry;
			while (iterator.hasNext()) {
				entry = iterator.next();
				if(value.equals(entry.getValue())){
					return entry.getKey();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取Map集合中的第一个Key
	 * @param map
	 * @return
	 */
	public static String getFirstKey(Map<String, String> map){
		return getKeyValue(map, MAP_KEY);
	}
	
	/**
	 * 获取Map集合中的第一个Value
	 * @param map
	 * @return
	 */
	public static String getFirstValue(Map<String, String> map){
		return getKeyValue(map, MAP_VALUE);
	}

	@SuppressWarnings("null")
	private static String getKeyValue(Map<String, String> map, int keyOrValue){
		if(map != null || !map.isEmpty()){
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			Entry<String, String> entry;
			if(iterator.hasNext()){
				entry = iterator.next();
				if(keyOrValue == MAP_KEY){
					return entry.getKey();
				} else if(keyOrValue == MAP_VALUE){
					return entry.getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取key在Map中的索引
	 * @param map
	 * @param key
	 * @return
	 */
	public static int getIndex(Map<String, String> map, String key){
		if(map != null && key != null){
			Set<String> keySet = map.keySet();
			Object[] array = keySet.toArray();
			for (int i = 0; i < array.length; i++) {
				if(array[i] != null && array[i].toString().equals(key)){
					return i;
				}
			}
		}
		return 0;
	}

	/**
	 * 获取Map的key的列表
	 * @param map
	 */
	public static List<String> getKeyList(Map<String, String> map){
		List<String> list = new ArrayList<String>();
		if(map != null){
	        Iterator<String> it = map.keySet().iterator();  
			if(it != null){
		        while(it.hasNext()){      
		            list.add(it.next());
		        }  
			}
		}
		return list;
	}
	
	/**
	 * 获取Map的value的列表
	 * @param map
	 */
	public static List<String> getValueList(Map<String, String> map){
		List<String> list = new ArrayList<String>();
		if(map != null){
	        Iterator<String> it = map.keySet().iterator();  
			if(it != null){
		        while(it.hasNext()){      
		            list.add(map.get(it.next()));
		        }  
			}
		}
		return list;
	}
}
