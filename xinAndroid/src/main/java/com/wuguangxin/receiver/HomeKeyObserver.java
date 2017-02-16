package com.wuguangxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Home键状态观察者
 *
 * @author wuguangxin
 * @date: 2015-3-31 上午11:43:03
 */
public class HomeKeyObserver {
	private OnHomePressedListener mOnHomePressedListener;
	private InnerReceiver mReceiver;
	private Context mContext;
	private boolean isRegister;
	
	public HomeKeyObserver(Context context, OnHomePressedListener mOnHomePressedListener){
		this.mContext = context;
		this.mOnHomePressedListener = mOnHomePressedListener;
		mReceiver = new InnerReceiver();
	}
	
	/**
	 * 开始Home键监听
	 */
	public void register(){
		if(!isRegister){
			isRegister = true;
			final IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			mContext.getApplicationContext().registerReceiver(mReceiver, filter);
		}
	}
	
	/**
	 * 停止Home键监听
	 * @return 
	 */
	public void unregister(){
		if(isRegister && mReceiver != null){
			isRegister = false;
			mContext.getApplicationContext().unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
	
	/**
	 * 广播接收者
	 */
	class InnerReceiver extends BroadcastReceiver{
		final String SYSTEM_DIALOG_REASON_KEY = "reason";
		final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
		final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
		final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
		
		@Override
		public void onReceive(Context context, Intent intent){
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
				if (reason != null) {
					if (mOnHomePressedListener != null) {
						if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
							mOnHomePressedListener.onHomePressed(); // 短按home键  
						} else {
							if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
								mOnHomePressedListener.onHomeLongPressed(); // 长按home键  
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Home键监听回调接口  
	 *
	 * @author wuguangxin
	 * @date: 2015-3-31 上午11:42:46
	 */
	public interface OnHomePressedListener{
		/**
		 * Home键被按下
		 */
		void onHomePressed();
		
		/**
		 * Home键被长按
		 */
		void onHomeLongPressed();
	}
}