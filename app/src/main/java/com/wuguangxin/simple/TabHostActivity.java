package com.wuguangxin.simple;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuguangxin.ui.XinTabActivity;

import java.util.ArrayList;

/**
 * Tab界面
 */
public class TabHostActivity extends XinTabActivity implements
        XinTabActivity.OnTabSwitchListener, XinTabActivity.OnGetItemViewListener {

    @Override
    public void onCreateView() {
        setContentView(R.layout.activity_tab_host);
        setOnGetItemViewListener(this);
    }

    @Override
    public void setCurrentTab(int position) {
        super.setCurrentTab(position);
    }

    @Override
    public void onGetItemView(View parentView, ImageView iconView, TextView nameView, int position) {
        // 改变文字状态颜色选择器
//        nameView.setTextColor(getResources().getColorStateList(R.color.main_tab_item_text));
    }

    @Override
    public ArrayList<Tab> getTabList() {
        ArrayList<Tab> tabList = new ArrayList<>();
        tabList.add(new Tab("首页", R.drawable.main_tab_home, TabHost0_Activity.class));
        tabList.add(new Tab("产品", R.drawable.main_tab_product, TabHost1_Activity.class));
        tabList.add(new Tab("我的", R.drawable.main_tab_assets, TabHost2_Activity.class));
        return tabList;
    }

    @Override
    public void onTabSwitch(int position) {
    }
}