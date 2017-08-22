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
	// key
	private static final int DEFAULT_KEY_COLOR = 0xff333333; // 默认key字体颜色
	private static final float DEFAULT_KEY_SIZE = 14; // 默认key字体大小(DIP)
	private static final float DEFAULT_KEY_WIDTH = LayoutParams.WRAP_CONTENT; // 默认key宽度
	private static final float DEFAULT_KEY_HEIGHT = LayoutParams.WRAP_CONTENT; // 默认key高度
	private static final float DEFAULT_KEY_WEIGHT = 0.0F; // 默认key权重
	private static final boolean DEFAULT_KEY_SINGLE_LINE = false; // 默认单行文本-KEY
	private static final GravityMode DEFAULT_KEY_GRAVITY = GravityMode.left; // 默认key对齐
	// value
	private static final int DEFAULT_VALUE_COLOR = 0xff333333; // 默认Value字体颜色
	private static final float DEFAULT_VALUE_SIZE = 14; // 默认Value字体大小(DIP)
	private static final float DEFAULT_VALUE_WIDTH = LayoutParams.WRAP_CONTENT; // 默认value宽度
	private static final float DEFAULT_VALUE_HEIGHT = LayoutParams.WRAP_CONTENT; // 默认value高度
	private static final float DEFAULT_VALUE_WEIGHT = 1.0F; // 默认value权重
	private static final boolean DEFAULT_VALUE_SINGLE_LINE = false; // 默认单行文本-VALUE
	private static final GravityMode DEFAULT_VALUE_GRAVITY = GravityMode.right; // 默认value对齐
	// 指示器
	private static final DividerMode DEFAULT_DIVIDER = DividerMode.None; // 默认不显示
	private static final int DEFAULT_DIVIDER_COLOR = 0xffDFDFDF; // 默认divider颜色
	private static final float DEFAULT_DIVIDER_SIZE = 0.5F; // 默认divider大小(DIP)
	// 左边 图标
	private static final float DEFAULT_ICON_LEFT_WIDTH = LayoutParams.WRAP_CONTENT; // 左边图标默认宽(DIP)
	private static final float DEFAULT_ICON_LEFT_HEIGHT = LayoutParams.WRAP_CONTENT; // 左边图标默认高(DIP)
	private static final float DEFAULT_ICON_LEFT_MARGIN_LEFT = 0; // 左边图标外边距左(DIP)
	private static final float DEFAULT_ICON_LEFT_MARGIN_TOP = 0;// 左边图标外边距上(DIP)
	private static final float DEFAULT_ICON_LEFT_MARGIN_RIGHT = 10; // 左边图标外边距右(DIP)
	private static final float DEFAULT_ICON_LEFT_MARGIN_BOTTOM = 0;// 左边图标外边距下(DIP)
	// 右边 图标
	private static final float DEFAULT_ICON_RIGHT_WIDTH = LayoutParams.WRAP_CONTENT; // 右边图标外边距(DIP)
	private static final float DEFAULT_ICON_RIGHT_HEIGHT = LayoutParams.WRAP_CONTENT;// 右边图标默认高(DIP)
	private static final float DEFAULT_ICON_RIGHT_MARGIN_LEFT = 5;// 右边图标外边距左(DIP)
	private static final float DEFAULT_ICON_RIGHT_MARGIN_TOP = 0;// 右边图标外边距上(DIP)
	private static final float DEFAULT_ICON_RIGHT_MARGIN_RIGHT = 0;// 右边图标外边距右(DIP)
	private static final float DEFAULT_ICON_RIGHT_MARGIN_BOTTOM = 0;// 右边图标外边距下(DIP)

	private Context context;
	private LayoutParams iconLeftParams;
	private LayoutParams iconRightParams;
	private LayoutParams keyParams;
	private LayoutParams valueParams;
	private ImageView mIconLeftView;
	private ImageView mIconRightView;
	private TextView mKeyView;
	private TextView mValueView;

	// 指示器
	private DividerMode dividerMode;
	private float dividerSize; // 线条大小
	private int dividerColor;  // 线条颜色
	// 左图标
	private Drawable iconLeft;
	private float iconLeftWidth;
	private float iconLeftHeight;
	private float iconLeftMarginLeft;
	private float iconLeftMarginTop;
	private float iconLeftMarginRight;
	private float iconLeftMarginBottom;
	// 右图标
	private Drawable iconRight;
	private float iconRightWidth;
	private float iconRightHeight;
	private float iconRightMarginLeft;
	private float iconRightMarginTop;
	private float iconRightMarginRight;
	private float iconRightMarginBottom;
	// 文字-key
	private String key;
	private int keyStyle;
	private int keyColor;
	private float keySize;
	private float keyWidth;
	private float keyHeight;
	private float keyWeight;
	private boolean keySingleLine;
	private GravityMode keyGravity;
	// 文字-value
	private String value;
	private int valueStyle;
	private int valueColor;
	private float valueSize;
	private float valueWidth;
	private float valueHeight;
	private float valueWeight;
	private boolean valueSingleLine;
	private GravityMode valueGravity;

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
		dividerColor = a.getColor(R.styleable.ItemView_dividerColor, DEFAULT_DIVIDER_COLOR);
		dividerSize = a.getDimension(R.styleable.ItemView_dividerSize, dip2px(DEFAULT_DIVIDER_SIZE));
		dividerMode = DividerMode.fromValue(a.getInteger(R.styleable.ItemView_dividerMode, DEFAULT_DIVIDER.value));

		dividerTopMargin = a.getDimension(R.styleable.ItemView_dividerTop_margin, 0);
		dividerTopMarginLeft = a.getDimension(R.styleable.ItemView_dividerTop_marginLeft, 0);
		dividerTopMarginRight = a.getDimension(R.styleable.ItemView_dividerTop_marginRight, 0);
		dividerBottomMargin = a.getDimension(R.styleable.ItemView_dividerBottom_margin, 0);
		dividerBottomMarginLeft = a.getDimension(R.styleable.ItemView_dividerBottom_marginLeft, 0);
		dividerBottomMarginRight = a.getDimension(R.styleable.ItemView_dividerBottom_marginRight, 0);

		// 左图标
		iconLeft = a.getDrawable(R.styleable.ItemView_iconLeft);
		iconLeftWidth = a.getDimension(R.styleable.ItemView_iconRight_width, LayoutParams.WRAP_CONTENT);
		iconLeftHeight = a.getDimension(R.styleable.ItemView_iconLeft_height, LayoutParams.WRAP_CONTENT);
		iconLeftMarginLeft = a.getDimension(R.styleable.ItemView_iconLeft_marginLeft, dip2px(DEFAULT_ICON_LEFT_MARGIN_LEFT));
		iconLeftMarginTop = a.getDimension(R.styleable.ItemView_iconLeft_marginTop, dip2px(DEFAULT_ICON_LEFT_MARGIN_TOP));
		iconLeftMarginRight = a.getDimension(R.styleable.ItemView_iconLeft_marginRight, dip2px(DEFAULT_ICON_LEFT_MARGIN_RIGHT));
		iconLeftMarginBottom = a.getDimension(R.styleable.ItemView_iconLeft_marginBottom, dip2px(DEFAULT_ICON_LEFT_MARGIN_BOTTOM));

		// 右图标
		iconRight = a.getDrawable(R.styleable.ItemView_iconRight);
		iconRightWidth = a.getDimension(R.styleable.ItemView_iconRight_width, LayoutParams.WRAP_CONTENT);
		iconRightHeight = a.getDimension(R.styleable.ItemView_iconRight_height, LayoutParams.WRAP_CONTENT);
		iconRightMarginLeft = a.getDimension(R.styleable.ItemView_iconRight_marginLeft, dip2px(DEFAULT_ICON_RIGHT_MARGIN_LEFT));
		iconRightMarginTop = a.getDimension(R.styleable.ItemView_iconRight_marginTop, dip2px(DEFAULT_ICON_RIGHT_MARGIN_TOP));
		iconRightMarginRight = a.getDimension(R.styleable.ItemView_iconRight_marginRight, dip2px(DEFAULT_ICON_RIGHT_MARGIN_RIGHT));
		iconRightMarginBottom = a.getDimension(R.styleable.ItemView_iconRight_marginBottom, dip2px(DEFAULT_ICON_RIGHT_MARGIN_BOTTOM));

		// key
		key = a.getString(R.styleable.ItemView_key);
		keySize = a.getDimension(R.styleable.ItemView_keySize, DEFAULT_KEY_SIZE);
		keyColor = a.getColor(R.styleable.ItemView_keyColor, DEFAULT_KEY_COLOR);
		keyWidth = a.getDimension(R.styleable.ItemView_keyWidth, LayoutParams.WRAP_CONTENT);
		keyHeight = a.getDimension(R.styleable.ItemView_keyWidth, LayoutParams.WRAP_CONTENT);
		keyWeight = a.getFloat(R.styleable.ItemView_keyWeight, DEFAULT_KEY_WEIGHT);
		keyStyle = a.getInt(R.styleable.ItemView_keyStyle, 0);
		keySingleLine = a.getBoolean(R.styleable.ItemView_keySingleLine, DEFAULT_KEY_SINGLE_LINE);
		keyGravity = GravityMode.fromValue(a.getInteger(R.styleable.ItemView_keyGravity, DEFAULT_KEY_GRAVITY.value));

		// value
		value = a.getString(R.styleable.ItemView_value);
		if (value != null) {
			valueSize = a.getDimension(R.styleable.ItemView_valueSize, DEFAULT_VALUE_SIZE);
			valueColor = a.getColor(R.styleable.ItemView_valueColor, DEFAULT_VALUE_COLOR);
			valueWidth = a.getDimension(R.styleable.ItemView_valueWidth, LayoutParams.WRAP_CONTENT);
			valueHeight = a.getDimension(R.styleable.ItemView_valueHeight, LayoutParams.WRAP_CONTENT);
			valueWeight = a.getFloat(R.styleable.ItemView_valueHeight, DEFAULT_VALUE_WEIGHT);
			valueStyle = a.getInt(R.styleable.ItemView_valueStyle, 0);
			valueSingleLine = a.getBoolean(R.styleable.ItemView_valueSingleLine, DEFAULT_VALUE_SINGLE_LINE);
			valueGravity = GravityMode.fromValue(a.getInteger(R.styleable.ItemView_valueGravity, DEFAULT_VALUE_GRAVITY.value));
		}

		a.recycle();

		if (getChildCount() > 0) {
			removeAllViews();
		}
		mIconLeftView = new ImageView(context);
		mIconRightView = new ImageView(context);
		mKeyView = new TextView(context);
		mValueView = new TextView(context);

		// icon left
		if (iconLeft != null) {
			iconLeftParams = new LayoutParams((int) iconLeftWidth, (int) iconLeftHeight);
			iconLeftParams.setMargins((int) iconLeftMarginLeft, (int) iconLeftMarginTop, (int) iconLeftMarginRight, (int) iconLeftMarginBottom);
			mIconLeftView.setLayoutParams(iconLeftParams);
			mIconLeftView.setImageDrawable(iconLeft);
			addView(mIconLeftView);
		}

		// key View
		keyParams = new LayoutParams((int) keyWidth, (int) keyHeight, keyWeight);
		mKeyView.setText(key);
		mKeyView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, px2dip(keySize));
		mKeyView.setTextColor(keyColor);
		mKeyView.setSingleLine(keySingleLine);
		mKeyView.setLayoutParams(keyParams);
		mKeyView.setTypeface(mKeyView.getTypeface(), keyStyle);
		addView(mKeyView);

		// value View
		if (value != null) {
			valueParams = new LayoutParams((int) valueWidth, (int) valueHeight, valueWeight);
			mValueView.setText(value);
			mValueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, px2dip(valueSize));
			mValueView.setTextColor(valueColor);
			mValueView.setSingleLine(valueSingleLine);
			mValueView.setLayoutParams(valueParams);
			mValueView.setTypeface(mValueView.getTypeface(), valueStyle);
			mValueView.setGravity(valueGravity.value);

			addView(mValueView);
		}

		// icon right
		if (iconRight != null) {
			iconRightParams = new LayoutParams((int) iconRightWidth, (int) iconRightHeight);
			iconRightParams.setMargins((int) iconRightMarginLeft, (int) iconRightMarginTop, (int) iconRightMarginRight, (int) iconRightMarginBottom);
			mIconRightView.setLayoutParams(iconRightParams);
			mIconRightView.setImageDrawable(iconRight);
			addView(mIconRightView);
		}

		// 加入layout
		initDivider(context);
	}

	/**
	 * 如果设置此值，则设置以下将无效：
	 * dividerTopMargin、
	 * dividerTopMarginLeft、
	 * dividerTopMarginRight、
	 *
	 * dividerBottomMargin、
	 * dividerBottomMarginLeft、
	 * dividerBottomMarginRight。
	 */
	private float dividerMargin = 0; // px

	/**
	 * 如果设置此值，则设置以下将无效：
	 * dividerTopMarginLeft、
	 * dividerTopMarginRight、
	 */
	private float dividerTopMargin = 0; // px
	private float dividerTopMarginLeft = 0; // px
	private float dividerTopMarginRight = 0; // px

	/**
	 * 如果设置此值，则设置以下将无效：
	 * dividerBottomMarginLeft、
	 * dividerBottomMarginRight、
	 */
	private float dividerBottomMargin = 0; // px
	private float dividerBottomMarginLeft = 0; // px
	private float dividerBottomMarginRight = 0; // px

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
		switch (dividerMode) {
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
			dividerTopMargin = dividerTopMarginLeft = dividerTopMarginRight = dividerMargin;
			dividerBottomMargin = dividerBottomMarginLeft = dividerBottomMarginRight = dividerMargin;
		} else if(dividerTopMargin > 0){
			dividerTopMarginLeft = dividerTopMarginRight = dividerTopMargin;
		} else if(dividerBottomMargin > 0){
			dividerBottomMarginLeft = dividerBottomMarginRight = dividerBottomMargin;
		}
	}

	/**
	 * 画上线
	 * @param canvas
	 */
	private void drawDividerTop(Canvas canvas) {
		int measuredWidth = getMeasuredWidth();
		float topStartX = 0 + dividerTopMarginLeft;
		float topStopX = measuredWidth - dividerTopMarginRight;
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
		float bottomStartX = 0 + dividerBottomMarginLeft;
		float bottomStopX = measuredWidth - dividerBottomMarginRight;
		float bottomStartY = measuredHeight - dividerSize/2;
		float bottomStopY = bottomStartY;
		canvas.drawLine(bottomStartX, bottomStartY, bottomStopX, bottomStopY, dividerPaint); // 下
	}

	/**
	 * 获取key的View
	 * @return key View
	 */
	public TextView getKeyView(){
		return mKeyView;
	}

	/**
	 * 获取value的View
	 * @return value View
	 */
	public TextView getValueView(){
		return mValueView;
	}

	/**
	 * 设置Key文本资源ID
	 * @param resId 文本资源ID
	 */
	public void setKey(int resId){
		mKeyView.setText(getResources().getString(resId));
	}

	/**
	 * 设置Key文本
	 * @param text 文本
	 */
	public void setKey(String text){
		mKeyView.setText(text);
	}

	/**
	 * 设置Key文字
	 * @param text 文本
	 */
	public void setKey(CharSequence text){
		mKeyView.setText(text);
	}

	/**
	 * 设置Key文字大小
	 * @param size 文本大小
	 */
	public void setKeySize(float size){
		mKeyView.setTextSize(size);
	}

	/**
	 * 设置key文本大小（px）
	 * @param unit 单位 {@link TypedValue}
	 * @param size 文本大小
	 */
	public void setKeySize(int unit, float size) {
		mKeyView.setTextSize(unit, size);
	}

	/**
	 * 设置Key文字颜色
	 * @param color 文本颜色
	 */
	public void setKeyColor(int color){
		mKeyView.setTextColor(color);
	}

	/**
	 * 设置Key文字颜色字符
	 * @param color 文本颜色 如#FFFFFF
	 */
	public void setKeyColor(String color){
		try {
			mKeyView.setTextColor(Color.parseColor(color));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置value的文本ID
	 * @param resId 文本资源ID
	 */
	public void setValue(int resId){
		mValueView.setText(getResources().getString(resId));
	}

	/**
	 * 设置value的文本
	 * @param text 文本
	 */
	public void setValue(String text){
		mValueView.setText(text);
	}

	/**
	 * 设置value的文本
	 * @param text 文本
	 */
	public void setValue(CharSequence text){
		mValueView.setText(text);
	}

	/**
	 * 设置value文本大小（px）
	 * @param size 文本大小
	 */
	public void setValueSize(float size){
		mValueView.setTextSize(size);
	}

	/**
	 * 设置value文本大小
	 * @param unit 单位 {@link TypedValue}
	 * @param size 文本大小.
	 */
	public void setValueSize(int unit, float size) {
		mValueView.setTextSize(unit, size);
	}

	/**
	 * 设置value文本颜色
	 * @param color
	 */
	public void setValueColor(int color){
		mValueView.setTextColor(color);
	}

	/**
	 * 设置value文本颜色
	 * @param color 如："#000000"
	 */
	public void setValueColor(String color){
		try {
			mValueView.setTextColor(Color.parseColor(color));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setKeyBoldText(boolean fakeBoldText){
		mKeyView.getPaint().setFakeBoldText(fakeBoldText);
	}

	public void setValueBoldText(boolean fakeBoldText){
		mValueView.getPaint().setFakeBoldText(fakeBoldText);
	}

	public void setValueGravity(int gravity){
		mValueView.setGravity(gravity);
	}

	/**
	 * 设置key和value的文本资源ID
	 * @param keyResId key资源ID
	 * @param valueResId value资源ID
	 */
	public void setKeyValue(int keyResId, int valueResId){
		setKey(keyResId);
		setValue(valueResId);
	}

	/**
	 * 设置key和value的文本
	 * @param key key文本
	 * @param value value文本
	 */
	public void setKeyValue(String key, String value){
		setKey(key);
		setValue(value);
	}

	/**
	 * 设置key和value的文本
	 * @param key key文本
	 * @param value value文本
	 */
	public void setKeyValue(CharSequence key, CharSequence value){
		setKey(key);
		setValue(value);
	}

	/**
	 * 设置左边icon的margins
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setIconLeftMargins(int left, int top, int right, int bottom){
		iconLeftParams.setMargins(left, top, right, bottom);
	}

	/**
	 * 设置右边icon的margins
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setIconRightMargins(int left, int top, int right, int bottom){
		iconRightParams.setMargins(left, top, right, bottom);
	}

	/**
	 * 设置左边图标ID
	 * @param resid 左边图标ID
	 */
	public void setIconLeftImageResource(int resid){
		mIconLeftView.setBackgroundResource(resid);
	}

	/**
	 * 设置左边图标Drawable
	 * @param drawable 左边图标Drawable
	 */
	@SuppressWarnings("deprecation")
	public void setIconLeftImageResource(Drawable drawable){
		mIconLeftView.setBackgroundDrawable(drawable);
	}

	/**
	 * 设置右边图标id
	 * @param resid 右边图标ID
	 */
	public void setIconRightImageResource(int resid){
		mIconRightView.setBackgroundResource(resid);
	}

	/**
	 * 设置右边图标Drawable
	 * @param drawable 右边图标Drawable
	 */
	@SuppressWarnings("deprecation")
	public void setIconRightImageDrawable(Drawable drawable){
		mIconRightView.setBackgroundDrawable(drawable);
	}

	public void setDividerMargin(float dividerMargin) {
		this.dividerMargin = dividerMargin;
		invalidate();
	}

	public void setDividerTopMargin(float dividerTopMargin) {
		this.dividerTopMargin = dividerTopMargin;
		invalidate();
	}

	public void setDividerTopMarginLeft(float dividerTopMarginLeft) {
		this.dividerTopMarginLeft = dividerTopMarginLeft;
		invalidate();
	}

	public void setDividerTopMarginRight(float dividerTopMarginRight) {
		this.dividerTopMarginRight = dividerTopMarginRight;
		invalidate();
	}

	public void setDividerBottomMargin(float dividerBottomMargin) {
		this.dividerBottomMargin = dividerBottomMargin;
		invalidate();
	}

	public void setDividerBottomMarginLeft(float dividerBottomMarginLeft) {
		this.dividerBottomMarginLeft = dividerBottomMarginLeft;
		invalidate();
	}

	public void setDividerBottomMarginRight(float dividerBottomMarginRight) {
		this.dividerBottomMarginRight = dividerBottomMarginRight;
		invalidate();
	}

	/**
	 * dip转换为px
	 * @param dipValue dip值
	 * @return xp值
	 */
	public float dip2px(float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转换为dip
	 * @param pxValue px值
	 * @return dip值
	 */
	public float px2dip(float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
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

	/**
	 * 对齐方式
	 * 
	 * <p>Created by wuguangxin on 2017/8/18.</p>
	 */
	public enum GravityMode {
		/** 左对齐 */
		left(Gravity.LEFT),
		/** 上对齐 */
		top(Gravity.TOP),
		/** 右对齐 */
		right(Gravity.RIGHT),
		/** 下对齐 */
		bottom(Gravity.BOTTOM),
		/** 居中 */
		center(Gravity.CENTER),
		/** 水平居中 */
		center_horizontal(Gravity.CENTER_HORIZONTAL),
		/** 垂直居中 */
		center_vertical(Gravity.CENTER_VERTICAL);

		public int value;

		GravityMode(int value){
			this.value = value;
		}

		public static GravityMode fromValue(int value){
			for (GravityMode position: GravityMode.values()) {
				if (position.value == value) {
					return position;
				}
			}
			return null;
		}
	}
}
