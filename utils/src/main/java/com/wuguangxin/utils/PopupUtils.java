package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * PopupWindow工具类
 *
 * <p>Created by wuguangxin on 15/2/6 </p>
 */
public class PopupUtils{
	private static PopupWindow mPopupWindows;
	private static BitmapDrawable emptyBitmapDrawable = new BitmapDrawable();
	
	/**
	 * 弹出默认PopupWindow, 透明度默认使用0.95f。
	 * 记得在Antivity.finish前调PopupUtils.dismiss();
	 * @param parentView 在哪个View下面弹出
	 * @param msg 消息文本
	 */
	public static void show(View parentView, String msg){
		if (parentView != null) show(createView(parentView.getContext(), msg), parentView, 0.95F);
	}
	
	/**
	 * 弹出默认PopupWindow <br>
	 * 记得在Antivity.finish前调PopupUtils.dismiss();
	 * @param parentView 在哪个View下面弹出
	 * @param msg 消息文本
	 * @param alpha PopupWindow透明度（0~1.0）
	 */
	public static void show(View parentView, String msg, float alpha){
		if (parentView != null) show(createView(parentView.getContext(), msg), parentView, alpha);
	}
	
	/**
	 * 弹出自定义布局的PopupWindow
	 * @param anchorView 在哪个View下面弹出
	 * @param contentView 自定义View
	 * @param alpha PopupWindow透明度（0~1.0）
	 */
	@SuppressLint("NewApi")
	public static void show(View contentView, View anchorView, float alpha){
		if (contentView == null || anchorView == null) {
			return;
		}
		contentView.setAlpha(alpha);
		dismiss();
		
		mPopupWindows = new PopupWindow(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupWindows.setOutsideTouchable(true);
		mPopupWindows.setFocusable(false);
		mPopupWindows.setBackgroundDrawable(emptyBitmapDrawable);
		mPopupWindows.setContentView(contentView);
		mPopupWindows.showAsDropDown(anchorView, 0, 0); //x y轴的偏移量
	}
	
	/**
	 * 显示菜单view
	 * @param context 上下文
	 * @param contentView 显示的内容布局
	 * @param parentView 与那个View对齐
	 * @param width 菜单的宽度
	 * @param height 菜单的高度
	 */
	@SuppressLint("NewApi")
	public static void showMenu(Context context, View contentView, View parentView, int width, int height){
		showMenu(context, contentView, parentView, width, height, 0, 0);
	}
	
	/**
	 * 显示菜单view
	 * @param context 上下文
	 * @param contentView 显示的内容布局
	 * @param parentView 与那个View对齐
	 * @param width 菜单的宽度
	 * @param height 菜单的高度
	 * @param xoff x轴偏移量
	 * @param yoff y轴偏移量
	 */
	@SuppressLint("NewApi")
	public static void showMenu(Context context, View contentView, View parentView, int width, int height, int xoff, int yoff){
		showMenu(context, contentView, parentView, width, height, xoff, yoff, 1);
	}
	
	/**
	 * 显示菜单view
	 * @param context 上下文
	 * @param contentView 显示的内容布局
	 * @param parentView 与那个View对齐
	 * @param width 菜单的宽度
	 * @param height 菜单的高度
	 * @param xoff x轴偏移量
	 * @param yoff y轴偏移量
	 * @param alpha 菜单布局透明度 0.0f~1.0f（sdk版本 大于等于 11 有效）
	 */
	@SuppressLint("NewApi")
	public static void showMenu(Context context, View contentView, View parentView, int width, int height, int xoff, int yoff, float alpha){
		showMenu(context, contentView, parentView, width, height, xoff, yoff, alpha, true);
	}
	
	/**
	 * 显示菜单view
	 * @param context 上下文
	 * @param contentView 显示的内容布局
	 * @param parentView 与那个View对齐
	 * @param width 菜单的宽度
	 * @param height 菜单的高度
	 * @param xoff x轴偏移量
	 * @param yoff y轴偏移量
	 * @param alpha 菜单布局透明度 0.0f~1.0f（sdk版本 大于等于 11 有效）
	 * @param outsideTouchable 点击外部关闭窗口
	 */
	@SuppressLint("NewApi")
	public static void showMenu(Context context, View contentView, View parentView, int width, int height, int xoff, int yoff, float alpha, boolean outsideTouchable){
		if (context == null || parentView == null) {
			return;
		}
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			contentView.setAlpha(alpha);
		}
		if(width > 0){
			width = Utils.dip2px(context, width);
		}
		if(height > 0){
			height = Utils.dip2px(context, height);
		}
		mPopupWindows = new PopupWindow(width, height);
		mPopupWindows.setOutsideTouchable(outsideTouchable);
		mPopupWindows.setFocusable(false);
		mPopupWindows.setBackgroundDrawable(emptyBitmapDrawable);
		mPopupWindows.setContentView(contentView);
		mPopupWindows.showAsDropDown(parentView, xoff, yoff); //x y轴的偏移量
	}
	
