package com.wuguangxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wuguangxin.R;

/**
 * 仿QQ5.5的滑动开关
 *
 * <p>Created by wuguangxin on 15/4/8 </p>
 */
public class SwitchView extends View{

	private Bitmap mSwitchBitmapON; // 开的背景图片
	private Bitmap mSwitchBitmapOFF; // 关的背景图片
	private Bitmap mSwitchBitmapSliderNormal; // 正常的滑动图片
	private Bitmap mSwitchBitmapSliderPressed; // 按下的滑动图片
	
	private Bitmap switchBackhroundBitmap; // 当前背景图片
	private Bitmap switchBitmapSliderBitmap; // 当前滑动图片
	
	private boolean currentState = false; //当前开关状态 true-开 false-关
	private int left;
	private int top;
	private float bgCenterX; // 滑动开关的中间点偏移量
	
	private OnSwitchChangeListener onSwitchChangeListener;
	private int toLeftX;
	private int sliderCenterX;
	
	public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener){
		this.onSwitchChangeListener = onSwitchChangeListener;
	}

	public SwitchView(Context context){
		super(context);
		init();
	}

	public SwitchView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	@SuppressLint("NewApi")
	private void init(){
		mSwitchBitmapON = BitmapFactory.decodeResource(getResources(), R.drawable.switch_bg_on);
		mSwitchBitmapOFF = BitmapFactory.decodeResource(getResources(), R.drawable.switch_bg_off);
		mSwitchBitmapSliderNormal = BitmapFactory.decodeResource(getResources(), R.drawable.switch_slider_normal);
		mSwitchBitmapSliderPressed = BitmapFactory.decodeResource(getResources(), R.drawable.switch_slider_pressed);
		
		setCurrentState(currentState);
		
		switchBitmapSliderBitmap = mSwitchBitmapSliderNormal;
		System.out.println("centerX=" + bgCenterX);
		bgCenterX = switchBackhroundBitmap.getWidth() >> 1;
		sliderCenterX = switchBitmapSliderBitmap.getWidth() >> 1;
		toLeftX = switchBackhroundBitmap.getWidth() - switchBitmapSliderBitmap.getWidth();
		left = currentState ? 0 : toLeftX;
		top = (switchBackhroundBitmap.getHeight() - switchBitmapSliderBitmap.getHeight()) >> 1;
	}
	
	public void setCurrentState(boolean currentState){
		this.currentState = currentState;
		switchBackhroundBitmap = currentState ? mSwitchBitmapON : mSwitchBitmapOFF;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(widthMeasureSpec <= 0 || heightMeasureSpec <= 0){ 
			setMeasuredDimension(mSwitchBitmapON.getWidth(), mSwitchBitmapON.getHeight());
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		// 画背景图片
		canvas.drawBitmap(switchBackhroundBitmap, 0, 0, null);
		// 画滑动图片
//		left = currentState ? 0 : toLeftX;
		
		if(left < 0){
			left = 0;
		}
		if(left > toLeftX){
			left = toLeftX;
		}
		canvas.drawBitmap(switchBitmapSliderBitmap, left, top, null);
	}
	
	@SuppressLint({
		"NewApi", "ClickableViewAccessibility"
	})
	@Override
	public boolean onTouchEvent(MotionEvent event){
		float downX = 0;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("ACTION_DOWN");
				switchBitmapSliderBitmap = mSwitchBitmapSliderPressed;
				downX = event.getX();
				System.out.println("centerX=" + bgCenterX);
				System.out.println("downX=" + downX);
				invalidate();
				break;
				
			case MotionEvent.ACTION_MOVE:
//				System.out.println("ACTION_MOVE");
				float moveX = event.getX();
				left = (int) moveX - sliderCenterX ;
//				System.out.println("left = " + left);
				setCurrentState(left < bgCenterX); 
				invalidate();
				break;
				
			case MotionEvent.ACTION_UP:
				System.out.println("ACTION_UP");
				float upX = event.getX();
				System.out.println("upX=" + upX);
				boolean state = upX < bgCenterX;
				left = state ? 0 : toLeftX;
				switchBitmapSliderBitmap = mSwitchBitmapSliderNormal;
				if(currentState == state && onSwitchChangeListener != null){
					onSwitchChangeListener.onSwitchChanged(this, state);
				}
				setCurrentState(state);
				invalidate();
				break;
		}
		return true;
	}
	
	public interface OnSwitchChangeListener {
		/**
		 * 当前状态。true: ON  false:OFF
		 * @param currentState
		 */
		void onSwitchChanged(SwitchView view, boolean currentState);
	}
	
}
