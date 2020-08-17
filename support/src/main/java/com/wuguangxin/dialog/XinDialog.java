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
import android.util.Log;
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

import com.wuguangxin.support.R;
import com.wuguangxin.view.MyGridView;
import com.wuguangxin.view.MyListView;

import java.util.Calendar;

import androidx.annotation.StringRes;

/**
 * 自定义对话框
 * <p>Created by wuguangxin on 14/8/29 </p>
 */
@SuppressLint("HandlerLeak")
public class XinDialog extends Dialog implements DialogInterface {
    private View mDialogLayout;
    private RelativeLayout mDialogTitleLayout;  // 对话框标题布局
    private ScrollView mDialogBodyLayout;  // 滚动布局
    private RelativeLayout mDialogBodyPasswordEditTextLayout; // 输入密码布局
    private LinearLayout mDialogBodyUserLayout; // 用户自定义的view布局显示区
    private View mDialogTopLine;
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
    private CharSequence title;
    private CharSequence message;
    private CharSequence menuLeftText; // 左边按钮文字
    private CharSequence menuRightText; // 右边按钮文字
    private boolean isCancelable = true; // 是否可以取消

    private static final int MIN_CLICK_DELAY_TIME = 700;
    private static long lastClickTime;
    private static int viewId;

    public XinDialog(Context context) {
        this(context, null);
    }

    /**
     * 构造一个MyDialog对话框
     *
     * @param context Activity上下文
     * @param title 对话框标题
     */
    public XinDialog(Context context, CharSequence title) {
        this(context, title, null);
    }

    /**
     * 构造一个MyDialog对话框
     *
     * @param context Activity上下文
     * @param title 对话框标题
     * @param message 对话框正文消息
     */
    public XinDialog(Context context, CharSequence title, CharSequence message) {
        super(context, R.style.xin_loading_dialog);
        this.context = context;
        this.title = title;
        this.message = message;
        initView();
    }

