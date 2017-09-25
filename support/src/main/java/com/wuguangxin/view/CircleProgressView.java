package com.wuguangxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.wuguangxin.R;
import com.wuguangxin.base.TimerHandler;

/**
 * 圆形进度View
 * <p>Created by wuguangxin on 16/8/18 </p>
 */
public class CircleProgressView extends View {
    private String TAG = "CircleProgressView";
    private int backgroundColor = 0xFFC2EDFF; // 进度条底色
    private int startColor = 0xFFFF5803; // 进度条渐变开始颜色
    private int endColor = 0xFFFF9119; // 进度条渐变结束颜色
    private int topColor = Color.WHITE; // 顶部颜色
    private int strokeWidth = 50;   // 圆环线条大小
    private int topStrokeWidth = 10; // 顶部圆环线条大小
    private float startAngle = 0;   // 开始角度
    private float sweepAngle = 0;   // 扇形跨度
    private float progress = 0.0f;  // 进度值 0.0F~1.0F
    private int width; // 组件的宽度
    private int height; // 组件的高度
    private float offset = 90; // 角度起始点便宜度，默认是右边0度，需要从底部开始，所以偏移90度。

//    private LinearGradient linearGradient;
    private SweepGradient sweepGradient;
    private Paint textPaint;
    private Paint paint;
    private Paint paintTop;
    private RectF oval;
    private RectF ovalTop;
    private boolean showTopOval = true; // 是否显示顶部圆环,默认显示

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
            topStrokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressView_top_stroke_width, topStrokeWidth);
            topColor = a.getColor(R.styleable.CircleProgressView_top_color, topColor);
            sweepAngle = a.getFloat(R.styleable.CircleProgressView_sweepAngle, sweepAngle);
            showTopOval = a.getBoolean(R.styleable.CircleProgressView_showTopOval, showTopOval);
            progress = a.getFloat(R.styleable.CircleProgressView_progress, progress);
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
        paintTop.setStrokeWidth(10);
        paintTop.setColor(topColor);
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
//        Log.e(TAG,"=========================================onMeasure()");

        if (progress > 1) progress = 1;
        if (progress < 0) progress = 0;

        if (sweepAngle >= 0) {
            startAngle = 180 - sweepAngle / 2 + offset;
        } else {
            startAngle = offset;
        }

        width = MeasureSpec.getSize(widthMeasureSpec); // 组件的宽度
        height = getRealHeight();

        setMeasuredDimension(width, height);

        // 这两个渲染器都可以，但选择一总使用即可，推荐SweepGradient
        /*
        线性渐变渲染
        注：Android中计算x,y坐标都是以屏幕左上角为原点，向右为x+，向下为y+
        第一个参数为线性起点的x坐标
        第二个参数为线性起点的y坐标
        第三个参数为线性终点的x坐标
        第四个参数为线性终点的y坐标
        第五个参数为实现渐变效果的颜色的组合
        第六个参数为前面的颜色组合中的各颜色在渐变中占据的位置（比重），如果为空，则表示上述颜色的集合在渐变中均匀出现
        第七个参数为渲染器平铺的模式，一共有三种
        -CLAMP 边缘拉伸
        -REPEAT 在水平和垂直两个方向上重复，相邻图像没有间隙
        -MIRROR 以镜像的方式在水平和垂直两个方向上重复，相邻图像有间隙
         */
