package com.wuguangxin.voice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wuguangxin.support.R;
import com.wuguangxin.utils.PermissionUtils;
import com.wuguangxin.utils.ToastUtils;

import java.io.File;

/**
 * 语音对话录音管理器
 * <p>Created by wuguangxin on 15/6/9 </p>
 */
public class RecordManger {
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
    private int[] amplitudeImgArr = new int[7]; // 显示录音振幅的图片缓存
    private int postDelayed = 100; // 延时执行
    private long maxDuration = 60; // 最大录制时长(秒)
    private boolean isRecording; // 正在录音中
    private final Handler mHandler = new Handler(); // 启动计时器监听振幅波动

    /**
     * 构造器
     *
     * @param context 上下文
     * @param savePath 保存目录
     */
    public RecordManger(Context context, File savePath) {
        this(context, savePath, null);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param savePath 保存目录
     * @param onRecordListener 录音监听器
     */
    public RecordManger(Context context, File savePath, OnRecordListener onRecordListener) {
        this.context = context;
        this.setSavePath(savePath);
        this.onRecordListener = onRecordListener;
        initDialog();
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void initDialog() {
        if (mDialogView == null) {
            initAmplitude();
            mDialogView = LayoutInflater.from(context).inflate(R.layout.xin_voice_dialog_layout, null);
            mChronometer = mDialogView.findViewById(R.id.xin_voice_chronometer);
            mAmplitudeView = mDialogView.findViewById(R.id.xin_voice_amplitude);// 振幅进度条
            mVoiceMoveMsg = mDialogView.findViewById(R.id.xin_voice_cancel_slither_msg);//
            mVoiceCancelMsg = mDialogView.findViewById(R.id.xin_voice_cancel_loosen_msg);//
            mPopupWindows = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindows.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindows.setOutsideTouchable(true);
            mPopupWindows.setFocusable(false);
            mPopupWindows.setContentView(mDialogView);
            mVoiceMoveMsg.setText(moveMsg);
            mVoiceCancelMsg.setText(cancelMsg);
        }
    }

    private void initAmplitude() {
        amplitudeImgArr[0] = R.drawable.voice_mic_1;
        amplitudeImgArr[1] = R.drawable.voice_mic_2;
        amplitudeImgArr[2] = R.drawable.voice_mic_3;
        amplitudeImgArr[3] = R.drawable.voice_mic_4;
        amplitudeImgArr[4] = R.drawable.voice_mic_5;
        amplitudeImgArr[5] = R.drawable.voice_mic_6;
        amplitudeImgArr[6] = R.drawable.voice_mic_7;
    }

    /**
     * 启动录音(默认录音最大时长 60 秒) 6.0+需要动态申请录音权限：Manifest.permission.RECORD_AUDIO,
     *
     * @return 是否播放成功
     */
    public boolean start() {
        boolean permission = PermissionUtils.checkPermission(context, Manifest.permission.RECORD_AUDIO);
        if (!permission) {
            PermissionUtils.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            ToastUtils.showToast(context, "需要\"麦克风\"权限");
            return false;
        }
        return start(maxDuration);
    }

    /**
     * 启动录音。
     * 关于音频格式和解码器：https://blog.csdn.net/dodod2012/article/details/80474490
     *
     * @param maxDuration 指定录音最大时长（秒）
     * @return 是否播放成功
     */
    @SuppressLint("InlinedApi")
    public boolean start(long maxDuration) {
        try {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                ToastUtils.showToast(context, "未安装SD卡！");
                return false;
            }
            if (savePath == null) {
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
            file = new File(savePath, String.format("%s.amr", System.currentTimeMillis()));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();// 创建文件
            mMediaRecorder = new MediaRecorder();// 创建录音对象
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// 从麦克风（MIC）源进行录音
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);// 设置输出格式
            // 设置音频编码格式 AMR_WB音质比AMR_NB好些
            int encoder = Build.VERSION.SDK_INT < 10 ? AudioEncoder.AMR_NB : AudioEncoder.AMR_WB;
            mMediaRecorder.setAudioEncoder(AudioEncoder.AAC);
            mMediaRecorder.setOutputFile(file.getAbsolutePath()); // 设置输出文件
            mMediaRecorder.prepare();// 准备录制
            mMediaRecorder.start();// 开始录制
            mTimeCount.start();// 启动倒计时
            startChronometer();
            showDialog();
            mHandler.post(mRunnable);// 启动振幅监听计时器
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
    public void cancel() {
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
     */
    public void stop() {
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

    /**
     * 初始化
     */
    private void init() {
        mTimeCount.cancel();
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mHandler.removeCallbacks(mRunnable);
            dismissDialog();
        }
    }

    private Runnable mRunnable = new Runnable() {
        final int BASE = 600; // 分贝的计算公式K=20lg(Vo/Vi) Vo当前振幅值 Vi基准值为600
        final int RATIO = 5;

        @Override
        public void run() {
            if (getCurrentDuration() >= getMaxDuration()) {
                stop();
            } else {
                int maxAmplitude = mMediaRecorder.getMaxAmplitude();
                System.out.println("maxAmplitude = " + maxAmplitude);
                int ratio = maxAmplitude / BASE;
                int db = (int) (20 * Math.log10(Math.abs(ratio)));
                int value = db / RATIO;
                if (value < 0) {
                    value = 0;
                }
                if (value >= 6) {
                    value = 6;
                }
                mAmplitudeView.setImageResource(amplitudeImgArr[value]);// 切换震幅图片
                mHandler.postDelayed(mRunnable, postDelayed);
            }
        }
    };

    /**
     * 启动计时器
     */
    private void startChronometer() {
        if (mChronometer != null) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
        }
    }

    /**
     * 保存路径
     *
     * @param savePath
     */
    public void setSavePath(File savePath) {
        this.savePath = savePath;
    }

    /**
     * 停止并重置计时器
     *
     * @return 返回计时时长（秒）
     */
    private long stopChronometer() {
        if (mChronometer != null) {
            long time = getCurrentDuration();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            return time;
        }
        return 0;
    }

    /**
     * 获取当前录制的时长
     *
     * @return 当前录制的时长(秒)
     */
    private long getCurrentDuration() {
        return (SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000;
    }

    /**
     * 显示返回信息
     *
     * @param isShow 是否显示
     */
    public void showBackMsg(boolean isShow) {
        if (mVoiceCancelMsg != null) {
            mVoiceCancelMsg.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 显示对话框
     */
    private void showDialog() {
        if (mPopupWindows != null && !mPopupWindows.isShowing()) {
            mPopupWindows.showAtLocation(mDialogView, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * dismiss 对话框
     */
    private void dismissDialog() {
        if (mPopupWindows != null && mPopupWindows.isShowing()) {
            mPopupWindows.dismiss();
        }
    }

    /**
     * 是否正在录制
     *
     * @return 是否正在录制
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 获取录制监听器
     *
     * @return 录制监听器
     */
    public OnRecordListener getOnRecordListener() {
        return onRecordListener;
    }

    /**
     * 设置录制监听器
     *
     * @param onRecordListener 录制监听器
     */
    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    /**
     * 获取最大录制时长
     *
     * @return 最大录制时长
     */
    public long getMaxDuration() {
        return maxDuration;
    }

    /**
     * 设置最大录制时长（秒），默认60秒
     *
     * @param maxDuration 最大录制时长
     */
    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }

    /**
     * 录音监听器
     * <p>Created by wuguangxin on 15/6/9 </p>
     */
    public interface OnRecordListener {
        /**
         * 开始录音
         */
        void onStart();

        /**
         * 暂停录音
         */
        void onPause();

        /**
         * 取消录音
         */
        void onCancel();

        /**
         * 停止录音
         * @param voiceInfo 语音信息
         */
        void onStop(VoiceInfo voiceInfo);
    }

    /**
     * 语音信息
     * <p>Created by wuguangxin on 15/6/10 </p>
     */
    public class VoiceInfo {
        /**
         * 录音文件
         */
        public File file; // 录音文件
        /**
         * 录音时长
         */
        public String length; // 录音时长

        public VoiceInfo(File file, String length) {
            this.file = file;
            this.length = length;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("VoiceInfo{");
            sb.append("file=").append(file);
            sb.append(", length='").append(length).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * 倒计时
     * <p>Created by wuguangxin on 15/6/9 </p>
     */
    class TimeCount extends CountDownTimer {
        /**
         * @param millisInFuture 总时长
         * @param countDownInterval 计时的时间间隔
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 计时完毕时触发
         */
        @Override
        public void onFinish() {
        }

        /**
         * 计时过程显示
         */
        @Override
        public void onTick(long millisUntilFinished) {
            mVoiceMoveMsg.setText(moveMsg + "(" + millisUntilFinished / 1000 + ")");
        }
    }
}
