package com.wuguangxin.base

import android.os.Handler
import kotlin.jvm.JvmOverloads
import com.wuguangxin.base.TimerHandler
import com.wuguangxin.base.TimerHandler.OnTimerListener
import java.util.*

/**
 * 定时处理器。指定首次启动延时、之后间隔执行延时和定时监听器，然后在回调中执行想要做的任务
 *
 * Created by wuguangxin on 15/8/9
 */
class TimerHandler @JvmOverloads constructor(
    private val mDelay: Long = DEFAULT_DELAY, // 首次延时
    private val mPeriod: Long = DEFAULT_PERIOD, // 间隔时间
    private var mListener: OnTimerListener? = null // 执行回TimerHandler调
) : TimerTask() {

    private val mHandler = Handler()
    private val mTimer = Timer(true)
    private var mRunnable: Runnable? = null
    private var isRunning = false

    /**
     * 定时任务构造器。默认延时5秒执行，间隔5秒执行下一次
     */
    init {
        isRunning = true
        mRunnable = Runnable {
            if (isRunning) {
                mListener?.onChange()
            }
        }
        mTimer.schedule(this, mDelay, mPeriod)
    }

    override fun run() {
        mRunnable?.let {
            mHandler.post(it)
        }
    }

    /**
     * 启动
     * @return TimerHandler实例
     */
    fun start(): TimerHandler {
        isRunning = true
        return this
    }

    /**
     * 暂停
     */
    fun pause() {
        isRunning = false
    }

    /**
     * 销毁定时器
     */
    override fun cancel(): Boolean {
        pause()
        return super.cancel()
    }

    fun setOnTimerListener(onTimerListener: OnTimerListener?): TimerHandler {
        mListener = onTimerListener
        return this
    }

    interface OnTimerListener {
        fun onChange()
    }

    companion object {
        private const val DEFAULT_DELAY: Long = 5000
        private const val DEFAULT_PERIOD: Long = 5000
    }

}