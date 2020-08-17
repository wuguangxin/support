package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.wuguangxin.simple.R;

import butterknife.BindView;

/**
 * Tab 1
 */
public class TabHost1_Activity extends BaseActivity {
	@BindView(R.id.text) TextView mText;

	@Override
	public int getLayoutRes() {
		return R.layout.activity_tab_host;
	}

	@Override
	public void initView(Bundle savedInstanceState) {
		mText.setText(getClass().getSimpleName());
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