package com.wuguangxin.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
 * Item项使用的View（LinearLayout+TextView的组合控件，水平排列 key-value）
 *
 * <p>Created by wuguangxin on 15/7/10 </p>
 */
public class ItemView extends LinearLayout{
	private LayoutParams iconLeftParams;
	private LayoutParams iconRightParams;
	private ImageView mIconLeftView;
	private ImageView mIconRightView;
	private TextView mKeyView;
	private TextView mValueView;

	private int width;
	private int height;

	// 指示器
	private Drawable divider;  							// 线条Drawable
	private DividerMode dividerMode = DividerMode.None;	// 线条模式
	private int dividerSize;							// 线条大小
	// 左图标
	private Drawable iconLeft;
	private int iconLeftWidth = LayoutParams.WRAP_CONTENT;	// 左边图标默认宽(DIP)
	private int iconLeftHeight = LayoutParams.WRAP_CONTENT;	// 左边图标默认高(DIP)
	private int iconLeftMarginLeft; 		// 左边图标外边距右(DIP)
	private int iconLeftMarginTop;			// 左边图标外边距上(DIP)
	private int iconLeftMarginRight = 10;	// 左边图标外边距右(DIP)
	private int iconLeftMarginBottom;		// 左边图标外边距下(DIP)
	// 右图标
	private Drawable iconRight;
	private int iconRightWidth = LayoutParams.WRAP_CONTENT;	// 右边图标外边距(DIP)
	private int iconRightHeight = LayoutParams.WRAP_CONTENT;// 右边图标默认高(DIP)
	private int iconRightMarginLeft = 5;	// 右边图标外边距左(DIP)
	private int iconRightMarginTop;			// 右边图标外边距上(DIP)
	private int iconRightMarginRight;		// 右边图标外边距右(DIP)
	private int iconRightMarginBottom;		// 右边图标外边距下(DIP)

	// key
	private String key;
	private int keyColor = Color.BLACK;				// key文本颜色
	private int keySize = 14;						// key文本大小
	private int keyStyle = 0; 						// 0正常，1粗体
	private int keyLineSpacingExtra; // 行间距值（不是倍数）
	private boolean keySingleLine;					// 是否只显示一行
	// key-hint
	private String keyHint;
	private int keyHintColor = Color.LTGRAY;		// key-hint文本颜色
	// key-width-height
	private float keyWeight = 0;							// key权重
	private int keyWidth = LayoutParams.WRAP_CONTENT;		// key宽
	private int keyHeight = LayoutParams.WRAP_CONTENT;		// key高
	// key-margin
	private int keyMargin = -1; 			// 当 keyMargin >= 0 时，左上右下的值等于 keyMargin
	private int keyMarginLeft;				// keyView距左
	private int keyMarginTop;				// keyView距上
	private int keyMarginRight;				// keyView距右
	private int keyMarginBottom;			// keyView距下
	// key-padding
	private int keyPadding = -1; 			// 当 keyPadding >= 0 时，左上右下的值等于 keyPadding
	private int keyPaddingLeft;				// key距离keyView的左
	private int keyPaddingTop;				// key距离keyView的上
	private int keyPaddingRight;			// key距离keyView的右
	private int keyPaddingBottom;			// key距离keyView的下
	// key-drawable
	private int keyDrawablePadding;			// 图标与内容的间距
	private Drawable keyDrawableLeft;		// keyView的drawableLeft
	private Drawable keyDrawableRight;		// keyView的drawableRight
	private Drawable keyBackground;  		// keyView的背景
	private GravityMode keyGravity = GravityMode.left;	// keyView文本对齐方式

