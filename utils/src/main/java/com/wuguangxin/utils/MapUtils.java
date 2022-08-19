package com.wuguangxin.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Map操作工具类
 *
 * Created by wuguangxin on 2015-4-2.
 */
public class MapUtils {

    /**
     * 根据value获取Map集合中的key
     *
     * @param map Map集合
     * @param value value值
     * @return key
     */
    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        if (map != null && !map.isEmpty() && value != null) {
            Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
            Entry<K, V> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();
                if (entry != null && value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * 根据Key获取Map集合中的value
     *
     * @param map Map集合
     * @param key key值
     * @return key
     */
    public static <K, V> V getValueByKey(Map<K, V> map, K key) {
        return map == null ? null : map.get(key);
    }

    /**
     * 获取Map集合中的第一个Key
     *
     * @param map Map
     * @return Key
     */
    public static <K, V> K getFirstKey(Map<K, V> map) {
        if (map != null && !map.isEmpty()) {
            Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
            Entry<K, V> entry;
            if (iterator.hasNext()) {
                entry = iterator.next();
                if (entry != null) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * 获取Map<K, V>集合中的第一个Value
     *
     * @param map Map<K, V>
     * @return Value
     */
    public static <K, V> V getFirstValue(Map<K, V> map) {
        if (map != null && !map.isEmpty()) {
            Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
            Entry<K, V> entry;
            if (iterator.hasNext()) {
                entry = iterator.next();
                if (entry != null) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取key在Map中的索引
     *
     * @param map Map<K, V>
     * @param key key
     * @return index
     */
    public static <K, V> int indexOfKey(Map<K, V> map, String key) {
        if (map != null && key != null) {
            Set<K> keySet = map.keySet();
            Object[] objects = keySet.toArray();
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] != null && objects[i].toString().equals(key)) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * 获取Map的key的列表
     *
     * @param map Map<K, V>
     * @return List
     */
    public static <K, V> List<K> getKeys(Map<K, V> map) {
        List<K> list = new ArrayList<>();
        if (map != null) {
            list.addAll(map.keySet());
        }
        return list;
    }

    /**
     * 获取Map的value的列表
     *
     * @param map Map<K, V>
     * @return List
     */
    public static <K, V> List<V> getValues(Map<K, V> map) {
        List<V> list = new ArrayList<>();
        if (map != null) {
            list.addAll(map.values());
        }
        return list;
    }
}
