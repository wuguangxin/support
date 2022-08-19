package com.wuguangxin.base

import android.app.Activity
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.wuguangxin.support.R
import com.wuguangxin.utils.AnimUtil
import java.util.ArrayList

/**
 * 标题栏下方的抽屉式伸缩菜单
 * Created by wuguangxin on 14/10/16.
 */
class MenuBar constructor(
    activity: Activity,
    list: ArrayList<String?> = ArrayList()
) {
    private var onMenuItemClickListener: OnMenuItemClickListener? = null
    private var mMenuLayout: RelativeLayout? = null
    private var mMenuListView: ListView? = null
    private var mMenuShadeView: View? = null // 菜单遮罩
    private var adapter: BaseAdapter? = null

    private var animationShow: Animation? = null // 菜单显示时的动画
    private var animationHide: Animation? = null // 菜单隐藏时的动画
    private var animationShadeShow: Animation? = null // 遮罩显示时的动画
    private var animationShadeHide: Animation? = null // 遮罩隐藏时的动画

    private var isShowing = false // 判断菜单显示状态

    init {
        mMenuLayout = activity.findViewById(R.id.menu_layout) // 菜单列表
        mMenuListView = activity.findViewById(R.id.xin_menu_listview)
        mMenuShadeView = activity.findViewById(R.id.xin_menu_shadeview)
        setViewAlign(mMenuLayout, RelativeLayout.BELOW, R.id.xin_titlebar_layout)
        animationShow = AnimUtil.getTopIn()
        animationHide = AnimUtil.getTopOut()
        animationShadeShow = AnimUtil.getLeftIn()
        animationShadeHide = AnimUtil.getLeftOut()
        adapter = ArrayAdapter(activity, R.layout.xin_item_menu_list, list)
        mMenuListView?.adapter = adapter
        mMenuShadeView?.setOnClickListener { hide() }
    }

    /**
     * 设置View的对齐方式。(view与anchor对齐方式为verb)
     *
     * @param view 哪个View需要设置对齐
     * @param verb 对齐方式（如：RelativeLayout.BELOW，view在id为verb的View的下方）
     * @param anchor 对齐哪个View的id
     */
    fun setViewAlign(view: View?, verb: Int, anchor: Int) {
        if (view != null && verb != -1) {
            val params = view.layoutParams as RelativeLayout.LayoutParams?
            params?.addRule(verb, anchor) // anchor为要对齐的View的id
            view.layoutParams = params
        }
    }

    fun setAdapter(adapter: BaseAdapter?) {
        this.adapter = adapter
        mMenuListView?.adapter = adapter
    }

    fun getAdapter(): BaseAdapter? = adapter

    fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
    }

    /**
     * 设置菜单显示动画
     * @param animationShow
     */
    fun setAnimationShow(animationShow: Animation?) {
        this.animationShow = animationShow
    }

    /**
     * 设置菜单隐藏动画
     * @param animationHide
     */
    fun setAnimationHide(animationHide: Animation?) {
        this.animationHide = animationHide
    }

    /**
     * 设置菜单遮罩显示动画
     * @param animationShadeShow
     */
    fun setAnimationShadeShow(animationShadeShow: Animation?) {
        this.animationShadeShow = animationShadeShow
    }

    /**
     * 设置菜单遮罩隐藏动画
     *
     * @param animationShadeHide
     */
    fun setAnimationShadeHide(animationShadeHide: Animation?) {
        this.animationShadeHide = animationShadeHide
        // 设置遮罩隐藏动画监听器，当菜单遮罩隐藏动画结束时，再隐藏整个菜单Layout
        this.animationShadeHide?.setAnimationListener(animationShadeHideListener)
    }

    /**
     * 显示菜单
     */
    fun show() {
        if (mMenuLayout != null && mMenuLayout?.visibility == View.GONE) {
            isShowing = true
            mMenuLayout?.visibility = View.VISIBLE
            mMenuListView?.visibility = View.VISIBLE
            mMenuListView?.startAnimation(animationShow)
            mMenuShadeView?.visibility = View.VISIBLE
            mMenuShadeView?.startAnimation(animationShadeShow)
            onMenuItemClickListener?.onMenuVisible(isShowing)
        }
    }

    /**
     * 隐藏菜单
     */
    fun hide() {
        if (mMenuLayout != null && mMenuLayout?.visibility == View.VISIBLE) {
            isShowing = false
            mMenuListView?.visibility = View.GONE
            mMenuListView?.startAnimation(animationHide)
            mMenuShadeView?.visibility = View.GONE
            mMenuShadeView?.startAnimation(animationShadeHide)
            onMenuItemClickListener?.onMenuVisible(isShowing)
        }
    }

    /**
     * 遮罩动画隐藏结束监听器
     */
    private val animationShadeHideListener: Animation.AnimationListener by lazy {
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (mMenuLayout != null) {
                    mMenuLayout!!.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 设置菜单项点击监听器
     *
     * @param onMenuItemClickListener
     */
    fun setOnMenuItemClickListener(onMenuItemClickListener: OnMenuItemClickListener?) {
        this.onMenuItemClickListener = onMenuItemClickListener
        mMenuListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            onMenuItemClickListener?.onItemClicked(parent, view, position, id)
        }
    }

    /**
     * 菜单项点击回事件调接口
     */
    interface OnMenuItemClickListener {
        /**
         * 当菜单项被点击时触发
         *
         * @param parent
         * @param view
         * @param position 被点击的Item的位置
         * @param id
         */
        fun onItemClicked(parent: AdapterView<*>?, view: View?, position: Int, id: Long)

        /**
         * 当菜单显示或隐藏时触发
         *
         * @param visible 是否显示
         */
        fun onMenuVisible(visible: Boolean)
    }
}