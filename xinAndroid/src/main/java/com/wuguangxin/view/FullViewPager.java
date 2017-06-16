package com.wuguangxin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 垂直自适应ViewPager。取最高的Child View作为父ViewPager的高度
 */
public class FullViewPager extends android.support.v4.view.ViewPager {
    private int position;

    public FullViewPager(Context context) {
        super(context);
    }

    public FullViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 取最高的Child View自为父View的高度
        int height = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setPosition(int position) {
        this.position = position;
        invalidate();
    }

}
