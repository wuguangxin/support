package com.wuguangxin.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.wuguangxin.support.R;

import androidx.annotation.RequiresApi;

/**
 * Created by Administrator on 2016/11/17.
 * 仿支付宝芝麻信用圆形仪表盘
 */
public class RoundIndicatorView extends View {
    private final static int DEF_BACKGROUND_COLOR = 0xFFC2EDFF; // 默认进度条底色
    private final static int DEF_START_COLOR = 0xFFFF5803; // 默认进度条渐变开始颜色
    private final static int DEF_END_COLOR = 0xFFFF9119; // 默认进度条渐变结束颜色
    private final static float DEF_START_ANGLE = 158; // 默认扇形跨度大小 158
    private final static float DEF_SWEEP_ANGLE = 224; // 默认扇形跨度大小 224
    private final static float DEF_STROKE_WIDTH = 50; // 默认圆环线条大小

    private Paint paint;
    private Paint paint_2;
    private Paint paint_3;
    private Paint paint_4;
    private Context context;
    private int maxNum; // 信用分
    private float startAngle;
    private float sweepAngle;
    private float radius;
    private int mWidth;
    private int mHeight;
    private float sweepInWidth;//内圆的宽度
    private float sweepOutWidth;//外圆的宽度
    private int currentNum=0;//需设置setter、getter 供属性动画使用
    private String[] text ={"较差","中等","良好","优秀","极好"};
    private int[] indicatorColor = {0xffffffff,0x00ffffff,0x99ffffff,0xffffffff};

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
        invalidate();
    }

    public RoundIndicatorView(Context context) {
        this(context,null);
    }

    public RoundIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundIndicatorView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setBackgroundColor(0x00000000);
        initAttr(attrs);
        initPaint();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setCurrentNumAnim(int num) {
        float duration = (float)Math.abs(num-currentNum)/maxNum *1500+500; //根据进度差计算动画时间
        ObjectAnimator anim = ObjectAnimator.ofInt(this,"currentNum",num);
        anim.setDuration((long) Math.min(duration,2000));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int color = calculateColor(value);
                setBackgroundColor(color);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private int calculateColor(int value){
        ArgbEvaluator evealuator = new ArgbEvaluator();
        float fraction;
        int color;
        if(value <= maxNum/2){
            fraction = (float)value/(maxNum/2);
            color = (int) evealuator.evaluate(fraction,0xFFFF6347,0xFFFF8C00); //由红到橙
        }else {
            fraction = ( (float)value-maxNum/2 ) / (maxNum/2);
            color = (int) evealuator.evaluate(fraction,0xFFFF8C00,0xFF00CED1); //由橙到蓝
        }
        return color;
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(DEF_BACKGROUND_COLOR);

        paint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundIndicatorView);
        maxNum = array.getInt(R.styleable.RoundIndicatorView_maxNum,500);
        startAngle = array.getFloat(R.styleable.RoundIndicatorView_startAngle, DEF_START_ANGLE);
        sweepAngle = array.getFloat(R.styleable.RoundIndicatorView_sweepAngle, DEF_SWEEP_ANGLE);
        //内外圆的宽度
        sweepInWidth = DEF_STROKE_WIDTH;
        sweepOutWidth = dp2px(3);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY ){
            mWidth = wSize;
        }else {
            mWidth =dp2px(430);
        }
        if (hMode == MeasureSpec.EXACTLY ){
            mHeight= hSize;
        }else {
            mHeight =dp2px(300);
        }
        mWidth = wSize;
        mHeight = mWidth;
        radius = getMeasuredWidth() / 2 - (sweepInWidth + sweepOutWidth); //不要在构造方法里初始化，那时还没测量宽高
        setMeasuredDimension(mWidth, (int)(mWidth * (300F/430F)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mWidth/2,(mWidth)/2);
        drawRound(canvas);  //画内外圆
        drawIndicator(canvas); //画当前进度值
//        drawScale(canvas);//画刻度
//        drawCenterText(canvas);//画中间的文字
        canvas.restore();
    }

    private void drawRound(Canvas canvas) {
        canvas.save();
        //内圆
        paint.setAlpha(0x40);
        paint.setStrokeWidth(sweepInWidth);
        RectF rectf = new RectF(-radius,-radius,radius,radius);
        canvas.drawArc(rectf,startAngle,sweepAngle,false,paint);
        //外圆
        paint.setStrokeWidth(sweepOutWidth);
        int w = dp2px(10);
        RectF rectf2 = new RectF(-radius-w , -radius-w , radius+w , radius+w);
        canvas.drawArc(rectf2,startAngle,sweepAngle,false,paint);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        paint_2.setStyle(Paint.Style.STROKE);
        float sweep;
        if(currentNum<=maxNum){
            sweep = (int)((float)currentNum/(float)maxNum*sweepAngle);
        }else {
            sweep = sweepAngle;
        }
        paint_2.setStrokeWidth(sweepOutWidth);
        Shader shader =new SweepGradient(0,0,indicatorColor,null);
        paint_2.setShader(shader);
        int w = dp2px(10);
        RectF rectf = new RectF(-radius-w , -radius-w , radius+w , radius+w);
        canvas.drawArc(rectf,startAngle,sweep,false,paint_2);
        float x = (float) ((radius+dp2px(10))*Math.cos(Math.toRadians(startAngle+sweep)));
        float y = (float) ((radius+dp2px(10))*Math.sin(Math.toRadians(startAngle+sweep)));
        paint_3.setStyle(Paint.Style.FILL);
        paint_3.setColor(0xff000000);
        paint_3.setMaskFilter(new BlurMaskFilter(dp2px(3), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
        canvas.drawCircle(x,y,dp2px(3),paint_3);
        canvas.restore();
    }

    private void drawCenterText(Canvas canvas) {
        canvas.save();
        paint_4.setStyle(Paint.Style.FILL);
        paint_4.setTextSize(radius/2);
        paint_4.setColor(0xff000000);
        canvas.drawText(currentNum+"",-paint_4.measureText(currentNum+"")/2,0,paint_4);
        paint_4.setTextSize(radius/4);
        String content = "信用";
        if(currentNum < maxNum*1/5){
            content += text[0];
        }else if(currentNum >= maxNum*1/5 && currentNum < maxNum*2/5){
            content += text[1];
        }else if(currentNum >= maxNum*2/5 && currentNum < maxNum*3/5){
            content += text[2];
        }else if(currentNum >= maxNum*3/5 && currentNum < maxNum*4/5){
            content += text[3];
        }else if(currentNum >= maxNum*4/5){
            content += text[4];
        }
        Rect r = new Rect();
        paint_4.getTextBounds(content,0,content.length(),r);
        canvas.drawText(content,-r.width()/2,r.height()+20,paint_4);
        canvas.restore();
    }

    private void drawScale(Canvas canvas) {
        canvas.save();
        float angle = (float)sweepAngle/30;//刻度间隔
        canvas.rotate(-270+startAngle); //将起始刻度点旋转到正上方（270)
        for (int i = 0; i <= 30; i++) {
            if(i%6 == 0){   //画粗刻度和刻度值
                paint.setStrokeWidth(dp2px(2));
                paint.setAlpha(0x70);
                canvas.drawLine(0, -radius-sweepInWidth/2,0, -radius+sweepInWidth/2+dp2px(1), paint);
                drawText(canvas,i*maxNum/30+"",paint);
            }else {         //画细刻度
                paint.setStrokeWidth(dp2px(1));
                paint.setAlpha(0x50);
                canvas.drawLine(0,-radius-sweepInWidth/2,0, -radius+sweepInWidth/2, paint);
            }
            if(i==3 || i==9 || i==15 || i==21 || i==27){  //画刻度区间文字
                paint.setStrokeWidth(dp2px(2));
                paint.setAlpha(0x50);
                drawText(canvas,text[(i-3)/6], paint);
            }
            canvas.rotate(angle); //逆时针
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas ,String text ,Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(sp2px(8));
        float width = paint.measureText(text); //相比getTextBounds来说，这个方法获得的类型是float，更精确些
//        Rect rect = new Rect();
//        paint.getTextBounds(text,0,text.length(),rect);
        canvas.drawText(text,-width/2 , -radius + dp2px(15),paint);
        paint.setStyle(Paint.Style.STROKE);
    }

    //一些工具方法
    protected int dp2px(int dp){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }
    protected int sp2px(int sp){
        return (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics());
    }
    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
