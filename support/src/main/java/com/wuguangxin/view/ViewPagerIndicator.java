package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wuguangxin.support.R;

/**
 * ViewPager 指示器
 *
 * <p>Created by wuguangxin on 16/12/21 </p>
 */
public class ViewPagerIndicator extends View {
	private int colorNormal = 0XFFcccccc; 	// 默认正常颜色
	private int colorSelected = 0XFFee2f3a; // 默认选中颜色
	private int lineSize = 1; 		// 圆圈线条的大小
	private int spaceWidth = 4; 	// 间隔距离
	private int circleRadius = 4; 	// 默认圆的变径
	private int rectWidth = 4; 		// 默认矩形的宽度
	private int rectHeight = 4; 	// 默认矩形的高度
	private int indicatorCount = 0; 			// 默认个数
	private int currentItem = 0; 	// 默认当前显示的位置

	private IndicatorType indicatorType = IndicatorType.Circle; // 指示器类型
	private Paint paint;

	public ViewPagerIndicator(Context context){
		this(context, null);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
		if(a != null){
			colorNormal = a.getColor(R.styleable.ViewPagerIndicator_colorNormal, colorNormal); // 正常颜色
			colorSelected = a.getColor(R.styleable.ViewPagerIndicator_colorSelected, colorSelected); // 选中颜色
			lineSize = a.getDimensionPixelSize(R.styleable.ViewPagerIndicator_lineSize, lineSize);
			circleRadius = a.getDimensionPixelSize(R.styleable.ViewPagerIndicator_circleRadius, circleRadius);
			spaceWidth = a.getDimensionPixelSize(R.styleable.ViewPagerIndicator_spaceWidth, spaceWidth);
			rectWidth = a.getDimensionPixelSize(R.styleable.ViewPagerIndicator_rectWidth, rectWidth);
			rectHeight = a.getDimensionPixelSize(R.styleable.ViewPagerIndicator_rectHeight, rectHeight);
			indicatorCount = a.getInt(R.styleable.ViewPagerIndicator_indicatorCount, indicatorCount);
			currentItem = a.getInt(R.styleable.ViewPagerIndicator_currentItem, currentItem);
			indicatorType = IndicatorType.format(a.getInt(R.styleable.ViewPagerIndicator_indicatorType, 0));

			a.recycle();
		}

		// 画笔设置
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(colorNormal);//设置线条颜色
		paint.setStrokeWidth(lineSize);// 线条宽度
		paint.setAntiAlias(true);//去除锯齿
	}

	private int width, height;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = getPaddingLeft() + getPaddingRight();
		height = getPaddingTop() + getPaddingBottom();

		if(indicatorType == IndicatorType.Circle){
			// 圆形
			width += circleRadius*2*indicatorCount + spaceWidth*indicatorCount; // 计算整个组件的宽度
			height += circleRadius*2;
		} else if(indicatorType == IndicatorType.Rect){
			// 方形
			width += rectWidth*indicatorCount + spaceWidth*indicatorCount; // 计算整个组件的宽度
			height += rectHeight;
		}

		setMeasuredDimension(width, height);
	}

	float r, cx, cy;

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		r = indicatorCount == 0 ? 0 : width / indicatorCount / 2F;
		cy = height / 2f;

		for (int i = 0; i < indicatorCount; i++) {
			cx = r + r * 2 * i;
			if(indicatorType == IndicatorType.Circle){
				// 圆形
				if(currentItem == i){
					paint.setColor(colorSelected);
					canvas.drawCircle(cx, cy, circleRadius, paint); // 选中
				} else {
					paint.setColor(colorNormal);
					canvas.drawCircle(cx, cy, circleRadius, paint); // 未选
				}
			} else if(indicatorType == IndicatorType.Rect){
				// 方形
				if(currentItem == i){
					paint.setColor(colorSelected);
					canvas.drawRect(cx-rectWidth/2, cy-rectHeight/2, cx+rectWidth/2, cy+rectHeight/2, paint);// 选中
				} else {
					paint.setColor(colorNormal);
					canvas.drawRect(cx-rectWidth/2, cy-rectHeight/2, cx+rectWidth/2, cy+rectHeight/2, paint); // 未选
				}
			}
		}
	}

	public void setCurrentItem(int currentItem) {
		if(this.currentItem == currentItem){
			return;
		}
		this.currentItem = currentItem;
		invalidate();
	}

	public void setCount(int indicatorCount) {
		this.indicatorCount = indicatorCount;
		invalidate();
	}

	public void setIndicatorType(IndicatorType indicatorType) {
		this.indicatorType = indicatorType;
	}

	public enum IndicatorType {
		/**
		 * 圆形
		 */
		Circle(0),

		/**
		 * 方形
		 */
		Rect(1);

		IndicatorType(int value) {
			this.value = value;
		}

		private int value;

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public static IndicatorType format(int value){
			if (value == 0) {
				return IndicatorType.Circle;
			} else if (value == 1) {
				return IndicatorType.Rect;
			}
			return IndicatorType.Rect;
		}
	}

	public void setColorNormal(int colorNormal) {
		this.colorNormal = colorNormal;
		invalidate();
	}

	public void setColorSelected(int colorSelected) {
		this.colorSelected = colorSelected;
		invalidate();
	}

	public void setLineSize(int lineSize) {
		this.lineSize = lineSize;
		invalidate();
	}

	public void setCircleRadius(int circleRadius) {
		this.circleRadius = circleRadius;
		invalidate();
	}

	public void setSpaceWidth(int spaceWidth) {
		this.spaceWidth = spaceWidth;
		invalidate();
	}

	public void setRectWidth(int rectWidth) {
		this.rectWidth = rectWidth;
		invalidate();
	}

	public void setRectHeight(int rectHeight) {
		this.rectHeight = rectHeight;
		invalidate();
	}


}
