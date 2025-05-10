package com.wuguangxin.adapter

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.wuguangxin.listener.OnItemClickListener
import com.wuguangxin.utils.ToastUtils

abstract class BaseRecyclerAdapter<T, V : BaseViewHolder?> : RecyclerView.Adapter<V> {
    private var context: Context?

    private val mData = mutableListOf<T>()

    var onItemClickListener: OnItemClickListener<T>? = null

    /** 列表类型（可用于标识列表的类型）  */
    var listType = 0

    constructor(context: Context) {
        this.context = context
    }

    constructor(context: Context?, data: List<T>?) {
        this.context = context
        mData.clear()
        if (data != null) {
            mData.addAll(data)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): V {
        val view = View.inflate(viewGroup.context, getLayoutId(), null)
        view.setOnClickListener {
            Log.e("BaseRecyclerAdapter", "position = $position")
            onItemClickListener?.onItemClick(it, getItem(position), position, listType)
        }
        return createViewHolder(view, position)
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        bindViewData(holder, mData[position], position, listType)
    }

    abstract fun getLayoutId(): Int

    open fun createViewHolder(view: View, position: Int): V {
        return BaseViewHolder(view) as V
    }

    abstract fun bindViewData(holder: V, data: T, position: Int, type: Int)

    override fun getItemCount(): Int {
        return mData.size
    }

    open var list: List<T>?
        get() = mData
        set(data) {
            mData.clear()
            data?.let {
                mData.addAll(it)
            }
            notifyDataSetChanged()
        }

    fun getItem(position: Int): T {
        return mData[position]
    }

    fun showToast(text: String?) {
        if (context == null) return
        ToastUtils.showToast(context, text)
    }
}