package com.wuguangxin.utils;

import java.lang.reflect.ParameterizedType;

public class TUtil {

    /**
     * 获取泛型类型
     * @param obj 类对象
     * @param position 泛型的位置
     * @param <T> 类型
     * @return
     */
    public static <T> T getT(Object obj, int position) {
        try {
            return ((Class<T>) ((ParameterizedType) (obj.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[position]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}