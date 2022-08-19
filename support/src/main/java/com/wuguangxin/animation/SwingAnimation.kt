package com.wuguangxin.animation

import kotlin.jvm.JvmOverloads
import android.view.animation.Animation
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.animation.Transformation

/**
 * 摇摆动画
 */
class SwingAnimation @JvmOverloads constructor(
    context: Context?,
    private val mCount: Int,
    angle: Float,
    pivotXType: Int = RELATIVE_TO_SELF,
    pivotXValue: Float = 0.5f,
    pivotYType: Int = RELATIVE_TO_SELF,
    pivotYValue: Float = 0f
) : Animation(context, null), Animation.AnimationListener {
    var fromDegrees: Float
    var toDegrees: Float
    private var mPivotXType = ABSOLUTE
    private var mPivotYType = ABSOLUTE
    private var mPivotXValue = 0.0f
    private var mPivotYValue = 0.0f
    private var mPivotX = 0f
    private var mPivotY = 0f
    private var mAngle = 15f
    private val singleSize: Float
    private var currentCount: Float

    init {
        currentCount = mCount.toFloat()
        mAngle = angle
        singleSize = mAngle * 2 / mCount
        fromDegrees = 0f
        toDegrees = mAngle
        mPivotXValue = pivotXValue
        mPivotXType = pivotXType
        mPivotYValue = pivotYValue
        mPivotYType = pivotYType
        startOffset = 200 // 在为重复执行前设置偏移，当重复执行后，设置偏移为0
        setAnimationListener(this)
        initializePivotPoint()
    }

    fun stop() {
        fromDegrees = 0f
        toDegrees = 0f
    }

    private fun initializePivotPoint() {
        if (mPivotXType == ABSOLUTE) {
            mPivotX = mPivotXValue
        }
        if (mPivotYType == ABSOLUTE) {
            mPivotY = mPivotYValue
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val degrees = fromDegrees + (toDegrees - fromDegrees) * interpolatedTime // -10 + ((10+10) * -10) = -210
        val scale = scaleFactor
        if (mPivotX == 0.0f && mPivotY == 0.0f) {
            t.matrix.setRotate(degrees)
        } else {
            t.matrix.setRotate(degrees, mPivotX * scale, mPivotY * scale)
        }
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth)
        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight)
    }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {}
    override fun onAnimationRepeat(animation: Animation) {
        startOffset = 0
        currentCount--
        if (currentCount <= 0) {
            toDegrees = 0f
        } else {
            if (currentCount % 2 == 0f) {
                toDegrees = singleSize * currentCount
            } else {
                fromDegrees = -singleSize * currentCount
            }
        }
    }
}