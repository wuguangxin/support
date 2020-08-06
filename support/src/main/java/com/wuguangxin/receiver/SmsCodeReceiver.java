package com.wuguangxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

/**
 * 短信验证码接收者（广播接收者）
 * <p>Created by wuguangxin on 15/3/18 </p>
 */
public class SmsCodeReceiver {
    private MyReceiver mReceiver;
    private Context context;
    private Callback callback;

    /**
     * 构造一个短信验证码广播接收者的管理器
     *
     * @param context 上下文
     * @param callback 回调
     */
    public SmsCodeReceiver(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    /**
     * 启动短信广播接收者
     *
     * @return SmsCodeReceiver
     */
    public final SmsCodeReceiver register() {
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		mReceiver = new MyReceiver();
        if (context != null) {
            context.registerReceiver(mReceiver, filter);
        }
        return this;
    }

    /**
     * 停止短信广播接收者
     */
    public final void unregister() {
        if (context != null && mReceiver != null) {
            context.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SmsMessage smsMessage = null;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object p : pdusObj) {
                    smsMessage = SmsMessage.createFromPdu((byte[]) p);
                    String number = smsMessage.getOriginatingAddress();    // 发送者
                    String msg = smsMessage.getMessageBody();              // 短信内容
                    long times = smsMessage.getTimestampMillis();          // 发送时间

					if (callback != null) callback.onReceive(number, msg, times);

                    // 避免是手机号伪造发来的验证信息
                    if (number.replace("+86", "").length() != 11) {
                        if (!TextUtils.isEmpty(msg)) {
                        	if (msg.contains("达飞") && msg.contains("验证码")) {
                                // 取冒号后的6位数字
                                int spaceIndex = msg.indexOf("：") + 1;
                                String msgCode = msg.substring(spaceIndex, spaceIndex + 6);
								if (callback != null) callback.onReceiveCode(number, msgCode, times);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

	/**
	 * 回调接口
	 */
	public interface Callback {

		/**
		 * 当收到任何短信时都回调
		 *
		 * @param number 发送者号码
		 * @param msg 短信内容
		 * @param times 发送时间（时间戳）
		 */
		void onReceive(String number, String msg, long times);

		/**
		 * 接收到验证码时回调
		 * @param number 发送人号码
		 * @param code 短信验证码
		 * @param times 发送时间
		 */
		void onReceiveCode(String number, String code, long times);
	}
}
