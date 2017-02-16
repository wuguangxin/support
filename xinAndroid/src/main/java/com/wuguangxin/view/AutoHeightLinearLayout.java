package com.wuguangxin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 根据背景图片宽高比例，自动LinearLayout的高度
 * 
 * @author wuguangxin
 * @date 16/11/10 上午11:45
 */
public class AutoHeightLinearLayout extends LinearLayout {

	public AutoHeightLinearLayout(Context context) {
		super(context);
	}

	public AutoHeightLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
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
