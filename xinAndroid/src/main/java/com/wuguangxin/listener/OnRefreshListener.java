package com.wuguangxin.listener;

/**
 * @author andong ListView刷新的监听事件
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
