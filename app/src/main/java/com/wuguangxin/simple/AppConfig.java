package com.wuguangxin.simple;

import com.wuguangxin.utils.mmkv.Config;
import com.wuguangxin.utils.mmkv.EditorProxy;


public class AppConfig extends Config {
    // 可使用单例
    private AppConfig() {
    }

    private static AppConfig instance;

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    public EditorProxy setGameRecordList(String gameRecordListJson) {
        return putString("game_record_list", gameRecordListJson);
    }

    public String getGameRecordList() {
        return getString("game_record_list");
    }

}