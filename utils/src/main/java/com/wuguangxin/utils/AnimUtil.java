package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.wuguangxin.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 动画工具类
 * Created by wuguangxin on 2015/4/1
 */
public class AnimUtil {

    /*
    详情参考：https://blog.csdn.net/qq_27061049/article/details/104537931
    AccelerateDecelerateInterpolator	先加速再减速（开始和结束慢，中间快）
    AccelerateInterpolator    加速（开始慢，结束快）
    DecelerateInterpolator    减速（开始快，结束慢）
    LinearInterpolator	其变化速率恒定
    AnticipateInterpolator 沿着开始相反的方向先运行
    OvershootInterpolator 结束后顺着结束的运行规律让然运行一段时间
    AnticipateOvershootInterpolator AnticipateInterpolator 和 OvershootInterpolator 的结合
    BounceInterpolator	自由落体规律运动
    CycleInterpolator	    其速率为正弦曲线
    LinearOutSlowInInterpolator 其变化先匀速再减速
    FastOutSlowInInterpolator 其变化是先加速，然后减速
    FastOutLinearInInterpolator 其变化先加速然后匀速，本质还是加速运动

    android:toXDelta="100%"：表示自身的100%，也就是从View自己的位置开始。
    android:toXDelta="80%p"：表示父层View的80%，是以它父层View为参照的。
    */

    private final static int DEF_ANIM_DURATION = 300; // 默认动画时长

    /**
     * 加载动画。
     *
     * @param context 上下文
     * @param animRes 动画资源ID
     * @return
     */
    public static Animation loadAnim(Context context, int animRes) {
        return AnimationUtils.loadAnimation(context, animRes);
    }

    /**
     * 从左边滑入的位移动画。
     *
     * @return
     */
    public static TranslateAnimation getLeftIn() {
        return getLeftIn(DEF_ANIM_DURATION);
    }

    /**
     * 从左边滑入的位移动画。
     *
     * @param duration 时长（毫秒）
     * @return
     */
    public static TranslateAnimation getLeftIn(long duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(duration);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;
//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(translate);
//        return set; //
//        return loadAnim(context, R.anim.xin_anim_left_in);
    }

    /**
     * 从左边滑出的位移动画。
     *
     * @return
     */
    public static TranslateAnimation getLeftOut() {
        return getLeftOut(DEF_ANIM_DURATION);
    }

    /**
     * 从左边滑出的位移动画。
     *
     * @param duration 动画时长毫秒值
     * @return
     */
    public static TranslateAnimation getLeftOut(long duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, -1.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(duration);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;

//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(translate);
//        set.setFillEnabled(true);
//        set.setFillAfter(true);
//        return set; //
//        return loadAnim(context, R.anim.xin_anim_left_out);
    }

    /**
     * 从顶部滑入的位移动画。
     *
     * @return
     */
    public static TranslateAnimation getTopIn() {
        return getTopIn(400);
    }

    /**
     * 从顶部滑入的位移动画。
     *
     * @param duration 动画时长毫秒值
     * @return
     */
    public static TranslateAnimation getTopIn(int duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, -1.0F,
                Animation.RELATIVE_TO_SELF, 0.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(duration);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;

//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(translate);
//        return set; //
//        return loadAnim(context, R.anim.xin_anim_top_in);
    }

    /**
     * 从顶部滑出的位移动画。
     *
     * @return
     */
    public static TranslateAnimation getTopOut() {
        return getTopOut(DEF_ANIM_DURATION);
    }

    /**
     * 从顶部滑出的位移动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static TranslateAnimation getTopOut(int duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, -1.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(duration);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;

//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(translate);
//        return set; //
//        return loadAnim(context, R.anim.xin_anim_top_out);
    }

    /**
     * 从右边滑入的位移动画。
     *
     * @return
     */
    public static TranslateAnimation getRightIn() {
        return getRightIn(DEF_ANIM_DURATION);
    }

    /**
     * 从右边滑入的位移动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static TranslateAnimation getRightIn(int duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0F,
                Animation.RELATIVE_TO_PARENT, 0.0F,
                Animation.RELATIVE_TO_PARENT, 0.0F,
                Animation.RELATIVE_TO_PARENT, 0.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(duration);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;
//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(translate);
//        return set; //
//        return loadAnim(context, R.anim.xin_anim_right_in);
    }

    /**
     * 从右边滑出的位移动画。
     *
     * @return
     */
    public static TranslateAnimation getRightOut() {
        return getRightOut(DEF_ANIM_DURATION);
    }

