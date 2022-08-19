package com.wuguangxin.receiver

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import com.wuguangxin.utils.Logger
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 短信验证码监测（观察者模式），观察短信数据库的变化来获取短信。
 *
 * Created by wuguangxin on 14/8/17
 */
abstract class SmsCodeObserver(private val context: Context, handler: Handler?, private val callback: Callback?) : ContentObserver(handler) {

    val smsNumber = "" //  短信号码
    val smsKeyword = "" //  短信识别关键字

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        try {
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(
                Uri.parse("content://sms/"),
                arrayOf("address", "body", "date"),
                null,
                null,
                null
            )
            cursor?.let {
                if (cursor.moveToFirst()) {
                    val smsNumber = cursor.getString(0)
                    val smsContent = cursor.getString(1)
                    if (checkSmsNumber(smsNumber)) {
                        findSmsCode(smsContent)?.let {
                            callback?.onReceiveCode(it)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.i("短信接收异常 $e")
        }
    }

    // 验证码短信号码，避免是手机号伪造发来的验证信息
    private fun checkSmsNumber(address: String?): Boolean {
//        address?.let {
//            if (address.replace("+86", "").length != 11) {
//                return false
//            }
//        }
        return true
    }

    // 从短信内容中查找验证码
    open fun findSmsCode(text: String?): String? {
        text?.let {
            val p: Pattern = Pattern.compile("(\\d{6})")
            val m: Matcher = p.matcher(text)
            return if (m.find()) { m.group(0) } else null
        }
        return null
    }

    /**
     * 当检测到验证码时
     * @param smsCode 检测到的验证码
     */
    abstract fun setValue(smsCode: String?)

    /**
     * 回调接口
     */
    interface Callback {
        /**
         * 接收到验证码时回调
         * @param smsCode 短信验证码
         */
        fun onReceiveCode(smsCode: String?)
    }
}