package com.wuguangxin.simple.bean

import androidx.annotation.ColorInt

/**
 *
 * 游戏信息
 *
 * com\wuguangxin\simple\bean\GameDataBean.java
 *
 * Created by wuguangxin on 2021-07-06 0:11
 */
data class GameDataBean(
    val text: String,
    @ColorInt val color: Int,
){
    var checked = false
    var checkIndex = 0
    var enable = true
}