package com.wuguangxin.listener

import android.annotation.SuppressLint
import android.widget.ScrollView
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.view.View

/**
 * 解决当 ScrollView 嵌套时，内部 ScrollView 可能无法滚动问题
 *
 * Created by wuguangxin on 15-1-23
 */
class InnerScrollViewTouchListener(private val scrollView: ScrollView) : OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scrollView.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                scrollView.requestDisallowInterceptTouchEvent(false)
            }
        }
        return false
    }
}