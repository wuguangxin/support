package com.wuguangxin.simple.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.wuguangxin.utils.Logger;

import androidx.annotation.Nullable;

public class GroupView1 extends LinearLayout {


    private static final String TAG = "View1";

    public GroupView1(Context context) {
        super(context);
    }

    public GroupView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure(). getMeasuredWidth()="+getMeasuredWidth());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout(). getMeasuredWidth()="+getMeasuredWidth());
        Log.e(TAG, "onLayout(). getWidth()="+getWidth());
    }

    // ViewGroup
    public boolean groupDispatchSuper = true;
    public boolean groupDispatchReturn;
    public boolean groupDispatchReturnSuper = true;

    public boolean groupInterceptSuper = true;
    public boolean groupInterceptReturn;
    public boolean groupInterceptReturnSuper = true;

    public boolean groupTouchSuper = true;
    public boolean groupTouchReturn;
    public boolean groupTouchReturnSuper = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (groupDispatchSuper) {
            boolean b = super.dispatchTouchEvent(event);
            println("super.dispatchTouchEvent() " + b);
            if (groupDispatchReturnSuper) {
                groupDispatchReturn = b;
            }
        }
        println("dispatchTouchEvent " + groupDispatchReturn);
        return groupDispatchReturn;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (groupInterceptSuper) {
            boolean b = super.onInterceptTouchEvent(event);
            println("super.onInterceptTouchEvent() " + b);
            if (groupInterceptReturnSuper) {
                groupInterceptReturn = b;
            }
        }
        println("onInterceptTouchEvent " + groupInterceptReturn);
        return groupInterceptReturn;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (groupTouchSuper) {
            boolean b = super.onTouchEvent(event);
            println("super.onTouchEvent() " + b);
            if (groupTouchReturnSuper) {
                groupTouchReturn = b;
            }
        }
        println("onTouchEvent " + groupTouchReturn);
        return groupTouchReturn;
    }

    private void println(String s) {
        Logger.e(getTag().toString(), s);
    }
}
