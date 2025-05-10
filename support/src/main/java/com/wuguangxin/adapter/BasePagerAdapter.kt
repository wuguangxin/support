package com.wuguangxin.adapter

import android.content.Context
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup
import java.util.ArrayList

/**
 * PagerView基础适配器
 *
 * Created by wuguangxin on 14/11/17
 */
abstract class BasePagerAdapter<T>(var context: Context, var list: MutableList<T>?) : PagerAdapter() {

    private val mData = mutableListOf<T>()

    /**
     * 更新PagerAdapter适配器
     * @param list 数据集
     */
    fun notify(list: MutableList<T>?) {
        mData.clear()
        list?.let {
            mData.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return mData[position]
    }

    /**
     * 获取要滑动的图片数量
     */
    override fun getCount(): Int {
        return mData.size
    }

    /**
     * 当滑动中的view对象和进来的对象是同一个时, 返回true 复用当前view对象
     */
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    /**
     * PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
     * 销毁一个Item position 对象的是将要销毁的下标
     */
    abstract override fun destroyItem(viewGroup: ViewGroup, position: Int, `object`: Any)

    /**
     * 预加载一个item
     * 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，
     * 我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
     */
    abstract override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any
}