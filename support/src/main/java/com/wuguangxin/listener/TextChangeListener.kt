package com.wuguangxin.listener;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * EditText内容变化监听类，当文本框获得输入焦点并输入了内容时，显示你定义的清除内容的按钮，
 * 当失去输入焦点，则隐藏按钮，当点击清除按钮时，清空文本框的内容
 *
 * <p>Created by wuguangxin on 15/4/14 </p>
 */
public class TextChangeListener implements TextWatcher, OnClickListener, OnFocusChangeListener {
	private static final int DEF_PRECISION = 2;
	private int precision = DEF_PRECISION; // 小数点位数
	private EditText mEditText;
	private View mClearBtn;
	private EditTextCallBack callBack;
	/**
	 * 输入的数据类型
	 */
	private int textType;
	private StringBuffer stringBuffer;
	private int bankNumberSpace = 4;
	private Context context;
	private Animation fade_in;
	private Animation fade_out;
	private BigDecimal maxNumber = BigDecimal.valueOf(Double.MAX_VALUE); // 允许输入的最大数

	public TextChangeListener(){
		this(null);
	}

	/**
	 * @param editText 要监听的EditText
	 */
	public TextChangeListener(EditText editText){
		this(editText, 0);
	}

	/**
	 * @param editText 要监听的EditText
	 * @param clearViewId 清除按钮ViewId
	 */
	public TextChangeListener(EditText editText, int clearViewId){
		this(editText, findViewById(getActivityFromView(editText), clearViewId));
	}

	/**
	 * @param editText 要监听的EditText
	 * @param clearViewId 清除按钮ViewId
	 */
	public TextChangeListener(EditText editText, View clearViewId){
		this(editText, clearViewId, TextType.TEXT);
	}

	/**
	 * @param editText 要监听的EditText
	 * @param clearViewId 清除按钮ViewId
	 * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
	 */
	public TextChangeListener(EditText editText, int clearViewId, int textType){
		this(editText, findViewById(getActivityFromView(editText), clearViewId), textType, null);
	}

	/**
	 * @param editText 要监听的EditText
	 * @param clearBtn 清除按钮
	 * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
	 */
	public TextChangeListener(EditText editText, View clearBtn, int textType){
		this(editText, clearBtn, textType, null);
	}

	/**
	 * @param editText 要监听的EditText
	 * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
	 * @param callBack 回调
	 */
	public TextChangeListener(EditText editText, int textType, EditTextCallBack callBack){
		this(editText, null, textType, DEF_PRECISION, callBack);
	}

	/**
	 * @param editText 要监听的EditText
	 * @param clearBtn 清除按钮
	 * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
	 * @param callBack 回调
	 */
	public TextChangeListener(EditText editText, View clearBtn, int textType, EditTextCallBack callBack){
		this(editText, clearBtn, textType, DEF_PRECISION, callBack);
	}

	/**
	 * @param editText 要监听的EditText
	 * @param clearBtn 清除按钮
	 * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
	 * @param precision 小数点位数
	 */
	public TextChangeListener(EditText editText, View clearBtn, int textType, int precision){
		this(editText, clearBtn, textType, precision, null);
	}

	/**
	 * @param editText 要监听的EditText
	 * @param clearBtn 清除按钮
	 * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
	 * @param precision 小数位数最大长多
	 * @param callBack 回调
	 */
	public TextChangeListener(EditText editText, View clearBtn, int textType, int precision, EditTextCallBack callBack){
		this.context = getActivityFromView(editText);
		this.mEditText = editText;
		this.mClearBtn = clearBtn;
		this.textType = textType;
		this.precision = precision;
		this.callBack  = callBack;
		if (clearBtn != null && editText.isEnabled()) {
			mClearBtn.setOnClickListener(this);
		}
		mEditText.setOnFocusChangeListener(this);
		if(textType < 0){
			this.textType = TextType.TEXT;
		}
		init();
	}

	public static TextChangeListener withText(EditText editText) {
		return new TextChangeListener(editText, null, TextType.TEXT, null);
	}

	public static TextChangeListener withNumber(EditText editText) {
		return new TextChangeListener(editText, null, TextType.NUMBER, null);
	}

	public static TextChangeListener withText(EditText editText, EditTextCallBack callBack) {
		return new TextChangeListener(editText, null, TextType.TEXT, callBack);
	}

