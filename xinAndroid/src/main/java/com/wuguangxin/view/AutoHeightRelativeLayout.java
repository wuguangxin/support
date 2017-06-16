package com.wuguangxin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 根据背景图片宽高比例，自动LinearLayout的高度。
 *
 * <p>Created by wuguangxin on 16/11/10 </p>
 */
public class AutoHeightRelativeLayout extends RelativeLayout{

	public AutoHeightRelativeLayout(Context context) {
		super(context);
	}

	public AutoHeightRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoHeightRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight(); // 布局宽
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom(); // 布局高
		if(getBackground() != null){
			sizeHeight = sizeWidth * getBackground().getIntrinsicHeight() / getBackground().getIntrinsicWidth();
		}
		getLayoutParams().height = sizeHeight;
	}
}
