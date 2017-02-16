package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.wuguangxin.R;

/**
 * View工具类
 * @author wuguangxin
 * @date: 2014-10-23 下午4:21:44
 */
public class ViewUtils{
	/**
	 * 判断View是否是可见的
	 * @param view
	 * @return
	 */
	public static boolean isVisible(View view){
		if (view != null) {
			return view.getVisibility() == View.VISIBLE;
		}
		return false;
	}

	/**
	 * 获取View的宽度
	 * @param view
	 * @return
	 */
	public static int getViewWidth(View view){
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		return view.getMeasuredWidth();
	}

	/**
	 * 获取View的高度
	 * @param view
	 * @return
	 */
	public static int getViewHeight(View view){
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		return view.getMeasuredHeight();
	}

	/**
	 * 重置 ViewPager 的宽高度 (宽为屏幕宽度，高度为 屏幕宽/2)
	 * @param viewPager
	 */
	public static void setViewPagerHeight(ViewPager viewPager){
		android.view.ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		WindowManager wm = (WindowManager) viewPager.getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		layoutParams.height = dm.widthPixels >> 1;
		viewPager.setLayoutParams(layoutParams);
	}

	/**
	 * 重置 ViewPager 的宽高度
	 * @param context
	 * @param viewPager ViewPager
	 * @param height 高
	 */
	public static void setViewPagerHeight(Context context, ViewPager viewPager, int height){
		android.view.ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		layoutParams.height = height;
		viewPager.setLayoutParams(layoutParams);
	}

