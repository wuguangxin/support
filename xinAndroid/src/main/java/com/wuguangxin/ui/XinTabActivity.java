package com.wuguangxin.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.wuguangxin.R;
import com.wuguangxin.ui.test.tabhost.Demo1Activity;
import com.wuguangxin.ui.test.tabhost.Demo2Activity;
import com.wuguangxin.ui.test.tabhost.Demo3Activity;
import com.wuguangxin.ui.test.tabhost.Demo4Activity;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 一个封装部分功能的便捷TabHost，继承XinTabActivity后，只需简单设置Tab数据即可
 * 
 * @author wuguangxin
 * @date: 2016-1-22 下午5:15:43
 */
@SuppressWarnings("deprecation")
public abstract class XinTabActivity extends TabActivity{
	public static String ACTION_CURRENT_TAB = "action_current_tab"; // 当前标签ID
	/** 接收切换TAB广播的意图过滤器name */
	public static String ACTION_INTENT_FILTER = "INTENT_FILTER_SWITCH";
	private SwitchTabHostReceiver mSwitchTabHostReceiver;
	private int currentTabId; // 当前TAB位置
	private ArrayList<Tab> tabList;
	private ArrayList<String> tabIdList = new ArrayList<String>();
	private TabHost mTabHost;
	private static Context mContext;
	private static ClickHistoryList mClickHistoryList = new ClickHistoryList();
	private static final Handler handler = new Handler();

	private static class ClickHistoryList extends LinkedList<Integer> {
		private final int maxSize = 2; // 最大存储数量
		@Override
		public void addFirst(Integer position) {
			if(!isEmpty() && size() >= maxSize){
				removeLast();
			}
			super.addFirst(position);
		}

		@Override
		public Integer getLast() {
			return super.getLast();
		}
	}

