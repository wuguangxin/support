package com.wuguangxin.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账单详情适配器
 *
 * <p>Created by wuguangxin on 14/11/17 </p>
 */
public abstract class BaseFooterListAdapter extends BaseListAdapter {
	public BigDecimal zero = new BigDecimal(0); 
	private int minCountEnableFooterView; // 最少多少条数据才显示页脚信息View
	
	public BaseFooterListAdapter(Context context, List<?> list){
		super(context, list);
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	/**
	 * 控制下拉刷新组件的刷新模式，当适配器已经显示了全部的数据后，不可在上拉加载。
	 * 如果数据没有加载完全，则隐藏脚布局（附加如果第一页就显示完数据了，但数据小于5条的，也隐藏脚布局）。
	 * @param mPullRefreshListView 下拉刷新组件
	 * @param mFooterView 页脚信息View
	 * @param totalNumber 总数量
	 * @param startIndex 开始索引（一般为0或1）
	 * @param pageNumber 当前第几页
	 */
	public void notifyFooterView(PullToRefreshListView mPullRefreshListView, View mFooterView, int totalNumber, int startIndex, int pageNumber){
		// 控制下拉刷新组件的刷新模式，当适配器已经显示了全部的数据后，不可在上拉加载
		mPullRefreshListView.setMode(getList().size() < totalNumber ? Mode.BOTH : Mode.PULL_FROM_START);
		// 如果数据没有加载完全，则隐藏脚布局（附加如果第一页就显示完数据了，但数据小于5条的，也隐藏脚布局）
		if(pageNumber == startIndex && getList().size() == totalNumber && getList().size() < minCountEnableFooterView){
			mFooterView.setVisibility(View.GONE);
		} else {
			mFooterView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 *  最少多少条数据才显示页脚信息View
	 * @return
	 */
	public int getMinCountEnableFooterView(){
		return minCountEnableFooterView;
	}

	/**
	 *  最少多少条数据才显示页脚信息View
	 */
	public void setMinCountEnableFooterView(int minCountEnableFooterView){
		this.minCountEnableFooterView = minCountEnableFooterView;
	}
}