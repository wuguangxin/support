package com.wuguangxin.simple.retrofit;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 封装请求参数的HashMap便捷类
 * Created by wuguangxin on 14/10/13
 */
public class Params extends HashMap<String, Object> {
    private static final long serialVersionUID = 3728066264475707716L;

    /**
     * 获取value
     * @param key key
     * @return value
     */
    public String optString(String key) {
        return optString(key, null);
    }

    /**
     * 获取 String value
     * @param key key
     * @param defValue 默认 String value
     * @return String value
     */
    public String optString(String key, String defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        Object obj = get(key);
        if (obj != null) {
            return (String) obj;
        }
        return defValue;
    }

    /**
     * 获取 int value
     * @param key key
     * @return int value
     */
    public int optInt(String key) {
        return optInt(key, 0);
    }

    /**
     * 获取int value
     * @param key key
     * @param defValue 默认 int value
     * @return int value
     */
    public int optInt(String key, int defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        Object obj = get(key);
        if (obj != null) {
            return Integer.parseInt(obj.toString());
        }
        return defValue;
    }

    /**
     * 获取URL参数串（默认保留最后的&）
     */
    public String getUrlParams() {
        if (isEmpty())
            return "";
        Uri.Builder builder = new Uri.Builder();
        for (String key : keySet()) {
            if (key != null && !key.isEmpty()) {
                builder.appendQueryParameter(key, String.valueOf(get(key)));
            }
        }
        return builder.toString();
    }

    /**
     * 转换为JSONString
     */
    public String toJSONString() {
        return toJSONObject().toString();
    }

    /**
     * 转换为JSONObject
     */
    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    /**
     * 为了方便，客户端使用OBJECT作为Params的Value的泛型，再给第三方传时，再转换成 Map<String, String>
     */
    public static HashMap<String, String> valueToString(Params params){
        HashMap<String, String> maps = new HashMap<>();
        if (params != null) {
            for (String key : params.keySet()) {
                maps.put(key, String.valueOf(params.get(key)));
            }
        }
        return maps;
    }
}
