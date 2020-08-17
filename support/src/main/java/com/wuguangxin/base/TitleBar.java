package com.wuguangxin.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wuguangxin.support.R;
import com.wuguangxin.utils.AndroidUtils;

import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 标题栏管理器
 *
 * Created by wuguangxin on 14/10/16.
 */
public class TitleBar {
    private Context mContext;
    private Activity mActivity;
    private Resources mResources;
//    private AttrResources mAttrResources;
    // 总布局
    private ConstraintLayout mTitleBarLayout;
    private View mDividerView; // 标题栏底部分割线
    // 网页加载进度条
    private ProgressBar mProgressBar;
    // 左边布局
    private LinearLayout mLeftLayout;
    private ImageView mBackView;
    private TextView mLeftTextView;
    private ImageView mCloseView;
    // 中间布局
    private ConstraintLayout mCenterLayout;
    private TextView mTitleView;
    private ImageView mLoadView;
    private AnimationDrawable mLoadAnim;
    //右边布局
    private LinearLayout mRightLayout;
    private ImageView mMenuView;
    private TextView mSettingView;

    private int titlebarPaddingTop;
    private int systemStatusBarHeight;


    /** 默认标题栏样式 */
//    private Theme mTitleBarTheme = Theme.BLACK;
    private int titleBarHeight;

    TitleBar(Context context){
        this.mContext = context;
        this.mActivity = (Activity) context;
        this.mResources = context.getResources();
//        this.mAttrResources = new AttrResources(context);
        this.titleBarHeight = mResources.getDimensionPixelSize(R.dimen.xin_titlebar_height);
        this.titlebarPaddingTop = getTitlebarPaddingTop();
        this.systemStatusBarHeight = AndroidUtils.getStatusBarHeight(context);
        initView();
        setListener();
//        setTheme(mTitleBarTheme);
    }

    public void initView(){
        mTitleBarLayout = mActivity.findViewById(R.id.xin_titlebar_layout);
        // ****************************左边布局**************************************************
        mLeftLayout = mActivity.findViewById(R.id.xin_titlebar_left_layout);
        mBackView = mActivity.findViewById(R.id.xin_titlebar_left_back_view);
        mCloseView = mActivity.findViewById(R.id.xin_titlebar_left_close_view);
        mLeftTextView = mActivity.findViewById(R.id.xin_titlebar_left_text_view);
        // ****************************中间布局**************************************************
        mCenterLayout = mActivity.findViewById(R.id.xin_titlebar_center_layout);
        mTitleView = mActivity.findViewById(R.id.xin_titlebar_title_view);
        mLoadView = mActivity.findViewById(R.id.xin_titlebar_load_view);
        mLoadAnim = (AnimationDrawable) mLoadView.getDrawable();
        // ****************************右边布局**************************************************
        mRightLayout = mActivity.findViewById(R.id.xin_titlebar_right_layout);
        mSettingView = mActivity.findViewById(R.id.xin_titlebar_right_setting);
        mMenuView = mActivity.findViewById(R.id.xin_titlebar_right_menu);
        // ****************************标题栏里的WebView加载进度条*********************************
        mProgressBar = mActivity.findViewById(R.id.xin_titlebar_progress_bar);
        mDividerView = mActivity.findViewById(R.id.xin_titlebar_divider);

        mDividerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        // ******************左边布局******************
        mLeftLayout.setVisibility(View.VISIBLE);
        mBackView.setVisibility(View.VISIBLE);
        mCloseView.setVisibility(View.GONE);
        mLeftTextView.setVisibility(View.VISIBLE);
        // ******************中间布局******************
        mCenterLayout.setVisibility(View.VISIBLE);
        mTitleView.setVisibility(View.VISIBLE);
        mLoadView.setVisibility(View.GONE);
        // ******************右边布局******************
        mRightLayout.setVisibility(View.VISIBLE);
        mSettingView.setVisibility(View.GONE);
        mMenuView.setVisibility(View.GONE);

        updateTitleToCenter();
    }

