package com.wuguangxin.simple.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class ChildView extends TextView {

    public ChildView(Context context) {
        super(context);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // View
    public boolean viewDispatchSuper = true;
    public boolean viewDispatchReturn;
    public boolean viewDispatchReturnSuper = true;

    public boolean viewTouchSuper = true;
    public boolean viewTouchReturn;
    public boolean viewTouchReturnSuper = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (viewDispatchSuper) {
            boolean b = super.dispatchTouchEvent(event);
            if (viewDispatchReturnSuper) {
                viewDispatchReturn = b;
            }
        }
        return viewDispatchReturn;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (viewTouchSuper) {
            boolean b = super.onTouchEvent(event);
            if (viewTouchReturnSuper) {
                viewTouchReturn = b;
            }
        }
        requestLayout();
        return viewTouchReturn;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
