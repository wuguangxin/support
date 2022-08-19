package com.wuguangxin.simple.ui;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.wuguangxin.mvp.ui.XinMVPActivity;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.bean.UserBean;
import com.wuguangxin.simple.databinding.ActivityMvpBinding;
import com.wuguangxin.simple.mvp.LoginContract;
import com.wuguangxin.simple.mvp.LoginPresenter;

public class MvpActivity extends XinMVPActivity<ActivityMvpBinding, LoginPresenter> implements LoginContract.View {
    AppBarLayout.Behavior appBarLayoutBehavior;

    @Override
    public LoginPresenter newPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mvp;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
    }

    @Override
    public void initListener() {
        findViewById(R.id.login).setOnClickListener(v -> {
            String username = binding.username.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            getPresenter().login(username, password);
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

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
