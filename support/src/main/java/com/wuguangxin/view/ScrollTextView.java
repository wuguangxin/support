package com.wuguangxin.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 跑马灯TextView
 *
 * <p>Created by wuguangxin on 15/12/15 </p>
 */
public class ScrollTextView extends TextView{
	public ScrollTextView(Context context){
		super(context);
		createView();
	}

	public ScrollTextView(Context context, AttributeSet attrs){
		super(context, attrs);
		createView();
	}

	public ScrollTextView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		createView();
	}

	private void createView(){
		setEllipsize(TruncateAt.MARQUEE);
		setMarqueeRepeatLimit(-1);
		setFocusableInTouchMode(true);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect){
		if (focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean focused){
		if (focused) {
			super.onWindowFocusChanged(focused);
		}
	}

	@Override
	public boolean isFocused(){
		return true;
	}
}
