package com.wuguangxin.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wuguangxin.support.R;
import com.wuguangxin.utils.AnimUtil;

import java.util.ArrayList;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 布局管理器
 * Created by wuguangxin on 14/10/16.
 */
public class LayoutManager {
    private static final String TAG = "LayoutManager";
    private Activity mActivity;
    private Context mContext;
    private ConstraintLayout mRootLayout; // 界面根布局
    private LinearLayout mBodyLayout; // 显示内容布局
    private LinearLayout mErrorLayout; // 网络异常布局
    private TitleBar mTitleBar;

    public LayoutManager(@NonNull Activity activity, @LayoutRes int baseLayoutRes) {
        this(activity, LayoutInflater.from(activity).inflate(baseLayoutRes, null));
    }

    public LayoutManager(@NonNull Activity activity, @NonNull View baseView) {
        this.mActivity = activity;
        this.mContext = activity.getApplicationContext();

        mRootLayout = baseView.findViewById(R.id.root_layout);
        mBodyLayout = baseView.findViewById(R.id.body_layout);
        mErrorLayout = baseView.findViewById(R.id.error_layout);

        mBodyLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);
    }

    /**
     * 设置Body布局
     * @param layoutRes
     */
    public LayoutManager setContentView(@LayoutRes int layoutRes) {
        if (layoutRes != 0) {
            // 这种方式会导致有下拉刷新界面布局高度测量有问题。
            // View view = View.inflate(mActivity, layoutRes, null);
            // return setContentView(view);

            // 完美解决
            View.inflate(mActivity, layoutRes, mBodyLayout);
        }
        return this;
    }

    /**
     * 设置Body布局
     * @param view
     */
    public LayoutManager setContentView(View view) {
        if (mBodyLayout != null) {
            if (mBodyLayout.getChildCount() > 0) {
                mBodyLayout.removeAllViews();
            }
            mBodyLayout.addView(view);
        }
        return this;
    }

    /**
     * 设置界面背景
     *
     * @param resId drawable资源ID
     */
    public LayoutManager setBackgroundResource(int resId) {
        mRootLayout.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置界面背景
     *
     * @param drawable
     */
    public LayoutManager setBackgroundResource(Drawable drawable) {
        setBackground(drawable);
        return this;
    }

    /**
     * 设置界面背景
     *
     * @param drawable
     */
    public LayoutManager setBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRootLayout.setBackground(drawable);
        } else {
            mRootLayout.setBackgroundDrawable(drawable);
        }
        return this;
    }

    /**
     * 设置界面背景
     *
     * @param color 颜色
     */
    public LayoutManager setBackgroundColor(int color) {
        mRootLayout.setBackgroundColor(color);
        return this;
    }

    /**
     * 控制Error布局显隐状态
     *
     * @param visibility 显隐状态
     */
    public LayoutManager setErrorLayoutVisible(boolean visibility) {
        // 20180311 wgx 注释，界面有多个fragment时，体验很不好。但没网络时用这个也不错
//		if(visibility){
//			mBodyLayout.setVisibility(View.GONE);
//			mErrorLayout.setVisibility(View.VISIBLE);
//		} else {
//			mBodyLayout.setVisibility(View.VISIBLE);
//			mErrorLayout.setVisibility(View.GONE);
//		}
        return this;
    }

    /**
     * 设置错误布局界面点击事件
     *
     * @param onClickListener
     */
    public LayoutManager setErrorLayoutListener(OnClickListener onClickListener) {
        mErrorLayout.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置内容主体显示状态
     *
     * @param visibility 是否可见
     */
    public LayoutManager setBodyVisibility(boolean visibility) {
        mBodyLayout.setVisibility(visibility ? View.VISIBLE : View.GONE);
        return this;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 获取标题栏管理器
     *
     * @return
     */
    public TitleBar getTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = new TitleBar(mActivity);
        }
        return mTitleBar;
    }

    /**
     * 设置标题栏显示状态
     *
     * @param visibility
     */
    public LayoutManager setTitleBarVisibility(boolean visibility) {
        if (mTitleBar != null) {
            mTitleBar.setVisibility(visibility);
        }
        return this;
    }

    /* ************************************ getter/setter updateSubscribe ******************************** */

    /**
     * 获取跟布局
     *
     * @return
     */
    public ConstraintLayout getRootLayout() {
        return mRootLayout;
    }

    /**
     * 获取context布局
     *
     * @return
     */
    public LinearLayout getBodyLayout() {
        return mBodyLayout;
    }

    /**
     * 获取错误界面布局
     *
     * @return
     */
    public LinearLayout getErrorLayout() {
        return mErrorLayout;
    }

    /* ************************************ getter/setter end ********************************** */

    private Menu mMenu;

    public Menu getMenu() {
        if (mMenu == null) {
            mMenu = new Menu(mActivity);
        }
        return this.mMenu;
    }

    /**
     * 标题栏下方的抽屉式伸缩菜单
     * Created by wuguangxin on 14/10/16.
     */
    public static class Menu {
        private OnMenuItemClickListener onMenuItemClickListener;
        private RelativeLayout mMenuLayout;
        private ListView mMenuListView;
        private View mMenuShadeView; // 菜单遮罩
        private BaseAdapter adapter;
        private ArrayList<String> list;
        private Activity activity;
        private boolean isShowing;
        private Animation animationShow; // 菜单显示时的动画
        private Animation animationHide; // 菜单隐藏时的动画
        private Animation animationShadeShow; // 遮罩显示时的动画
        private Animation animationShadeHide; // 遮罩隐藏时的动画

        public Menu(Activity activity) {
            this(activity, new ArrayList<>());
        }

        public Menu(Activity activity, ArrayList<String> list) {
            this.activity = activity;
            this.list = list;
            init();
        }

        private void init() {
            this.mMenuLayout = activity.findViewById(R.id.menu_layout);  // 菜单列表
            this.mMenuListView = activity.findViewById(R.id.xin_menu_listview);
            this.mMenuShadeView = activity.findViewById(R.id.xin_menu_shadeview);
            setViewAlign(mMenuLayout, RelativeLayout.BELOW, R.id.xin_titlebar_layout);
            animationShow = AnimUtil.getTopIn();
            animationHide = AnimUtil.getTopOut();
            animationShadeShow = AnimUtil.getLeftIn();
            animationShadeHide = AnimUtil.getLeftOut();
            adapter = new ArrayAdapter<>(activity, R.layout.xin_item_menu_list, list);
            mMenuListView.setAdapter(adapter);
            mMenuShadeView.setOnClickListener(v -> hide());
        }

        /**
         * 设置View的对齐方式。(view与anchor对齐方式为verb)
         *
         * @param view 哪个View需要设置对齐
         * @param verb 对齐方式（如：RelativeLayout.BELOW，view在id为verb的View的下方）
         * @param anchor 对齐哪个View的id
         */
        public void setViewAlign(View view, int verb, int anchor) {
            if (view != null && verb != -1) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (params != null) {
                    params.addRule(verb, anchor); // anchor为要对齐的View的id
                    view.setLayoutParams(params);
                }
            }
        }

        /**
         * 判断菜单显示状态
         *
         * @return
         */
        public boolean isShowing() {
            return isShowing;
        }

        public void setAdapter(BaseAdapter adapter) {
            this.adapter = adapter;
            if (this.mMenuListView != null) {
                this.mMenuListView.setAdapter(adapter);
            }
        }

        public BaseAdapter getAdapter() {
            return this.adapter;
        }

        public void notifyDataSetChanged() {
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
        }

        /**
         * 设置菜单显示动画
         *
         * @param animationShow
         */
        public void setAnimationShow(Animation animationShow) {
            this.animationShow = animationShow;
        }

        /**
         * 设置菜单隐藏动画
         *
         * @param animationHide
         */
        public void setAnimationHide(Animation animationHide) {
            this.animationHide = animationHide;
        }

        /**
         * 设置菜单遮罩显示动画
         *
         * @param animationShadeShow
         */
        public void setAnimationShadeShow(Animation animationShadeShow) {
            this.animationShadeShow = animationShadeShow;
        }

        /**
         * 设置菜单遮罩隐藏动画
         *
         * @param animationShadeHide
         */
        public void setAnimationShadeHide(Animation animationShadeHide) {
            this.animationShadeHide = animationShadeHide;
            // 设置遮罩隐藏动画监听器，当菜单遮罩隐藏动画结束时，再隐藏整个菜单Layout
            if (this.animationShadeHide != null) {
                this.animationShadeHide.setAnimationListener(animationShadeHideListener);
            }
        }

        /**
         * 显示菜单
         */
        public void show() {
            if (this.mMenuLayout != null && this.mMenuLayout.getVisibility() == View.GONE) {
                isShowing = true;
                this.mMenuLayout.setVisibility(View.VISIBLE);
                this.mMenuListView.setVisibility(View.VISIBLE);
                this.mMenuListView.startAnimation(animationShow);
                this.mMenuShadeView.setVisibility(View.VISIBLE);
                this.mMenuShadeView.startAnimation(animationShadeShow);
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onMenuVisible(isShowing);
                }
            }
        }

        /**
         * 隐藏菜单
         */
        public void hide() {
            if (this.mMenuLayout != null && this.mMenuLayout.getVisibility() == View.VISIBLE) {
                isShowing = false;
                this.mMenuListView.setVisibility(View.GONE);
                this.mMenuListView.startAnimation(animationHide);
                this.mMenuShadeView.setVisibility(View.GONE);
                this.mMenuShadeView.startAnimation(animationShadeHide);
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onMenuVisible(isShowing);
                }
            }
        }

        /**
         * 遮罩动画隐藏结束监听器
         */
        private Animation.AnimationListener animationShadeHideListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mMenuLayout != null) {
                    mMenuLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        /**
         * 设置菜单项点击监听器
         *
         * @param onMenuItemClickListener
         */
        public void setOnMenuItemClickListener(final OnMenuItemClickListener onMenuItemClickListener) {
            this.onMenuItemClickListener = onMenuItemClickListener;
            if (this.mMenuListView != null && onMenuItemClickListener != null) {
                this.mMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onMenuItemClickListener.onItemClicked(parent, view, position, id);
                    }
                });
            }
        }

        /**
         * 菜单项点击回事件调接口
         */
        public interface OnMenuItemClickListener {
            /**
             * 当菜单项被点击时触发
             *
             * @param parent
             * @param view
             * @param position 被点击的Item的位置
             * @param id
             */
            void onItemClicked(AdapterView<?> parent, View view, int position, long id);

            /**
             * 当菜单显示或隐藏时触发
             *
             * @param visible 是否显示
             */
            void onMenuVisible(boolean visible);
        }
    }


}
