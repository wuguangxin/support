package com.wuguangxin.listener;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 禁止多次点击的 OnClickListener
 *
 * @author wuguangxin
 * @date: 2015-12-24 下午1:54:58
 */
public abstract class OnDoubleClickListener implements OnClickListener{
	public static long MIN_DOUBLE_CLICK_SPANCE_TIME = 300;
	public static long LAST_DOUBLE_CLICK_TIME = 0;
	private View mView;
	
	public OnDoubleClickListener(){
		super();
	}

	public OnDoubleClickListener(View view){
		super();
		this.mView = view;
	}

	public OnDoubleClickListener(View mView, long minClickSpanceTime){
		super();
		this.mView = mView;
		OnDoubleClickListener.MIN_DOUBLE_CLICK_SPANCE_TIME = minClickSpanceTime;
	}
	
	/**
	 * 当正确的点击按钮时回调
	 * @param v
	 */
	public abstract void onClicked(View v);
	
	@Override
	final public void onClick(View v){
		long currentTime = System.currentTimeMillis();
		if (currentTime - LAST_DOUBLE_CLICK_TIME > MIN_DOUBLE_CLICK_SPANCE_TIME) {
			LAST_DOUBLE_CLICK_TIME = currentTime;
			onClicked(mView != null ? mView : v);
		} else {
			System.out.println("click too fast ~~~");
		}
	}
}