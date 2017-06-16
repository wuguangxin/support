package com.wuguangxin.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 防止多次点击的 OnItemClickListener
 *
 * <p>Created by wuguangxin on 15-12-24 </p>
 */
public abstract class OnItemDoubleClickListener implements OnItemClickListener{
	public static long MIN_ITEM_DOUBLE_CLICK_SPANCE_TIME = 1000;
	public static long LAST_ITEM_DOUBLE_CLICK_TIME = 0;
	private View mView;
	
	public OnItemDoubleClickListener(){
		super();
	}

	public OnItemDoubleClickListener(View mView){
		super();
		this.mView = mView;
	}
	
	public OnItemDoubleClickListener(View mView, long minClickSpanceTime){
		super();
		this.mView = mView;
		OnItemDoubleClickListener.MIN_ITEM_DOUBLE_CLICK_SPANCE_TIME = minClickSpanceTime;
	}
	
	/**
	 * 当正确的点击item时回调
	 * @param parent parent
	 * @param view view
	 * @param position position
	 * @param id id
	 */
	public abstract void onItemClicked(AdapterView<?> parent, View view, int position, long id);
	
	@Override
	final public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		long currentTime = System.currentTimeMillis();
		if (currentTime - LAST_ITEM_DOUBLE_CLICK_TIME > MIN_ITEM_DOUBLE_CLICK_SPANCE_TIME) {
			LAST_ITEM_DOUBLE_CLICK_TIME = currentTime;
			onItemClicked(parent, mView != null ? mView : view, position, id);
		} else {
			System.out.println("click too fast ~~~");
		}
	}
}