//        linearGradient = new LinearGradient(0, 0, 400, 0, new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        // 扇形渐变渲染
        sweepGradient = new SweepGradient(width / 2, width / 2,
                new int[]{startColor, endColor, endColor}, null);
        //扇形渐变默认从0度开始，这里需要旋转到90度
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setRotate(startAngle, width / 2, width / 2);
        sweepGradient.setLocalMatrix(matrix);

        // 顶部圆环参数
        float radius = (width - strokeWidth / 2) / 2;
        float left = strokeWidth / 2;
        float leftTop = left / 2;
        oval = new RectF(left, left, radius * 2, radius * 2);
        ovalTop = new RectF(leftTop, leftTop, radius * 2 + leftTop, radius * 2 + leftTop);
    }

    /**
     * 动态计算组件的实际高度
     * @return
     */
    private int getRealHeight(){
        /*
         已知C的角度，AC的长度，计算AB的长度：AB = AC * cos∠A
         1、小于180度时的三角形：
         B______ C
         │┘90° ╱
         │    ╱
         │   ╱
         │  ╱
         │ ╱
         │╱
         A（圆点）

         2、大于180度时的三角形（可看成倒影）：
         A（圆点）
         │╲
         │ ╲
         │  ╲
         │   ╲
         │    ╲
         │┐90° ╲
         B￣￣￣￣ C
         */

        // 三角形斜边AC（就是内圆的半径）
        int AC;
        // 三角形直角边
        int AB;
        // 三角形夹角∠A，注意扇形大于180度的夹角变化
        float cosA = 0;

        // 计算AC
        if (sweepAngle >= 180) {
            // 大于等于180度时，外圆于底部最接近(等于180度，可忽略差距)
            AC = width / 2;
        } else {
            // 小于180度时，内圆于底部最接近
            AC = (width - strokeWidth * 2) / 2;
        }

        // 计算cosA
        if (sweepAngle < 180) {
            cosA = (180 + offset) - startAngle;
        } else if (sweepAngle > 180) {
            cosA = startAngle - offset;
        }

        // 计算 AB = AC * cosA
        AB = (int) (AC * Math.abs(Math.cos(cosA * (Math.PI / 180))));

        // 计算组件高度（三角形直角边高度+圆半径）
        if (sweepAngle < 180) {
            height = AC - AB;
            height += strokeWidth;
        } else if (sweepAngle > 180) {
            height = AC + AB;
        } else {
            height = AC; // = 180度时，以内圆或外圆直径都一样
        }

//        Log.e(TAG, "基准偏移度 offset       =" + offset);
//        Log.e(TAG, "开始的角度 startAngle   =" + startAngle);
//        Log.e(TAG, "内外圆间距 strokeWidth  =" + strokeWidth);
//        Log.e(TAG, "扇形的跨度 sweepAngle   =" + sweepAngle);
//        Log.e(TAG, "组件的宽度 width        =" + width);
//        Log.e(TAG, "组件的高度 height       =" + height);
//        Log.e(TAG, "三角形角A    cosA       =" + cosA);
//        Log.e(TAG, "三角形斜边     AC       =" + AC);
//        Log.e(TAG, "三角形直角边   BC       =" + AB);

        return height;
    }

    @SuppressLint("DrawAllocation")
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
            //把渐变设置到笔刷
            paint.setShader(sweepGradient);
            canvas.drawArc(oval, startAngle, currentParent, false, paint);
            paint.setShader(null);
        }

        // 顶部圆环
        if (showTopOval) {
            canvas.drawArc(ovalTop, startAngle, sweepAngle, false, paintTop);
        }
        canvas.restore();
    }

    private float lastProgress = 0; // 记住最后一次进度值(百分比)
    private float curProgress; // 当前增加的进度值
    private TimerHandler mProgressTimer; // 定时器
    private boolean isUseAnim = true; // 是否使用动画效果

    /**
     * 设置是否显示顶部的圆环
     * @param showTopOval
     */
    public void setTopOvalVisible(boolean showTopOval){
        if (this.showTopOval != showTopOval) {
            this.showTopOval = showTopOval;
            invalidate();
            requestLayout();
        }
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * 设置底部颜色
     * @param backgroundColor
     */
    public void setBackgroundColor(int backgroundColor) {
        if (this.backgroundColor != backgroundColor) {
            this.backgroundColor = backgroundColor;
            invalidate();
            requestLayout();
        }
    }

    public int getTopColor() {
        return topColor;
    }

    /**
     * 设置顶部颜色
     * @param topColor
     */
    public void setTopColor(int topColor) {
        if (this.topColor != topColor) {
            this.topColor = topColor;
            invalidate();
            requestLayout();
        }
    }

    /**
     * 设置是否使用动画进度
     *
     * @param useAnim 是否使用动画
     */
    public void setUseAnim(boolean useAnim) {
        if (this.isUseAnim != useAnim) {
            this.isUseAnim = useAnim;
            invalidate();
            requestLayout();
        }
    }

    /**
     * 设置扇形跨度（0-360）
     *
     * @param sweepAngle
     */
    public void setSweepAngle(float sweepAngle) {
        if (this.sweepAngle != sweepAngle) {
            this.sweepAngle = sweepAngle;
            invalidate();
            requestLayout();
        }
    }

    /**
     * 获取当前跨度
     *
     * @return
     */
    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setHeight(int height) {
        if (this.height != height) {
            this.height = height;
            invalidate();
            requestLayout();
        }
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
        if (this.progress != progress) {
            this.progress = progress;
            invalidate();
            requestLayout();
        }
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