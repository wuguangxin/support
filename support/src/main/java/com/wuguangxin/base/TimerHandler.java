package com.wuguangxin.base;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时处理器。指定首次启动延时、之后间隔执行延时和定时监听器，然后在回调中执行想要做的任务
 *
 * <p>Created by wuguangxin on 15/8/9 </p>
 */
public class TimerHandler extends TimerTask {
	private static final long DEFAULT_DELAY = 5000;
	private static final long DEFAULT_PERIOD = 5000;
	private Handler mHandler = new Handler();
	private Timer mTimer = new Timer(true);
	private OnTimerListener mListener;
	private Runnable mRunnable;
	private boolean isRunning;
	private long mDelay;
	private long mPeriod;

	/**
	 * 定时任务构造器。默认延时5秒执行，间隔5秒执行下一次
	 */
	public TimerHandler(){
		this(DEFAULT_DELAY, DEFAULT_PERIOD);
	}

	/**
	 * 定时任务构造器
	 * @param delay 首次延时
	 * @param period 间隔时间
     */
	public TimerHandler(long delay, long period){
		this(delay, period, null);
	}

	/**
	 * 定时任务构造器
	 * @param delay 首次延时
	 * @param period 间隔时间
	 * @param onTimerListener 执行回TimerHandler调
	 */
	public TimerHandler(long delay, long period, OnTimerListener onTimerListener){
		super();
		this.mDelay = delay;
		this.mPeriod = period;
		this.mListener = onTimerListener;
		initRunnable();
	}
	
	private void initRunnable(){
		isRunning = true;
		mRunnable = new Runnable(){
			@Override
			public void run(){
				if(isRunning && mListener != null){
					mListener.onChange();
				}
			}
		};
		mTimer.schedule(this, mDelay, mPeriod);
	}

	@Override
	public void run(){
		mHandler.post(mRunnable);
	}

	/**
	 * 启动
	 * @return TimerHandler实例
	 */
	public TimerHandler start(){
		isRunning = true;
		return this;
	}
	
	/**
	 * 暂停
	 */
	public void pause(){
		isRunning = false;
	}
	
	/**
	 * 销毁定时器
	 */
	public boolean cancel(){
		pause();
		return super.cancel();
	}
	
	public void setOnTimerListener(OnTimerListener onTimerListener){
		this.mListener = onTimerListener;
	}
	
	public interface OnTimerListener {
		void onChange();
	}
}
