package com.wuguangxin.simple.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.utils.ShakeUtils;
import com.wuguangxin.utils.ViewUtils;

/**
 * ${DESC}
 * Created by wuguangxin on 2017/12/17.
 */

public class MyView extends View{

    private Paint paint;

    private Point point1 = new Point(300, 400);
    private Point point2 = new Point(0, 0);
    private float radius = 100F;
    private float strokeWidth = 5;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(Color.GREEN);
    }

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("wgx", "=============================================");
        // 以最小的值为实际尺寸
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("wgx", "measureWidth="+measureWidth);
        Log.e("wgx", "measureHeight="+measureHeight);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.e("wgx", "widthMode="+widthMode);
        Log.e("wgx", "heightMode="+heightMode);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            Log.e("wgx", "widthMode == MeasureSpec.UNSPECIFIED");
        }

        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();

        // 最小宽度
        int minWidth = (int) (point1.x + radius + strokeWidth);
        // 最小高度
        int minHeight = (int) (point1.y + radius + strokeWidth);
        Log.e("wgx", "minWidth="+minWidth);
        Log.e("wgx", "minHeight="+minHeight);

        if (measureWidth < minWidth) measureWidth = minWidth;
        if (measureHeight < minHeight) measureHeight = minHeight;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            measureWidth = measureWidth;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            measureWidth = Math.min(minWidth, measureWidth);
        } else {
            //Be whatever you want
            measureWidth = minWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            measureHeight = measureHeight;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            measureHeight = Math.min(minHeight, measureHeight);
        } else {
            //Be whatever you want
            measureHeight = minHeight;
        }

        Log.e("wgx", "判断类型===============");
        Log.e("wgx", "measureWidth="+measureWidth);
        Log.e("wgx", "measureHeight="+measureHeight);

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int padX = paddingLeft + paddingRight;
        int padY = paddingTop + paddingBottom;

        canvas.drawCircle(point1.x + padX, point1.y + padY, radius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            point2.set((int)event.getX(), (int)event.getY());
            updateStatus();
            Log.e("wgx", "按下");
            break;
        case MotionEvent.ACTION_MOVE:
            Log.e("wgx", "移动");
            point2.set((int)event.getX(), (int)event.getY());
//            updateStatus();
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            Log.e("wgx", "抬起");
            point2.set(-1, -1);
            updateStatus();
            break;
        }
        return true;
//        return super.onTouchEvent(event);

    }

    private void updateStatus() {
//        boolean within = ViewUtils.withinCircle(point2, point1, radius);
        boolean within = ViewUtils.withinCircle(point2.x, point2.y, point1.x, point1.y, radius);
        if (within) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            invalidate();
            Log.e("wgx", "在圆内");
            ShakeUtils.shake(this);
        } else{
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            invalidate();
            Log.e("wgx", "在圆外");
        }
    }
}
