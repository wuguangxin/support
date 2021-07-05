package com.wuguangxin.simple.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;

/**
 * <p>让高度等于宽度(正方形)
 * <p>com\wuguangxin\simple\view\SquareCheckBox.java
 * <p>Created by wuguangxin on 2021-07-06 0:03
 */
public class SquareCheckBox extends AppCompatCheckBox {

    public SquareCheckBox(Context context) {
        super(context);
    }

    public SquareCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(sizeWidth, sizeWidth);

        int imageHeight = 0;
        Drawable[] compoundDrawables = getCompoundDrawables();
//		if(compoundDrawables.length > 2){
//			Drawable compoundDrawable = compoundDrawables[1];
//			if(compoundDrawable != null){
//				Bitmap bitmap = IOFormat.getInstance().drawableToBitmap(compoundDrawable);
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
