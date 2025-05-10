package com.wuguangxin.view.titlebar;

import android.graphics.Color;

import com.wuguangxin.support.R;

public enum TitleBarThemeBAK {
    /**
     * 白色主题，背景不透明
     */
    WHITE(new TitleTheme()
            .setStatusBarDark(false)
            .setTextColor(Color.WHITE)
            .setDividerColor(0xFFEFEFEF)
            .setBackgroundColor(Color.BLACK)
            .setBackIcon(R.drawable.title_back_white)
            .setMenuIcon(R.drawable.title_share_white)),

    /**
     * 白色主题，背景透明
     */
    WHITE_TRANSPARENT(new TitleTheme()
            .setStatusBarDark(false)
            .setTextColor(Color.WHITE)
            .setDividerColor(0xFFEFEFEF)
            .setBackgroundColor(Color.TRANSPARENT)
            .setBackIcon(R.drawable.title_back_white)
            .setMenuIcon(R.drawable.title_share_white)),

    /**
     * 黑色主题
     */
    BLACK(new TitleTheme()
            .setStatusBarDark(true)
            .setTextColor(Color.BLACK)
            .setDividerColor(0xFFEFEFEF)
            .setBackgroundColor(Color.WHITE)
            .setBackIcon(R.drawable.title_back_black)
            .setMenuIcon(R.drawable.title_share_black)),

    /**
     * 黑色主题
     */
    BLACK_TRANSPARENT(new TitleTheme()
            .setStatusBarDark(true)
            .setTextColor(Color.BLACK)
            .setDividerColor(0xFFEFEFEF)
            .setBackgroundColor(Color.TRANSPARENT)
            .setBackIcon(R.drawable.title_back_black)
            .setMenuIcon(R.drawable.title_share_black));

    public TitleTheme config;

    TitleBarThemeBAK(TitleTheme config) {
        this.config = config;
    }

}