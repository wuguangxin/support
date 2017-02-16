package com.wuguangxin.view;

import com.wuguangxin.R;
import com.wuguangxin.utils.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KeyValueView extends LinearLayout{
//	private static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.ephwealth.financing";
	private static final float DEFAULT_TEXT_SIZE = 14.0f; // 默认文字尺寸
	private LayoutParams keyParams;
	private LayoutParams valueParams;
	private TextView keyView;
	private TextView valueView;
	// key
	private String key;
	private float keySize;
	private int keyColor;
	// value
	private String value;
	private float valueSize;
	private int valueColor;
	
	public KeyValueView(Context context){
		this(context, null);
	}

	public KeyValueView(Context context, AttributeSet attrs){
		super(context, attrs);
//		keyParams = new LinearLayout.LayoutParams(Utils.dip2px(context, 100), LayoutParams.WRAP_CONTENT);
		keyParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		valueParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		keyView = new TextView(context);
//		keyView.setTextSize(Utils.px2dip(context, getResources().getDimension(R.dimen.font_M)));
		keyView.setTextColor(Color.parseColor("#222222"));
		keyView.setGravity(Gravity.RIGHT);

		valueView = new TextView(context);
		valueView.setText(value);
		valueView.setTextColor(Color.parseColor("#444444"));
//		valueView.setTextSize(Utils.px2dip(context, getResources().getDimension(R.dimen.font_M)));
		valueView.setPadding(20, 0, 0, 0);
		setOrientation(LinearLayout.HORIZONTAL);
		
		// 测试
		// keyView.setBackgroundColor(Color.parseColor("#CCCCCC"));
		// valueView.setBackgroundColor(Color.parseColor("#999999"));
		
		// 获取属性方法1，不许要设置attr.xml，那么在xml中直接使用属性key。（如 key="姓名："   value="吴光新"）
//		int keyId = attrs.getAttributeResourceValue(NAME_SPACE , "key", -1);
//		int valueId = attrs.getAttributeResourceValue(NAME_SPACE, "value", -1);
//		if(keyId > 0){
//			key = context.getResources().getText(keyId).toString();
//		}
//		if(valueId > 0){
//			value = context.getResources().getText(valueId).toString();
//		}
		
		// 获取属性方法2_1，使用attr.xml
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyValueView);
		
		key = mTypedArray.getString(R.styleable.KeyValueView_keys);
		keySize = mTypedArray.getDimension(R.styleable.KeyValueView_keySize, -1);
		keyColor = mTypedArray.getColor(R.styleable.KeyValueView_keyColor, R.color.black);
		
		value = mTypedArray.getString(R.styleable.KeyValueView_values);
		valueSize = mTypedArray.getDimension(R.styleable.KeyValueView_valueSize, -1);
		valueColor = mTypedArray.getColor(R.styleable.KeyValueView_valueColor, R.color.black);
		
		// 处理文字大小
		if(keySize == -1){
			keySize = DEFAULT_TEXT_SIZE;
		} else {
			keySize = Utils.px2dip(context, keySize);
		}
		
		if(valueSize == -1){
			valueSize = DEFAULT_TEXT_SIZE;
		} else {
			valueSize = Utils.px2dip(context, valueSize);
		}
		
		// 获取属性方法2_2，使用attr.xml，当format="string"时，可以使用
//		key = attrs.getAttributeValue(NAME_SPACE, "key");
//		value = attrs.getAttributeValue(NAME_SPACE, "value");
		
		mTypedArray.recycle();
		
		setKey(key);
		setKeySize(keySize);
		setKeyColor(keyColor);
		
		setValue(value);
		setValueSize(valueSize);
		setValueColor(valueColor);
		
		init();
		
	}

	private void init(){
		removeAllViews();
		addView(keyView, keyParams);
		addView(valueView, valueParams);
	}

	public String getKey(){
		return key;
	}
	
	public void setKey(String key){
		if(key == null){
			key = "";
		}
		this.key = key;
		keyView.setText(key);
	}
	
	public void setKeySize(float keySize){
		if(keySize < 0){
			keySize = 0;
		}
		this.keySize = keySize;
		keyView.setTextSize(keySize);
	}
	
	public void setKeyColor(int keyColor){
		this.keyColor = keyColor;
		keyView.setTextColor(keyColor);
	}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(String value){
		if(value == null){
			value = "";
		}
		this.value = value;
		valueView.setText(value);
	}
	
	public void setValueSize(float valueSize){
		if(valueSize < 0){
			valueSize = 0;
		}
		this.valueSize = valueSize;
		valueView.setTextSize(valueSize);
	}
	
	public void setValueColor(int valueColor){
		this.valueColor = valueColor;
		valueView.setTextColor(valueColor);
	}

	public TextView getKeyView(){
		return keyView;
	}

	public TextView getValueView(){
		return valueView;
	}
}
