package com.wuguangxin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wuguangxin.utils.Utils;

/**
 * ViewPager 指示器
 *
 * <p>Created by wuguangxin on 16/12/21 </p>
 */
public class ViewPagerIndicator extends View {
	private static final float DEF_CIRCLE_RADIUS = 5;
	private static final float DEF_CIRCLE_PADDING = 5;
	private int COLOR_NORMAL = 0XFFcccccc; // 正常颜色
	private int COLOR_SELECTED = 0XFFee2f3a; // 选中颜色
	private float DEF_LINE_SIZE = 0; // 圆圈线条的大小
	private Paint paint;
	private float lineSize;
	private float circleRadius = 5f;
	private float circlePadding = 5f;
	private float width;
	private float height;
	private float rectWidth;
	private float rectHeight;
	private int count = 0;
	private int currentItem = 0;
	private IndicatorType indicatorType = IndicatorType.Rect; // 指示器类型

	public ViewPagerIndicator(Context context) {
		super(context);
		initView(context);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		// 线条大小
		lineSize = Utils.dip2px(context, DEF_LINE_SIZE);
		circlePadding = Utils.dip2px(context, DEF_CIRCLE_PADDING); // 间隔距离

		// 圆形样式尺寸
		circleRadius = Utils.dip2px(context, DEF_CIRCLE_RADIUS);

		// 方形样式尺寸
		rectWidth = Utils.dip2px(context, 27) / 2;
		rectHeight = Utils.dip2px(context, 5) / 2;

		// 画笔设置
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(COLOR_NORMAL);//设置线条颜色
		paint.setStrokeWidth(lineSize);// 线条宽度
		paint.setAntiAlias(true);//去除锯齿
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = getPaddingLeft() + getPaddingRight();
		height = getPaddingTop() + getPaddingBottom();

		if(indicatorType == IndicatorType.Circle){
			// 圆形
			width += circleRadius*2*count + circlePadding*count; // 计算整个组件的宽度
			height += circleRadius*2;
		} else if(indicatorType == IndicatorType.Rect){
			// 方形
			width += rectWidth*count + circlePadding*count; // 计算整个组件的宽度
			height += rectHeight;
		}

		setMeasuredDimension((int)width, (int)height);
	}

	float r, cx, cy;

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		r = width / count / 2F;
		cy = height / 2f;

		for (int i = 0; i < count; i++) {
			cx = r + r * 2 * i;
			if(indicatorType == IndicatorType.Circle){
				// 圆形
				if(currentItem == i){
					paint.setColor(COLOR_SELECTED);
					canvas.drawCircle(cx, cy, circleRadius, paint); // 选中
				} else {
					paint.setColor(COLOR_NORMAL);
					canvas.drawCircle(cx, cy, circleRadius, paint); // 未选
				}
			} else if(indicatorType == IndicatorType.Rect){
				// 方形
				if(currentItem == i){
					paint.setColor(COLOR_SELECTED);
					canvas.drawRect(cx-rectWidth/2, cy-rectHeight/2, cx+rectWidth/2, cy+rectHeight/2, paint);// 选中
				} else {
					paint.setColor(COLOR_NORMAL);
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

	public void setCount(int count) {
		this.count = count;
		invalidate();
	}

	public void setIndicatorType(IndicatorType indicatorType) {
		this.indicatorType = indicatorType;
	}

	public enum IndicatorType {
		/**
		 * 方形
		 */
		Rect,

		/**
		 * 圆形
		 */
		Circle
	}
}
