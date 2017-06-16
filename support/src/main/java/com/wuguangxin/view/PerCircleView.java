package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wuguangxin.R;

/**
 * 显示百分比的圆环
 *
 * <p>Created by wuguangxin on 15/7/20 </p>
 */
public class PerCircleView extends ImageView{
	private Context context;
	private boolean isRunning = false;
	private float left;
	private float top;
	private float right;
	private float bottom;
	private float bottomCircleSize; // 底部圆环宽度
	private float topCircleSize; // 上面圆环宽度
	private RectF rect;
	private double maxValue = 0; // 总数
	private double progressValue = 0; // 当前数
	private Rect textRect;
	private Paint textPaint;
	private Paint paint;
	private int baseline;
	private FontMetricsInt fontMetrics;
	private float sweepAngle;
	private float currentAngle;
	private int textColor;
	private float textSize;
	private int topColor;
	private int bottomColor;
	private boolean isFirstRun = true;

	public PerCircleView(Context context){
		this(context, null);
	}

	public PerCircleView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	private void init(AttributeSet attrs){
		// 默认数据
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PerCircleView);
		maxValue = a.getInteger(R.styleable.PerCircleView_maxValue, 0);
		progressValue = a.getInteger(R.styleable.PerCircleView_currentValue, 0);
		textSize = a.getDimension(R.styleable.PerCircleView_textSize, dip2px(14));
		textColor = a.getColor(R.styleable.PerCircleView_textColor, Color.BLACK);
		bottomColor = a.getColor(R.styleable.PerCircleView_bottomColor, Color.LTGRAY);
		topColor = a.getColor(R.styleable.PerCircleView_topColor, Color.RED);
		topCircleSize = a.getDimension(R.styleable.PerCircleView_topCircleSize, dip2px(3));
		bottomCircleSize = a.getDimension(R.styleable.PerCircleView_bottomCircleSize, dip2px(6));
		a.recycle(); // 重复利用
		//
		paint = new Paint();
		paint.setAntiAlias(true); // 消除锯齿   
		paint.setStyle(Style.STROKE);  //绘制空心圆或 空心矩形   
		
		// 文字
	    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
	    textPaint.setStrokeWidth(1);  
	    textPaint.setTextSize(textSize);
		textPaint.setColor(textColor);  
	    
	    sweepAngle = (float) getSweepAngle();
	}

	@Override
	protected void onDraw(Canvas canvas){
		drawCircle(canvas);
		super.onDraw(canvas);
		if(isRunning){
			invalidate();
		}
	}

	private void drawCircle(Canvas canvas){
		left = 0;
		top = 0;
		right = getWidth();
		bottom = getHeight();
		
		//绘制底色
		paint.setStrokeWidth(bottomCircleSize);
		rect = new RectF(left + bottomCircleSize/2, top + bottomCircleSize/2, right - bottomCircleSize/2, bottom - bottomCircleSize/2);
		paint.setColor(bottomColor);
		canvas.drawArc(rect, 270, 360, false, paint);
		
		//绘制进度色
		paint.setStrokeWidth(topCircleSize);
		rect = new RectF(left + topCircleSize/2, top + topCircleSize/2, right - topCircleSize/2, bottom - topCircleSize/2);
		paint.setColor(topColor);
		canvas.drawArc(rect, 270, currentAngle, false, paint);
		
		//绘制文字
		String text = (int)(currentAngle / 360 * 100) +"%";
	    textRect = new Rect((int)left, (int)top, (int)right, (int)bottom);  
	
	    // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()  
	    textPaint.setTextAlign(Paint.Align.CENTER);  
	    fontMetrics = textPaint.getFontMetricsInt();  
	    baseline = textRect.top + (textRect.bottom - textRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  
	    canvas.drawText(text, textRect.centerX(), baseline, textPaint);
	    int n = 2;
	    // 值递增
	    if(currentAngle < sweepAngle){
	    	float curN = sweepAngle - currentAngle;
	    	if(curN <= n){
	    		currentAngle+=curN;
	    	} else {
	    		currentAngle+=n;
	    	}
	    } else {
	    	isRunning = false;
		}
	}
	
	private double getSweepAngle(){
		if(maxValue > 0f){
			return progressValue / maxValue * 360d;
		}
		return 0d;
	}

	/* 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public int dip2px(float dpValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public int px2sp(float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
	
	public void start(){
		if(isFirstRun){
			isFirstRun = false;
			isRunning = true;
			currentAngle = 0;
			invalidate();
		}
	}
	
	public void stop(){
		isRunning = false;
	}
	
	public boolean isRunning(){
		return isRunning;
	}

	public double getMaxValue(){
		return maxValue;
	}

	public void setMaxValue(double maxValue){
		this.maxValue = maxValue;
		invalidate();
	}

	public double getProgressValue(){
		return progressValue;
	}

	public void setProgressValue(double progressValue){
		this.progressValue = progressValue;
		invalidate();
	}
	
	
}
