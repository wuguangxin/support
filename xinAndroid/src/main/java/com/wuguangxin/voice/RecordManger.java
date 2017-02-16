package com.wuguangxin.voice;

import java.io.File;

import com.wuguangxin.R;
import com.wuguangxin.utils.ToastUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 录音管理器
 */
public class RecordManger{
	private final String moveMsg = "上滑取消录音";
	private final String cancelMsg = "松手取消录音";
	private Context context;
	private View mDialogView;
	private TextView mVoiceMoveMsg; // 移动手指提示
	private TextView mVoiceCancelMsg; // 取消提示
	private ImageView mAmplitudeView; // 声音振幅View
	private PopupWindow mPopupWindows;
	private MediaRecorder mMediaRecorder;
	private OnRecordListener onRecordListener;
	private Chronometer mChronometer;
	private TimeCount mTimeCount;
	private File savePath;
	private File file;
	private int[] amplitudeImgs = new int[7]; // 显示录音振幅的图片缓存
	private int postDelayed = 100;
	private long maxDuration = 60; // 最大录制时长(秒)
	private boolean isRecording; // 正在录音中
	private final Handler mHandler = new Handler(); // 启动计时器监听振幅波动
	
	private Runnable mRunnable = new Runnable(){
		private int BASE = 600; // 分贝的计算公式K=20lg(Vo/Vi) Vo当前振幅值 Vi基准值为600
		private int RATIO = 5;
		@Override
		public void run(){
			if (getCurrentTimelong() >= getMaxDuration()) {
				stop();
			} else {
				int ratio = mMediaRecorder.getMaxAmplitude() / BASE;
				int db = (int) (20 * Math.log10(Math.abs(ratio)));
				int value = db / RATIO;
				if (value < 0) {
					value = 0;
				}
				if (value >= 6) {
					value = 6;
				}
				mAmplitudeView.setImageResource(amplitudeImgs[value]);// 切换震幅图片
				mHandler.postDelayed(mRunnable, postDelayed);
			}
		}
	};

	/**
	 * 构造器
	 * @param context 上下文
	 * @param savePath 保存目录
	 */
	public RecordManger(Context context, File savePath){
		this(context, savePath, null);
	}

