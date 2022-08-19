package com.wuguangxin.simple.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.wuguangxin.simple.R;
import com.wuguangxin.simple.adapter.TempPagerAdapter;
import com.wuguangxin.simple.databinding.ActivityMainBinding;
import com.wuguangxin.simple.view.LineGraphicView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

        initTabLayout();

        LineGraphicView LineGraphicView = findViewById(R.id.lineGraphicView);

        ArrayList<Long> times = new ArrayList<>();
        times.add(2000L);
        times.add(1743L);
        times.add(1660L);
        times.add(1308L);
        times.add(2432L);
        times.add(2120L);
        times.add(1520L);
        times.add(2150L);
        times.add(2150L);
        times.add(2510L);
        times.add(2450L);
        times.add(2150L);
        times.add(2350L);

        ArrayList<String> dates = new ArrayList<>();
        dates.add("05-19");
        dates.add("05-20");
        dates.add("05-21");
        dates.add("05-22");
        dates.add("05-23");
        dates.add("05-24");
        dates.add("05-25");
        dates.add("05-26");
        dates.add("05-27");
        dates.add("05-28");
        dates.add("05-29");
        dates.add("05-30");
        dates.add("05-31");
        LineGraphicView.setData(times, dates);
    }

    @Override
    public void initListener() {
    }

    public void onClick(View v) {
        openActivity(mActivityMap.get(v.getId()));
    }

    private void testPopupWindow(View v) {

        LinearLayout rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText("哈哈哈哈");
        textView.setHeight(300);
        textView.setBackgroundColor(Color.RED);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -2));

        Button button = new Button(this);
        button.setText("确认");
        button.setWidth(200);
        button.setHeight(100);

        rootView.addView(textView);
        rootView.addView(button);

        popupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, 400);
//        popupWindow.setContentView(rootView);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(v);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void initTabLayout() {
        final List<Fragment> fragments = new ArrayList<>();
        final List<String> titles = Arrays.asList("张三", "李四", "王五", "马六", "阿甘", "大师", "大神", "刘德华", "张学友", "刘若英");
        for (String title : titles) {
            fragments.add(TestEmptyFragment.newInstance(title));
        }
        binding.viewPager.setAdapter(new TempPagerAdapter(getSupportFragmentManager(), fragments, titles));
        binding.tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        binding.tabLayout.setViewPager(binding.viewPager);
        binding.viewPager.setCurrentItem(3);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }

}
