package com.wuguangxin.view;

import java.util.ArrayList;
import java.util.List;

import com.wuguangxin.utils.Logger;
import com.wuguangxin.utils.ToastUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义锁屏九宫格组件View
 * 
 * @author wgx
 */
public class GestureViewDemo extends View {
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private Paint linePaint;
	private Paint circlePaint;
	/** 控手势组件的宽 */
	private int viewWidth;
	/** 控手势组件的高 */
	private int viewHeight;
	/** 圆的半径 */
	private int radius;
	private Context context;
	/** 圆心矩阵 */
	private PointF[][] centerCxCy;
	/** 密码数组 */
	private int[][] data;
	/** 已选中状态集合（选中之后不能重选） */
	private boolean[][] selected;
	/** 选中的圆中点集合 */
	private List<PointF> selPointList;
	/** 手指是否已按下(必须在滑动时再标记为按下状态) */
	private boolean isPressedDown = false;

	/* **************************颜色配置******************************** */
	/** 外空心圆正常时颜色 */
	private int outerHollowCircleNormal = Color.parseColor("#272A4D");
	/** 外空心圆选中时颜色（半透明） */
	private int outerHollowCircleSelected = Color.parseColor("#00B55A");
	/** 外实心圆选中时填充（半透明） */
	private int outerSolidCircleSelected = Color.parseColor("#66005E2F");
	/** 内实心圆选中时颜色 */
	private int innerSolidCircleSelected = Color.parseColor("#FFFFFF");
	/** 线条颜色（半透明） */
	private int lineColor = Color.parseColor("#99FFFFFF");
	/** 线条宽度（px） */
	private int lineBroad = 8;
	/** 最终密码字符串 */
	private String lockPassword = "";
	private Canvas canvas;
	public Handler handler = new Handler();

	public GestureViewDemo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public GestureViewDemo(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public GestureViewDemo(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * 初始化画笔状态
	 */
	private void init() {
		linePaint = new Paint();
		linePaint.setStyle(Style.FILL); // 画笔样式
		linePaint.setAntiAlias(true);

		circlePaint = new Paint();
		circlePaint.setStrokeWidth(4);
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.STROKE); // 画笔样式空心

		centerCxCy = new PointF[3][3];
		data = new int[3][3];
		selected = new boolean[3][3];
		selPointList = new ArrayList<PointF>();
		initData();
	}

	/**
	 * 初始化正确时的颜色，在开始前调用
	 */
	private void initRightColor() {
		outerHollowCircleNormal = Color.parseColor("#272A4D");
		outerHollowCircleSelected = Color.parseColor("#00B55A");// 外空心圆选中时颜色（半透明）
		outerSolidCircleSelected = Color.parseColor("#66005E2F");// 外实心圆选中时填充（半透明）
		innerSolidCircleSelected = Color.parseColor("#FFFFFF");// 内实心圆选中时颜色
		lineColor = Color.parseColor("#99FFFFFF");// 线条颜色（半透明）
	}

	/**
	 * 初始化错误时的颜色，在验证失败后调用
	 */
	private void initErrorColor() {
		outerHollowCircleNormal = Color.parseColor("#272A4D");
		outerHollowCircleSelected = Color.parseColor("#FF0000");// 外空心圆选中时颜色（半透明）
		outerSolidCircleSelected = Color.parseColor("#66FF0000");// 外实心圆选中时填充（半透明）
		innerSolidCircleSelected = Color.parseColor("#FF0000");// 内实心圆选中时颜色
		lineColor = Color.parseColor("#99FF0000");// 线条颜色（半透明）
	}

	/**
	 * 清除选中状态
	 */
	private void clearSelected() {
		for (int i = 0; i < selected.length; i++) {
			for (int j = 0; j < selected.length; j++) {
				selected[i][j] = false;
			}
		}
	}

	/**
	 * 初始化9宫格数字密码矩阵
	 */
	private void initData() {
		int num = 1;
		for (int i = 0; i < data[0].length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				data[j][i] = num;
				num++;
			}
		}
	}

	/**
	 * 判断是否在某个圆内
	 * 
	 * @param p
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isInCircle(PointF p, int x, int y) {
		return (int) Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y)) <= radius;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// if (System.currentTimeMillis() - upTime < 2000) {
		// return;
		// }else{
		// upTime = 0;
		// }
		this.canvas = canvas;
		initRightColor(); // 初始化颜色
		drawCircle(); // 画圆
		drawLine(); // 画线条
		super.onDraw(canvas);
	}

	/**
	 * 画圆
	 * 
	 * @param canvas
	 *            画布
	 */
	private void drawCircle() {
		linePaint.setStrokeWidth(lineBroad); // 设置画笔宽度
		for (int i = 0; i < selected[0].length; i++) {
			for (int j = 0; j < selected[0].length; j++) {
				PointF center = centerCxCy[i][j];
				if (selected[i][j]) { // 选中时
					circlePaint.setColor(outerHollowCircleSelected); // 设置选中时外圆颜色
					linePaint.setColor(innerSolidCircleSelected); // 设置选中时内圆颜色(实心)
					canvas.drawCircle(center.x, center.y, radius / 3, linePaint); // 画选中时的内圆
					linePaint.setColor(outerSolidCircleSelected); // 设置被选中时外圆半透明填充
					canvas.drawCircle(center.x, center.y, radius, linePaint);
				} else { // 正常时
					circlePaint.setColor(outerHollowCircleNormal); // 设置正常时外圆颜色
				}
				canvas.drawCircle(center.x, center.y, radius, circlePaint); // 画正常时的外圆
			}
		}
	}

