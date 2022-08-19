package com.wuguangxin.utils;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;

/**
 * 日志打印工具类
 * <p>Created by wuguangxin on 14/10/11 </p>
 */
public class Logger {
    private final static String TAG = Logger.class.getSimpleName();
    private static String TAG_PREFIX = "";
    private static boolean mDebug = false;

    /**
     * 是否是DEBUG
     *
     * @return 是否是DEBUG
     */
    public static boolean isDebug() {
        return mDebug;
    }

    /**
     * 设置Logger的debug模式，true则打印日志，false则不打印。
     *
     * @param debug debug
     */
    public static void setDebug(boolean debug) {
        mDebug = debug;
    }

    /**
     * 设置Tag的前缀，可以给整个项目加个前缀，便于过滤项目以外的日志。
     *
     * @param tagPrefix 前缀
     */
    public static void setTagPrefix(String tagPrefix) {
        TAG_PREFIX = tagPrefix;
    }

    // *****************************************************************************

    public static void i(Object msg) { i(TAG, msg); }
    public static void d(Object msg) { d(TAG, msg); }
    public static void e(Object msg) { e(TAG, msg); }
    public static void w(Object msg) { w(TAG, msg); }
    public static void v(Object msg) { v(TAG, msg); }

    // *****************************************************************************
    public static void i(Object tag, Object msg) { i(getClassName(tag), msg); }
    public static void d(Object tag, Object msg) { d(getClassName(tag), msg); }
    public static void e(Object tag, Object msg) { e(getClassName(tag), msg); }
    public static void w(Object tag, Object msg) { w(getClassName(tag), msg); }
    public static void v(Object tag, Object msg) { v(getClassName(tag), msg); }

    // *****************************************************************************
    public static <T> void i(Class<T> clazz, Object msg) { i(getClassName(clazz), msg);}
    public static <T> void d(Class<T> clazz, Object msg) { d(getClassName(clazz), msg);}
    public static <T> void e(Class<T> clazz, Object msg) { e(getClassName(clazz), msg);}
    public static <T> void w(Class<T> clazz, Object msg) { w(getClassName(clazz), msg);}
    public static <T> void v(Class<T> clazz, Object msg) { v(getClassName(clazz), msg); }

    // *****************************************************************************
    public static void i(String tag, Object msg) { printLog(tag, msg, Log.INFO); }
    public static void d(String tag, Object msg) { printLog(tag, msg, Log.DEBUG); }
    public static void e(String tag, Object msg) { printLog(tag, msg, Log.ERROR); }
    public static void w(String tag, Object msg) { printLog(tag, msg, Log.WARN); }
    public static void v(String tag, Object msg) { printLog(tag, msg, Log.VERBOSE); }

    private static void printLog(String tag, Object msg, int type) {
        if (!mDebug) return;
        if (tag == null) tag = TAG;
        tag = String.format("%s%s", TAG_PREFIX, tag);
        switch (type) {
        case Log.VERBOSE: Log.v(tag, toString(msg)); break;
        case Log.DEBUG: Log.d(tag, toString(msg)); break;
        case Log.INFO: Log.i(tag, toString(msg)); break;
        case Log.WARN: Log.w(tag, toString(msg)); break;
        case Log.ERROR: Log.e(tag, toString(msg)); break;
        }
    }

    private static String toString(Object msg) {
        if (msg instanceof byte[])  return String.valueOf(msg);
        if (msg instanceof String[]) return Arrays.toString((String[]) msg);
        return String.valueOf(msg);
    }

    private static String getClassName(Object obj) {
        return obj == null ? null : obj.getClass().getSimpleName();
    }

    private static String getClassName(Context context) {
        return context == null ? null : context.getClass().getSimpleName();
    }

    private static <T> String getClassName(Class<T> clazz) {
        return clazz == null ? null : clazz.getSimpleName();
    }
}
