package com.wuguangxin.listener

import android.app.Dialog

/**
 * 加载对话框监听接口
 *
 * Created by wuguangxin on 17/5/19.
 */
interface LoadingListener {
    /**
     * 设置加载状态， <br>
     * 1.如果要加载的数据在本地没有缓存，则显示加载中的提示对话框，<br>
     * 2.如果缓存了数据，则加载时，只在标题左边显示转圈的动画。<br>
     *
     * @param loadingStatus 加载状态，看@[com.wuguangxin.base.LoadingStatus]
     * @param isPull 是否是获取数据
     * @param isCached 是否缓存过数据
     */
    fun setLoadingStatus(loadingStatus: Int, isPull: Boolean, isCached: Boolean)

    /**
     * 控制“加载中”的对话框显示状态
     * @param visible 是否显示
     */
    fun setLoadingDialogVisible(visible: Boolean)

    /**
     * 控制标题栏上的加载动画
     * @param visible 是否显示
     */
    fun setTitleLoadingProgressVisible(visible: Boolean)

    /**
     * 关闭对话框
     */
    fun dismissDialog()

    /**
     * 关闭对话框
     * @param dialogs 对话框集
     */
    fun dismissDialog(vararg dialogs: Dialog?)
}