package com.wuguangxin.listener;

/**
 * ListView刷新的监听事件
 *
 * <p>Created by wuguangxin on 14/6/14 </p>
 */
public interface OnRefreshListener {

	/**
	 * 当下拉刷新时回调
	 */
	public void onDownRefresh();

	/**
	 * 当加载更多时回调
	 */
	public void onLoadMoring();
}
