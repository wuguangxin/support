package com.wuguangxin.mvp.ui

import androidx.databinding.ViewDataBinding
import com.wuguangxin.mvp.presenter.BasePresenter
import com.wuguangxin.mvp.IModel
import com.wuguangxin.mvp.IView
import com.wuguangxin.ui.XinBaseActivity

/**
 * MVP 中 Activity基类
 * Created by wuguangxin on 2015/4/1
 */
abstract class XinMVPActivity<B : ViewDataBinding, P : BasePresenter<out IModel, out IView>?> : XinBaseActivity<B>() {
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