package com.wuguangxin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * 左箭头
 *
 * @author wuguangxin
 * @date: 2016-2-24 下午4:29:14
 */
public class Arrow extends Drawable{
	private static final float DEFAULT_WIDTH = 3.5f;
	private static final int DEFAULT_COLOR = Color.WHITE;
	private Context context;
	private Paint paint;
	private float width;
	private int color;
	
	public Arrow(Context context){
		this(context, DEFAULT_COLOR);
	}
	
	public Arrow(Context context, int color){
		this(context, color, DEFAULT_WIDTH);
	}
	
	public Arrow(Context context, int color, float width){
		super();
		this.context = context;
		this.color = color;
		this.width = width;
	}
	
	public void setColor(int color){
		this.color = color;
		invalidateSelf();
	}

	public int getColor(){
		return color;
	}
	
	public void setWidth(float width){
		this.width = width;
		invalidateSelf();
	}
	
	public float getWidth(){
		return width;
	}
	
	@Override
	public void draw(Canvas canvas){
		canvas.restore();
		paint = new Paint();
		paint.setAntiAlias(true);//锯齿不显示
		paint.setStrokeWidth(width);// 线条宽度
		paint.setColor(color);//设置线条颜色  
		
//		canvas.drawColor(Color.RED);//设置画布颜色  
		// 左边点
		float leftX = dip2px(0);
		float leftY = dip2px(0);
		// 上边点
		float topX = dip2px(10);
		float topY = dip2px(-10);
		// 下边点
		float bottomX = dip2px(10);
		float bottomY = dip2px(10);
		
		canvas.drawLine(leftX, leftY+1, topX, topY, paint);
		canvas.drawLine(leftX, leftY-1, bottomX, bottomY, paint);
	}

	@Override
	public void setAlpha(int alpha){}

	@Override
	public void setColorFilter(ColorFilter cf){}

	@Override
	public int getOpacity(){
		return 0;
	}
	
	/**
	 * dip转换为px
	 * @return
	 */
	private float dip2px(float dipValue){
		if(context != null){
			float density = context.getResources().getDisplayMetrics().density;
			dipValue = dipValue * density + 0.5f;
		}
		return dipValue;
	}
}