	// value
	private String value;
	private int valueColor = Color.BLACK;
	private int valueSize = 14;
	private int valueStyle = 0; // 0正常，1粗体
	private int valueLineSpacingExtra; // 行间距值（不是倍数）
	private boolean valueSingleLine;
	// value-hint
	private String valueHint;
	private int valueHintColor = Color.LTGRAY;
	// value-width-height
	private float valueWeight;
	private int valueWidth = LayoutParams.WRAP_CONTENT;
	private int valueHeight = LayoutParams.WRAP_CONTENT;
	// value-margin
	private int valueMargin = -1; // 当 valueMargin >= 0 时，左上右下的值等于 valueMargin
	private int valueMarginLeft;
	private int valueMarginTop;
	private int valueMarginRight;
	private int valueMarginBottom;
	// value-padding
	private int valuePadding = -1; // 当 valuePadding >= 0 时，左上右下的值等于 valuePadding
	private int valuePaddingLeft;
	private int valuePaddingTop;
	private int valuePaddingRight;
	private int valuePaddingBottom;
	// value-drawable
	private int valueDrawablePadding;
	private Drawable valueDrawableLeft;
	private Drawable valueDrawableRight;
	private Drawable valueBackground;  // value的背景
	private GravityMode valueGravity = GravityMode.left;

	private int dividerTopMarginLeft; // px
	private int dividerTopMarginRight; // px
	private int dividerBottomMarginLeft = 0; // px
	private int dividerBottomMarginRight = 0; // px


	public ItemView(Context context) {
		this(context, null);
	}

