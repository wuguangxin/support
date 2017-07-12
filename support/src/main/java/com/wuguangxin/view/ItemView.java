package com.wuguangxin.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuguangxin.R;

/**
 * Item项使用的View（组合控件）
 *
 * <p>Created by wuguangxin on 15/7/10 </p>
 */
public class ItemView extends LinearLayout{
	private static final float DEFAULT_KEY_SIZE = 14; // 默认key字体大小(DIP)
	private static final int DEFAULT_KEY_COLOR = 0xff333333; // 默认key字体颜色
	private static final int DEFAULT_KEY_WIDTH = LayoutParams.WRAP_CONTENT; // 默认key宽度
	private static final float DEFAULT_VALUE_SIZE = 14; // 默认Value字体大小(DIP)
	private static final int DEFAULT_VALUE_COLOR = 0xff333333; // 默认Value字体颜色
	private static final int DEFAULT_VALUE_WIDTH = LayoutParams.WRAP_CONTENT; // 默认Value宽

	private static final float DEFAULT_DIVIDER_SIZE = 0.5F; // 默认divider大小(DIP)
	private static final int DEFAULT_DIVIDER_COLOR = 0xffDFDFDF; // 默认divider颜色
	private static final int DEFAULT_LINE_HEIGHT = 1; // 默认上下分割线大小
	private static final boolean DEFAULT_SINGLE_LINE_KEY = true; // 默认单行文本-KEY
	private static final boolean DEFAULT_SINGLE_LINE_VALUE = true; // 默认单行文本-VALUE
	private static final DividerMode DEFAULT_DIVIDER = DividerMode.None; // 默认不显示
	// 左边的图标
	private static final float DEFAULT_ICON_LEFT_WIDTH = 0; // 左边图标默认宽(DIP)
	private static final float DEFAULT_ICON_LEFT_HEIGHT = 0; // 左边图标默认高(DIP)
	private static final float DEFAULT_ICON_LEFT_MARGIN_LEFT = 0; // 左边图标外边距左(DIP)
	private static final float DEFAULT_ICON_LEFT_MARGIN_TOP = 0;// 左边图标外边距上(DIP)
	private static final float DEFAULT_ICON_LEFT_MARGIN_RIGHT = 10; // 左边图标外边距右(DIP)
	private static final float DEFAULT_ICON_LEFT_MARGIN_BOTTOM = 0;// 左边图标外边距下(DIP)
	// 右边的图标
	private static final float DEFAULT_ICON_RIGHT_WIDTH = 0; // 右边图标外边距(DIP)
	private static final float DEFAULT_ICON_RIGHT_HEIGHT = 0;// 右边图标默认高(DIP)
	private static final float DEFAULT_ICON_RIGHT_MARGIN_LEFT = 5;// 右边图标外边距左(DIP)
	private static final float DEFAULT_ICON_RIGHT_MARGIN_TOP = 0;// 右边图标外边距上(DIP)
	private static final float DEFAULT_ICON_RIGHT_MARGIN_RIGHT = 0;// 右边图标外边距右(DIP)
	private static final float DEFAULT_ICON_RIGHT_MARGIN_BOTTOM = 0;// 右边图标外边距下(DIP)
	private LayoutParams iconLeftParams;
	private LayoutParams iconRightParams;
	private ImageView iconLeft;
	private ImageView iconRight;
	private TextView keyView;
	private TextView valueView;
	private DividerMode mItemDividerMode;
	private int dividerColor; // 线条颜色
	private float dividerSize; // 线条大小
	private Context context;

	public ItemView(Context context){
		this(context, null);
	}

