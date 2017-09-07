package com.wuguangxin.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 基本List适配器(泛型为 List 的泛型)
 *
 * <p>Created by wuguangxin on 14/11/17 </p>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter{
	public Context context;
	/** 数据列表 */
	protected List<T> list;
	/** 列表类型（可用于标识列表的类型） */
	protected int listType;

	public BaseListAdapter(Context context){
		this.context = context;
	}

	public BaseListAdapter(Context context, List<T> list){
		this.context = context;
		this.list = list;
	}

	/**
	 * 获取列表类型。
	 * @return 类型
	 */
	public int getListType() {
		return listType;
	}

	/**
	 * 设置列表类型。
	 * @param listType 类型
	 */
	public void setListType(int listType) {
		this.listType = listType;
	}

	/**
	 * 设置列表数据。(不更新)
	 * @param list 列表
	 */
	public void setList(List<T> list){
		this.list = list;
	}

	/**
	 * 获取当前列表数据
	 * @return 列表数据
	 */
	public List<T> getList(){
		return this.list;
	}

	/**
	 * 获取列表总数
	 * @return 总数
	 */
	@Override
	public int getCount(){
		return list == null ? 0 : list.size();
	}

	@Override
	public T getItem(int position){
		return list == null ? null : list.get(position);
	}
	
	@Override
	public long getItemId(int position){
		return position;
	}
	
	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	/**
	 * 更新BaseAdapter适配器
	 * @param list 数据集
	 */
	public void notify(List<T> list){
		this.list = list;
		super.notifyDataSetChanged();
	}
}