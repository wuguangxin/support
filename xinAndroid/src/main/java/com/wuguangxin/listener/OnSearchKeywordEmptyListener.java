package com.wuguangxin.listener;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * 标题栏中搜索输入框内容变化监听器，输入框获得输入焦点并输入了内容时，显示你定义的清除内容的按钮，
 * 当失去输入焦点，则隐藏按钮，当点击清除按钮时，清空文本框的内容
 *
 * @author wuguangxin
 * @date: 2015-5-12 下午6:17:31
 */
public class OnSearchKeywordEmptyListener implements TextWatcher, OnClickListener, OnFocusChangeListener{
	private EditText mEditText;
	private View mClearBtn;
	/**
	 * 构造器。EditText内容变化监听类，当文本框获得输入焦点并输入了内容时，显示你定义的清除内容的按钮，
	 * 当失去输入焦点，则隐藏按钮，当点击清除按钮时，清空文本框的内容
	 * 
	 * @param editText 要监听的EditText
	 * @param clearBtn 清除按钮
	 */
	public OnSearchKeywordEmptyListener(EditText editText, View clearBtn){
		this.mEditText = editText;
		this.mClearBtn = clearBtn;
		if (clearBtn != null) {
			mClearBtn.setOnClickListener(this);
		}
		if (editText != null) {
			mEditText.setOnFocusChangeListener(this);
		}
	}

	@Override
	public void onClick(View v){
		mEditText.setText("");
		mEditText.requestFocus();
		setClearViewVisibility(View.GONE);
	}

	@Override
	public void afterTextChanged(Editable s){
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after){}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){
		setClearViewVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus){
		setClearViewVisibility(hasFocus ? View.VISIBLE : View.GONE);
	}

	/**
	 * 控制清除内容的按钮显示状态，失去输入焦点，隐藏按钮
	 * @param visibility
	 */
	private void setClearViewVisibility(int visibility){
		if (visibility == View.GONE) {
			if (mClearBtn.getVisibility() != View.GONE) {
				mClearBtn.setVisibility(View.GONE);
			}
		} else {
			// 如果已获得焦点、不是显示的并且内容不为空，则显示清除按钮
			if (mEditText.isFocused() && mClearBtn.getVisibility() != visibility && !TextUtils.isEmpty(mEditText.getText().toString().trim())) {
				mClearBtn.setVisibility(visibility);
			}
		}
	}

}