	public static TextChangeListener withNumber(EditText editText, EditTextCallBack callBack) {
		return new TextChangeListener(editText, null, TextType.NUMBER, callBack);
	}

	public static Activity getActivityFromView(View view) {
		Context context = view.getContext();
		while (context instanceof ContextWrapper) {
			if (context instanceof Activity) {
				return (Activity) context;
			}
			context = ((ContextWrapper) context).getBaseContext();
		}
		return null;
	}

	private void init(){
		if(mClearBtn != null){
			fade_in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
			fade_out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
		}

		if (textType == TextType.PHONE) {
			//限定最大输入字符数
			setMaxLength(13);
		} else if (textType == TextType.BANK_CARD) {
			setMaxLength(22);
		}
	}

	public BigDecimal getMaxNumber() {
		return maxNumber;
	}

	/**
	 * 设置允许输入的最大数值（生效于类型：{@link TextType#NUMBER}）
	 * @param maxNumber
	 */
	public void setMaxNumber(BigDecimal maxNumber) {
		this.maxNumber = maxNumber;
		if (textType == TextType.NUMBER && mEditText != null && maxNumber != null) {
			formatNumber(mEditText.getText());
		}
	}

	/**
	 * 设置EditText最大长度
	 * @param maxLength
	 */
	public void setMaxLength(int maxLength){
		if(mEditText != null){
			mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
		}
	}

	@Override
	public void onClick(View v){
		mEditText.setText("");
		mEditText.requestFocus();
		setClearViewVisibility(View.GONE);
		bankNumberSpace = 0;
	}

