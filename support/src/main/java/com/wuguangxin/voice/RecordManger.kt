package com.wuguangxin.voice

import android.Manifest
import kotlin.jvm.JvmOverloads
import android.widget.TextView
import android.widget.PopupWindow
import android.media.MediaRecorder
import android.widget.Chronometer
import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.wuguangxin.support.R
import android.view.ViewGroup
import android.graphics.drawable.BitmapDrawable
import android.app.Activity
import android.content.Context
import android.media.MediaRecorder.AudioEncoder
import android.os.*
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.wuguangxin.utils.PermissionUtils
import com.wuguangxin.utils.ToastUtils
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder

/**
 * 语音对话录音管理器
 *
 * Created by wuguangxin on 15/6/9
 */
class RecordManger @JvmOverloads constructor(
    private val context: Context,
    savePath: File?,
    onRecordListener: OnRecordListener? = null
) {
    private val moveMsg = "上滑取消录音"
    private val cancelMsg = "松手取消录音"
    private var mDialogView: View? = null
    private var mVoiceMoveMsg: TextView? = null // 移动手指提示
    private var mVoiceCancelMsg: TextView? = null // 取消提示
    private var mAmplitudeView: ImageView? = null // 声音振幅View
    private var mPopupWindows: PopupWindow? = null
    private var mMediaRecorder: MediaRecorder? = null

    /**
     * 设置录制监听器
     *
     * @param onRecordListener 录制监听器
     */
    var onRecordListener: OnRecordListener?
    private var mChronometer: Chronometer? = null
    private var mTimeCount: TimeCount? = null
    private var savePath: File? = null
    private var file: File? = null
    private val amplitudeImgArr = IntArray(7) // 显示录音振幅的图片缓存
    private val postDelayed = 100 // 延时执行

    /**
     * 设置最大录制时长（秒），默认60秒
     *
     * @param maxDuration 最大录制时长
     */
    var maxDuration: Long = 60 // 最大录制时长(秒)

    /**
     * 是否正在录制
     *
     * @return 是否正在录制
     */
    var isRecording= false // 正在录音中
        private set
    private val mHandler = Handler() // 启动计时器监听振幅波动
    @SuppressLint("InflateParams")
    private fun initDialog() {
        mDialogView?.let {
            initAmplitude()
            mDialogView = LayoutInflater.from(context).inflate(R.layout.xin_voice_dialog_layout, null)
            mChronometer = it.findViewById(R.id.xin_voice_chronometer)
            mAmplitudeView = it.findViewById(R.id.xin_voice_amplitude) // 振幅进度条
            mVoiceMoveMsg = it.findViewById(R.id.xin_voice_cancel_slither_msg) //
            mVoiceCancelMsg = it.findViewById(R.id.xin_voice_cancel_loosen_msg) //
            mPopupWindows = PopupWindow(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            mPopupWindows?.apply {
                setBackgroundDrawable(BitmapDrawable())
                isOutsideTouchable = true
                isFocusable = false
                contentView = mDialogView
            }
            mVoiceMoveMsg?.text = moveMsg
            mVoiceCancelMsg?.setText(cancelMsg)
        }
    }

    private fun initAmplitude() {
        amplitudeImgArr[0] = R.drawable.voice_mic_1
        amplitudeImgArr[1] = R.drawable.voice_mic_2
        amplitudeImgArr[2] = R.drawable.voice_mic_3
        amplitudeImgArr[3] = R.drawable.voice_mic_4
        amplitudeImgArr[4] = R.drawable.voice_mic_5
        amplitudeImgArr[5] = R.drawable.voice_mic_6
        amplitudeImgArr[6] = R.drawable.voice_mic_7
    }

    /**
     * 启动录音(默认录音最大时长 60 秒) 6.0+需要动态申请录音权限：Manifest.permission.RECORD_AUDIO,
     *
     * @return 是否播放成功
     */
    fun start(): Boolean {
        val permission = PermissionUtils.checkPermission(
            context, Manifest.permission.RECORD_AUDIO
        )
        if (!permission) {
            PermissionUtils.requestPermissions(context as Activity, arrayOf(Manifest.permission.RECORD_AUDIO), 0)
            ToastUtils.showToast(context, "需要\"麦克风\"权限")
            return false
        }
        return start(maxDuration)
    }

    /**
     * 启动录音。
     * 关于音频格式和解码器：https://blog.csdn.net/dodod2012/article/details/80474490
     *
     * @param maxDuration 指定录音最大时长（秒）
     * @return 是否播放成功
     */
    @SuppressLint("InlinedApi")
    fun start(maxDuration: Long): Boolean {
        return try {
            if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
                ToastUtils.showToast(context, "未安装SD卡！")
                return false
            }
            if (savePath == null) {
                ToastUtils.showToast(context, "语音存储路径有误！")
                return false
            }
            isRecording = true
            this.maxDuration = maxDuration
            mVoiceCancelMsg?.visibility = View.GONE
            mTimeCount = TimeCount(maxDuration * 1000, 1000)

            onRecordListener?.onStart()

            file = File(savePath, String.format("%s.amr", System.currentTimeMillis()))
            file?.let {
                if (it.parentFile != null && !it.parentFile.exists()) {
                    it.parentFile.mkdirs()
                }
                it.createNewFile() // 创建文件
            }
            mMediaRecorder = MediaRecorder() // 创建录音对象
            mMediaRecorder?.let {
                it.setAudioSource(MediaRecorder.AudioSource.DEFAULT) // 从麦克风（MIC）源进行录音
                it.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT) // 设置输出格式
                it.setAudioEncoder(AudioEncoder.AAC) // 设置音频编码格式 AMR_WB音质比AMR_NB好些
                it.setOutputFile(file?.absolutePath) // 设置输出文件
                it.prepare() // 准备录制
                it.start() // 开始录制
            }
            mTimeCount!!.start() // 启动倒计时
            startChronometer()
            showDialog()
            mHandler.post(mRunnable) // 启动振幅监听计时器
            true
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showToast(context, "录音失败！")
            if (file?.exists() == true) {
                file?.delete()
            }
            isRecording = false
            false
        }
    }

    /**
     * 取消录音
     */
    fun cancel() {
        if (isRecording) {
            isRecording = false
            init()
            if (onRecordListener != null) {
                onRecordListener!!.onCancel()
            }
            if (file?.exists() == true) {
                file?.delete()
            }
        }
    }

    /**
     * 停止录音并返回录音文件
     */
    fun stop() {
        if (isRecording) {
            isRecording = false
            val time = stopChronometer()
            mVoiceCancelMsg!!.visibility = View.GONE
            init()
            if (time < 1) {
                onRecordListener?.onCancel()
                if (file?.exists() == true) {
                    file?.delete()
                }
            } else {
                if (onRecordListener != null && file != null) {
                    file?.let {
                        val vi = VoiceInfo(it, time.toString())
                        onRecordListener?.onStop(vi)
                    }
                }
            }
        }
    }

    /**
     * 初始化
     */
    private fun init() {
        mTimeCount?.cancel()
        mMediaRecorder?.let {
            it.stop()
            it.release()
            mHandler.removeCallbacks(mRunnable)
            dismissDialog()
        }
        mMediaRecorder = null
    }

    private val mRunnable: Runnable = object : Runnable {
        val BASE = 600 // 分贝的计算公式K=20lg(Vo/Vi) Vo当前振幅值 Vi基准值为600
        val RATIO = 5
        override fun run() {
            if (currentDuration >= maxDuration) {
                stop()
            } else {
                val maxAmplitude = mMediaRecorder!!.maxAmplitude
                println("maxAmplitude = $maxAmplitude")
                val ratio = maxAmplitude / BASE
                val db = (20 * Math.log10(Math.abs(ratio).toDouble())).toInt()
                var value = db / RATIO
                if (value < 0) {
                    value = 0
                }
                if (value >= 6) {
                    value = 6
                }
                mAmplitudeView?.setImageResource(amplitudeImgArr[value]) // 切换震幅图片
                mHandler.postDelayed(this, postDelayed.toLong())
            }
        }
    }

    /**
     * 启动计时器
     */
    private fun startChronometer() {
        mChronometer?.let {
            it.base = SystemClock.elapsedRealtime()
            it.start()
        }
    }

    /**
     * 保存路径
     *
     * @param savePath
     */
    fun setSavePath(savePath: File?) {
        this.savePath = savePath
    }

    /**
     * 停止并重置计时器
     *
     * @return 返回计时时长（秒）
     */
    private fun stopChronometer(): Long {
        if (mChronometer != null) {
            val time = currentDuration
            mChronometer!!.base = SystemClock.elapsedRealtime()
            return time
        }
        return 0
    }

    /**
     * 获取当前录制的时长
     *
     * @return 当前录制的时长(秒)
     */
    private val currentDuration: Long
        private get() = (SystemClock.elapsedRealtime() - mChronometer!!.base) / 1000

    /**
     * 显示返回信息
     *
     * @param isShow 是否显示
     */
    fun showBackMsg(isShow: Boolean) {
        if (mVoiceCancelMsg != null) {
            mVoiceCancelMsg!!.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    /**
     * 显示对话框
     */
    private fun showDialog() {
        if (mPopupWindows != null && !mPopupWindows!!.isShowing) {
            mPopupWindows!!.showAtLocation(mDialogView, Gravity.CENTER, 0, 0)
        }
    }

    /**
     * dismiss 对话框
     */
    private fun dismissDialog() {
        if (mPopupWindows != null && mPopupWindows!!.isShowing) {
            mPopupWindows!!.dismiss()
        }
    }

    /**
     * 录音监听器
     *
     * Created by wuguangxin on 15/6/9
     */
    interface OnRecordListener {
        /**
         * 开始录音
         */
        fun onStart()

        /**
         * 暂停录音
         */
        fun onPause()

        /**
         * 取消录音
         */
        fun onCancel()

        /**
         * 停止录音
         * @param voiceInfo 语音信息
         */
        fun onStop(voiceInfo: VoiceInfo?)
    }

    /**
     * 语音信息
     *
     * Created by wuguangxin on 15/6/10
     */
    inner class VoiceInfo(
        /**
         * 录音文件
         */
        var file: File, // 录音文件

        /**
         * 录音时长
         */
        var length: String // 录音时长

    ) {
        override fun toString(): String {
            val sb = StringBuilder("VoiceInfo{")
            sb.append("file=").append(file)
            sb.append(", length='").append(length).append('\'')
            sb.append('}')
            return sb.toString()
        }
    }

    /**
     * 倒计时
     *
     * @param millisInFuture 总时长
     * @param countDownInterval 计时的时间间隔
     * Created by wuguangxin on 15/6/9
     */
    internal inner class TimeCount(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        /**
         * 计时完毕时触发
         */
        override fun onFinish() {}

        /**
         * 计时过程显示
         */
        override fun onTick(millisUntilFinished: Long) {
            mVoiceMoveMsg?.text = moveMsg + "(" + millisUntilFinished / 1000 + ")"
        }
    }
    /**
     * 构造器
     *
     * @param context 上下文
     * @param savePath 保存目录
     * @param onRecordListener 录音监听器
     */
    /**
     * 构造器
     *
     * @param context 上下文
     * @param savePath 保存目录
     */
    init {
        setSavePath(savePath)
        this.onRecordListener = onRecordListener
        initDialog()
    }
}