package com.wuguangxin.simple.mvp;

import com.wuguangxin.mvp.BaseModel;
import com.wuguangxin.mvp.BasePresenter;
import com.wuguangxin.mvp.BaseView;
import com.wuguangxin.mvp.callback.BaseModelCallback;
import com.wuguangxin.simple.bean.UserBean;

/**
 * 用户 Contract
 * 
 * Created by wuguangxin on 2020-04-06.
 */
public interface LoginContract {
    interface Presenter extends BasePresenter<Model, View> {
        void login(String username, String password);
    }

    interface View extends BaseView {
        void onSuccessLogin(UserBean userBean);
    }

    interface Model extends BaseModel {
        void login(String username, String password, Callback Callback);
    }

    interface Callback extends BaseModelCallback<UserBean> {
        void onSuccessLogin(UserBean userBean);
    }
}