	/**
	 * 判断是否是显示中
	 * @return 是否显示中
	 */
	public static boolean isShowing(){
		if(mPopupWindows != null){
			return mPopupWindows.isShowing();
		}
		return false;
	}
	
	/**
	 * 关闭PopupWindows
	 */
	public static void dismiss(){
		if(mPopupWindows != null && mPopupWindows.isShowing()){
			mPopupWindows.dismiss();
		}
	}

	/**
	 * 获取View
	 * @param context 上下文
	 * @param msg 文本
	 * @return View
	 */
	private static View createView(Context context, String msg){
		TextView mTextView = new TextView(context);
		mTextView.setPadding(10, 10, 10, 10);
		mTextView.setGravity(Gravity.CENTER_VERTICAL);
		mTextView.setTextColor(Color.BLACK);
		mTextView.setText(msg);
		return mTextView;
	}
	
	/**
	 * enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效 。
	 * 该方法通过反射设置
	 * @param menu Menu菜单
	 * @param isShow 是否显示icon
	 */
	public static void setIconEnable(Menu menu, boolean isShow){
		try {
			Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
			Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
			m.setAccessible(true);
			//MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)  
			m.invoke(menu, isShow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建一个PopupMenu（in API level 11）
	 * @param context 上下文
	 * @param anchor  父View
	 * @param menuRes menu的配置文件xml
	 * @return PopupMenu
	 */
	@SuppressLint("NewApi")
	public static PopupMenu createPopupMenu(Context context, View anchor, int menuRes){
		PopupMenu pm = new PopupMenu(context, anchor); // 创建PopupMenu对象
		pm.getMenuInflater().inflate(menuRes, pm.getMenu());
		return pm;
	}
	
	/**
	 * 创建一个PopupMenu（in API level 11）
	 * @param context 上下文
	 * @param anchor 父View
	 * @param menuRes menu的配置文件xml
	 * @param isShowIcon 是否显示图标
	 * @return PopupMenu
	 */
	@SuppressLint("NewApi")
	public static PopupMenu createPopupMenu(Context context, View anchor, int menuRes, boolean isShowIcon){
		PopupMenu pm = new PopupMenu(context, anchor); // 创建PopupMenu对象
		pm.getMenuInflater().inflate(menuRes, pm.getMenu());
		if(isShowIcon){
			PopupUtils.setIconEnable(pm.getMenu(), true);
		}
		return pm;
	}

	/**
	 * 设置屏幕的背景透明度
	 *
	 * @param alpha 透明度 0.0F ~ 1.0F
	 */
	public static void setWindowBackgroundAlpha(Activity activity, float alpha) {
		LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = alpha; //0.0-1.0
		activity.getWindow().setAttributes(lp);
	}
}
