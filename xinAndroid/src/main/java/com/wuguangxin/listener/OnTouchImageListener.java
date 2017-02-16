package com.wuguangxin.listener;

import com.wuguangxin.utils.Utils;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 对图片多点缩放的监听器
 *
 * @author wuguangxin
 * @date: 2015-3-31 上午11:43:46
 */
public class OnTouchImageListener implements OnTouchListener {
	/** 图片移动前的矩阵 */
	private Matrix oldMatrix = new Matrix();
	/** 图片移动后的矩阵 */
	private Matrix newMatrix = new Matrix(); 
	/** 按下时手指的x、y轴偏移量 */
	private float downX, downY;
	/** 图片移动后的x和y轴偏移量 */
	private float moveX, moveY;
	/** 第二根手指按下时与第一根手指的距离 */
	private float startDistance;
	/** 其中一根手指移动后与另一根手指的距离 */
	private float lastDistance;
	/** 第二个手指移动时与第一根手指的中点坐标 */
	private PointF midpoint;
	/** 触摸类型, 1是单指触摸，2是双指触摸 */ 
	private int touchType = 0;
	private ImageView imageView;
	
	public OnTouchImageListener(ImageView imageView){
		this.imageView = imageView;
	}

	@SuppressLint({ "ClickableViewAccessibility", "InlinedApi" })
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: 				// 第一个手指按下	
				touchType = 1;												// 标记为单指触摸
				downX = event.getX();
				downY = event.getY();
				oldMatrix.set(imageView.getImageMatrix());	// 把imageView的矩阵设置为oldMatrix矩阵
				break;
				
			case MotionEvent.ACTION_POINTER_DOWN: 			// 第二个手指按下
				touchType = 2;												// 标记双指触摸
				startDistance = Utils.getDistance(event); 		// 获取第二根手指按下时与第一根手指的距离
				midpoint = Utils.getPoint(event); 				// 获取第二个手指移动时与第一根手指的中点坐标
				oldMatrix.set(imageView.getImageMatrix());	// 把imageView的矩阵设置为oldMatrix矩阵
				break;
				
			case MotionEvent.ACTION_MOVE: 					// 第二个手指移动
				// 放在ACTION_MOVE里改变ScaleType，不会看到改变时瞬间产生的一闪的情况。
				imageView.setScaleType(ImageView.ScaleType.MATRIX);   
				
				newMatrix.set(oldMatrix);
				moveX = event.getX() - downX;
				moveY = event.getY() - downY;
				if(touchType==1){
					newMatrix.postTranslate(moveX, moveY);
				}else if(touchType==2){
					lastDistance = Utils.getDistance(event);
					float scaling = lastDistance / startDistance;// 缩放比例=新的移动距离/之前的移动距离
					newMatrix.postScale(scaling, scaling, midpoint.x, midpoint.y); // 对图像矩阵进行缩放
				}
				break; 
				
			case MotionEvent.ACTION_UP: 							// 第一个手指按下
				touchType = 0;
				break;
				
			case MotionEvent.ACTION_POINTER_UP: 			// 其中一个手指抬起，但不是最后个	
				oldMatrix.set(imageView.getImageMatrix());
				touchType = 1;
				break;
				
			default:
				break;
		}
		imageView.setImageMatrix(newMatrix);	//改变图像容器中的图片
		return true; // 消费掉此事件
	}
}