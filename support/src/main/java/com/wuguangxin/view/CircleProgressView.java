package com.wuguangxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wuguangxin.R;
import com.wuguangxin.base.TimerHandler;

/**
 * 圆形进度View
 * <p>Created by wuguangxin on 16/8/18 </p>
 */
public class CircleProgressView extends View {
    private String TAG = "CircleProgressView";
    private int backgroundColor = 0xFFC2EDFF; // 默认进度条底色
    private int startColor = 0xFFFF5803; // 默认进度条渐变开始颜色
    private int endColor = 0xFFFF9119; // 默认进度条渐变结束颜色
    private int strokeWidth = 50;   // 默认圆环线条大小
    private float startAngle = 0;   // 开始角度
    private float sweepAngle = 0;   // 扇形跨度
    private float progress = 0.0f;  // 进度值 0.0F~1.0F
    private int width; // 组件的宽度
    private int height; // 组件的高度

    public static final int BASE_LINE_RIGHT = 0; // 右
    public static final int BASE_LINE_BOTTOM = 1; // 下
    public static final int BASE_LINE_LEFT = 2; // 左
    public static final int BASE_LINE_TOP = 3; // 上
    /**
     * 基准线位置。0右，1下，2左，3上
     */
    private int baseLine = BASE_LINE_BOTTOM;

