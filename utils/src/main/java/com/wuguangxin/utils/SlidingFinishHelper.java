package com.wuguangxin.utils;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * 滑动关闭Activity辅助类
 * Created by wuguangxin on 2020-04-09.
 */
public class SlidingFinishHelper {
    private Activity activity;
    private VelocityTracker mVelocityTracker;
    private MotionEvent mActionDownEvent;
    private boolean validClose;
    private float downX; // 按下时的X轴
    private static final float MAX_DOWN_X = 100; // 按下时在距左边100px范围内才触发
    private static final float MIN_MOVE_X = 200; // 有效触发距离

    public SlidingFinishHelper(Activity activity) {
        this.activity = activity;
        mVelocityTracker = VelocityTracker.obtain();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (activity == null) {
            return false;
        }
        mVelocityTracker.addMovement(ev);
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (mActionDownEvent != null) {
                mActionDownEvent.recycle();
            }
            mActionDownEvent = MotionEvent.obtain(ev);  // 记录按下时的事件
            downX = mActionDownEvent.getX();
            validClose = downX < MAX_DOWN_X;
        } else if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            if (!validClose)
                return activity.dispatchTouchEvent(ev);
            // 右滑返回手势检测
            int pointerId = ev.getPointerId(0);
            // 获取指定上下文的配置参数对象
            ViewConfiguration configuration = ViewConfiguration.get(activity);
            int maximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
            int minimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
            mVelocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity); // 1秒的速度
            final float velocityX = mVelocityTracker.getXVelocity(pointerId);

            if (downX <= MAX_DOWN_X  // 左边缘检测，可根据需要调整，单位像素
                    && ev.getX() - downX >= MIN_MOVE_X  // 有效触发距离，可根据需要调整，单位像素
                    && Math.abs(velocityX) >= minimumFlingVelocity) {
                activity.finish();
            }
        }
        return activity.dispatchTouchEvent(ev);  // 分发控制还给Activity
    }
}
