package com.wuguangxin.simple.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.ui.XinBaseActivity;
import com.wuguangxin.simple.R;
import com.wuguangxin.utils.MapUtils;
import com.wuguangxin.utils.ShakeUtils;
import com.wuguangxin.view.ViewPagerIndicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnTouch;

public class WidgetActivity extends XinBaseActivity implements View.OnClickListener {

    private static final String TAG = "WidgetActivity";
    private ViewPagerIndicator mViewPagerIndicator;
    // 触发点击事件的ViewID/要打开的Activity Class
    private Map<Integer, Class<? extends Activity>> mActivityMap;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_widget;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);

        mActivityMap = new HashMap<>();
        mActivityMap.put(R.id.tabHost, TabHostDemoActivity.class);
//        mActivityMap.put(R.id.okhttp, OKHttpsUtilsActivity.class);
        mActivityMap.put(R.id.ItemView, ItemViewActivity.class);
        mActivityMap.put(R.id.CircleProgressView, CircleProgressViewActivity.class);
        mActivityMap.put(R.id.XinDialog, MyDialogActivity.class);
        mActivityMap.put(R.id.GestureView, GestureViewActivity.class);

        // 直接跳转
//        openActivity(DialogUtilsActivity.class);
    }

    @Override
    public void initListener() {
        List<Integer> keyList = MapUtils.getKeyList(mActivityMap);
        for (Integer id : keyList) findViewById(id).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        openActivity(mActivityMap.get(view.getId()));
    }

    @OnTouch(R.id.GestureView)
    public boolean onTouch(View v, MotionEvent event) {
        ShakeUtils.shake(v);
        return false;
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }


}
