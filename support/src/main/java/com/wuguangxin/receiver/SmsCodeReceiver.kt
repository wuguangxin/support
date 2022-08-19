package com.wuguangxin.receiver

import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.text.TextUtils

/**
 * 短信验证码接收者（广播接收者）
 *
 * Created by wuguangxin on 15/3/18
 */
class SmsCodeReceiver(private val context: Context, private val callback: Callback?) {
    private var receiver: MyReceiver? = null
    private var smsKeyword: String? = null
    get() = field
    set(value) {
        field = value
    }

    /**
     * 启动短信广播接收者
     * @return SmsCodeReceiver
     */
    fun register(): SmsCodeReceiver {
        val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        receiver = MyReceiver()
        context.registerReceiver(receiver, filter)
        return this
    }

    /**
     * 停止短信广播接收者
     */
    fun unregister() {
        if (receiver != null) {
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    private inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdusObj = bundle["pdus"] as Array<*>?
                var smsMessage: SmsMessage?
                if (pdusObj == null) return
                for (p in pdusObj) {
                    smsMessage = SmsMessage.createFromPdu(p as ByteArray)
                    val number = smsMessage.originatingAddress // 发送者
                    val msg = smsMessage.messageBody // 短信内容
                    val times = smsMessage.timestampMillis // 发送时间
                    callback?.onReceive(number, msg, times)

                    // 避免是手机号伪造发来的验证信息
                    if (number?.replace("+86", "")?.length != 11) {
                        if (!TextUtils.isEmpty(msg)) {
                            smsKeyword?.let {
                                if (msg.contains(it)) {
                                    // 取冒号后的6位数字
                                    val spaceIndex = msg.indexOf("：") + 1
                                    val msgCode = msg.substring(spaceIndex, spaceIndex + 6)
                                    callback?.onReceiveCode(number, msgCode, times)
                                }
                            }
                        }
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
         * 当收到任何短信时都回调
         *
         * @param number 发送者号码
         * @param msg 短信内容
         * @param times 发送时间（时间戳）
         */
        fun onReceive(number: String?, msg: String?, times: Long)

        /**
         * 接收到验证码时回调
         * @param number 发送人号码
         * @param code 短信验证码
         * @param times 发送时间
         */
        fun onReceiveCode(number: String?, code: String?, times: Long)
    }
}