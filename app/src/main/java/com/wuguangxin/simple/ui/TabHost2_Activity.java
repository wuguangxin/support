package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Tab 2
 */
public class TabHost2_Activity extends BaseActivity {

	@BindView(android.R.id.text1) TextView mTextView;

	@Override
	public int getLayoutRes() {
		return android.R.layout.simple_list_item_1;
	}

	@Override
	public void initView(Bundle savedInstanceState) {
		mTextView.setText(getClass().getSimpleName());
	}

	@Override
	public void initListener() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void setData() {

	}
}