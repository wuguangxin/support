package com.wuguangxin.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

/**
 * Created by wuguangxin on 2022/6/26.
 */

public class ViewUtils {

    public static void setDimensionRatio(ConstraintLayout constraintLayout, String dimensionRatio) {
        if (constraintLayout != null) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) constraintLayout.getLayoutParams();
            if (params == null) {
                params = new ConstraintLayout.LayoutParams(-1, -2);
            }
            params.dimensionRatio = dimensionRatio;
            constraintLayout.setLayoutParams(params);
        }
    }

    public static void setText(Activity activity, @IdRes int id, CharSequence text, View.OnClickListener listener) {
        View view = activity.findViewById(id);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(listener);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setText(text);
            }
        }
    }

    public static void setDrawableStart(TextView textView, @DrawableRes int resId) {
        setDrawable(textView, resId, Gravity.START);
    }

    public static void setDrawableTop(TextView textView, @DrawableRes int resId) {
        setDrawable(textView, resId, Gravity.TOP);
    }

    public static void setDrawableEnd(TextView textView, @DrawableRes int resId) {
        setDrawable(textView, resId, Gravity.END);
    }

    public static void setDrawableBottom(TextView textView, @DrawableRes int resId) {
        setDrawable(textView, resId, Gravity.BOTTOM);
    }

    public static void setDrawable(TextView textView, @DrawableRes int resId, int gravity) {
        if (textView != null && resId != -1) {
            Drawable icon = ContextCompat.getDrawable(textView.getContext(), resId);
            if (icon != null) {
                icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                switch (gravity) {
                case Gravity.LEFT:
                case Gravity.START:
                    textView.setCompoundDrawables(icon, null, null, null);
                    break;
                case Gravity.TOP:
                    textView.setCompoundDrawables(null, icon, null, null);
                    break;
                case Gravity.RIGHT:
                case Gravity.END:
                    textView.setCompoundDrawables(null, null, icon, null);
                    break;
                case Gravity.BOTTOM:
                    textView.setCompoundDrawables(null, null, null, icon);
                    break;
                }
            }
        }
    }
}
