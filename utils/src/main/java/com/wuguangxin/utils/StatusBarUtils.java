package com.wuguangxin.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 系统状态栏工具类（状态栏颜色、沉浸式）
 * Created by wuguangxin on 2017/12/21.
 */
public class StatusBarUtils {
    /**
     * @param activity
     * @param viewGroup
     * @param dark
     * @param color 状态栏背景色（SDK >= 21时生效）
     */
    public static void setImmersionStatusBar(Activity activity, ViewGroup viewGroup, boolean dark, int color) {
        if (activity == null || viewGroup == null) {
            return;
        }
        // >=19
        if (Build.VERSION.SDK_INT >= 19) {
            // 透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏。注意华为和HTC等有虚拟HOME键盘的，如果不设置下面这段代码，虚拟键盘将覆盖APP底部界面，无法操作底部TAB
//				activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (Build.VERSION.SDK_INT < 21) {
                // <21
                // android:fitsSystemWindows=""，
                // false：布局不受StatusBar的影响，可以完全的展示在StatusBar的下面。
                // true：布局不受StatusBar的影响，不会被StatusBar遮住，
                // android:clipToPadding="false"
                // false：布局不受Padding的影响，可以展示在Padding的区域。其实fitsSystemWindows就是设置一个Padding使View不会展示在StatusBar的下方，
                // 设置系统是否需要考虑 StatusBar 占据的区域来显示
                viewGroup.setFitsSystemWindows(false);
                viewGroup.setClipToPadding(true);
            } else {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().setStatusBarColor(color);
            }
        }
    }


    public static void setImmersionStatusBar(Activity activity, int statusBarColor) {
        Window window = activity.getWindow();
        // 设置全屏
//         window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 3.让内容延伸至刘海区域
        if (Build.VERSION.SDK_INT >= 28 /*Build.VERSION_CODES.P*/) {
            // int LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT = 0; // 全屏模式内容下移，非全屏不受影响
            // int LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES = 1; // 允许内容延伸进入刘海区域
            // int LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER = 2; // 不允许内容延伸进入刘海区域
            WindowManager.LayoutParams params = window.getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        if (Build.VERSION.SDK_INT == 20) {
            // 透明状态栏（android 4.4 API 19+可以使用)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // api 19+
        } else if (Build.VERSION.SDK_INT >= 21) {
            if (statusBarColor == Color.TRANSPARENT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // api 19+
            } else {
                // 注：API19的，通过模拟器看到标题栏延伸到了状态栏底部，所以标题栏应该设置paddingTop=状态栏高度，所以不适配该版本
                // 给状态栏设置透明（android 5.0(SDK21) 可以通过window.setStatusBarColor()来设置状态栏背景色）
                window.setStatusBarColor(statusBarColor);
                //window.setStatusBarColor(Color.TRANSPARENT);
                //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                //window.setNavigationBarColor(Color.RED);
            }
        } else {
            // <21
            // android:fitsSystemWindows=""，
            // false：布局不受StatusBar的影响，可以完全的展示在StatusBar的下面。
            // true：布局不受StatusBar的影响，不会被StatusBar遮住，
            // android:clipToPadding="false"
            // false：布局不受Padding的影响，可以展示在Padding的区域。其实fitsSystemWindows就是设置一个Padding使View不会展示在StatusBar的下方，
            // 设置系统是否需要考虑 StatusBar 占据的区域来显示
//            window.getDecorView().setFitsSystemWindows(false);
//            ((ViewGroup)window.getDecorView()).setClipToPadding(true);
        }
    }

    /**
     * 设置状态栏文字的亮色/暗色
     *
     * @param activity
     * @param dark 是否是暗色
     */
    public static void setStatusBarFontColor(Activity activity, boolean dark) {
        setStatusBarFontStyle_SDK23(activity, dark);
    }

    /**
     * SYSTEM_UI_FLAG_LOW_PROFILE				弱化状态栏和导航栏的图标
     * SYSTEM_UI_FLAG_HIDE_NAVIGATION			隐藏导航栏，用户点击屏幕会显示导航栏
     * SYSTEM_UI_FLAG_FULLSCREEN				隐藏状态栏
     * SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION	拓展布局到导航栏后面
     * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN			拓展布局到状态栏后面
     * SYSTEM_UI_FLAG_LAYOUT_STABLE				稳定的布局，不会随系统栏的隐藏、显示而变化
     * SYSTEM_UI_FLAG_IMMERSIVE					沉浸模式，用户可以交互的界面
     * SYSTEM_UI_FLAG_IMMERSIVE_STICKY			沉浸模式，用户可以交互的界面。同时，用户上下拉系统栏时，会自动隐藏系统栏
     * ————————————————
     * 原文链接：https://blog.csdn.net/QQxiaoqiang1573/article/details/79867127
     *
     * @param activity
     * @param isDarkForStatusBarTextColor
     */
    // Android 6.0 SDK 23+
    private static void setStatusBarFontStyle_SDK23(Activity activity, boolean isDarkForStatusBarTextColor) {
        Window window = activity.getWindow();
        // 改变状态栏的字体颜色
        if (isDarkForStatusBarTextColor) {
            // 灰色字体
            if (Build.VERSION.SDK_INT >= 23) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            // 白色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE); // 0
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格,Flyme和6.0以上
     * 可以用来判断是否为魅族 Flyme用户
     *
     * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setStatusBarFontStyle_Flyme(Activity activity, boolean isFontColorDark) {
        Window window = activity.getWindow();
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = lp.getClass().getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = lp.getClass().getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isFontColorDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 【小米 未生效】设置状态栏字体图标为深色，需要MIUI6以上
     *
     * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setStatusBarFontStyle_MIUI(Activity activity, boolean isFontColorDark) {
        Window window = activity.getWindow();
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (isFontColorDark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void setPaddingTop(Activity activity, int viewId) {
        setPaddingTop(activity, activity.findViewById(viewId));
    }

    public static void setPaddingTop(Activity activity, View view) {
        if (view == null) return;
        // 如果有刘海屏,并且让布局延伸至刘海屏区域内，则设置标题栏的paddingTop=刘海区域高度
        int notchHeight = getNotchHeight(activity);
        int statusBarHeight = getStatusBarHeight(activity);
        int paddingTop = Math.max(notchHeight, statusBarHeight);
        view.setPadding(view.getPaddingLeft(), paddingTop, view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * 获取刘海屏的槽口高度
     * @param activity
     * @return
     */
    public static int getNotchHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= 28 /*Build.VERSION_CODES.P*/) {
            // 判断有没有刘海屏 (获取需要在View绑定到Window之后，否则拿不到)
            WindowInsets rootWindowInsets = activity.getWindow().getDecorView().getRootWindowInsets();
            if (rootWindowInsets != null) {
                DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
                if (displayCutout != null) {
                    // 有刘海屏
                    return displayCutout.getSafeInsetTop();
//                    Log.i(TAG, "getBoundingRects = " + displayCutout.getBoundingRects());
//                    Log.i(TAG, "getSafeInsetLeft = " + displayCutout.getSafeInsetLeft());
//                    Log.i(TAG, "getSafeInsetTop = " + displayCutout.getSafeInsetTop());
//                    Log.i(TAG, "getSafeInsetRight = " + displayCutout.getSafeInsetRight());
//                    Log.i(TAG, "getSafeInsetBottom = " + displayCutout.getSafeInsetBottom());
                }
            }
        }
        return 0;
    }

    /**
     * 获取状态栏高度，一般是24dp
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int status_bar_height = 96; // 一般是24dp
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            status_bar_height = context.getResources().getDimensionPixelSize(identifier);
        }
//		Log.e(TAG, "status_bar_height = " + status_bar_height);
        return status_bar_height;
    }

}