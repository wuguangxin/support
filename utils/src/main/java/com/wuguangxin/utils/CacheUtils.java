package com.wuguangxin.utils;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * 数据缓存工具类(支持基本数据类型、JSONObject、JSONArray、集合以及已实现Serializable的对象)
 * Created by wuguangxin on 2015/3/9
 */
public class CacheUtils {

    public static <T> boolean put(String key, T value) {
        return write(key, value);
    }

    public static <T> T get(String key, T def) {
        return read(key, def);
    }

    public static boolean putString(String key, String value) {
        return write(key, value);
    }

    public static String getString(String key) {
        return read(key, null);
    }

    public static String getString(String key, String defValue) {
        return read(key, defValue);
    }

    public static boolean putNumber(String key, Number value) {
        return write(key, value);
    }

    public static Number getNumber(String key) {
        return read(key, 0);
    }

    public static Number getNumber(String key, Number defValue) {
        return read(key, defValue);
    }

    public static boolean putInt(String key, int value) {
        return write(key, value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defValue) {
        return read(key, defValue);
    }

    public static boolean putLong(String key, long value) {
        return write(key, value);
    }

    public static long getLong(String key) {
        return read(key,  0L);
    }

    public static long getLong(String key, long defValue) {
        return read(key, defValue);
    }

    public static boolean putFloat(String key, float value) {
        return write(key, value);
    }

    public static float getFloat(String key) {
        return read(key, null);
    }

    public static float getFloat(String key, float defValue) {
        return read(key, defValue);
    }

    public static boolean putDouble(String key, double value) {
        return write(key, value);
    }

    public static double getDouble(String key) {
        return read(key, null);
    }

    public static double getDouble(String key, double defValue) {
        return read(key, defValue);
    }

    public static boolean putBoolean(String key, boolean value) {
        return write(key, value);
    }

    public static boolean getBoolean(String key) {
        return read(key, null);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return read(key, defValue);
    }

    public static boolean putJSONObject(String key, JSONObject value) {
        return write(key, value);
    }

    public static JSONObject getJSONObject(String key) {
        return read(key, null);
    }

    public static JSONObject getJSONObject(String key, JSONObject defValue) {
        return read(key, defValue);
    }

    public static boolean putJSONArray(String key, JSONArray value) {
        return write(key, value);
    }

    public static JSONArray getJSONArray(String key) {
        return read(key, null);
    }

    public static JSONArray getJSONArray(String key, JSONArray defValue) {
        return read(key, defValue);
    }

    public static <T> boolean putList(String key, List<T> list) {
        return write(key, list);
    }

    public static <T> List<T> getList(String key) {
        return read(key, null);
    }

    public static <T> List<T> getList(String key, List<T> defValue) {
        return read(key, defValue);
    }

    public static <K, V> boolean putMap(String key, Map<K, V> map) {
        return write(key, map);
    }

    public static <K, V> Map<K, V> getMap(String key) {
        return read(key, null);
    }

    public static <K, V> Map<K, V> getMap(String key, Map<K, V> defValue) {
        return read(key, defValue);
    }

    /**
     * 根据key删除文件
     */
    private static boolean delete(String key) {
        return FileUtils.deleteFile(createFile(key));
    }

    /**
     * 删除文件目录
     *
     * @param dir 文件目录
     * @return
     */
    public static boolean deleteDir(File dir) {
        return FileUtils.deleteFileDir(dir);
    }

    /**
     * 写对象
     */
    private static <T> boolean write(String key, T obj) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = createFile(key);
            if (file.exists()) {
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(obj);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(oos, fos);
        }
        return false;
    }

    /**
     * 读对象
     */
    private static <T> T read(String key, T defObj) {
        if (!TextUtils.isEmpty(key)) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                File file = createFile(key);
                if (file.exists() && file.length() > 0) {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    return (T) ois.readObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                FileUtils.close(ois, fis);
            }
        }
        return defObj;
    }


    /** 用来区分多用户分别存储 */
    private static String userTag;
    private static WeakReference<Context> contextWeakReference;

    public static void init(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    private static Context getContext() {
        return contextWeakReference != null ? contextWeakReference.get() : null;
    }

    /**
     * 设置用户的标签（用来区分多用户分别存储）
     * @param userTag 用户tag，内部已做MD5加密
     */
    public static void setUserTag(String userTag) {
        CacheUtils.userTag = md5(userTag);
    }

    public static String getUserTag() {
        return userTag;
    }

    /**
     * 创建目标文件
     */
    private static File createFile(String key) {
        File file = new File(getUserCacheDir(), md5(key));
        if (!file.exists()) {
            boolean result = file.mkdirs();
        }
        return file;
    }

    /**
     * 按用户获取数据缓存目录
     */
    public static File getUserCacheDir() {
        Context context = getContext();
        return context == null ? null : new File(context.getCacheDir(), getUserTag());
    }

    /**
     * 获取缓存大小
     */
    public static long getCacheSize() {
        return FileUtils.getFileSize(getUserCacheDir());
    }

    private static String md5(String value) {
        return MD5.encode(value);
    }

}
