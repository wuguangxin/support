package com.wuguangxin.simple.mvp

import com.wuguangxin.mvp.presenter.BasePresenter
import com.wuguangxin.simple.bean.UserBean

/**
 * 登录
 * Created by wuguangxin on 17/5/9.
 */
class LoginPresenter(view: LoginContract.View) :
    BasePresenter<LoginContract.Model, LoginContract.View>(view),
    LoginContract.Presenter,
    LoginContract.Callback {

    override fun newModel(): LoginContract.Model {
        return LoginModel()
    }

    override fun login(username: String, password: String) {
        getModel().login(username, password, this)
    }

    override fun onSuccessLogin(userBean: UserBean?) {
        getView().onSuccessLogin(userBean)
        getView().onFinish()
    }

    override fun isPull(): Boolean {
        return true
    }
}