package com.wuguangxin.adapter

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup

/**
 * View的适配器
 * Created by wuguangxin on 2019/12/2.
 */
class BaseViewAdapter(val list: List<View>?) : PagerAdapter() {
    private val mData = mutableListOf<View>()

    init {
        mData.clear()
        list?.let {
            mData.addAll(list)
        }
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return o === view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = getItem(position)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(getItem(position))
    }

    override fun getCount(): Int {
        return mData.size
    }

    fun getItem(position: Int): View {
        return mData[position]
    }
}