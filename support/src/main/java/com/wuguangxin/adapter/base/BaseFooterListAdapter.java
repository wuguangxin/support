package com.wuguangxin.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.math.BigDecimal;
import java.util.List;

/**
 * 带页脚的适配器
 *
 * <p>Created by wuguangxin on 14/11/17 </p>
 */
public abstract class BaseFooterListAdapter<T> extends BaseListAdapter<T> {
	public BigDecimal zero = BigDecimal.ZERO;
	private int minCountEnableFooterView; // 最少多少条数据才显示页脚信息View

	public BaseFooterListAdapter(Context context){
		super(context);
	}

	public BaseFooterListAdapter(Context context, List<T> list){
		super(context, list);
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	/**
	 * 控制下拉刷新组件的刷新模式，当适配器已经显示了全部的数据后，不可在上拉加载。
	 * 如果数据没有加载完全，则隐藏脚布局（附加如果第一页就显示完数据了，但数据小于5条的，也隐藏脚布局）。
	 * @param listView 下拉刷新组件
	 * @param footerView FootView
	 * @param totalNumber 总数量
	 * @param startIndex 开始索引（一般为0或1）
	 * @param curPage 当前第几页
	 */
	public void notifyFooterView(PullToRefreshListView listView, View footerView, int totalNumber, int startIndex, int curPage){
		// 控制下拉刷新组件的刷新模式，当适配器已经显示了全部的数据后，不可在上拉加载
		listView.setMode(getList().size() < totalNumber ? Mode.BOTH : Mode.PULL_FROM_START);
		// 如果数据没有加载完全，则隐藏脚布局（附加如果第一页就显示完数据了，但数据小于5条的，也隐藏脚布局）
		if(curPage == startIndex && getCount() == totalNumber && getCount() < minCountEnableFooterView){
			footerView.setVisibility(View.GONE);
		} else {
			footerView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 *  最少多少条数据才显示页脚信息View
	 * @return 最少多少条数据才显示页脚信息View
	 */
	public int getMinCountEnableFooterView(){
		return minCountEnableFooterView;
	}

	/**
	 * 最少多少条数据才显示页脚信息View
	 * @param minCountEnableFooterView 最少多少条数据才显示页脚信息View
	 */
	public void setMinCountEnableFooterView(int minCountEnableFooterView){
		this.minCountEnableFooterView = minCountEnableFooterView;
	}
}