	public ItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 不设置颜色，就看不到线条，待解决（暂时使用透明色）
		setBackgroundColor(Color.TRANSPARENT);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
		if (a != null) {
			// 线
			divider = a.getDrawable(R.styleable.ItemView_divider);
			dividerMode = DividerMode.fromValue(a.getInteger(R.styleable.ItemView_dividerMode, dividerMode.value));
			dividerTopMarginLeft = a.getDimensionPixelSize(R.styleable.ItemView_dividerTop_marginLeft, dividerTopMarginLeft);
			dividerTopMarginRight = a.getDimensionPixelSize(R.styleable.ItemView_dividerTop_marginRight, dividerTopMarginRight);
			dividerBottomMarginLeft = a.getDimensionPixelSize(R.styleable.ItemView_dividerBottom_marginLeft, dividerBottomMarginLeft);
			dividerBottomMarginRight = a.getDimensionPixelSize(R.styleable.ItemView_dividerBottom_marginRight, dividerBottomMarginRight);

			// 左图标
			iconLeft = a.getDrawable(R.styleable.ItemView_iconLeft);
			iconLeftWidth = a.getDimensionPixelSize(R.styleable.ItemView_iconRight_width, iconLeftWidth);
			iconLeftHeight = a.getDimensionPixelSize(R.styleable.ItemView_iconLeft_height, iconLeftHeight);
			iconLeftMarginLeft = a.getDimensionPixelSize(R.styleable.ItemView_iconLeft_marginLeft, iconLeftMarginLeft);
			iconLeftMarginTop = a.getDimensionPixelSize(R.styleable.ItemView_iconLeft_marginTop, iconLeftMarginTop);
			iconLeftMarginRight = a.getDimensionPixelSize(R.styleable.ItemView_iconLeft_marginRight, iconLeftMarginRight);
			iconLeftMarginBottom = a.getDimensionPixelSize(R.styleable.ItemView_iconLeft_marginBottom, iconLeftMarginBottom);

			// 右图标
			iconRight = a.getDrawable(R.styleable.ItemView_iconRight);
			iconRightWidth = a.getDimensionPixelSize(R.styleable.ItemView_iconRight_width, iconRightWidth);
			iconRightHeight = a.getDimensionPixelSize(R.styleable.ItemView_iconRight_height, iconRightHeight);
			iconRightMarginLeft = a.getDimensionPixelSize(R.styleable.ItemView_iconRight_marginLeft, iconRightMarginLeft);
			iconRightMarginTop = a.getDimensionPixelSize(R.styleable.ItemView_iconRight_marginTop, iconRightMarginTop);
			iconRightMarginRight = a.getDimensionPixelSize(R.styleable.ItemView_iconRight_marginRight, iconRightMarginRight);
			iconRightMarginBottom = a.getDimensionPixelSize(R.styleable.ItemView_iconRight_marginBottom, iconRightMarginBottom);

			// key
			key = a.getString(R.styleable.ItemView_key);
			keySize = a.getDimensionPixelSize(R.styleable.ItemView_keySize, keySize);
			keyColor = a.getColor(R.styleable.ItemView_keyColor, keyColor);
			keyStyle = a.getInt(R.styleable.ItemView_keyStyle, keyStyle);
			keyLineSpacingExtra = a.getDimensionPixelSize(R.styleable.ItemView_keyLineSpacingExtra, keyLineSpacingExtra);
			keySingleLine = a.getBoolean(R.styleable.ItemView_keySingleLines, keySingleLine);
			keyBackground = a.getDrawable(R.styleable.ItemView_keyBackground);
			keyGravity = GravityMode.fromValue(a.getInteger(R.styleable.ItemView_keyGravity, keyGravity.value));
			// key-hint
			keyHint = a.getString(R.styleable.ItemView_keyHint);
			keyHintColor = a.getColor(R.styleable.ItemView_keyHintColor, keyHintColor);
			// key-width-height
			keyWeight = a.getFloat(R.styleable.ItemView_keyWeight, keyWeight);
			keyWidth = a.getDimensionPixelSize(R.styleable.ItemView_keyWidth, keyWidth);
			keyHeight = a.getDimensionPixelSize(R.styleable.ItemView_keyHeight, keyHeight);
			// key-margin
			keyMargin = a.getDimensionPixelSize(R.styleable.ItemView_keyMargin, keyMargin);
			keyMarginLeft = a.getDimensionPixelSize(R.styleable.ItemView_keyMarginLeft, keyMarginLeft);
			keyMarginTop = a.getDimensionPixelSize(R.styleable.ItemView_keyMarginTop, keyMarginTop);
			keyMarginRight = a.getDimensionPixelSize(R.styleable.ItemView_keyMarginRight, keyMarginRight);
			keyMarginBottom = a.getDimensionPixelSize(R.styleable.ItemView_keyMarginBottom, keyMarginBottom);
			// key-padding
			keyPadding = a.getDimensionPixelSize(R.styleable.ItemView_keyPadding, keyPadding);
			keyPaddingLeft = a.getDimensionPixelSize(R.styleable.ItemView_keyPaddingLeft, keyPaddingLeft);
			keyPaddingTop = a.getDimensionPixelSize(R.styleable.ItemView_keyPaddingTop, keyPaddingTop);
			keyPaddingRight = a.getDimensionPixelSize(R.styleable.ItemView_keyPaddingRight, keyPaddingRight);
			keyPaddingBottom = a.getDimensionPixelSize(R.styleable.ItemView_keyPaddingBottom, keyPaddingBottom);
			// key-drawable
			keyDrawablePadding = a.getDimensionPixelSize(R.styleable.ItemView_keyDrawablePadding, keyDrawablePadding);
			keyDrawableLeft = a.getDrawable(R.styleable.ItemView_keyDrawableLeft);
			keyDrawableRight = a.getDrawable(R.styleable.ItemView_keyDrawableRight);

			// value
			value = a.getString(R.styleable.ItemView_value);
			if (value != null) {
				valueSize = a.getDimensionPixelSize(R.styleable.ItemView_valueSize, valueSize);
				valueColor = a.getColor(R.styleable.ItemView_valueColor, valueColor);
				valueStyle = a.getInt(R.styleable.ItemView_valueStyle, valueStyle);
				valueSingleLine = a.getBoolean(R.styleable.ItemView_valueSingleLine, valueSingleLine);
				valueLineSpacingExtra = a.getDimensionPixelSize(R.styleable.ItemView_valueLineSpacingExtra, valueLineSpacingExtra);
				valueBackground = a.getDrawable(R.styleable.ItemView_valueBackground);
				valueGravity = GravityMode.fromValue(a.getInteger(R.styleable.ItemView_valueGravity, valueGravity.value));
				// value-hint
				valueHint = a.getString(R.styleable.ItemView_valueHint);
				valueHintColor = a.getColor(R.styleable.ItemView_valueHintColor, valueHintColor);
				// value-width-height
				valueWeight = a.getFloat(R.styleable.ItemView_valueWeight, valueWeight);
				valueWidth = a.getDimensionPixelSize(R.styleable.ItemView_valueWidth, valueWidth);
				valueHeight = a.getDimensionPixelSize(R.styleable.ItemView_valueHeight, valueHeight);
				// value-margin
				valueMargin = a.getDimensionPixelSize(R.styleable.ItemView_valueMargin, valueMargin);
				valueMarginLeft = a.getDimensionPixelSize(R.styleable.ItemView_valueMarginLeft, valueMarginLeft);
				valueMarginTop = a.getDimensionPixelSize(R.styleable.ItemView_valueMarginTop, valueMarginTop);
				valueMarginRight = a.getDimensionPixelSize(R.styleable.ItemView_valueMarginRight, valueMarginRight);
				valueMarginBottom = a.getDimensionPixelSize(R.styleable.ItemView_valueMarginBottom, valueMarginBottom);
				// value-padding
				valuePadding = a.getDimensionPixelSize(R.styleable.ItemView_valuePadding, valuePadding);
				valuePaddingLeft = a.getDimensionPixelSize(R.styleable.ItemView_valuePaddingLeft, valuePaddingLeft);
				valuePaddingTop = a.getDimensionPixelSize(R.styleable.ItemView_valuePaddingTop, valuePaddingTop);
				valuePaddingRight = a.getDimensionPixelSize(R.styleable.ItemView_valuePaddingRight, valuePaddingRight);
				valuePaddingBottom = a.getDimensionPixelSize(R.styleable.ItemView_valuePaddingBottom, valuePaddingBottom);
			}
			// value-drawable
			valueDrawablePadding = a.getDimensionPixelSize(R.styleable.ItemView_valueDrawablePadding, valueDrawablePadding);
			valueDrawableLeft = a.getDrawable(R.styleable.ItemView_valueDrawableLeft);
			valueDrawableRight = a.getDrawable(R.styleable.ItemView_valueDrawableRight);

			a.recycle();
		}

