package com.wuguangxin.listener

import android.view.View

/**
 * item点击监听器
 * Created by wuguangxin on 17/4/15.
 */
interface OnItemClickListener<T> {
    /**
     * @param view 被点击的item View
     * @param item item对应的数据
     * @param position 点击的item位置
     * @param type 数据类型 (可以对应viewPager的currentItem)
     */
    fun onItemClick(view: View, item: T, position: Int, type: Int)
}