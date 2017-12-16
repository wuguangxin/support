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
	// ================== 默认值 =========================
	/** 绘制手势点太短 */
	public static final int ERROR_KEY_SHORT = 1;
	/** 在设置手势时，第二次与第一次不一致 */
	public static final int ERROR_KEY_UNLIKE = 2; 
	/** 解锁密码不正确 */
	public static final int ERROR_UNLOCK_NO = 3;
	/** 延时恢复组件时间(毫秒) */
	private static final int DELAY_RESET_TIME = 300;
	// ================== 默认值 END =====================

	// ================== 配置变量 ==================================================================
	// 背景
	private int colorBackgroundNormal = 0x00000000;		// 背景颜色 正常
	private int colorBackgroundTouch = 0x00000000;		// 背景颜色 触摸
	private int colorBackgroundError = 0x00000000;		// 背景颜色 错误
	// 外圆
	private int colorOuterCycleNormal = 0xFF999999;		// 外圆颜色 正常
	private int colorOuterCycleTouch = 0xFF999999;		// 外圆颜色 触摸
	private int colorOuterCycleError = 0xFFEE2F3A;		// 外圆颜色 错误
	// 内圆
	private int colorInnerCycleNormal = 0x00000000;		// 内圆颜色 正常
	private int colorInnerCycleTouch = 0xFF404040;		// 内圆颜色 触摸
	private int colorInnerCycleError = 0xFFEE2F3A;		// 内圆颜色 错误
	// 连接线
	private int colorLinkLineNormal = 0xFF959595;		// 线条颜色 正常
	private int colorLinkLineError = 0xFFfc727a;		// 线条颜色 错误
	// 边框大小
	private float outerCycleStrokeSizeError = 3; 		// 外圆边框大小 错误时
	private float outerCycleStrokeSize = 3; 			// 外圆边框大小
	private float innerCycleStrokeSize = 0; 			// 内圆边框大小
	private float backgroundStrokeSize = 0; 			// 背景边框大小
	private float linkLineStrokeSize = 5; 				// 连接线条大小
	// 其他配置
	private int limitNum = 4; 							// 设置最少绘制数量
	private int circleNum = 3;							// 水平的圆圈个数，3X3就是3，4X4就是4
	private float width = -1; 							// 整个手势View的宽度(默认填充父View)
	private float circleSpace = 50.0F; 					// 圆间隔
	private float circleRadius = 70.0F; 				// 圆半径

	private List<Integer> mPasswordList = new ArrayList<>(); // 存储密码集合
	private OnGestureListener mOnGestureListener;
	private LockCircle[] cycles; 		// 解锁圆点数组
	private String key; 				// 解锁密码
	private Timer timer;				// 定时器
	private Paint paintOuterCycle;		// 外圆
	private Paint paintBackground;		// 内圆
	private Paint paintCenterCycle;		// 中心正方形
	private Paint paintLinkLines; 		// 路径画笔

	private boolean canContinue = true; // 能否操控界面绘画
	private boolean shakeable; 			// 是否使用触觉反馈
	private boolean isTouch; 			// 手指是否按下
	private int eventX; 				// 当前手指X位置
	private int eventY; 				// 当前手指Y位置

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
			// 背景
			colorBackgroundNormal = a.getColor(R.styleable.GestureView_colorBackgroundNormal, colorBackgroundNormal);	// 背景颜色 正常
			colorBackgroundTouch = a.getColor(R.styleable.GestureView_colorBackgroundTouch, colorBackgroundTouch);		// 背景颜色 触摸
			colorBackgroundError = a.getColor(R.styleable.GestureView_colorBackgroundError, colorBackgroundError);		// 背景颜色 错误
			// 外圆
			colorOuterCycleNormal = a.getColor(R.styleable.GestureView_colorOuterCycleNormal, colorOuterCycleNormal);	// 外圆颜色 正常
			colorOuterCycleTouch = a.getColor(R.styleable.GestureView_colorOuterCycleTouch, colorOuterCycleTouch);		// 外圆颜色 触摸
			colorOuterCycleError = a.getColor(R.styleable.GestureView_colorOuterCycleError, colorOuterCycleError);		// 外圆颜色 错误
			// 内圆
			colorInnerCycleNormal = a.getColor(R.styleable.GestureView_colorInnerCycleNormal, colorInnerCycleNormal);	// 内圆颜色 正常
			colorInnerCycleTouch = a.getColor(R.styleable.GestureView_colorInnerCycleTouch, colorInnerCycleTouch);		// 内圆颜色 触摸
			colorInnerCycleError = a.getColor(R.styleable.GestureView_colorInnerCycleError, colorInnerCycleError);		// 内圆颜色 错误
			// 连接线
			colorLinkLineNormal = a.getColor(R.styleable.GestureView_colorLinkLineNormal, colorLinkLineNormal);			// 线条颜色 正常
			colorLinkLineError = a.getColor(R.styleable.GestureView_colorLinkLineError, colorLinkLineError);			// 线条颜色 错误
			// 边框
			outerCycleStrokeSizeError = a.getDimension(R.styleable.GestureView_outerCycleStrokeSizeError, outerCycleStrokeSizeError); 	// 外圆边框大小 错误时
			outerCycleStrokeSize = a.getDimension(R.styleable.GestureView_outerCycleStrokeSize, outerCycleStrokeSize); 				// 外圆边框大小
			innerCycleStrokeSize = a.getDimension(R.styleable.GestureView_innerCycleStrokeSize, innerCycleStrokeSize); 				// 内圆边框大小
			backgroundStrokeSize = a.getDimension(R.styleable.GestureView_backgroundStrokeSize, backgroundStrokeSize); 				// 背景边框大小
			linkLineStrokeSize = a.getDimension(R.styleable.GestureView_linkLineStrokeSize, linkLineStrokeSize); 					// 连接线条大小
			// 其他配置
			circleNum = a.getInteger(R.styleable.GestureView_circleNum, circleNum);				// 水平的圆圈个数，3X3就是3，4X4就是4
			limitNum = a.getInteger(R.styleable.GestureView_limitNum, limitNum); 				// 设置最少绘制数量
			width = a.getDimension(R.styleable.GestureView_width, width); 						// 整个手势View的宽度
			circleSpace = a.getDimension(R.styleable.GestureView_circleSpace, circleSpace); 	// 圆间隔
			circleRadius = a.getDimension(R.styleable.GestureView_circleRadius, circleRadius); 	// 圆半径

			a.recycle();
		}

		linkLineStrokeSize = Utils.dip2px(context, 2.5f); 					// 连接线条大小
		outerCycleStrokeSize = Utils.dip2px(context, 1.5f); 				// 外圆边框大小
		outerCycleStrokeSizeError = outerCycleStrokeSize; 							// 外圆边框大小 错误时

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
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 以最小的值为实际尺寸
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

		// 手势View的最小宽度
		float minWidth = circleRadius * 2 * circleNum;
		// 最大宽度
		float maxWidth = (circleRadius * 2 + circleSpace) * circleNum;

		// 以矩形最小边为View的宽高，但这样会造成软键盘未收回的情况下就测量了UI组件，会造成手势密码布局缩小问题。
		width = Math.min(measureWidth, measureHeight);
		if (width < minWidth) width = minWidth;
		if (width > maxWidth) width = maxWidth;

		setMeasuredDimension((int) width, (int) width);
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
	 * @param limitNum 手势密码连接的数量值
	 */
	public void setLimitNum(int limitNum){
		this.limitNum = limitNum;
	}

	/**
	 * 设置key值留验证用，要是没有设置就代表是设置手势密码
	 * @param key  key
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
		 * @return 设置的解锁密码
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
		 * @param code 错误码
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
			drawLine(canvas, colorLinkLineError);
		} else {
			drawLine(canvas, colorLinkLineNormal);
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
//		drawBackground(canvas, circle, colorBackgroundNormal);	// 1 画背景
		drawOuterCycle(canvas, circle, colorOuterCycleNormal);	// 2 画外圆
		drawInnerCycle(canvas, circle, colorInnerCycleNormal);	// 3 画内圆
	}

	/** 画按下的 */
	private void drawTouch(Canvas canvas, LockCircle circle){
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSize);
//		drawBackground(canvas, circle, colorBackgroundTouch);		// 1 画背景
		drawOuterCycle(canvas, circle, colorOuterCycleTouch);	// 2 画外圆
		drawInnerCycle(canvas, circle, colorInnerCycleTouch);	// 3 画内圆
	}

	/** 画错误的 */
	private void drawError(Canvas canvas, LockCircle circle){
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSizeError);
//		drawBackground(canvas, circle, colorBackgroundError);		// 1 画背景
		drawOuterCycle(canvas, circle, colorOuterCycleError);	// 2 画外圆
		drawInnerCycle(canvas, circle, colorInnerCycleError);	// 3 画内圆
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

		/**
		 * 圆心X轴
		 * @return 返回圆的X轴
		 */
		public float getCircleX(){
			return circleX;
		}

		/**
		 * 设置圆的X轴
		 * @param circleX 圆的X轴
		 */
		public void setCircleX(float circleX){
			this.circleX = circleX;
		}

		/**
		 * 半圆心Y轴
		 * @return 返回圆的Y轴
		 */
		public float getCircleY(){
			return circleY;
		}

		/**
		 * 设置圆的Y轴
		 * @param circleY 圆的Y轴
		 */
		public void setCircleY(float circleY){
			this.circleY = circleY;
		}

		/**
		 * 半径
		 * @return 返回半径
		 */
		public float getRadius(){
			return radius;
		}

		/**
		 * 设置圆的半径
		 * @param radius 圆的半径
		 */
		public void setRadius(float radius){
			this.radius = radius;
		}

		/**
		 * 获取圆的数量
		 * @return 圆的数量
		 */
		public Integer getNum(){
			return num;
		}

		/**
		 * 设置圆的数量
		 * @param num 圆的数量
		 */
		public void setNum(Integer num){
			this.num = num;
		}

		/**
		 * 判断是否触摸
		 * @return 是否触摸
		 */
		public boolean isTouch(){
			return isTouch;
		}

		/**
		 * 设置触摸状态
		 * @param touch 触摸状态
		 */
		public void setTouch(boolean touch){
			this.isTouch = touch;
		}

		/**
		 * 判读传入位置是否在圆心内部
		 * @param x 当前触摸的X轴
		 * @param y 当前触摸的Y轴
		 * @return 是否在圆心内部
		 */
		public boolean isPointIn(int x, int y){
			double distance = Math.sqrt((x - circleX) * (x - circleX) + (y - circleY) * (y - circleY));
			return distance < radius;
		}
	}
	
	/**
	 * 设置是否开启触觉反馈
	 * @param shake 是否开启触觉反馈
	 */
	public void setShake(boolean shake){
		this.shakeable = shake;
	}


	//==================== 变量 setter/getter ===============================================


	public int getColorBackgroundNormal() {
		return colorBackgroundNormal;
	}

	public void setColorBackgroundNormal(int colorBackgroundNormal) {
		this.colorBackgroundNormal = colorBackgroundNormal;
		invalidate();
	}

	public int getColorBackgroundTouch() {
		return colorBackgroundTouch;
	}

	public void setColorBackgroundTouch(int colorBackgroundTouch) {
		this.colorBackgroundTouch = colorBackgroundTouch;
		invalidate();
	}

	public int getColorBackgroundError() {
		return colorBackgroundError;
	}

	public void setColorBackgroundError(int colorBackgroundError) {
		this.colorBackgroundError = colorBackgroundError;
		invalidate();
	}

	public int getColorOuterCycleNormal() {
		return colorOuterCycleNormal;
	}

	public void setColorOuterCycleNormal(int colorOuterCycleNormal) {
		this.colorOuterCycleNormal = colorOuterCycleNormal;
		invalidate();
	}

	public int getColorOuterCycleTouch() {
		return colorOuterCycleTouch;
	}

	public void setColorOuterCycleTouch(int colorOuterCycleTouch) {
		this.colorOuterCycleTouch = colorOuterCycleTouch;
		invalidate();
	}

	public int getColorOuterCycleError() {
		return colorOuterCycleError;
	}

	public void setColorOuterCycleError(int colorOuterCycleError) {
		this.colorOuterCycleError = colorOuterCycleError;
		invalidate();
	}

	public int getColorInnerCycleNormal() {
		return colorInnerCycleNormal;
	}

	public void setColorInnerCycleNormal(int colorInnerCycleNormal) {
		this.colorInnerCycleNormal = colorInnerCycleNormal;
		invalidate();
	}

	public int getColorInnerCycleTouch() {
		return colorInnerCycleTouch;
	}

	public void setColorInnerCycleTouch(int colorInnerCycleTouch) {
		this.colorInnerCycleTouch = colorInnerCycleTouch;
		invalidate();
	}

	public int getColorInnerCycleError() {
		return colorInnerCycleError;
	}

	public void setColorInnerCycleError(int colorInnerCycleError) {
		this.colorInnerCycleError = colorInnerCycleError;
		invalidate();
	}

	public int getColorLinkLineNormal() {
		return colorLinkLineNormal;
	}

	public void setColorLinkLineNormal(int colorLinkLineNormal) {
		this.colorLinkLineNormal = colorLinkLineNormal;
		invalidate();
	}

	public int getColorLinkLineError() {
		return colorLinkLineError;
	}

	public void setColorLinkLineError(int colorLinkLineError) {
		this.colorLinkLineError = colorLinkLineError;
		invalidate();
	}


	/**
	 * 设置背景色
	 * @param normalColor 正常颜色
	 * @param touchColor 触摸颜色
	 * @param errorColor 错误颜色
	 */
	public void setColorBackground(int normalColor, int touchColor, int errorColor){
		this.colorBackgroundNormal = normalColor;
		this.colorBackgroundTouch = touchColor;
		this.colorBackgroundError = errorColor;
		invalidate();
	}

	/**
	 * 设置外圆颜色
	 * @param normalColor 正常颜色
	 * @param touchColor 触摸颜色
	 * @param errorColor 错误颜色
	 */
	public void setColorOuterCycle(int normalColor, int touchColor, int errorColor){
		this.colorOuterCycleNormal = normalColor;
		this.colorOuterCycleTouch = touchColor;
		this.colorOuterCycleError = errorColor;
		invalidate();
	}

	/**
	 * 设置连接线颜色
	 * @param normalColor 正常颜色
	 * @param touchColor 触摸颜色
	 * @param errorColor 错误颜色
	 */
	public void setColorInnerCycle(int normalColor, int touchColor, int errorColor){
		this.colorInnerCycleNormal = normalColor;
		this.colorInnerCycleTouch = touchColor;
		this.colorInnerCycleError = errorColor;
		invalidate();
	}

	/**
	 * 设置连接线颜色
	 * @param normalColor 正常颜色
	 * @param errorColor 错误颜色
	 */
	public void setColorLinkLine(int normalColor, int errorColor){
		this.colorLinkLineNormal = normalColor;
		this.colorInnerCycleError = errorColor;
		invalidate();
	}
}