		dividerSize = divider != null ? divider.getIntrinsicHeight() : 0;

		mIconLeftView = new ImageView(context);
		mIconRightView = new ImageView(context);
		mKeyView = new TextView(context);
		mValueView = new TextView(context);
		// icon left
		if (iconLeft != null) {
			iconLeftParams = new LayoutParams(iconLeftWidth, iconLeftHeight);
			iconLeftParams.leftMargin = iconLeftMarginLeft;
			iconLeftParams.topMargin = iconLeftMarginTop;
			iconLeftParams.rightMargin = iconLeftMarginRight;
			iconLeftParams.bottomMargin = iconLeftMarginBottom;
			mIconLeftView.setLayoutParams(iconLeftParams);
			mIconLeftView.setImageDrawable(iconLeft);
			addView(mIconLeftView);
		}

		// key View
		if(keyWeight > 0) keyWidth = LayoutParams.WRAP_CONTENT;
		LayoutParams keyParams = new LayoutParams(keyWidth, keyHeight, keyWeight);
		if(keyMargin != -1) keyMarginLeft = keyMarginTop = keyMarginRight = keyMarginBottom = keyMargin;
		keyParams.leftMargin = keyMarginLeft;
		keyParams.topMargin = keyMarginTop;
		keyParams.rightMargin = keyMarginRight;
		keyParams.bottomMargin = keyMarginBottom;
		mKeyView.setLayoutParams(keyParams);
		mKeyView.setGravity(keyGravity.value);
		mKeyView.setBackgroundDrawable(keyBackground);

		if(keyPadding != -1) keyPaddingLeft = keyPaddingTop = keyPaddingRight = keyPaddingBottom = keyPadding;
		mKeyView.setPadding(keyPaddingLeft, keyPaddingTop, keyPaddingRight, keyPaddingBottom);
		mKeyView.setText(key);
		mKeyView.setTextColor(keyColor);
		mKeyView.getPaint().setFakeBoldText(keyStyle == 1);
		mKeyView.getPaint().setTextSize(keySize);

		mKeyView.setLineSpacing(keyLineSpacingExtra, 1);
		mKeyView.setSingleLine(keySingleLine);
		mKeyView.setHint(keyHint);
		mKeyView.setHintTextColor(keyHintColor);

