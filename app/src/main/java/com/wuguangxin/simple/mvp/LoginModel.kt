package com.wuguangxin.simple.mvp

import android.text.TextUtils
import com.wuguangxin.simple.bean.UserBean
import com.wuguangxin.simple.constans.Constants
import com.wuguangxin.utils.MD5

/**
 * 登录接口模型
 * Created by wuguangxin on 17/4/14.
 */
class LoginModel : LoginContract.Model {

    override fun login(username: String, password: String, callback: LoginContract.Callback) {
        callback.onStart()

        // 以下模拟server接口处理
        if (TextUtils.isEmpty(username)) {
            callback.onFailure("用户名不能为空")
            return
        }
        if (TextUtils.isEmpty(password)) {
            callback.onFailure("密码不能为空")
            return
        }
        var userBean: UserBean? = null

        for (user in Constants.userList) {
            if (username == user.username) {
                userBean = user
                break
            }
        }
        if (userBean == null || MD5.encode(password) != userBean.password) {
            callback.onFailure("用户名或密码不正确")
            return
        }
        val resultUser = UserBean(username, null, userBean.realName, userBean.sex, userBean.age)
        callback.onSuccessLogin(resultUser)

        callback.onFinish()
    }
}