package com.wuguangxin.mvp

/**
 * MVP架构的基础接口-Presenter
 * M: extends IModel
 * V: extends IView
 *
 * Created by wuguangxin on 2016-08-26.
 */
interface IPresenter<M : IModel, V : IView> {
    /**
     * 创建M层的实现类实例，不能多次调用。如果一个P有多个model，可以在P的实现类里创建另外的model即可
     * @return 返回 BaseModel 的实现类
     */
    fun newModel(): M

    /**
     * 获取 Model层 的接口实现类对象
     * @return
     */
//    fun getModel(): M

    /**
     * 获取 View层 的接口实现类对象
     * @return
     */
//    fun getView(): V
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    /**
     * 是否是拉取数据
     * @return
     */
//    open fun isPull(): Boolean = true

    /**
     * 是否缓存了数据
     * @return
     */
//    fun isCached(): Boolean = false

    /**
     * 当成请求开始时回调。
     */
    fun onStart()

    /**
     * 当请求成功时回调
     * @param data 返回的数据
     * @param key 请求接口地址的MD5值
     */
    fun onSuccess(data: Any?, key: String?)

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
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    /**
     * 设置加载对话框显示/隐藏
     * @param loadingStatus 加载状态
     */
    fun setLoadingStatus(loadingStatus: Int)

    /**
     * 显示加载对话框
     */
    fun showLoading()

    /**
     * 隐藏加载对话框就
     */
    fun hideLoading()
}