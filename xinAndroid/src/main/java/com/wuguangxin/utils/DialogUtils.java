package com.wuguangxin.utils;

import java.lang.reflect.Field;

import com.wuguangxin.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

/**
 * 对话框工具类
 *
 * @author wuguangxin
 * @date: 2014-9-11 下午4:01:22
 */
public class DialogUtils{

	/**
	 * 设置对话框显示位置、宽、透明度
	 * @param dialog AlertDialog对话框
	 * @param position 要显示在什么位置，左上右下（Gravity取值）
	 * @param width 宽（android.view.ViewGroup.LayoutParams 取值）
	 * @param alpha 透明度0.0-1.0之间float值
	 */
	public static void setDialog(AlertDialog dialog, int position, int width, float alpha){
		Window window = dialog.getWindow();
		window.setGravity(position);
		window.setWindowAnimations(R.style.xin_dialog_from_bottom_in_out);
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
	public static void showDialog(final Context context, String title, String message, final Intent posIntent, final Intent negIntent){
		Builder dialog = new AlertDialog.Builder(context);
		if (title != null) {
			dialog.setTitle(title);
		} else {
			dialog.setTitle("提示");
		}
		if (message != null) {
			dialog.setMessage(message);
		}
		if (negIntent != null) {
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					dialog.cancel();
					context.startActivity(negIntent);
				}
			});
		}
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();
				if (posIntent != null) {
					context.startActivity(posIntent);
				}
			}
		});
		dialog.create().show();
	}

	/**
	 * 点击对话框按钮后对话框不关闭
	 * @param dialog
	 */
	public static void keepDialog(DialogInterface dialog){
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
	 * @param dialog
	 */
	public static void closeDialog(DialogInterface dialog){
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建dialog 并显示
	 * @param context
	 * @return
	 */
	public static Dialog makeGoingDialog(Context context){
		ProgressDialog pd = null;
		if (context != null) {
			pd = new ProgressDialog(context);
			pd.setTitle("提示");
			pd.setMessage("请稍后..");
			pd.show();
		}
		return pd;
	}
}