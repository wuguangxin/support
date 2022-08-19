package com.wuguangxin.utils.mmkv;


import android.content.Context;
import android.os.Parcelable;

import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
使用说明:
更多查看：https://github.com/Tencent/MMKV/wiki/android_tutorial_cn

MMKV kv = MMKV.defaultMMKV(); // 全局实例对象
MMKV kv = MMKV.mmkvWithID("myID"); // 根据业务不同创建不同实例对象

kv.encode("bool", true);
System.out.println("bool: " + kv.decodeBool("bool"));

kv.encode("int", Integer.MIN_VALUE);
System.out.println("int: " + kv.decodeInt("int"));

kv.encode("long", Long.MAX_VALUE);
System.out.println("long: " + kv.decodeLong("long"));

kv.encode("float", -3.14f);
System.out.println("float: " + kv.decodeFloat("float"));

kv.encode("double", Double.MIN_VALUE);
System.out.println("double: " + kv.decodeDouble("double"));

kv.encode("string", "Hello from mmkv");
System.out.println("string: " + kv.decodeString("string"));

byte[] bytes = {'m', 'm', 'k', 'v'};
kv.encode("bytes", bytes);
System.out.println("bytes: " + new String(kv.decodeBytes("bytes")));
*/

/**
 * <p>
 * MMKV快速存取工具类，代替SP，是微信团队开源的一个key-value存储组件。支持多用户存储。（静态方法）
 * </p>
 * <P>
 * <br/>基本要求：
 *  <ol>
 *   <li>MMKV 支持 API level 16 以上平台；
 *   <li>MMKV 需使用 NDK r16b 或以下进行编译 (通过源码引入 MMKV 的话)。
 *  </ol>
 * </p>
 * <ul>
 * <li>添加依赖库时，最好使用静态库导入：<br/>
 * implementation 'com.tencent: mmkv-static:1.2.2'
 * <li>如果是动态库，会额外占用2M内存空间，特别还要注意一些兼容问题：<br/>
 * implementation 'com.tencent:mmkv: 1.2.2'
 * <li>官方GitHub：https://github.com/Tencent/MMKV
 * <li>官方文档：https://github.com/Tencent/MMKV/wiki/android_setup_cn
 * </ul>
 * Created by wuguangxin on 2020-07-01.
 */
public class MmkvUtils {

    private static Config config = new Config();

    private static Config edit() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public static String init(Context context) {
        return init(context, null);
    }

    /**
     * 在 Application.onCreate() 中初始化
     */
    public static String init(Context context, String cryptKey) {
        edit().setCryptKey(cryptKey);
        return MMKV.initialize(context);

        // 先不考虑该问题，出问题再解决
//        if (Build.VERSION.SDK_INT >= 23) {
//            MMKV.initialize(context);
//        } else {
//            // 一些 Android 设备（API 19）在安装/更新 APK 时可能出错（ReLinker的说法是如果您的应用程序包含本机库，
//            // 并且您的最低SDK低于API 23）, 导致 libmmkv.so 找不到。
//            // 然后就会遇到 java.lang.UnsatisfiedLinkError 之类的 crash。
//            // 有个开源库 ReLinker (https://github.com/KeepSafe/ReLinker）专门解决这个问题，用它来加载 MMKV：
//            // 下面定义的rootDir的目录也是MMKV的原始目录，使用他即可
//            String rootDir = context.getFilesDir().getAbsolutePath() + "/mmkv";
//            MMKV.initialize(rootDir, libName -> ReLinker.loadLibrary(context, libName));
//        }
    }

    /**
     * 设置AES加密KEY
     * @param cryptKey AES加密KEY
     */
    public static void setCryptKey(String cryptKey) {
        edit().setCryptKey(cryptKey);
    }

    /**
     * 切换用户
     * @param userId
     */
    public static void switchUser(String userId) {
        edit().switchUser(userId);
    }

    /**
     * 重置加密（AES）秘钥
     * @param encryptKey 秘钥
     * @return
     */
    public static boolean reKey(String encryptKey) {
        return edit().reKey(encryptKey);
    }

    /** 清除加密，并使数据明文。 */
    public static boolean clearKey() {
        return edit().reKey(null); // 设置null即可
    }

    /** 删除指定key对应的value */
    public static void removeValueForKey(String key) {
        edit().removeValueForKey(key);
    }

    /** 删除指定多个key对应的value */
    public static void removeValuesForKeys(String... keys) {
        edit().removeValuesForKeys(keys);
    }

    /** 是否包含指定的key */
    public static boolean containsKey(String key) {
        return edit().containsKey(key);
    }

    // ------------------------------------ PUT ------------------------------------

    public static EditorProxy put(String key, int value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, long value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, float value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, byte[] value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, double value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, boolean value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, String value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, String[] value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, Serializable value) {
        return edit().put(key, value);
    }

    public static EditorProxy put(String key, Parcelable value) {
        return edit().put(key, value);
    }

    public  static EditorProxy put(String key, Set<String> value) {
        return edit().put(key, value);
    }

    public static <T extends Serializable> EditorProxy put(String key, List<T> value) {
        return edit().put(key, value);
    }

    public static <K extends Serializable, V extends Serializable> EditorProxy put(String key, Map<K, V> value) {
        return edit().put(key, value);
    }

    // ------------------------------------ GET ------------------------------------

    public static int get(String key, int defValue) {
        return edit().get(key, defValue);
    }

    public static long get(String key, long defValue) {
        return edit().get(key, defValue);
    }

    public static float get(String key, float defValue) {
        return edit().get(key, defValue);
    }

    public static byte[] get(String key, byte[] defValue) {
        return edit().get(key, defValue);
    }

