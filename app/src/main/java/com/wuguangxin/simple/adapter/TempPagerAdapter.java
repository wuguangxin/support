package com.wuguangxin.simple.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TempPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    public TempPagerAdapter(FragmentManager fm) {
        this(fm, null, null);
    }

    public TempPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        setFragmentList(fragments);
        setTitleList(titles);

    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragments.clear();
        if (fragmentList != null) {
            this.fragments.addAll(fragmentList);
        }
    }

    public void setTitleList(List<String> titleList) {
        this.titles.clear();
        if (titleList != null) {
            this.titles.addAll(titleList);
        }
    }
}