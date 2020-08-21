package com.wuguangxin.simple.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.wuguangxin.ui.XinBaseActivity;
import com.wuguangxin.simple.R;

import butterknife.BindView;

/**
 * Tab 0
 */
public class TabHost0_Activity extends XinBaseActivity {
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