package com.wuguangxin.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Fragment适配器
 *
 * Created by wuguangxin on 17/3/27
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {
	private List<? extends Fragment> list;
	private List<String> titleList = new ArrayList<>();

	public BaseFragmentAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
	}

	public BaseFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public BaseFragmentAdapter(FragmentManager fm, List<? extends Fragment> list) {
		super(fm);
		this.list = list;
	}

	public BaseFragmentAdapter(FragmentManager fm, List<? extends Fragment> list, List<String> titleList) {
		super(fm);
		this.list = list;
		this.titleList = titleList;
	}

	@Override
	public Fragment getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return titleList == null ? null : titleList.get(position);
	}

	public void setList(List<? extends Fragment> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setList(List<? extends Fragment> fragmentList, List<String> titleList) {
		this.list = fragmentList;
		this.titleList = titleList;
		notifyDataSetChanged();
	}

	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
		notifyDataSetChanged();
	}
}