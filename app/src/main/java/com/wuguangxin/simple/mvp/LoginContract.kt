package com.wuguangxin.simple.mvp

import com.wuguangxin.mvp.IModel
import com.wuguangxin.mvp.IPresenter
import com.wuguangxin.mvp.IView
import com.wuguangxin.mvp.callback.IModelCallback
import com.wuguangxin.simple.bean.UserBean

/**
 * 用户 Contract
 *
 * Created by wuguangxin on 2020-04-06.
 */
interface LoginContract {

    interface Presenter : IPresenter<Model, View> {
        fun login(username: String, password: String)
    }

    interface View : IView {
        fun onSuccessLogin(userBean: UserBean?)
    }

    interface Model : IModel {
        fun login(username: String, password: String, callback: Callback)
    }

    interface Callback : IModelCallback<UserBean?> {
        fun onSuccessLogin(userBean: UserBean?)
    }
}