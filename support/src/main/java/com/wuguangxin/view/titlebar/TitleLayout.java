package com.wuguangxin.view.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wuguangxin.support.R;
import com.wuguangxin.utils.StatusBarUtils;
import com.wuguangxin.view.ListenerScrollView;
import com.wuguangxin.view.ViewUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;

/**
 * 自定义标题栏
 * <p>
 * Created by wuguangxin on 2022/7/23.
 */
public class TitleLayout extends ConstraintLayout {

    private final static int UNSET = -1;
    private final static float maxScrollY = 300f;

    private TextView mTitle;
    private ImageView mBack;
    private ImageView mMenu;
    private View mDivider;
    private Activity mActivity;
    private CharSequence title;

    private TitleTheme normalTheme = TitleTheme.WHITE.clone(); // 正常时的主题
    private TitleTheme scrollTheme = TitleTheme.WHITE.clone(); // 界面滑动时。要显示的主题
    private float titleBarAlpha = 0F;

    public TitleLayout(Context context) {
        this(context, null);
    }

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttr(context, attrs);
        setTheme();
    }

    private void initView(Context context) {
        ViewGroup viewGroup = (ViewGroup) View.inflate(context, R.layout.title_bar_layout, null);
        int childCount = viewGroup.getChildCount();
        // 孩子搬家，减少一层布局嵌套，同时避免标题栏占用状态栏失败的问题出现
        View[] views = new View[childCount];
        for (int i = 0; i < childCount; i++) {
            views[i] = viewGroup.getChildAt(i);
        }
        viewGroup.removeAllViews();
        for (int i = 0; i < views.length; i++) {
            addView(views[i], i);
        }

        mTitle = this.findViewById(R.id.tv_title_name);
        mBack = this.findViewById(R.id.iv_title_back);
        mMenu = this.findViewById(R.id.iv_title_menu);
        mDivider = this.findViewById(R.id.vi_title_divider);
        mDivider.setVisibility(View.GONE);

        mBack.setOnClickListener(v -> {
            if (mActivity != null) {
                mActivity.finish();
            }
        });
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout);
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            // 先取theme，单属性值需要覆盖theme的值
            if (index == R.styleable.TitleLayout_tl_theme) {
                normalTheme = TitleTheme.findTheme(a.getInt(R.styleable.TitleLayout_tl_theme, UNSET), normalTheme);
            } else if (index == R.styleable.TitleLayout_tl_themeScroll) {
                scrollTheme = TitleTheme.findTheme(a.getInt(R.styleable.TitleLayout_tl_themeScroll, UNSET), scrollTheme);
            } else if (index == R.styleable.TitleLayout_tl_title) {
                title = a.getString(R.styleable.TitleLayout_tl_title);
            } else if (index == R.styleable.TitleLayout_tl_titleSize) {
                setTitleSize(a.getDimension(R.styleable.TitleLayout_tl_titleSize, UNSET));
            } else if (index == R.styleable.TitleLayout_tl_titleMaxEms) {
                setTitleMaxEms(a.getInt(R.styleable.TitleLayout_tl_titleMaxEms, UNSET));
            } else if (index == R.styleable.TitleLayout_tl_titleIconTop) {
                setTitleIconTop(a.getResourceId(R.styleable.TitleLayout_tl_titleIconTop, UNSET));
            } else if (index == R.styleable.TitleLayout_tl_titleIconBottom) {
                setTitleIconBottom(a.getResourceId(R.styleable.TitleLayout_tl_titleIconBottom, UNSET));
            } else if (index == R.styleable.TitleLayout_tl_titleIconStart) {
                setTitleIconStart(a.getResourceId(R.styleable.TitleLayout_tl_titleIconStart, UNSET));
            } else if (index == R.styleable.TitleLayout_tl_titleIconEnd) {
                setTitleIconEnd(a.getResourceId(R.styleable.TitleLayout_tl_titleIconEnd, UNSET));
            } else if (index == R.styleable.TitleLayout_tl_titleIconPadding) {
                setTitleIconPadding((int) a.getDimension(R.styleable.TitleLayout_tl_titleIconPadding, 0));
            } else if (index == R.styleable.TitleLayout_tl_titleColor) {
                normalTheme.setTextColor(a.getColor(R.styleable.TitleLayout_tl_titleColor, normalTheme.getTextColor()));
            } else if (index == R.styleable.TitleLayout_tl_backIcon) {
                normalTheme.setBackIcon(a.getResourceId(R.styleable.TitleLayout_tl_backIcon, normalTheme.getBackIcon()));
            } else if (index == R.styleable.TitleLayout_tl_menuIcon) {
                normalTheme.setMenuIcon(a.getResourceId(R.styleable.TitleLayout_tl_menuIcon, normalTheme.getMenuIcon()));
            } else if (index == R.styleable.TitleLayout_tl_showBack) {
                normalTheme.setShowBack(a.getBoolean(R.styleable.TitleLayout_tl_showBack, normalTheme.isShowBack()));
            } else if (index == R.styleable.TitleLayout_tl_showMenu) {
                normalTheme.setShowMenu(a.getBoolean(R.styleable.TitleLayout_tl_showMenu, normalTheme.isShowMenu()));
            } else if (index == R.styleable.TitleLayout_tl_showDivider) {
                normalTheme.setShowDivider(a.getBoolean(R.styleable.TitleLayout_tl_showDivider, normalTheme.isShowDivider()));
            } else if (index == R.styleable.TitleLayout_tl_dividerColor) {
                normalTheme.setDividerColor(a.getColor(R.styleable.TitleLayout_tl_dividerColor, normalTheme.getDividerColor()));
            } else if (index == R.styleable.TitleLayout_tl_themeScrollBy) {
                setThemeScrollBy(a.getResourceId(R.styleable.TitleLayout_tl_themeScrollBy, UNSET));
            }
        }
        a.recycle();

        //normalTheme.setBackgroundColor(a.getColor(TitleLayout_tl_backgroundColor, UNSET));
        Drawable mBackground = getBackground();
        if (mBackground instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) mBackground.mutate();
            normalTheme.setBackgroundColor(colorDrawable.getColor());
        }
    }

    private void setTheme() {
        mTitle.setText(title);
        mTitle.setTextColor(normalTheme.getTextColor());
        mBack.setImageResource(normalTheme.getBackIcon());
        mMenu.setImageResource(normalTheme.getMenuIcon());
        mDivider.setBackgroundColor(normalTheme.getDividerColor());
        setBackgroundColor(normalTheme.getBackgroundColor());

        mBack.setVisibility(normalTheme.isShowBack() ? View.VISIBLE : View.GONE);
        mMenu.setVisibility(normalTheme.isShowMenu() ? View.VISIBLE : View.GONE);
        mDivider.setVisibility(normalTheme.isShowDivider() ? View.VISIBLE : View.GONE);
    }

    public void setThemeScrollBy(@IdRes int scrollViewId) {
        if (scrollViewId == UNSET) {
            return;
        }
        View dDecorView = null;
        if (getContext() instanceof Activity) {
            Window window = ((Activity) getContext()).getWindow();
            if (window != null) {
                dDecorView = window.getDecorView();
            }
        }
        final View rootView = dDecorView;
        if (rootView != null) {
            // UI初始化完后再去查找就能找到
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                setBackgroundFadeBy(findScrollView(rootView, scrollViewId));
            });
        } else {
            // 延时检查宿主
            postDelayed(() -> {
                if (mActivity != null) {
                    // 是否有绑定Activity
                    View decorView = mActivity.getWindow().getDecorView();
                    setBackgroundFadeBy(findScrollView(decorView, scrollViewId));
                } else {
                    // 从父布局去找
                    setBackgroundFadeBy(findScrollView((ViewGroup) getParent(), scrollViewId));
                }
            }, 500);
        }
    }

    private ScrollView findScrollView(View parent, int scrollViewId) {
        if (parent != null) {
            View view = parent.findViewById(scrollViewId);
            if (view instanceof ScrollView) {
                return (ScrollView) view;
            }
        }
        return null;
    }

    public TitleLayout bind(Activity activity) {
        mActivity = activity;
        return this;
    }

    public TitleTheme getNormalTheme() {
        return normalTheme;
    }

    public TitleTheme getScrollTheme() {
        if (scrollTheme == null) {
            if (normalTheme != null) {
                scrollTheme = normalTheme.clone();
            } else {
                scrollTheme = TitleTheme.WHITE.clone();
            }
        }
        return scrollTheme;
    }

    public TitleLayout setThemeNormal(TitleTheme theme) {
        if (theme != null) {
            this.normalTheme = theme.clone();
        }
        return this;
    }

    public TitleLayout setThemeScroll(TitleTheme theme) {
        if (theme != null) {
            this.scrollTheme = theme.clone();
        }
        return this;
    }

    public CharSequence getTitle() {
        return title;
    }

    public TitleLayout setTitle(CharSequence title) {
        this.title = title;
        mTitle.setText(title);
        return this;
    }

    public TitleLayout setTitle(@StringRes int textId) {
        if (textId != UNSET) {
            this.title = getContext().getString(textId);
            mTitle.setText(this.title);
        }
        return this;
    }

    public TitleLayout setTitleColor(int color) {
        if (color != UNSET) {
            normalTheme.setTextColor(color);
            mTitle.setTextColor(color);
        }
        return this;
    }

    public TitleLayout setTitleSize(float textSize) {
        if (textSize != UNSET) {
            mTitle.setTextSize(textSize);
        }
        return this;
    }

    public TitleLayout setTitleMaxEms(int maxEms) {
        if (maxEms != UNSET) {
            mTitle.setMaxEms(maxEms);
            invalidate();
        }
        return this;
    }

    public TitleLayout setTitleIconStart(int resId) {
        ViewUtils.setDrawableStart(mTitle, resId);
        return this;
    }

    public TitleLayout setTitleIconTop(int resId) {
        ViewUtils.setDrawableTop(mTitle, resId);
        return this;
    }

    public TitleLayout setTitleIconEnd(int resId) {
        ViewUtils.setDrawableEnd(mTitle, resId);
        return this;
    }

    public TitleLayout setTitleIconBottom(int resId) {
        ViewUtils.setDrawableBottom(mTitle, resId);
        return this;
    }

    public TitleLayout setTitleIconPadding(int pad) {
        mTitle.setCompoundDrawablePadding(pad);
        return this;
    }

    public TitleLayout setBackIcon(@DrawableRes int resId) {
        return setBackIcon(resId, null);
    }

    public TitleLayout setBackIcon(@DrawableRes int resId, OnClickListener listener) {
        if (resId != UNSET) {
            normalTheme.setBackIcon(resId);
            mBack.setVisibility(View.VISIBLE);
            mBack.setImageResource(resId);
            mBack.setOnClickListener(listener);
        }
        return this;
    }

    public TitleLayout setBankListener(OnClickListener listener) {
        if (mBack != null) {
            mBack.setVisibility(View.VISIBLE);
            mBack.setOnClickListener(listener);
        }
        return this;
    }

    public TitleLayout setShowMenu(boolean showMenu) {
        mMenu.setVisibility(showMenu ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleLayout setMenuIcon(@DrawableRes int resId) {
        return setMenuIcon(resId, null);
    }

    public TitleLayout setMenuIcon(@DrawableRes int resId, OnClickListener listener) {
        if (resId != UNSET) {
            normalTheme.setMenuIcon(resId);
            mMenu.setVisibility(View.VISIBLE);
            mMenu.setImageResource(resId);
            mMenu.setOnClickListener(listener);
        }
        return this;
    }

    public TitleLayout setMenuListener(OnClickListener listener) {
        if (mMenu != null) {
            mMenu.setVisibility(View.VISIBLE);
            mMenu.setOnClickListener(listener);
        }
        return this;
    }

    public TitleLayout setDividerVisible(boolean visible) {
        normalTheme.setShowDivider(visible);
        mDivider.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleLayout setDividerColor(@ColorInt int color) {
        if (color != UNSET) {
            normalTheme.setDividerColor(color);
            mDivider.setBackgroundColor(color);
            setDividerVisible(true);
        }
        return this;
    }

    ////////////////////////////////// 标题栏设置 END //////////////////////////////////


    ////////////////////////////////// 标题栏设置 //////////////////////////////////

    public TitleLayout setBackgroundFadeBy(View scrollView) {
        if (scrollView instanceof NestedScrollView) {
            ((NestedScrollView) scrollView).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    setTitleBarAlphaBy(scrollY);
                }
            });
        } else if (scrollView instanceof ListenerScrollView) {
            ((ListenerScrollView) scrollView).addOnScrollListener((scrollX, scrollY, oldScrollX, oldScrollY) -> {
                setTitleBarAlphaBy(scrollY);
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                setTitleBarAlphaBy(scrollY);
            });
        } else {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                setTitleBarAlphaBy(scrollView.getScrollY());
            });
        }
            return this;
    }

    public TitleLayout setTitleBarAlphaBy(float scrollY) {
        if (mActivity == null) {
            Log.e("TitleLayout", "未绑定到Activity");
            return this;
        }

        float maxAlpha = 255; // 100还是255
        if (scrollY > maxScrollY - 10) scrollY = maxScrollY;
        if (scrollY < 10) scrollY = 0;
        int currAlpha = (int) (maxAlpha / maxScrollY * scrollY);
        if (titleBarAlpha != currAlpha && currAlpha >= 0 && currAlpha <= maxAlpha) {
            titleBarAlpha = currAlpha;
//            int fromColor = titleBarConfig.getBackgroundColor();
            int toColor = scrollTheme.getBackgroundColor();
//            int transitionColor = evaluate(currAlpha, fromColor, toColor);
            int transitionColor = ColorUtils.setAlphaComponent(toColor, currAlpha);
            this.setBackgroundColor(transitionColor);
        }
        float half = maxScrollY / 2F;
        if (inRange(scrollY, 0, half)) {
            // 滚动 -> 静止
            StatusBarUtils.setStatusBarFontColor(mActivity, normalTheme.isStatusBarDark());
            if (mTitle != null) {
                mTitle.setTextColor(normalTheme.getTextColor());
                mBack.setImageResource(normalTheme.getBackIcon());
                mMenu.setImageResource(normalTheme.getMenuIcon());
            }
        } else if (inRange(scrollY, half, maxScrollY)) {
            // 静止 -> 滚动
            StatusBarUtils.setStatusBarFontColor(mActivity, scrollTheme.isStatusBarDark());
            if (mTitle != null) {
                mTitle.setTextColor(scrollTheme.getTextColor());
                mBack.setImageResource(scrollTheme.getBackIcon());
                mMenu.setImageResource(scrollTheme.getMenuIcon());
            }
        }
        return this;
    }


    //原文链接：https://blog.csdn.net/chenlove1/article/details/41548703
    private int evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return ((startA + (int) (fraction * (endA - startA))) << 24)
                | ((startR + (int) (fraction * (endR - startR))) << 16)
                | ((startG + (int) (fraction * (endG - startG))) << 8)
                | (startB + (int) (fraction * (endB - startB)));
    }

    private boolean inRange(float value, float min, float max) {
        return value >= min && value < max;
    }

}


