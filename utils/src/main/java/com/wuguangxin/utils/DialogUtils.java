package com.wuguangxin.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 对话框工具类
 * <p>Created by wuguangxin on 14/9/11 </p>
 */
public class DialogUtils {

    /**
     * 设置对话框显示位置、宽、透明度
     *
     * @param dialog AlertDialog对话框
     * @param position 要显示在什么位置，左上右下（Gravity取值）
     * @param width 宽（android.view.ViewGroup.LayoutParams 取值）
     * @param alpha 透明度0.0-1.0之间float值
     */
    public static void setDialog(AlertDialog dialog, int position, int width, float alpha) {
        if (dialog == null) return;
        Window window = dialog.getWindow();
        window.setGravity(position);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width;
        lp.alpha = alpha;
        window.setAttributes(lp);
    }

    /**
     * 弹出普通对话框
     *
     * @param context 上下文
     * @param title 对话框标题
     * @param message 对话框内容
     * @param posIntent 点击确定后的意图
     * @param negIntent 点击取消后的意图
     */
    public static AlertDialog showDialog(final Context context, String title, String message, final Intent posIntent, final Intent negIntent) {
        Builder dialog = new Builder(context);
        dialog.setTitle(title != null ? title : "提示");
        if (message != null) {
            dialog.setMessage(message);
        }
        if (negIntent != null) {
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    context.startActivity(negIntent);
                }
            });
        }
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (posIntent != null) {
                    context.startActivity(posIntent);
                }
            }
        });
		AlertDialog alertDialog = dialog.create();
		alertDialog.show();
		return alertDialog;
    }

    /**
     * 点击对话框按钮后对话框不关闭
     *
     * @param dialog
     */
    public static void keepDialog(DialogInterface dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击对话框按钮后对话框可关闭
     *
     * @param dialog
     */
    public static void closeDialog(DialogInterface dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}