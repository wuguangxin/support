package com.wuguangxin.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 语音播放器
 * 
 * <p>Created by wuguangxin on 17/6/14 </p>
 */
public class VoicePlayer{
	private float mLeftVolume = 1.0f; // 左声道
	private float mRightVolume = 1.0f; // 右声道
	private MediaPlayer mp;
	private OnPlayerChangeListener onPlayerChangeListener;
	private TelephonyManager telephonyManager;
	
	public VoicePlayer(Context context){
		this(context, null);
	}

	public VoicePlayer(Context context, OnPlayerChangeListener onPlayerChangeListener){
		this.onPlayerChangeListener = onPlayerChangeListener;
		telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);	// 监听来电状态
	}
	
	private OnCompletionListener onCompletionListener = new OnCompletionListener(){
		@Override
		public void onCompletion(MediaPlayer mp){
			stop();
		}
	};

	public void play(String path) throws Exception{
		play(path, false);
	}

	/**
	 * 根据path路径播放背景音乐
	 * 
	 * @param path :assets中的音频路径
	 * @param isLoop :是否循环播放
	 * @throws Exception 
	 */
	public void play(String path, boolean isLoop) throws Exception{
		if (TextUtils.isEmpty(path)) {
			return;
		}
		if(mp == null){
			mp = new MediaPlayer();
			mp.setOnCompletionListener(onCompletionListener);
			mp.setDataSource(path);
			mp.prepare();
			mp.start();
			if (onPlayerChangeListener != null) {
				onPlayerChangeListener.onStart();
			}
		} else if(mp.isPlaying()){
			pause();
		} else {
			mp.start();
			if (onPlayerChangeListener != null) {
				onPlayerChangeListener.onStart();
			}
		}
	}

	/**
	 * 停止播放背景音乐
	 */
	public void stop(){
		if (mp != null) {
			mp.stop();
			mp.reset();
			mp.release();
			mp = null;
		}
		if (onPlayerChangeListener != null) {
			onPlayerChangeListener.onStop();
		}
	}

	/**
	 * 暂停播放背景音乐
	 */
	public void pause(){
		if (mp != null) {
			mp.pause();
			if (onPlayerChangeListener != null) {
				onPlayerChangeListener.onPause();
			}
		}
	}

	/**
	 * 可以使播放器从Error状态中恢复过来，重新会到Idle状态。
	 */
	public void reset(){
		if (mp != null) {
			mp.reset();
			if (onPlayerChangeListener != null) {
				onPlayerChangeListener.onReset();
			}
		}
	}

	/**
	 * 重播
	 */
	public void replay(){
		if (mp != null) {
			mp.seekTo(0);
			if (onPlayerChangeListener != null) {
				onPlayerChangeListener.onReplay();
			}
		}
	}

	/**
	 * 判断背景音乐是否正在播放
	 * 
	 * @return 是否正在播放
	 */
	public boolean isPlaying(){
		try {
			return mp.isPlaying(); // 如果播放器已经释放了资源，调此方法会报错
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 得到背景音乐的“音量”
	 * 
	 * @return 音量值
	 */
	public float getVolume(){
		return (mLeftVolume + mRightVolume) / 2;
	}

	/**
	 * 设置背景音乐的音量
	 * 
	 * @param volume ：设置播放的音量，float类型
	 */
	public void setVolume(float volume){
		this.mLeftVolume = volume;
		this.mRightVolume = volume;
		if (mp != null) {
			mp.setVolume(mLeftVolume, mRightVolume);
		}
	}

	/**
	 * 设置左声道音量
	 * 
	 * @param volumeLeft 左声道音量
	 */
	public void setVolumeLeft(float volumeLeft){
		this.mLeftVolume = volumeLeft;
		if (mp != null) {
			mp.setVolume(mLeftVolume, mRightVolume);
		}
	}

//	/**
//	 * @param path assets中的音频路径
//	 * @return
//	 */
//	public MediaPlayer createMediaplayerFromAssets(String path){
//		try {
//			MediaPlayer mediaPlayer = new MediaPlayer();
//			AssetFileDescriptor assetFileDescritor = context.getAssets().openFd(path);
//			mediaPlayer.setDataSource(assetFileDescritor.getFileDescriptor(), assetFileDescritor.getStartOffset(), assetFileDescritor.getInterval());
//			mediaPlayer.setDataSource(path);
//			mediaPlayer.prepare();
//			mediaPlayer.setVolume(mLeftVolume, mRightVolume);
//			return mediaPlayer;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	/**
	 * 设置播放监听器
	 * @param onPlayerChangeListener
	 */
	public void setOnPlayerChangeListener(OnPlayerChangeListener onPlayerChangeListener){
		this.onPlayerChangeListener = onPlayerChangeListener;
	}

	/**
	 * 播放监听器
	 *
	 * <p>Created by wuguangxin on 15/6/11 </p>
	 */
	public static class OnPlayerChangeListener{
		/**
		 * 开始播放
		 */
		public void onStart(){}

		/**
		 * 结束播放
		 */
		public void onPause(){}
		
		/**
		 * 暂停播放
		 */
		public void onStop(){}

		/**
		 * 从Error中恢复时
		 */
		public void onReset(){}

		/**
		 * 重新播放
		 */
		public void onReplay(){}
	}
	
	/**
	 * 定义一个用来监听电话状态的监听器
	 */
	private class MyPhoneStateListener extends PhoneStateListener {
		private boolean flag;
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:	// 震铃(incomingNumber只能在震铃状态获取)
					if (mp != null && mp.isPlaying()) {
						mp.pause();
						flag = true;
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:	// 摘机
					break;
				case TelephonyManager.CALL_STATE_IDLE:		// 空闲
					if (mp != null && !mp.isPlaying() && flag) {
						mp.start();	// 当挂掉电话后继续播放,如果不做这一步，那么挂掉电话后播放器还是暂停的。
						flag = false;
					}
					break;
			}
		}
	}
}