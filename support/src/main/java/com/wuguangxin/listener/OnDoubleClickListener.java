package com.wuguangxin.listener;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 禁止多次点击的 OnClickListener
 *
 * <p>Created by wuguangxin on 15-12-24 </p>
 */
public abstract class OnDoubleClickListener implements OnClickListener{
	private View mView;
	private long minClickSpaceTime = 300;
	private long lastClickTime = 0;

	public OnDoubleClickListener(){
		super();
	}

	/**
	 * @param view view
	 */
	public OnDoubleClickListener(View view){
		super();
		this.mView = view;
	}

	public OnDoubleClickListener(View mView, long minClickSpaceTime){
		super();
		this.mView = mView;
		this.minClickSpaceTime = minClickSpaceTime;
	}
	
	/**
	 * 当正确的点击按钮时回调
	 * @param v 点击的View
	 */
	public abstract void onClicked(View v);
	
	@Override
	final public void onClick(View v){
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastClickTime > minClickSpaceTime) {
			lastClickTime = currentTime;
			onClicked(mView != null ? mView : v);
		} else {
			System.out.println("click too fast ~~~");
		}
	}
}