package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;

import com.wuguangxin.simple.R;

public class WidgetActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.permission:
            break;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_widget;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

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
