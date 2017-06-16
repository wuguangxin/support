package com.wuguangxin.base;

import android.app.Activity;
import android.util.Log;

import com.wuguangxin.utils.Logger;

import java.util.LinkedList;

/**
 * 全局管理Activity任务栈
 */
public class AppTask {
	private static final String TAG = AppTask.class.getSimpleName();
	private static final String UP = "UP"; // 上浮
	private static final String IN = "IN"; // 入栈
	private static final String OUT = "OUT"; // 出栈
	private static final String CLEAN = "CLEAN"; // 清栈
	/** 记录点击手机返回键时间 */
	public static long exitTime;
	/** 队列集合 */
	private static LinkedList<Activity> taskList = new LinkedList<Activity>();

	/**
	 * 入栈
	 * @param activity 入栈的 Activity
	 */
	public static void inTask(Activity activity){
		if(activity != null){
			String className = activity.getClass().getSimpleName();
			if (taskList.size() > 0){
				if(taskList.contains(activity)){
					if(taskList.remove(activity)){
						taskList.addFirst(activity);
						printTaskInfo(className, UP);  //移到栈顶
					}else{
						Log.e(TAG, String.format("#s UP ERROR!  TASK SIZE:%s", TAG, taskList.size()));
					}
				} else {
					int indexOf = taskList.indexOf(activity);
					if(indexOf != -1){
						Activity oldActivity = taskList.get(indexOf);
						if(oldActivity != null){
							if( activity.getClass().getSimpleName().equals(oldActivity.getClass().getSimpleName())){
								if(taskList.remove(activity)){
									taskList.addFirst(activity);
									printTaskInfo(className, UP);  //移到栈顶
								}
							}
						} else {
							taskList.addFirst(activity);
							printTaskInfo(className, IN);
						}
					} else {
						taskList.addFirst(activity);
						printTaskInfo(className, IN);
					}
				}
			} else {
				taskList.addFirst(activity);
				printTaskInfo(className, IN);
			}
		}

	}

	/**
	 * 出栈
	 * @param activity 出栈的Activity
	 */
	public static void outTask(Activity activity){
		if (taskList != null && taskList.size() > 0){
			if(activity != null && taskList.remove(activity)){
				printTaskInfo(activity.getClass().getSimpleName(), OUT);
				activity.finish();
				activity = null;
			}
		}
	}

	/**
	 * 清栈
	 * @return 是否是空的
	 */
	public static boolean clearTask(){
		if (taskList != null && !taskList.isEmpty()) {
			Activity taskTop;
			String className;
			int len = taskList.size();
			for (int i = 0; i < len; i++) {
				taskTop = taskList.removeLast();
				if(taskTop != null){
					className = taskTop.getClass().getSimpleName();
					taskTop.finish();
					taskTop = null;
					printTaskInfo(className, CLEAN);
				}
			}
		}
		return taskList.isEmpty();
	}
	
	/**
	 * 获取栈顶Activity
	 * @return 栈顶Activity
	 */
	public static Activity getTopActivity(){
		if (taskList != null && !taskList.isEmpty()) {
			return taskList.getFirst();
		}
		return null;
	}

//	/**
//	 * 回退栈(如从A-B-C-D, 在D界面直接返回到A,现在需要把D,C,B给Finish掉,这时的回退次数num为3)
//	 * @param num 回退的位数
//	 */
//	public static void backTask(int num){
//		if (taskList != null && !taskList.isEmpty()) {
//			int len = taskList.size();
//			for (int i = 0; i < num && i < len - 1; i++) {
//				outTask(taskList.getLast());
//			}
//		}
//	}
//
//	/**
//	 * 回退栈(如从A-B-C-D, 在D界面直接返回到A,会将D,C,B给直接Finish掉)
//	 * @param backActivityClass 回退到该Activity.class
//	 */
//	public static void backTask(Class backActivityClass){
//		if (backActivityClass != null && taskList != null && !taskList.isEmpty()) {
//			Activity taskTop;
//			int len = taskList.size();
//			for (int i = 0; i < len; i++) {
//				taskTop = taskList.getLast();
//				if(taskTop != null){
//					if(backActivityClass.equals(taskTop.getClass())){
//						return;
//					}
//					printTaskInfo(taskTop.getClass().getSimpleName(), CLEAN);
//					taskTop.finish();
//					taskTop = null;
//				}
//			}
//		}
//	}
	
	private static void printTaskInfo(String className, String msg){
		String taskTopActivity = "null";
		int taskSize = 0;
		if (taskList != null) {
			taskSize = taskList.size();
			if (taskSize > 0) {
				taskTopActivity = taskList.getFirst().getClass().getSimpleName();
			}
		}
		Logger.d(TAG, String.format("%s:%s   TOP:%s   TASK SIZE:%s", msg, className, taskTopActivity, taskSize));
	}
	
	/**
	 * 获取栈大小
	 * @return 栈大小
	 */
	public static int getTaskSize(){
		return taskList != null ? taskList.size() : 0;
	}

	/**
	 *
	 * @return 列表
	 */
	public static LinkedList<Activity> getTaskList(){
		return taskList;
	}
}
