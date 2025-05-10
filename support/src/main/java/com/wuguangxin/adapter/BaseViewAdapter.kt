package com.wuguangxin.adapter

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup

/**
 * View的适配器
 * Created by wuguangxin on 2019/12/2.
 */
class BaseViewAdapter(val list: MutableList<View>?) : PagerAdapter() {
    private val mData = mutableListOf<View>()

    init {
        this.mData.clear()
        list?.let {
            this.mData.addAll(list)
        }
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return o === view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mData[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mData[position])
    }

    override fun getCount(): Int {
        return mData.size
    }
}