	/**
	 * 构造器
	 * @param context 上下文
	 * @param savePath 保存目录
	 * @param onRecordListener 录音监听器
	 */
	public RecordManger(Context context, File savePath, OnRecordListener onRecordListener){
		this.context = context;
		this.setSavePath(savePath);
		this.onRecordListener = onRecordListener;
		initDialog();
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void initDialog(){
		if (mDialogView == null) {
			initAmplitude();
			mDialogView = LayoutInflater.from(context).inflate(R.layout.xin_voice_dialog_layout, null);
			mChronometer = (Chronometer) mDialogView.findViewById(R.id.voice_chronometer);
			mAmplitudeView = (ImageView) mDialogView.findViewById(R.id.voice_amplitude);// 振幅进度条
			mVoiceMoveMsg = (TextView) mDialogView.findViewById(R.id.voice_move_msg);// 
			mVoiceCancelMsg = (TextView) mDialogView.findViewById(R.id.voice_cancel_msg);//
			mPopupWindows = new PopupWindow(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mPopupWindows.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindows.setOutsideTouchable(true);
			mPopupWindows.setFocusable(false);
			mPopupWindows.setContentView(mDialogView);
			mVoiceMoveMsg.setText(moveMsg);
			mVoiceCancelMsg.setText(cancelMsg);
		}
	}

	private void initAmplitude(){
		amplitudeImgs[0] = R.drawable.voice_mic_1;
		amplitudeImgs[1] = R.drawable.voice_mic_2;
		amplitudeImgs[2] = R.drawable.voice_mic_3;
		amplitudeImgs[3] = R.drawable.voice_mic_4;
		amplitudeImgs[4] = R.drawable.voice_mic_5;
		amplitudeImgs[5] = R.drawable.voice_mic_6;
		amplitudeImgs[6] = R.drawable.voice_mic_7;
	}
	
	/**
	 * 启动录音(默认录音最大时长 60 秒)
	 */
	public boolean start(){
		return start(maxDuration);
	}
	
	/**
	 * 启动录音
	 * @param maxDuration 指定录音最大时长（秒）
	 * @return
	 */
	@SuppressLint("InlinedApi")
	public boolean start(long maxDuration){
		try {
			if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				ToastUtils.showToast(context, "未安装SD卡！");
				return false;
			}
			if(savePath == null){
				ToastUtils.showToast(context, "语音存储路径有误！");
				return false;
			}
			this.isRecording = true;
			this.maxDuration = maxDuration;
			mVoiceCancelMsg.setVisibility(View.GONE);
			mTimeCount = new TimeCount(maxDuration * 1000, 1000);
			if (onRecordListener != null) {
				onRecordListener.onStart();
			}
			file = new File(savePath, String.format("s%.amr", System.currentTimeMillis()));
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();  															// 创建文件
			mMediaRecorder = new MediaRecorder(); 						// 创建录音对象
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); 				// 从麦克风源进行录音
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);		// 设置输出格式
			// 设置音频编码格式 AMR_WB音质比AMR_NB好些
			int encoder = Build.VERSION.SDK_INT < 10 ? AudioEncoder.AMR_NB : AudioEncoder.AMR_WB;
			mMediaRecorder.setAudioEncoder(encoder);
			mMediaRecorder.setOutputFile(file.getAbsolutePath());// 设置输出文件
			mMediaRecorder.prepare(); 												// 准备录制
			mMediaRecorder.start();													// 开始录制
			mTimeCount.start();															// 启动倒计时
			startChronometer();
			showDialog();
			mHandler.post(mRunnable); 											// 启动振幅监听计时器
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtils.showToast(context, "录音失败！");
			if (file != null && file.exists()) {
				file.delete();
			}
			isRecording = false;
			return false;
		}
	}
	
	/**
	 * 取消录音
	 */
	public void cancel(){
		if (isRecording) {
			isRecording = false;
			init();
			if (onRecordListener != null) {
				onRecordListener.onCancel();
			}
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * 停止录音并返回录音文件
	 * @return
	 */
	public void stop(){
		if (isRecording) {
			isRecording = false;
			long time = stopChronometer();
			mVoiceCancelMsg.setVisibility(View.GONE);
			init();
			if (time < 1) {
				if (onRecordListener != null) {
					onRecordListener.onCancel();
				}
				if (file != null && file.exists()) {
					file.delete();
				}
			} else {
				if (onRecordListener != null && file != null) {
					VoiceInfo vi = new VoiceInfo(file, String.valueOf(time));
					onRecordListener.onStop(vi);
					file = null;
				}
			}
		}
	}
	
	private void init(){
		mTimeCount.cancel();
		if (mMediaRecorder != null) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
			mHandler.removeCallbacks(mRunnable);
			dismissDialog();
		}
	}

	/**
	 * 启动计时器
	 */
	private void startChronometer(){
		if (mChronometer != null) {
			mChronometer.setBase(SystemClock.elapsedRealtime());
			mChronometer.start();
		}
	}

	public void setSavePath(File savePath) {
		this.savePath = savePath;
	}

	/**
	 * 停止并重置计时器，返回计时时长（秒）
	 * @return
	 */
	private long stopChronometer(){
		if (mChronometer != null) {
			long time = getCurrentTimelong();
			mChronometer.setBase(SystemClock.elapsedRealtime());
			return time;
		}
		return 0;
	}

	/**
	 * 获取当前录制的时长
	 * @return
	 */
	private long getCurrentTimelong(){
		return (SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000;
	}

	/**
	 * 显示返回信息
	 * @param isShow
	 */
	public void showBackMsg(boolean isShow){
		if (mVoiceCancelMsg != null) {
			mVoiceCancelMsg.setVisibility(isShow ? View.VISIBLE : View.GONE);
		}
	}

	private void showDialog(){
		if (mPopupWindows != null && !mPopupWindows.isShowing()) {
			mPopupWindows.showAtLocation(mDialogView, Gravity.CENTER, 0, 0);
		}
	}

	private void dismissDialog(){
		if (mPopupWindows != null && mPopupWindows.isShowing()) {
			mPopupWindows.dismiss();
		}
	}

	public boolean isRecording(){
		return isRecording;
	}

	public OnRecordListener getOnRecordListener(){
		return onRecordListener;
	}

	public void setOnRecordListener(OnRecordListener onRecordListener){
		this.onRecordListener = onRecordListener;
	}

	/**
	 * 获取最大录制时长
	 * @return
	 */
	public long getMaxDuration(){
		return maxDuration;
	}

	/**
	 * 设置最大录制时长（秒），默认60秒
	 * @param maxDuration
	 */
	public void setMaxDuration(long maxDuration){
		this.maxDuration = maxDuration;
	}

	/**
	 * 录音监听器
	 * 
	 * @author wuguangxin
	 * @date: 2015-6-9 上午10:44:28
	 */
	public static class OnRecordListener{
		/**
		 * 开始录音
		 */
		public void onStart(){};

		/**
		 * 暂停录音
		 */
		public void onPause(){};

		/**
		 * 取消录音
		 */
		public void onCancel(){};

		/**
		 * 停止录音
		 * @param voiceInfo
		 */
		public void onStop(VoiceInfo voiceInfo){};
	}

	/**
	 * 语音信息
	 * 
	 * @author wuguangxin
	 * @date: 2015-6-10 下午5:01:58
	 */
	public class VoiceInfo{
		/**
		 * 录音文件
		 * @return
		 */
		public File file; // 录音文件
		/**
		 * 录音时长
		 * @return
		 */
		public String length; // 录音时长

		public VoiceInfo(File file, String length){
			this.file = file;
			this.length = length;
		}
	}
	
	/**
	 * 倒计时
	 */
	class TimeCount extends CountDownTimer{
		/**
		 * @param millisInFuture 总时长
		 * @param countDownInterval 计时的时间间隔
		 */
		public TimeCount(long millisInFuture, long countDownInterval){
			super(millisInFuture, countDownInterval);
		}

		/**
		 * 计时完毕时触发
		 */
		@Override
		public void onFinish(){
		}

		/**
		 * 计时过程显示
		 */
		@Override
		public void onTick(long millisUntilFinished){
			mVoiceMoveMsg.setText(moveMsg + "(" + millisUntilFinished / 1000 + ")");
		}
	}
}
