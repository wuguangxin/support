package com.wuguangxin.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wuguangxin.R;
import com.wuguangxin.listener.SensorManagerHelper;
import com.wuguangxin.listener.SensorManagerHelper.OnShakeListener;
import com.wuguangxin.utils.NumberUtils;

//import android.graphics.Path;

/**
 * 水缸添水进度效果,可以实现圆形现状
 */
public class WaveView extends FrameLayout{
	private static final int DEFAULT_TEXTCOLOR = 0xFFff6600;
	private static final int DEFAULT_TEXTSIZE = 14; // 单位为DIP
	private static final float DEFAULT_SPEED = 10; // 波浪速度（理想值1-10）
	private int mTextColor = DEFAULT_TEXTCOLOR;
	private int mTextSize = DEFAULT_TEXTSIZE; // 单位为DIP
	private Paint mPaint = new Paint();
//	private Path path = new Path();
//	private float lineSize = 1.5f; // 圆的线条大小
	private Bitmap mScaledBitmap;
	private Bitmap mBitmap;
	private float mPercent;
	private float mLeft;
	private float mSpeed = DEFAULT_SPEED;
	private int mRepeatCount = 0; // 循环次数
	private Status mFlag = Status.NONE;
	private Context context;
	private float h = 0f;
	private boolean isShake; // 是否摇动

	public WaveView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
	}

	public void setTextColor(int color){
		mTextColor = color;
	}

	/**
	 * 文字大小（单位dip)
	 * @param size
	 */
	public void setTextSize(int size){
		mTextSize = size;
	}

	public void setPercent(float percent){
		mFlag = Status.RUNNING;
		if(mPercent != percent){
			mPercent = percent;
			if(h != 0){
				h = 0;
			}
		}
		postInvalidate();
		onShakeListener();
	}

	public void setStatus(Status status){
		mFlag = status;
	}

	public void clear(){
		mFlag = Status.NONE;
		if (mScaledBitmap != null) {
			mScaledBitmap.recycle();
			mScaledBitmap = null;
		}
		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas){
		super.dispatchDraw(canvas);
		// 绘制外圆环（画外圆有位置错乱BUG，未解决，暂时做一个层盖在上面)
//		int width = getWidth();
//		int height = getHeight();
//		canvas.save();
//		path.reset();
//		canvas.clipPath(path);
//		path.addCircle(width / 2, height / 2, width / 2, Direction.CCW); // CCW逆时针画圆，CW顺时针
//		canvas.clipPath(path, Op.REPLACE);  
		//
		if (mFlag == Status.RUNNING) {
			if (mScaledBitmap == null) {
				mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sinkingview_wave);
				mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), getHeight(), false);
				mBitmap.recycle();
				mBitmap = null;
				mRepeatCount = (int) Math.ceil(getWidth() / mScaledBitmap.getWidth() + 0.5) + 1; // 将小数部分一律向整数部分进位。 
			}
			// 绘制水波纹水平移动
			int w = mScaledBitmap.getWidth();
			for (int i = 0; i < mRepeatCount; i++) {
				canvas.drawBitmap(mScaledBitmap, mLeft + (i - 1) * w, (1 - h) * getHeight(), null);
			}
			// 控制水波纹高度
			float rate = mPercent / 100f; // 增长基本系数
			if(h < mPercent){
				int override = (h + rate) < mPercent ? 2 : 1; // 增长倍率
				h += (rate * override);
			}
			// 控制水波纹水平移动位置
			mLeft += mSpeed;
			if (mLeft >= mScaledBitmap.getWidth()) {
				mLeft = 0;
			}
			// 当摇动手机时，水波纹速度加快，这里控制速度慢慢减小，当减小到默认值时，不再减
			if(isShake && mSpeed > DEFAULT_SPEED){
				mSpeed -= 0.5;
			} else {
				isShake = false;
			}
			// 绘制文字
			String percent = NumberUtils.formatPercent(mPercent, 0);
			mPaint.setColor(mTextColor);
			mPaint.setTextSize(dip2px(mTextSize)); // Paint.setTextSize(),单位是px
			mPaint.setStyle(Style.FILL);
			mPaint.setAntiAlias(true);
			mPaint.setTextScaleX(1f);
			canvas.drawText(percent, (getWidth() - mPaint.measureText(percent)) / 2, getHeight() / 2 + mTextSize / 2, mPaint);
			//
			// 绘制外圆环（画外圆有位置错乱BUG，未解决，暂时做一个层盖在上面)
//			mPaint.setStyle(Style.STROKE);
//			mPaint.setStrokeWidth(lineSize);
//			mPaint.setColor(0xffe7e7e7);
//			mPaint.setAntiAlias(true);
//			canvas.drawCircle(width / 2, height / 2, width / 2 - lineSize, mPaint);
			
			postInvalidateDelayed(50);
		}

		canvas.save();
//		canvas.restore();
	}

	public enum Status {
		RUNNING, NONE
	}
	
	private void onShakeListener(){
	    SensorManagerHelper sensorHelper = new SensorManagerHelper(context);  
	     
        sensorHelper.setOnShakeListener(extracted());  
	}

	private OnShakeListener extracted(){
		return new OnShakeListener() {  
            @Override  
            public void onShake() {  
            	if(!isShake){
            		isShake = true;
            		mSpeed = 50;
            		invalidate();
            	}
            }

			@Override
			public void onSpeedChange(double speed){
			}  
        };
	}
	
	private int dip2px(float dipValue){
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * density + 0.5f);
	}
}
