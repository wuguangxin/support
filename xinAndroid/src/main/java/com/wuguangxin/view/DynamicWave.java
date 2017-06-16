package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wuguangxin.R;

/**
 * 动态水波纹效果View
 */
public class DynamicWave extends View {
	// 波纹颜色
	private static final int WAVE_PAINT_COLOR = 0x15ffffff;
	// y = Asin(wx+b)+h
	private static final float STRETCH_FACTOR_A = 20;
	private static final int OFFSET_Y = 0;
	// 第一条水波移动速度
	private static final int TRANSLATE_X_SPEED_ONE = 6;
	// 第二条水波移动速度
	private static final int TRANSLATE_X_SPEED_TWO = 3;
	private float mCycleFactorW;

	private int mTotalWidth, mTotalHeight;
	private float[] mYPositions;
	private float[] mResetOneYPositions;
	private float[] mResetTwoYPositions;
	private int mXOffsetSpeedOne;
	private int mXOffsetSpeedTwo;
	private int mXOneOffset;
	private int mXTwoOffset;

	private Paint mWavePaint;
	private DrawFilter mDrawFilter;

	private float progress = 0;

	/**
	 * 进度百分比,也就是水波纹高度
	 *
	 * @param progress (取值 1~100)
	 */
	public void setProgress(float progress) {
		if (this.progress != progress) {
			this.progress = progress;
			invalidate();
		}
	}

	public DynamicWave(Context context) {
		this(context, null);
	}

	public DynamicWave(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DynamicWave(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 将dp转化为px，用于控制不同分辨率上移动速度基本一致
		mXOffsetSpeedOne = dip2px(context, TRANSLATE_X_SPEED_ONE);
		mXOffsetSpeedTwo = dip2px(context, TRANSLATE_X_SPEED_TWO);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DynamicWave, defStyleAttr, 0);
		progress = a.getInteger(R.styleable.DynamicWave_dw_progress, 0);
		a.recycle();

		// 初始绘制波纹的画笔
		mWavePaint = new Paint();
		// 去除画笔锯齿
		mWavePaint.setAntiAlias(true);
		// 设置风格为实线
		mWavePaint.setStyle(Paint.Style.FILL);
		// 设置画笔颜色
		mWavePaint.setColor(WAVE_PAINT_COLOR);
		mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.setDrawFilter(mDrawFilter); // 从canvas层面去除绘制时锯齿
        resetPositionY();
		float len = progress / 100 * mTotalHeight;
		for (int i = 0; i < mTotalWidth; i++) {
			// 减 len 只是为了控制波纹绘制的y的在屏幕的位置，可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
			// 绘制第一条水波纹
			canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] - len, i, mTotalHeight, mWavePaint);
			// 绘制第二条水波纹
			canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] - len, i, mTotalHeight, mWavePaint);
		}
		// 改变两条波纹的移动点
		mXOneOffset += mXOffsetSpeedOne;
		mXTwoOffset += mXOffsetSpeedTwo;

		// 如果已经移动到结尾处，则重头记录
		if (mXOneOffset >= mTotalWidth) {
			mXOneOffset = 0;
		}
		if (mXTwoOffset > mTotalWidth) {
			mXTwoOffset = 0;
		}
		// 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
//		postInvalidate();
		postInvalidateDelayed(100);
	}

	private void resetPositionY() {
		// mXOneOffset代表当前第一条水波纹要移动的距离
		int yOneInterval = mYPositions.length - mXOneOffset;
		// 使用System.arraycopy方式重新填充第一条波纹的数据
		System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
		System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

		int yTwoInterval = mYPositions.length - mXTwoOffset;
		System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0, yTwoInterval);
		System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 记录下view的宽高
		mTotalWidth = w;
		mTotalHeight = h;
		mYPositions = new float[mTotalWidth];	// 用于保存原始波纹的y值
		mResetOneYPositions = new float[mTotalWidth];	// 用于保存波纹一的y值
		mResetTwoYPositions = new float[mTotalWidth];	// 用于保存波纹二的y值
		mCycleFactorW = (float) (2 * Math.PI / mTotalWidth); 	// 将周期定为view总宽度
		// 根据view总宽度得出所有对应的y值
		for (int i = 0; i < mTotalWidth; i++) {
			mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
		}
	}

	/**
	 * dip转换为px
	 * @param context 上下文
	 * @param dipValue dip值
	 * @return 返回px值
	 */
	public static int dip2px(Context context, float dipValue) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * density + 0.5f);
	}

	private void printLogE(String msg){
		Log.e("LOG_WGX", msg);
	}

}
