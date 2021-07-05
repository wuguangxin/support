package com.wuguangxin.simple.bean;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

/**
 * <p>游戏信息
 * <p>com\wuguangxin\simple\bean\GameDataBean.java
 * <p>Created by wuguangxin on 2021-07-06 0:11
 */
public class GameDataBean {

    public String text;
    public boolean checked;
    public int checkIndex;
    public boolean enable = true;
    public @ColorInt int color = Color.GRAY;

    private GameDataBean() {
    }

    public GameDataBean(String text, @ColorInt int color) {
        this.text = text;
        this.color = color;
    }

}