package com.wuguangxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.R;

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
	private int backgroundColorNormal = 0x00000000;		// 背景颜色 正常
	private int backgroundColorTouch = 0x00000000;		// 背景颜色 触摸
	private int backgroundColorError = 0x00000000;		// 背景颜色 错误
	// 外圆
	private int outerCycleColorNormal = 0xFF999999;		// 外圆颜色 正常
	private int outerCycleColorTouch = 0xFF999999;		// 外圆颜色 触摸
	private int outerCycleColorError = 0xFFEE2F3A;		// 外圆颜色 错误
	// 内圆
	private int innerCycleColorNormal = 0x00000000;		// 内圆颜色 正常
	private int innerCycleColorTouch = 0xFF404040;		// 内圆颜色 触摸
	private int innerCycleColorError = 0xFFEE2F3A;		// 内圆颜色 错误
	// 边框
	private float outerCycleStrokeSize = 1.5F; 			// 外圆边框大小
	private float innerCycleStrokeSize = 0; 			// 内圆边框大小
	private float backgroundStrokeSize = 0; 			// 背景边框大小
	private float linkLineStrokeSize = 2.5F; 			// 连接线条大小
	// 连接线
	private int linkLineColorNormal = 0xFF959595;		// 线条颜色 正常
	private int linkLineColorError = 0xFFfc727a;		// 线条颜色 错误
	// 其他
	private int limitNum = 4; 							// 设置最少绘制数量
	private int circleNum = 3;							// 水平的圆圈个数，3X3就是3，4X4就是4
	private float width = -1; 							// 整个手势View的宽度(默认填充父View)
	private float circleSpace = 50.0F; 					// 圆间隔
	private float circleRadius = 70.0F; 				// 圆半径

	private List<Integer> mPasswordList = new ArrayList<>(); // 存储密码集合
	private OnGestureListener mOnGestureListener;
	private LockCircle[] cycleList; 		// 解锁圆点数组
	private String key; 				// 解锁密码
	private Timer timer;				// 定时器
	private Paint paintOuterCycle;		// 外圆
	private Paint paintBackground;		// 内圆
	private Paint paintCenterCycle;		// 中心正方形
	private Paint paintLinkLines; 		// 路径画笔

	private boolean touchShake; 		// 是否使用触觉反馈

	private boolean canContinue = true; // 能否操控界面绘画
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
			backgroundColorNormal = a.getColor(R.styleable.GestureView_backgroundColorNormal, backgroundColorNormal);	// 背景颜色 正常
			backgroundColorTouch = a.getColor(R.styleable.GestureView_backgroundColorTouch, backgroundColorTouch);		// 背景颜色 触摸
			backgroundColorError = a.getColor(R.styleable.GestureView_backgroundColorError, backgroundColorError);		// 背景颜色 错误
			// 外圆
			outerCycleColorNormal = a.getColor(R.styleable.GestureView_outerCycleColorNormal, outerCycleColorNormal);	// 外圆颜色 正常
			outerCycleColorTouch = a.getColor(R.styleable.GestureView_outerCycleColorTouch, outerCycleColorTouch);		// 外圆颜色 触摸
			outerCycleColorError = a.getColor(R.styleable.GestureView_outerCycleColorError, outerCycleColorError);		// 外圆颜色 错误
			// 内圆
			innerCycleColorNormal = a.getColor(R.styleable.GestureView_innerCycleColorNormal, innerCycleColorNormal);	// 内圆颜色 正常
			innerCycleColorTouch = a.getColor(R.styleable.GestureView_innerCycleColorTouch, innerCycleColorTouch);		// 内圆颜色 触摸
			innerCycleColorError = a.getColor(R.styleable.GestureView_innerCycleColorError, innerCycleColorError);		// 内圆颜色 错误
			// 边框
			outerCycleStrokeSize = a.getDimension(R.styleable.GestureView_outerCycleStrokeSize, outerCycleStrokeSize); 	// 外圆边框大小
			innerCycleStrokeSize = a.getDimension(R.styleable.GestureView_innerCycleStrokeSize, innerCycleStrokeSize); 	// 内圆边框大小
			linkLineStrokeSize = a.getDimension(R.styleable.GestureView_linkLineStrokeSize, linkLineStrokeSize); 		// 连接线条大小
			// 连接线
			linkLineColorNormal = a.getColor(R.styleable.GestureView_linkLineColorNormal, linkLineColorNormal);			// 线条颜色 正常
			linkLineColorError = a.getColor(R.styleable.GestureView_linkLineColorError, linkLineColorError);			// 线条颜色 错误
			// 其他配置
			circleNum = a.getInteger(R.styleable.GestureView_circleNum, circleNum);				// 水平的圆圈个数，3X3就是3，4X4就是4
			limitNum = a.getInteger(R.styleable.GestureView_limitNum, limitNum); 				// 设置最少绘制数量
			circleSpace = a.getDimension(R.styleable.GestureView_circleSpace, circleSpace); 	// 圆间隔
			circleRadius = a.getDimension(R.styleable.GestureView_circleRadius, circleRadius); 	// 圆半径
			touchShake = a.getBoolean(R.styleable.GestureView_touchShake, touchShake); 			// 是否在触摸时震动

			a.recycle();
		}

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

	private float paddingLeft;
	private float paddingRight;
	private float paddingTop;
	private float paddingBottom;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		Log.e("wgx", "===========GestureView==================================");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 以最小的值为实际尺寸
		float measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		float measureHeight = MeasureSpec.getSize(heightMeasureSpec);
		float widthMode = MeasureSpec.getMode(widthMeasureSpec);
		float heightMode = MeasureSpec.getMode(heightMeasureSpec);

		paddingLeft = getPaddingLeft();
		paddingRight = getPaddingRight();
		paddingTop = getPaddingTop();
		paddingBottom = getPaddingBottom();

		// 手势View的最大宽度
		float contextMaxSize = circleRadius * 2 * circleNum + circleSpace * (circleNum - 1) + paddingLeft + paddingRight;
		// 手势View的最小宽度
		float contextMinSize = circleRadius * 2 * circleNum + paddingLeft+ paddingRight;

		//Measure Width
		if (widthMode == MeasureSpec.EXACTLY) {
			//Must be this size
		} else if (widthMode == MeasureSpec.AT_MOST) {
			measureWidth = Math.min(contextMaxSize, measureWidth);
		} else {
			measureWidth = contextMaxSize;
		}

		//Measure Height
		if (heightMode == MeasureSpec.EXACTLY) {
			//Must be this size
		} else if (heightMode == MeasureSpec.AT_MOST) {
			measureHeight = Math.min(contextMaxSize, measureHeight);
		} else {
			measureHeight = contextMaxSize;
		}
		Log.e("wgx", "初始化：measureWidth="+measureWidth);
		Log.e("wgx", "初始化：measureHeight="+measureHeight);
		Log.e("wgx", "初始化：contextMaxSize="+contextMaxSize);
		Log.e("wgx", "初始化：contextMinSize="+contextMinSize);

		// 布局的可显示大小
		width = Math.min(measureWidth, measureHeight);
		printInfo("初始化");

		// 画板<最大内容，则 缩小圆间距
		if (width < contextMaxSize) {
			circleSpace = (width - paddingLeft - paddingRight - circleRadius * 2 * circleNum) / (circleNum-1);
			printInfo("画板<最大内容，则 缩小圆间距");
		}

		// 画板<最小内容，则 缩小圆半径，间距置为0
		if (width < contextMinSize) {
			circleRadius = (width-paddingLeft-paddingRight) / circleNum / 2;
			circleSpace = 0;
			printInfo("画板<最小内容，则 缩小圆半径，间距置为0");
		}

		// 画板>最大内容，则 画板=最大内容
		if (width > contextMaxSize) {
			width = contextMaxSize;
			printInfo("画板>最大内容，则 画板=最大内容");
		}


		printInfo("结果");

		setMeasuredDimension((int) width, (int) width);

	}

	private boolean DEBUG = true;
	private void printInfo(String tag) {
		if (DEBUG) {
			Log.e("wgx", tag + " width=" + width);
			Log.e("wgx", tag + " paddingLeft=" + paddingLeft);
			Log.e("wgx", tag + " circleSpace=" + circleSpace);
			Log.e("wgx", tag + " circleRadius=" + circleRadius);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);

		// 新方式20171217
		if (cycleList == null && width > 0) {
			float padX = paddingLeft + paddingRight;
			float sp = (width - padX - circleRadius * 2 * circleNum) / (circleNum -1); // 根据宽度，算出每个圆的间隔距离
			sp = Math.min(sp, circleSpace);
			float r = circleRadius;
			cycleList = new LockCircle[circleNum * circleNum];
			for (int x = 0; x < circleNum; x++) {
				for (int y = 0; y < circleNum; y++) {
					LockCircle circle = new LockCircle();	// 创建圆对象
					circle.setNum(x * circleNum + y);		// 在矩阵中的位置
					circle.setRadius(circleRadius - outerCycleStrokeSize / 2); 			// 半径
//					float r = (width - ((circleNum-1) * circleSpace)) / circleNum / 2;	// 就是算得每个格子的中间到它的边的距离
					circle.setCircleX(r + r * 2 * y + sp * y + paddingLeft);			// 圆心X轴，就是每个圆的圆心坐标
					circle.setCircleY(r + r * 2 * x + sp * x + paddingTop);				// 圆心Y轴
					cycleList[x * circleNum + y] = circle;
				}
			}
		}

		// 方式1：备份20171217之前的代码
//		if (cycleList == null && width > 0) {
//			cycleList = new LockCircle[circleNum * circleNum];
//			for (int x = 0; x < circleNum; x++) {
//				for (int y = 0; y < circleNum; y++) {
//					LockCircle circle = new LockCircle();	// 创建圆对象
//					circle.setNum(x * circleNum + y);		// 在矩阵中的位置
//					circle.setRadius(circleRadius); 		// 半径,
//					float r = width / circleNum / 2;		// 就是算得每个格子的中间到它的边的距离，并不是圆的半径
//					circle.setCircleX(r + r * 2 * y);		// 圆心X轴，就是每个圆的圆心坐标
//					circle.setCircleY(r + r * 2 * x);		// 圆心Y轴
//					cycleList[x * circleNum + y] = circle;
//				}
//			}
//		}

		// 方式2：老版备份
//		if (cycleList == null && perWidthSize > 0 && perHeightSize > 0) {
//			cycleList = new LockCircle[9];
//			for (int i = 0; i < 3; i++) {
//				for (int j = 0; j < 3; j++) {
//					LockCircle lockCircle = new LockCircle();
//					lockCircle.setNum(i * 3 + j);
//					float circleRadius = perWidthSize * 0.6f;
//					lockCircle.setRadius(circleRadius);
//					lockCircle.setCircleX(perWidthSize * (j * 2 + 1.5f) + 0.5f);
//					lockCircle.setCircleY(perHeightSize * (i * 2 + 1.5f) + 0.5f);
//					cycleList[i * 3 + j] = lockCircle;
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
			for (int i = 0; i < cycleList.length; i++) {
				if (cycleList[i].isPointIn(eventX, eventY)) {
					cycleList[i].setTouch(true);
					if (!mPasswordList.contains(cycleList[i].getNum())) {
						mPasswordList.add(cycleList[i].getNum());
						if (mOnGestureListener != null && mPasswordList.size() > 0) {
							mOnGestureListener.onDrawing(getKey());
						}
						// 连接后震动手机
						if (touchShake) {
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
		if (cycleList != null) {
			int length = cycleList.length;
			for (int i = 0; i < length; i++) {
				cycleList[i].setTouch(false);
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
			drawLine(canvas, linkLineColorError);
		} else {
			drawLine(canvas, linkLineColorNormal);
		}

		int cycleSize = cycleList.length;
		for (int i = 0; i < cycleSize; i++) {
			if (!canContinue) {
				// 绘画完
				if (cycleList[i].isTouch()) {
					drawError(canvas, cycleList[i]); 	// 错误
				} else {
					drawNormal(canvas, cycleList[i]);	// 正常
				}
			} else {
				//绘画中
				if (cycleList[i].isTouch()) {
					drawTouch(canvas, cycleList[i]);	// 触摸到的
				} else {
					drawNormal(canvas, cycleList[i]);	// 触摸不到的
				}
			}
		}
		canvas.restore();
	}

	/** 画正常的 */
	private void drawNormal(Canvas canvas, LockCircle circle){
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSize);
//		drawBackground(canvas, circle, backgroundColorNormal);	// 1 画背景
		drawOuterCycle(canvas, circle, outerCycleColorNormal);	// 2 画外圆
		drawInnerCycle(canvas, circle, innerCycleColorNormal);	// 3 画内圆
	}

	/** 画按下的 */
	private void drawTouch(Canvas canvas, LockCircle circle){
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSize);
//		drawBackground(canvas, circle, backgroundColorTouch);		// 1 画背景
		drawOuterCycle(canvas, circle, outerCycleColorTouch);	// 2 画外圆
		drawInnerCycle(canvas, circle, innerCycleColorTouch);	// 3 画内圆
	}

	/** 画错误的 */
	private void drawError(Canvas canvas, LockCircle circle){
		paintOuterCycle.setStrokeWidth(outerCycleStrokeSize);
//		drawBackground(canvas, circle, backgroundColorError);		// 1 画背景
		drawOuterCycle(canvas, circle, outerCycleColorError);	// 2 画外圆
		drawInnerCycle(canvas, circle, innerCycleColorError);	// 3 画内圆
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
				float x = cycleList[index].getCircleX();
				float y = cycleList[index].getCircleY();
				if (i == 0) {
					linePath.moveTo(x, y);
				} else {
					linePath.lineTo(x, y);
				}
			}
			if (canContinue) {
				linePath.lineTo(eventX, eventY);
			} else {
				linePath.lineTo(cycleList[mPasswordList.get(mPasswordList.size() - 1)].getCircleX(), cycleList[mPasswordList.get(mPasswordList.size() - 1)].getCircleY());
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
	 * @param touchShake 是否开启触觉反馈
	 */
	public void setTouchShake(boolean touchShake){
		this.touchShake = touchShake;
	}


	//==================== 变量 setter/getter ===============================================


	public int getBackgroundColorNormal() {
		return backgroundColorNormal;
	}

	public void setBackgroundColorNormal(int backgroundColorNormal) {
		this.backgroundColorNormal = backgroundColorNormal;
		invalidate();
	}

	public int getBackgroundColorTouch() {
		return backgroundColorTouch;
	}

	public void setBackgroundColorTouch(int backgroundColorTouch) {
		this.backgroundColorTouch = backgroundColorTouch;
		invalidate();
	}

	public int getBackgroundColorError() {
		return backgroundColorError;
	}

	public void setBackgroundColorError(int backgroundColorError) {
		this.backgroundColorError = backgroundColorError;
		invalidate();
	}

	public int getOuterCycleColorNormal() {
		return outerCycleColorNormal;
	}

	public void setOuterCycleColorNormal(int outerCycleColorNormal) {
		this.outerCycleColorNormal = outerCycleColorNormal;
		invalidate();
	}

	public int getOuterCycleColorTouch() {
		return outerCycleColorTouch;
	}

	public void setOuterCycleColorTouch(int outerCycleColorTouch) {
		this.outerCycleColorTouch = outerCycleColorTouch;
		invalidate();
	}

	public int getOuterCycleColorError() {
		return outerCycleColorError;
	}

	public void setOuterCycleColorError(int outerCycleColorError) {
		this.outerCycleColorError = outerCycleColorError;
		invalidate();
	}

	public int getInnerCycleColorNormal() {
		return innerCycleColorNormal;
	}

	public void setInnerCycleColorNormal(int innerCycleColorNormal) {
		this.innerCycleColorNormal = innerCycleColorNormal;
		invalidate();
	}

	public int getInnerCycleColorTouch() {
		return innerCycleColorTouch;
	}

	public void setInnerCycleColorTouch(int innerCycleColorTouch) {
		this.innerCycleColorTouch = innerCycleColorTouch;
		invalidate();
	}

	public int getInnerCycleColorError() {
		return innerCycleColorError;
	}

	public void setInnerCycleColorError(int innerCycleColorError) {
		this.innerCycleColorError = innerCycleColorError;
		invalidate();
	}

	public int getLinkLineColorNormal() {
		return linkLineColorNormal;
	}

	public void setLinkLineColorNormal(int linkLineColorNormal) {
		this.linkLineColorNormal = linkLineColorNormal;
		invalidate();
	}

	public int getLinkLineColorError() {
		return linkLineColorError;
	}

	public void setLinkLineColorError(int linkLineColorError) {
		this.linkLineColorError = linkLineColorError;
		invalidate();
	}


	/**
	 * 设置背景色
	 * @param normalColor 正常颜色
	 * @param touchColor 触摸颜色
	 * @param errorColor 错误颜色
	 */
	public void setColorBackground(int normalColor, int touchColor, int errorColor){
		this.backgroundColorNormal = normalColor;
		this.backgroundColorTouch = touchColor;
		this.backgroundColorError = errorColor;
		invalidate();
	}

	/**
	 * 设置外圆颜色
	 * @param normalColor 正常颜色
	 * @param touchColor 触摸颜色
	 * @param errorColor 错误颜色
	 */
	public void setColorOuterCycle(int normalColor, int touchColor, int errorColor){
		this.outerCycleColorNormal = normalColor;
		this.outerCycleColorTouch = touchColor;
		this.outerCycleColorError = errorColor;
		invalidate();
	}

	/**
	 * 设置连接线颜色
	 * @param normalColor 正常颜色
	 * @param touchColor 触摸颜色
	 * @param errorColor 错误颜色
	 */
	public void setColorInnerCycle(int normalColor, int touchColor, int errorColor){
		this.innerCycleColorNormal = normalColor;
		this.innerCycleColorTouch = touchColor;
		this.innerCycleColorError = errorColor;
		invalidate();
	}

	/**
	 * 设置连接线颜色
	 * @param normalColor 正常颜色
	 * @param errorColor 错误颜色
	 */
	public void setColorLinkLine(int normalColor, int errorColor){
		this.linkLineColorNormal = normalColor;
		this.innerCycleColorError = errorColor;
		invalidate();
	}
}
