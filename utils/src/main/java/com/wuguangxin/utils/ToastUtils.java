package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wuguangxin.R;

import java.lang.ref.WeakReference;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * Toast工具类。
 * Created by wuguangxin on 14/11/28
 */
public class ToastUtils {

    private static final int ICON_SUCCESS = R.drawable.xin_toast_icon;
    private static final int ICON_FAILED = R.drawable.xin_toast_icon;
    private static WeakReference<Toast> mSystemToast;
    private static WeakReference<CustomToast> mToast;

    private static CustomToast getToast(Context context) {
        if (mToast == null) {
            if (context == null) {
                return null;
            }
            mToast = new WeakReference<>(new CustomToast(context));
        }
        return mToast.get();
    }

    @SuppressLint("ShowToast")
    public static void showToast(Context context, String msg, @DrawableRes int resId, boolean isLong) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        CustomToast customToast = getToast(context);
        if (customToast == null) {
            showToastSystem(context, msg);
            return;
        }
        customToast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        customToast.setMessage(msg);
        customToast.show();
    }

    public static void showToastSystem(Context context, String msg) {
        if (mSystemToast == null) {
            mSystemToast = new WeakReference<>(Toast.makeText(context, msg, Toast.LENGTH_LONG));
        }
        Toast toast = mSystemToast.get();
        if (toast != null) {
            toast.setText(msg);
            toast.show();
        }
    }

    public static void showToast(Context context, String msg) {
        showToast(context, msg, 0, false);
    }

    public static void showToast(Context context, String msg, boolean isLong) {
        showToast(context, msg, 0, isLong);
    }

    public static void showSuccess(Context context, String msg) {
        showToast(context, msg, ICON_SUCCESS, false);
    }

    public static void showFailed(Context context, String msg) {
        showToast(context, msg, ICON_FAILED, false);
    }

    static class CustomToast extends Toast {
        View mRootView;
        TextView msgView;

        CustomToast(Context context) {
            super(context);
            mRootView = LayoutInflater.from(context).inflate(R.layout.xin_toast_warn_layout, null);
            msgView = mRootView.findViewById(R.id.xin_toast_text);
            setView(mRootView);
            setGravity(Gravity.TOP, 0, 200);
        }

        public CustomToast setMessage(String text) {
            msgView.setText(text);
            return this;
        }

        public CustomToast setMessage(@StringRes int resid) {
            msgView.setText(resid);
            return this;
        }


    }
}
