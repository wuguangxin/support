package com.wuguangxin.ui.test.tabhost;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Demo3Activity
 */
public class Demo2Activity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText(this.getClass().getSimpleName());
		textView.setPadding(20, 20, 20, 20);
		setContentView(textView);
	}
}