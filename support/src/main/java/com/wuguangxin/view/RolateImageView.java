package com.wuguangxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class RolateImageView extends ImageView{
	/**
	 * 缩放开始
	 */
	private static final int SCALE_START = 0;
	/**
	 * 缩放中
	 */
	private static final int SCALE_ING = 1;
	/**
	 * 缩放结束
	 */
	private static final int SCALE_END = 6;
	/**
	 * 控件的宽
	 */
	private int mWidth;
	/**
	 * 控件的高
	 */
	private int mHeight;
	/**
	 * 控件的宽1/2
	 */
	private int mCenterWidth;
	/**
	 * 控件的高 1/2
	 */
	private int mCenterHeight;
	/**
	 * 设置一个缩放的常量
	 */
	private float mMinScale = 0.85f;
	/**
	 * 缩放是否结束
	 */
	private boolean isFinish = true;

	public RolateImageView(Context context){
		this(context, null);
	}

	public RolateImageView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public RolateImageView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	/**
	 * 必要的初始化
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
			mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
			mCenterWidth = mWidth >> 1;
			mCenterHeight = mHeight >> 1;
			Drawable drawable = getDrawable();
			BitmapDrawable bd = (BitmapDrawable) drawable;
			bd.setAntiAlias(true); // 给Canvas加上抗锯齿标志
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mScaleHandler.sendEmptyMessage(SCALE_START);
				break;
			case MotionEvent.ACTION_UP:
				mScaleHandler.sendEmptyMessage(SCALE_END);
				break;
		}
		return true;
	}

	/**
	 * 控制缩放的Handler
	 */
	private Handler mScaleHandler = new Handler(){
		private Matrix matrix = new Matrix();
		private int count = 0;
		private float s;
		/**
		 * 是否已经调用了点击事件
		 */
		private boolean isClicked;

		public void handleMessage(android.os.Message msg){
			matrix.set(getImageMatrix());
			switch (msg.what) {
				case SCALE_START:
					if (isFinish) {
						isFinish = false;
						count = 0;
						s = (float) Math.sqrt(Math.sqrt(mMinScale));
						beginScale(matrix, s);
						mScaleHandler.sendEmptyMessage(SCALE_ING);
					} else {
						mScaleHandler.sendEmptyMessage(SCALE_START);
					}
					break;
				case SCALE_ING:
					beginScale(matrix, s);
					if (count < 4) {
						mScaleHandler.sendEmptyMessage(SCALE_ING);
					} else {
						isFinish = true;
						if (mOnViewClickListener != null && !isClicked) {
							isClicked = true;
							mOnViewClickListener.onViewClick(RolateImageView.this);
						} else {
							isClicked = false;
						}
					}
					count++;
					break;
				case SCALE_END:
					if (isFinish) {
						isFinish = false;
						count = 0;
						s = (float) Math.sqrt(Math.sqrt(1.0f / mMinScale));
						beginScale(matrix, s);
						mScaleHandler.sendEmptyMessage(SCALE_ING);
					} else {
						mScaleHandler.sendEmptyMessage(SCALE_END);
					}
					break;
			}
		}
	};

	/**
	 * 缩放
	 * 
	 * @param matrix
	 * @param scale
	 */
	private synchronized void beginScale(Matrix matrix, float scale){
		matrix.postScale(scale, scale, mCenterWidth, mCenterHeight);
		setImageMatrix(matrix);
	}

	/**
	 * 回调接口
	 */
	private OnViewClickListener mOnViewClickListener;

	public void setOnClickIntent(OnViewClickListener onViewClickListener){
		this.mOnViewClickListener = onViewClickListener;
	}

	public interface OnViewClickListener{
		void onViewClick(RolateImageView view);
	}
}
