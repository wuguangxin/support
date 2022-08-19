package com.wuguangxin.base

import android.os.CountDownTimer
import android.widget.TextView

/**
 * 倒计时辅助类
 *
 *
 * Created by wuguangxin on 15/8/20
 */
class CountDownHelper : CountDownTimer {
    private var button: TextView? = null // 按钮
    private var reText: String? = DEF_REG // 获取成功后,显示的按钮文字(带格式化), 例如 "重新获取(%s秒)"
    private var text: String? = null // 附加的文字，例如 "获取验证码"
    private var countDownInterval: Long = 0 // 间隔时间

    /**
     * @param millisInFuture 总时长
     * @param countDownInterval 计时的时间间隔 (一般1000毫秒)
     */
    constructor(millisInFuture: Long, countDownInterval: Long)
            : super(millisInFuture, countDownInterval) {
    }

    /**
     * @param millisInFuture 总时长
     * @param countDownInterval 计时的时间间隔
     * @param button 显示倒计时的Button
     * @param reText 获取成功后,显示的按钮文字(带格式化), 例如 "重新获取(%s秒)"
     * 注意：总时间加上countDownInterval是因为需要，比如5秒倒计时，需要在最开始时就显示5秒
     */
    constructor(
        millisInFuture: Long,
        countDownInterval: Long,
        button: TextView?,
        reText: String?
    ) : super(millisInFuture + countDownInterval, countDownInterval) {
        this.countDownInterval = countDownInterval
        this.button = button
        this.reText = reText?: DEF_REG
        if (button != null) {
            text = button.text.toString()
        }
    }

    /**
     * 计时过程显示
     */
    override fun onTick(millisUntilFinished: Long) {
        button?.let {
            val curTime = millisUntilFinished / countDownInterval
            it.text = String.format(reText.toString(), curTime)
        }
    }

    /**
     * 计时完毕时触发
     */
    override fun onFinish() {
        button?.isEnabled = true
        button?.text = text
    }

    /**
     * 停止倒计时
     */
    fun stop() {
        cancel()
        onFinish()
    }

    companion object {
        private const val DEF_REG = "%s秒"
    }
}