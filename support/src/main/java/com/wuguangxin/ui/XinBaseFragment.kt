package com.wuguangxin.ui

import androidx.databinding.ViewDataBinding
import com.wuguangxin.listener.BaseInterface
import com.wuguangxin.dialog.XinDialog
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import com.wuguangxin.base.FragmentTask
import com.wuguangxin.base.TitleBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.wuguangxin.base.LayoutManager
import com.wuguangxin.support.R
import com.wuguangxin.utils.Logger

/**
 * 基础Fragment
 * Created by wuguangxin on 16/8/26
 */
abstract class XinBaseFragment<B : ViewDataBinding> : Fragment(), BaseInterface {
    lateinit var layoutManager: LayoutManager
    lateinit var mActivity: XinBaseActivity<*>
    open var mDialog: XinDialog? = null
    open var TAG: String? = null
    var rootView : ViewGroup? = null  // 根布局
    private var visible: Boolean = false // 界面是否可见
    open fun isUserVisible(): Boolean = visible

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as XinBaseActivity<*>
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        visible = isVisibleToUser
        if (isAdded) {
            if (isUserVisible()) {
                onResume()
            } else {
                onPause()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        visible = !hidden
        if (isAdded) {
            if (isUserVisible()) {
                onResume()
            } else {
                onPause()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // super.onCreateView(inflater, container, savedInstanceState);
        rootView = container
        TAG = this::class.simpleName
        rootView = inflater.inflate(getLayoutId(), null) as ViewGroup
        initArguments(arguments) // 接收参数
        initView(savedInstanceState) // 初始化界面
        initListener() // 设置监听器
        setErrorLayoutListener()
        return rootView
    }

    /**
     * 初始化错误布局点击监听器
     */
    fun setErrorLayoutListener() {
        layoutManager.setErrorLayoutListener {
            val fragment = FragmentTask.topTask
            if (fragment is XinBaseFragment<*>) {
                fragment.initData()
            }
        }
    }

    /**
     * 初始化传参
     */
    fun initArguments(arguments: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        if (isUserVisible()) {
            FragmentTask.inTask(this)
            initData() // 初始化数据
        }
    }

    override fun onDestroy() {
        dismissDialog()
        titleBar.setLoadAnimVisible(false)
        super.onDestroy()
    }

    override fun onDestroyView() {
        FragmentTask.outTask(this)
        super.onDestroyView()
    }

    /**
     * 获取当前使用的 SmartRefreshLayout
     */
    private val refreshLayout: SmartRefreshLayout
        get() {
            return LayoutInflater.from(context).inflate(R.layout.xin_def_refresh_layout, null) as SmartRefreshLayout
        }

    val titleBar: TitleBar get() = layoutManager.titleBar

    /**
     * 关闭Activity
     */
    fun finish() = mActivity.finish()

    /**
     * 获取传递的参数，如果没有设置参数，则返回空的 Bundle
     */
    val argumentsByBase: Bundle get() = arguments ?: Bundle.EMPTY

    override fun showToast(text: String) {
        mActivity.showToast(text)
    }

    override fun printLogI(text: String) {
        Logger.i(TAG, text)
    }

    override fun openActivity(clazz: Class<out Activity?>) {
        mActivity.openActivity(clazz)
    }

    override fun openActivity(clazz: Class<out Activity?>, bundle: Bundle?) {
        mActivity.openActivity(clazz, bundle)
    }

    //========================== BaseListener ==================================================
    //========================== LoadingListener ===============================================
    override fun setLoadingStatus(loadingStatus: Int, isPull: Boolean, isCached: Boolean) {
        mActivity.setLoadingStatus(loadingStatus, isPull, isCached)
    }

    override fun setLoadingDialogVisible(visible: Boolean) {
        mActivity.setLoadingDialogVisible(visible)
    }

    override fun setTitleLoadingProgressVisible(visible: Boolean) {
        mActivity.setTitleLoadingProgressVisible(visible)
    }

    override fun dismissDialog() {
        mActivity.dismissDialog(mDialog!!)
    }

    override fun dismissDialog(vararg dialogs: Dialog) {
        mActivity.dismissDialog(*dialogs)
    }

    //========================== LoadingListener end ===============================================
    fun setResult(resultCode: Int) {
        setResult(resultCode, null)
    }

    fun setResult(resultCode: Int, data: Intent?) {
        mActivity.setResult(resultCode, data)
    }
}