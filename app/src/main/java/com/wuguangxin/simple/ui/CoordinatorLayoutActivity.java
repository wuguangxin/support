package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.adapter.StringAdapter;
import com.wuguangxin.simple.databinding.ActivityCoordinatorBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CoordinatorLayoutActivity extends BaseActivity<ActivityCoordinatorBinding> {

    AppBarLayout.Behavior appBarLayoutBehavior;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coordinator;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getTitleBar().setBackVisibility(false);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) list.add(String.valueOf(i));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        StringAdapter simpleAdapter = new StringAdapter(this, list);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(simpleAdapter);
        binding.recyclerView.setFocusableInTouchMode(false);

        binding.topRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false));
        binding.topRecyclerView.setAdapter(simpleAdapter);
        binding.topRecyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        binding.topRecyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS); // 滑到头/尾时的效果，never会不显示滑动条
        binding.topRecyclerView.setScrollBarFadeDuration(Integer.MAX_VALUE);
        binding.topRecyclerView.requestFocus();

//        binding.homeTabLayout.addTab(new TabLayout.Tab().setText("菜单1"));

        FixAppBarLayoutBehavior fixAppBarLayoutBehavior = new FixAppBarLayoutBehavior();
        fixAppBarLayoutBehavior.stopAnimation();
    }

    public void backTop() {
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) binding.appbarLayout.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
            if (topAndBottomOffset != 0) {
                appBarLayoutBehavior.setTopAndBottomOffset(0);
            }
        }
        binding.recyclerView.smoothScrollToPosition(0);
        binding.backTop.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // 向下滚动
                    binding.backTop.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        binding.backTop.setOnClickListener(new View.OnClickListener() {
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
