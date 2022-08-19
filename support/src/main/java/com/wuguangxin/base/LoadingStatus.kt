package com.wuguangxin.base

/**
 * 加载状态。
 *
 * Created by wuguangxin on 16/10/13
 */
object LoadingStatus {
    /**
     * 请求开始
     */
    const val START = 100

    /**
     * 文件大小
     */
    const val COUNT = 110

    /**
     * 下载进度
     */
    const val PROGRESS = 111

    /**
     * 请求成功
     */
    const val SUCCESS = 200

    /**
     * 请求取消
     */
    const val CANCEL = 300

    /**
     * 请求失败
     */
    const val FAILURE = 400

    /**
     * 请求完成
     */
    const val FINISH = 900
}