    /**
     * 从右边滑出的位移动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static TranslateAnimation getRightOut(int duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0F,
                Animation.RELATIVE_TO_PARENT, 1.0F,
                Animation.RELATIVE_TO_PARENT, 0.0F,
                Animation.RELATIVE_TO_PARENT, 0.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(duration);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;
    }

    /**
     * 从底部滑入的位移动画。
     *
     * @return
     */
    public static TranslateAnimation getBottomIn() {
        return getBottomIn(DEF_ANIM_DURATION);
    }

    /**
     * 从底部滑入的位移动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static TranslateAnimation getBottomIn(int duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 1.0F,
                Animation.RELATIVE_TO_PARENT, 0.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(duration);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;

//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(translate);
//        set.setFillAfter(true);
//        set.setFillEnabled(true);
//        return set; //
//        return loadAnim(context, R.anim.xin_anim_bottom_in);
    }

    /**
     * 从底部滑出的位移动画。
     *
     * @return
     */
    public static TranslateAnimation getBottomOut() {
        return getBottomOut(DEF_ANIM_DURATION);
    }

    /**
     * 从底部滑出的位移动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static TranslateAnimation getBottomOut(int duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_PARENT, 0.0F,
                Animation.RELATIVE_TO_SELF, 1.0F);
        translate.setInterpolator(new AccelerateDecelerateInterpolator());
        translate.setDuration(duration);
        translate.setFillEnabled(true);
        translate.setFillAfter(true); // 让动画停留在最后一帧
        return translate;

//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(translate);
//        set.setFillAfter(true); // 动画停留在最后一帧
//        set.setFillEnabled(true);
//        return set;
//        return loadAnim(context, R.anim.xin_anim_bottom_out);
    }

    /**
     * 创建淡入效果的动画。
     *
     * @return
     */
    public static AlphaAnimation getFadeIn() {
        return getFadeIn(DEF_ANIM_DURATION);
    }

    /**
     * 创建淡入效果的动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static AlphaAnimation getFadeIn(int duration) {
        AlphaAnimation alpha = new AlphaAnimation(0.0F, 1.0F);
        alpha.setDuration(duration);
        return alpha;

//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(alpha);
//        return set;
//        return loadAnim(context, R.anim.xin_anim_fade_in);
    }

    /**
     * 创建淡出效果的动画。
     *
     * @return
     */
    public static AlphaAnimation getFadeOut() {
        return getFadeOut(DEF_ANIM_DURATION);
    }

    /**
     * 淡出动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static AlphaAnimation getFadeOut(int duration) {
        AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.0F);
        alpha.setDuration(duration);
        return alpha;

//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(alpha);
//        return set;
//        return loadAnim(context, R.anim.xin_anim_fade_out);
    }

    /**
     * 以自身为圆心，左右摇摆20度的动画
     *
     * @return
     */
    public static RotateAnimation getRotate20() {
        return getRotate20(1000);
    }

