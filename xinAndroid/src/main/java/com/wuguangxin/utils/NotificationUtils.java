package com.wuguangxin.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 通知工具类
 *
 * @author wuguangxin
 * @date: 2015-7-1 下午1:31:25
 */
public class NotificationUtils{
private static boolean isShakeUsable;
private static boolean isAudioUsable;

//	private static long[] vibrate = { 0, 100, 200, 300 };
	
	/**
	 * 发送通知
	 * @param context 上下文
	 * @param intent 意图，点击通知后要打开的Activity
	 * @param id 通知的ID
	 * @param title 标题
	 * @param message 消息
	 */
	@SuppressWarnings("deprecation")
	public static void send(Context context, Intent intent, int id, String title, String message, int icon){
		Notification notification = new Notification(icon, message, System.currentTimeMillis());
		//获取通知管理器
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
		PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_ONE_SHOT);
		// 设置点击状态栏的图标出现的提示信息
		notification.setLatestEventInfo(context, title, message, pendingIntent); 
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER; 
		// 点击通知自动消失
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// 点击清除通知不消失，直到用户点击通知
//		notification.flags |= Notification.FLAG_NO_CLEAR;					
		if(isAudioUsable()){
			// 默认声音
			notification.defaults |= Notification.DEFAULT_SOUND;
			// 自定义声音
//			notification.sound = Uri.parse("file:///android_asset/sound_buy_ok.mp3");  // asset文件
//			notification.sound = Uri.parse("file:///sdcard/Music/voice/xy2_tingyuan.mp3");  // SD卡
//			notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound_buy_ok);
//			notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "7");
		}
		if(isShakeUsable()){
			// 默认震动
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			// 自定义震动规律
//			notification.vibrate = vibrate; 
		}
		// 发送通知
		nm.notify(id, notification);
	}

	public static boolean isShakeUsable() {
		return isShakeUsable;
	}

	public static void setShakeUsable(boolean isShakeUsable) {
		NotificationUtils.isShakeUsable = isShakeUsable;
	}

	public static boolean isAudioUsable() {
		return isAudioUsable;
	}

	public static void setAudioUsable(boolean isAudioUsable) {
		NotificationUtils.isAudioUsable = isAudioUsable;
	}
}
