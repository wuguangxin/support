package com.wuguangxin.simple.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wuguangxin.simple.R;
import com.wuguangxin.simple.databinding.ActivityWidgetBinding;
import com.wuguangxin.utils.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetActivity extends BaseActivity<ActivityWidgetBinding> implements View.OnClickListener {
    private Map<Integer, Class<? extends Activity>> mActivityMap;

    @Override
    public int getLayoutId() {
        return R.layout.activity_widget;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);

        mActivityMap = new HashMap<>();
        mActivityMap.put(R.id.coordinatorLayout, CoordinatorLayoutActivity.class);
        mActivityMap.put(R.id.ItemView, ItemViewActivity.class);
        mActivityMap.put(R.id.CircleProgressView, CircleProgressViewActivity.class);
        mActivityMap.put(R.id.XinDialog, MyDialogActivity.class);
        mActivityMap.put(R.id.GestureView, GestureViewActivity.class);
    }

    @Override
    public void initListener() {
        List<Integer> keyList = MapUtils.getKeys(mActivityMap);
        for (Integer id : keyList) findViewById(id).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        openActivity(mActivityMap.get(view.getId()));
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }
}
