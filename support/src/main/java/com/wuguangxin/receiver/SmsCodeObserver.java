package com.wuguangxin.receiver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.wuguangxin.utils.Logger;

/**
 * 短信验证码监测（观察者模式），观察短信数据库的变化来获取短信。
 *
 * <p>Created by wuguangxin on 14/8/17 </p>
 */
public abstract class SmsCodeObserver extends ContentObserver {
	private Context mContext;
	private Callback mCallback;

	public SmsCodeObserver(Context context, Handler handler, Callback callback){
		super(handler);
		this.mContext = context;
		this.mCallback = callback;
	}

	@Override
	public void onChange(boolean selfChange){
		super.onChange(selfChange);
		try {
			ContentResolver contentResolver = mContext.getContentResolver();
			Cursor cursor = contentResolver.query(Uri.parse("content://sms/"), new String[]{"address", "body", "date"}, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				String address = cursor.getString(0);
				if (!TextUtils.isEmpty(address)/* && address.equals(SMS_NUMBER) 如果短信号码不变，则可以直接判断短信号码*/) {
					// 避免是手机号伪造发来的验证信息
					if(address.replace("+86", "").length() != 11){
						String body = cursor.getString(1);
						// 分析短信内容，获取短信验证码
						handleMsg(body);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.i("短信接收异常 " + e);
		}
	}

	private void handleMsg(String body){
		if(!TextUtils.isEmpty(body)){
			if(body.contains("[这里替换为平台签名]") && body.contains("验证码")){
				// 取冒号后的6位数字
				int spaceIndex = body.indexOf("：") + 1;
				String msgCode = body.substring(spaceIndex, spaceIndex + 6); // 加6是因为验证码是6位
				if (mCallback != null) mCallback.onReceiveCode(msgCode);
			}
		}
	}

	/**
	 * 当检测到验证码时
	 * @param smsCode 检测到的验证码
	 */
	public abstract void setValue(String smsCode);

	/**
	 * 回调接口
	 */
	public interface Callback {

		/**
		 * 接收到验证码时回调
		 * @param smsCode 短信验证码
		 */
		void onReceiveCode(String smsCode);
	}
}
