package com.wuguangxin.simple.bean;

import java.io.Serializable;

/**
 * <p>游戏记录信息
 * <p>com\wuguangxin\simple\bean\GameDataBean.java
 * <p>Created by wuguangxin on 2021-07-06 0:11
 */
public class GameRecordBean implements Serializable {

    public int r; // red
    public int g; // green
    public int b; // blue
    public String result;

    public long createTime; // 记录时间

    public GameRecordBean(int r, int g, int b, String result) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.result = result;
        this.createTime = System.currentTimeMillis();
    }
}