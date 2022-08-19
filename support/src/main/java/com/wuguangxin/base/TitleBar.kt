package com.wuguangxin.base

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.drawable.AnimationDrawable
import com.wuguangxin.support.R
import android.os.Build
import android.view.View
import android.widget.ImageView
import com.wuguangxin.utils.AndroidUtils
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import com.wuguangxin.support.databinding.XinTitlebarLayoutBinding

/**
 * 标题栏管理器
 *
 * Created by wuguangxin on 14/10/16.
 */
class TitleBar internal constructor(private val context: Context, baseView: View) {
    private val activity: Activity = context as Activity
    private val baseBinding: XinTitlebarLayoutBinding

    // 总布局
    private val titleBarLayout: ConstraintLayout // 网页加载进度条
    private val dividerView: View // 标题栏底部分割线
    // 左边布局
    private val leftLayout: LinearLayout
    private val backView: ImageView
    private val leftTextView: TextView
    private val closeView: ImageView
    // 中间布局
    private val centerLayout: ConstraintLayout
    private val titleView: TextView
    private val loadView: ImageView
    private val loadAnim: AnimationDrawable
    //右边布局
    private val rightLayout: LinearLayout
    private val menuView: ImageView
    private val settingView: TextView
    // 网页加载进度条
    private val progressBar: ProgressBar


    fun getTitleBarLayout(): ConstraintLayout = titleBarLayout
    // ************左**********
    fun getLeftLayout(): LinearLayout = leftLayout
    fun getBackView(): ImageView = backView
    fun getLeftTextView(): TextView = leftTextView
    fun getCloseView(): ImageView = closeView
    // ************中**********
    fun getCenterLayout(): ConstraintLayout = centerLayout
    fun getLoadView(): ImageView = loadView
    fun getTitleView(): TextView = titleView
    // ************右**********
    fun getRightLayout(): LinearLayout = rightLayout
    fun getSettingView(): TextView = settingView
    fun getMenuView(): ImageView = menuView
    fun getLoadAnimView(): ImageView = loadView
    fun getDividerView(): View = dividerView

    private var titlebarPaddingTop = 0
    private var systemStatusBarHeight = 0
    /** 默认标题栏样式  */
    private var titleBarHeight = 0

    init {
        titleBarHeight = context.resources.getDimensionPixelSize(R.dimen.xin_titlebar_height)
        titlebarPaddingTop = getTitlebarPaddingTop()
        systemStatusBarHeight = AndroidUtils.getStatusBarHeight(context)

        titleBarLayout = baseView.findViewById(R.id.xin_titlebar_layout)
        baseBinding = XinTitlebarLayoutBinding.bind(titleBarLayout)


        // 左边
        leftLayout = baseBinding.xinTitlebarLeftLayout
        backView = baseBinding.xinTitlebarLeftBackView
        closeView = baseBinding.xinTitlebarLeftCloseView
        leftTextView = baseBinding.xinTitlebarLeftTextView
        // 中间
        centerLayout = baseBinding.xinTitlebarCenterLayout
        titleView = baseBinding.xinTitlebarTitleView
        loadView = baseBinding.xinTitlebarLoadView
        loadAnim = loadView.drawable as AnimationDrawable
        // 右边
        rightLayout = baseBinding.xinTitlebarRightLayout
        settingView = baseBinding.xinTitlebarRightSetting
        menuView = baseBinding.xinTitlebarRightMenu
        // 加载进度条
        progressBar = baseBinding.xinTitlebarProgressBar
        dividerView = baseBinding.xinTitlebarDivider

        // 初始化显示
        dividerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        // 左边
        leftLayout.visibility = View.VISIBLE
        backView.visibility = View.VISIBLE
        closeView.visibility = View.GONE
        leftTextView.visibility = View.VISIBLE
        // 中间
        centerLayout.visibility = View.VISIBLE
        titleView.visibility = View.VISIBLE
        loadView.visibility = View.GONE
        // 右边
        rightLayout.visibility = View.VISIBLE
        settingView.visibility = View.GONE
        menuView.visibility = View.GONE

        updateTitleToCenter()

        setListener()
    }


    private fun setListener() {
        backView.setOnClickListener { activity.finish() }
        closeView.setOnClickListener { activity.finish() }
    }

    /**
     * 标题栏颜色样式
     *
     * Created by wuguangxin on 15/9/21.
     */
    enum class Theme(val value: Int) {
        /** 默认背景主题  */
        DEFAULT(0),

        /** 白色背景主题  */
        WHITE(1),

        /** 蓝色背景主题  */
        BLUE(2),

        /** 黑色背景主题  */
        BLACK(3),

        /** 透明背景主题  */
        TRANSPARENT(-1);

    }

