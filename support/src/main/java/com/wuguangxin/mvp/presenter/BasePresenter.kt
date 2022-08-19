package com.wuguangxin.mvp.presenter

import com.wuguangxin.mvp.IModel
import com.wuguangxin.mvp.IView
import com.wuguangxin.mvp.IPresenter
import com.wuguangxin.base.LoadingStatus

/**
 * Presenter的基础实现类，实现了公共方法，子类无需在实现。
 * Created by wuguangxin on 2016-08-26.
 */
abstract class BasePresenter<M : IModel, V : IView>(view: V) : IPresenter<M, V> {
    private val mModel: M = newModel()
    private val mView = view
    private var data: Any? = null

    open fun getModel(): M = mModel

    open fun getView(): V = mView

    open fun isPull(): Boolean = true

    open fun isCached(): Boolean = data != null

    override fun setLoadingStatus(loadingStatus: Int) {
        getView().setLoadingStatus(loadingStatus, isPull(), isCached())
    }

    override fun showLoading() {
        getView().setLoadingStatus(LoadingStatus.START, isPull(), isCached())
    }

    override fun hideLoading() {
        getView().setLoadingStatus(LoadingStatus.FINISH, isPull(), isCached())
    }

    override fun onStart() {
        showLoading()
    }

    override fun onSuccess(data: Any?, key: String?) {
        this.data = data
        setLoadingStatus(LoadingStatus.SUCCESS)
        getView().onFinish()
    }

    override fun onFailure(msg: String?) {
        setLoadingStatus(LoadingStatus.FAILURE)
        getView().showToast(msg)
        getView().onFinish()
    }

    override fun onFinish() {
        setLoadingStatus(LoadingStatus.FINISH)
        getView().onFinish()
    }

    override fun onDestroy() {
    }
}