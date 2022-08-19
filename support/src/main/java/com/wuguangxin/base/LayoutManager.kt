package com.wuguangxin.base

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.os.Build
import android.view.View
import androidx.core.view.isEmpty
import com.wuguangxin.support.R

/**
 * 布局管理器
 * Created by wuguangxin on 14/10/16.
 */
class LayoutManager(private var activity: Activity, private var baseView: View) {

    /** 根布局布局 */
    val rootLayout: ConstraintLayout = baseView.findViewById(R.id.root_layout)
    /** 显示内容布局 */
    val bodyLayout: ConstraintLayout = baseView.findViewById(R.id.body_layout)
    /** 网络异常布局 */
    val errorLayout : LinearLayout = baseView.findViewById(R.id.error_layout)
    /** 标题栏管理器 */
    val titleBar: TitleBar get() = TitleBar(activity, baseView)
    /** 菜单按钮管理器 */
    val menuBar: MenuBar by lazy {  MenuBar(activity)  }

    constructor(activity: Activity, @LayoutRes baseLayoutRes: Int)
            : this(activity, LayoutInflater.from(activity).inflate(baseLayoutRes, null)) {
        bodyLayout.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        ActivityTask.clearTask()
    }

    /**
     * 设置Body布局
     * @param layoutId
     */
    fun setContentView(@LayoutRes layoutId: Int): LayoutManager {
        if (layoutId != 0) {
            // 这种方式会导致有下拉刷新界面布局高度测量有问题。
            // View view = View.inflate(mActivity, layoutRes, null);
            // return setContentView(view);

            // 完美解决
            View.inflate(activity, layoutId, bodyLayout)
        }
        return this
    }

    /**
     * 设置Body布局
     * @param view
     */
    fun setContentView(view: View): LayoutManager {
        if (bodyLayout.isEmpty()) {
            bodyLayout.removeAllViews()
        }
        bodyLayout.addView(view)
        return this
    }

    /**
     * 设置界面背景
     * @param resId drawable资源ID
     */
    fun setBackgroundResource(resId: Int): LayoutManager {
        rootLayout.setBackgroundResource(resId)
        return this
    }

    /**
     * 设置界面背景
     * @param drawable
     */
    fun setBackgroundResource(drawable: Drawable?): LayoutManager {
        setBackground(drawable)
        return this
    }

    /**
     * 设置界面背景
     * @param drawable
     */
    fun setBackground(drawable: Drawable?): LayoutManager {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootLayout.background = drawable
        } else {
            rootLayout.setBackgroundDrawable(drawable)
        }
        return this
    }

    /**
     * 设置界面背景
     * @param color 颜色
     */
    fun setBackgroundColor(color: Int): LayoutManager {
        rootLayout.setBackgroundColor(color)
        return this
    }

    /**
     * 控制Error布局显隐状态
     * @param visible 是否显示
     */
    fun setErrorLayoutVisible(visible: Boolean): LayoutManager {
        // 20180311 wgx 注释，界面有多个fragment时，体验很不好。但没网络时用这个也不错
		if(visible){
            bodyLayout.visibility = View.GONE;
            errorLayout.visibility = View.VISIBLE;
		} else {
            bodyLayout.visibility = View.VISIBLE;
            errorLayout.visibility = View.GONE;
		}
        return this
    }

    /**
     * 设置错误布局界面点击事件
     * @param onClickListener
     */
    fun setErrorLayoutListener(onClickListener: View.OnClickListener?): LayoutManager {
        errorLayout.setOnClickListener(onClickListener)
        return this
    }

    /**
     * 设置内容主体显示状态
     * @param visibility 是否可见
     */
    fun setBodyVisibility(visibility: Boolean): LayoutManager {
        bodyLayout.visibility = if (visibility) View.VISIBLE else View.GONE
        return this
    }

    /**
     * 设置标题栏显示状态
     * @param visible
     */
    fun setTitleBarVisibility(visible: Boolean): LayoutManager {
        titleBar.setVisibility(visible)
        return this
    }

}