    /**
     * 设置标题LAN主题
     * @param titleBarTheme  {@link Theme}
     */
    /*fun setTheme(titleBarTheme: Theme){
        this.mTitleBarTheme = titleBarTheme;
        int titleBarBackground = mAttrResources.getColor(R.attr.titlebar_background);
        int settingTextColor = mAttrResources.getColor(R.attr.titlebar_setting_color);
        int titleTextColor = mAttrResources.getColor(R.attr.titlebar_title_color);
        Drawable titlebarBackIcon = mAttrResources.getDrawable(R.attr.titlebar_back_icon);
        titleBarLayout.setBackgroundColor(titleBarBackground);
        dividerView.setVisibility(View.VISIBLE);
        backView.setImageDrawable(titlebarBackIcon);
        leftTextView.setTextColor(titleTextColor);
        titleView.setTextColor(titleTextColor);
        settingView.setTextColor(settingTextColor);
    }*/

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
    //        titleBarLayout.setBackgroundColor(titleBarBackground);
    //        dividerView.setVisibility(View.VISIBLE);
    //        backView.setImageDrawable(titlebarBackIcon);
    //        leftTextView.setTextColor(titleTextColor);
    //        titleView.setTextColor(titleTextColor);
    //        settingView.setTextColor(settingTextColor);
    //    }

    /**
     * 设置标题栏标题显示状态
     * @param visible 是否可见
     */
    fun setVisibility(visible: Boolean) {
        setViewVisibility(titleBarLayout, visible)
    }

    /**
     * 设置标题栏背景颜色
     * @param color 颜色字符串（如#FFFFFF）
     */
    fun setBackground(color: String?) {
        if (color != null && color.startsWith("#")) {
            setBackground(Color.parseColor(color))
        }
    }

    /**
     * 设置标题栏背景颜色
     * @param color 颜色
     */
    fun setBackground(color: Int) {
        titleBarLayout.setBackgroundColor(color)
    }

    /**
     * 设置标题栏背景图片
     * @param resId 图片ID
     */
    fun setBackgroundResource(resId: Int) {
        titleBarLayout.setBackgroundResource(resId)
    }

    /**
     * 设置标题栏下方水平分割线显示状态
     * @param visible 是否可见
     */
    fun setDividerVisibility(visible: Boolean) {
        setViewVisibility(dividerView, visible)
    }

    /**
     * 设置标题栏标题TextView显示状态
     * @param visible 是否可见
     */
    fun setTitleViewVisibility(visible: Boolean) {
        setViewVisibility(titleView, visible)
    }

    /**
     * 设置标题栏下方水平分割线颜色
     * @param color 颜色，如Color.GARY
     */
    fun setDividerBackgroundColor(color: Int) {
        dividerView.setBackgroundColor(color)
    }

    /**
     * 设置标题栏下方水平分割线高度
     * @param size 分割线高度(单位为px)
     */
    fun setDividerHeight(size: Int) {
        dividerView.layoutParams.height = size
    }

    /**
     * 获取标题栏高度（px单位）
     * @return
     */
    fun getTitleBarHeightPixelSize(): Int {
        if (titleBarHeight == 0) {
            titleBarHeight = context.resources.getDimensionPixelSize(R.dimen.xin_titlebar_height)
        }
        return titleBarHeight
    }

    /**
     * 获取标题栏 paddingTop 值（px单位）
     * @return
     */
    fun getTitlebarPaddingTop(): Int {
        if (Build.VERSION.SDK_INT >= 19) {
            if (systemStatusBarHeight == 0) systemStatusBarHeight =
                AndroidUtils.getStatusBarHeight(context)
            titlebarPaddingTop = systemStatusBarHeight
        } else {
            titlebarPaddingTop = 0
        }
        return titlebarPaddingTop
    }

    /**
     * 获取标题栏高度
     * @param pixelSize （px单位）
     * @return
     */
    fun setTitleBarHeightPixelSize(pixelSize: Int) {
        titleBarHeight = pixelSize
    }

    /**
     * 设置标题区域布局显示状态。如果显示标题，将隐藏搜索框并且显示右边的菜单
     * @param visible
     */
    fun setTitleVisibility(visible: Boolean) {
        setViewVisibility(centerLayout, visible)
        updateTitleToCenter()
    }

    /**
     * 设置标题
     * @param resId
     */
    fun setTitle(@StringRes resId: Int) {
        titleView.setText(resId)
        setVisibility(true)
    }

    /**
     * 设置标题
     * @param text
     */
    fun setTitle(text: CharSequence?) {
        titleView.text = text
        setVisibility(true)
    }

    /**
     * 设置标题文字颜色
     * @param color 如 Color.DEFAULT
     */
    fun setTitleColor(color: Int) {
        titleView.setTextColor(color)
    }

    /**
     * 设置“设置”按钮的文字
     * @param resId
     */
    fun setSetting(resId: Int) {
        settingView.setText(resId)
        setSettingVisibility(true)
    }

    /**
     * 设置“设置”按钮的文字
     * @param text
     */
    fun setSetting(text: CharSequence?) {
        settingView.text = text
        setSettingVisibility(true)
    }

    /**
     * 设置标题栏Menu菜单按钮显示状态
     * @param visible 是否显示
     */
    fun setMenuVisibility(visible: Boolean) {
        setViewVisibility(menuView, visible)
        updateTitleToCenter()
    }

