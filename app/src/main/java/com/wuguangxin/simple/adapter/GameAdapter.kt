package com.wuguangxin.simple.adapter

import com.wuguangxin.simple.bean.GameDataBean
import com.wuguangxin.simple.R
import com.wuguangxin.simple.view.SquareCheckBox
import android.widget.CompoundButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.util.*

class GameAdapter(list: MutableList<GameDataBean>) :
    BaseQuickAdapter<GameDataBean, BaseViewHolder>(R.layout.item_game, list) {
    private var resultData = LinkedList<GameDataBean>()
    var full = false

    fun reset() {
        full = false
        resultData.clear()
    }

    fun getResultData(): List<GameDataBean> {
        return resultData
    }

    override fun convert(holder: BaseViewHolder, item: GameDataBean) {
        holder.setIsRecyclable(false) // false: view不重复利用，降低性能
        val checkBox = holder.itemView as SquareCheckBox
        if (item.checked) {
            checkBox.setBackgroundColor(item.color)
            checkBox.setOnCheckedChangeListener(null)
        } else if (!item.enable) {
            checkBox.setBackgroundResource(R.color.gray)
            checkBox.setOnCheckedChangeListener(null)
        } else {
            checkBox.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
                if (checked) {
                    if (full) {
                        return@setOnCheckedChangeListener
                    }
                    resultData.add(item)
                    item.checked = true
                    item.enable = false
                    checkBox.setBackgroundColor(item.color)
                    checkBox.isEnabled = item.enable
                    onStatusListener?.onChecked(item)
                    if (resultData.size >= 12) {
                        resultData.forEach {
                            it.enable = false
                        }
                        full = true
                        notifyItemRangeChanged(0, itemCount)
                    }
                }
            }
        }
        checkBox.isChecked = item.checked
        checkBox.isEnabled = item.enable || resultData.size >= 12
        checkBox.text = item.text
    }

    private var onStatusListener: OnStatusListener? = null
    fun setOnStatusListener(listener: OnStatusListener?) {
        onStatusListener = listener
    }

    interface OnStatusListener {
        fun onChecked(gameDataBean: GameDataBean?)
    }
}