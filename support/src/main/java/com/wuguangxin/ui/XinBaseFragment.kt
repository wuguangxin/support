package com.wuguangxin.ui

import com.wuguangxin.listener.BaseInterface
import com.wuguangxin.dialog.XinDialog
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import com.wuguangxin.base.FragmentTask
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.wuguangxin.base.LayoutManager
import com.wuguangxin.utils.Logger

/**
 * 基础Fragment
 * Created by wuguangxin on 16/8/26
 */
abstract class XinBaseFragment : Fragment(), BaseInterface {
    lateinit var layoutManager: LayoutManager
    lateinit var mActivity: XinBaseActivity<*>
    open var mDialog: XinDialog? = null
    open var TAG: String? = null
    var rootView : ViewGroup? = null  // 根布局
    private var visible: Boolean = false // 界面是否可见
    open fun isUserVisible(): Boolean = visible

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), null) as ViewGroup
        TAG = this::class.simpleName

        initArguments(arguments) // 接收参数
        initView(savedInstanceState) // 初始化界面
        initListener() // 设置监听器
        return rootView
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        FragmentTask.outTask(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
    }

    /**
     * 关闭Activity
     */
    fun finishActivity() = mActivity.finish()

    /**
     * 获取传递的参数，如果没有设置参数，则返回空的 Bundle
     */
    val argumentsByBase: Bundle get() = arguments ?: Bundle.EMPTY

    override fun showToast(text: String) {
        mActivity.showToast(text)
    }

    override fun log(text: String) {
        Logger.i(TAG, text)
    }

    fun loge(text: String) {
        Logger.e(TAG, text)
    }

    override fun openActivity(clazz: Class<out Activity?>) {
        mActivity.openActivity(clazz)
    }

    override fun openActivity(clazz: Class<out Activity?>, bundle: Bundle?) {
        mActivity.openActivity(clazz, bundle)
    }

    private fun dismissDialog(vararg dialogs: Dialog?) {
        mActivity.dismissDialog(*dialogs)
        mDialog?.dismiss()
        mDialog = null
    }

    fun setResult(resultCode: Int, data: Intent?) {
        mActivity.setResult(resultCode, data)
    }
}