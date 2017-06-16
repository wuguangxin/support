package com.wuguangxin.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.wuguangxin.R;
import com.wuguangxin.listener.OnDoubleClickListener;
import com.wuguangxin.utils.AndroidUtils;
import com.wuguangxin.utils.Utils;
import com.wuguangxin.view.MyGridView;
import com.wuguangxin.view.MyListView;

import java.util.Calendar;

/**
 * 自定义对话框
 *
 * <p>Created by wuguangxin on 14/8/29 </p>
 */
@SuppressLint("HandlerLeak")
public class MyDialog extends Dialog implements DialogInterface{
	private View mDialogLayout;
	private RelativeLayout mDialogTitleLayout;  // 对话框标题布局
	private ScrollView mDialogBodyLayout;  // 滚动布局
	private RelativeLayout mDialogBodyPasswordEditTextLayout; // 输入密码布局
	private LinearLayout mDialogBodyUserLayout; // 用户自定义的view布局显示区
	private View mDialogTopLine;
	private View mDialogBottomLine;
	private Context context;
	private View mDialogButtonLayout;
	private TextView mPositiveButton; // 确认按钮
	private TextView mNegativeButton; // 否认按钮
	private TextView mDialogTitle;
	private TextView mDialogMessage;
	private MyListView mDialogListView;
	private MyGridView mDialogGridView;
	private DatePicker mDialogDatePicker;
	private TimePicker mDialogTimePicker;
	private TextView mDialogMenuLeft;
	private TextView mDialogMenuRight;
	private View enableView; // 需要解禁的View
	private LayoutInflater inflater;
	private boolean isShowBackView;
	private String title;
	private String message;
	private String menuLeftText; // 左边按钮文字
	private String menuRightText; // 右边按钮文字
	private boolean isCancelable = true; // 是否可以取消
	
	public MyDialog(Context context){
		this(context, null);
	}

	/**
	 * 构造一个MyDialog对话框
	 * @param context Activity上下文
	 * @param title 对话框标题
	 */
	public MyDialog(Context context, String title){
		this(context, title, null);
	}

	/**
	 * 构造一个MyDialog对话框
	 * @param context Activity上下文
	 * @param title 对话框标题
	 * @param message 对话框正文消息
	 */
	public MyDialog(Context context, String title, String message){
		super(context, R.style.xin_loading_dialog);
		this.context = context;
		this.title = title;
		this.message = message;
		initView();
	}

	/**
	 * 构造一个MyDialog对话框
	 * @param context Activity上下文
	 * @param themeResId 主题ID,如 AlertDialog.THEME_HOLO_LIGHT
	 */
	public MyDialog(Context context, int themeResId) {
		super(context, themeResId);
		this.context = context;
		initView();
	}

	/**
	 * 初始化对话框
	 */
	@SuppressLint("InflateParams")
	private void initView(){
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDialogLayout = inflater.inflate(R.layout.xin_dialog_layout, null);
		mDialogTitleLayout = (RelativeLayout) mDialogLayout.findViewById(R.id.df_dialog_title_layout);
		mDialogBodyLayout = (ScrollView) mDialogLayout.findViewById(R.id.df_dialog_body_layout);
		mDialogBodyPasswordEditTextLayout = (RelativeLayout) mDialogLayout.findViewById(R.id.xin_dialog_password_edittext_layout);
		mDialogBodyUserLayout = (LinearLayout) mDialogLayout.findViewById(R.id.df_dialog_body_user_layout);
		mDialogTopLine = mDialogLayout.findViewById(R.id.dialog_top_line);  //分割线
		mDialogTitle = (TextView) mDialogLayout.findViewById(R.id.dialog_top_title);  //标题
		mDialogMenuLeft = (TextView) mDialogLayout.findViewById(R.id.dialog_top_left);  // 左边按钮
		mDialogMenuRight = (TextView) mDialogLayout.findViewById(R.id.dialog_top_right);  // 右边按钮
		mDialogMessage = (TextView) mDialogLayout.findViewById(R.id.df_dialog_message);  //消息内容
		mDialogListView = (MyListView) mDialogLayout.findViewById(R.id.df_dialog_listview_default);  // 默认listView
		mDialogGridView = (MyGridView) mDialogLayout.findViewById(R.id.df_dialog_gridview_default);  // 默认listView
		mDialogBottomLine = mDialogLayout.findViewById(R.id.dialog_bottom_line); // 提交按钮
		mDialogButtonLayout = mDialogLayout.findViewById(R.id.df_dialog_button_layout); // 按钮布局
		mPositiveButton = (TextView) mDialogLayout.findViewById(R.id.df_dialog_button_positive); // 确认按钮
		mNegativeButton = (TextView) mDialogLayout.findViewById(R.id.df_dialog_button_negative); //否认按钮

		mDialogTitleLayout.setVisibility(View.GONE);
		mDialogBodyPasswordEditTextLayout.setVisibility(View.GONE);
		mDialogBodyUserLayout.setVisibility(View.GONE);
		mDialogListView.setVisibility(View.GONE);
		mDialogMessage.setVisibility(View.GONE);

		mDialogButtonLayout.setVisibility(View.GONE);
		mPositiveButton.setVisibility(View.GONE);
		mNegativeButton.setVisibility(View.GONE);
		
		setTitle(title);
		setMessage(message);
		
		setContentView(mDialogLayout);
		setCancelable(isCancelable);
	}
	
