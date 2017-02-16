package com.wuguangxin.listener;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 当ListView嵌套在ScrollView中时，该ListView可能无法滚动，这时可以给该ListView设置OnTouchListener事件监听，传递本类对象即可
 *
 * @author wuguangxin
 * @date: 2015-1-23 下午7:36:25
 */
public class InnerListViewTouchListener implements android.view.View.OnTouchListener {
	private ScrollView mScrollView;
	
	/**
	 * 构造实例时需传递外层的ScrollView对象
	 * @param scrollView
	 */
	public InnerListViewTouchListener(ScrollView scrollView){
		this.mScrollView = scrollView;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event){
		int eventAction = event.getActionMasked();
		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:
			mScrollView.requestDisallowInterceptTouchEvent(true);
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			;
		case MotionEvent.ACTION_CANCEL:
			mScrollView.requestDisallowInterceptTouchEvent(false);
			break;
		}
		return false;
	}
}
