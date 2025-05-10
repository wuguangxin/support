package com.wuguangxin.simple.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wuguangxin.simple.R

/**
 * Created by wuguangxin on 2021/6/30.
 */
class StringAdapter(list: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_simple_layout, list) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.textview, item)
    }
}