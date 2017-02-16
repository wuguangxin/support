package com.wuguangxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.R;
import com.wuguangxin.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义锁屏View
 */
public class GestureView extends View{
	private static final int DEF_CIRCLE_NUM = 3;
	private static final float DEF_CIRCLE_RADIUS = 70f;
	private static final float DEF_CIRCLE_SPACE = 50f;

	/** 绘制手势点太短 */
	public static final int ERROR_KEY_SHORT = 1;
	/** 在设置手势时，第二次与第一次不一致 */
	public static final int ERROR_KEY_UNLIKE = 2; 
	/** 解锁密码不正确 */
	public static final int ERROR_UNLOCK_NO = 3;
	private static final int DELAY_RESET_TIME = 300;		// 延时恢复组件时间(毫秒)

	// 背景
	private int COLOR_BACKGROUND_NORMAL = 0x00000000;		// 背景颜色 正常
	private int COLOR_BACKGROUND_TOUCH = 0x00000000;		// 背景颜色 触摸
	private int COLOR_BACKGROUND_ERROR = 0x00000000;		// 背景颜色 错误
	// 外圆
	private int COLOR_OUTER_CYCLE_NORMAL = 0xFF999999;		// 外圆颜色 正常
	private int COLOR_OUTER_CYCLE_TOUCH = 0xFF999999;		// 外圆颜色 触摸
	private int COLOR_OUTER_CYCLE_ERROR = 0xFFEE2F3A;		// 外圆颜色 错误
	// 内圆
	private int COLOR_INNER_CYCLE_NORMAL = 0x00000000;		// 内圆颜色 正常
	private int COLOR_INNER_CYCLE_TOUCH = 0xFF404040;		// 内圆颜色 触摸
	private int COLOR_INNER_CYCLE_ERROR = 0xFFEE2F3A;		// 内圆颜色 错误
	// 连接线
	private int COLOR_LINK_LINE_NORMAL = 0xFF959595;		// 线条颜色 正常
	private int COLOR_LINK_LINE_ERROR = 0xFFfc727a;			// 线条颜色 错误

	private float outerCycleStrokeSizeError = 3; 			// 外圆边框大小 错误时
	private float outerCycleStrokeSize = 3; 				// 外圆边框大小
	private float innerCycleStrokeSize = 0; 				// 内圆边框大小
	private float backgroundStrokeSize = 0; 				// 背景边框大小
	private float linkLineStrokeSize = 5; 					// 连接线条大小

	private float width = 0; 							// 视图的宽度
	private float circleSpace = DEF_CIRCLE_SPACE; 		// 圆间隔
	private float circleRadius = DEF_CIRCLE_RADIUS; 	// 圆半径
	private int circleNum = DEF_CIRCLE_NUM;
	private int eventX; 				// 当前手指X位置
	private int eventY; 				// 当前手指Y位置
	private int limitNum = 4; 			// 设置最少绘制数量
	private boolean canContinue = true; // 能否操控界面绘画
	private boolean shakeable; 			// 是否使用触觉反馈
	private boolean isTouch; 			// 手指是否按下
	private String key; 				// 解锁密码
	private Timer timer;
	private List<Integer> mPasswordList = new ArrayList<>(); // 存储密码集合
	private OnGestureListener mOnGestureListener;
	private LockCircle[] cycles; // 解锁圆点数组

	private Paint paintOuterCycle;		// 外圆
	private Paint paintBackground;		// 内圆
	private Paint paintCenterCycle;		// 中心正方形
	private Paint paintLinkLines; 		// 路径画笔

	private Path linePath = new Path();
	
	public GestureView(Context context){
		this(context, null);
	}

