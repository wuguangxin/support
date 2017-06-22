package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.wuguangxin.R;

/**
 * Toast工具类。
 * Created by wuguangxin on 14/11/28
 */
public class ToastUtils {
    private static Toast mErrorToast;
    private static Toast mWarnToast;
    private static Toast mFlowToast;
    private static TextView mErrorToastTextView;
    private static TextView mWarnToastTextView;
    private static TextView mFlowToastTextView;

    /**
     * 普通Toast(默认短时间、居中）
     *
     * @param context 上下文
     * @param msg 文本
     */
    public static void showToast(Context context, String msg) {
        warnToast(context, msg, false);
    }

    /**
     * 普通Toast(居中）
     *
     * @param context 上下文
     * @param msg 文本
     * @param isLong 是否是长时间
     */
    public static void showToast(Context context, String msg, boolean isLong) {
        warnToast(context, msg, isLong);
    }

    /**
     * 警告吐司（短时间、居中）
     *
     * @param context 上下文
     * @param msg 文本
     */
    @SuppressLint("InflateParams")
    public static void warnToast(Context context, String msg) {
        warnToast(context, msg, false);
    }

    /**
     * 显示流量吐司-短时间（顶部）
     *
     * @param context 上下文
     * @param msg 文本
     */
    @SuppressLint("InflateParams")
    public static void flowToast(Context context, String msg) {
        flowToast(context, msg, false);
    }

    /**
     * 警告吐司-短时间（居中）
     *
     * @param context
     * @param msg
     */
    @SuppressLint("InflateParams")
    public static void warnToast(Context context, String msg, boolean isLong) {
        if (context != null) {
            try {
                if (isDestroyed(context)) {
                    return;
                }
                if (mWarnToast == null || mWarnToastTextView == null) {
                    mWarnToast = Toast.makeText(context, null, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                    mWarnToast.setGravity(Gravity.CENTER, 0, 0);
                    if (mWarnToastTextView == null) {
                        mWarnToastTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.xin_toast_warn_layout, null);
                    }
                    mWarnToast.setView(mWarnToastTextView);
                }
                mWarnToastTextView.setText(msg);
                mWarnToast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 错误吐司（顶部）
     *
     * @param context 上下文
     * @param msg 吐司信息
     */
    @SuppressLint("InflateParams")
    public static void errorToast(Context context, String msg) {
        if (context != null) {
            try {
                if (isDestroyed(context)) {
                    return;
                }
                if (mErrorToast == null) {
                    mErrorToast = Toast.makeText(context, null, Toast.LENGTH_LONG);
                    mErrorToast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
                    if (mErrorToastTextView == null) {
                        mErrorToastTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.xin_toast_error_layout, null);
                    }
                    mErrorToast.setView(mErrorToastTextView);
                }
                mErrorToastTextView.setText(msg);
                mErrorToast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示流量吐司-短时间（顶部）
     *
     * @param context
     * @param msg
     */
    @SuppressLint("InflateParams")
    public static void flowToast(Context context, String msg, boolean isLong) {
        if (context != null) {
            try {
                if (isDestroyed(context)) {
                    return;
                }
                if (mFlowToast == null || mFlowToastTextView == null) {
                    mFlowToast = Toast.makeText(context, null, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                    mFlowToast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
                    if (mFlowToastTextView == null) {
                        mFlowToastTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.xin_toast_flow_layout, null);
                    }
                    mFlowToast.setView(mFlowToastTextView);
                }
                mFlowToastTextView.setText(msg);
                mFlowToast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断context是否已销毁
     *
     * @param context 上下文
     * @return 是否已销毁
     */
    public static boolean isDestroyed(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context != null) {
            boolean isDestroyed = ((Activity) context).isDestroyed();
            Log.e("aa", "context is isDestroyed=" + isDestroyed);
            return isDestroyed;
        }
        return false;
    }

    /**
     * 打印toast消息到日志
     *
     * @param context 上下文
     * @param msg 消息
     */
    private static void printToast(Context context, String msg) {
        Logger.i(context, msg);
    }
}
