package com.wuguangxin.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 基本List适配器
 *
 * @author wuguangxin
 * @date: 2014-11-17 下午2:05:38
 */
public abstract class BaseListAdapter extends BaseAdapter{
	public Context context;
	protected List<?> list;
	
	public BaseListAdapter(Context context, List<?> list){
		this.context = context;
		this.list = list;
	}
	
	public void setList(List<?> list){
		this.list = list;
	}

	public List<?> getList(){
		return this.list;
	}

	@Override
	public int getCount(){
		if(list == null){
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position){
		if(list == null){
			return null;
		}
		return list.get(position);
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
	public void notify(List<?> list){
		this.list = list;
		super.notifyDataSetChanged();
	}
}