    /**
     * 显示或隐藏设置按钮
     * @param visible
     */
    fun setSettingVisibility(visible: Boolean) {
        setViewVisibility(settingView, visible)
        updateTitleToCenter()
    }

    /**
     * 设置标题栏返回按钮是否可见
     * @param visible
     */
    fun setBackVisibility(visible: Boolean) {
        setViewVisibility(backView, visible)
        updateTitleToCenter()
    }

    /**
     * 设置标题栏左边文本按钮是否可见
     * @param visible
     */
    fun setLeftTextVisibility(visible: Boolean) {
        setViewVisibility(leftTextView, visible)
        updateTitleToCenter()
    }

    /**
     * 设置标题栏左边的关闭按钮是否可见
     * @param visible
     */
    fun setCloseVisibility(visible: Boolean) {
        setViewVisibility(closeView, visible)
        updateTitleToCenter()
    }

    /**
     * 设置标题返回按钮的事件监听器
     * @param onClickListener
     */
    fun setBackListener(onClickListener: View.OnClickListener?) {
        backView.setOnClickListener(onClickListener)
    }


    /**
     * 设置标题栏左边文本按钮的事件监听器
     * @param onClickListener
     */
    fun setLeftTextListener(onClickListener: View.OnClickListener?) {
        leftTextView.setOnClickListener(onClickListener)
    }

    /**
     * 设置标题返回按钮的事件监听器
     * @param onClickListener
     */
    fun setCloseListener(onClickListener: View.OnClickListener?) {
        closeView.setOnClickListener(onClickListener)
    }

    /**
     * 控制View显示状态
     * @param view
     * @param visible
     */
    private fun setViewVisibility(view: View?, visible: Boolean) {
        view?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 保证title文字居中
     */
    private fun updateTitleToCenter() {
        val minWidth = context.resources.getDimensionPixelSize(R.dimen.xin_titlebar_menu_minWidth)
        var maxWidth = Math.max(getLeftLayout().measuredWidth, getRightLayout().measuredWidth)
        maxWidth = Math.max(maxWidth, minWidth)
        //leftLayout.minimumWidth = maxWidth
        //rightLayout.minimumWidth = maxWidth
        centerLayout.setPadding(maxWidth, 0, maxWidth, 0)
    }

    /**
     * 设置标题栏右边"设置"按钮的点击事件
     * @param onClickListener 点击事件
     */
    fun setSettingListener(onClickListener: View.OnClickListener?) {
        settingView.setOnClickListener(onClickListener)
    }

    /**
     * 设置标题栏右边"设置"按钮的点击事件
     * @param text 按钮文字
     * @param onClickListener 按钮监听器
     */
    fun setSettingListener(text: CharSequence?, onClickListener: View.OnClickListener?) {
        settingView.setOnClickListener(onClickListener)
        setSetting(text)
    }

    /**
     * 设置标题栏右边菜单按钮的点击事件
     * @param onClickListener
     */
    fun setMenuListener(onClickListener: View.OnClickListener?) {
        menuView.setOnClickListener(onClickListener)
    }

    /**
     * 设置标题栏右边菜单menu1的点击事件
     * @param drawableRes 按钮背景资源ID
     * @param onClickListener
     */
    fun setMenuListener(drawableRes: Int, onClickListener: View.OnClickListener?) {
        menuView.setOnClickListener(onClickListener)
        menuView.setImageResource(drawableRes)
        setMenuVisibility(true)
    }

    /**
     * 设置标题栏右边菜单按钮的背景
     * @param resId 按钮背景资源ID
     */
    fun setMenuResource(resId: Int) {
        menuView.setImageResource(resId)
        setMenuVisibility(true)
    }

    /**
     * 设置标题栏上“加载中”动画的显示隐藏
     */
    fun setLoadAnimVisible(visible: Boolean) {
        if (visible) { // 显示
            if (titleBarLayout.visibility == View.VISIBLE) {
                loadView.visibility = View.VISIBLE
                if (!loadAnim.isRunning) {
                    loadAnim.start()
                }
            }
        } else { // 隐藏
            loadView.visibility = View.GONE
            if (loadAnim.isRunning) {
                loadAnim.stop()
            }
        }
    }

    fun addCenterLayout(view: View?) {
        if (view != null) {
            centerLayout.removeAllViews()
            centerLayout.addView(view)
        }
    }

    /**
     * 设置标题栏上的WebView加载进度条值
     * @param progress 进度值 0-100
     */
    fun setProgressValue(progress: Int): ProgressBar {
        progressBar.progress = when {
            progress < 0 -> 0
            progress > 100 -> 100
            else -> progress
        }
        return progressBar
    }

    /**
     * 设置标题栏上的WebView加载进度可见状态
     * @param visible
     */
    fun setProgressBarVisible(visible: Boolean): ProgressBar {
        progressBar.progress = 0
        setViewVisibility(progressBar, visible)
        return progressBar
    }

}