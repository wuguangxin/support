package com.wuguangxin.simple.mvp;

import com.wuguangxin.mvp.presenter.AbsBasePresenter;
import com.wuguangxin.simple.bean.UserBean;

/**
 * 登录
 * Created by wuguangxin on 17/5/9.
 */
public class LoginPresenter extends AbsBasePresenter<LoginContract.Model, LoginContract.View>
        implements LoginContract.Presenter, LoginContract.Callback {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public LoginContract.Model newModel() {
        return new LoginModel();
    }

    @Override
    public boolean isPull() {
        return true;
    }

    @Override
    public void login(String username, String password) {
        getModel().login(username, password, this);
    }

    @Override
    public void onSuccessLogin(UserBean userBean) {
        getView().onSuccessLogin(userBean);
        getView().onFinish();
    }
}
