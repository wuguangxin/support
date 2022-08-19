package com.wuguangxin.utils.mmkv;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;

/**
 * 基础配置类
 * Created by wuguangxin on 16/1/28
 */
public class Config extends EditorProxy {

    /**
     * 使用简单实现 示例类
     */
    public static class SimpleConfig extends Config {
        // 可使用单例
        private SimpleConfig() {}
        private static SimpleConfig instance;
        public static SimpleConfig getInstance() {
            if (instance == null) {
                synchronized (SimpleConfig.class) {
                    if (instance == null) {
                        instance = new SimpleConfig();
                    }
                }
            }
            return instance;
        }

        // setter
        public EditorProxy setSimpleName(String simpleName){
            return putString("simpleName", simpleName);
        }

        // getter
        public String getSimpleName() {
            return getString("simpleName");
        }
    }

    private MMKV mmkv;
    private Gson mGson;

    /** 当前唯一标识（比如用户ID） */
    private String id;

    /** 加密key */
    private String cryptKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCryptKey() {
        return cryptKey;
    }

    public void setCryptKey(String cryptKey) {
        this.cryptKey = cryptKey;
    }

    /**
     * 切换用户
     * @param id 用户唯一标识
     */
    public boolean switchUser(String id) {
        if (TextUtils.equals(getId(), id)) {
            return false;
        }
        setId(id);
        destroy();
        edit();
        return true;
    }

    public void destroy() {
        this.mmkv = null;
    }

    @Override
    public MMKV edit() {
        if (mmkv == null) {
            mmkv = createMMKV();
        }
        return mmkv;
    }

    /**
     * 创建mmkv实例
     */
    private MMKV createMMKV() {
        // 设置了用户ID，则使用用户专用的存储实例对象，否则使用默认的
        // mode：单进程 SINGLE_PROCESS_MODE(默认)，多进程 MULTI_PROCESS_MODE
        // cryptKey：加密key

        // 创建指定ID用户的专用实例
        if (!TextUtils.isEmpty(getId())) {
            if (TextUtils.isEmpty(getCryptKey())) {
                return MMKV.mmkvWithID(getId(), MMKV.MULTI_PROCESS_MODE);
            }
            return MMKV.mmkvWithID(getId(), MMKV.MULTI_PROCESS_MODE, getCryptKey());
        }

        // 创建默认的-全局可用实例
        if (!TextUtils.isEmpty(getCryptKey())) {
            return MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, getCryptKey());
        }
        return MMKV.defaultMMKV();
    }

    private Gson gson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }

    @Override
    protected String toJson(Object obj) {
        return gson().toJson(obj);
    }

    @Override
    protected <T> T fromJson(String json, Type type) {
        return gson().fromJson(json, type);
    }

    @Override
    protected <T> T fromJson(String json, Class<T> clazz) {
        return gson().fromJson(json, clazz);
    }
}
