package com.wuguangxin.listener

import android.app.Activity
import android.os.Bundle

/**
 * View的基本接口。（View指 Activity、Fragment）
 * Created by wuguangxin on 17/5/19.
 */
interface BaseInterface {

    /**
     * 获取布局资源ID
     */
    fun getLayoutId(): Int

    /**
     * 初始化View。
     * 在 BaseActivity.onCreate() 中初始化基本数据后被调用
     * @param savedInstanceState
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化监听器。
     * 在 BaseActivity.onCreate() 中 initView() 后被调用
     */
    fun initListener()

    /**
     * 初始化数据。
     * 在 BaseActivity.onStart() 被调用
     */
    fun initData()

    /**
     * 设置数据。BaseActivity中未回调此函数，需要子类手动回调
     */
    fun setData()

    /**
     * 弹出 Toast
     * @param text 文本信息
     */
    fun showToast(text: String)

    /**
     * 打印日志
     * @param text 文本信息
     */
    fun log(text: String)

    /**
     * 打开Activity
     * @param clazz 文本信息
     */
    fun openActivity(clazz: Class<out Activity?>)

    /**
     * 打开Activity
     * @param clazz 目标Activity
     * @param bundle 参数
     */
    fun openActivity(clazz: Class<out Activity?>, bundle: Bundle?)
}