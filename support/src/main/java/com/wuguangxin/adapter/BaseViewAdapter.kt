package com.wuguangxin.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * View的适配器
 * Created by wuguangxin on 2019/12/2.
 */
public class BaseViewAdapter extends PagerAdapter {
    private List<View> list;

    public BaseViewAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (list == null)
            return null;
        container.addView(list.get(position));
        return list.get(position);//View

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

}
