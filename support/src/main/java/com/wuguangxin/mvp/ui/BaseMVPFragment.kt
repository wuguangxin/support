package com.wuguangxin.mvp.ui

import com.wuguangxin.mvp.presenter.BasePresenter
import com.wuguangxin.mvp.IModel
import com.wuguangxin.mvp.IView
import com.wuguangxin.ui.XinBaseFragment
import androidx.databinding.ViewDataBinding

/**
 * 基础Fragment
 * Created by wuguangxin on 16/8/26
 */
abstract class BaseMVPFragment<B : ViewDataBinding, P : BasePresenter<out IModel, out IView>?> : XinBaseFragment() {
    val presenter: P? = newPresenter()

    /**
     * 创建 Presenter
     */
    abstract fun newPresenter(): P?

    override fun onDestroy() {
        presenter?.onDestroy()
        super.onDestroy()
    }
}