	public GestureView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public GestureView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GestureView);
		if(a != null){
			circleNum = a.getInteger(R.styleable.GestureView_circleNum, DEF_CIRCLE_NUM);
			circleSpace = a.getDimension(R.styleable.GestureView_circleSpace, DEF_CIRCLE_SPACE);
			circleRadius = a.getDimension(R.styleable.GestureView_circleRadius, DEF_CIRCLE_RADIUS);
			a.recycle();
		}

		linkLineStrokeSize = Utils.dip2px(context, 2.5f); 					// 连接线条大小
		outerCycleStrokeSize = Utils.dip2px(context, 1.5f); 				// 外圆边框大小
		outerCycleStrokeSizeError = outerCycleStrokeSize; 					// 外圆边框大小 错误时

		innerCycleStrokeSize = Utils.dip2px(context, 0); 					// 内圆边框大小
		backgroundStrokeSize = Utils.dip2px(context, 0); 					// 背景边框大小

		init();
	}

	/** 初始化 */
	public void init(){
		// 背景
		paintBackground = new Paint();
		paintBackground.setAntiAlias(true);
		paintBackground.setStyle(Paint.Style.FILL);
		paintBackground.setStrokeWidth(backgroundStrokeSize);
		// 外圆
		paintOuterCycle = new Paint();
		paintOuterCycle.setAntiAlias(true);
		paintOuterCycle.setStyle(Paint.Style.STROKE);
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSize);
		// 内圆
		paintCenterCycle = new Paint();
		paintCenterCycle.setAntiAlias(true);
		paintCenterCycle.setStyle(Paint.Style.FILL);
		paintCenterCycle.setStrokeWidth(innerCycleStrokeSize);
		// 连接线
		paintLinkLines = new Paint();
		paintLinkLines.setAntiAlias(true);
		paintLinkLines.setStyle(Paint.Style.STROKE);
		paintLinkLines.setStrokeWidth(linkLineStrokeSize);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, widthMeasureSpec); // 让高==宽

		// 以最小的值为实际尺寸
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);

		// 1、以组件最小边为组件的宽高，比如宽3，高2，则宽为2，但这样会造成软键盘未收回的情况下就测量了UI组件，会造成缩小问题。
//		float min = Math.min(w, h) - circleSpace * 3;
		// 2、以组件宽为组件宽高
		float min = w - circleSpace * 3;

		// 间隔不能小于0, 且不能大于圆的直径
		if (circleSpace < 0) circleSpace = 0F;
		if (circleSpace > circleRadius * 2) circleSpace = circleRadius * 2;

		width = (circleRadius*2 + circleSpace*2) * circleNum; // 计算整个组件的宽度
		if(width > min){
			float scaling = min / width; // 缩放比例
			circleRadius *=  scaling;
			circleSpace *=  scaling;
			invalidate();
		}

		setMeasuredDimension((int)width, (int)width);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
		// 初始化圆的参数
		if (cycles == null && width > 0) {
			cycles = new LockCircle[circleNum * circleNum];
			for (int x = 0; x < circleNum; x++) {
				for (int y = 0; y < circleNum; y++) {
					LockCircle circle = new LockCircle();
					circle.setNum(x * circleNum + y);
					circle.setRadius(circleRadius);
					float r = width / circleNum / 2F;
					circle.setCircleX(r + r * 2 * y);
					circle.setCircleY(r + r * 2 * x);
					cycles[x * circleNum + y] = circle;
				}
			}
		}

		// 老版备份
