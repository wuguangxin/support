package com.wuguangxin.base;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 倒计时辅助类
 *
 * <p>Created by wuguangxin on 15/8/20 </p>
 */
public class CountDownHelper extends CountDownTimer {
	private static final String DEF_REGET = "%s秒";
	private TextView button; // 按钮
	private String reText; // 获取成功后,显示的按钮文字(带格式化), 例如 "重新获取(%s秒)"
	private String text; // 附加的文字，例如 "获取验证码"
	private long countDownInterval; // 间隔时间

	/**
	 * @param millisInFuture 总时长
	 * @param countDownInterval 计时的时间间隔 (一般1000毫秒)
	 */
	public CountDownHelper(long millisInFuture, long countDownInterval){
		super(millisInFuture, countDownInterval);
	}

	/**
	 * @param millisInFuture 总时长
	 * @param countDownInterval 计时的时间间隔
	 * @param button 显示倒计时的Button
	 * @param reText 获取成功后,显示的按钮文字(带格式化), 例如 "重新获取(%s秒)"
	 * 注意：总时间加上countDownInterval是因为需要，比如5秒倒计时，需要在最开始时就显示5秒
	 */
	public CountDownHelper(long millisInFuture, long countDownInterval, TextView button, String reText){
		super(millisInFuture + countDownInterval, countDownInterval);
		this.countDownInterval = countDownInterval;
		this.button = button;
		this.reText = reText;
		if(button != null){
			this.text = button.getText().toString();
		}
	}

	/**
	 * 计时过程显示
	 */
	@Override
	public void onTick(long millisUntilFinished){
		if(button != null){
			long curTime = millisUntilFinished / countDownInterval;
			if(TextUtils.isEmpty(reText) || !reText.contains("%s")){
				reText = DEF_REGET;
			}
			button.setText(String.format(reText, curTime));
		}
	}

	/**
	 * 计时完毕时触发
	 */
	@Override
	public void onFinish(){
		if(button != null){
			button.setEnabled(true);
			button.setText(text);
		}
	}
	
	/**
	 * 停止倒计时
	 */
	public void stop(){
		cancel();
		onFinish();
	}
}