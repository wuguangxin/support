package com.wuguangxin.listener

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView

/**
 * 对图片多点缩放的监听器
 *
 *
 * Created by wuguangxin on 15-3-31
 */
class OnTouchImageListener(private val imageView: ImageView) : OnTouchListener {

    private val oldMatrix = Matrix() // 图片移动前的矩阵
    private val newMatrix = Matrix() // 图片移动后的矩阵

    // 按下时手指的x、y轴偏移量
    private var downX = 0f
    private var downY = 0f

    // 图片移动后的x和y轴偏移量
    private var moveX = 0f
    private var moveY = 0f

    private var startDistance = 0f  // 第二根手指按下时与第一根手指的距离
    private var lastDistance = 0f   // 其中一根手指移动后与另一根手指的距离
    private var touchType = 0       // 触摸类型, 1是单指触摸，2是双指触摸
    private var midpoint: PointF? = null    // 第二个手指移动时与第一根手指的中点坐标

    @SuppressLint("ClickableViewAccessibility", "InlinedApi")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                touchType = 1 // 标记为单指触摸
                downX = event.x
                downY = event.y
                oldMatrix.set(imageView.imageMatrix) // 把imageView的矩阵设置为oldMatrix矩阵
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                touchType = 2 // 标记双指触摸
                startDistance = getDistance(event) // 获取第二根手指按下时与第一根手指的距离
                midpoint = getPoint(event) // 获取第二个手指移动时与第一根手指的中点坐标
                oldMatrix.set(imageView.imageMatrix) // 把imageView的矩阵设置为oldMatrix矩阵
            }
            MotionEvent.ACTION_MOVE -> {
                // 放在ACTION_MOVE里改变ScaleType，不会看到改变时瞬间产生的一闪的情况。
                imageView.scaleType = ImageView.ScaleType.MATRIX
                newMatrix.set(oldMatrix)
                moveX = event.x - downX
                moveY = event.y - downY
                if (touchType == 1) {
                    newMatrix.postTranslate(moveX, moveY)
                } else if (touchType == 2) {
                    lastDistance = getDistance(event)
                    val scaling = lastDistance / startDistance // 缩放比例=新的移动距离/之前的移动距离
                    newMatrix.postScale(scaling, scaling, midpoint!!.x, midpoint!!.y) // 对图像矩阵进行缩放
                }
            }
            MotionEvent.ACTION_UP -> touchType = 0
            MotionEvent.ACTION_POINTER_UP -> {
                oldMatrix.set(imageView.imageMatrix)
                touchType = 1
            }
            else -> {}
        }
        imageView.imageMatrix = newMatrix //改变图像容器中的图片
        return true // 消费掉此事件
    }

    /**
     * 获取两个点之间的距离
     *
     * @param event MotionEvent
     * @return 距离
     */
    private fun getDistance(event: MotionEvent): Float {
        val a = event.getX(1) - event.getX(0)
        val b = event.getY(1) - event.getY(0)
        return Math.sqrt((a * a + b * b).toDouble()).toFloat() // 平方根(勾股定理)
    }

    /**
     * 获取两个点的中间点坐标， 第一个点的x坐标+ 上第二个点的x坐标 结果/2 就是他们的中心点， y坐标同理。
     *
     * @param event MotionEvent
     * @return 中间点坐标
     */
    private fun getPoint(event: MotionEvent): PointF {
        val x = (event.getX(0) + event.getX(1)) / 2
        val y = (event.getY(0) + event.getY(1)) / 2
        return PointF(x, y)
    }
}