package com.wuguangxin.ui.test.tabhost;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Demo2Activity
 */
public class Demo3Activity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText(this.getClass().getSimpleName());
		textView.setPadding(20, 20, 20, 20);
		setContentView(textView);
	}
}