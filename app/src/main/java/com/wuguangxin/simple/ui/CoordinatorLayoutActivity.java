package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.adapter.StringAdapter;
import com.wuguangxin.ui.XinBaseActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CoordinatorLayoutActivity extends XinBaseActivity {
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.appbar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.button_layout) LinearLayout buttonLayout;
    @BindView(R.id.button1) TextView mButton1;
    @BindView(R.id.menu_layout) LinearLayout menuLayout;
    @BindView(R.id.menu_1) TextView menu_1;

    AppBarLayout.Behavior appBarLayoutBehavior;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_coordinator;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) list.add(String.valueOf(i));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        StringAdapter simpleAdapter = new StringAdapter(this, list);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(simpleAdapter);
    }

    @Override
    public void initListener() {
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
//                printLogI("offset = " + offset);
//            }
//        });
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appBarLayoutBehavior == null) {
                    CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
                    if (behavior instanceof AppBarLayout.Behavior) {
                        appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
                    }
                }
                if (appBarLayoutBehavior != null) {
                    int height = appBarLayout.getHeight() - buttonLayout.getHeight();
                    appBarLayoutBehavior.setTopAndBottomOffset(-height);
                }

                //menuLayout.setVisibility(menuLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                if (menuLayout.getVisibility() == View.GONE) {
//                    menuLayout.startAnimation(getTopIn());
//                    menuLayout.setVisibility(View.VISIBLE);
//                } else {
//                    menuLayout.startAnimation(AnimUtil.getTopOut());
//                    menuLayout.setVisibility(View.GONE);
//                }
            }
        });

//        menu_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                menuLayout.setVisibility(View.GONE);
//            }
//        });
    }

    private Animation getTopIn() {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, -1.0F,
                Animation.RELATIVE_TO_SELF, 0.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(300);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }

}
