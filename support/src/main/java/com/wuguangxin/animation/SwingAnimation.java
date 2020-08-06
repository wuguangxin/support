package com.wuguangxin.animation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 摇摆动画
 */
public class SwingAnimation extends Animation implements Animation.AnimationListener {
    private float mFromDegrees;
    private float mToDegrees;

    private int mPivotXType = ABSOLUTE;
    private int mPivotYType = ABSOLUTE;
    private float mPivotXValue = 0.0f;
    private float mPivotYValue = 0.0f;

    private float mPivotX;
    private float mPivotY;

    private int mCount;
    private float mAngle = 15;
    private float singleSize;
    private float currentCount;

    public float getToDegrees() {
        return mToDegrees;
    }

    public void setToDegrees(float mToDegrees) {
        this.mToDegrees = mToDegrees;
    }

    public float getFromDegrees() {
        return mFromDegrees;
    }

    public void setFromDegrees(float mFromDegrees) {
        this.mFromDegrees = mFromDegrees;
    }

    public void stop() {
        this.mFromDegrees = 0;
        this.mToDegrees = 0;
    }

    public SwingAnimation(Context context, int count, float angle) {
        this(context, count, angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
    }

    public SwingAnimation(Context context, int count, float angle, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        super(context, null);
        mCount = count;
        currentCount = mCount;

        mAngle = angle;
        singleSize = mAngle * 2 / count;

        mFromDegrees = 0;
        mToDegrees = mAngle;

        mPivotXValue = pivotXValue;
        mPivotXType = pivotXType;
        mPivotYValue = pivotYValue;
        mPivotYType = pivotYType;

        setStartOffset(200); // 在为重复执行前设置偏移，当重复执行后，设置偏移为0
        setAnimationListener(this);
        initializePivotPoint();
    }

    private void initializePivotPoint() {
        if (mPivotXType == ABSOLUTE) {
            mPivotX = mPivotXValue;
        }
        if (mPivotYType == ABSOLUTE) {
            mPivotY = mPivotYValue;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degrees = mFromDegrees + ((mToDegrees - mFromDegrees) * interpolatedTime); // -10 + ((10+10) * -10) = -210
        float scale = getScaleFactor();

        if (mPivotX == 0.0f && mPivotY == 0.0f) {
            t.getMatrix().setRotate(degrees);
        } else {
            t.getMatrix().setRotate(degrees, mPivotX * scale, mPivotY * scale);
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        setStartOffset(0);
        currentCount--;
        if (currentCount <= 0) {
            mToDegrees = 0;
        } else {
            if (currentCount % 2 == 0) {
                mToDegrees = singleSize * currentCount;
            } else {
                mFromDegrees = -singleSize * currentCount;
            }
        }
    }
}
