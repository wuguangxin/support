package com.wuguangxin.adapter

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*

/**
 * 基础ViewHolder，为了统一加入 ButterKnife.bind(this, view);
 * Created by wuguangxin on 2018/10/16.
 */
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val sparseArray = SparseArray<View>()

    fun getItemView(): View {
        return itemView
    }

    fun <T> findView(@IdRes viewId: Int): T {
        var view = sparseArray[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            sparseArray.put(viewId, view)
        }
        return view as T
    }

    fun <T> setText(@IdRes viewId: Int, text: String?): BaseViewHolder {
        findView<TextView>(viewId).text = text
        return this
    }

    fun setText(@IdRes viewId: Int, @StringRes stringId: Int): BaseViewHolder {
        findView<TextView>(viewId).setText(stringId)
        return this
    }

    fun setTextSize(@IdRes viewId: Int, pixelSize: Float): BaseViewHolder {
        findView<TextView>(viewId).textSize = pixelSize
        return this
    }

    fun setTextSize(@IdRes viewId: Int, unit: Int, pixelSize: Float): BaseViewHolder {
        findView<TextView>(viewId).setTextSize(unit, pixelSize)
        return this
    }

    fun setTypeface(@IdRes viewId: Int, typeface: Typeface): BaseViewHolder {
        findView<TextView>(viewId).typeface = typeface
        return this
    }

    fun setGone(@IdRes viewId: Int, isGone: Boolean): BaseViewHolder {
        findView<TextView>(viewId).visibility = if (isGone) View.GONE else View.VISIBLE
        return this
    }

    fun setVisible(@IdRes viewId: Int, isVisible: Boolean): BaseViewHolder {
        findView<TextView>(viewId).visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        return this
    }

    fun setTextColor(@IdRes viewId: Int, @ColorInt color: Int): BaseViewHolder {
        findView<TextView>(viewId).setTextColor(color)
        return this
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes drawableRes: Int): BaseViewHolder {
        findView<ImageView>(viewId).setImageResource(drawableRes)
        return this
    }

    fun setImageDrawable(@IdRes viewId: Int, @Nullable drawable: Drawable): BaseViewHolder {
        findView<ImageView>(viewId).setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): BaseViewHolder {
        findView<View>(viewId).setBackgroundColor(color)
        return this
    }

    fun setBackgroundResource(@IdRes viewId: Int, @ColorRes color: Int): BaseViewHolder {
        findView<View>(viewId).setBackgroundResource(color)
        return this
    }

    fun setChecked(@IdRes viewId: Int, checked: Boolean): BaseViewHolder {
        findView<Checkable>(viewId).isChecked = checked
        return this
    }

    fun setEnable(@IdRes viewId: Int, enable: Boolean): BaseViewHolder {
        findView<View>(viewId).isEnabled = enable
        return this
    }

    fun setSelected(@IdRes viewId: Int, selected: Boolean): BaseViewHolder {
        findView<View>(viewId).isSelected = selected
        return this
    }

    fun setPaddingStart(@IdRes viewId: Int, padStart: Int): BaseViewHolder {
        findView<View>(viewId).apply {
            setPadding(padStart, paddingTop, paddingRight, paddingBottom)
        }
        return this
    }

    fun setPaddingEnd(@IdRes viewId: Int, padEnd: Int): BaseViewHolder {
        findView<View>(viewId).apply {
            setPadding(paddingLeft, paddingTop, padEnd, paddingBottom)
        }
        return this
    }
}