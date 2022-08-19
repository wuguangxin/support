package com.wuguangxin.utils;

import java.util.List;

/**
 * Created by wuguangxin on 2021/10/5.
 */
public class ArrayUtils {

    @SafeVarargs
    public static <T> T[] of(T... t) {
        return t;
    }

    public static <T> T[] of(List<T> list) {
        return list == null ? null : (T[]) list.toArray();
    }
}
