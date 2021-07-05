package com.wuguangxin.simple.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import com.wuguangxin.simple.R;
import com.wuguangxin.ui.XinBaseActivity;

import butterknife.BindView;

public class GameActivity extends XinBaseActivity {
    @BindView(R.id.recycler_view1) EditText mRelavice;
    @BindView(R.id.password) EditText mPassword;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_game;
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
}
