package com.wuguangxin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.util.ArrayList

/**
 * Fragment适配器
 *
 * Created by wuguangxin on 17/3/27
 */
class BaseFragmentAdapter : FragmentPagerAdapter {
    private val list = arrayListOf<Fragment>()
    private val titleList = mutableListOf<String>()

    constructor(activity: FragmentActivity) : this(activity.supportFragmentManager)
    constructor(fm: FragmentManager) : this(fm, null)
    constructor(fm: FragmentManager, list: MutableList<Fragment>?) : this(fm, list, null)
    constructor(fm: FragmentManager, list: MutableList<Fragment>?, titleList: MutableList<String>?): super(fm) {
        setList(list, titleList)
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    fun setTitleList(titleList: MutableList<String>?) {
        this.titleList.clear()
        titleList?.let { this.titleList.addAll(titleList) }
        notifyDataSetChanged()
    }

    fun setList(list: MutableList<Fragment>?) {
        this.list.clear()
        list?.let { this.list.addAll(list) }
        notifyDataSetChanged()
    }

    fun setList(list: MutableList<Fragment>?, titleList: MutableList<String>?) {
        this.list.clear()
        this.titleList.clear()
        list?.let { this.list.addAll(list) }
        titleList?.let { this.titleList.addAll(titleList) }
        notifyDataSetChanged()
    }
}