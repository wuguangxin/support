package com.wuguangxin.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;

/**
 * Attr资源管理工具类。
 * Created by wuguangxin on 2019-12-15.
 */
public class AttrUtils {

    /**
     * 根据给定的attr资源ID获取颜色, 默认返回黑色 Color.BLACK
     * @param context 上下文
     * @param attrRes attr资源ID，如 android.R.attr.background
     * @return
     */
    public static int getColor(Context context, @AttrRes int attrRes) {
        return AttrUtils.getColor(context, attrRes, Color.BLACK);
    }

    /**
     * 根据给定的attr资源ID获取颜色
     * @param context 上下文
     * @param attrRes attr资源ID，如 android.R.attr.background
     * @param defColor 默认颜色值，如 android.R.color.red
     * @return
     */
    public static int getColor(Context context, @AttrRes int attrRes, @ColorInt int defColor) {
        int color = defColor;
        int[] attribute = new int[] { attrRes };
        TypedArray type = context.obtainStyledAttributes(attribute);
        color = type.getColor(0, color);
        type.recycle();
        return color;
    }


    /**
     * 根据给定的attr资源ID获取Drawable
     * @param context 上下文
     * @param attrRes attr资源ID，如 android.R.attr.background
     * @return
     */
    public static Drawable getDrawable(Context context, @AttrRes int attrRes) {
        int[] attribute = new int[] { attrRes };
        TypedArray type = context.obtainStyledAttributes(attribute);
        Drawable drawable = type.getDrawable(0);
        type.recycle();
        return drawable;
    }
}
