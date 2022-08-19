package com.wuguangxin.simple.bean

/**
 * 游戏记录信息
 *
 * Created by wuguangxin on 2021-07-06 0:11
 */
data class GameRecordBean(
    val r: Int,
    val g: Int,
    val b: Int,
    val result: String,
) {
    var createTime = System.currentTimeMillis() // 记录时间
}