package com.wuguangxin.receiver

import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context

/**
 * 手势密码必须的姿态观察者
 *
 * Created by wuguangxin on 15/3/31
 */
class GestureReceiver(private val context: Context, callback: Callback?) {
    private var receiver: Receiver?
    private var registered = false // 是否注册过

    init {
        receiver = Receiver(callback)
    }

    /**
     * 注册监听
     */
    fun register() {
        if (!registered) {
            registered = true
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            context.applicationContext.registerReceiver(receiver, filter)
        }
    }

    /**
     * 注销监听
     */
    fun unregister() {
        if (registered && receiver != null) {
            registered = false
            context.applicationContext.unregisterReceiver(receiver)
            receiver = null
        }
    }

    /**
     * 广播接收者
     */
    class Receiver(var callback: Callback?) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (callback == null || intent == null) {
                return
            }
            when (intent.action) {
                Intent.ACTION_SCREEN_ON -> callback?.onScreenON() // 屏幕开
                Intent.ACTION_SCREEN_OFF -> callback?.onScreenOFF() // 屏幕关
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS -> {
                    // 按home键
                    intent.getStringExtra("reason")?.let {
                        if ("recentapps" == it) {
                            callback?.onHomePressedLong() // 长按Home按键 reason="recentapps"
                        } else {
                            callback?.onHomePressed() // 按Home按键 reason="homekey"
                        }
                    }
                }
            }
        }
    }

    /**
     * 监听回调接口
     */
    interface Callback {
        /** Home键被按下（轻按）*/
        fun onHomePressed()

        /** Home键被按下（长按）*/
        fun onHomePressedLong()

        /** 亮屏 */
        fun onScreenON()

        /** 暗屏幕 */
        fun onScreenOFF()
    }
}