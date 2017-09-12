package com.wuguangxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * 短信验证码接收者（广播接收者）
 *
 * <p>Created by wuguangxin on 15/3/18 </p>
 */
public class SmsCodeReceiver{
	private SmsBroadcastReceiver smsBroadcastReceiver;
	private IntentFilter filter;
	private Context context;
	private EditText editText;

	/**
	 * 构造一个短信验证码广播接收者的管理器
	 * @param context Activity的context
	 * @param editText 验证码输入框
	 */
	public SmsCodeReceiver(Context context, EditText editText){
		this.context = context;
		this.editText = editText;
		filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		smsBroadcastReceiver = new SmsBroadcastReceiver();
	}
	
	/**
	 * 启动短信广播接收者
	 * @return  SmsCodeReceiver
	 */
	public final SmsCodeReceiver start(){
		if(context != null && smsBroadcastReceiver != null && filter != null){
			context.registerReceiver(smsBroadcastReceiver, filter);
		}
		return this;
	}
	
	/**
	 * 停止短信广播接收者
	 */
	public final void stop(){
		if(context != null && smsBroadcastReceiver != null){
			context.unregisterReceiver(smsBroadcastReceiver);
		}
	}
	
	private final class SmsBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			SmsMessage smsMessage = null;
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdusObj = (Object[]) bundle.get("pdus");
				for (Object p: pdusObj) {
					smsMessage = SmsMessage.createFromPdu((byte[]) p);
					String number = smsMessage.getOriginatingAddress();	// 发送者
					String msg = smsMessage.getMessageBody();							// 短信内容
					long times = smsMessage.getTimestampMillis();						// 发送时间
					
//					Date date = new Date(times);
//					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					String receiveTime = format.format(date);
					
					SmsCodeReceiver.this.onReceive(number, msg, times);
					
					// 避免是手机号伪造发来的验证信息
					if(number.replace("+86", "").length() != 11){
						if(!TextUtils.isEmpty(msg)){
							if(msg.contains("达飞") && msg.contains("验证码")){
								// 取冒号后的6位数字
								int spaceIndex = msg.indexOf("：") + 1;
								String msgCode = msg.substring(spaceIndex, spaceIndex + 6);
								if(editText != null){
									editText.setText(msgCode);
									editText.requestFocus();
									Editable text = editText.getText();
									Selection.setSelection(text, text.length());
								}
								return;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 当收到短信时回调
	 * @param number 发送者号码
	 * @param msg 短信内容
	 * @param times 发送时间（时间戳）
	 */
	public void onReceive(String number, String msg, long times){
		
	}
}
