package com.wuguangxin.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

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
		NotificationManager manager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建通知
		Notification notification = null;
		if (Build.VERSION.SDK_INT >= 16) {
			notification = getNotificationOnSDK22After(context, intent, id, title, message, icon);
		} else {
//			notification = getNotification(context, intent, id, title, message, icon);
		}
		// 发送通知
		if(notification != null){
			manager.notify(id, notification);
		} else {
			Log.e("NotificationUtils", "notification is null");
		}
	}

	/**
	 * 当创建通知时，如果SDK大于22，则使用本方法，但该方法最小支持为SDK16
	 * @param context 上下文
	 * @param intent 意图
	 * @param id 通知ID
	 * @param title 通知标题
	 * @param message 通知信息
	 * @param icon 通知icon
	 * @return Notification
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

	/**
	 * 是否开启震动
	 * @return 是否开启震动
	 */
	public static boolean isShakeUsable() {
		return isShakeUsable;
	}

	/**
	 * 设置是否开启震动
	 * @param isShakeUsable 是否开启震动
	 */
	public static void setShakeUsable(boolean isShakeUsable) {
		NotificationUtils.isShakeUsable = isShakeUsable;
	}

	/**
	 * 是否播放声音
	 * @return 是否播放声音
	 */
	public static boolean isAudioUsable() {
		return isAudioUsable;
	}

	/**
	 * 设置是否播放声音
	 * @param isAudioUsable 是否播放声音
	 */
	public static void setAudioUsable(boolean isAudioUsable) {
		NotificationUtils.isAudioUsable = isAudioUsable;
	}
}
