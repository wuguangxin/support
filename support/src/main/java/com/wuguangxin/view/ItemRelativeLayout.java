package com.wuguangxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wuguangxin.R;

/**
 * 带有上分割线、下分割线的 RelativeLayout
 *
 * <p>Created by wuguangxin on 15/7/10 </p>
 */
public class ItemRelativeLayout extends RelativeLayout {
	private int topMarginLeft;
	private int topMarginRight;
	private int bottomMarginLeft;
	private int bottomMarginRight;
	private int width;
	private int height;
	private int dividerSize;
	private Drawable divider;
	private int dividerHeight;
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
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemRelativeLayout);
			if(a != null){
				divider = a.getDrawable(R.styleable.ItemRelativeLayout_divider);
				dividerMode = DividerMode.fromValue(a.getInteger(R.styleable.ItemRelativeLayout_dividerMode, dividerMode.value));
				dividerHeight = a.getDimensionPixelSize(R.styleable.ItemRelativeLayout_dividerHeight, dividerHeight);
				topMarginLeft = a.getDimensionPixelSize(R.styleable.ItemRelativeLayout_dividerTop_marginLeft, topMarginLeft);
				topMarginRight = a.getDimensionPixelSize(R.styleable.ItemRelativeLayout_dividerTop_marginRight, topMarginRight);
				bottomMarginLeft = a.getDimensionPixelSize(R.styleable.ItemRelativeLayout_dividerBottom_marginLeft, bottomMarginLeft);
				bottomMarginRight = a.getDimensionPixelSize(R.styleable.ItemRelativeLayout_dividerBottom_marginRight, bottomMarginRight);

				a.recycle();
			}
		}
		dividerSize = divider != null ? divider.getIntrinsicHeight() : 0;
//		dividerSize = dividerHeight;

		// 不设置颜色，就看不到线条，待解决（暂时判断未设置颜色时，使用透明色）
		if (getBackground() == null) {
			setBackgroundColor(Color.TRANSPARENT);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (dividerMode == DividerMode.None) {
			return;
		}

		width = getMeasuredWidth();
		height = getMeasuredHeight();

		switch (dividerMode) {
		case None:
			break;
		case Both:
			drawDividerTop(canvas);
			drawDividerBottom(canvas);
			break;
		case Top:
			drawDividerTop(canvas);
			break;
		case Bottom:
			drawDividerBottom(canvas);
			break;
		}
	}

	/**
	 * 画上线
	 * @param canvas
	 */
	private void drawDividerTop(Canvas canvas) {
		if(divider != null){
			divider.setBounds(topMarginLeft, 0, width - topMarginRight, dividerSize);
			divider.draw(canvas);
		}
	}

	/**
	 * 画下线
	 * @param canvas
	 */
	private void drawDividerBottom(Canvas canvas) {
		if(divider != null){
			divider.setBounds(bottomMarginLeft, height - dividerSize, width - bottomMarginRight, width);
			divider.draw(canvas);
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
}