	/**
	 * 初始化时间组件 
	 */
	private void initTimePicker(){
		if(mDialogTimePicker == null){
			mDialogTimePicker = (TimePicker) mDialogLayout.findViewById(R.id.dialog_timepicker);  // 时间组件 
		}
	}
	
	/**
	 * 初始化日期组件
	 */
	private void initDatePicker(){
		if(mDialogDatePicker == null){
			mDialogDatePicker = (DatePicker) mDialogLayout.findViewById(R.id.dialog_datepicker);  // 日期组件
		}
	}

	/**
	 * 设置对话框标题左边按钮监听器 <br>
	 * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
	 * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
	 * @param text 按钮文字
	 * @param onClickDialogListener 按钮监听器
	 * @return
	 */
	public MyDialog setMenuLeft(String text, final OnClickDialogListener onClickDialogListener){
		this.menuLeftText = text;
		mDialogMenuLeft.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(menuLeftText)) {
			mDialogMenuLeft.setBackgroundColor(context.getResources().getColor(R.color.transparent));
			mDialogMenuLeft.setText(menuLeftText);
		}
		mDialogMenuLeft.setOnClickListener(new OnDoubleClickListener(){
			@Override
			public void onClicked(View v){
				if (onClickDialogListener != null) {
					onClickDialogListener.onClick(v, MyDialog.this);
				} else {
					dismiss();
				}
			}
		});
		return this;
	}

	/**
	 * 设置对话框标题右边按钮监听器 <br>
	 * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
	 * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
	 * 当点击按钮时，该按钮会默认被禁止点击setClickable(false) 直到对话框回调cancel()、dismiss()、hide()时被重置为true<br>
	 * @param text 按钮文字
	 * @param onClickDialogListener 按钮监听器
	 * @return
	 */
	public MyDialog setMenuRight(String text, final OnClickDialogListener onClickDialogListener){
		this.menuRightText = text;
		mDialogMenuRight.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(menuRightText)) {
			mDialogMenuRight.setBackgroundColor(context.getResources().getColor(R.color.transparent));
			mDialogMenuRight.setText(menuRightText);
		}
		mDialogMenuRight.setOnClickListener(new OnDoubleClickListener(){
			@Override
			public void onClicked(View v){
				if (onClickDialogListener != null) {
					onClickDialogListener.onClick(v, MyDialog.this);
				} else {
					dismiss();
				}
			}
		});
		return this;
	}
	
	/**
	 * 设置对话框的取消按钮监听器 <br>
	 * @param text 取消按钮文字
	 * @param isFinish 点击按钮关闭Activity
	 * @return
	 */
	public MyDialog setNegativeButton(String text, boolean isFinish){
		return setNegativeButton(text, isFinish, null);
	}

	/**
	 * 设置对话框的取消按钮监听器 <br>
	 * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
	 * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
	 * @param text
	 * @param onClickDialogListener 监听器
	 * @return
	 */
	public MyDialog setNegativeButton(String text, final OnClickDialogListener onClickDialogListener){
		return setNegativeButton(text, false, onClickDialogListener);
	}

	/**
	 * 设置对话框的取消按钮监听器 <br>
	 * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
	 * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
	 * 当点击按钮时，该按钮会默认被禁止点击setClickable(false) 直到对话框回调cancel()、dismiss()、hide()时被重置为true<br>
	 * @param text 按钮文字
	 * @param isFinish 点击按钮关闭Activity, 当该值为true时，将不执行onClickDialogListener回调
	 * @param onClickDialogListener 按钮监听器
	 * @return
	 */
	public MyDialog setNegativeButton(String text, final boolean isFinish, final OnClickDialogListener onClickDialogListener){
		if (!TextUtils.isEmpty(text)) {
			mNegativeButton.setVisibility(View.VISIBLE);
			mDialogButtonLayout.setVisibility(View.VISIBLE);
			mNegativeButton.setText(text);
			setButtonStyle(mNegativeButton);
			mNegativeButton.setOnClickListener(new OnDoubleClickListener(){
				@Override
				public void onClicked(View v){
					if (onClickDialogListener != null) {
//						mNegativeButton.setClickable(false);
						onClickDialogListener.onClick(v, MyDialog.this);
					} else {
						dismiss();
						if(isFinish){
							((Activity) context).finish();
						}
					}
				}
			}); // 取消
		}
		return this;
	}
	
	/**
	 * 设置对话框的确认按钮监听器 <br>
	 * @param text 确认按钮文字
	 * @param isFinish 点击按钮关闭Activity
	 * @return
	 */
	public MyDialog setPositiveButton(String text, boolean isFinish){
		return setPositiveButton(text, isFinish, null);
	}

	/**
	 * 设置对话框确认按钮监听器。<br>
	 * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
	 * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
	 * @param text 按钮文字
	 * @param onClickDialogListener
	 * @return
	 */
	public MyDialog setPositiveButton(String text, final OnClickDialogListener onClickDialogListener){
		return setPositiveButton(text, false, onClickDialogListener);
	}

	/**
	 * 设置对话框确认按钮监听器。<br>
	 * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
	 * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
	 * @param text 按钮文字
	 * @param isFinish 点击按钮关闭Activity,当该值为true时，将不执行onClickDialogListener回调
	 * @param onClickDialogListener
	 * @return
	 */
	private MyDialog setPositiveButton(String text, final boolean isFinish, final OnClickDialogListener onClickDialogListener){
		if (!TextUtils.isEmpty(text)) {
			mPositiveButton.setVisibility(View.VISIBLE);
			mDialogButtonLayout.setVisibility(View.VISIBLE);
			mPositiveButton.setText(text);
			setButtonStyle(mPositiveButton);
			mPositiveButton.setOnClickListener(new OnDoubleClickListener(){
				@Override
				public void onClicked(View v){
					if (onClickDialogListener != null) {
//						mPositiveButton.setClickable(false);
						onClickDialogListener.onClick(v, MyDialog.this);
					} else {
						dismiss();
						if(isFinish){
							((Activity) context).finish();
						}
					}
				}
			});
		}
		return this;
	}

	private void setButtonStyle(TextView button){
		// 两按钮都显示，则显示中间分割线
		if (mPositiveButton.getVisibility() == View.VISIBLE && mNegativeButton.getVisibility() == View.VISIBLE) {
			mDialogBottomLine.setVisibility(View.VISIBLE);
			mNegativeButton.setBackgroundResource(R.drawable.xin_dialog_bottom_button_left);
			mPositiveButton.setBackgroundResource(R.drawable.xin_dialog_bottom_button_right);
		} else {
			mDialogBottomLine.setVisibility(View.GONE);
			button.setBackgroundResource(R.drawable.xin_dialog_bottom_button_single);
		}
	}

	/**
	 * 设置NegativeButton按钮是否可用
	 * @param enabled
	 */
	public void setNegativeButtonEnabled(boolean enabled){
		if (mNegativeButton != null) {
			mNegativeButton.setEnabled(enabled);
		}
	}

	/**
	 * 设置ConfirmButton按钮是否可用
	 * @param enabled
	 */
	public void setPositiveButtonEnabled(boolean enabled){
		if (mPositiveButton != null) {
			mPositiveButton.setEnabled(enabled);
		}
	}

	/**
	 * 设置对话框标题
	 * @param title
	 * @return
	 */
	public MyDialog setTitle(String title){
		if (title != null) {
			this.title = title;
			mDialogTitle.setText(title); // 标题
			mDialogTitleLayout.setVisibility(View.VISIBLE);
			if (isShowBackView) {
				mDialogMenuLeft.setVisibility(View.VISIBLE);
			}
		}
		return this;
	}

	/**
	 * 设置对话框正文内容消息
	 * @param message
	 * @return
	 */
	public MyDialog setMessage(String message){
		if (message != null) {
			this.message = message;
			mDialogMessage.setText(message); //消息正文
			mDialogMessage.setVisibility(View.VISIBLE);
		}
		return this;
	}

	/**
	 * 设置对话框正文内容消息
	 * @param message
	 * @return
	 */
	public MyDialog setMessage(CharSequence message){
		if (message != null) {
			this.message = message.toString();
			mDialogMessage.setText(message); //消息正文
			mDialogMessage.setVisibility(View.VISIBLE);
		}
		return this;
	}

	/**
	 * 获取默认的消息文本框
	 * @return
	 */
	public TextView getMessageView(){
		return mDialogMessage;
	}
	
	/**
	 * 获取默认的消息文本框
	 * @return
	 */
	public TextView getTitleView(){
		return mDialogTitle;
	}
	
	/**
	 * 获取默认的消息文本框
	 * @return
	 */
	public View getPasswordFocusLayout(){
		return mFocusLayout;
	}

	/**
	 * 获取对话框中的ListView
	 * @return
	 */
	public MyListView getListView(){
		return mDialogListView;
	}

	/**
	 * 获取对话框中的GridView
	 * @return
	 */
	public MyGridView getGridView(){
		return mDialogGridView;
	}

	/**
	 * 获取对话框中确认按钮
	 * @return
	 */
	public TextView getPositiveButton(){
		return mPositiveButton;
	}

	/**
	 * 获取对话框中取消按钮
	 * @return
	 */
	public TextView getNegativeButton(){
		return mNegativeButton;
	}

	/**
	 * 设置GridView适配器
	 * @param adapter
	 * @return
	 */
	public MyDialog setGridViewAdapter(BaseAdapter adapter){
		if (adapter != null) {
			mDialogBodyUserLayout.setVisibility(View.GONE);
			mDialogGridView.setVisibility(View.VISIBLE);
			mDialogGridView.setAdapter(adapter);
		}
		return this;
	}

	public MyDialog notifyGridView(){
		if (mDialogGridView != null) {
			mDialogGridView.getAdapter().notify();
		}
		return this;
	}

	/**
	 * 设置列表适配器
	 * @param adapter
	 * @return
	 */
	public MyDialog setListViewAdapter(BaseAdapter adapter){
		if (adapter != null) {
			mDialogBodyUserLayout.setVisibility(View.GONE);
			mDialogListView.setVisibility(View.VISIBLE);
			mDialogListView.setAdapter(adapter);
		}
		return this;
	}

	public MyDialog notifyListView(){
		if (mDialogListView != null) {
			mDialogListView.getAdapter().notify();
		}
		return this;
	}

	// *****密码对话框s****************************************************************************************************************
	/** 密码输入框组 */
	private TextView[] payPasswordTextViewArrs;
	private View mFocusLayout;
	private boolean isSoftKeyboardVisibility = true;
	private TextView mPayPasswordDesc; // 用于对交易密码进行提示
	private EditText mPayPassword;
	private TextView mPayPassword1, mPayPassword2, mPayPassword3, mPayPassword4, mPayPassword5, mPayPassword6;
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch (msg.what) {
				case View.VISIBLE:
					if(Utils.isShowSoftKey(context)){
						return;
			        }
					InputMethodManager immShow = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
					immShow.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
					break;
				case View.GONE:
					if(!Utils.isShowSoftKey(context)){
						return;
			        }
					InputMethodManager immHide = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
					immHide.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					break;
			}
		}
	};
	
	/**
	 * 设置密码输入框组件显示状态
	 * @param visible 是否可见 false-隐藏 true-显示
	 * @return
	 */
	public MyDialog setPasswordVisibility(boolean visible){
		if (mDialogBodyPasswordEditTextLayout != null) {
			if(visible){
				initPasswordEditTextLayout(visible);
				mDialogBodyPasswordEditTextLayout.setVisibility(View.VISIBLE);
			} else {
				mDialogBodyPasswordEditTextLayout.setVisibility(View.GONE);
			}
		}
		return this;
	}
	
	/**
	 * 对本次输入交易密码的说明
	 * @param descString
	 * @return
	 */
	public MyDialog setPasswordDesc(String descString){
		if (mDialogBodyPasswordEditTextLayout == null) {
			setPasswordVisibility(true);
		}
		if(mPayPasswordDesc != null){
			mPayPasswordDesc.setText(descString);
			mPayPasswordDesc.setVisibility(View.VISIBLE);
		}
		return this;
	}
	
	/**
	 * 获取交易密码描述TextView
	 * @return
	 */
	public TextView getPasswordDescView(){
		if(mPayPasswordDesc != null){
			return mPayPasswordDesc;
		}
		return null;
	}
	
	
	/**
	 * 判断是否显示软键盘
	 * @return
	 */
	public boolean isSoftKeyboardVisibility(){
		return isSoftKeyboardVisibility;
	}
	
	/**
	 * 设置是否显示软键盘
	 * @param isSoftKeyboardVisibility 是否显示
	 */
	public void setSoftKeyboardVisibility(boolean isSoftKeyboardVisibility){
		this.isSoftKeyboardVisibility = isSoftKeyboardVisibility;
	}
	
	/**
	 * 获取输入的密码（如果设置显示了密码输入框才生效）
	 * @return
	 */
	public String getPassword(){
		if(mPayPassword != null){
			return mPayPassword.getText().toString();
		}
		return null;
	}
	
	@SuppressLint("InflateParams")
	private void initPasswordEditTextLayout(boolean visible){
		mPayPasswordDesc = (TextView) findViewById(R.id.xin_dialog_password_desc);
		mPayPasswordDesc.setVisibility(View.GONE);
		mPayPassword = (EditText) findViewById(R.id.xin_dialog_password_edittext);
		mPayPassword1 = (TextView) findViewById(R.id.xin_dialog_password_edittext_1);
		mPayPassword2 = (TextView) findViewById(R.id.xin_dialog_password_edittext_2);
		mPayPassword3 = (TextView) findViewById(R.id.xin_dialog_password_edittext_3);
		mPayPassword4 = (TextView) findViewById(R.id.xin_dialog_password_edittext_4);
		mPayPassword5 = (TextView) findViewById(R.id.xin_dialog_password_edittext_5);
		mPayPassword6 = (TextView) findViewById(R.id.xin_dialog_password_edittext_6);
		payPasswordTextViewArrs = new TextView[]{mPayPassword1,mPayPassword2,mPayPassword3,mPayPassword4,mPayPassword5,mPayPassword6};
		mFocusLayout = findViewById(R.id.xin_dialog_password_edittext_focus_layout);
		mFocusLayout.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				setOnClientInputMethod();
			}
		});
		mPayPassword.addTextChangedListener(new TextWatcher(){
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
				if(payPasswordTextViewArrs != null && start < payPasswordTextViewArrs.length){
					payPasswordTextViewArrs[start].setText(before == 0 ? "●" : "");
				}
				setPositiveButtonEnabled(s.length() == 6);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}

			@Override
			public void afterTextChanged(Editable s){}
		});
	}
	
	protected void setOnClientInputMethod(){
		if (!isShowSoftKey()) {
			handler.sendEmptyMessage(View.VISIBLE); // 显示软键盘
			mPayPassword.requestFocus();
			setFocusPosition(mPayPassword);
		} else {
			handler.sendEmptyMessage(View.GONE); // 隐藏软键盘
			mPayPassword.clearFocus();
		}
	}

	/**
	 * 软键盘是否弹出
	 * @return
	 */
	public boolean isShowSoftKey(){
		return getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
	}

	/**
	 * 定位光标位置到EditText文本的末尾
	 * @param mEditText EditText
	 */
	public static void setFocusPosition(EditText mEditText){
		mEditText.requestFocus();
		Editable text = mEditText.getText();
		Selection.setSelection(text, text.length());
	}
	
	/**
	 * 设置对话框中密码输入框监听器
	 * @param onClickPasswordEditTextsListener
	 * @return
	 */
	public MyDialog setOnClickPasswordEditTextsListener(final OnClickPasswordEditTextsListener onClickPasswordEditTextsListener){
		mFocusLayout.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				System.out.println("点击密码输入框了===============");
				if (onClickPasswordEditTextsListener != null) {
					onClickPasswordEditTextsListener.onClick();
				}
			}
		});
		return this;
	}
	
	protected void showSoftkey(){
//		if(Utils.isShowSoftKey(context)){
//			return;
//        }
		InputMethodManager immShow = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		immShow.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
	}
	// *****密码对话框e****************************************************************************************************************

	/**
	 * 设置自定义的对话框正文布局
	 * @param view 你的布局
	 * @return
	 */
	public MyDialog setView(View view){
		if (view != null) {
			try {
				LinearLayout parent = (LinearLayout) view.getParent();
				if(parent != null){
					parent.removeView(view);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mDialogBodyUserLayout.removeAllViews();
			mDialogBodyUserLayout.addView(view);
			mDialogBodyUserLayout.setVisibility(View.VISIBLE);
		}
		return this;
	}
	
	/**
	 * 获取对话框中ContextView里的Child的个数
	 * @return
	 */
	public int getChildCount(){
		if (mDialogBodyUserLayout != null) {
			return mDialogBodyUserLayout.getChildCount();
		}
		return 0;
	}

	public void setPadding(int size){
		if (mDialogLayout != null) {
			if (size < 0) {
				size = 0;
			}
			mDialogLayout.setPadding(size, size, size, size);
		}
	}

	/**
	 * 设置对话框顶部分割线颜色（若不想显示分割线，设置为透明色即可）
	 * @param color
	 * @return
	 */
	public MyDialog setLineColor(int color){
		if (mDialogTopLine != null) {
			mDialogTopLine.setBackgroundColor(color);
		}
		return this;
	}

	/**
	 * 设置分割线可见状态
	 * @param visible
	 * @return
	 */
	public MyDialog setLineVisible(int visible){
		if (mDialogTopLine != null) {
			mDialogTopLine.setVisibility(visible);
		}
		return this;
	}

	/**
	 * 设置标题文字颜色
	 * @param color
	 * @return
	 */
	public MyDialog setTitleTextColor(int color){
		if (mDialogTitle != null) {
			mDialogTitle.setTextColor(color);
		}
		return this;
	}

	/**
	 * 设置标题文字大小
	 * @param size
	 * @return
	 */
	public MyDialog setTitleTextSize(float size){
		if (mDialogTitle != null) {
			mDialogTitle.setTextSize(size);
		}
		return this;
	}

	/**
	 * 设置消息文字大小
	 * @param size
	 * @return
	 */
	public MyDialog setMessageTextSize(float size){
		if (mDialogMessage != null) {
			mDialogMessage.setTextSize(size);
		}
		return this;
	}
	
	/**
	 * 设置消息文字对齐方式
	 * @param gravity
	 * @return
	 */
	public MyDialog setMessageGravity(int gravity){
		if (mDialogMessage != null) {
			mDialogMessage.setGravity(gravity);
		}
		return this;
	}
	
	/**
	 * 设置日期组件 变化监听器
	 * @param onDateChangedListener
	 * @return
	 */
	public MyDialog setDataPicker(OnDateChangedListener onDateChangedListener){
		return setDataPicker(System.currentTimeMillis(), onDateChangedListener);
	}
	
	/**
	 * 设置日期组件 变化监听器
	 * @param timeMillis 默认显示的时间
	 * @param onDateChangedListener
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public MyDialog setDataPicker(long timeMillis, OnDateChangedListener onDateChangedListener){
		setDataPickerVisible(true);
		Calendar c = Calendar.getInstance(java.util.Locale.CHINA);
		c.setTimeInMillis(timeMillis);
		mDialogDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), onDateChangedListener);
		return this;
	}
	
	/**
	 * 设置时间组件变化监听器
	 * @param onTimeChangedListener
	 * @return
	 */
	public MyDialog setTimePicker(OnTimeChangedListener onTimeChangedListener){
		return setTimePicker(System.currentTimeMillis(), onTimeChangedListener);
	}
	
	/**
	 * 设置时间组件变化监听器
	 * @param timeMillis 默认显示的时间
	 * @param onTimeChangedListener 监听器
	 * @return
	 */
	public MyDialog setTimePicker(long timeMillis, OnTimeChangedListener onTimeChangedListener){
		setTimePickerVisible(true, true);
		Calendar c = Calendar.getInstance(java.util.Locale.CHINA);
		c.setTimeInMillis(timeMillis);
		mDialogTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		mDialogTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		mDialogTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		mDialogTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		mDialogTimePicker.setOnTimeChangedListener(onTimeChangedListener);
		return this;
	}
	
	/**
	 * 是否显示日期组件
	 * @param visible
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public MyDialog setDataPickerVisible(boolean visible){
		if(!visible && mDialogDatePicker == null){
			return this;
		}
		initDatePicker();
		mDialogBodyLayout.setVisibility(View.GONE);
		mDialogDatePicker.setVisibility(visible? View.VISIBLE : View.GONE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mDialogDatePicker.setSpinnersShown(true);
			mDialogDatePicker.setCalendarViewShown(false);
		}
		return this;
	}
	
	/**
	 * 是否显示时间组件
	 * @param visible
	 * @return
	 */
	public MyDialog setTimePickerVisible(boolean visible, Boolean is24HourView){
		if(!visible && mDialogTimePicker == null){
			return this;
		}
		initTimePicker();
		mDialogBodyLayout.setVisibility(View.GONE);
		mDialogTimePicker.setVisibility(visible? View.VISIBLE : View.GONE);
		mDialogTimePicker.setIs24HourView(is24HourView);
		return this;
	}
	
	/** 
	 * TODO 此方法所实现的功能未完成，待改
	 * @param calendar	Calendar
	 * @param year						年
	 * @param monthOfYear		月
	 * @param dayOfMonth		日
	 * @param hourOfDay			时
	 * @param minute					分
	 */
	@SuppressWarnings("unused")
	private void setCalendarData(Calendar calendar, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute){
		if(calendar == null){
			return;
		}
		if(year != -1){
			calendar.set(Calendar.YEAR, year); 										// 设置年
		}
		if(monthOfYear != -1){
			calendar.set(Calendar.MONTH, monthOfYear); 					// 设置月
		}
		if(dayOfMonth != -1){
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); 		// 设置日
		}
		if(hourOfDay != -1){
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay); 			// 设置时
		}
		if(minute != -1){
			calendar.set(Calendar.MINUTE, minute); 								// 设置分
		}
		calendar.set(Calendar.SECOND, 0);											// 设置秒
		calendar.set(Calendar.MILLISECOND, 0);									// 设置毫秒
	}

	/**
	 * 设置消息文字颜色
	 * @param color
	 * @return
	 */
	public MyDialog setMessageTextColor(int color){
		if (mDialogMessage != null) {
			mDialogMessage.setTextColor(color);
		}
		return this;
	}

	@Override
	public void setCancelable(boolean isCancelable){
		this.isCancelable = isCancelable;
		super.setCancelable(isCancelable);
	}

	@Override
	public void show(){
		if(context instanceof Activity){
			if (((Activity) context).isFinishing()) {
				return;
			}
		}
		super.show();
	}

	/**
	 * 对话框按钮点击事件监听器 
	 */
	public interface OnClickDialogListener{
		/**
		 * 当点击按钮时回调。必须在该方法中dismiss()，否则当点击按钮时，默认不关闭
		 * @param view 被点击的按钮,该按钮被点击后会默认禁用点击Button.setClickable(false);
		 * @param dialog MyDialog实例
		 */
		void onClick(View view, MyDialog dialog);
	}
	
	/**
	 * 密码框布局点击监听器
	 *
	 * <p>Created by wuguangxin on 15/9/9 </p>
	 */
	public interface OnClickPasswordEditTextsListener {
		/**
		 * 当密码输入框显示时，并点击输入框布局时，回调此方法，可以用来控制软键盘的弹出和隐藏
		 */
		void onClick();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (AndroidUtils.isPressedKeycodeBack(keyCode, event)) {
			if (isCancelable) {
				this.cancel();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 设置当对话框调用dismiss()或者cancel()后解除禁用的View
	 * @param enableView
	 */
	public void setEnableView(View enableView){
		this.enableView = enableView;
		if(enableView != null){
			enableView.setEnabled(false);
		}
	}
	
	/**
	 * 当对话框消失或取消或隐藏时，让相应的点击的按钮可点击
	 */
	private void resetView(){
		if(mNegativeButton != null){
			mNegativeButton.setClickable(true);
		}
		if(mPositiveButton != null){
			mPositiveButton.setClickable(true);
		}
		if(enableView != null){
			enableView.setEnabled(true);
		}
	}
	
	@Override
	public void cancel(){
		super.cancel();
		resetView();
	}
	
	@Override
	public void dismiss(){
		super.dismiss();
		resetView();
	}
	
	@Override
	public void hide(){
		super.hide();
		resetView();
	}
}