		// 设置Key的图标
		setKeyDrawablesPadding(keyDrawablePadding);
		setKeyDrawablesLeft(keyDrawableLeft);
		setKeyDrawablesRight(keyDrawableRight);

		addView(mKeyView);

		// value View
		if (value != null) {
			if(valueWeight > 0) valueWidth = LayoutParams.WRAP_CONTENT;
			LayoutParams valueParams = new LayoutParams(valueWidth, valueHeight, valueWeight);
			if(valueMargin != -1) {
				valueMarginLeft = valueMarginTop = valueMarginRight = valueMarginBottom = valueMargin;
			}
			valueParams.leftMargin = valueMarginLeft;
			valueParams.topMargin = valueMarginTop;
			valueParams.rightMargin = valueMarginRight;
			valueParams.bottomMargin = valueMarginBottom;
			mValueView.setLayoutParams(valueParams);

			if(valuePadding != -1) {
				valuePaddingLeft = valuePaddingTop = valuePaddingRight = valuePaddingBottom = valuePadding;
			}
			mValueView.setPadding(valuePaddingLeft, valuePaddingTop, valuePaddingRight, valuePaddingBottom);

			mValueView.setText(value);
			mValueView.setTextColor(valueColor);
			mValueView.getPaint().setFakeBoldText(valueStyle == 1);
			mValueView.getPaint().setTextSize(valueSize);
			mValueView.setGravity(valueGravity.value);
			mValueView.setBackgroundDrawable(valueBackground);

			mValueView.setLineSpacing(valueLineSpacingExtra, 1);
			mValueView.setSingleLine(valueSingleLine);
			mValueView.setHint(valueHint);
			mValueView.setHintTextColor(valueHintColor);

			// 设置Value的图标
			setValueDrawablesPadding(valueDrawablePadding);
			setValueDrawablesLeft(valueDrawableLeft);
			setValueDrawablesRight(valueDrawableRight);

			addView(mValueView);
		}

		// icon right
		if (iconRight != null) {
			iconRightParams = new LayoutParams(iconRightWidth, iconRightHeight);
			iconRightParams.setMargins(iconRightMarginLeft, iconRightMarginTop, iconRightMarginRight, iconRightMarginBottom);
			mIconRightView.setLayoutParams(iconRightParams);
			mIconRightView.setImageDrawable(iconRight);
			addView(mIconRightView);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		if (dividerMode == DividerMode.None) {
			return;
		}

		switch (dividerMode) {
		case None:
			break;
		case SingleTop:
			drawDividerTop(canvas);		// 上
			break;
		case SingleBottom:
			drawDividerBottom(canvas); 	// 下
			break;
		case Both:
			drawDividerTop(canvas);		// 上
			drawDividerBottom(canvas); 	// 下
			break;
		default:
			break;
		}
	}

	/**
	 * 画上线
	 * @param canvas
	 */
	private void drawDividerTop(Canvas canvas) {
		if(divider != null){
			divider.setBounds(dividerTopMarginLeft, 0, width - dividerTopMarginRight, dividerSize);
			divider.draw(canvas);
		}
	}

	/**
	 * 画下线
	 * @param canvas
	 */
	private void drawDividerBottom(Canvas canvas) {
		if(divider != null){
			divider.setBounds(dividerBottomMarginLeft, height - dividerSize, width - dividerBottomMarginRight, width);
			divider.draw(canvas);
		}
	}

	@Override
	final public void setOrientation(int orientation) {
		if (orientation == VERTICAL) {
			throw new SecurityException("只能设置为水平排列：HORIZONTAL");
		}
		super.setOrientation(LinearLayout.HORIZONTAL);
	}

	/**
	 * 获取ItemView的key文本
	 * @return
	 */
	public String getKey(){
		this.key = mKeyView.getText().toString();
		return this.key;
	}

	/**
	 * 获取ItemView的value文本
	 * @return
	 */
	public String getValue(){
		this.value = mValueView.getText().toString();
		return this.value;
	}

