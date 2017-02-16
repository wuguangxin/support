package com.wuguangxin.listener;

import android.app.Activity;
import android.content.Context;
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

import com.wuguangxin.utils.MoneyUtils;

/**
 * EditText内容变化监听类，当文本框获得输入焦点并输入了内容时，显示你定义的清除内容的按钮，
 * 当失去输入焦点，则隐藏按钮，当点击清除按钮时，清空文本框的内容
 *
 * @author wuguangxin
 */
public class TextChangeListener implements TextWatcher, OnClickListener, OnFocusChangeListener{
	private EditText mEditText;
	private View mClearBtn;
	private EditTextCallBack callBack;
	/**
	 * 输入的数据类型
	 */
	private int textType = TextType.TEXT;
	private StringBuffer stringBuffer;
	private StringBuffer strs;
	private int bankNumberSpance = 4;
	private Context context;
	private Animation fade_in;
	private Animation fade_out;

	/**
	 */
	public TextChangeListener(){
		this(null, null, TextType.TEXT);
	}

	/**
	 * @param editText 要监听的EditText
	 */
	public TextChangeListener(EditText editText){
		this(editText, null, TextType.TEXT);
	}

	/**
	 * @param editText 要监听的EditText
	 * @param clearViewId 清除按钮ViewId
	 */
	public TextChangeListener(EditText editText, int clearViewId){
		this(editText, findViewById(editText.getContext(), clearViewId), TextType.TEXT);
	}
	
	/**
	 * @param editText 要监听的EditText
	 * @param clearViewId 清除按钮ViewId
	 * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
	 */
	public TextChangeListener(EditText editText, int clearViewId, int textType){
		this(editText, findViewById(editText.getContext(), clearViewId), textType, null);
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
	 * @param clearBtn 清除按钮
	 * @param textType 输入文本类型如，看TextChangeListener.TextType类（有手机号码、金额等类型）
	 */
	public TextChangeListener(EditText editText, View clearBtn, int textType, EditTextCallBack callBack){
		this.context = editText.getContext();
		this.mEditText = editText;
		this.mClearBtn = clearBtn;
		this.textType = textType;
		this.callBack  = callBack;
		if (clearBtn != null && editText.isEnabled()) {
			mClearBtn.setOnClickListener(this);
		}
		if (editText != null) {
			mEditText.setOnFocusChangeListener(this);
		}
		if(textType < 0){
			this.textType = TextType.TEXT;
		}
		init();
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
		bankNumberSpance = 0;
	}

	@Override
	public void afterTextChanged(Editable s){
		if (stringBuffer != null) {
			stringBuffer.delete(0, stringBuffer.length() - 1);
			stringBuffer = null;
		}
		//如果是金额类型
		if (textType == TextType.MONEY) {
			formatMoney(s);
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
			if (textType == TextType.MONEY  && mEditText != null) {
				String money = mEditText.getText().toString().trim();
				if(!TextUtils.isEmpty(money)){
					if(money.endsWith(".")){
						mEditText.setText(MoneyUtils.format2bit(money));
					} else {
						mEditText.setText(money);
					}
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
			if (visibility == View.GONE) {
				if (mClearBtn.getVisibility() != View.GONE) {
					mClearBtn.setVisibility(View.GONE);
					mClearBtn.startAnimation(fade_out);
				}
			} else {
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
	 * 格式化为金额
	 * @param s
	 */
	private void formatMoney(Editable s){
		if(s == null){
			return;
		}
		String money = s.toString();
		if (!TextUtils.isEmpty(money)) {
			int dotPosition = money.indexOf("."); // 点的位置
			if (dotPosition == 0) {
				s.delete(0, 1);
			}
			if (dotPosition > 0) {
				if (money.subSequence(dotPosition + 1, money.length()).toString().length() > 2) {
					s.delete(dotPosition + 3, money.length());
				}
			}
			if (money.length() >= 2) {
				if(money.length() == 2 && money.subSequence(0, 1).equals("0") && !"0.".equals(money)){
					s.delete(0, 1);
				} else if (money.subSequence(0, 2).equals("00")) { // =00
					s.delete(1, 2);
				} else if (money.subSequence(0, 1).toString().contains("0") && !money.subSequence(1, 2).toString().contains(".")) { // =0.
					s.delete(1, 2);
				}
			}
		}
	}

	/**
	 * 格式化为手机号
	 * @param string
	 * @return
	 */
	private String formatPhoneNumber(String string){
		strs = new StringBuffer(string.replaceAll(" ", ""));
		for (int i = 0; i < string.length(); i++) {
			if (i == 3 || i == 8) {
				strs.insert(i, " ");
			}
		}
		return strs.toString();
	}

	/**
	 * 格式化为银行卡
	 * @return
	 */
	private void formatBankCard(CharSequence s, int start, int count){
		if (count == 1) {
			stringBuffer = new StringBuffer(s);
			if (start > 0 && start % bankNumberSpance == 0) {
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

	private void setValue(String s){
		mEditText.setText(s);
		mEditText.setSelection(mEditText.getText().toString().length());
	}
	
	public interface EditTextCallBack {
		void onTextSet(String str);
	}
	
	private static View findViewById(Context mContext, int clearViewId){
		if(clearViewId == 0 || clearViewId == -1){
			return null;
		}
		return ((Activity)mContext).findViewById(clearViewId);
	}
	
	/**
	 * EditText输入的文本内容类型
	 *
	 * @author wuguangxin
	 * @date: 2014-9-25 下午1:43:37
	 */
	public static class TextType {
		/**
		 * 普通文本类型（默认）
		 */
		public static int TEXT = 0;
		
		/**
		 * 金额(如 100.01)（保留2位小数）
		 */
		public static int MONEY = 1;
		
		/**
		 * 手机号码类型。<br> 
		 * 将格式化为如 186 1111 2222样式，并在输入过程中自动格式化 <br>
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