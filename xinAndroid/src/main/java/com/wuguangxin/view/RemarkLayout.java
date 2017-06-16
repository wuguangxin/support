package com.wuguangxin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 监听然键盘弹出隐藏的布局
 */
public class RemarkLayout extends RelativeLayout{
	public RemarkLayout(Context context) {
		super(context);
	}

	public RemarkLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RemarkLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		if(onSoftKeyboardListener != null){
			if(h < oldh && oldh - h > 20){ // 软键盘占高肯定大于20
				onSoftKeyboardListener.onShow();
			} else {
				onSoftKeyboardListener.onHide();
			}
		}
	}
	
	private OnSoftKeyboardListener onSoftKeyboardListener;
	
	public void setOnSoftKeyboardListener(OnSoftKeyboardListener onSoftKeyboardListener){
		this.onSoftKeyboardListener = onSoftKeyboardListener;
	}
	
	/**
	 * 软键盘弹出隐藏监听器
	 */
	public interface OnSoftKeyboardListener{
		/**
		 * 键盘显示
		 */
		void onShow();
		/**
		 * 键盘隐藏
		 */
		void onHide();
	}
}
