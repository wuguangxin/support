package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.wuguangxin.mvp.ui.XinMVPActivity;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.bean.UserBean;
import com.wuguangxin.simple.mvp.LoginContract;
import com.wuguangxin.simple.mvp.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MvpActivity extends XinMVPActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.username) EditText mUsername;
    @BindView(R.id.password) EditText mPassword;

    @Override
    public LoginPresenter newPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_mvp;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }

    @OnClick({ R.id.login, })
    public void onClick(View v) {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        getPresenter().login(username, password);
    }

    @Override
    public void onSuccessLogin(UserBean userBean) {
        if (userBean == null) {
            showToast("登录失败");
        } else {
            showToast("登录成功 \n欢迎回来" + userBean.getRealName());
        }
    }

    @Override
    public void onFinish() {

    }
}
