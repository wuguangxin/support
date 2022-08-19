package com.wuguangxin.utils;

import android.util.Log;
import android.view.View;

/**
 * 点击View的工具类
 * Created by wuguangxin on 2019/11/1.
 */
public class ClickUtils {
    private static final int MIN_CLICK_DELAY_TIME = 700;
    private static long lastClickTime;
    private static int viewId;

    /**
     * 是否过快点击
     * @return
     */
    public static boolean isFastClick() {
        return isFastClick(null);
    }

    /**
     * 是否过快点击
     * @param view 被点击的View，如果上一次点击的不是当前点击的View，不限制点击时间
     * @return
     */
    public static boolean isFastClick(View view) {
        long curClickTime = System.currentTimeMillis();

        if (view != null) {
            int CurrentViewId = view.getId();
            if (CurrentViewId != viewId) {
                viewId = CurrentViewId;
                lastClickTime = curClickTime;
                return false;
            }
        }
        boolean flag = false;
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            Log.d("ClickUtils", "点击太快了....");
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
