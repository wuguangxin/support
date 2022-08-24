package com.wuguangxin.hardware

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal

/**
 * 指纹锁管理器
 * Created by wuguangxin on 2019/11/13.
 */
@SuppressLint("MissingPermission")
class FingerprintLockManager(private val content: Context) {
    private var cancel: CancellationSignal? = null
    private var authenticationCallback: FingerprintManagerCompat.AuthenticationCallback? = null
    private var fingerprint: FingerprintManagerCompat = FingerprintManagerCompat.from(content)

    init {
        //v4包下的API，包装内部已经判断Android系统版本是否大于6.0，这也是官方推荐的方式
    }

    /**
     * 判断设备是否支持指纹解锁
     * Determine if fingerprint hardware is present and functional.
     * @return true if hardware is present and functional, false otherwise.
     */
    val isHardwareDetected: Boolean
        get() = fingerprint.isHardwareDetected

    /**
     * 是否至少有一个指纹注册。
     * Determine if there is at least one fingerprint enrolled.
     * @return true if at least one fingerprint is enrolled, false otherwise
     */
    fun hasEnrolledFingerprints(): Boolean {
        return fingerprint.hasEnrolledFingerprints()
    }

    /**
     * 开始验证
     */
    fun startAuthentication() {
        val crypto: FingerprintManagerCompat.CryptoObject? = null //可以置为null，愿意了解的人可以参考底部博客和源码
        val flags = 0
        cancel = CancellationSignal()
        val handler: Handler? = null // 也可以置为null,系统自动处理
        /**
         * @param crypto object associated with the call or null if none required. //不太理解，加密指纹特征还是什么，可以不加密置为null
         * @param flags optional flags; should be 0  //设置标记，暂时无用
         * @param cancel an object that can be used to cancel authentication  //取消验证
         * @param callback an object to receive authentication events   //系统认证完成之后，回调该接口
         * @param handler an optional handler for events  //处理callback接口后，界面的处理，默认是主线程handler
         */
        fingerprint.authenticate(crypto, flags, cancel, authenticationCallback!!, handler) //验证指纹
    }

    fun setAuthenticationCallback(authenticationCallback: FingerprintManagerCompat.AuthenticationCallback?) {
        this.authenticationCallback = authenticationCallback
    }

    fun cancel() {
        cancel?.cancel()
    }

    //对应不同的错误，可以有不同的操作
    //    public String formatErrorByCode(int code) {
    //        switch (code) {
    //        case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE: // 1
    //            return "设备不支持指纹识别";
    //        case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS: // 2
    //            return content.getString(android.R.string.fingerprint_error_unable_to_process);
    //        case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT: // 3
    //            return content.getString(android.R.string.fingerprint_error_timeout);
    //        case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE: // 4
    //            return content.getString(android.R.string.fingerprint_error_no_space);
    //        case FingerprintManager.FINGERPRINT_ERROR_CANCELED: // 5
    //            return content.getString(android.R.string.fingerprint_error_canceled);
    //        case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT: // 7
    //            return content.getString(android.R.string.fingerprint_error_lockout);
    //        case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT_PERMANENT: // 9
    //            return content.getString(android.R.string.fingerprint_error_lockout_permanent);
    //        case FingerprintManager.FINGERPRINT_ERROR_USER_CANCELED: // 10
    //            return content.getString(android.R.string.fingerprint_error_user_canceled);
    //        case FingerprintManager.FINGERPRINT_ERROR_NO_FINGERPRINTS: // 11
    //            return content.getString(android.R.string.fingerprint_error_no_fingerprints);
    //        case FingerprintManager.FINGERPRINT_ERROR_HW_NOT_PRESENT: // 12
    //            return content.getString(android.R.string.fingerprint_error_hw_not_present);
    //
    //        case FingerprintManager.FINGERPRINT_NAME_TEMPLATE:
    //            return content.getString(R.string.fingerprint_name_template);
    //        case FingerprintManager.FINGERPRINT_NOT_RECOGNIZED: // 无法识别
    //            return content.getString(R.string.fingerprint_not_recognized);
    //        case FingerprintManager.FINGERPRINT_AUTHENTICATED: // 已验证指纹
    //            return content.getString(R.string.fingerprint_authenticated);
    //        case FingerprintManager.FINGERPRINT_ERROR_HW_NOT_AVAILABLE: // 指纹硬件无法使用
    //            return content.getString(R.string.fingerprint_error_hw_not_available);
    //        default:
    //            return "指纹识别错误";
    //        }
    //    }
    //    public String formatAcquiredByCode(int acquireInfo) {
    //        switch (acquireInfo) {
    //        case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
    //            return null;
    //        case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL: // 1
    //            return content.getString(R.string.fingerprint_acquired_partial);
    //        case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT: // 2
    //            return content.getString(R.string.fingerprint_acquired_insufficient);
    //        case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY: // 3
    //            return content.getString(R.string.fingerprint_acquired_imager_dirty);
    //        case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW: // 4
    //            return content.getString(R.string.fingerprint_acquired_too_slow);
    //        case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST: // 5
    //            return content.getString(R.string.fingerprint_acquired_too_fast);
    //        default:
    //            if (acquireInfo >= FingerprintManager.FINGERPRINT_ACQUIRED_ASUS_BASE) {
    //                int msgNumber = acquireInfo - FingerprintManager.FINGERPRINT_ACQUIRED_ASUS_BASE;
    //                String[] msgArray = content.getResources().getStringArray(R.array.fingerprint_acquired_vendor);
    //                if (msgNumber < msgArray.length) {
    //                    return msgArray[msgNumber];
    //                }
    //            }
    //        return null;
    //    }

    companion object {
        /** 解锁指纹  */
        const val ACTION_UNLOCK = 0

        /** 设置指纹  */
        const val ACTION_SET = 1

        /** 验证指纹  */
        const val ACTION_CHECK = 2
    }
}