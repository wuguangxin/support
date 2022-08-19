package com.wuguangxin.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wuguangxin on 2020-01-08.
 */
public class ListUtils {

    /**
     * 截取 list 中从下标 index 开始（包含index）的 len 条数据，如果需要的长度不够，则截取到最后一条
     * @param list 列表
     * @param index 开始角标
     * @param len 长度
     * @param <T>
     * @return
     */
    public static <T> List<T> subList(List<T> list, int index, int len) {
        List<T> result = new ArrayList<>();
        if (list != null) {
            result.addAll(list);
        }
        int size = result.size();
        if (size == 0 || index >= size - 1) {
            return new ArrayList<>();
        } else if (len >= size - 1) {
            return result.subList(index, size - 1);
        } else {
            return result.subList(index, Math.min(index + len, size));
        }
    }

    public static <T> List<T> toList(T... elements) {
        List<T> list = new ArrayList<T>();
        Collections.addAll(list, elements);
        return list;
    }
}
