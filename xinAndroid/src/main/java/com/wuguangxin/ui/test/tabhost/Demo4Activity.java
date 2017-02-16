package com.wuguangxin.ui.test.tabhost;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Demo2Activity
 *
 * @author wuguangxin
 * @date: 2016-1-22 下午5:15:43
 */
public class Demo4Activity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText(this.getClass().getSimpleName());
		textView.setPadding(20, 20, 20, 20);
		setContentView(textView);
	}
}