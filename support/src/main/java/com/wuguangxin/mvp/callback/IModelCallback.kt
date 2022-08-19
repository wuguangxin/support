package com.wuguangxin.mvp.callback

/**
 * MVP架构中V和P层的基础回调接口。
 * 泛型D：为返回的数据类型
 * Created by wuguangxin on 2016-10-08.
 */
interface IModelCallback<D> {
    /**
     * 是否是拉取数据
     */
//    fun isPull(): Boolean = true

    /**
     * 是否缓存过数据
     */
//    fun isCached(): Boolean = false

    /**
     * 当成请求开始时回调。
     */
    fun onStart()

    /**
     * 当成请求失败时回调。
     * @param msg String类型，错误消息
     */
    fun onFailure(msg: String?)

    /**
     * 当一个请求结束时回调
     */
    fun onFinish()

    /**
     * 销毁
     */
    fun onDestroy()
}