package com.wuguangxin.ui

import androidx.databinding.ViewDataBinding
import androidx.appcompat.app.AppCompatActivity
import com.wuguangxin.listener.BaseInterface
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.wuguangxin.dialog.LoadingDialog
import com.wuguangxin.dialog.XinDialog
import android.os.Bundle
import com.wuguangxin.support.R
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import com.wuguangxin.base.TitleBar
import com.wuguangxin.base.LoadingStatus
import com.wuguangxin.base.FragmentTask
import com.wuguangxin.base.LayoutManager
import com.wuguangxin.utils.*

/**
 * Activity基类
 * Created by wuguangxin on 2015/4/1
 */
abstract class XinBaseActivity<B : ViewDataBinding> : AppCompatActivity(), BaseInterface {
    open lateinit var layoutManager: LayoutManager
    open lateinit var titleBar: TitleBar

    open var TAG: String? = null
    open var loadingDialog: LoadingDialog? = null
    open var mDialog: XinDialog? = null
    lateinit var binding: B

    protected var screenWidth = 0
    private var slidingFinish = false
    private var mSlidingFinishHelper: SlidingFinishHelper? = null

    fun getContext(): Context = super.getBaseContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = this::class.simpleName
        loadingDialog = LoadingDialog(this) // 加载对话框
        mSlidingFinishHelper = SlidingFinishHelper(this)




        layoutManager = LayoutManager(this, R.layout.activity_base) // 布局管理器
        layoutManager.setErrorLayoutListener { onClickErrorLayout() }
        //layoutManager.setContentView(getLayoutId())

        binding = DataBindingUtil.inflate(LayoutInflater.from(this), getLayoutId(), layoutManager.bodyLayout, false)!!
        layoutManager.setContentView(binding.root)

        titleBar = layoutManager.titleBar
        titleBar.setTitle(TAG)

        val rootView = layoutManager.bodyLayout.getChildAt(0)
        if (rootView != null) {
            binding = DataBindingUtil.bind(rootView)!!
        }
        setContentView(layoutManager.rootLayout)

        SoftHideKeyBoardUtil.assistActivity(this)
        setImmersionStatusBar(this)
        initArguments(intent)
        initView(savedInstanceState)
        initListener()
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param activity
     */
    open fun setImmersionStatusBar(activity: Activity?) {
        StatusBarUtils.setImmersionStatusBar(
            activity,
            resources.getColor(R.color.xin_titlebar_background)
        )
        StatusBarUtils.setStatusBarFontColor(activity, false)
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

    override fun onStart() {
        super.onStart()
        try {
            initData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        // 关闭所有对话框dismiss
        titleBar.setLoadAnimVisible(false)
        dismissDialog()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        AnimUtil.animClose(this)
    }

    /**
     * 当点击错误界面时执行的方法。
     * （使用场景：某个界面第一次请求数据并发生网络异常时，显示错误界面，点击错误界面时，执行该方法，例如重新请求数据。）
     */
    open fun onClickErrorLayout() {
        initData()
    }

    /**
     * 获取当前使用的 SmartRefreshLayout
     */
    private val refreshLayout: SmartRefreshLayout?
        get() {
            return LayoutInflater.from(this)
                .inflate(R.layout.xin_def_refresh_layout, null) as SmartRefreshLayout
        }

    /**
     * 取消透明状态栏样式
     */
    fun clearImmersionStatusBar() {
        AndroidUtils.clearImmersionStatusBar(this)
    }

    /**
     * 该方法会将传入的layoutRes布局放入到SmartRefreshLayout里，实现下拉刷新，这只是少写一层代码而已。
     *
     * @param layoutRes
     */
    fun setContentViewOnRefreshLayout(layoutRes: Int) {
        val view = LayoutInflater.from(this).inflate(layoutRes, refreshLayout)
        setContentView(view)
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)
        titleBar.setTitle(title)
    }

    override fun setTitle(title: Int) {
        super.setTitle(title)
        titleBar.setTitle(title)
    }

    override fun setTitleColor(color: Int) {
        titleBar.getTitleView().setTextColor(color)
    }

    protected fun setTitleBarVisibility(visibility: Boolean) {
        titleBar.setVisibility(visibility)
    }

    protected fun setBodyVisibility(visibility: Boolean) {
        layoutManager.setBodyVisibility(visibility)
    }

    override fun showToast(text: String) {
        ToastUtils.showToast(baseContext, text)
    }

    override fun printLogI(text: String) {
        Logger.i(baseContext, text)
    }

    override fun openActivity(clazz: Class<out Activity>) {
        openActivity(clazz, null)
    }

    override fun openActivity(clazz: Class<out Activity>, bundle: Bundle?) {
        val intent = Intent(baseContext, clazz)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    override fun startActivity(intent: Intent) {
        // super.startActivity(intent);
        // 换成startActivityForResult打开的Activity切换动画比较好看，并且requestCode=0
        super.startActivityForResult(intent, 0)
    }

    override fun setLoadingStatus(loadingStatus: Int, isPull: Boolean, isCached: Boolean) {
        when (loadingStatus) {
            LoadingStatus.START -> {
                if (isPull && isCached) {
                    setTitleLoadingProgressVisible(true)
                } else {
                    setLoadingDialogVisible(true)
                }
            }
            LoadingStatus.SUCCESS,
            LoadingStatus.FAILURE -> {
                // 是获取数据时，且没有缓存数据，且网获取失败，则可显示网络错误界面
                if (isPull && !isCached && loadingStatus == LoadingStatus.FAILURE) {
                    layoutManager.setErrorLayoutVisible(true)
                }
                if (isCached) {
                    setTitleLoadingProgressVisible(false)
                } else {
                    setLoadingDialogVisible(false)
                }
            }
            LoadingStatus.CANCEL,
            LoadingStatus.FINISH -> {
                setTitleLoadingProgressVisible(false)
                setLoadingDialogVisible(false)
            }
        }
    }

    override fun setLoadingDialogVisible(visible: Boolean) {
        loadingDialog?.setVisible(visible)
    }

    override fun setTitleLoadingProgressVisible(isStart: Boolean) {
        titleBar.setLoadAnimVisible(isStart)
    }

    override fun dismissDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
        mDialog?.dismiss()
        mDialog = null
    }

    override fun dismissDialog(vararg dialogs: Dialog) {
        for (dialog in dialogs) {
            dialog.dismiss()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 让最后打开的那个fragment执行
        FragmentTask.topTask?.onActivityResult(requestCode, resultCode, data)
    }
}