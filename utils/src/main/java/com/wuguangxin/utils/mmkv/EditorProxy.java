package com.wuguangxin.utils.mmkv;

import android.os.Parcelable;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通过 EditorProxy (编辑代理类) 来实现更方便更快捷的数据存取操作。
 */
public abstract class EditorProxy {

    /** 获取MMKV实例 */
    public abstract MMKV edit();

    protected abstract String toJson(Object obj);

    protected abstract <T> T fromJson(String json, Type type);

    protected abstract <T> T fromJson(String json, Class<T> clazz);

    ////////////////////////////////////////////////////////////

    /**
     * 重置加密（AES）秘钥
     *
     * @param encryptKey 秘钥
     * @return
     */
    public boolean reKey(String encryptKey) {
        return edit().reKey(encryptKey);
    }

    /**
     * 清除加密，并使数据明文。
     *
     * @return
     */
    public boolean clearKey() {
        return edit().reKey(null); // 设置null即可
    }

    /** 删除指定key对应的value */
    public void removeValueForKey(String key) {
        edit().removeValueForKey(key);
    }

    /** 删除指定多个key对应的value */
    public void removeValuesForKeys(String... keys) {
        edit().removeValuesForKeys(keys);
    }

    /** 是否包含指定的key */
    public boolean containsKey(String key) {
        return edit().containsKey(key);
    }

    // ------------------------------------ PUT ------------------------------------

    public EditorProxy put(String key, int value) {
        putInt(key, value);
        return this;
    }

    public EditorProxy put(String key, long value) {
        putLong(key, value);
        return this;
    }

    public EditorProxy put(String key, float value) {
        putFloat(key, value);
        return this;
    }

    public EditorProxy put(String key, byte[] value) {
        putBytes(key, value);
        return this;
    }

    public EditorProxy put(String key, double value) {
        putDouble(key, value);
        return this;
    }

    public EditorProxy put(String key, boolean value) {
        putBoolean(key, value);
        return this;
    }

    public EditorProxy put(String key, String value) {
        putString(key, value);
        return this;
    }

    public EditorProxy put(String key, String[] value) {
        putStringArray(key, value);
        return this;
    }

    public EditorProxy put(String key, Serializable value) {
        putSerializable(key, value);
        return this;
    }

    public EditorProxy put(String key, Parcelable value) {
        putParcelable(key, value);
        return this;
    }

    public  EditorProxy put(String key, Set<String> value) {
        putStringSet(key, value);
        return this;
    }

    public <T> EditorProxy put(String key, List<T> value) {
        putList(key, value);
        return this;
    }

    public <K, V> EditorProxy put(String key, Map<K, V> value) {
        putMap(key, value);
        return this;
    }

    // ------------------------------------ GET ------------------------------------

    public int get(String key, int defValue) {
        return getInt(key, defValue);
    }

    public long get(String key, long defValue) {
        return getLong(key, defValue);
    }

    public float get(String key, float defValue) {
        return getFloat(key, defValue);
    }

    public byte[] get(String key, byte[] defValue) {
        return getBytes(key, defValue);
    }

