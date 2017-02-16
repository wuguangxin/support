package com.wuguangxin.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 到期提醒工具类
 *
 * @author wuguangxin
 * @date: 2015-6-16 下午2:03:46
 */
public class AlarmUtils{
	// 阅读资料
	//	AlarmManager一共定义了四种类型的闹钟：RTC, RTC_WAKEUP, ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP。
	//	AlarmManager.RTC，硬件闹钟，不唤醒休眠；
	//	AlarmManager.RTC_WAKEUP，硬件闹钟，唤醒休眠；
	//	AlarmManager.ELAPSED_REALTIME，真实时间流逝闹钟，不唤醒休眠；
	//	AlarmManager.ELAPSED_REALTIME_WAKEUP，真实时间流逝闹钟，唤醒休眠；
	//	RTC闹钟和ELAPSED_REALTIME最大的差别就是前者可以通过修改时间触发闹钟事件，后者要通过真实时间的流逝，即使在休眠状态，时间也会被计算。
	/**
	 * 添加提醒
	 * @param context
	 * @param requestCode
	 * @param triggerAtMillis
	 * @param intent 
	 */
	public static void add(Context context, int requestCode, long triggerAtMillis, Intent intent){
		// PendingIntent.FLAG_UPDATE_CURRENT如果pendingIntent已存在，则更新数据
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 获取闹钟管理的实例
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); 
		// 设置提醒
		am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent); 
		// 设置周期提醒
		// am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1000), (24 * 60 * 60 * 1000), pendingIntent); 
	}
	
	/**
	 * 取消提醒
	 * @param context
	 * @param requestCode Private request code for the sender
	 * @param intent The Intent to be broadcast.
	 */
	public static void cancel(Context context, int requestCode, Intent intent){
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, android.app.PendingIntent.FLAG_NO_CREATE );
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // 获取闹钟管理的实例
		am.cancel(pendingIntent); // 取消
	}
}
