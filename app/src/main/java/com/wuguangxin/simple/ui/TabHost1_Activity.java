package com.wuguangxin.simple.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Tab 1
 */
public class TabHost1_Activity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText(getClass().getSimpleName());
		setContentView(textView);
	}
}