	@Override
	public void afterTextChanged(Editable s){
		if (stringBuffer != null) {
			stringBuffer.delete(0, stringBuffer.length() - 1);
			stringBuffer = null;
		}
		//如果是数字类型
		if (textType == TextType.NUMBER) {
			formatNumber(s);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after){}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){
		setClearViewVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
		if (textType == TextType.PHONE) {
			// 格式化手机号码
			formatPhone(s, start, count);
		} else if (textType == TextType.BANK_CARD) {
			// 银行卡
			formatBankCard(s, start, count);
		}
		if(callBack != null){
			callBack.onTextSet(s.toString());
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus){
		setClearViewVisibility(hasFocus ? View.VISIBLE : View.GONE);
		if(!hasFocus){
			if (textType == TextType.NUMBER && mEditText != null) {
				String money = mEditText.getText().toString().trim();
				if(!TextUtils.isEmpty(money)){
					if(money.endsWith(".")){
						mEditText.setText(formatMoney(money));
					} else {
						mEditText.setText(money);
					}
					mEditText.setSelection(mEditText.getText().length());
				}
			}
		}
	}

	/**
	 * 控制清除内容的按钮显示状态，失去输入焦点，隐藏按钮
	 * @param visibility
	 */
	private void setClearViewVisibility(int visibility){
		if(mClearBtn != null){
			// 如果输入框不可用，则不显示清除按钮
			if (!mEditText.isEnabled()) {
				visibility = View.GONE;
			}
			if (visibility == View.GONE) {
				if (mClearBtn.getVisibility() != View.GONE) {
					mClearBtn.setVisibility(View.GONE);
					mClearBtn.startAnimation(fade_out);
				}
			} else {
				// 不可用，则不显示清空按钮
				if (!mEditText.isEnabled()) {
					return;
				}
				// 如果已获得焦点、不是显示的并且内容不为空，则显示清除按钮
				if (mEditText.isFocused() && mClearBtn.getVisibility() != visibility && !TextUtils.isEmpty(mEditText.getText().toString().trim())) {
					mClearBtn.setVisibility(visibility);
					mClearBtn.startAnimation(fade_in);
				}
			}
		}
	}

	private void formatPhone(CharSequence s, int start, int count){
		if (count == 1) { //输入
			stringBuffer = new StringBuffer(s);
			if (start == 3 || start == 8) {
				setValue(formatPhoneNumber(s.toString()));
			}
		} else { //删除
			if (s.toString().endsWith(" ")) {
				setValue(formatPhoneNumber(s.toString().trim()));
			}
		}
	}

	/**
	 * 格式化为数字
	 * @param s
	 */
	private void formatNumber(Editable s){
		if(s == null){
			return;
		}
		String number = s.toString();

		if (!TextUtils.isEmpty(number)) {

			if (maxNumber != null) {
				BigDecimal bigDecimal = toBigDecimal(number);
				if (bigDecimal.compareTo(maxNumber) > 0) {
					number = bigDecimal.toString();

					TextWatcher textWatcher = this;
					mEditText.removeTextChangedListener(this);
					s.clear();
					s.append(String.format("%."+precision+"f", maxNumber));
					mEditText.addTextChangedListener(textWatcher);
				}
			}

			int dotPosition = number.indexOf("."); // 点的位置
			if (dotPosition == 0) {
				s.delete(0, 1);
			}
			if (dotPosition > 0) { // 0.123456789
				int length = number.subSequence(dotPosition + 1, number.length()).toString().length();
				if (precision == 0) {
					s.delete(dotPosition + precision, number.length());
				} else if (length > precision) {
					s.delete(dotPosition + 1 + precision, number.length());
				}
			}
			if (number.length() >= 2) {
				if(number.length() == 2 && number.subSequence(0, 1).equals("0") && !"0.".equals(number)){
					s.delete(0, 1);
				} else if (number.subSequence(0, 2).equals("00")) { // =00
					s.delete(1, 2);
				} else if (number.subSequence(0, 1).toString().contains("0") && !number.subSequence(1, 2).toString().contains(".")) { // =0.
					s.delete(1, 2);
				}
			}
		}
	}

	private BigDecimal toBigDecimal(String value) {
		if(TextUtils.isEmpty(value)) {
			return BigDecimal.ZERO;
		}
		value = value.trim();
		value = value.replace(",", "");
		try {
			return new BigDecimal(value);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 格式化为手机号
	 * @param string
	 * @return
	 */
	private String formatPhoneNumber(String string){
		StringBuffer sb = new StringBuffer(string.replaceAll(" ", ""));
		for (int i = 0; i < string.length(); i++) {
			if (i == 3 || i == 8) {
				sb.insert(i, " ");
			}
		}
		return sb.toString();
	}

	/**
	 * 格式化为银行卡
	 * @return
	 */
	private void formatBankCard(CharSequence s, int start, int count){
		if (count == 1) {
			stringBuffer = new StringBuffer(s);
			if (start > 0 && start % bankNumberSpace == 0) {
				for (int i = 0; i < stringBuffer.length(); i++) {
					if (i > 0 && i % 4 == 0) {
						stringBuffer.insert(i, " ");
						setValue(stringBuffer.toString());
					}
				}
			}
		} else {
			if (s.toString().endsWith(" ")) {
				setValue(s.toString().trim());
			}
		}
	}

	public int getPrecision() {
		return precision;
	}

	public TextChangeListener setPrecision(int precision) {
		this.precision = precision;
		return this;
	}

	private void setValue(String s){
		mEditText.setText(s);
		mEditText.setSelection(mEditText.getText().toString().length());
	}

	public interface EditTextCallBack {
		void onTextSet(String str);
	}

	private static View findViewById(Activity activity, int clearViewId){
		if(clearViewId == 0 || clearViewId == -1 || activity == null){
			return null;
		}
		try {
			return activity.findViewById(clearViewId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化金额
	 */
	public static String formatMoney(String value) {
		if (TextUtils.isEmpty(value)) return "";
		BigDecimal bigDecimal = new BigDecimal(value.replaceAll(",", ""));
		return new DecimalFormat("#######.######").format(bigDecimal.toString());
	}

	/**
	 * EditText输入的文本内容类型
	 *
	 * <p>Created by wuguangxin on 14/9/25 </p>
	 */
	public static class TextType {
		/**
		 * 普通文本类型（默认）
		 */
		public static int TEXT = 0;

		/**
		 * 数字(如 100.01)（默认保留2位小数）
		 */
		public static int NUMBER = 1;

		/**
		 * 手机号码类型。
		 *  将格式化为如 186 1111 2222样式，并在输入过程中自动格式化；
		 *  当调用getText()时，务必调用.replaceAll(" ", "")，去掉所有空格。
		 */
		public static int PHONE = 2;

		/**
		 * 银行卡号码 （4位空一格，如xxxx xxxx xxxx xxxx xxx）
		 */
		public static int BANK_CARD = 3;

		/**
		 * 身份证号码 （如xxxxxx xxxx xxxx xxxx )
		 */
		public static int ID_CARD = 4;
	}
}