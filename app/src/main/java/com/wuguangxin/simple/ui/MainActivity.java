package com.wuguangxin.simple.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private Map<Integer, Class<? extends Activity>> mActivityMap;
    private PopupWindow popupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
        mActivityMap = new HashMap<>();
        mActivityMap.put(R.id.widget, WidgetActivity.class);
        mActivityMap.put(R.id.mvp, MvpActivity.class);
        mActivityMap.put(R.id.game, GameActivity.class);
        mActivityMap.put(R.id.test1, TestActivity.class);
        mActivityMap.put(R.id.apps, ApplicationsActivity.class);
    }

    @Override
    public void initListener() {
    }

    public void onClick(View v) {
        openActivity(mActivityMap.get(v.getId()));
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }

}
