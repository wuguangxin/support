package com.wuguangxin.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 根据宽度自动缩放高度的ImageView
 *
 * <p>Created by wuguangxin on 16/11/10 </p>
 */
public class AutoHeightImageView extends androidx.appcompat.widget.AppCompatImageView {

	public AutoHeightImageView(Context context) {
		super(context);
	}

	public AutoHeightImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoHeightImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		if(getDrawable() != null){
			sizeHeight = sizeWidth * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
		}
//		if (sizeWidth == 0) {
//			sizeHeight = 0;
//		}
		setMeasuredDimension(sizeWidth, sizeHeight);
	}
}
