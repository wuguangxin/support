package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;

import com.wuguangxin.simple.R;

import butterknife.OnClick;

public class WidgetActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_widget;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("动态权限");
    }

    @Override
    public void initListener() {
    }

    @OnClick({R.id.permission})
    public void onClicked(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.permission:
            break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }
}