	//	key
	public void setKeyDrawablesPadding(int drawablePadding) {
		this.keyDrawablePadding = drawablePadding;
		mKeyView.setCompoundDrawablePadding(keyDrawablePadding);
	}

	public void setKeyDrawablesLeft(Drawable drawablesLeft) {
		this.keyDrawableLeft = drawablesLeft;
		if (keyDrawableLeft != null) {
			keyDrawableLeft.setBounds(0, 0, keyDrawableLeft.getIntrinsicWidth(), keyDrawableLeft.getIntrinsicHeight());
		}
		mKeyView.setCompoundDrawablesWithIntrinsicBounds(keyDrawableLeft, null, keyDrawableRight, null);
	}

	public void setKeyDrawablesRight(Drawable drawablesRight) {
		this.keyDrawableRight = drawablesRight;
		if (keyDrawableRight != null) {
			keyDrawableRight.setBounds(0, 0, keyDrawableRight.getIntrinsicWidth(), keyDrawableRight.getIntrinsicHeight());
		}
		mKeyView.setCompoundDrawablesWithIntrinsicBounds(keyDrawableLeft, null, keyDrawableRight, null);
	}

	//	value
	public void setValueDrawablesPadding(int drawablePadding) {
		this.valueDrawablePadding = drawablePadding;
		mValueView.setCompoundDrawablePadding(valueDrawablePadding);
	}

	public void setValueDrawablesLeft(Drawable drawablesLeft) {
		this.valueDrawableLeft = drawablesLeft;
		if (valueDrawableLeft != null) {
			valueDrawableLeft.setBounds(0, 0, valueDrawableLeft.getIntrinsicWidth(), valueDrawableLeft.getIntrinsicHeight());
		}
		mValueView.setCompoundDrawablesWithIntrinsicBounds(valueDrawableLeft, null, valueDrawableRight, null);
	}

	public void setValueDrawablesRight(Drawable drawablesRight) {
		this.valueDrawableRight= drawablesRight;
		if (valueDrawableRight != null) {
			valueDrawableRight.setBounds(0, 0, valueDrawableRight.getIntrinsicWidth(), valueDrawableRight.getIntrinsicWidth());
		}
		mValueView.setCompoundDrawablesWithIntrinsicBounds(null, null, valueDrawableRight, null);
	}

	public void setKeyOnClickListener(OnClickListener keyOnClickListener) {
		if (mKeyView != null) {
			mKeyView.setOnClickListener(keyOnClickListener);
		}
	}

	public void setValueOnClickListener(OnClickListener valueOnClickListener) {
		if (mValueView != null) {
			mValueView.setOnClickListener(valueOnClickListener);
		}
	}

	// =============================== getter start=================================================

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
	 * 获取左边的图标ImageView
	 * @return
	 */
	public ImageView getIconLeftView() {
		return mIconLeftView;
	}

	/**
	 * 获取右边的图标ImageView
	 * @return
	 */
	public ImageView getIconRightView() {
		return mIconRightView;
	}

	// =============================== 对key、value的操作 start======================================
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

	// =============================== 对左边图标的操作 start=========================================
	/**
	 * 设置左边icon的margins
	 * @param left 左
	 * @param top 上
	 * @param right 右
	 * @param bottom 下
	 */
	public void setIconLeftMargins(int left, int top, int right, int bottom){
		iconLeftParams.setMargins(left, top, right, bottom);
	}

	/**
	 * 设置右边icon的margins
	 * @param left 左
	 * @param top 上
	 * @param right 右
	 * @param bottom 下
	 */
	public void setIconRightMargins(int left, int top, int right, int bottom){
		iconRightParams.setMargins(left, top, right, bottom);
	}

	/**
	 * 设置左边图标资源ID
	 * @param resId 资源ID
	 */
	public void setIconLeftImageResource(int resId){
		mIconLeftView.setImageResource(resId);
	}

	/**
	 * 设置左边图标Drawable
	 * @param drawable Drawable
	 */
	public void setIconLeftImageDrawable(Drawable drawable){
		mIconLeftView.setImageDrawable(drawable);
	}

