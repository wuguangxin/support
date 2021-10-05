package com.wuguangxin.simple.ui;

import com.wuguangxin.simple.ui.test.AppBarLayout;
import com.wuguangxin.simple.ui.test.OverScroller;

import java.lang.reflect.Field;

/**
 * 修复 CoordinatorLayout+AppBarLayout快速滑动抖动问题
 *
 * AppbarLayout在fling的时候，再主动滑动RecyclerView导致的动画错误
 */
// 解决不了抖动问题，反射不到 OverScroller scroller;
public class FixAppBarLayoutBehavior extends AppBarLayout.Behavior {

    private static final String TAG = "FixAppBarLayoutBehavior";
    OverScroller overScroller;

    public FixAppBarLayoutBehavior() {
    }


    public void stopAnimation() {
        initOverScroller();
        System.out.println("overScroller = " + overScroller);
        if (overScroller != null) {
            overScroller.abortAnimation();
        }
    }

    private void initOverScroller() {
        if (overScroller == null) {
            Object scroller = getSuperSuperField(this, "scroller");

            if (scroller == null) {
                scroller = getSuperSuperField(this.getClass(), "mScroller");
            }
            if (scroller instanceof OverScroller) {
                overScroller = (OverScroller) scroller;
            }
        }
    }

    private Object getSuperSuperField(Object paramClass, String paramString) {
        Field field = null;
        Object object = null;
        try { //
            Class<?> clazz = paramClass.getClass();
            Class<?> superclass = clazz.getSuperclass();
            while (!"HeaderBehavior".equals(superclass.getSimpleName())) {
                superclass = superclass.getSuperclass();
            }
//            field = superclass.getDeclaredField(paramString);
            field = superclass.getDeclaredField("age");
            field.setAccessible(true);
            object = field.get(paramClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
      }



}
