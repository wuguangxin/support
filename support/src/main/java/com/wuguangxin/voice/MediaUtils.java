package com.wuguangxin.voice;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;

import java.io.File;

public class MediaUtils{
	private static MediaPlayer mediaPlayer = new MediaPlayer(); //播放器对象 

	/**
	 * 播放
	 * @param file
	 */
	public static void play(File file){
		play(file.getPath());
	}

	/**
	 * 播放
	 * @param path
	 */
	public static void play(String path){
		if (!TextUtils.isEmpty(path)) {
			try {
				if (mediaPlayer == null) {
					init();
				}
				mediaPlayer.reset();	// 把各项参数恢复到最初始的状态  
				mediaPlayer.setDataSource(path); // 设置数据源
				mediaPlayer.prepare(); // 准备
				mediaPlayer.start(); // 开始
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void init(){
		mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp){
				mp.release(); // 当结束时释放资源
			}
		});
		mediaPlayer.setOnPreparedListener(new OnPreparedListener(){
			public void onPrepared(MediaPlayer mp){
				mp.start();//开始播放  
			}
		});
	}

	/**
	 * 暂停播放
	 */
	public static void pause(){
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	/**
	 * 停止播放
	 */
	public static void stop(){
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}

	/**
	 * 重新播放
	 */
	public static void rePlay(){
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);	//从开始位置播放
		}
	}

	/**
	 * 释放资源
	 */
	public static void release(){
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