	@Override
	final public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext = this;
		onCreateView();
		initTabHostView();
		createReceiver();
	}
	
	/**
	 * 设置View。
	 *
	 * 可以使用 R.layout.xin_tabhost_layout
	 */
	public abstract void onCreateView();

	/**
	 * 获取当前TabHost实例
	 */
	public TabHost getTabHost(){
		return mTabHost;
	}

	// 创建广播接受者
	private void createReceiver(){
		mSwitchTabHostReceiver = new SwitchTabHostReceiver();
		registerReceiver(mSwitchTabHostReceiver, new IntentFilter(ACTION_INTENT_FILTER));
	}

	// 销毁广播接受者
	private void destroyReceiver(){
		if (mSwitchTabHostReceiver != null) {
			unregisterReceiver(mSwitchTabHostReceiver);
			mSwitchTabHostReceiver = null;
		}
	}

	private void initTabHostView(){
		mTabHost = super.getTabHost();
		tabList = getTabList();
		tabIdList = new ArrayList<String>();
		currentTabId = getIntent().getIntExtra(ACTION_CURRENT_TAB, 0);
		for (int i = 0; i < tabList.size(); i++) {
			String tabId = "tab_" + i;
			tabIdList.add(tabId);
			Intent intent = new Intent(this, tabList.get(i).activity).putExtra("TAB_NAME", 0);
			View view = getItemView(tabList.get(i).name, tabList.get(i).icon);
			mTabHost.addTab(mTabHost.newTabSpec(tabId).setIndicator(view).setContent(intent));
		}
		mTabHost.setCurrentTab(currentTabId);
		mClickHistoryList.addFirst(currentTabId);
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if(tabIdList != null){
					mClickHistoryList.addFirst(tabIdList.indexOf(tabId));
				}
			}
		});
	}

	/**
	 * 设置当前TabHost的位置
	 * @param position
	 */
	public void setCurrentTab(int position){
		if(mTabHost != null){
			mTabHost.setCurrentTab(position);
		}
	}

	/**
	 * 获取Tab ItemView
	 * @param name tab名称
	 * @param icon tab图标资源id
	 * @return
	 */
	@SuppressLint("InflateParams")
	public View getItemView(String name, int icon){
		View view = LayoutInflater.from(this).inflate(R.layout.xin_tabhost_item_layout, null);
		ImageView mIcon = (ImageView) view.findViewById(R.id.xin_tabhost_tab_icon);
		TextView mName = (TextView) view.findViewById(R.id.xin_tabhost_tab_name);
		mIcon.setImageResource(icon);
		mName.setText(name);
		return view;
	}

	/**
	 * 切换TAB的广播 <br/>
	 *
	 * 实现方法<br/>
	 * ProductActivity.setLastMenuIndex(currentItem);<br/>
	 * 发送切换TAB的广播到MainActivity，1:TabHost的currentTab，即产品.<br/>
	 * sendBroadcast(new Intent(MainActivity.ACTION_INTENT_FILTER).putExtra(MainActivity.ACTION_CURRENT_TAB, 1));<br/>
	 *
	 * @author wuguangxin
	 * @date: 2016-1-22 下午3:44:11
	 */
	private class SwitchTabHostReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			if (intent != null && ACTION_INTENT_FILTER.equals(intent.getAction())) {
				int currentTab = intent.getIntExtra(ACTION_CURRENT_TAB, 0);
				if(onTabSwitchListener != null){
					onTabSwitchListener.onTabSwitch(currentTab);
				}
				setCurrentTab(currentTab);
			}
		}
	}

	public OnTabSwitchListener onTabSwitchListener;

	/**
	 * 当Tab切换前的监听回调
	 * @param onTabSwitchListener
     */
	public void setOnTabSwitchListener(OnTabSwitchListener onTabSwitchListener) {
		this.onTabSwitchListener = onTabSwitchListener;
	}

	/**
	 * Tab切换前的回调接口
	 */
	public interface OnTabSwitchListener {
		/**
		 * 当Tab切换前回调
		 * @param position 即将切换到的Tab位置
         */
		void onTabSwitch(int position);
	}

	/**
	 * TabHost管理器，提供改变TabHost选项卡方法和返回上一个选项卡方法
	 */
	public static class TabManager {
		private static Intent intent;

		/**
		 * 重置TabHost位置到0
		 * @param context
		 */
		public static void reset(Context context){
			setPosition(context, 0);
		}

		/**
		 * 切换TabHost位置 0-1-2-3
		 * @param context
		 * @param position tab位置
		 */
		public static void setPosition(Context context, int position){
			intent = new Intent(ACTION_INTENT_FILTER);
			intent.putExtra(ACTION_CURRENT_TAB, position);
			context.sendBroadcast(intent);
		}

		/**
		 * 返回到上一个位置
		 */
		public static void back(final Context context){
			int last = 0;
			if(mClickHistoryList != null){
				last = mClickHistoryList.getLast();
			}
			setPosition(context, last);
		}

		public static void finish(){
			((Activity)mContext).finish();
		}
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		destroyReceiver();
	}

	/**
	 * TabHost需要的信息
	 * 
	 * @author wuguangxin
	 * @date: 2016-1-22 下午5:23:07
	 */
	public class Tab{
		/** Tab的名字 */
		public String name;
		/** Tab的图标资源ID */
		public int icon;
		/** 点击该Tab显示的Activity */
		public Class<? extends Activity> activity;

		/**
		 * 构造Tab信息
		 * @param name Tab的名字
		 * @param icon Tab的图标资源ID
		 * @param activity 点击该Tab显示的Activity
		 */
		public Tab(String name, int icon, Class<? extends Activity> activity){
			super();
			this.name = name;
			this.icon = icon;
			this.activity = activity;
		}

		@Override
		public String toString(){
			return "Tab [name=" + name + ", icon=" + icon + ", activity=" + activity + "]";
		}
	}

	/**
	 * 获取TAB信息，请子类自行复写该方法
	 * @return
	 */
	public ArrayList<Tab> getTabList(){
		ArrayList<Tab> tabList = new ArrayList<XinTabActivity.Tab>();
		tabList.add(new Tab("Item1", android.R.drawable.sym_def_app_icon, Demo1Activity.class));
		tabList.add(new Tab("Item2", android.R.drawable.sym_def_app_icon, Demo2Activity.class));
		tabList.add(new Tab("Item3", android.R.drawable.sym_def_app_icon, Demo3Activity.class));
		tabList.add(new Tab("Item4", android.R.drawable.sym_def_app_icon, Demo4Activity.class));
		return tabList;
	}
}