package com.wuguangxin.receiver

import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context

/**
 * 屏幕开关广播接收器辅助类
 *
 * Created by wuguangxin on 14/5/12
 */
class ScreenReceiver(
    private val content: Context,
    private var callback: Callback?,
) {
    private var receiver: Receiver? = null
    private var registered = false

    /**
     * 注册屏幕状态监听
     * Intent.ACTION_SCREEN_OFF 屏幕灭屏广播；
     * Intent.ACTION_SCREEN_ON 屏幕亮屏广播；
     * Intent.ACTION_USER_PRESENT 屏幕解锁广播；
     * Intent.ACTION_CLOSE_SYSTEM_DIALOGS 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播；
     * example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
     * 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
     */
    fun register() {
        if (!registered) {
            registered = true
            receiver = Receiver(callback)
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_ON)
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            content.applicationContext.registerReceiver(receiver, filter)
        }
    }

    /**
     * 注销屏幕状态监听
     */
    fun unregister() {
        if (registered) {
            registered = false
            content.applicationContext.unregisterReceiver(receiver)
        }
    }

    /**
     * 屏幕开关广播接收者
     */
    class Receiver(var callback: Callback?) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            callback?.let {
                when (intent.action) {
                    Intent.ACTION_SCREEN_ON -> it.onScreenON() // 屏幕开
                    Intent.ACTION_SCREEN_OFF -> it.onScreenOFF() // 屏幕关
                }
            }
        }
    }

    /**
     * 屏幕状态监听器
     */
    interface Callback {
        /** 打开  */
        fun onScreenON()

        /** 关闭  */
        fun onScreenOFF()
    }
}