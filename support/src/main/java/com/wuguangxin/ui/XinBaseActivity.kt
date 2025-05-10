package com.wuguangxin.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wuguangxin.base.FragmentTask
import com.wuguangxin.base.LoadingStatus
import com.wuguangxin.dialog.LoadingDialog
import com.wuguangxin.dialog.XinDialog
import com.wuguangxin.listener.BaseInterface
import com.wuguangxin.utils.*
import com.wuguangxin.view.titlebar.TitleLayout

/**
 * Activity基类
 * Created by wuguangxin on 2015/4/1
 */
abstract class XinBaseActivity<B : ViewDataBinding> : AppCompatActivity(), BaseInterface {
    protected var titleLayout: TitleLayout? = null

    open var TAG: String? = ""
    open var loadingDialog: LoadingDialog? = null
    open var mDialog: XinDialog? = null
    lateinit var binding: B

    protected var screenWidth = 0
    private var slidingFinish = false
    private var mSlidingFinishHelper: SlidingFinishHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        loadingDialog = LoadingDialog(this) // 加载对话框
        mSlidingFinishHelper = SlidingFinishHelper(this)
        TAG = this::javaClass.name

        //SoftHideKeyBoardUtil.assistActivity(this)

        initArguments(intent)
        initView(savedInstanceState)
        setImmersionStatusBar();
        initListener()
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param activity
     */
    fun setImmersionStatusBar(view: ViewGroup? = null) {
        val titleBar = view ?: titleLayout
        val barColor = titleLayout?.normalTheme?.backgroundColor?: Color.TRANSPARENT
        val isDark = titleLayout?.normalTheme?.isStatusBarDark?: true
        setImmersionStatusBar(this, titleBar, barColor, isDark)
        StatusBarUtils.setStatusBarFontColor(this, isDark)
    }

    open fun setImmersionStatusBar(
        activity: Activity,
        titleBarLayout: ViewGroup?,
        @ColorInt statusBarColor: Int,
        isDarkText: Boolean
    ) {
        if (titleBarLayout == null) {
            return
        }
        // >=19
        if (Build.VERSION.SDK_INT >= 19) {
            // 透明状态栏
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 透明导航栏。注意华为和HTC等有虚拟HOME键盘的，如果不设置下面这段代码，虚拟键盘将覆盖APP底部界面，无法操作底部TAB
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            if (Build.VERSION.SDK_INT >= 21) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                activity.window.statusBarColor = statusBarColor
                titleBarLayout.setPadding(0, StatusBarUtils.getStatusBarHeight(activity), 0, 0)
            } else {
                /* <21
                android:fitsSystemWindows=""，
                false：布局不受StatusBar的影响，可以完全的展示在StatusBar的下面。
                true：布局不受StatusBar的影响，不会被StatusBar遮住，
                android:clipToPadding="false"
                false：布局不受Padding的影响，可以展示在Padding的区域。其实fitsSystemWindows就是设置一个Padding使View不会展示在StatusBar的下方，
                设置系统是否需要考虑 StatusBar 占据的区域来显示
                */
                titleBarLayout.fitsSystemWindows = false
                titleBarLayout.clipToPadding = true
            }
        }
        StatusBarUtils.setStatusBarFontColor(activity, isDarkText)
    }

    /**
     * 取消透明状态栏样式
     */
    fun clearImmersionStatusBar() {
        AndroidUtils.clearImmersionStatusBar(this)
    }

    /**
     * 是否开启滑动关闭 Activity
     */
    fun setSlidingFinish(slidingFinish: Boolean) {
        this.slidingFinish = slidingFinish
    }

    /**
     * 初始化传参
     */
    fun initArguments(intent: Intent) {
    }

    fun getContext(): Context = this

    override fun onStart() {
        super.onStart()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
    }

    override fun finish() {
        super.finish()
        AnimUtil.animClose(this)
    }

    open fun setTitleLayout(@IdRes id: Int): TitleLayout {
        val view = binding.root.findViewById<TitleLayout>(id).bind(this)
        this.titleLayout = view
        return view
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        titleLayout?.title = title
    }

    override fun setTitle(titleId: Int) {
        super.setTitle(titleId)
        titleLayout?.setTitle(titleId)
    }

    override fun log(text: String) {
        Logger.i(TAG, text)
    }

    fun loge(text: String) {
        Logger.e(TAG, text)
    }

    override fun showToast(text: String) {
        ToastUtils.showToast(getContext(), text)
    }

    override fun openActivity(clazz: Class<out Activity>) {
        openActivity(clazz, null)
    }

    override fun openActivity(clazz: Class<out Activity>, bundle: Bundle?) {
        val intent = Intent(getContext(), clazz)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    override fun startActivity(intent: Intent) {
        // 换成startActivityForResult打开的Activity切换动画比较好看，并且requestCode=0
        super.startActivityForResult(intent, 0)
    }

    fun setLoadingStatus(loadingStatus: Int, isPull: Boolean, isCached: Boolean) {
        if (loadingStatus == LoadingStatus.START) {
            if (isPull && isCached) {
                loadingDialog?.setVisible(true)
            }
        } else {
            loadingDialog?.setVisible(false)
        }
    }

    fun dismissDialog(vararg dialogs: Dialog?) {
        loadingDialog?.dismiss()
        loadingDialog = null
        mDialog?.dismiss()
        mDialog = null
        dialogs.forEach { it?.dismiss() }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 让最后打开的那个fragment执行
        FragmentTask.topTask?.onActivityResult(requestCode, resultCode, data)
    }
}