    /**
     * 以自身为圆心，左右旋转20度的动画。比如用于消息图标摇晃提醒功能。
     *
     * @param duration 动画时长
     * @return
     */
    public static RotateAnimation getRotate20(int duration) {
//        float fromDegrees,
//        float toDegrees,
//        int pivotXType,
//        float pivotXValue,
//        int pivotYType,
//        float pivotYValue
        RotateAnimation rotate = new RotateAnimation(-20, 20, // 旋转幅度 -20度到20度
                Animation.RELATIVE_TO_SELF, 0.5F, // 50%
                Animation.RELATIVE_TO_SELF, 0.5F  // 50%
        );
        rotate.setDuration(duration); // 执行时长
        rotate.setRepeatCount(Animation.INFINITE); // infinite 无限次数
        rotate.setRepeatMode(Animation.REVERSE); // reverse 循环播放
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());
        return rotate;

//        AnimationSet set = new AnimationSet(false);
//        set.setInterpolator(new AccelerateInterpolator()); // @android:anim/accelerate_interpolator
//        set.addAnimation(rotate);
//        return set;
//        return loadAnim(context, R.anim.xin_anim_rotate_20);
    }

    /**
     * 以自身为圆心，做360度旋转的动画。
     *
     * @return
     */
    public static RotateAnimation getRotate360() {
        return getRotate360(1000);
    }

    /**
     * 以自身为圆心，做360度旋转的动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static RotateAnimation getRotate360(int duration) {
        RotateAnimation rotate = new RotateAnimation(0, 360, // 360度旋转
                Animation.RELATIVE_TO_SELF, 0.5F, // 50%
                Animation.RELATIVE_TO_SELF, 0.5F // 50%
        );
        rotate.setDuration(duration); // 执行时长
        rotate.setRepeatCount(Animation.INFINITE); // infinite 无限次数
        rotate.setRepeatMode(Animation.INFINITE); // reverse 循环播放
        rotate.setInterpolator(new LinearInterpolator()); // 加这个插值器，可以匀速旋转不停顿
        return rotate;

//        AnimationSet set = new AnimationSet(true);
//        set.setInterpolator(new AccelerateInterpolator()); // @android:anim/accelerate_interpolator
//        set.addAnimation(rotate);
//        return set;
//        return loadAnim(context, R.anim.xin_anim_rotate_360);
    }

    /**
     * 自身三倍进行放大+淡入的组合动画。
     *
     * @return
     */
    public static AnimationSet getZoomOut() {
        return getZoomOut(DEF_ANIM_DURATION);
    }

    /**
     * 自身三倍进行放大+淡入的组合动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static AnimationSet getZoomOut(int duration) {
        AlphaAnimation alpha = new AlphaAnimation(0.0F, 1.0F);
        alpha.setDuration(duration);
        alpha.setRepeatCount(0);

        ScaleAnimation scale = new ScaleAnimation(
                3.0F, 1.0F, 3.0F, 1.0F,
                Animation.RELATIVE_TO_SELF, 0.5F, // 50%
                Animation.RELATIVE_TO_SELF, 0.5F); // 50%
        scale.setDuration(duration);
        scale.setRepeatCount(0);

        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new AccelerateInterpolator());
        set.addAnimation(alpha);
        set.addAnimation(scale);

        return set;
        // return loadAnim(context, R.anim.xin_anim_zoomout);
    }


    /**
     * 从自身三倍大小进行缩小+淡出的组合动画。
     *
     * @return
     */
    public static AnimationSet getZoomIn() {
        return getZoomIn(DEF_ANIM_DURATION);
    }


    /**
     * 从自身三倍大小进行缩小+淡出的组合动画。
     *
     * @param duration 动画时长
     * @return
     */
    public static AnimationSet getZoomIn(int duration) {
        AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.0F);
        alpha.setDuration(duration);
        alpha.setRepeatCount(0);

        ScaleAnimation scale = new ScaleAnimation(
                1.0F, 3.0F, 1.0F, 3.0F,
                Animation.RELATIVE_TO_SELF, 0.5F, // 50%
                Animation.RELATIVE_TO_SELF, 0.5F); // 50%
        scale.setDuration(duration);
        scale.setRepeatCount(0);

        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new AccelerateInterpolator()); // 加速
        set.addAnimation(alpha);
        set.addAnimation(scale);

        return set;
        // return loadAnim(context, R.anim.xin_anim_zoomout);
    }

    /**
     * 左右摆动5次的动画。适用于让View摇晃引起用户的注意。
     *
     * @return
     */
    public static TranslateAnimation getShakeLR() {
        return getShakeLR(DEF_ANIM_DURATION);
    }

    /**
     * 左右摆动5次的动画。适用于让View摇晃引起用户的注意。
     *
     * @param duration 动画时长
     * @return
     */
    public static TranslateAnimation getShakeLR(int duration) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 5.0F,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, 0.0F);
        translate.setDuration(duration);
        translate.setInterpolator(new CycleInterpolator(5));
        return translate;