	/**
	 * 重置 GridView 的高度,使高度等于Child 总数的高度
	 * @param gridView GridView
	 */
	@SuppressLint("NewApi")
	public static void setGridViewHeight(GridView gridView){
		if (gridView == null) {
			return;
		}
		ListAdapter adapter = gridView.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = adapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
			View listItem = adapter.getView(i, null, gridView);
			listItem.measure(0, 0); //计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
		}
		LayoutParams params = gridView.getLayoutParams();
		int verticalSpacing = 0;  // 垂直的间隔高度
//		if (AndroidUtils.isSdkOn4_1()) {
//			verticalSpacing = gridView.getVerticalSpacing();
//		}
		params.height = totalHeight + (verticalSpacing * (adapter.getCount() - 1));
		//listView.getDividerHeight()获取子项间分隔符占用的高度
		//params.height最后得到整个ListView完整显示需要的高度
		gridView.setLayoutParams(params);
	}

	/**
	 * 重置 ViewPager 的高度,使高度等于Child 总数的高度(有bug)
	 * @param viewPager
	 */
	@SuppressLint("NewApi")
	public static void setViewPagerChildCountHeight(ViewPager viewPager, int position){
		if (viewPager != null) {
			View childView = viewPager.getChildAt(position);
			if(childView != null){
				int h;
				LayoutParams params = viewPager.getLayoutParams();
				h = childView.getMeasuredHeight();
				h += viewPager.getPaddingTop();
				h += viewPager.getPaddingBottom();
				params.height = h;
				viewPager.setLayoutParams(params);
			}
		}
	}

	/**
	 * 重置 ViewPager 的高度,使高度等于Child 总数的高度
	 * @param viewGroup
	 */
	@SuppressLint("NewApi")
	public static void setViewGroupChildTotalHeight(ViewGroup viewGroup){
		if (viewGroup != null) {
			int childCount = viewGroup.getChildCount();
			if(childCount > 0){
				int totalHeight = 0;
				for (int i = 0; i < childCount; i++) {
					View child = viewGroup.getChildAt(i);
					if(child != null){
						totalHeight += child.getMeasuredHeight();
						totalHeight += viewGroup.getPaddingTop();
						totalHeight += viewGroup.getPaddingBottom();
					}
				}
				LayoutParams params = viewGroup.getLayoutParams();
				params.height = totalHeight;
				viewGroup.setLayoutParams(params);
			}
		}
	}

	/**
	 * 重置 ViewPager 的高度,为最高child的高度
	 * @param parentView
	 */
	@SuppressLint("NewApi")
	public static void setViewPagerHeightByMaxChild(ViewGroup parentView){
		if (parentView != null) {
			int childCount = parentView.getChildCount();
			if(childCount > 0){
				int maxHeight = 0;
				for (int i = 0; i < childCount; i++) {
					View child = parentView.getChildAt(i);
					if(child != null){
						int childHeight = child.getMeasuredHeight();
						if(childHeight > maxHeight){
							maxHeight = childHeight;
						}
					}
				}
				maxHeight += parentView.getPaddingTop();
				maxHeight += parentView.getPaddingBottom();
				LayoutParams params = parentView.getLayoutParams();
				params.height = maxHeight;
				parentView.setLayoutParams(params);
			}
		}
	}

	/**
	 * 获取数据为空时显示的View
	 * @param context 上下文
	 * @return
	 */
	public static View getDataEmptyView(Context context){
		return getDataEmptyView(context, null);
	}

	/**
	 * 获取数据为空时显示的View
	 * @param context 上下文
	 * @param msg 提示信息
	 * @return
	 */
	public static View getDataEmptyView(Context context, String msg){
		return getDataEmptyView(context, msg, null, null);
	}

	/**
	 * 获取数据为空时显示的View
	 * @param msg 消息
	 * @param buttonName 按钮名称
	 * @param onClickListener 点击监听器
	 * @return
	 */
	public static View getDataEmptyView(String msg, String buttonName, OnClickListener onClickListener){
		return getDataEmptyView(null, msg, buttonName, onClickListener);
	}

	/**
	 * 获取数据为空时显示的View
	 * @param context 上下文
	 * @param msg 提示信息
	 * @param buttonName 按钮名称
	 * @param onClickListener 点击回调事件 注意: buttonName或者onClickListener其一为空, 将不显示按钮
	 * @return
	 */
	public static View getDataEmptyView(Context context, String msg, String buttonName, OnClickListener onClickListener){
		if (context == null) {
			return null;
		}
		View mEmptyLayout = View.inflate(context, R.layout.xin_empty_layout, null);
		TextView mButton = (TextView) mEmptyLayout.findViewById(R.id.xin_empty_layout_button);

		if(msg != null){
			TextView mMessage = (TextView) mEmptyLayout.findViewById(R.id.xin_empty_layout_message);
			mMessage.setText(msg);
		}

		if (buttonName != null) {
			mButton.setText(buttonName);
			mButton.setVisibility(View.VISIBLE);
			if (onClickListener == null) {
				mButton.setOnClickListener(onClickListener);
			}
		}
		// 左右摇摆动画
//		mEmptyLayout.startAnimation(AnimUtil.getRotate_20());
		return mEmptyLayout;
	}

	/**
	 * 获取FootView
	 * @param context
	 * @param text
	 * @return
	 */
	public static TextView getFootView(Context context, String text){
		if (context != null) {
			TextView textView = (TextView) View.inflate(context, R.layout.xin_listview_footview, null);
			textView.setText(text);
			return textView;
		}
		return null;
	}
	
	/**
	 * 设置 RefreshView 上次更新时间
	 * @param refreshView
	 */
	public static void setRefreshViewLastUpdatedLabel(PullToRefreshBase<?> refreshView){
		if (refreshView != null) {
			String label = DateUtils.formatDateTime(refreshView.getContext(), System.currentTimeMillis(), //
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(String.format("最后刷新: %s", label));
		}
	}
	
	/**
	 * 设置View的对齐方式。(view与anchor对齐方式为verb)
	 * @param view 哪个View需要设置对齐
	 * @param verb 对齐方式（如：RelativeLayout.BELOW，view在id为verb的View的下方）
	 * @param anchor 对齐哪个View的id
	 */
	public static void setViewAlign(View view, int verb, int anchor){
		if(view != null && verb != -1){
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
			if(params != null){
				params.addRule(verb, anchor); // anchor为要对齐的View的id
				view.setLayoutParams(params);
			}
		}
	}

	/**
	 * 设置EditText内容的显示或隐藏状态
	 * @param checkBox
	 * @param editText
     */
	public static void setEditTextVisibleStatus(CheckBox checkBox, final EditText editText){
		if(checkBox != null && editText != null){
			checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				private TransformationMethod visibleMethod = HideReturnsTransformationMethod.getInstance();
				private PasswordTransformationMethod goneMethod = PasswordTransformationMethod.getInstance();
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// 如果选中，显示密码, 否则隐藏密码
					editText.setTransformationMethod(isChecked ? visibleMethod : goneMethod);
					Utils.setFocusPosition(editText);
				}
			});
		}
	}

	/**
	 * 垂直图像的宽高，以宽在屏幕的尺寸为参考值等比例缩放高度
	 * @param view 显示图像的View
	 * @param width 原图像的宽
	 * @param height 原图像的高
     */
	public static void setRealSize(View view, int width, int height) {
		if(view != null){
			int realWidth = view.getLayoutParams().width;
			view.getLayoutParams().height = realWidth * height / width;
		}
	}
}
