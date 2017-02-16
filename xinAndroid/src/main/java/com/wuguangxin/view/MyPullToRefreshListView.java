package com.wuguangxin.view;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Context;
import android.util.AttributeSet;

public class MyPullToRefreshListView extends PullToRefreshListView{
	public MyPullToRefreshListView(Context context){
		super(context); 
	}

	public MyPullToRefreshListView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
