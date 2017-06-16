package com.wuguangxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 监听屏幕锁定和解锁状态
 *
 * <p>Created by wuguangxin on 14/5/12 </p>
 */
public class ScreenObserver {
	private Context mContext;
	private ScreenReceiver mScreenReceiver;
	private OnScreenStatusListener screenStatusListener;
	private boolean isRegister;


	public ScreenObserver(Context context, OnScreenStatusListener mOnScreenStatusListener) {
		this.mContext = context;
		this.screenStatusListener = mOnScreenStatusListener;
		this.mScreenReceiver = new ScreenReceiver();
	}


//		// 屏幕灭屏广播
//		mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//		// 屏幕亮屏广播
//		mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
//		// 屏幕解锁广播
//		mIntentFilter.addAction(Intent.ACTION_USER_PRESENT);
//		// 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
//		// example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
//		// 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
//		mIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);


	/**
	 * 注册屏幕状态监听
	 */
	public void register() {
		if(!isRegister){
			isRegister = true;
			final IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_ON);
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			mContext.getApplicationContext().registerReceiver(mScreenReceiver, filter);
		}
	}

	/**
	 * 注销屏幕状态监听
	 */
	public void unregister() {
		if(isRegister && mScreenReceiver != null){
			isRegister = false;
			mContext.getApplicationContext().unregisterReceiver(mScreenReceiver);
			mScreenReceiver = null;
		}
	}

	/**
	 * 屏幕状态广播接收者
	 */
	private class ScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(screenStatusListener != null && intent != null){
				String action = intent.getAction();
				if (Intent.ACTION_SCREEN_ON.equals(action)) { // 屏幕开
					screenStatusListener.onScreenON();
				} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {  // 屏幕关
					screenStatusListener.onScreenOFF();
				}
			}
		}
	}

	/**
	 * 屏幕状态监听器
	 * @author wuguangxin
	 */
	public interface OnScreenStatusListener {
		/** 打开 */
		void onScreenON();
		/** 关闭 */
		void onScreenOFF();
	}
}