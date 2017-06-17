package com.wuguangxin.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

/**
 * 通知工具类
 *
 * <p>Created by wuguangxin on 15/7/1 </p>
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
		//获取通知管理器
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建通知
		Notification notification;
		if (android.os.Build.VERSION.SDK_INT >= 22) {
			notification = getNotificationOnSDK22After(context, intent, id, title, message, icon);
		} else {
			notification = getNotification(context, intent, id, title, message, icon);
		}
		// 发送通知
		manager.notify(id, notification);
	}

	/**
	 * 该方法在API 22 以前使用（不包括22）
	 * @param context
	 * @param intent
	 * @param id
	 * @param title
	 * @param message
	 * @param icon
	 * @return
	 */
	@NonNull
	private static Notification getNotification(Context context, Intent intent, int id, String title, String message, int icon) {
		Notification notification = new Notification(icon, message, System.currentTimeMillis());
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
		return notification;
	}

	/**
	 * 当创建通知时，如果SDK大于22，则使用本方法，但该方法最小支持为SDK16
	 * @param context
	 * @param intent
	 * @param id
	 * @param title
	 * @param message
	 * @param icon
	 * @return
	 */
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public static Notification getNotificationOnSDK22After(Context context, Intent intent, int id, String title, String message, int icon){
		//获取通知管理器
		Notification.Builder builder = new Notification.Builder(context);
		builder.setTicker("新消息"); // 状态栏上显示的文字，比如"新消息"
		builder.setSmallIcon(icon);
		builder.setContentTitle(title);
		builder.setContentText(message);
//		builder.setContentInfo(""); // 补充内容
		builder.setAutoCancel(true); // 点击后通知自动消失
		builder.setWhen(System.currentTimeMillis());
		PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_ONE_SHOT);
		builder.setContentIntent(pendingIntent);
		Notification notification = builder.build();
		return notification;
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
