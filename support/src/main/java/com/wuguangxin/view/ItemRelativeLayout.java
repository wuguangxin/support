package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wuguangxin.R;
import com.wuguangxin.utils.Utils;

/**
 * 带有上分割线、下分割线的 RelativeLayout
 *
 * <p>Created by wuguangxin on 15/7/10 </p>
 */
public class ItemRelativeLayout extends RelativeLayout {
	private static final int DEF_DIVIDER_COLOR = 0xffd1d1d1;
	private static final float DEF_DIVIDER_SIZE = 0.5F;
	private float lineSize = DEF_DIVIDER_SIZE;
	private float topPaddingLeft;
	private float topPaddingRight;
	private float bottomPaddingLeft;
	private float bottomPaddingRight;
	private int width;
	private int height;
	private int dividerColor = 0xffd1d1d1;
	private Paint paint;
	private DividerMode dividerMode = DividerMode.Both;

	public ItemRelativeLayout(Context context) {
		this(context, null);
	}

	public ItemRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ItemRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if(attrs != null){
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemTextView);
			if(a != null){
				dividerColor = a.getColor(R.styleable.ItemTextView_dividerColor, DEF_DIVIDER_COLOR);
				topPaddingLeft = a.getDimension(R.styleable.ItemTextView_topPaddingLeft, 0);
				topPaddingRight = a.getDimension(R.styleable.ItemTextView_topPaddingRight, 0);
				bottomPaddingLeft = a.getDimension(R.styleable.ItemTextView_bottomPaddingLeft, 0);
				bottomPaddingRight = a.getDimension(R.styleable.ItemTextView_bottomPaddingRight, 0);
				int integer = a.getInteger(R.styleable.ItemTextView_dividerMode, DividerMode.Both.value);
				dividerMode = DividerMode.fromValue(integer);
				a.recycle();
			}
		}

//		oldBackgroundDrawable = getBackground();

		lineSize = Utils.dip2px(context, DEF_DIVIDER_SIZE);
		paint = new Paint();
		paint.setColor(dividerColor);//设置线条颜色
		paint.setStrokeWidth(lineSize);// 线条宽度
		paint.setAntiAlias(true);//去除锯齿
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (dividerMode == DividerMode.None) {
			return;
		}

		width = getMeasuredWidth();
		height = getMeasuredHeight();

		float topStartX = 0 + topPaddingLeft;
		float topStartY = lineSize / 2;
		float topEndX = width - topPaddingRight;
		float topEndY = lineSize / 2;

		float bottomStartX = 0 + bottomPaddingLeft;
		float bottomStartY = height - lineSize / 2;
		float bottomEndX = width - bottomPaddingRight;
		float bottomEndY = height - lineSize / 2;

		switch (dividerMode) {
		case None:
			break;
		case Both:
			canvas.drawLine(topStartX, topStartY, topEndX, topEndY, paint); // Top
			canvas.drawLine(bottomStartX, bottomStartY, bottomEndX, bottomEndY, paint); // Bottom
			break;
		case Top:
			canvas.drawLine(topStartX, topStartY, topEndX, topEndY, paint); // Top
			break;
		case Bottom:
			canvas.drawLine(bottomStartX, bottomStartY, bottomEndX, bottomEndY, paint); // Bottom
			break;
		}
	}

	/**
	 * 分割线显示模式
	 *
	 * <p>Created by wuguangxin on 15/7/10 </p>
	 */
	public enum DividerMode {
		/**
		 * 不显示分割线
		 */
		None(0),
		/**
		 * 显示上下分割线
		 */
		Both(1),
		/**
		 * 只显示上面的分割线
		 */
		Top(2),
		/**
		 * 只显示下面的分割线
		 */
		Bottom(3);
		public final int value;

		DividerMode(int value){
			this.value = value;
		}

		public static DividerMode fromValue(int value){
			for (DividerMode position: DividerMode.values()) {
				if (position.value == value) {
					return position;
				}
			}
			return null;
		}
	}

//	private static int DEF_BACKGROUND_COLOR_TOUCH = 0xffF6F6F6;
//	private final Drawable oldBackgroundDrawable;
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()){
//		case MotionEvent.ACTION_DOWN:
//			setBackgroundColor(DEF_BACKGROUND_COLOR_TOUCH);
//			break;
//		case MotionEvent.ACTION_UP:
//		case MotionEvent.ACTION_CANCEL:
//			setBackgroundColor(Color.TRANSPARENT);
//			setBackgroundDrawable(oldBackgroundDrawable);
//			break;
//		}
//		return super.onTouchEvent(event);
//	}
}