//		if (cycles == null && perWidthSize > 0 && perHeightSize > 0) {
//			cycles = new LockCircle[9];
//			for (int i = 0; i < 3; i++) {
//				for (int j = 0; j < 3; j++) {
//					LockCircle lockCircle = new LockCircle();
//					lockCircle.setNum(i * 3 + j);
//					float circleRadius = perWidthSize * 0.6f;
//					lockCircle.setRadius(circleRadius);
//					lockCircle.setCircleX(perWidthSize * (j * 2 + 1.5f) + 0.5f);
//					lockCircle.setCircleY(perHeightSize * (i * 2 + 1.5f) + 0.5f);
//					cycles[i * 3 + j] = lockCircle;
//				}
//			}
//		}
	}

	/**
	 * 设置手势密码连接的数量值
	 * @param limitNum
	 */
	public void setLimitNum(int limitNum){
		this.limitNum = limitNum;
	}

	/**
	 * 设置key值留验证用，要是没有设置就代表是设置手势密码
	 * @param key
	 */
	public void setKey(String key){
		this.key = key;
	}

	public void setOnGestureListener(OnGestureListener onGestureListener){
		this.mOnGestureListener = onGestureListener;
	}

	/** 手势输入完成后回调接口 */
	public interface OnGestureListener{
		/**
		 * 当为解锁状态时，请返回设置的解锁密码
		 * @return
		 */
		String getOldKey();
		
		/**
		 * 在绘制中时回调
		 * @param key 当前绘制的密码
		 */
		void onDrawing(String key);
		
		/**
		 * 在绘制手势结束后回调
		 * @param key 绘制的密码
		 */
		void onSucceed(String key);
		
		/**
		 * 错误。<br>
		 * 1：绘制手势点太短。<br>
		 * 2：在设置手势时，第二次与第一次不一致。<br>
		 * 3：解锁密码不正确。<br>
		 * @param code
		 */
		void onError(int code);

		/**
		 * 重置手势状态
		 */
		void onReset();
	}

	/** 监听手势 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if (!canContinue) {
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouch = true;
			if (TextUtils.isEmpty(key)) {
				reset();
			}
			if(timer != null){
				timer.cancel();
			}
			timer = new Timer();
		case MotionEvent.ACTION_MOVE:
			eventX = (int) event.getX();
			eventY = (int) event.getY();
			for (int i = 0; i < cycles.length; i++) {
				if (cycles[i].isPointIn(eventX, eventY)) {
					cycles[i].setTouch(true);
					if (!mPasswordList.contains(cycles[i].getNum())) {
						mPasswordList.add(cycles[i].getNum());
						if (mOnGestureListener != null && mPasswordList.size() > 0) {
							mOnGestureListener.onDrawing(getKey());
						}
						// 连接后震动手机
						if (shakeable) {
							performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
									HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING |
											HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
						}
						break;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			//手指离开暂停触碰
			canContinue = false;
			int len = mPasswordList.size();
			if(len <= 1){
				reset();
			} else if (len < limitNum) {
				callError(ERROR_KEY_SHORT);
				resetDelay(DELAY_RESET_TIME);
			} else {
				String curKey = getKey(); // 当前绘制的密码
				if (!TextUtils.isEmpty(key)) {
					if (key.equals(curKey)){
						callSucceed(curKey);
						reset();
					} else {
						// 两次绘制不一致
						callError(ERROR_KEY_UNLIKE);
						resetDelay(DELAY_RESET_TIME);
					}
				} else {
					String oldKey = getOldKey();
					if(TextUtils.isEmpty(oldKey)){
						// 来设置的
						if (!TextUtils.isEmpty(curKey)) {
							callSucceed(curKey);
						}
						reset();
					} else {
						// 来解锁的
						if (!TextUtils.isEmpty(curKey) && curKey.equals(oldKey)) {
							callSucceed(curKey);
							reset();
						} else {
							callError(ERROR_UNLOCK_NO);
							resetDelay(DELAY_RESET_TIME);
						}
					}
				}
			}
			// ===================================================
			break;
		}
		invalidate();
		return true;
	}

	/**
	 * 延时垂直解锁组件
	 * @param delay 延时时间(毫秒值)
     */
	private void resetDelay(int delay) {
		if(timer != null){
			timer.schedule(new TimerTask(){
				@Override
				public void run(){
					// 1秒后如果还没有触摸手势界面，则自动恢复
					if (isTouch) {
						reset();
					}
				}
			}, delay);
		}
	}

	private String getOldKey(){
		if(mOnGestureListener != null){
			return mOnGestureListener.getOldKey();
		}
		return null;
	}

	/**
	 * 错误回调
	 * @param errorCode 错误码
     */
	private void callError(int errorCode){
		if(mOnGestureListener != null){
			mOnGestureListener.onError(errorCode);
		}
	}
	
	private void callSucceed(String string){
		if(mOnGestureListener != null){
			mOnGestureListener.onSucceed(string);
		}
	}

	private String getKey(){
		if (mPasswordList == null) {
			return null;
		}
		StringBuffer keyBuffer = new StringBuffer();
		for (int i = 0; i < mPasswordList.size(); i++) {
			keyBuffer.append(mPasswordList.get(i));
		}
		return keyBuffer.toString();
	}

	/**
	 * 恢复手势锁界面
	 */
	public void reset(){
		canContinue = true;
		eventX = eventY = 0;
		if (cycles != null) {
			int length = cycles.length;
			for (int i = 0; i < length; i++) {
				cycles[i].setTouch(false);
			}
		}
		if (linePath != null) {
			linePath.reset();
		}
		if (mPasswordList != null) {
			mPasswordList.clear();
		}

		if(mOnGestureListener != null){
			mOnGestureListener.onReset();
		}

		postInvalidate();//在非ui线程刷新界面
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.save();
		if (!canContinue) {
			drawLine(canvas, COLOR_LINK_LINE_ERROR);
		} else {
			drawLine(canvas, COLOR_LINK_LINE_NORMAL);
		}

		int cycleSize = cycles.length;
		for (int i = 0; i < cycleSize; i++) {
			if (!canContinue) {
				// 绘画完
				if (cycles[i].isTouch()) {
					drawError(canvas, cycles[i]); 	// 错误
				} else {
					drawNormal(canvas, cycles[i]);	// 正常
				}
			} else {
				//绘画中
				if (cycles[i].isTouch()) {
					drawTouch(canvas, cycles[i]);	// 触摸到的
				} else {
					drawNormal(canvas, cycles[i]);	// 触摸不到的
				}
			}
		}
		canvas.restore();
	}

	/** 画正常的 */
	private void drawNormal(Canvas canvas, LockCircle circle){
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSize);
//		drawBackground(canvas, circle, COLOR_BACKGROUND_NORMAL);	// 1 画背景
		drawOuterCycle(canvas, circle, COLOR_OUTER_CYCLE_NORMAL);	// 2 画外圆
		drawInnerCycle(canvas, circle, COLOR_INNER_CYCLE_NORMAL);	// 3 画内圆
	}

	/** 画按下的 */
	private void drawTouch(Canvas canvas, LockCircle circle){
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSize);
//		drawBackground(canvas, circle, COLOR_BACKGROUND_TOUCH);		// 1 画背景
		drawOuterCycle(canvas, circle, COLOR_OUTER_CYCLE_TOUCH);	// 2 画外圆
		drawInnerCycle(canvas, circle, COLOR_INNER_CYCLE_TOUCH);	// 3 画内圆
	}

	/** 画错误的 */
	private void drawError(Canvas canvas, LockCircle circle){
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSizeError);
//		drawBackground(canvas, circle, COLOR_BACKGROUND_ERROR);		// 1 画背景
		drawOuterCycle(canvas, circle, COLOR_OUTER_CYCLE_ERROR);	// 2 画外圆
		drawInnerCycle(canvas, circle, COLOR_INNER_CYCLE_ERROR);	// 3 画内圆
	}

	/** 画外圆 */
	private void drawOuterCycle(Canvas canvas, LockCircle circle, int color){
		paintOuterCycle.setColor(color);
		canvas.drawCircle(circle.getCircleX(), circle.getCircleY(), circle.getRadius(), paintOuterCycle);
	}

	/** 画背景 */
	private void drawBackground(Canvas canvas, LockCircle circle, int color){
		paintBackground.setColor(color);
		canvas.drawCircle(circle.getCircleX(), circle.getCircleY(), circle.getRadius(), paintBackground);
	}

	/** 画内圆 */
	private void drawInnerCycle(Canvas canvas, LockCircle circle, int color){
		paintCenterCycle.setColor(color);
		float r = circle.getRadius() / 2.3f;

		// 画圆
		canvas.drawCircle(circle.getCircleX(), circle.getCircleY(), r, paintCenterCycle);

		// 画方块
//		canvas.save();
//		canvas.rotate(45, circle.getCircleX(), circle.getCircleY());// 以中心点旋转45度
//		canvas.drawRect( //
//				circle.getCircleX() - r, //
//				circle.getCircleY() - r, //
//				circle.getCircleX() + r, //
//				circle.getCircleY() + r, //
//				paintCenterCycle);	// 正方形
//		canvas.restore();
	}

	/** 画横线 */
	private void drawLine(Canvas canvas, int color){
		//构建路径
		linePath.reset();
		if (mPasswordList.size() > 0) {
			int size = mPasswordList.size();
			for (int i = 0; i < size; i++) {
				int index = mPasswordList.get(i);
				float x = cycles[index].getCircleX();
				float y = cycles[index].getCircleY();
				if (i == 0) {
					linePath.moveTo(x, y);
				} else {
					linePath.lineTo(x, y);
				}
			}
			if (canContinue) {
				linePath.lineTo(eventX, eventY);
			} else {
				linePath.lineTo(cycles[mPasswordList.get(mPasswordList.size() - 1)].getCircleX(), cycles[mPasswordList.get(mPasswordList.size() - 1)].getCircleY());
			}
			paintLinkLines.setColor(color);
			canvas.drawPath(linePath, paintLinkLines);
		}
	}

	/**
	 * 每个圆点类
	 * 
	 * 2014年12月12日 上午10:05:48
	 */
	public static class LockCircle{
		/** 圆心横坐标 */
		private float circleX;
		/** 圆心纵坐标 */
		private float circleY;
		/** 半径长度 */
		private float radius;
		/** 代表数值 */
		private Integer num;
		/** 是否选择:false=未选中 */
		private boolean isTouch;

		/** 圆心X轴 */
		public float getCircleX(){
			return circleX;
		}

		public void setCircleX(float circleX){
			this.circleX = circleX;
		}

		/** 半圆心Y轴 */
		public float getCircleY(){
			return circleY;
		}

		public void setCircleY(float circleY){
			this.circleY = circleY;
		}

		/** 半径 */
		public float getRadius(){
			return radius;
		}

		public void setRadius(float radius){
			this.radius = radius;
		}

		public Integer getNum(){
			return num;
		}

		public void setNum(Integer num){
			this.num = num;
		}

		public boolean isTouch(){
			return isTouch;
		}

		public void setTouch(boolean touch){
			this.isTouch = touch;
		}

		/** 判读传入位置是否在圆心内部 */
		public boolean isPointIn(int x, int y){
			double distance = Math.sqrt((x - circleX) * (x - circleX) + (y - circleY) * (y - circleY));
			return distance < radius;
		}
	}
	
	/**
	 * 设置触觉反馈
	 * @param shake
	 */
	public void setShake(boolean shake){
		this.shakeable = shake;
	}
}
