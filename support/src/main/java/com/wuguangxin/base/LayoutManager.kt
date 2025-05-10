package com.wuguangxin.base

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isEmpty
import com.wuguangxin.support.R

/**
 * 布局管理器
 * Created by wuguangxin on 14/10/16.
 */
class LayoutManager(private var activity: Activity, private var baseView: View) {

    // 根布局布局
    val rootLayout: ConstraintLayout = baseView.findViewById(R.id.root_layout)
    // 显示内容布局
    val bodyLayout: ConstraintLayout = baseView.findViewById(R.id.body_layout)
    // 标题栏管理器
    val titleBar: TitleBar get() = TitleBar(activity, baseView)

    constructor(
        activity: Activity,
        @LayoutRes baseLayoutRes: Int
    ) : this(activity, LayoutInflater.from(activity).inflate(baseLayoutRes, null)) {
        bodyLayout.visibility = View.VISIBLE
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
    fun setBackground(drawable: Drawable?): LayoutManager {
        rootLayout.background = drawable
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