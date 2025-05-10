package com.wuguangxin.simple.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wuguangxin.simple.bean.GameRecordBean
import com.wuguangxin.simple.R
import com.wuguangxin.utils.DateUtils

class GameRecordAdapter(list: MutableList<GameRecordBean>?) :
    BaseQuickAdapter<GameRecordBean, BaseViewHolder>(R.layout.item_game_record, list) {

    override fun convert(holder: BaseViewHolder, item: GameRecordBean) {
        holder.setText(R.id.time, DateUtils.formatStringLong(item.createTime))
        holder.setText(R.id.textRed, "红:" + item.r + "")
        holder.setText(R.id.textGreen, "绿:" + item.g + "")
        holder.setText(R.id.textBlue, "蓝:" + item.b + "")
        holder.setText(R.id.result, item.result)
    }
}