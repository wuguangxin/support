package com.wuguangxin.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 让高度等于宽度
 *
 * <p>Created by wuguangxin on 16/11/10 </p>
 */
public class AutoHeightTextView extends TextView {

	public AutoHeightTextView(Context context) {
		super(context);
	}

	public AutoHeightTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoHeightTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(sizeWidth, sizeWidth);

		int imageHeight = 0;
		Drawable[] compoundDrawables = getCompoundDrawables();
//		if(compoundDrawables.length > 2){
//			Drawable compoundDrawable = compoundDrawables[1];
//			if(compoundDrawable != null){
//				Bitmap bitmap = IOFormat.getInstance().drawable2Bitmap(compoundDrawable);
//				if(bitmap != null){
//					imageHeight = bitmap.getHeight();
//				}
//			}
//		}
		int compoundDrawablePadding = getCompoundDrawablePadding();
		float textSize = getTextSize();

		int padLen = (int) ((sizeHeight - textSize - compoundDrawablePadding - imageHeight) / 2);

//		setPadding(getPaddingLeft(), padLen, getPaddingRight(), padLen);
	}
}
