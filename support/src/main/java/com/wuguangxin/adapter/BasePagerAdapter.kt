package com.wuguangxin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

/**
 * PagerView基础适配器
 *
 * <p>Created by wuguangxin on 14/11/17 </p>
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {
	protected Context context;
	protected List<T> list;

	public BasePagerAdapter(Context context, List<T> list){
		super();
		this.context = context;
		this.list = list;
    }
	
	public void setList(List<T> list){
		this.list = list;
	}

	public List<T> getList(){
		return list;
	}

	/**
	 * 获取要滑动的图片数量
	 */
	@Override
	public int getCount(){
		return list.size();
	}
	
	public T getItem(int position){
		return list.get(position);
	}
	
	/**
	 * 当滑动中的view对象和进来的对象是同一个时, 返回true 复用当前view对象
	 */
	@Override
	public boolean isViewFromObject(View view, Object obj){
		return view == obj;
	}

	/**
	 * PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
	 * 销毁一个Item position 对象的是将要销毁的下标
	 */
	@Override
	public abstract void destroyItem(ViewGroup viewGroup, int position, Object object);

	/**
	 * 预加载一个item
	 * 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，
	 * 我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
	 */
	@Override
	public abstract Object instantiateItem(ViewGroup viewGroup, final int position);
	
	/**
	 * 更新PagerAdapter适配器
	 * @param list 数据集
	 */
	public void notify(List<T> list){
		this.list = list;
		notifyDataSetChanged();
	}
}