package com.wuguangxin.utils;

import android.view.HapticFeedbackConstants;
import android.view.View;

/**
 * 震动工具类
 * Created by wuguangxin on 2017/12/17.
 */

public class ShakeUtils {

    /**
     * 让View在触摸时短暂震动
     * @param view
     */
    public static void shake(View view){
        if (view != null) {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                            | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }
    }
}