    private void setListener(){
        mBackView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mActivity.finish();
            }
        });
        mCloseView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mActivity.finish();
            }
        });
    }

    /**
     * 标题栏颜色样式
     *
     * Created by wuguangxin on 15/9/21.
     */
    public enum Theme {
        /** 默认背景主题 */
        DEFAULT(0),
        /** 白色背景主题 */
        WHITE(1),
        /** 蓝色背景主题 */
        BLUE(2),
        /** 黑色背景主题 */
        BLACK(3),
        /** 透明背景主题 */
        TRANSPARENT(-1);

        private int value;

        public int getValue() {
            return value;
        }

        Theme(int value) {
            this.value = value;
        }
    }

//    /**
//     * 设置标题LAN主题
//     * @param titleBarTheme  {@link Theme}
//     */
//    public void setTheme(Theme titleBarTheme){
//        this.mTitleBarTheme = titleBarTheme;
//        int titleBarBackground = mAttrResources.getColor(R.attr.titlebar_background);
//        int settingTextColor = mAttrResources.getColor(R.attr.titlebar_setting_color);
//        int titleTextColor = mAttrResources.getColor(R.attr.titlebar_title_color);
//        Drawable titlebarBackIcon = mAttrResources.getDrawable(R.attr.titlebar_back_icon);
//        mTitleBarLayout.setBackgroundColor(titleBarBackground);
//        mDividerView.setVisibility(View.VISIBLE);
//        mBackView.setImageDrawable(titlebarBackIcon);
//        mLeftTextView.setTextColor(titleTextColor);
//        mTitleView.setTextColor(titleTextColor);
//        mSettingView.setTextColor(settingTextColor);
//    }

    /**
     * 设置标题栏标题显示状态
     * @param visibility 是否可见
     */
    public void setVisibility(boolean visibility){
        setViewVisibility(mTitleBarLayout, visibility);
    }

    /**
     * 设置标题栏背景颜色
     * @param color 颜色字符串（如#FFFFFF）
     */
    public void setBackground(String color){
        if(color != null && color.startsWith("#")){
            setBackground(android.graphics.Color.parseColor(color));
        }
    }

    /**
     * 设置标题栏背景颜色
     * @param color 颜色
     */
    public void setBackground(int color){
        mTitleBarLayout.setBackgroundColor(color);
    }

    /**
     * 设置标题栏背景图片
     * @param resId 图片ID
     */
    public void setBackgroundResource(int resId){
        mTitleBarLayout.setBackgroundResource(resId);
    }

    /**
     * 设置标题栏下方水平分割线显示状态
     * @param visibility 是否可见
     */
    public void setDividerVisibility(boolean visibility){
        setViewVisibility(mDividerView, visibility);
    }

    /**
     * 设置标题栏标题TextView显示状态
     * @param visibility 是否可见
     */
    public void setTitleViewVisibility(boolean visibility){
        setViewVisibility(mTitleView, visibility);
    }

    /**
     * 设置标题栏下方水平分割线颜色
     * @param color 颜色，如Color.GARY
     */
    public void setDividerBackgroundColor(int color){
        mDividerView.setBackgroundColor(color);
    }

    /**
     * 设置标题栏下方水平分割线高度
     * @param size 分割线高度(单位为px)
     */
    public void setDividerHeight(int size){
        mDividerView.getLayoutParams().height = size;
    }

    /**
     * 获取标题栏高度（px单位）
     * @return
     */
    public int getTitleBarHeightPixelSize(){
        if(titleBarHeight == 0){
            titleBarHeight = mContext.getResources().getDimensionPixelSize(R.dimen.xin_titlebar_height);
        }
        return titleBarHeight;
    }

    /**
     * 获取标题栏 paddingTop 值（px单位）
     * @return
     */
    public int getTitlebarPaddingTop() {
        if (Build.VERSION.SDK_INT >= 19) {
            if (systemStatusBarHeight == 0)
                systemStatusBarHeight = AndroidUtils.getStatusBarHeight(mContext);
            titlebarPaddingTop = systemStatusBarHeight;
        } else {
            titlebarPaddingTop = 0;
        }
        return titlebarPaddingTop;
    }

    /**
     * 获取标题栏高度
     * @param pixelSize （px单位）
     * @return
     */
    public void setTitleBarHeightPixelSize(int pixelSize){
        this.titleBarHeight = pixelSize;
    }

    /**
     * 设置标题区域布局显示状态。如果显示标题，将隐藏搜索框并且显示右边的菜单
     * @param visibility
     */
    public void setTitleVisibility(boolean visibility){
        setViewVisibility(mCenterLayout, visibility);
        updateTitleToCenter();
    }

    /**
     * 设置标题
     * @param resId
     */
    public void setTitle(@StringRes int resId){
        mTitleView.setText(resId);
        setVisibility(true);
    }

    /**
     * 设置标题
     * @param text
     */
    public void setTitle(CharSequence text){
        mTitleView.setText(text);
        setVisibility(true);
    }

    /**
     * 设置标题文字颜色
     * @param color 如 Color.DEFAULT
     */
    public void setTitleColor(int color){
        if(mTitleView != null){
            mTitleView.setTextColor(color);
        }
    }

    /**
     * 设置“设置”按钮的文字
     * @param resId
     */
    public void setSetting(int resId){
        mSettingView.setText(resId);
        setSettingVisibility(true);
    }

    /**
     * 设置“设置”按钮的文字
     * @param text
     */
    public void setSetting(CharSequence text){
        mSettingView.setText(text);
        setSettingVisibility(true);
    }

    /**
     * 设置标题栏Menu菜单按钮显示状态
     * @param visibility 是否显示
     */
    public void setMenuVisibility(boolean visibility){
        setViewVisibility(mMenuView, visibility);
        updateTitleToCenter();
    }

    /**
     * 显示或隐藏设置按钮
     * @param visibility
     */
    public void setSettingVisibility(boolean visibility){
        setViewVisibility(mSettingView, visibility);
        updateTitleToCenter();
    }

    /**
     * 设置标题栏返回按钮是否可见
     * @param visibility
     */
    public void setBackVisibility(boolean visibility){
        setViewVisibility(mBackView, visibility);
        updateTitleToCenter();
    }

    /**
     * 设置标题栏左边文本按钮是否可见
     * @param visibility
     */
    public void setLeftTextVisibility(boolean visibility){
        setViewVisibility(mLeftTextView, visibility);
        updateTitleToCenter();
    }

    /**
     * 设置标题栏左边的关闭按钮是否可见
     * @param visibility
     */
    public void setCloseVisibility(boolean visibility){
        setViewVisibility(mCloseView, visibility);
        updateTitleToCenter();
    }

    /**
     * 设置标题返回按钮的事件监听器
     * @param onClickListener
     */
    public void setBackListener(View.OnClickListener onClickListener){
        mBackView.setOnClickListener(onClickListener);
    }


    /**
     * 设置标题栏左边文本按钮的事件监听器
     * @param onClickListener
     */
    public void setLeftTextListener(View.OnClickListener onClickListener){
        mLeftTextView.setOnClickListener(onClickListener);
    }

    /**
     * 设置标题返回按钮的事件监听器
     * @param onClickListener
     */
    public void setCloseListener(View.OnClickListener onClickListener){
        mCloseView.setOnClickListener(onClickListener);
    }

    /**
     * 控制View显示状态
     * @param view
     * @param visibility
     */
    private void setViewVisibility(View view, boolean visibility){
        if(view != null){
            view.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 保证title文字居中
     */
    private void updateTitleToCenter() {
        int maxWidth = Math.max(getLeftLayout().getMeasuredWidth(), getRightLayout().getMeasuredWidth());
//        getLeftLayout().setMinimumWidth(maxWidth);
//        getRightLayout().setMinimumWidth(maxWidth);
        maxWidth = Math.max(maxWidth, mResources.getDimensionPixelSize(R.dimen.xin_titlebar_menu_minWidth));
        mCenterLayout.setPadding(maxWidth, 0, maxWidth, 0);
    }

    /**
     * 设置标题栏右边"设置"按钮的点击事件
     * @param onClickListener 点击事件
     */
    public void setSettingListener(View.OnClickListener onClickListener){
        mSettingView.setOnClickListener(onClickListener);
    }

    /**
     * 设置标题栏右边"设置"按钮的点击事件
     * @param text 按钮文字
     * @param onClickListener 按钮监听器
     */
    public void setSettingListener(CharSequence text, View.OnClickListener onClickListener){
        mSettingView.setOnClickListener(onClickListener);
        setSetting(text);
    }

    /**
     * 设置标题栏右边菜单按钮的点击事件
     * @param onClickListener
     */
    public void setMenuListener(View.OnClickListener onClickListener){
        mMenuView.setOnClickListener(onClickListener);
    }

    /**
     * 设置标题栏右边菜单menu1的点击事件
     * @param drawableRes 按钮背景资源ID
     * @param onClickListener
     */
    public void setMenuListener(int drawableRes, View.OnClickListener onClickListener){
        mMenuView.setOnClickListener(onClickListener);
        mMenuView.setImageResource(drawableRes);
        setMenuVisibility(true);
    }

    /**
     * 设置标题栏右边菜单按钮的背景
     * @param resId 按钮背景资源ID
     */
    public void setMenuResource(int resId){
        mMenuView.setImageResource(resId);
        setMenuVisibility(true);
    }

    /**
     * 设置标题栏上“加载中”动画的显示隐藏
     */
    public void setLoadAnimVisible(boolean visible){
        if(visible){
            // 显示
            if (mTitleBarLayout.getVisibility() == View.VISIBLE) {
                mLoadView.setVisibility(View.VISIBLE);
                if(mLoadAnim != null && !mLoadAnim.isRunning()){
                    mLoadAnim.start();
                }
            }
        } else {
            // 隐藏
            mLoadView.setVisibility(View.GONE);
            if(mLoadAnim != null && mLoadAnim.isRunning()){
                mLoadAnim.stop();
            }
        }
    }

    public void addCenterLayout(View view){
        if(mCenterLayout != null && view != null){
            mCenterLayout.removeAllViews();
            mCenterLayout.addView(view);
        }
    }

    /**
     * 设置标题栏上的WebView加载进度条值
     * @param progress 进度值 0-100
     */
    public ProgressBar setProgressValue(int progress){
        if(progress < 0) progress = 0;
        if(progress > 100) progress = 100;
        if(mProgressBar != null){
            mProgressBar.setProgress(progress);
        }
        return mProgressBar;
    }

    /**
     * 设置标题栏上的WebView加载进度可见状态
     * @param visible
     */
    public ProgressBar setProgressBarVisible(boolean visible){
        if(mProgressBar != null){
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        return mProgressBar;
    }


    // ***************************************************************************
    // 				getter / setter
    // ***************************************************************************

    public ConstraintLayout getTitleBarLayout(){
        return mTitleBarLayout;
    }

    // ************左***************************************************************

    public LinearLayout getLeftLayout() {
        return mLeftLayout;
    }

    public ImageView getBackView() {
        return mBackView;
    }

    public TextView getLeftTextView() {
        return mLeftTextView;
    }

    public ImageView getCloseView() {
        return mCloseView;
    }

    // ************中***************************************************************

    public ConstraintLayout getCenterLayout(){
        return mCenterLayout;
    }

    public ImageView getLoadView() {
        return mLoadView;
    }

    public TextView getTitleView(){
        return mTitleView;
    }

    // ************右***************************************************************

    public LinearLayout getRightLayout() {
        return mRightLayout;
    }

    public TextView getSettingView() {
        return mSettingView;
    }

    /**
     * 获取标题栏右边菜单按钮ImageView
     * @return
     */
    public ImageView getMenuView(){
        return mMenuView;
    }

    public ImageView getLoadAnimView(){
        return mLoadView;
    }

    /**
     * 获取标题栏分割线View
     * @return
     */
    public View getDividerView() {
        return mDividerView;
    }

}
