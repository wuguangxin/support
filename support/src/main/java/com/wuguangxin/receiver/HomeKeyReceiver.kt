package com.wuguangxin.receiver

import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context

/**
 * Home键状态观察者
 *
 * Created by wuguangxin on 15/3/31
 */
class HomeKeyReceiver(private val context: Context, callback: Callback?) {
    private var receiver: Receiver?
    private var registered = false

    companion object {
        const val REASON_KEY = "reason"
        const val REASON_GLOBAL_ACTIONS = "globalactions"
        const val REASON_RECENT_APPS = "recentapps"
        const val REASON_HOME_KEY = "homekey"
    }

    init {
        receiver = Receiver(callback)
    }

    /**
     * 开始Home键监听
     */
    fun register() {
        if (!registered) {
            registered = true
            val filter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            context.applicationContext.registerReceiver(receiver, filter)
        }
    }

    /**
     * 停止Home键监听
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
            if (callback != null && intent != null) {
                if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS == intent.action) {
                    when (intent.getStringExtra(REASON_KEY)) {
                        REASON_HOME_KEY -> callback?.onHomePressed() // 短按home键
                        REASON_RECENT_APPS -> callback?.onHomeLongPressed() // 长按home键
                    }
                }
            }
        }
    }

    /**
     * 回调接口
     */
    interface Callback {
        /**
         * Home键被按下
         */
        fun onHomePressed()

        /**
         * Home键被长按
         */
        fun onHomeLongPressed()
    }
}