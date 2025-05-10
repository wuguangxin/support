package com.wuguangxin.view.titlebar;

import android.graphics.Color;

import com.wuguangxin.support.R;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

/**
 * 标题栏配置
 */
public class TitleBarConfigBAK {

    private int textColor = Color.parseColor("#1A1A1A");
    private int divider = Color.parseColor("#EFEFEF");
    private int backIcon = R.drawable.title_back_black;
    private int menuIcon = R.drawable.title_share_black;
    private int backgroundColor = Color.WHITE;
    private boolean statusBarDark = true;
    private boolean dividerVisible = false; // 标题栏分割线是否可见

    /**
     * 白色主题，背景不透明
     */
    TitleBarConfigBAK WHITE = new TitleBarConfigBAK()
            .setStatusBarDark(false)
            .setTextColor(Color.WHITE)
            .setDivider(0xFFEFEFEF)
            .setBackgroundColor(Color.BLACK)
            .setBackIcon(R.drawable.title_back_white)
            .setMenuIcon(R.drawable.title_share_white);

    /**
     * 白色主题，背景透明
     */
    TitleBarConfigBAK WHITE_TRANSPARENT = new TitleBarConfigBAK()
            .setStatusBarDark(false)
            .setTextColor(Color.WHITE)
            .setDivider(0xFFEFEFEF)
            .setBackgroundColor(Color.TRANSPARENT)
            .setBackIcon(R.drawable.title_back_white)
            .setMenuIcon(R.drawable.title_share_white);

    /**
     * 黑色主题
     */
    TitleBarConfigBAK BLACK = new TitleBarConfigBAK()
            .setStatusBarDark(true)
            .setTextColor(Color.BLACK)
            .setDivider(0xFFEFEFEF)
            .setBackgroundColor(Color.WHITE)
            .setBackIcon(R.drawable.title_back_black)
            .setMenuIcon(R.drawable.title_share_black);

    /**
     * 黑色主题
     */
    TitleBarConfigBAK BLACK_TRANSPARENT = new TitleBarConfigBAK()
            .setStatusBarDark(true)
            .setTextColor(Color.BLACK)
            .setDivider(0xFFEFEFEF)
            .setBackgroundColor(Color.TRANSPARENT)
            .setBackIcon(R.drawable.title_back_black)
            .setMenuIcon(R.drawable.title_share_black);


    public @ColorInt int getTextColor() {
        return textColor;
    }

    public TitleBarConfigBAK setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        return this;
    }

    public @ColorInt int getDivider() {
        return divider;
    }

    public TitleBarConfigBAK setDivider(@ColorInt int divider) {
        this.divider = divider;
        return this;
    }

    public @ColorInt int getBackgroundColor() {
        return backgroundColor;
    }

    public TitleBarConfigBAK setBackIcon(@DrawableRes int backIcon) {
        this.backIcon = backIcon;
        return this;
    }

    public @DrawableRes int getMenuIcon() {
        return menuIcon;
    }

    public TitleBarConfigBAK setMenuIcon(@DrawableRes int menuIcon) {
        this.menuIcon = menuIcon;
        return this;
    }

    public boolean isStatusBarDark() {
        return statusBarDark;
    }

    public TitleBarConfigBAK setStatusBarDark(boolean statusBarDark) {
        this.statusBarDark = statusBarDark;
        return this;
    }

    public boolean isDividerVisible() {
        return dividerVisible;
    }

    public TitleBarConfigBAK setDividerVisible(boolean dividerVisible) {
        this.dividerVisible = dividerVisible;
        return this;
    }

    public TitleBarConfigBAK setBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        return this;
    }

    public @DrawableRes int getBackIcon() {
        return backIcon;
    }

    //    public TitleBarConfig setTextColor(@ColorInt int color) {
//        this.textColor = color;
//        return this;
//    }
//
//    public TitleBarConfig setBackIcon(@DrawableRes int resId) {
//        this.backIcon = resId;
//        return this;
//    }
//
//    public TitleBarConfig setMenuIcon(@DrawableRes int resId) {
//        this.menuIcon = resId;
//        return this;
//    }
//
//    public TitleBarConfig setStatusBarDark(boolean dark) {
//        this.statusBarDark = dark;
//        return this;
//    }
//
//    public TitleBarConfig setDivider(@ColorInt int color) {
//        this.divider = color;
//        return this;
//    }
//
//    public TitleBarConfig setBackground(@ColorInt int color) {
//        this.background = color;
//        return this;
//    }
//
//    public TitleBarConfig setDividerVisible(boolean visible) {
//        this.dividerVisible = visible;
//        return this;
//    }
//
//    public boolean isDividerVisible() {
//        return dividerVisible;
//    }
//
//    public boolean isStatusBarDark() {
//        return statusBarDark;
//    }
//
//    public @ColorInt int getBackground() {
//        return background;
//    }
//
//
//    public @ColorInt int getDivider() {
//        return divider;
//    }
//
//    public @ColorInt
//    int getTextColor() {
//        return textColor;
//    }
//
//
//    public @DrawableRes
//    int getBackIcon() {
//        return backIcon;
//    }
//
//    public @DrawableRes
//    int getMenuIcon() {
//        return menuIcon;
//    }

}