    private LinearGradient shader;
    private Paint textPaint;
    private Paint paint;
    private Paint paintTop;
    private RectF oval;
    private RectF ovalTop;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        if (a != null) {
            backgroundColor = a.getColor(R.styleable.CircleProgressView_background_color, backgroundColor);
            startColor = a.getColor(R.styleable.CircleProgressView_start_color, startColor);
            endColor = a.getColor(R.styleable.CircleProgressView_end_color, endColor);
            strokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressView_stroke_width, strokeWidth);
            sweepAngle = a.getFloat(R.styleable.CircleProgressView_sweepAngle, sweepAngle);
            progress = a.getFloat(R.styleable.CircleProgressView_progress, progress);
//            baseLine = a.getInt(R.styleable.CircleProgressView_baseLine, baseLine);
            a.recycle();
        }

        // 进度圆环画笔
        paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        // 顶部圆环画笔
        paintTop = new Paint();
        paintTop.setStrokeWidth(10f);
        paintTop.setColor(Color.WHITE);
        paintTop.setStyle(Paint.Style.STROKE);
        paintTop.setAntiAlias(true);

        // 文字画笔
        textPaint = new Paint();
        textPaint.setTextSize(100f);
        textPaint.setColor(Color.RED);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec); // 组件的宽度
        initData();
    }


    private void initData() {
        Log.e(TAG,"=========================================initData()");
        // 渐变属性
        shader = new LinearGradient(0, 0, 400, 0, new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);

        if (progress > 1) progress = 1;
        if (progress < 0) progress = 0;

        float offset = baseLine * 90;
        if (sweepAngle >= 0) {
            startAngle = 180 - sweepAngle / 2 + offset;
        } else {
            startAngle = offset;
        }

        // 得到三角形斜边（就是内圆的半径）
        int hypotenuse;
        if (sweepAngle >= 180) {
            hypotenuse = width / 2;   // 大于等于180度时，外圆于底部最接近(等于180度，可忽略差距)
        } else {
            hypotenuse = (width - strokeWidth * 2) / 2;   // 小于180度时，内圆于底部最接近
        }

        // 三角形夹角
        float triangleAngle = 0;
        if (sweepAngle < 180) {
            triangleAngle = (180 + offset) - startAngle;
        } else if (sweepAngle > 180) {
            triangleAngle = startAngle - offset;
        }

        // 得到组件的高度（三角形直角边高度+圆半径）
        height = hypotenuse;
        // BC=AB*cos∠A=c*cos∠A
        double cosAngle = Math.abs(Math.cos(triangleAngle * (Math.PI / 180)));
        // 求得三角形直角边（高）
        int triangleH = (int) (hypotenuse * cosAngle);
        if (sweepAngle < 180) {
            height = hypotenuse - triangleH;
            height += strokeWidth;
        } else if (sweepAngle > 180) {
            height = hypotenuse + triangleH;
        }

        Log.e(TAG, "基准偏移度 offset       =" + offset);
        Log.e(TAG, "基准线方向 baseLine     =" + baseLine);
        Log.e(TAG, "开始的角度 startAngle   =" + startAngle);
        Log.e(TAG, "内外圆间距 strokeWidth  =" + strokeWidth);
        Log.e(TAG, "扇形的跨度 sweepAngle   =" + sweepAngle);
        Log.e(TAG, "三角函数值 cosAngle     =" + cosAngle);
        Log.e(TAG, "组件的宽度 width        =" + width);
        Log.e(TAG, "组件的高度 height       =" + height);
        Log.e(TAG, "直角边的高 triangleH    =" + triangleH);
        Log.e(TAG, "三角形斜边 hypotenuse   =" + hypotenuse);

        setMeasuredDimension(width, height);

        float radius = (width - strokeWidth / 2) / 2;
        float left = strokeWidth / 2;
        float leftTop = left / 2;
        oval = new RectF(left, left, radius * 2, radius * 2);
        ovalTop = new RectF(leftTop, leftTop, radius * 2 + leftTop, radius * 2 + leftTop);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 画背景圆环
        paint.setColor(backgroundColor);

        canvas.save();
        canvas.drawArc(oval, startAngle, sweepAngle, false, paint);

        // 画进度圆环
        float currentParent = sweepAngle * progress;
        // 进度大于0时才绘画，不然会360度绘制
        if (currentParent > 0) {
            paint.setShader(shader);
            canvas.drawArc(oval, startAngle, currentParent, false, paint);
            paint.setShader(null);
        }

        // 顶部圆环
        canvas.drawArc(ovalTop, startAngle, sweepAngle, false, paintTop);
        canvas.restore();
    }

    private float lastProgress = 0; // 记住最后一次进度值(百分比)
    private float curProgress; // 当前增加的进度值
    private TimerHandler mProgressTimer; // 定时器
    private boolean isUseAnim = true; // 是否使用动画效果

    /**
     * 设置是否使用动画进度
     *
     * @param useAnim 是否使用动画
     */
    public void setUseAnim(boolean useAnim) {
        isUseAnim = useAnim;
    }

    /**
     * 设置扇形跨度（0-360）
     *
     * @param sweepAngle
     */
    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
        initData();
    }

    /**
     * 获取当前跨度
     *
     * @return
     */
    public float getSweepAngle() {
        return sweepAngle;
    }

    /**
     * 设置基线模式
     *
     * @param baseLine （0右，1下，2左，3上）
     */
    public void setBaseLine(int baseLine) {
        this.baseLine = baseLine;
        invalidate();
    }

    public void setHeight(int height) {
        this.height = height;
        invalidate();
        initData();
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public float getProgress() {
        return progress;
    }


    // 更新进度
    private void update(float progress) {
        this.progress = progress;
        invalidate();
        initData();
    }

    /**
     * 设置当前进度百分比 0.0F~1.0F
     *
     * @param progress 进度
     */
    public void setProgress(float progress) {
        if (isUseAnim) {
            setProgressOnAnim(progress);
        } else {
            update(progress);
        }
    }

    /**
     * 动态设置当前进度百分比 0~100
     *
     * @param progress 进度
     */
    public void setProgressOnAnim(final float progress) {
        if (progress == lastProgress) {
            return;
        }
        // 通过判断上一次的进度>=0, 大本次进度小于上一次的进度, 说明更换了产品ID, 所以重置进度为0
        if (progress < lastProgress && lastProgress > 0) {
            lastProgress = 0;
        }
        curProgress = 0;
        // 启动定时器
        if (mProgressTimer == null) {
            mProgressTimer = new TimerHandler(600, 3, new TimerHandler.OnTimerListener() {
                @Override
                public void onChange() {
                    curProgress += 0.001; // 每次增加 0.1
                    if (curProgress + lastProgress < progress) {
                        update(curProgress + lastProgress);
                    } else {
                        update(progress);
                        lastProgress = progress;
                        mProgressTimer.cancel();
                        mProgressTimer = null;
                    }
                }
            });
        }
        mProgressTimer.start();
    }
}