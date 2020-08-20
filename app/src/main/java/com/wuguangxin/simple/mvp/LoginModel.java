package com.wuguangxin.simple.mvp;

import android.text.TextUtils;

import com.wuguangxin.simple.bean.UserBean;
import com.wuguangxin.simple.constans.Constants;
import com.wuguangxin.utils.MD5;


/**
 * 登录接口模型
 * Created by wuguangxin on 17/4/14.
 */
public class LoginModel implements LoginContract.Model {

    @Override
    public void login(String username, String password, LoginContract.Callback callback) {
        // 以下模拟server接口处理
        if (TextUtils.isEmpty(username)) {
            callback.onFailure("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            callback.onFailure("密码不能为空");
            return;
        }

        UserBean userBean = null;
        for (UserBean user : Constants.userList) {
            if (username.equals(user.username)) {
                userBean = user;
                break;
            }
        }
        if (userBean == null || !MD5.encode(password).equals(userBean.password)) {
            callback.onFailure("用户名或密码不正确");
            return;
        }

        final UserBean resultUser = new UserBean();
        resultUser.setUsername(username);
        resultUser.setRealName(userBean.realName);
        resultUser.setAge(userBean.age);
        resultUser.setSex(userBean.sex);

        callback.onStart();
        callback.onSuccessLogin(resultUser);
        callback.onFinish();
    }
}