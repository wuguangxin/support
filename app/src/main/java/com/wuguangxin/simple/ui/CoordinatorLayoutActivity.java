package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.adapter.StringAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CoordinatorLayoutActivity extends BaseActivity {
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.menu_layout) LinearLayout menuLayout;
    @BindView(R.id.menu_1) TextView menu_1;
    @BindView(R.id.home_tab_layout) TabLayout mHomeTabLayout;
    @BindView(R.id.back_top) Button mBackTop;
    @BindView(R.id.topRecyclerView) RecyclerView mTopRecyclerView;

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
        mRecyclerView.setFocusableInTouchMode(false);

        mTopRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false));
        mTopRecyclerView.setAdapter(simpleAdapter);
        mTopRecyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        mTopRecyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS); // 滑到头/尾时的效果，never会不显示滑动条
        mTopRecyclerView.setScrollBarFadeDuration(Integer.MAX_VALUE);
        mTopRecyclerView.requestFocus();

//        mHomeTabLayout.addTab(new TabLayout.Tab().setText("菜单1"));

        FixAppBarLayoutBehavior fixAppBarLayoutBehavior = new FixAppBarLayoutBehavior();
        fixAppBarLayoutBehavior.stopAnimation();
    }

    public void backTop() {
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
            if (topAndBottomOffset != 0) {
                appBarLayoutBehavior.setTopAndBottomOffset(0);
            }
        }
        mRecyclerView.smoothScrollToPosition(0);
        mBackTop.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // 向下滚动
                    mBackTop.setVisibility(View.VISIBLE);
                }
            }
        });

        mHomeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                backTop();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTop();
            }
        });
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