    public double get(String key, double defValue) {
        return getDouble(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return getBoolean(key, defValue);
    }

    public String get(String key, String defValue) {
        return getString(key, defValue);
    }

    public String[] get(String key, String[] defValue) {
        return getStringArray(key, defValue);
    }

    public Set<String> get(String key, Set<String> defValue) {
        return getStringSet(key, defValue);
    }

    // ------------------------------------ put/get XXX ------------------------------------

    // ----------int

    public EditorProxy putInt(String key, int value) {
        edit().encode(key, value);
        return this;
    }

    public int getInt(String key, int defValue) {
        return edit().decodeInt(key, defValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    // ----------long

    public EditorProxy putLong(String key, long value) {
        edit().encode(key, value);
        return this;
    }

    public long getLong(String key, long defValue) {
        return edit().decodeLong(key, defValue);
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    // ----------float

    public EditorProxy putFloat(String key, float value) {
        edit().encode(key, value);
        return this;
    }

    public float getFloat(String key, float defValue) {
        return edit().decodeFloat(key, defValue);
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0F);
    }

    // ----------double

    public EditorProxy putDouble(String key, double value) {
        edit().encode(key, value);
        return this;
    }

    public double getDouble(String key, double defValue) {
        return edit().decodeDouble(key, defValue);
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0D);
    }

    // ----------boolean

    public EditorProxy putBoolean(String key, boolean value) {
        edit().encode(key, value);
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return edit().decodeBool(key, defValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    // ----------String

    public EditorProxy putString(String key, String value) {
        edit().encode(key, value);
        return this;
    }

    public String getString(String key, String defValue) {
        return edit().decodeString(key, defValue);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    // ----------byte[]

    public EditorProxy putBytes(String key, byte[] value) {
        edit().encode(key, value);
        return this;
    }

    public byte[] getBytes(String key, byte[] defValue) {
        return edit().decodeBytes(key, defValue);
    }

    public byte[] getBytes(String key) {
        return getBytes(key, null);
    }

    // ---------- String[]

    public EditorProxy putStringArray(String key, String[] value) {
        Set<String> set = new HashSet<>(); // 转为set来存储
        Collections.addAll(set, value);
        edit().encode(key, set);
        return this;
    }

    public String[] getStringArray(String key, String[] defValue) {
        Set<String> set = getStringSet(key);
        if (set != null) {
            return set.toArray(new String[0]);
        }
        return defValue;
    }

    public String[] getStringArray(String key) {
        return getStringArray(key, null);
    }

    // ---------- Set<String>

    public EditorProxy putStringSet(String key, Set<String> value) {
        edit().encode(key, value);
        return this;
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return edit().getStringSet(key, defValue);
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, null);
    }

    // ########################### 存储对象数据类型 #################################################

    // ------- Parcelable

    public EditorProxy putParcelable(String key, Parcelable value) {
        edit().encode(key, value);
        return this;
    }

    public <T extends Parcelable> T getParcelable(String key, Class<T> clazz) {
        return edit().decodeParcelable(key, clazz);
    }

    public <T extends Parcelable> T getParcelable(String key) {
        return getParcelable(key, null);
    }

    // ------- Serializable

    public <T extends Serializable> EditorProxy putSerializable(String key, T value) {
        putString(key, toJson(value));
        return this;
    }

    public <T> T getSerializable(String key, Class<T> clazz) {
        if (clazz == null) return null;
        String json = getString(key);
        return TextUtils.isEmpty(json) ? null : fromJson(json, clazz);
    }

    // ------- BigDecimal

    public EditorProxy putBigDecimal(String key, BigDecimal value) {
        putString(key, value == null ? null : value.toString());
        return this;
    }

    public BigDecimal getBigDecimal(String key, BigDecimal defValue) {
        String valueStr = getString(key, null);
        if (!TextUtils.isEmpty(valueStr)) {
            try {
                return new BigDecimal(valueStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    public BigDecimal getBigDecimal(String key) {
        return getBigDecimal(key, null);
    }

    // ------- JSONObject

    public EditorProxy putJSONObject(String key, JSONObject value) {
        putString(key, value == null ? null : value.toString());
        return this;
    }

    public JSONObject getJSONObject(String key, JSONObject defValue) {
        String strValue = getString(key, null);
        if (!TextUtils.isEmpty(strValue)) {
            try {
                return new JSONObject(strValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    public JSONObject getJSONObject(String key) {
        return getJSONObject(key, null);
    }

    // ------- JSONArray

    public EditorProxy putJSONArray(String key, JSONArray value) {
        putString(key, value == null ? null : value.toString());
        return this;
    }

    public JSONArray getJSONArray(String key, JSONArray defValue) {
        String strValue = getString(key, null);
        if (!TextUtils.isEmpty(strValue)) {
            try {
                return new JSONArray(strValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    public JSONArray getJSONArray(String key) {
        return getJSONArray(key, null);
    }

    /**
     * 存储List
     * @param key 键
     * @param value 值
     * @return
     */
    public <T> EditorProxy putList(String key, List<T> value) {
        putString(key, toJson(value));
        return this;
    }

    /**
     * 获取List
     * @param key 键
     * @param defValue 默认值
     * @return
     */
    public <T> List<T> getList(String key, List<T> defValue) {
        String json = getString(key);
        if (!TextUtils.isEmpty(json)) {
            return fromJson(json, (Type) List.class);
        }
        return defValue;
    }

    public <T> List<T> getList(String key) {
        return getList(key, null);
    }

    /**
     * 保存Map
     * @param key 键
     * @param value 值
     * @return
     */
    public <K, V> EditorProxy putMap(String key, Map<K, V> value) {
        putString(key, toJson(value));
        return this;
    }

    /**
     * 获取Map
     * @param key 键
     * @param value 值
     * @return
     */
    public <K, V> Map<K, V> getMap(String key, Map<K, V> defValue) {
        String json = getString(key);
        if (!TextUtils.isEmpty(json)) {
            return fromJson(json, (Type) Map.class);
        }
        return defValue;
    }

    public <K, V> Map<K, V> getMap(String key) {
        return getMap(key, null);
    }
}

