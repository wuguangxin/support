package com.wuguangxin.view.titlebar;

import android.graphics.Color;

import com.wuguangxin.support.R;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class TitleTheme {

    private int textColor = Color.parseColor("#1A1A1A");
    private int backIcon = R.drawable.title_back_black;
    private int menuIcon = R.drawable.title_share_black;
    private int backgroundColor = Color.WHITE;
    private boolean statusBarDark = true;
    private boolean showDivider = false; // 标题栏分割线是否可见
    private boolean showMenu = false; // 右侧菜单按钮是否可见
    private boolean showBack = true; // 返回按钮是否可见
    private int dividerColor = Color.parseColor("#EFEFEF"); // 标题栏分割线颜色

    public TitleTheme() {
    }

    public TitleTheme(TitleTheme theme) {
        this.textColor = theme.textColor;
        this.backIcon = theme.backIcon;
        this.menuIcon = theme.menuIcon;
        this.dividerColor = theme.dividerColor;
        this.backgroundColor = theme.backgroundColor;
        this.statusBarDark = theme.statusBarDark;
        this.showDivider = theme.showDivider;
    }

    @NonNull
    @Override
    public TitleTheme clone() {
        //super.clone();
        return new TitleTheme(this);
    }

    /**
     * 白色主题，背景不透明
     */
    public static final TitleTheme WHITE = new TitleTheme()
            .setStatusBarDark(false)
            .setTextColor(Color.WHITE)
            .setDividerColor(0xFFEFEFEF)
            .setBackgroundColor(Color.GRAY)
            .setBackIcon(R.drawable.title_back_white)
            .setMenuIcon(R.drawable.title_share_white);

    /**
     * 白色主题，背景透明
     */
    public static final TitleTheme WHITE_TRANSPARENT = new TitleTheme()
            .setStatusBarDark(false)
            .setTextColor(Color.WHITE)
            .setDividerColor(0xFFEFEFEF)
            .setBackgroundColor(Color.TRANSPARENT)
            .setBackIcon(R.drawable.title_back_white)
            .setMenuIcon(R.drawable.title_share_white);

    /**
     * 黑色主题
     */
    public static final TitleTheme BLACK = new TitleTheme()
            .setStatusBarDark(true)
            .setTextColor(Color.BLACK)
            .setDividerColor(0xFFEFEFEF)
            .setBackgroundColor(Color.WHITE)
            .setBackIcon(R.drawable.title_back_black)
            .setMenuIcon(R.drawable.title_share_black);

    /**
     * 黑色主题
     */
    public static final TitleTheme BLACK_TRANSPARENT = new TitleTheme()
            .setStatusBarDark(true)
            .setTextColor(Color.BLACK)
            .setDividerColor(0xFFEFEFEF)
            .setBackgroundColor(Color.TRANSPARENT)
            .setBackIcon(R.drawable.title_back_black)
            .setMenuIcon(R.drawable.title_share_black);

    public static TitleTheme findTheme(int themeType, TitleTheme def) {
        switch (themeType) {
            case 0: return TitleTheme.WHITE.clone();
            case 1: return TitleTheme.BLACK.clone();
            case 2: return TitleTheme.WHITE_TRANSPARENT.clone();
            case 3: return TitleTheme.BLACK_TRANSPARENT.clone();
        }
        return def;
    }


    public @ColorInt int getTextColor() {
        return textColor;
    }

    public TitleTheme setTextColor(@ColorInt int color) {
        this.textColor = color;
        return this;
    }

    public @ColorInt int getBackgroundColor() {
        return backgroundColor;
    }

    public TitleTheme setBackIcon(@DrawableRes int resId) {
        this.backIcon = resId;
        return this;
    }

    public @DrawableRes int getMenuIcon() {
        return menuIcon;
    }

    public TitleTheme setShowBack(boolean showBack) {
        this.showBack = showBack;
        return this;
    }

    public boolean isShowBack() {
        return showBack;
    }

    public TitleTheme setMenuIcon(@DrawableRes int resId) {
        this.menuIcon = resId;
        return this;
    }

    public TitleTheme setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
        return this;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public boolean isStatusBarDark() {
        return statusBarDark;
    }

    public TitleTheme setStatusBarDark(boolean statusBarDark) {
        this.statusBarDark = statusBarDark;
        return this;
    }

    public boolean isShowDivider() {
        return showDivider;
    }

    public TitleTheme setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
        return this;
    }

    public TitleTheme setDividerColor(@ColorInt int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public TitleTheme setBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        return this;
    }

    public @DrawableRes int getBackIcon() {
        return backIcon;
    }

}