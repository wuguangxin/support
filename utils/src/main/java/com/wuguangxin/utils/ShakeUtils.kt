package com.wuguangxin.utils

import android.content.Context
import android.view.HapticFeedbackConstants
import android.os.Vibrator
import android.view.View

/**
 * 震动工具类
 * Created by wuguangxin on 2017/12/17.
 */
object ShakeUtils {
    /**
     * 让View在触摸时短暂震动
     * @param view
     */
    @JvmStatic
    fun shake(view: View?) {
        view?.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
            HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                    or HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
    }

    /**
     * 震动（需要震动权限）
     * @param context
     */
    /*@JvmStatic
    fun vibrate(context: Context) {
        val vibrator = context.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(100)
    }*/
}