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
    private val mFragments = arrayListOf<Fragment>()
    private val mTitles = mutableListOf<String>()

    constructor(activity: FragmentActivity) : this(activity.supportFragmentManager)
    constructor(fm: FragmentManager) : this(fm, null)
    constructor(fm: FragmentManager, fragments: MutableList<Fragment>?) : this(fm, fragments, null)
    constructor(fm: FragmentManager, fragments: MutableList<Fragment>?, titles: MutableList<String>?): super(fm) {
        setList(fragments, titles)
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    fun setTitleList(titles: MutableList<String>?) {
        this.mTitles.clear()
        titles?.let { this.mTitles.addAll(titles) }
        notifyDataSetChanged()
    }

    fun setList(fragments: MutableList<Fragment>?) {
        this.mFragments.clear()
        fragments?.let { this.mFragments.addAll(fragments) }
        notifyDataSetChanged()
    }

    fun setList(fragments: MutableList<Fragment>?, titles: MutableList<String>?) {
        this.mFragments.clear()
        this.mTitles.clear()
        fragments?.let { this.mFragments.addAll(fragments) }
        titles?.let { this.mTitles.addAll(titles) }
        notifyDataSetChanged()
    }
}