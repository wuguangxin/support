package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wuguangxin.R;
import com.wuguangxin.utils.Utils;

/**
 * ViewPager 指示器
 *
 * <p>Created by wuguangxin on 16/12/21 </p>
 */
public class ViewPagerIndicator extends View {
	private static final int DEF_COLOR_NORMAL = 0XFFcccccc; // 默认正常颜色
	private static final int DEF_COLOR_SELECTED = 0XFFee2f3a; // 默认选中颜色
	private static final float DEF_LINE_SIZE = 0F; // 圆圈线条的大小
	private static final float DEF_CIRCLE_RADIUS = 5; // 默认圆的变径
	private static final float DEF_CIRCLE_PADDING = 5; // 默认圆的间距
	private static final float DEF_WIDTH = 0; // 默认圆的宽度
	private static final float DEF_HEIGHT = 0; // 默认圆的高度

	private static final float DEF_RECT_WIDTH = 0; // 默认矩形的宽度
	private static final float DEF_RECT_HEIGHT = 0; // 默认矩形的高度
	private static final int DEF_COUNT = 0; // 默认个数
	private static final int DEF_CURRENT_ITEM = 0; // 默认当前显示的位置

	private int colorNormal = DEF_COLOR_NORMAL; // 正常颜色
	private int colorSelected = DEF_COLOR_SELECTED; // 选中颜色
	private float lineSize = DEF_LINE_SIZE;
	private float circleRadius = DEF_CIRCLE_RADIUS;
	private float circlePadding = DEF_CIRCLE_PADDING;
	private float width = DEF_WIDTH;
	private float height = DEF_HEIGHT;
	private float rectWidth = DEF_RECT_WIDTH;
	private float rectHeight = DEF_RECT_HEIGHT;
	private int count = DEF_COUNT;
	private int currentItem = DEF_CURRENT_ITEM;

	private IndicatorType indicatorType = IndicatorType.Rect; // 指示器类型
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
			colorNormal = a.getColor(R.styleable.ViewPagerIndicator_colorNormal, DEF_COLOR_NORMAL); // 正常颜色
			colorSelected = a.getColor(R.styleable.ViewPagerIndicator_colorSelected, DEF_COLOR_SELECTED); // 选中颜色
			lineSize = a.getDimension(R.styleable.ViewPagerIndicator_lineSize, DEF_LINE_SIZE);
			circleRadius = a.getDimension(R.styleable.ViewPagerIndicator_circleRadius, DEF_CIRCLE_RADIUS);
			circlePadding = a.getDimension(R.styleable.ViewPagerIndicator_circlePadding, DEF_CIRCLE_PADDING);
			width = a.getDimension(R.styleable.ViewPagerIndicator_width, DEF_WIDTH);
			height = a.getDimension(R.styleable.ViewPagerIndicator_height, DEF_HEIGHT);
			rectWidth = a.getDimension(R.styleable.ViewPagerIndicator_rectWidth, DEF_RECT_WIDTH);
			rectHeight = a.getDimension(R.styleable.ViewPagerIndicator_rectHeight, DEF_RECT_HEIGHT);
			count = a.getInteger(R.styleable.ViewPagerIndicator_count, DEF_COUNT);
			currentItem = a.getInteger(R.styleable.ViewPagerIndicator_currentItem, DEF_CURRENT_ITEM);
			a.recycle();
		}
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
		paint.setColor(colorNormal);//设置线条颜色
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

	public void setColorNormal(int colorNormal) {
		this.colorNormal = colorNormal;
		invalidate();
	}

	public void setColorSelected(int colorSelected) {
		this.colorSelected = colorSelected;
		invalidate();
	}

	public void setLineSize(float lineSize) {
		this.lineSize = lineSize;
		invalidate();
	}

	public void setCircleRadius(float circleRadius) {
		this.circleRadius = circleRadius;
		invalidate();
	}

	public void setCirclePadding(float circlePadding) {
		this.circlePadding = circlePadding;
		invalidate();
	}

	public void setWidth(float width) {
		this.width = width;
		invalidate();
	}

	public void setHeight(float height) {
		this.height = height;
		invalidate();
	}

	public void setRectWidth(float rectWidth) {
		this.rectWidth = rectWidth;
		invalidate();
	}

	public void setRectHeight(float rectHeight) {
		this.rectHeight = rectHeight;
		invalidate();
	}
}