	public ItemView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
		init(attrs, -1);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ItemView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs, defStyle);
	}

	@Override
	final public void setOrientation(int orientation) {
		if (orientation == VERTICAL) {
			throw new SecurityException("只能设置为水平排列：HORIZONTAL");
		}
		super.setOrientation(HORIZONTAL);
	}

	@SuppressWarnings("deprecation")
	private void init(AttributeSet attrs, int defStyle){
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		// 获取属性方法2_1，使用attr.xml
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemView, defStyle, 0);
		// 线
		dividerColor = a.getColor(R.styleable.ItemView_item_dividerColor, DEFAULT_DIVIDER_COLOR);
		dividerSize = a.getDimension(R.styleable.ItemView_item_dividerSize, dip2px(DEFAULT_DIVIDER_SIZE));
		mItemDividerMode = DividerMode.fromValue(a.getInteger(R.styleable.ItemView_item_dividerMode, DEFAULT_DIVIDER.value));

		topDividerMargin = a.getDimension(R.styleable.ItemView_topDividerMargin, 0);
		topDividerMarginLeft = a.getDimension(R.styleable.ItemView_topDividerMarginLeft, 0);
		topDividerMarginRight = a.getDimension(R.styleable.ItemView_topDividerMarginRight, 0);
		bottomDividerMargin = a.getDimension(R.styleable.ItemView_bottomDividerMargin, 0);
		bottomDividerMarginLeft = a.getDimension(R.styleable.ItemView_bottomDividerMarginLeft, 0);
		bottomDividerMarginRight = a.getDimension(R.styleable.ItemView_bottomDividerMarginRight, 0);

		// 左图标
		Drawable drawableLeft = a.getDrawable(R.styleable.ItemView_item_iconLeft);
		int iconLeftWidth = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_width, dip2px(DEFAULT_ICON_LEFT_WIDTH));
		int iconLeftHeight = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_height, dip2px(DEFAULT_ICON_LEFT_HEIGHT));
		int iconLeftMarginLeft = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_margin_left, dip2px(DEFAULT_ICON_LEFT_MARGIN_LEFT));
		int iconLeftMarginTop = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_margin_top, dip2px(DEFAULT_ICON_LEFT_MARGIN_TOP));
		int iconLeftMarginRight = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_margin_right, dip2px(DEFAULT_ICON_LEFT_MARGIN_RIGHT));
		int iconLeftMarginBottom = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_margin_bottom, dip2px(DEFAULT_ICON_LEFT_MARGIN_BOTTOM));
		// 右图标
		Drawable drawableRight = a.getDrawable(R.styleable.ItemView_item_iconRight);
		int iconRightWidth = (int) a.getDimension(R.styleable.ItemView_item_iconRight_width, dip2px(DEFAULT_ICON_RIGHT_WIDTH));
		int iconRightHeight = (int) a.getDimension(R.styleable.ItemView_item_iconRight_height, dip2px(DEFAULT_ICON_RIGHT_HEIGHT));
		int iconRightMarginLeft = (int) a.getDimension(R.styleable.ItemView_item_iconRight_margin_left, dip2px(DEFAULT_ICON_RIGHT_MARGIN_LEFT));
		int iconRightMarginTop = (int) a.getDimension(R.styleable.ItemView_item_iconRight_margin_top, dip2px(DEFAULT_ICON_RIGHT_MARGIN_TOP));
		int iconRightMarginRight = (int) a.getDimension(R.styleable.ItemView_item_iconRight_margin_right, dip2px(DEFAULT_ICON_RIGHT_MARGIN_RIGHT));
		int iconRightMarginBottom = (int) a.getDimension(R.styleable.ItemView_item_iconRight_margin_bottom, dip2px(DEFAULT_ICON_RIGHT_MARGIN_BOTTOM));
		// 文字-key
		String text = a.getString(R.styleable.ItemView_item_key);
		float keySize = a.getDimension(R.styleable.ItemView_item_keySize, DEFAULT_KEY_SIZE);
		int keyColor = a.getColor(R.styleable.ItemView_item_keyColor, DEFAULT_KEY_COLOR);
		int keyWidth = a.getColor(R.styleable.ItemView_item_keyWidth, DEFAULT_KEY_WIDTH);
		// 文字-value
		String value = a.getString(R.styleable.ItemView_item_value);
		float valueSize = a.getDimension(R.styleable.ItemView_item_valueSize, DEFAULT_VALUE_SIZE);
		int valueColor = a.getColor(R.styleable.ItemView_item_valueColor, DEFAULT_VALUE_COLOR);
		int valueWidth = a.getColor(R.styleable.ItemView_item_valueWidth, DEFAULT_VALUE_WIDTH);
		// 
		boolean keySingleLine = a.getBoolean(R.styleable.ItemView_item_keySingleLine, DEFAULT_SINGLE_LINE_KEY);
		boolean valueSingleLine = a.getBoolean(R.styleable.ItemView_item_valueSingleLine, DEFAULT_SINGLE_LINE_VALUE);
		a.recycle();

		// item 左边图标
		iconLeft = new ImageView(context);
		if (iconLeftWidth == 0) iconLeftWidth = LayoutParams.WRAP_CONTENT;
		if (iconLeftHeight == 0) iconLeftHeight = LayoutParams.WRAP_CONTENT;
		iconLeftParams = new LayoutParams(iconLeftWidth, iconLeftHeight);
		iconLeftParams.setMargins(iconLeftMarginLeft, iconLeftMarginTop, iconLeftMarginRight, iconLeftMarginBottom);
		iconLeft.setLayoutParams(iconLeftParams);
		if (drawableLeft != null) {
			iconLeft.setImageDrawable(drawableLeft);
			iconLeft.setVisibility(View.VISIBLE);
		} else {
			iconLeft.setVisibility(View.GONE);
		}
		// item 右边图标
		iconRight = new ImageView(context);
		if (iconRightWidth == 0) iconRightWidth = LayoutParams.WRAP_CONTENT;
		if (iconRightHeight == 0) iconRightHeight = LayoutParams.WRAP_CONTENT;
		iconRightParams = new LayoutParams(iconRightWidth, iconRightHeight);
		iconRightParams.setMargins(iconRightMarginLeft, iconRightMarginTop, iconRightMarginRight, iconRightMarginBottom);
		iconRight.setLayoutParams(iconRightParams);
		if (drawableRight != null) {
			iconRight.setImageDrawable(drawableRight);
			iconRight.setVisibility(View.VISIBLE);
		} else {
			iconRight.setVisibility(View.GONE);
		}
		// key文字
		keyView = new TextView(context);
		LayoutParams keyParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		keyView.setText(text);
		keyView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, px2dip(keySize));
		keyView.setTextColor(keyColor);
		keyView.setSingleLine(keySingleLine);
		keyView.setLayoutParams(keyParams);
		// value文字
		valueView = new TextView(context);
		LayoutParams valueParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		valueView.setText(value);
		valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, px2dip(valueSize));
		valueView.setTextColor(valueColor);
		valueView.setSingleLine(valueSingleLine);
		valueView.setLayoutParams(valueParams);
		// 加入layout
		removeAllViews();
		addView(iconLeft);
		addView(keyView);
		addView(valueView);
		addView(iconRight);
		initDivider(context);
	}

	/**
	 * 如果设置此值，则设置以下将无效：
	 * topDividerMargin、
	 * topDividerMarginLeft、
	 * topDividerMarginRight、
	 *
	 * bottomDividerMargin、
	 * bottomDividerMarginLeft、
	 * bottomDividerMarginRight。
	 */
	private float dividerMargin = 0; // px

	/**
	 * 如果设置此值，则设置以下将无效：
	 * topDividerMarginLeft、
	 * topDividerMarginRight、
	 */
	private float topDividerMargin = 0; // px
	private float topDividerMarginLeft = 0; // px
	private float topDividerMarginRight = 0; // px

	/**
	 * 如果设置此值，则设置以下将无效：
	 * bottomDividerMarginLeft、
	 * bottomDividerMarginRight、
	 */
	private float bottomDividerMargin = 0; // px
	private float bottomDividerMarginLeft = 0; // px
	private float bottomDividerMarginRight = 0; // px

	private Paint dividerPaint;




	private void initDivider(Context context) {
		this.context = context;
		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);//去除锯齿
		dividerPaint.setStrokeWidth(dividerSize);// 线条宽度
		dividerPaint.setColor(dividerColor);//设置线条颜色
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		initMargin();
		switch (mItemDividerMode) {
		case None:
			break;
		case SingleTop:
			drawDividerTop(canvas);	// 上
			break;
		case SingleBottom:
			drawDividerBottom(canvas); // 下
			break;
		case Both:
			drawDividerTop(canvas);	// 上
			drawDividerBottom(canvas); // 下
			break;
		default:
			break;
		}
	}

	private void initMargin() {
		if(dividerMargin > 0){
			topDividerMargin = topDividerMarginLeft = topDividerMarginRight = dividerMargin;
			bottomDividerMargin = bottomDividerMarginLeft = bottomDividerMarginRight = dividerMargin;
		} else if(topDividerMargin > 0){
			topDividerMarginLeft = topDividerMarginRight = topDividerMargin;
		} else if(bottomDividerMargin > 0){
			bottomDividerMarginLeft = bottomDividerMarginRight = bottomDividerMargin;
		}
	}

	/**
	 * 画上线
	 * @param canvas
	 */
	private void drawDividerTop(Canvas canvas) {
		int measuredWidth = getMeasuredWidth();
		float topStartX = 0 + topDividerMarginLeft;
		float topStopX = measuredWidth - topDividerMarginRight;
		float topStartY = 0 + dividerSize/2;
		float topStopY = topStartY;
		canvas.drawLine(topStartX, topStartY, topStopX, topStopY, dividerPaint); // 上
	}

	/**
	 * 画下线
	 * @param canvas
	 */
	private void drawDividerBottom(Canvas canvas) {
		int measuredWidth = getMeasuredWidth();
		int measuredHeight = getMeasuredHeight();
		float bottomStartX = 0 + bottomDividerMarginLeft;
		float bottomStopX = measuredWidth - bottomDividerMarginRight;
		float bottomStartY = measuredHeight - dividerSize/2;
		float bottomStopY = bottomStartY;
		canvas.drawLine(bottomStartX, bottomStartY, bottomStopX, bottomStopY, dividerPaint); // 下
	}

	public TextView getKeyView(){
		return keyView;
	}

	/**
	 * 设置Key文本资源ID
	 * @param resId 文本资源ID
	 * @deprecated use #setKey
	 */
	public void setText(int resId){
		keyView.setText(getResources().getString(resId));
	}

	/**
	 * 设置Key文本
	 * @param text 文本
	 * @deprecated use #setKey
	 */
	public void setText(String text){
		keyView.setText(text);
	}

	/**
	 * 设置Key文本
	 * @param text 字符
	 * @deprecated use #setKey
	 */
	public void setText(CharSequence text){
		keyView.setText(text);
	}

	/**
	 * 设置Key文本大小
	 * @param textSize 文本大小
	 * @deprecated use #setKeySize
	 */
	public void setTextSize(float textSize){
		keyView.setTextSize(textSize);
	}

	/**
	 * 设置Key文本颜色
	 * @param color 文本颜色
	 * @deprecated use #setKeyColor
	 */
	public void setTextColor(int color){
		keyView.setTextColor(color);
	}

	/**
	 * 设置Key文本颜色
	 * @param textColor 文本颜色，如 #FFFFFF
	 * @deprecated use #setKeyColor
	 */
	public void setTextColor(String textColor){
		try {
			keyView.setTextColor(Color.parseColor(textColor));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置Key文本资源ID
	 * @param resId 文本资源ID
	 */
	public void setKey(int resId){
		keyView.setText(getResources().getString(resId));
	}

	/**
	 * 设置Key文本
	 * @param text 文本
	 */
	public void setKey(String text){
		keyView.setText(text);
	}

	/**
	 * 设置Key文字
	 * @param text 文本
	 */
	public void setKey(CharSequence text){
		keyView.setText(text);
	}

	/**
	 * 设置Key文字大小
	 * @param textSize 文本大小
	 */
	public void setKeySize(float textSize){
		keyView.setTextSize(textSize);
	}

	/**
	 * 设置Key文字颜色
	 * @param color 文本颜色
	 */
	public void setKeyColor(int color){
		keyView.setTextColor(color);
	}

	/**
	 * 设置Key文字颜色字符
	 * @param textColor 文本颜色 如#FFFFFF
	 */
	public void setKeyColor(String textColor){
		try {
			keyView.setTextColor(Color.parseColor(textColor));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setValue(int resId){
		valueView.setText(getResources().getString(resId));
	}

	public void setValue(String value){
		valueView.setText(value);
	}

	public void setValue(CharSequence value){
		valueView.setText(value);
	}

	public TextView getValueView(){
		return valueView;
	}

	public void setValueSize(float valueSize){
		valueView.setTextSize(valueSize);
	}

	public void setValueColor(int color){
		valueView.setTextColor(color);
	}

	public void setValueColor(String valueColor){
		try {
			valueView.setTextColor(Color.parseColor(valueColor));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setValueGravity(int gravity){
		valueView.setGravity(gravity);
	}

	public void setIconLeftMargins(int left, int top, int right, int bottom){
		iconLeftParams.setMargins(left, top, right, bottom);
	}

	public void setIconRightMargins(int left, int top, int right, int bottom){
		iconRightParams.setMargins(left, top, right, bottom);
	}

	/**
	 * 设置左边图标ID
	 * @param resid 左边图标ID
	 */
	public void setIconLeftImageResource(int resid){
		iconLeft.setBackgroundResource(resid);
	}

	/**
	 * 设置左边图标Drawable
	 * @param drawable 左边图标Drawable
	 */
	@SuppressWarnings("deprecation")
	public void setIconLeftImageResource(Drawable drawable){
		iconLeft.setBackgroundDrawable(drawable);
	}

	/**
	 * 设置右边图标id
	 * @param resid 右边图标ID
	 */
	public void setIconRightImageResource(int resid){
		iconRight.setBackgroundResource(resid);
	}

	/**
	 * 设置右边图标Drawable
	 * @param drawable 右边图标Drawable
	 */
	@SuppressWarnings("deprecation")
	public void setIconRightImageDrawable(Drawable drawable){
		iconRight.setBackgroundDrawable(drawable);
	}

	public void setDividerMargin(float dividerMargin) {
		this.dividerMargin = dividerMargin;
		invalidate();
	}

	public void setTopDividerMargin(float topDividerMargin) {
		this.topDividerMargin = topDividerMargin;
		invalidate();
	}

	public void setTopDividerMarginLeft(float topDividerMarginLeft) {
		this.topDividerMarginLeft = topDividerMarginLeft;
		invalidate();
	}

	public void setTopDividerMarginRight(float topDividerMarginRight) {
		this.topDividerMarginRight = topDividerMarginRight;
		invalidate();
	}

	public void setBottomDividerMargin(float bottomDividerMargin) {
		this.bottomDividerMargin = bottomDividerMargin;
		invalidate();
	}

	public void setBottomDividerMarginLeft(float bottomDividerMarginLeft) {
		this.bottomDividerMarginLeft = bottomDividerMarginLeft;
		invalidate();
	}

	public void setBottomDividerMarginRight(float bottomDividerMarginRight) {
		this.bottomDividerMarginRight = bottomDividerMarginRight;
		invalidate();
	}

	/**
	 * dip转换为px
	 * @param dipValue dip值
	 * @return xp值
	 */
	public float dip2px(float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		float px = dipValue * scale + 0.5f;
		return px;
	}

	/**
	 * px转换为dip
	 * @param pxValue px值
	 * @return dip值
	 */
	public float px2dip(float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return pxValue / scale + 0.5f;
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
		SingleTop(2),
		/**
		 * 只显示下面的分割线
		 */
		SingleBottom(3);
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
