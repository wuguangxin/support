package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.wuguangxin.R;
import com.wuguangxin.base.TimerHandler;

/**
 * 圆形进度View
 *
 * <p>Created by wuguangxin on 16/8/18 </p>
 */
public class CircleProgressView extends View {
    private final static int DEF_BACKGROUND_COLOR = 0xFFC2EDFF; // 默认进度条底色
    private final static int DEF_START_COLOR = 0xFFFF5803; // 默认进度条渐变开始颜色
    private final static int DEF_END_COLOR = 0xFFFF9119; // 默认进度条渐变结束颜色
    private final static float DEF_START_ANGLE = 158; // 默认扇形跨度大小 158
    private final static float DEF_SWEEP_ANGLE = 224; // 默认扇形跨度大小 224
    private final static float DEF_STROKE_WIDTH = 50; // 默认圆环线条大小
    private final static float DEF_WIDTH = 430; // 默认组件宽度
    private final static float DEF_HEIGHT = 300; // 默认组件高度
    private final static float DEF_PROGRESS = 0.0f; // 默认进度值 0.0F~1.0F

    private int backgroundColor;
    private int startColor;
    private int endColor;
    private float startAngle;   // 开始角度
    private float sweepAngle;   // 进度扇形跨度大小
    private float strokeWidth;
    private float width = DEF_WIDTH; // 宽
    private float height = DEF_HEIGHT; // 高
    private float progress;     // 当前进度百分比 0.0F~1.0F

    private LinearGradient shader;
    private Paint textpaint;
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
        if(a != null){
            backgroundColor = a.getColor(R.styleable.CircleProgressView_background_color, DEF_BACKGROUND_COLOR);
            startColor = a.getColor(R.styleable.CircleProgressView_start_color, DEF_START_COLOR);
            endColor = a.getColor(R.styleable.CircleProgressView_end_color, DEF_END_COLOR);
            strokeWidth = a.getDimension(R.styleable.CircleProgressView_stroke_width, DEF_STROKE_WIDTH);
            startAngle = a.getFloat(R.styleable.CircleProgressView_startAngle, DEF_START_ANGLE);
            sweepAngle = a.getFloat(R.styleable.CircleProgressView_sweepAngle, DEF_SWEEP_ANGLE);
            progress = a.getFloat(R.styleable.CircleProgressView_cur_progress, DEF_PROGRESS);
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
        textpaint = new Paint();
        textpaint.setTextSize(100f);
        textpaint.setColor(Color.RED);

        // 渐变属性
        shader = new LinearGradient(0, 0, 400, 0, new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        // 在直角三角形中30度所对的边是斜边的一半
//        setMeasuredDimension(w, w / 2 / 2 + w / 2);

        int h = (int)(w * DEF_HEIGHT / DEF_WIDTH); // 宽高比=430:300
        setMeasuredDimension(w, h);

        float radius = (w - strokeWidth / 2) / 2;
        float left = strokeWidth / 2;
        float leftTop = left / 2;
        oval = new RectF(left, left, radius*2, radius*2);
        ovalTop = new RectF(leftTop, leftTop, radius*2 + leftTop, radius*2 + leftTop);
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
        if(currentParent > 0){
            paint.setShader(shader);
            canvas.drawArc(oval, startAngle, currentParent, false, paint);
            paint.setShader(null);
        }

        // 顶部圆环
        canvas.drawArc(ovalTop, startAngle, sweepAngle, false, paintTop);
        canvas.restore();
    }

    /**
     * 设置当前进度百分比 0.0F~1.0F
     * @param progress 进度
     */
    public void setProgress(final float progress) {
        if(isUseAnim){
            setProgressOnAnim(progress);
        } else {
            update(progress);
        }
    }

    private float lastProgress = 0; // 记住最后一次进度值(百分比)
    private float curProgress; // 当前增加的进度值
    private TimerHandler mProgressTimer; // 定时器
    private boolean isUseAnim = true; // 是否使用动画效果

    /**
     * 设置是否使用动画进度
     * @param useAnim 是否使用动画
     */
    public void setUseAnim(boolean useAnim) {
        isUseAnim = useAnim;
    }

    /**
     * 动态设置当前进度百分比 0~100
     * @param progress 进度
     */
    public void setProgressOnAnim(final float progress) {
        if(progress == lastProgress) {
            return;
        }
        // 通过判断上一次的进度>=0, 大本次进度小于上一次的进度, 说明更换了产品ID, 所以重置进度为0
        if(progress < lastProgress && lastProgress > 0){
            lastProgress = 0;
        }
        curProgress = 0;
        // 启动定时器
        if(mProgressTimer == null){
            mProgressTimer = new TimerHandler(600, 3, new TimerHandler.OnTimerListener(){
                @Override
                public void onChange(){
                    curProgress += 0.001; // 每次增加 0.1
                    if(curProgress + lastProgress < progress){
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

    // 更新进度
    private void update(float progress) {
        if(this.progress != progress){
            if(progress > 1){
                progress = 1;
            }
            if(progress < 0){
                progress = 0;
            }
            this.progress = progress;
            invalidate();
        }
    }
}