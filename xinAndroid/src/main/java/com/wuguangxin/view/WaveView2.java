package com.wuguangxin.view;

import com.wuguangxin.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import android.graphics.Path;

/**
 * 水波纹圆形进度，形状为正方形的两层水波纹，无法实现圆形形状
 * Created by John on 2014/10/15.
 */
public class WaveView2 extends LinearLayout{
	protected static final int LARGE = 1;
	protected static final int MIDDLE = 2;
	protected static final int LITTLE = 3;
	private int aboveWaveColor;
	private int blowWaveColor;
	private int progress;
	private int waveHeight;
	private int waveMultiple;
	private int waveHz;
	private int mWaveToTop;
	private Wave mWave;
	private Solid mSolid;
	private final int DEFAULT_ABOVE_WAVE_COLOR = 0xff0000ff;
	private final int DEFAULT_BLOW_WAVE_COLOR = 0xff0000ff;
	private final int DEFAULT_PROGRESS = 0;

	public WaveView2(Context context){
		this(context, null);
	}

	public WaveView2(Context context, AttributeSet attrs){
		super(context, attrs);
		setOrientation(VERTICAL);
		//load styled attributes.
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveView2, R.attr.waveViewStyle, 0);
		aboveWaveColor = a.getColor(R.styleable.WaveView2_above_wave_color, DEFAULT_ABOVE_WAVE_COLOR);
		blowWaveColor = a.getColor(R.styleable.WaveView2_blow_wave_color, DEFAULT_BLOW_WAVE_COLOR);
		progress = a.getInt(R.styleable.WaveView2_progress, DEFAULT_PROGRESS);
		waveHeight = a.getInt(R.styleable.WaveView2_wave_height, MIDDLE);
		waveMultiple = a.getInt(R.styleable.WaveView2_wave_length, LARGE);
		waveHz = a.getInt(R.styleable.WaveView2_wave_hz, MIDDLE); 
		a.recycle();
		
		mWave = new Wave(context, null);
		mWave.initializeWaveSize(waveMultiple, waveHeight, waveHz);
		mWave.setAboveWaveColor(aboveWaveColor);
		mWave.setBlowWaveColor(blowWaveColor);
		mWave.initializePainters();
//		mWave.startWave();
		
		mSolid = new Solid(context, null);
		mSolid.setAboveWavePaint(mWave.getAboveWavePaint());
		mSolid.setBlowWavePaint(mWave.getBlowWavePaint());
		
		addView(mWave);
		addView(mSolid);
		setProgress(progress);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		System.out.println("w=" + w + "    h="+h+"    oldw="+oldw+ "   oldh="+oldh);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
	}

	public void setProgress(int progress){
		this.progress = progress > 100 ? 100 : progress;
		computeWaveToTop();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus){
		super.onWindowFocusChanged(hasWindowFocus);
		if (hasWindowFocus) {
			computeWaveToTop();
		}
	}

	private void computeWaveToTop(){
		mWaveToTop = (int) (getHeight() * (1f - progress / 100f));
		ViewGroup.LayoutParams params = mWave.getLayoutParams();
		if (params != null) {
			((LayoutParams) params).topMargin = mWaveToTop;
		}
		mWave.setLayoutParams(params);
	}

//	@Override
//	public Parcelable onSaveInstanceState(){
//		// Force our ancestor class to save its state
//		Parcelable superState = super.onSaveInstanceState();
//		SavedState ss = new SavedState(superState);
//		ss.progress = progress;
//		return ss;
//	}
	
//	@Override
//	public void onRestoreInstanceState(Parcelable state){
//		SavedState ss = (SavedState) state;
//		super.onRestoreInstanceState(ss.getSuperState());
//		setProgress(ss.progress);
//	}
	
	@Override
	public Parcelable onSaveInstanceState(){
		return super.onSaveInstanceState();
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state){
		super.onRestoreInstanceState(state);
	}