	/**
	 * 设置左边图标 Bitmap
	 * @param bitmap Bitmap
	 */
	public void setIconLeftImageBitmap(Bitmap bitmap){
		mIconLeftView.setImageBitmap(bitmap);
	}

	/**
	 * 设置左边图标背景资源ID
	 * @param resId 资源ID
	 */
	public void setIconLeftBackgroundResource(int resId){
		mIconLeftView.setBackgroundResource(resId);
	}

	/**
	 * 设置左边图标背景Drawable
	 * @param drawable Drawable
	 */
	public void setIconLeftBackgroundDrawable(Drawable drawable){
		mIconLeftView.setBackgroundDrawable(drawable);
	}

	/**
	 * 设置左边图标背景Drawable
	 * @param drawable Drawable
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setIconLeftBackground(Drawable drawable){
		mIconLeftView.setBackground(drawable);
	}

	/**
	 * 设置左边图标背景颜色
	 * @param color 颜色值
	 */
	public void setIconLeftBackgroundColor(int color){
		mIconLeftView.setBackgroundColor(color);
	}

	// =============================== 对右边图标的操作 start=========================================
	/**
	 * 设置右边图标资源ID
	 * @param resId 图标资源ID
	 */
	public void setIconRightImageResource(int resId){
		mIconRightView.setImageResource(resId);
	}

	/**
	 * 设置右边图标Drawable
	 * @param drawable 右边图标Drawable
	 */
	public void setIconRightImageDrawable(Drawable drawable){
		mIconRightView.setImageDrawable(drawable);
	}

	/**
	 * 设置右边图标 Bitmap
	 * @param bitmap 图标 Bitmap
	 */
	public void setIconRightImageBitmap(Bitmap bitmap){
		mIconRightView.setImageBitmap(bitmap);
	}

	/**
	 * 设置右边图标背景资源ID
	 * @param resId 资源ID
	 */
	public void setIconRightBackgroundResource(int resId){
		mIconRightView.setBackgroundResource(resId);
	}

	/**
	 * 设置右边图标背景Drawable
	 * @param drawable Drawable
	 */
	public void setIconRightBackgroundDrawable(Drawable drawable){
		mIconRightView.setBackgroundDrawable(drawable);
	}

	/**
	 * 设置右边图标背景Drawable
	 * @param drawable Drawable
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setIconRightBackground(Drawable drawable){
		mIconRightView.setBackground(drawable);
	}

	/**
	 * 设置右边图标背景颜色
	 * @param color 颜色值
	 */
	public void setIconRightBackgroundColor(int color){
		mIconRightView.setBackgroundColor(color);
	}

	// =============================== 对分割线的操作 start===========================================

	/**
	 * 设置Top分割线MarginLeft
	 * @param dividerTopMarginLeft 值
	 */
	public void setDividerTopMarginLeft(int dividerTopMarginLeft) {
		this.dividerTopMarginLeft = dividerTopMarginLeft;
		invalidate();
	}

	/**
	 * 设置Top分割线MarginRight
	 * @param dividerTopMarginRight 值
	 */
	public void setDividerTopMarginRight(int dividerTopMarginRight) {
		this.dividerTopMarginRight = dividerTopMarginRight;
		invalidate();
	}

	/**
	 * 设置Bottom分割线MarginLeft
	 * @param dividerBottomMarginLeft 值
	 */
	public void setDividerBottomMarginLeft(int dividerBottomMarginLeft) {
		this.dividerBottomMarginLeft = dividerBottomMarginLeft;
		invalidate();
	}

	/**
	 * 设置Bottom分割线MarginRight
	 * @param dividerBottomMarginRight 值
	 */
	public void setDividerBottomMarginRight(int dividerBottomMarginRight) {
		this.dividerBottomMarginRight = dividerBottomMarginRight;
		invalidate();
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
			return left;
		}
	}

	/**
	 * dip转换为px
	 * @param context 上下文
	 * @param dipValue dip
	 * @return px
	 */
	public int dip2px(Context context, float dipValue){
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * density + 0.5f);
	}
}