	/**
	 * 画线
	 * 
	 * @param canvas
	 *            画布
	 */
	private void drawLine() {
		linePaint.setColor(lineColor); // 设置画笔颜色
		if (isPressedDown) {
			for (int i = 0; i < selPointList.size() - 1; i++) { // 画已选中圆之间的路径
				PointF preCenter = selPointList.get(i); // 前一个圆中点
				PointF curCenter = selPointList.get(i + 1); // 现在圆中点
				canvas.drawLine(preCenter.x, preCenter.y, curCenter.x, curCenter.y, linePaint);
			}

			if (selPointList.size() > 0) {
				PointF center = selPointList.get(selPointList.size() - 1); // 最后一个选中圆中点
				canvas.drawLine(center.x, center.y, endX, endY, linePaint);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (changed) {
			viewWidth = getWidth(); // 获取九宫格宽
			viewHeight = getHeight();// 获取九宫格高
			setRadius();
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	/**
	 * 设置圆的半径和圆心
	 */
	private void setRadius() {
		int w = viewWidth / 3;
		int h = viewHeight / 3;
		radius = w / 4;
		for (int i = 0; i < centerCxCy[0].length; i++) {
			for (int j = 0; j < centerCxCy[0].length; j++) {
				PointF p = new PointF();
				p.x = (i * w) + w / 2;
				p.y = (j * h) + h / 2;
				centerCxCy[i][j] = p;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 滑动手指事件监听
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 得到触摸点的坐标
		int pin = 0;
		switch (event.getAction()) {
		// 按下时
		case MotionEvent.ACTION_DOWN:
			clearSelected(); // 恢复为原来的颜色状态
			lockPassword = ""; // 初始密码
			selPointList.clear(); // 清除选中状态
			startX = (int) event.getX(); // 获取按下时的偏移量
			startY = (int) event.getY();
			pin = getLockPinData(startX, startY);
			if (pin > 0) {
				lockPassword += pin;
				invalidate();
			}
			break;

		// 移动时
		case MotionEvent.ACTION_MOVE:
			isPressedDown = true;
			endX = (int) event.getX(); // 移动中的偏移量
			endY = (int) event.getY();
			pin = getLockPinData(endX, endY);
			if (pin > 0) {
				lockPassword += pin;
			}
			invalidate();
			break;

		// 抬起时
		case MotionEvent.ACTION_UP:
			isPressedDown = false;
			endX = (int) event.getX(); // 抬起手指时的偏移量
			endY = (int) event.getY();
			onCheck(lockPassword);
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return true;
	}

	/**
	 * 当验证密码失败，改变圆和线的颜色为红色
	 * 
	 * @param lockPassword
	 *            手势密码
	 */
	private void onCheck(String lockPassword) {
		Logger.i(context, "密码=" + lockPassword);
		if (checkLockScreenPassword(lockPassword)) {
			// 改为正确状态颜色
			clearSelected(); // 恢复为原来的颜色状态
			invalidate();
		} else {
			setVisibility(View.GONE);
			ToastUtils.errorToast(context, "密码不正确");
			// 改为错误状态颜色
			initErrorColor();
			// 重绘
			linePaint.setStrokeWidth(lineBroad); // 设置画笔宽度
			drawCircle();
			drawLine(); // 画线条
			// 2秒后恢复
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					initRightColor();
					clearSelected(); // 恢复为原来的颜色状态
					invalidate();
					setVisibility(View.VISIBLE);
				}
			}, 2000);
		}
	}

	/**
	 * 获取锁屏密码
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int getLockPinData(int x, int y) {
		for (int i = 0; i < data[0].length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				PointF center = centerCxCy[i][j];
				// 判断是否在圆内
				if (isInCircle(center, x, y)) {
					if (!selected[i][j]) {
						selected[i][j] = true;
						selPointList.add(center);
						return data[i][j];
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 当用户按下移动并抬起时，回调的方法，用于校验密码是否正确
	 * 
	 * @param password
	 *            手势动作结束后产生的数据
	 * @return true验证成功，false验证失败
	 */
	private boolean checkLockScreenPassword(String password) {
		return password.equals("1235789");
	}
}