    /**
     * 构造一个MyDialog对话框
     *
     * @param context Activity上下文
     * @param themeResId 主题ID,如 AlertDialog.THEME_HOLO_LIGHT
     */
    public XinDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
    }

    /**
     * 构建一个新的MyDialog实例，
     *
     * @param context 上下文
     * @return
     */
    public static XinDialog with(Context context) {
        return XinDialog.with(context, null);
    }

    /**
     * 构建一个新的MyDialog实例，
     *
     * @param context 上下文
     * @param needCancelOfDialog 传入需要关闭的MyDialog实例，在构建新的MyDialog之前会自动调用needCancelOfDialog的cancel()方法。
     * @return
     */
    public static XinDialog with(Context context, XinDialog needCancelOfDialog) {
        if (context == null) throw new IllegalArgumentException("context cannot be null");
        if (needCancelOfDialog != null) needCancelOfDialog.cancel();
        return new XinDialog(context);
    }

    /**
     * 初始化对话框
     */
    @SuppressLint("InflateParams")
    private void initView() {
        inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialogLayout = inflater.inflate(R.layout.xin_dialog_layout, null);
        mDialogTitleLayout = mDialogLayout.findViewById(R.id.df_dialog_title_layout);
        mDialogBodyLayout = mDialogLayout.findViewById(R.id.df_dialog_body_layout);
        mDialogBodyPasswordEditTextLayout = mDialogLayout.findViewById(R.id.xin_dialog_password_edittext_layout);
        mDialogBodyUserLayout = mDialogLayout.findViewById(R.id.df_dialog_body_user_layout);
        mDialogTopLine = mDialogLayout.findViewById(R.id.dialog_top_line);  //分割线
        mDialogTitle = mDialogLayout.findViewById(R.id.dialog_top_title);  //标题
        mDialogMenuLeft = mDialogLayout.findViewById(R.id.dialog_top_left);  // 左边按钮
        mDialogMenuRight = mDialogLayout.findViewById(R.id.dialog_top_right);  // 右边按钮
        mDialogMessage = mDialogLayout.findViewById(R.id.df_dialog_message);  //消息内容
        mDialogListView = mDialogLayout.findViewById(R.id.df_dialog_listview_default);  // 默认listView
        mDialogGridView = mDialogLayout.findViewById(R.id.df_dialog_gridview_default);  // 默认listView
        mDialogButtonLayout = mDialogLayout.findViewById(R.id.df_dialog_button_layout); // 按钮布局
        mPositiveButton = mDialogLayout.findViewById(R.id.df_dialog_button_positive); // 确认按钮
        mNegativeButton = mDialogLayout.findViewById(R.id.df_dialog_button_negative); //否认按钮

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
    private void initTimePicker() {
        if (mDialogTimePicker == null) {
            mDialogTimePicker = mDialogLayout.findViewById(R.id.dialog_timepicker);  // 时间组件
        }
    }

    /**
     * 初始化日期组件
     */
    private void initDatePicker() {
        if (mDialogDatePicker == null) {
            mDialogDatePicker = mDialogLayout.findViewById(R.id.dialog_datepicker);  // 日期组件
        }
    }

    /**
     * 设置对话框标题左边按钮监听器 <br>
     * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
     * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
     *
     * @param text 按钮文字
     * @param onClickDialogListener 按钮监听器
     * @return XinDialog
     */
    public XinDialog setMenuLeft(CharSequence text, final OnClickDialogListener onClickDialogListener) {
        this.menuLeftText = text;
        mDialogMenuLeft.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(menuLeftText)) {
            mDialogMenuLeft.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            mDialogMenuLeft.setText(menuLeftText);
        }
        mDialogMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFastClick(mDialogMenuLeft)) return;
                if (onClickDialogListener != null) {
                    onClickDialogListener.onClick(view, XinDialog.this);
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
     *
     * @param text 按钮文字
     * @param onClickDialogListener 按钮监听器
     * @return XinDialog
     */
    public XinDialog setMenuRight(CharSequence text, final OnClickDialogListener onClickDialogListener) {
        this.menuRightText = text;
        mDialogMenuRight.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(menuRightText)) {
            mDialogMenuRight.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            mDialogMenuRight.setText(menuRightText);
        }
        mDialogMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFastClick(mDialogMenuRight)) return;
                if (onClickDialogListener != null) {
                    onClickDialogListener.onClick(view, XinDialog.this);
                } else {
                    dismiss();
                }
            }
        });
        return this;
    }

    /**
     * 设置对话框的取消按钮监听器 <br>
     *
     * @param text 取消按钮文字
     * @param isFinish 点击按钮关闭Activity
     * @return XinDialog
     */
    public XinDialog setNegativeButton(CharSequence text, boolean isFinish) {
        return setNegativeButton(text, isFinish, null);
    }

    /**
     * 设置对话框的取消按钮监听器，点击按钮默认调用dismiss()。
     * @param text 按钮文字
     * @return XinDialog
     */
    public XinDialog setNegativeButton(CharSequence text) {
        return setNegativeButton(text, null);
    }

    /**
     * 设置对话框的取消按钮监听器 <br>
     * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
     * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
     *
     * @param text 按钮文字
     * @param onClickDialogListener 监听器
     * @return XinDialog
     */
    public XinDialog setNegativeButton(CharSequence text, final OnClickDialogListener onClickDialogListener) {
        return setNegativeButton(text, false, onClickDialogListener);
    }

    /**
     * 设置对话框的取消按钮监听器 <br>
     * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
     * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
     * 当点击按钮时，该按钮会默认被禁止点击setClickable(false) 直到对话框回调cancel()、dismiss()、hide()时被重置为true<br>
     *
     * @param text 按钮文字
     * @param isFinish 点击按钮关闭Activity, 当该值为true时，将不执行onClickDialogListener回调
     * @param onClickDialogListener 按钮监听器
     * @return XinDialog
     */
    public XinDialog setNegativeButton(CharSequence text, final boolean isFinish, final OnClickDialogListener onClickDialogListener) {
        if (!TextUtils.isEmpty(text)) {
            mNegativeButton.setVisibility(View.VISIBLE);
            mDialogButtonLayout.setVisibility(View.VISIBLE);
            mNegativeButton.setText(text);
            setButtonStyle(mNegativeButton);
            mNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFastClick()) return;
                    if (onClickDialogListener != null) {
//						mNegativeButton.setClickable(false);
                        onClickDialogListener.onClick(view, XinDialog.this);
                    } else {
                        dismiss();
                        if (isFinish && context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置对话框的确认按钮监听器
     *
     * @param text 确认按钮文字
     * @param isFinish 点击按钮关闭Activity
     * @return XinDialog
     */
    public XinDialog setPositiveButton(CharSequence text, boolean isFinish) {
        return setPositiveButton(text, isFinish, null);
    }

    /**
     * 设置对话框确认按钮监听器。点击按钮默认调用dismiss()。<br>
     * @param text 按钮文字
     * @return XinDialog
     */
    public XinDialog setPositiveButton(CharSequence text) {
        return setPositiveButton(text, null);
    }

    /**
     * 设置对话框确认按钮监听器。<br>
     * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
     * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
     *
     * @param text 按钮文字
     * @param onClickDialogListener 监听器
     * @return XinDialog
     */
    public XinDialog setPositiveButton(CharSequence text, final OnClickDialogListener onClickDialogListener) {
        return setPositiveButton(text, false, onClickDialogListener);
    }

    /**
     * 设置对话框确认按钮监听器。<br>
     * 当text不为null但onClickListener为null时，点击按钮默认调用dismiss()。<br>
     * 注：想要在点击按钮时自动关闭对话框，则在监听器的onClick()方法中利用返回的dialog调用.dismiss()，否则对话框仍然存在。<br>
     *
     * @param text 按钮文字
     * @param isFinish 点击按钮关闭Activity,当该值为true时，将不执行onClickDialogListener回调
     * @param onClickDialogListener 监听器
     * @return XinDialog
     */
    private XinDialog setPositiveButton(CharSequence text, final boolean isFinish, final OnClickDialogListener onClickDialogListener) {
        if (!TextUtils.isEmpty(text)) {
            mPositiveButton.setVisibility(View.VISIBLE);
            mDialogButtonLayout.setVisibility(View.VISIBLE);
            mPositiveButton.setText(text);
            setButtonStyle(mPositiveButton);
            mPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFastClick()) return;
                    if (onClickDialogListener != null) {
                        onClickDialogListener.onClick(view, XinDialog.this);
                    } else {
                        dismiss();
                        if (isFinish && context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置按钮样式
     *
     * @param button 按钮TextView
     */
    private void setButtonStyle(TextView button) {
        // 两按钮都显示，则显示中间分割线
        if (mPositiveButton.getVisibility() == View.VISIBLE && mNegativeButton.getVisibility() == View.VISIBLE) {
            mNegativeButton.setBackgroundResource(R.drawable.xin_dialog_bottom_button_left);
            mPositiveButton.setBackgroundResource(R.drawable.xin_dialog_bottom_button_right);
        } else {
            button.setBackgroundResource(R.drawable.xin_dialog_bottom_button_single);
        }
    }

    /**
     * 设置NegativeButton按钮是否可用
     *
     * @param enabled NegativeButton按钮是否可用
     */
    public void setNegativeButtonEnabled(boolean enabled) {
        if (mNegativeButton != null) {
            mNegativeButton.setEnabled(enabled);
        }
    }

    /**
     * 设置ConfirmButton按钮是否可用
     *
     * @param enabled ConfirmButton按钮是否可用
     */
    public void setPositiveButtonEnabled(boolean enabled) {
        if (mPositiveButton != null) {
            mPositiveButton.setEnabled(enabled);
        }
    }

    /**
     * 设置对话框标题
     *
     * @param title 对话框标题
     * @return XinDialog
     */
    public XinDialog setTitle(String title) {
        if (title != null) {
            this.title = title;
            mDialogTitle.setText(title); // 标题
            mDialogTitleLayout.setVisibility(View.VISIBLE);
            if (isShowBackView) {
                mDialogMenuLeft.setVisibility(View.VISIBLE);
            }
        }
        super.setTitle(title);
        return this;
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        this.setTitle(context.getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        this.setTitle(title != null ? title.toString() : "");
    }

    /**
     * 设置标题对齐位置
     *
     * @param gravity @see android.view.Gravity
     * @return XinDialog
     */
    public XinDialog setTitleGravity(int gravity) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mDialogTitle.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        mDialogTitle.setGravity(gravity);
        return this;
    }

    /**
     * 设置对话框正文内容消息
     *
     * @param message 对话框正文内容消息
     * @return XinDialog
     */
    public XinDialog setMessage(@StringRes int message) {
        return setMessage(context.getString(message));
    }

    /**
     * 设置对话框正文内容消息
     *
     * @param message 对话框正文内容消息
     * @return XinDialog
     */
    public XinDialog setMessage(CharSequence message) {
        if (message != null) {
            this.message = message.toString();
            mDialogMessage.setText(message); //消息正文
            mDialogMessage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 获取消息文本框
     *
     * @return 消息文本框
     */
    public TextView getMessageView() {
        return mDialogMessage;
    }

    /**
     * 获取标题文本
     *
     * @return 标题文本
     */
    public TextView getTitleView() {
        return mDialogTitle;
    }

    /**
     * 获取密码框焦点布局
     *
     * @return 密码框焦点布局
     */
    public View getPasswordFocusLayout() {
        return mFocusLayout;
    }

    /**
     * 获取对话框中的ListView
     *
     * @return 对话框中的ListView
     */
    public MyListView getListView() {
        return mDialogListView;
    }

    /**
     * 获取对话框中的GridView
     *
     * @return 对话框中的GridView
     */
    public MyGridView getGridView() {
        return mDialogGridView;
    }

    /**
     * 获取对话框中确认按钮
     *
     * @return 对话框中确认按钮
     */
    public TextView getPositiveButton() {
        return mPositiveButton;
    }

    /**
     * 获取对话框中取消按钮
     *
     * @return 对话框中取消按钮
     */
    public TextView getNegativeButton() {
        return mNegativeButton;
    }

    /**
     * 设置GridView适配器
     *
     * @param adapter GridView适配器
     * @return XinDialog
     */
    public XinDialog setGridViewAdapter(BaseAdapter adapter) {
        if (adapter != null) {
            mDialogBodyUserLayout.setVisibility(View.GONE);
            mDialogGridView.setVisibility(View.VISIBLE);
            mDialogGridView.setAdapter(adapter);
        }
        return this;
    }

    /**
     * 更新GridView适配器
     *
     * @return XinDialog
     */
    public XinDialog notifyGridView() {
        if (mDialogGridView != null) {
            mDialogGridView.getAdapter().notify();
        }
        return this;
    }

    /**
     * 设置列表适配器
     *
     * @param adapter ListView列表适配器
     * @return XinDialog
     */
    public XinDialog setListViewAdapter(BaseAdapter adapter) {
        if (adapter != null) {
            mDialogBodyUserLayout.setVisibility(View.GONE);
            mDialogListView.setVisibility(View.VISIBLE);
            mDialogListView.setAdapter(adapter);
        }
        return this;
    }

    /**
     * 更新列表适配器
     *
     * @return XinDialog
     */
    public XinDialog notifyListView() {
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

    /**
     * 判断软键盘是否是弹出状态
     *
     * @param context 上下文
     * @return 软键盘是否显示
     */
    public static boolean isShowSoftKey(Context context) {
        return ((Activity) context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case View.VISIBLE:
                if (!isShowSoftKey(context)) {
                    InputMethodManager immShow = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    immShow.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                }
                break;
            case View.GONE:
                if (isShowSoftKey(context)) {
                    InputMethodManager immHide = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    immHide.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;
            }
        }
    };

    /**
     * 设置密码输入框组件显示状态
     *
     * @param visible 是否可见 false-隐藏 true-显示
     * @return XinDialog
     */
    public XinDialog setPasswordVisibility(boolean visible) {
        if (mDialogBodyPasswordEditTextLayout != null) {
            if (visible) {
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
     *
     * @param descString 交易密码的说明
     * @return XinDialog
     */
    public XinDialog setPasswordDesc(String descString) {
        if (mDialogBodyPasswordEditTextLayout == null) {
            setPasswordVisibility(true);
        }
        if (mPayPasswordDesc != null) {
            mPayPasswordDesc.setText(descString);
            mPayPasswordDesc.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 获取交易密码描述TextView
     *
     * @return TextView
     */
    public TextView getPasswordDescView() {
        if (mPayPasswordDesc != null) {
            return mPayPasswordDesc;
        }
        return null;
    }


    /**
     * 判断是否显示软键盘
     *
     * @return 是否显示软键盘
     */
    public boolean isSoftKeyboardVisibility() {
        return isSoftKeyboardVisibility;
    }

    /**
     * 设置是否显示软键盘
     *
     * @param isSoftKeyboardVisibility 是否显示
     */
    public void setSoftKeyboardVisibility(boolean isSoftKeyboardVisibility) {
        this.isSoftKeyboardVisibility = isSoftKeyboardVisibility;
    }

    /**
     * 获取输入的密码（如果设置显示了密码输入框才生效）
     *
     * @return 输入的密码
     */
    public String getPassword() {
        if (mPayPassword != null) {
            return mPayPassword.getText().toString();
        }
        return null;
    }

    /**
     * 初始化6个密码框布局
     *
     * @param visible 是否可见
     */
    @SuppressLint("InflateParams")
    private void initPasswordEditTextLayout(boolean visible) {
        mPayPasswordDesc = findViewById(R.id.xin_dialog_password_desc);
        mPayPasswordDesc.setVisibility(View.GONE);
        mPayPassword = findViewById(R.id.xin_dialog_password_edittext);
        mPayPassword1 = findViewById(R.id.xin_dialog_password_edittext_1);
        mPayPassword2 = findViewById(R.id.xin_dialog_password_edittext_2);
        mPayPassword3 = findViewById(R.id.xin_dialog_password_edittext_3);
        mPayPassword4 = findViewById(R.id.xin_dialog_password_edittext_4);
        mPayPassword5 = findViewById(R.id.xin_dialog_password_edittext_5);
        mPayPassword6 = findViewById(R.id.xin_dialog_password_edittext_6);
        payPasswordTextViewArrs = new TextView[]{mPayPassword1, mPayPassword2, mPayPassword3, mPayPassword4, mPayPassword5, mPayPassword6};
        mFocusLayout = findViewById(R.id.xin_dialog_password_edittext_focus_layout);
        mFocusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClientInputMethod();
            }
        });
        mPayPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (payPasswordTextViewArrs != null && start < payPasswordTextViewArrs.length) {
                    payPasswordTextViewArrs[start].setText(before == 0 ? "●" : "");
                }
                setPositiveButtonEnabled(s.length() == 6);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 控制软键盘显示状态
     */
    protected void setOnClientInputMethod() {
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
     *
     * @return 软键盘是否弹出
     */
    public boolean isShowSoftKey() {
        return getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
    }

    /**
     * 定位光标位置到EditText文本的末尾
     *
     * @param mEditText EditText
     */
    public static void setFocusPosition(EditText mEditText) {
        mEditText.requestFocus();
        Editable text = mEditText.getText();
        Selection.setSelection(text, text.length());
    }

    /**
     * 设置对话框中密码输入框监听器
     *
     * @param onClickPasswordEditTextsListener 监听器
     * @return XinDialog
     */
    public XinDialog setOnClickPasswordEditTextsListener(final OnClickPasswordEditTextsListener onClickPasswordEditTextsListener) {
        mFocusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击密码输入框了===============");
                if (onClickPasswordEditTextsListener != null) {
                    onClickPasswordEditTextsListener.onClick();
                }
            }
        });
        return this;
    }

    /**
     * 显示软键盘
     */
    protected void showSoftkey() {
//		if(Utils.isShowSoftKey(context)){
//			return;
//        }
        InputMethodManager immShow = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        immShow.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }
    // *****密码对话框e****************************************************************************************************************

    /**
     * 设置自定义的对话框正文布局
     *
     * @param view 你的布局
     * @return XinDialog
     */
    public XinDialog setView(View view) {
        if (view != null) {
            try {
                LinearLayout parent = (LinearLayout) view.getParent();
                if (parent != null) {
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
     *
     * @return 对话框中ContextView里的Child的个数
     */
    public int getChildCount() {
        if (mDialogBodyUserLayout != null) {
            return mDialogBodyUserLayout.getChildCount();
        }
        return 0;
    }

    /**
     * 设置Padding
     *
     * @param size 大小
     */
    public void setPadding(int size) {
        if (mDialogLayout != null) {
            if (size < 0) {
                size = 0;
            }
            mDialogLayout.setPadding(size, size, size, size);
        }
    }

    /**
     * 设置对话框顶部分割线颜色（若不想显示分割线，设置为透明色即可）
     *
     * @param color 对话框顶部分割线颜色
     * @return XinDialog
     */
    public XinDialog setLineColor(int color) {
        if (mDialogTopLine != null) {
            mDialogTopLine.setBackgroundColor(color);
        }
        return this;
    }

    /**
     * 设置分割线可见状态
     *
     * @param visible 分割线可见状态
     * @return XinDialog
     */
    public XinDialog setLineVisible(int visible) {
        if (mDialogTopLine != null) {
            mDialogTopLine.setVisibility(visible);
        }
        return this;
    }

    /**
     * 设置标题文字颜色
     *
     * @param color 标题文字颜色
     * @return XinDialog
     */
    public XinDialog setTitleTextColor(int color) {
        if (mDialogTitle != null) {
            mDialogTitle.setTextColor(color);
        }
        return this;
    }

    /**
     * 设置标题文字大小
     *
     * @param size 标题文字大小
     * @return XinDialog
     */
    public XinDialog setTitleTextSize(float size) {
        if (mDialogTitle != null) {
            mDialogTitle.setTextSize(size);
        }
        return this;
    }

    /**
     * 设置消息文字大小
     *
     * @param size 消息文字大小
     * @return XinDialog
     */
    public XinDialog setMessageTextSize(float size) {
        if (mDialogMessage != null) {
            mDialogMessage.setTextSize(size);
        }
        return this;
    }

    /**
     * 设置消息文字对齐方式
     *
     * @param gravity 消息文字对齐方式
     * @return XinDialog
     */
    public XinDialog setMessageGravity(int gravity) {
        if (mDialogMessage != null) {
            mDialogMessage.setGravity(gravity);
        }
        return this;
    }

    /**
     * 设置日期组件 变化监听器
     *
     * @param onDateChangedListener 监听器
     * @return XinDialog
     */
    public XinDialog setDataPicker(OnDateChangedListener onDateChangedListener) {
        return setDataPicker(System.currentTimeMillis(), onDateChangedListener);
    }

    /**
     * 设置日期组件 变化监听器
     *
     * @param timeMillis 默认显示的时间
     * @param onDateChangedListener 监听器
     * @return XinDialog
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public XinDialog setDataPicker(long timeMillis, OnDateChangedListener onDateChangedListener) {
        setDataPickerVisible(true);
        Calendar c = Calendar.getInstance(java.util.Locale.CHINA);
        c.setTimeInMillis(timeMillis);
        mDialogDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), onDateChangedListener);
        return this;
    }

    /**
     * 设置时间组件变化监听器
     *
     * @param onTimeChangedListener 监听器
     * @return XinDialog
     */
    public XinDialog setTimePicker(OnTimeChangedListener onTimeChangedListener) {
        return setTimePicker(System.currentTimeMillis(), onTimeChangedListener);
    }

    /**
     * 设置时间组件变化监听器
     *
     * @param timeMillis 默认显示的时间
     * @param onTimeChangedListener 监听器
     * @return XinDialog
     */
    public XinDialog setTimePicker(long timeMillis, OnTimeChangedListener onTimeChangedListener) {
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
     *
     * @param visible 是否显示日期组件
     * @return XinDialog
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public XinDialog setDataPickerVisible(boolean visible) {
        if (!visible && mDialogDatePicker == null) {
            return this;
        }
        initDatePicker();
        mDialogBodyLayout.setVisibility(View.GONE);
        mDialogDatePicker.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mDialogDatePicker.setSpinnersShown(true);
            mDialogDatePicker.setCalendarViewShown(false);
        }
        return this;
    }

    /**
     * 是否显示时间组件
     *
     * @param visible 是否显示时间组件
     * @param is24HourView 是否是24小时
     * @return XinDialog
     */
    public XinDialog setTimePickerVisible(boolean visible, Boolean is24HourView) {
        if (!visible && mDialogTimePicker == null) {
            return this;
        }
        initTimePicker();
        mDialogBodyLayout.setVisibility(View.GONE);
        mDialogTimePicker.setVisibility(visible ? View.VISIBLE : View.GONE);
        mDialogTimePicker.setIs24HourView(is24HourView);
        return this;
    }

    /**
     * 此方法所实现的功能未完成，待改
     *
     * @param calendar Calendar
     * @param year 年
     * @param monthOfYear 月
     * @param dayOfMonth 日
     * @param hourOfDay 时
     * @param minute 分
     */
    private void setCalendarData(Calendar calendar, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        if (calendar == null) return;
        if (year != -1) calendar.set(Calendar.YEAR, year);                        // 设置年
        if (monthOfYear != -1) calendar.set(Calendar.MONTH, monthOfYear);        // 设置月
        if (dayOfMonth != -1) calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);    // 设置日
        if (hourOfDay != -1) calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);        // 设置时
        if (minute != -1) calendar.set(Calendar.MINUTE, minute);                    // 设置分
        calendar.set(Calendar.SECOND, 0);                                        // 设置秒
        calendar.set(Calendar.MILLISECOND, 0);                                    // 设置毫秒
    }

    /**
     * 设置消息文字颜色
     *
     * @param color 消息文字颜色
     * @return XinDialog
     */
    public XinDialog setMessageTextColor(int color) {
        if (mDialogMessage != null) {
            mDialogMessage.setTextColor(color);
        }
        return this;
    }

    @Override
    public void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
        super.setCancelable(isCancelable);
    }

    @Override
    public void show() {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        super.show();
    }

    public XinDialog showDialog() {
        show();
        return this;
    }

    /**
     * 对话框按钮点击事件监听器
     */
    public interface OnClickDialogListener {
        /**
         * 当点击按钮时回调。必须在该方法中dismiss()，否则当点击按钮时，默认不关闭
         *
         * @param view 被点击的按钮,该按钮被点击后会默认禁用点击Button.setClickable(false);
         * @param dialog MyDialog实例
         */
        void onClick(View view, XinDialog dialog);
    }

    /**
     * 密码框布局点击监听器
     * <p>Created by wuguangxin on 15/9/9 </p>
     */
    public interface OnClickPasswordEditTextsListener {
        /**
         * 当密码输入框显示时，并点击输入框布局时，回调此方法，可以用来控制软键盘的弹出和隐藏
         */
        void onClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isCancelable) {
                this.cancel();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置当对话框调用dismiss()或者cancel()后解除禁用的View
     *
     * @param enableView View
     */
    public void setEnableView(View enableView) {
        this.enableView = enableView;
        if (enableView != null) enableView.setEnabled(false);
    }

    /**
     * 当对话框消失或取消或隐藏时，让相应的点击的按钮可点击
     */
    private void resetView() {
        if (mNegativeButton != null) mNegativeButton.setClickable(true);
        if (mPositiveButton != null) mPositiveButton.setClickable(true);
        if (enableView != null) enableView.setEnabled(true);
    }

    public XinDialog cancelDialog(XinDialog dialog) {
        if (dialog == this) throw new IllegalArgumentException("不能传入当前实例对象");
        if (dialog != null) dialog.cancel();
        return this;
    }

    public XinDialog dismissDialog(XinDialog dialog) {
        if (dialog == this) throw new IllegalArgumentException("不能传入当前实例对象");
        if (dialog != null) dialog.dismiss();
        return this;
    }

    @Override
    public void cancel() {
        super.cancel();
        resetView();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        resetView();
    }

    @Override
    public void hide() {
        super.hide();
        resetView();
    }

    /**
     * 是否过快点击
     *
     * @return
     */
    public static boolean isFastClick() {
        return isFastClick(null);
    }

    /**
     * 是否过快点击
     *
     * @param view 被点击的View，如果上一次点击的不是当前点击的View，不限制点击时间
     * @return
     */
    public static boolean isFastClick(View view) {
        long curClickTime = System.currentTimeMillis();

        if (view != null) {
            int CurrentViewId = view.getId();
            if (CurrentViewId != viewId) {
                viewId = CurrentViewId;
                lastClickTime = curClickTime;
                return false;
            }
        }
        boolean flag = false;
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            Log.d("ClickUtils", "点击太快了....");
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