//	private static class SavedState extends BaseSavedState{
//		int progress;
//
//		/**
//		 * Constructor called from {@link android.widget.ProgressBar#onSaveInstanceState()}
//		 */
//		SavedState(Parcelable superState){
//			super(superState);
//		}
//
//		/**
//		 * Constructor called from {@link #CREATOR}
//		 */
//		private SavedState(Parcel in){
//			super(in);
//			progress = in.readInt();
//		}
//
//		@Override
//		public void writeToParcel(Parcel out, int flags){
//			super.writeToParcel(out, flags);
//			out.writeInt(progress);
//		}
//
//		@SuppressWarnings("unused")
//		public static final Creator<SavedState> CREATOR = new Creator<SavedState>(){
//			public SavedState createFromParcel(Parcel in){
//				return new SavedState(in);
//			}
//
//			public SavedState[] newArray(int size){
//				return new SavedState[size];
//			}
//		};
//	}
	
	@SuppressLint("InlinedApi")
	class Solid extends View{
		private Paint aboveWavePaint;
		private Paint blowWavePaint;

		public Solid(Context context, AttributeSet attrs){
			this(context, attrs, 0);
		}

		public Solid(Context context, AttributeSet attrs, int defStyleAttr){
			super(context, attrs, defStyleAttr);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight = 1;
			setLayoutParams(params);
		}

		public void setAboveWavePaint(Paint aboveWavePaint){
			this.aboveWavePaint = aboveWavePaint;
		}

		public void setBlowWavePaint(Paint blowWavePaint){
			this.blowWavePaint = blowWavePaint;
		}

		@Override
		protected void onDraw(Canvas canvas){
			super.onDraw(canvas);
			canvas.drawRect(getLeft(), 0, getRight(), getBottom(), blowWavePaint);
			canvas.drawRect(getLeft(), 0, getRight(), getBottom(), aboveWavePaint);
		}
	}

	// y=Asin(ωx+φ)+k
	class Wave extends View{
		private final int WAVE_HEIGHT_LARGE = 16;
		private final int WAVE_HEIGHT_MIDDLE = 8;
		private final int WAVE_HEIGHT_LITTLE = 5;
		private final float WAVE_LENGTH_MULTIPLE_LARGE = 1.5f;
		private final float WAVE_LENGTH_MULTIPLE_MIDDLE = 1f;
		private final float WAVE_LENGTH_MULTIPLE_LITTLE = 0.5f;
		private final float WAVE_HZ_FAST = 0.13f;
		private final float WAVE_HZ_NORMAL = 0.09f;
		private final float WAVE_HZ_SLOW = 0.05f;
		public final int DEFAULT_ABOVE_WAVE_ALPHA = 50;
		public final int DEFAULT_BLOW_WAVE_ALPHA = 30;
		private final float X_SPACE = 20;
		private final double PI2 = 2 * Math.PI;
		private Path mAboveWavePath = new Path();
		private Path mBlowWavePath = new Path();
		private Paint mAboveWavePaint = new Paint();
		private Paint mBlowWavePaint = new Paint();
		private int mAboveWaveColor;
		private int mBlowWaveColor;
		private float mWaveMultiple;
		private float mWaveLength;
		private int mWaveHeight;
		private float mMaxRight;
		private float mWaveHz;
		// wave animation
		private float mAboveOffset = 0.0f;
		private float mBlowOffset;
		private RefreshProgressRunnable mRefreshProgressRunnable;
		private int left, right, bottom;
		// ω
		private double omega;

		public Wave(Context context, AttributeSet attrs){
			this(context, attrs, R.attr.waveViewStyle);
		}

		public Wave(Context context, AttributeSet attrs, int defStyle){
			super(context, attrs, defStyle);
		}

		@Override
		protected void onDraw(Canvas canvas){
			super.onDraw(canvas);
			canvas.drawPath(mBlowWavePath, mBlowWavePaint);
			canvas.drawPath(mAboveWavePath, mAboveWavePaint);
		}

		public void setAboveWaveColor(int aboveWaveColor){
			this.mAboveWaveColor = aboveWaveColor;
		}

		public void setBlowWaveColor(int blowWaveColor){
			this.mBlowWaveColor = blowWaveColor;
		}

		public Paint getAboveWavePaint(){
			return mAboveWavePaint;
		}

		public Paint getBlowWavePaint(){
			return mBlowWavePaint;
		}

		public void initializeWaveSize(int waveMultiple, int waveHeight, int waveHz){
			mWaveMultiple = getWaveMultiple(waveMultiple);
			mWaveHeight = getWaveHeight(waveHeight);
			mWaveHz = getWaveHz(waveHz);
			mBlowOffset = mWaveHeight * 0.4f;
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mWaveHeight * 2);
			setLayoutParams(params);
		}

		public void initializePainters(){
			mAboveWavePaint.setColor(mAboveWaveColor);
			mAboveWavePaint.setAlpha(DEFAULT_ABOVE_WAVE_ALPHA);
			mAboveWavePaint.setStyle(Paint.Style.FILL);
			mAboveWavePaint.setAntiAlias(true);
			mBlowWavePaint.setColor(mBlowWaveColor);
			mBlowWavePaint.setAlpha(DEFAULT_BLOW_WAVE_ALPHA);
			mBlowWavePaint.setStyle(Paint.Style.FILL);
			mBlowWavePaint.setAntiAlias(true);
		}

		private float getWaveMultiple(int size){
			switch (size) {
				case WaveView2.LARGE:
					return WAVE_LENGTH_MULTIPLE_LARGE;
				case WaveView2.MIDDLE:
					return WAVE_LENGTH_MULTIPLE_MIDDLE;
				case WaveView2.LITTLE:
					return WAVE_LENGTH_MULTIPLE_LITTLE;
			}
			return 0;
		}

		private int getWaveHeight(int size){
			switch (size) {
				case WaveView2.LARGE:
					return WAVE_HEIGHT_LARGE;
				case WaveView2.MIDDLE:
					return WAVE_HEIGHT_MIDDLE;
				case WaveView2.LITTLE:
					return WAVE_HEIGHT_LITTLE;
			}
			return 0;
		}

		private float getWaveHz(int size){
			switch (size) {
				case WaveView2.LARGE:
					return WAVE_HZ_FAST;
				case WaveView2.MIDDLE:
					return WAVE_HZ_NORMAL;
				case WaveView2.LITTLE:
					return WAVE_HZ_SLOW;
			}
			return 0;
		}

		/**
		 * calculate wave track
		 */
		private void calculatePath(){
			mAboveWavePath.reset();
			mBlowWavePath.reset();
			getWaveOffset();
			float y;
			mAboveWavePath.moveTo(left, bottom);
			for (float x = 0; x <= mMaxRight; x += X_SPACE) {
				y = (float) (mWaveHeight * Math.sin(omega * x + mAboveOffset) + mWaveHeight);
				mAboveWavePath.lineTo(x, y);
			}
			mAboveWavePath.lineTo(right, bottom);
			mBlowWavePath.moveTo(left, bottom);
			for (float x = 0; x <= mMaxRight; x += X_SPACE) {
				y = (float) (mWaveHeight * Math.sin(omega * x + mBlowOffset) + mWaveHeight);
				mBlowWavePath.lineTo(x, y);
			}
			mBlowWavePath.lineTo(right, bottom);
		}

		@Override
		protected void onWindowVisibilityChanged(int visibility){
			System.out.println("=================onWindowVisibilityChanged=================");
			super.onWindowVisibilityChanged(visibility);
			if (View.GONE == visibility) {
				removeCallbacks(mRefreshProgressRunnable);
			} else {
				removeCallbacks(mRefreshProgressRunnable);
				mRefreshProgressRunnable = new RefreshProgressRunnable();
				post(mRefreshProgressRunnable);
			}
		}

		@Override
		protected void onDetachedFromWindow(){
			super.onDetachedFromWindow();
		}

		@Override
		public void onWindowFocusChanged(boolean hasWindowFocus){
			super.onWindowFocusChanged(hasWindowFocus);
			if (hasWindowFocus) {
				if (mWaveLength == 0) {
					startWave();
				}
			}
		}

		private void startWave(){
			if (getWidth() != 0) {
				int width = getWidth();
				mWaveLength = width * mWaveMultiple;
				left = getLeft();
				right = getRight();
				bottom = getBottom() + 2;
				mMaxRight = right + X_SPACE;
				omega = PI2 / mWaveLength;
			}
		}

		private void getWaveOffset(){
			if (mBlowOffset > Float.MAX_VALUE - 100) {
				mBlowOffset = 0;
			} else {
				mBlowOffset += mWaveHz;
			}
			if (mAboveOffset > Float.MAX_VALUE - 100) {
				mAboveOffset = 0;
			} else {
				mAboveOffset += mWaveHz;
			}
		}

		private class RefreshProgressRunnable implements Runnable{
			public void run(){
				synchronized (Wave.this) {
					long start = System.currentTimeMillis();
					calculatePath();
					invalidate();
					long gap = 16 - (System.currentTimeMillis() - start);
					postDelayed(this, gap < 0 ? 0 : gap);
				}
			}
		}
	}
}
