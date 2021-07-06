package com.wuguangxin.simple.mvp;

import com.wuguangxin.mvp.IModel;
import com.wuguangxin.mvp.IPresenter;
import com.wuguangxin.mvp.IView;
import com.wuguangxin.mvp.callback.IModelCallback;
import com.wuguangxin.simple.bean.UserBean;

/**
 * 用户 Contract
 * 
 * Created by wuguangxin on 2020-04-06.
 */
public interface LoginContract {
    interface Presenter extends IPresenter<Model, View> {
        void login(String username, String password);
    }

    interface View extends IView {
        void onSuccessLogin(UserBean userBean);
    }

    interface Model extends IModel {
        void login(String username, String password, Callback Callback);
    }

    interface Callback extends IModelCallback<UserBean> {
        void onSuccessLogin(UserBean userBean);
    }
}
