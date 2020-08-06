package com.wuguangxin.simple.listener;

import android.app.Dialog;

/**
 * 加载对话框监听接口
 *
 * Created by wuguangxin on 17/5/19.
 */
public interface LoadingListener {

    /**
     * 设置加载状态， <br>
     * 1.如果要加载的数据在本地没有缓存，则显示加载中的提示对话框，<br>
     * 2.如果已有缓存，则加载时，只在标题左边显示转圈的动画。<br>
     *
     * @param state 请求服务器结果状态（START、SUCCESS、FAILURE）
     * @param isCache 是否有缓存
     */
    void setLoadingStatus(int state, boolean isCache);

    /**
     * 设置加载状态， <br>
     * 1.如果要加载的数据在本地没有缓存，则显示加载中的提示对话框，<br>
     * 2.如果已有缓存，则加载时，只在标题左边显示转圈的动画。<br>
     *
     * @param state 请求服务器结果状态（START、SUCCESS、FAILURE）
     * @param isGet 获取数据
     * @param isEmpty 当前数据是否为空
     */
    void setLoadingStatus(int state, boolean isGet, boolean isEmpty);

    /**
     * 控制“加载中”的对话框显示状态
     * @param visible 是否显示
     */
    void setLoadingDialogVisible(boolean visible);

    /**
     * 控制标题栏上的加载动画
     * @param visible 是否显示
     */
    void setTitleLoadingProgressVisible(boolean visible);

    /**
     * 关闭对话框
     */
    void dismissDialog();

    /**
     * 关闭对话框
     * @param dialogs 对话框集
     */
    void dismissDialog(Dialog... dialogs);
}
