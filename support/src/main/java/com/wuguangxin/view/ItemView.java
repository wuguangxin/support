package com.wuguangxin.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
	private static final int DEFAULT_TEXT_SIZE = 14; // 默认key字体大小(DIP)
	private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#333333"); // 默认key字体颜色
	private static final int DEFAULT_VALUE_SIZE = 14; // 默认Value字体大小(DIP)
	private static final int DEFAULT_VALUE_COLOR = Color.parseColor("#333333"); // 默认Value字体颜色
	// ITEM距离左上右下内边距
	private static final float DEFAULT_PADDINGLEFT = 15; // 内容距左(DIP)
	private static final float DEFAULT_PADDINGTOP = 0;// 内容距上(DIP)
	private static final float DEFAULT_PADDINGRIGHT = 15;// 内容距右(DIP)
	private static final float DEFAULT_PADDINGBOTTOM = 0;// 内容距底(DIP)
	private static final int DEFAULT_LINEHEIGHT = 1; // 默认上下分割线大小
	private static final boolean DEFAULT_SINGLELINE = true; // 默认单行文本
	private static final DividerMode DEFAULT_DIVIDER = DividerMode.Both; // 默认分割线为上下都显示
	// 左边的图标
	private static final float DEFAULT_ICONLEFT_WIDTH = 30; // 左边图标默认宽(DIP)
	private static final float DEFAULT_ICONLEFT_HEIGHT = 30; // 左边图标默认高(DIP)
	private static final float DEFAULT_ICONLEFT_MARGIN_LEFT = 0; // 左边图标外边距左(DIP)
	private static final float DEFAULT_ICONLEFT_MARGIN_TOP = 0;// 左边图标外边距上(DIP)
	private static final float DEFAULT_ICONLEFT_MARGIN_RIGHT = 10; // 左边图标外边距右(DIP)
	private static final float DEFAULT_ICONLEFT_MARGIN_BOTTOM = 0;// 左边图标外边距下(DIP)
	// 右边的图标
	private static final float DEFAULT_ICONRIGHT_WIDTH = 18; // 右边图标外边距(DIP)
	private static final float DEFAULT_ICONRIGHT_HEIGHT = 18;// 右边图标默认高(DIP)
	private static final float DEFAULT_ICONRIGHT_MARGIN_LEFT = 5;// 右边图标外边距左(DIP)
	private static final float DEFAULT_ICONRIGHT_MARGIN_TOP = 0;// 右边图标外边距上(DIP)
	private static final float DEFAULT_ICONRIGHT_MARGIN_RIGHT = 0;// 右边图标外边距右(DIP)
	private static final float DEFAULT_ICONRIGHT_MARGIN_BOTTOM = 0;// 右边图标外边距下(DIP)
	// text和value组件的参数
	private LayoutParams textParams, valueParams;
	private LayoutParams iconLeftParams;
	private LayoutParams iconRightParams;
	private LinearLayout itemLayout;
	private ImageView lineTop;
	private ImageView lineBottom;
	private ImageView iconLeft;
	private ImageView iconRight;
	private TextView textView, valueView;
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

	@SuppressWarnings("deprecation")
	private void init(AttributeSet attrs, int defStyle){
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER_VERTICAL);
		// 获取属性方法2_1，使用attr.xml
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemView, defStyle, 0);
		// item背景
		Drawable itemBackground = a.getDrawable(R.styleable.ItemView_item_background);
		int item_paddingLeft = (int) a.getDimension(R.styleable.ItemView_item_paddingLeft, dip2px(DEFAULT_PADDINGLEFT));
		int item_paddingTop = (int) a.getDimension(R.styleable.ItemView_item_paddingTop, dip2px(DEFAULT_PADDINGTOP));
		int item_paddingRight = (int) a.getDimension(R.styleable.ItemView_item_paddingRight, dip2px(DEFAULT_PADDINGRIGHT));
		int item_paddingBottom = (int) a.getDimension(R.styleable.ItemView_item_paddingBottom, dip2px(DEFAULT_PADDINGBOTTOM));
		// 线
		int dividerColor = a.getColor(R.styleable.ItemView_item_dividerColor, Color.parseColor("#d1d1d1"));
		DividerMode mItemDivider = DividerMode.fromValue(a.getInteger(R.styleable.ItemView_item_divider, DEFAULT_DIVIDER.value));
		// 左图标
		Drawable drawableLeft = a.getDrawable(R.styleable.ItemView_item_iconLeft);
		int iconLeftWidth = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_width, dip2px(DEFAULT_ICONLEFT_WIDTH));
		int iconLeftHeight = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_height, dip2px(DEFAULT_ICONLEFT_HEIGHT));
		int iconLeftMarginLeft = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_margin_left, dip2px(DEFAULT_ICONLEFT_MARGIN_LEFT));
		int iconLeftMarginTop = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_margin_top, dip2px(DEFAULT_ICONLEFT_MARGIN_TOP));
		int iconLeftMarginRight = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_margin_right, dip2px(DEFAULT_ICONLEFT_MARGIN_RIGHT));
		int iconLeftMarginBottom = (int) a.getDimension(R.styleable.ItemView_item_iconLeft_margin_bottom, dip2px(DEFAULT_ICONLEFT_MARGIN_BOTTOM));
		// 右图标
		Drawable drawableRight = a.getDrawable(R.styleable.ItemView_item_iconRight);
		int iconRightWidth = (int) a.getDimension(R.styleable.ItemView_item_iconRight_width, dip2px(DEFAULT_ICONRIGHT_WIDTH));
		int iconRightHeight = (int) a.getDimension(R.styleable.ItemView_item_iconRight_height, dip2px(DEFAULT_ICONRIGHT_HEIGHT));
		int iconRightMarginLeft = (int) a.getDimension(R.styleable.ItemView_item_iconRight_margin_left, dip2px(DEFAULT_ICONRIGHT_MARGIN_LEFT));
		int iconRightMarginTop = (int) a.getDimension(R.styleable.ItemView_item_iconRight_margin_top, dip2px(DEFAULT_ICONRIGHT_MARGIN_TOP));
		int iconRightMarginRight = (int) a.getDimension(R.styleable.ItemView_item_iconRight_margin_right, dip2px(DEFAULT_ICONRIGHT_MARGIN_RIGHT));
		int iconRightMarginBottom = (int) a.getDimension(R.styleable.ItemView_item_iconRight_margin_bottom, dip2px(DEFAULT_ICONRIGHT_MARGIN_BOTTOM));
		// 文字-text
		String text = a.getString(R.styleable.ItemView_item_text);
		float textSize = a.getDimension(R.styleable.ItemView_item_textSize, dip2px(DEFAULT_TEXT_SIZE));
		int textColor = a.getColor(R.styleable.ItemView_item_textColor, DEFAULT_TEXT_COLOR);
		// 文字-value
		String value = a.getString(R.styleable.ItemView_item_value);
		float valueSize = a.getDimension(R.styleable.ItemView_item_valueSize, dip2px(DEFAULT_VALUE_SIZE));
		int valueColor = a.getColor(R.styleable.ItemView_item_valueColor, DEFAULT_VALUE_COLOR);
		// 
		boolean singleLine = a.getBoolean(R.styleable.ItemView_item_singleLine, DEFAULT_SINGLELINE);
		a.recycle();
		// 线
		LayoutParams LineParams = new LayoutParams(LayoutParams.MATCH_PARENT, DEFAULT_LINEHEIGHT);
		switch (mItemDivider) {
			case None:
				break;
			case SingleTop:
				lineTop = new ImageView(context);
				lineTop.setLayoutParams(LineParams);
				lineTop.setBackgroundColor(dividerColor);
				break;
			case SingleBottom:
				lineBottom = new ImageView(context);
				lineBottom.setLayoutParams(LineParams);
				lineBottom.setBackgroundColor(dividerColor);
				break;
			case Both:
				lineTop = new ImageView(context);
				lineTop.setLayoutParams(LineParams);
				lineTop.setBackgroundColor(dividerColor);
				lineBottom = new ImageView(context);
				lineBottom.setLayoutParams(LineParams);
				lineBottom.setBackgroundColor(dividerColor);
				break;
			default:
				break;
		}
		// item布局
		itemLayout = new LinearLayout(context);
		itemLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
		itemLayout.setGravity(Gravity.CENTER_VERTICAL);
		itemLayout.setOrientation(LinearLayout.HORIZONTAL);
		itemLayout.setPadding(item_paddingLeft, item_paddingTop, item_paddingRight, item_paddingBottom);
		if (itemBackground != null) {
			itemLayout.setBackgroundDrawable(itemBackground);
		}
		// item左边图标
		iconLeft = new ImageView(context);
		iconLeftParams = new LayoutParams(iconLeftWidth, iconLeftHeight);
		iconLeftParams.setMargins(iconLeftMarginLeft, iconLeftMarginTop, iconLeftMarginRight, iconLeftMarginBottom);
		iconLeft.setLayoutParams(iconLeftParams);
		if (drawableLeft != null) {
			iconLeft.setBackgroundDrawable(drawableLeft);
			iconLeft.setVisibility(View.VISIBLE);
		} else {
			iconLeft.setVisibility(View.GONE);
		}
		// item 右边图标
		iconRight = new ImageView(context);
		iconRightParams = new LayoutParams(iconRightWidth, iconRightHeight);
		iconRightParams.setMargins(iconRightMarginLeft, iconRightMarginTop, iconRightMarginRight, iconRightMarginBottom);
		iconRight.setLayoutParams(iconRightParams);
		if (drawableRight != null) {
			iconRight.setBackgroundDrawable(drawableRight);
			iconRight.setVisibility(View.VISIBLE);
		} else {
			iconRight.setVisibility(View.GONE);
		}
		// item中间key文字
		textView = new TextView(context);
		textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		textView.setText(text);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, px2dip(textSize));
		textView.setTextColor(textColor);
		textView.setSingleLine(singleLine);
		textView.setLayoutParams(textParams);
		// item中间value文字
		valueView = new TextView(context);
		valueParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		valueView.setText(value);
		valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, px2dip(valueSize));
		valueView.setTextColor(valueColor);
		valueView.setSingleLine(true);
		valueView.setLayoutParams(valueParams);
		// 加入中间layout
		itemLayout.removeAllViews();
		itemLayout.addView(iconLeft);
		itemLayout.addView(textView);
		itemLayout.addView(valueView);
		itemLayout.addView(iconRight);
		// 加入外层layout
		removeAllViews();
		switch (mItemDivider) {
			case None:
				addView(itemLayout);
				break;
			case SingleTop:
				addView(lineTop);
				addView(itemLayout);
				break;
			case SingleBottom:
				addView(itemLayout);
				addView(lineBottom);
				break;
			case Both:
				addView(lineTop);
				addView(itemLayout);
				addView(lineBottom);
				break;
			default:
				break;
		}
	}

	public void setText(int resId){
		textView.setText(getResources().getString(resId));
	}

	public void setText(String text){
		textView.setText(text);
	}

	public void setText(CharSequence text){
		textView.setText(text);
	}

	public TextView getTextView(){
		return textView;
	}

	public void setTextSize(float textSize){
		textView.setTextSize(textSize);
	}

	public void setTextColor(int color){
		textView.setTextColor(color);
	}

	public void setTextColor(String textColor){
		try {
			textView.setTextColor(Color.parseColor(textColor));
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
	 * 设置左边图标
	 * @param resid
	 */
	public void setIconLeftImageResource(int resid){
		iconLeft.setBackgroundResource(resid);
	}

	/**
	 * 设置左边图标
	 * @param drawable
	 */
	@SuppressWarnings("deprecation")
	public void setIconLeftImageResource(Drawable drawable){
		iconLeft.setBackgroundDrawable(drawable);
	}

	/**
	 * 设置右边图标
	 * @param resid
	 */
	public void setIconRightImageResource(int resid){
		iconRight.setBackgroundResource(resid);
	}

	/**
	 * 设置右边图标
	 * @param drawable
	 */
	@SuppressWarnings("deprecation")
	public void setIconRightImageDrawable(Drawable drawable){
		iconRight.setBackgroundDrawable(drawable);
	}

	/**
	 * dip转换为px
	 * @param dipValue
	 * @return
	 */
	public float dip2px(float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		float px = dipValue * scale + 0.5f;
		return px;
	}

	/**
	 * px转换为dip
	 * @param pxValue
	 * @return
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
	public static enum DividerMode {
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

		private DividerMode(int value){
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