    public static double get(String key, double defValue) {
        return edit().get(key, defValue);
    }

    public static boolean get(String key, boolean defValue) {
        return edit().get(key, defValue);
    }

    public static String get(String key, String defValue) {
        return edit().get(key, defValue);
    }

    public static String[] get(String key, String[] defValue) {
        return edit().get(key, defValue);
    }

    public static Set<String> get(String key, Set<String> defValue) {
        return edit().get(key, defValue);
    }

    // ------------------------------------ put/get XXX ------------------------------------

    // ----------int

    public static EditorProxy putInt(String key, int value) {
        return edit().putInt(key, value);
    }

    public static int getInt(String key, int defValue) {
        return edit().getInt(key, defValue);
    }

    public static int getInt(String key) {
        return edit().getInt(key);
    }

    // ----------long

    public static EditorProxy putLong(String key, long value) {
        return edit().putLong(key, value);
    }

    public static long getLong(String key, long defValue) {
        return edit().getLong(key, defValue);
    }

    public static long getLong(String key) {
        return edit().getLong(key);
    }

    // ----------float

    public static EditorProxy putFloat(String key, float value) {
        return edit().putFloat(key, value);
    }

    public static float getFloat(String key, float defValue) {
        return edit().getFloat(key, defValue);
    }

    public static float getFloat(String key) {
        return edit().getFloat(key);
    }

    // ----------double

    public static EditorProxy putDouble(String key, double value) {
        return edit().putDouble(key, value);
    }

    public static double getDouble(String key, double defValue) {
        return edit().getDouble(key, defValue);
    }

    public static double getDouble(String key) {
        return edit().getDouble(key);
    }

    // ----------boolean

    public static EditorProxy putBoolean(String key, boolean value) {
        return edit().putBoolean(key, value);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return edit().getBoolean(key, defValue);
    }

    public static boolean getBoolean(String key) {
        return edit().getBoolean(key);
    }

    // ----------String

    public static EditorProxy putString(String key, String value) {
        return edit().putString(key, value);
    }

    public static String getString(String key, String defValue) {
        return edit().getString(key, defValue);
    }

    public static String getString(String key) {
        return edit().getString(key);
    }

    // ----------byte[]

    public static EditorProxy putBytes(String key, byte[] value) {
        return edit().putBytes(key, value);
    }

    public static byte[] getBytes(String key, byte[] defValue) {
        return edit().getBytes(key, defValue);
    }

    public static byte[] getBytes(String key) {
        return edit().getBytes(key);
    }

    // ---------- String[]

    public static EditorProxy putStringArray(String key, String[] value) {
        return edit().putStringArray(key, value);
    }

    public static String[] getStringArray(String key, String[] defValue) {
        return edit().getStringArray(key, defValue);
    }

    public static String[] getStringArray(String key) {
        return edit().getStringArray(key);
    }

    // ---------- Set<String>

    public static EditorProxy putStringSet(String key, Set<String> value) {
        return edit().putStringSet(key, value);
    }

    public static Set<String> getStringSet(String key, Set<String> defValue) {
        return edit().getStringSet(key, defValue);
    }

    public static Set<String> getStringSet(String key) {
        return edit().getStringSet(key);
    }

    // ########################### 存储对象数据类型 #################################################

    // ------- Parcelable

    public static EditorProxy putParcelable(String key, Parcelable value) {
        return edit().putParcelable(key, value);
    }

    public static <T extends Parcelable> T getParcelable(String key, Class<T> clazz) {
        return edit().getParcelable(key, clazz);
    }

    public static <T extends Parcelable> T getParcelable(String key) {
        return edit().getParcelable(key);
    }

    // ------- Serializable

    public static <T extends Serializable> EditorProxy putSerializable(String key, T value) {
        return edit().putSerializable(key, value);
    }

    public static <T> T getSerializable(String key, Class<T> clazz) {
        return edit().getSerializable(key, clazz);
    }

    // ------- BigDecimal

    public static EditorProxy putBigDecimal(String key, BigDecimal value) {
        return edit().putBigDecimal(key, value);
    }

    public static BigDecimal getBigDecimal(String key, BigDecimal defValue) {
        return edit().getBigDecimal(key, defValue);
    }

    public static BigDecimal getBigDecimal(String key) {
        return edit().getBigDecimal(key);
    }

    // ------- JSONObject

    public static EditorProxy putJSONObject(String key, JSONObject value) {
        return edit().putJSONObject(key, value);
    }

    public static JSONObject getJSONObject(String key, JSONObject defValue) {
        return edit().getJSONObject(key, defValue);
    }

    public static JSONObject getJSONObject(String key) {
        return edit().getJSONObject(key);
    }

    // ------- JSONArray

    public static EditorProxy putJSONArray(String key, JSONArray value) {
        return edit().putJSONArray(key, value);
    }

    public static JSONArray getJSONArray(String key, JSONArray defValue) {
        return edit().getJSONArray(key, defValue);
    }

    public static JSONArray getJSONArray(String key) {
        return edit().getJSONArray(key);
    }

    // ------- List

    public static <T> EditorProxy putList(String key, List<T> value) {
        return edit().putList(key, value);
    }

    public static <T> List<T> getList(String key, List<T> defValue) {
        return edit().getList(key, defValue);
    }

    public static <T> List<T> getList(String key) {
        return edit().getList(key);
    }

    // ------- Map

    public static <K, V> EditorProxy putMap(String key, Map<K, V> value) {
        return edit().putMap(key, value);
    }

    public static <K, V> Map<K, V> getMap(String key, Map<K, V> defValue) {
        return edit().getMap(key, defValue);
    }

    public static <K, V> Map<K, V> getMap(String key) {
        return edit().getMap(key, null);
    }
}