//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(translate);
//        return set; //
//        return loadAnim(context, R.anim.xin_anim_shake_view);
    }

    /**
     * 以动画从左边滑入的方式显示view
     *
     * @param view 要执行动画的view
     */
    public static void startLeftIn(View view) {
        if (view != null) startAnim(view, View.VISIBLE, getLeftIn());
    }

    /**
     * 以动画从顶部滑入的方式显示view
     *
     * @param view 要执行动画的view
     */
    public static void startTopIn(View view) {
        if (view != null) startAnim(view, View.VISIBLE, getTopIn());
    }

    /**
     * 以动画从右边滑入的方式显示view
     *
     * @param view 要执行动画的view
     */
    public static void startRightIn(View view) {
        if (view != null) startAnim(view, View.VISIBLE, getRightIn());
    }

    /**
     * 以动画从底部滑入的方式显示view
     *
     * @param view 要执行动画的view
     */
    public static void startBottomIn(View view) {
        if (view != null) startAnim(view, View.VISIBLE, getBottomIn());
    }

    /**
     * 以动画从左边滑出的方式隐藏view
     *
     * @param view 要执行动画的view
     */
    public static void startLeftOut(View view) {
        if (view != null) startAnim(view, View.GONE, getLeftOut());
    }

    /**
     * 以动画从顶部滑出的方式隐藏view
     *
     * @param view 要执行动画的view
     */
    public static void startTopOut(View view) {
        if (view != null) startAnim(view, View.GONE, getTopOut());
    }

    /**
     * 以动画从右边滑出的方式隐藏view
     *
     * @param view 要执行动画的view
     */
    public static void startRightOut(View view) {
        if (view != null) startAnim(view, View.GONE, getRightOut());
    }

    /**
     * 以动画从底部滑出的方式隐藏view
     *
     * @param view 要执行动画的view
     */
    public static void startBottomOut(View view) {
        if (view != null) startAnim(view, View.GONE, getBottomOut());
    }

    /**
     * 以动画淡入的方式显示view
     *
     * @param view 要执行动画的view
     */
    public static void startFadeIn(View view) {
        if (view != null) startAnim(view, View.VISIBLE, getFadeIn());
    }

    /**
     * 以动画淡出的方式隐藏view
     *
     * @param view 要执行动画的view
     */
    public static void startFadeOut(View view) {
        if (view != null) startAnim(view, View.GONE, getFadeOut());
    }

    /**
     * 以动画淡入的方式显示view（蒙层效果）
     *
     * @param view 要执行动画的view
     */
    public static void startAlphaLayerFadeIn(View view) {
        if (view != null) startAnim(view, View.VISIBLE, getFadeIn());
    }

    /**
     * 以动画淡出的方式隐藏view（蒙层效果）
     *
     * @param view 要执行动画的view
     */
    public static void startAlphaLayerFadeOut(View view) {
        if (view != null) startAnim(view, View.GONE, getFadeOut(300));
    }

    /**
     * 以自身为圆心，左右各20度摆动（如钟摆）
     *
     * @param view 要执行动画的view
     */
    public static void startRotate20(View view) {
        if (view != null) startAnim(view, View.VISIBLE, getRotate20());
    }

    /**
     * 以自身为圆心，360度循环旋转view。
     *
     * @param view 要执行动画的view
     */
    public static void startRotate360(View view) {
        if (view != null) startAnim(view, View.VISIBLE, getRotate360());
    }

    /**
     * 让view开始执行动画。
     *
     * @param view 执行动画的View
     * @param visibility view是否显示
     * @param anim 动画具体实现对象
     */
    public static void startAnim(View view, int visibility, Animation anim) {
        if (view != null) {
            view.setVisibility(visibility);
            if (anim != null) {
                view.startAnimation(anim);
            }
        }
    }


    /**
     * Activity 打开时动画，在Activity.startActivity()后调用
     *
     * @param activity 源Activity
     */
    public static void animEnter(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.xin_anim_activity_open_enter, R.anim.xin_anim_activity_open_exit);
        }
    }

    /**
     * Activity 关闭时动画，在Activity.finish()后调用
     *
     * @param activity 源Activity
     */
    public static void animClose(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.xin_anim_activity_close_enter, R.anim.xin_anim_activity_close_exit);
        }
    }

    /**
     * Lock Activity 打开时动画，在Activity.startActivity()后调用
     *
     * @param activity 源Activity
     */
    public static void animEnterLock(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.xin_anim_fade_in, R.anim.xin_anim_fade_out);
        }
    }

    /**
     * Lock Activity 关闭时动画，在Activity.finish()后调用
     *
     * @param activity 源Activity
     */
    public static void animCloseLock(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.xin_anim_fade_in, R.anim.xin_anim_fade_out);
        }
    }

    /**
     * Lock Activity 打开时动画，在Activity.startActivity()后调用
     *
     * @param activity 源Activity
     */
    public static void animEnterLockDialog(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.xin_anim_dialog_in, R.anim.xin_anim_dialog_out);
        }
    }

    /**
     * Lock Activity 关闭时动画，在Activity.finish()后调用
     *
     * @param activity 源Activity
     */
    public static void animCloseLockDialog(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.xin_anim_dialog_in, R.anim.xin_anim_dialog_out);
        }
    }

    /**
     * 让View钟摆方式摆动
     *
     * @param view view
     * @param angle 距离中心点(0)偏移角度
     * @param duration 执行时长
     * @param count 摆动次数
     */
    public static SwingAnimation startSwingAnimation(View view, float angle, long duration, int count) {
        final SwingAnimation swingAnim = new SwingAnimation(view.getContext(), count, angle);
        swingAnim.setDuration(duration);
        swingAnim.setRepeatCount(count);
        swingAnim.setRepeatMode(Animation.REVERSE);
        view.startAnimation(swingAnim);
        return swingAnim;
    }

    /**
     * 模仿摇摆动画
     */
    public static class SwingAnimation extends Animation implements Animation.AnimationListener {
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

    /**
     * 让View震动，如果是EditText，则自动获取焦点，并把输入光标置于字符串后面。
     *
     * @param context 上下文
     * @param shakeView 要震动的View对象
     * @param msg 震动时提示的Toast消息，如果为null，则不提示消息
     */
    public static void shakeView(Context context, View shakeView, String msg) {
        shakeView(context, null, shakeView, msg);
    }

    /**
     * 让View震动，如果是EditText，则自动获取焦点，并把输入光标置于字符串后面。
     * （需要震动权限）
     *
     * @param context 上下文
     * @param clickView 触发点击事件的View（只是让此View可点击）
     * @param shakeView 要震动的View对象
     * @param toastMsg 震动时提示的Toast消息，如果为null，则不提示消息
     */
    @SuppressLint("MissingPermission")
    public static void shakeView(Context context, View clickView, View shakeView, String toastMsg) {
        if (clickView != null) {
            clickView.setEnabled(true);
        }
        if (shakeView != null) {
            Vibrator vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                // vibrator.vibrate(new long[]{100,50,200,50}, 1); // 使用震动规则
                vibrator.vibrate(100); // 震动时长（毫秒）

                // Animation shakeAnimation = loadAnim(context, R.anim.xin_anim_shake_view);
                // 构建一个水平位移的动画，代替上面的xml动画配置文件
                // float fromXDelta = 0
                // float toXDelta = 5
                // float fromYDelta = 0
                // float toYDelta = 0
                Animation shakeAnimation = new TranslateAnimation(0, 5, 0, 0);
                shakeAnimation.setDuration(800);
                shakeAnimation.setInterpolator(new CycleInterpolator(5)); //

                shakeView.startAnimation(shakeAnimation); // 震动view
                shakeView.setFocusable(true);
                shakeView.setEnabled(true);
                shakeView.setFocusableInTouchMode(true);
                if (shakeView instanceof EditText) {
                    shakeView.requestFocus(); // 获取焦点，位置在内容后面
                    Editable text = ((EditText) shakeView).getText();
                    Selection.setSelection(text, text.length());
                }
            }
        }
        if (!TextUtils.isEmpty(toastMsg)) {
            ToastUtils.showToast(context, toastMsg);
        }
    }

    /**
     * 给 ViewGroup 的 child 做动画，特别最适合给列表的item项动画。
     * 此动画效果可以换一种方式做，就是在继承至ViewGroup的xml属性中增加：android:layoutAnimation="@anim/xin_anim_layout"
     *
     * @param viewGroup
     */
    public static void startAnimChild(ViewGroup viewGroup) {
        if (viewGroup != null) {
            // 创建LayoutAnimationController方式1:
            // java：
//            Animation translate = new TranslateAnimation(
//                    Animation.RELATIVE_TO_SELF, -1.0F,
//                    Animation.RELATIVE_TO_SELF, 0.0F,
//                    Animation.RELATIVE_TO_SELF, 0.0F,
//                    Animation.RELATIVE_TO_SELF, 0.0F);
//            translate.setDuration(300);
//            AlphaAnimation alpha = new AlphaAnimation(0.0F, 1.0F);
//            alpha.setInterpolator(new DecelerateInterpolator());
//            alpha.setRepeatCount(0);
//            alpha.setDuration(500);
//            AnimationSet animation = new AnimationSet(false);
//            animation.addAnimation(translate);
//            animation.addAnimation(alpha);
            // xml：
            Animation animation = AnimationUtils.loadAnimation(viewGroup.getContext(), android.R.anim.fade_in);
            LayoutAnimationController controller = new LayoutAnimationController(animation);

            // 创建LayoutAnimationController方式2:
            // LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(viewGroup.getContext(), R.anim.xin_anim_left_in_fade_in);

            //设置控件显示的顺序
            controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
            //设置控件显示间隔时间
            controller.setDelay(300);
            // 为ListView设置LayoutAnimationController属性；
            viewGroup.setLayoutAnimation(controller);
            viewGroup.startLayoutAnimation();
        }
    }

    //============================================================================================
    //============以下代码是给列表的Item做动画========================================================

    private static boolean isRepeatItemAnim = true; // 已经动画过的item是否可以再次动画
    private static final long ANIM_DURATION = 500; // 动画的时长
    private static int animParentViewHeight; // 列表的高
    private static int animItemViewHeight; // item的高
    private static List<Integer> animItemPositionList; // 存储已经动画过的item的position集合
    private static WeakReference<View> animParentView; // 进行动画的列表

    /**
     * 创建item动画记录集合（对应的是item所在的位置集合）
     */
    public static void initAnimItemPositionList() {
        if (animItemPositionList == null) {
            animItemPositionList = new ArrayList<>();
        } else {
            animItemPositionList.clear();
        }
    }

    /**
     * 重置item动画记录集合
     */
    public static void clearAnimItemPositionList() {
        if (animItemPositionList != null) {
            animItemPositionList.clear();
            animItemPositionList = null;
        }
    }

    /**
     * 给ListView的Item做逐个淡出的动画
     */
    public static void setAnimParentView(View view) {
        animParentView = new WeakReference<>(view);
        initAnimItemPositionList();
        getAnimParentViewHeight();
    }

    public static void getAnimParentViewHeight() {
        if (animParentView != null) {
            ViewTreeObserver viewTreeObserver = animParentView.get().getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    animParentViewHeight = animParentView.get().getMeasuredHeight();
                    return true;
                }
            });
        }
    }

    public static void getAnimItemViewHeight(Context context, final View view) {
        if (view != null && animItemViewHeight == 0) {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    animItemViewHeight = view.getMeasuredHeight();
                    return true;
                }
            });
        }
    }

    /**
     * 给List的item设置动画
     *
     * @param context
     * @param position 位置
     * @param itemView
     */
    public static void startItemAnim(Context context, int position, View itemView) {
        if (!isRepeatItemAnim && animItemPositionList.contains(position)) {
            return;
        }
        animItemPositionList.add(position);
        getAnimItemViewHeight(context, itemView);

        // itemView.startAnimation(BaseActivity.right_in); //动画1
        // startRotateAnimation(position, itemView); //动画2
        startTranslateAnimation(context, position, itemView);  // 动画3 当前使用
    }

    /**
     * 给List中的Item增加TranslateAnimation动画
     *
     * @param context
     * @param position 位置
     * @param itemView item对应的View
     */
    public static void startTranslateAnimation(Context context, int position, View itemView) {
        AnimationSet animationSet = new AnimationSet(true);
        // 透明动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(ANIM_DURATION);
        // 位移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,   // x轴
                Animation.RELATIVE_TO_SELF, 0f,     //
                Animation.RELATIVE_TO_SELF, 1f,   // y轴
                Animation.RELATIVE_TO_SELF, 0f);    //
        translateAnimation.setDuration(ANIM_DURATION);
        // 动画开始时间逐渐延后
        if (animParentViewHeight > 0 && animItemViewHeight > 0) {
            // 可见的item数量
            int canSeeItemCount = animParentViewHeight / animItemViewHeight;
            if (position < canSeeItemCount + 1) {
                animationSet.setStartOffset(position * 100);
            }
        }
        animationSet.addAnimation(translateAnimation); // 位移动画
        animationSet.addAnimation(alphaAnimation); // 透明动画
        animationSet.setFillAfter(true);
        itemView.startAnimation(animationSet);
    }

    /**
     * 给列表中的 item 增加 RotateAnimation 动画
     *
     * @param position 位置
     * @param itemView item 对应的 View
     */
    public static void startRotateAnimation(int position, View itemView) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                0.0f, 350.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);
        // mRotateAnimation.setStartOffset(position * 500);
        itemView.startAnimation(rotateAnimation);
    }

    //============以上代码是给列表的Item做动画 END====================================================

}
