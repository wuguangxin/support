package com.wuguangxin.base;

import com.wuguangxin.utils.Logger;

import java.util.LinkedList;

public abstract class Task<T> {

    LinkedList<T> taskList = new LinkedList<>();

    /**
     * 入栈
     * @param task 入栈的 Activity/Fragment
     */
    abstract void inTask(T task);

    /**
     * 出栈
     * @param task 出栈的 Activity/Fragment
     */
    abstract void outTask(T task);

    /**
     * 清栈，销毁所有 Activity/Fragment
     */
    abstract void clearTask();

    /**
     * 获取栈顶 Activity/Fragment
     * @return 栈顶Activity/Fragment
     */
    abstract T getTopTask();

//	/**
//	 * 回退栈(如从A-B-C-D, 在D界面直接返回到A,现在需要把D,C,B给Finish掉,这时的回退次数num为3)
//	 * @param num 回退的位数
//	 */
//	void backTask(int num);

//	/**
//	 * 回退栈(如从A-B-C-D, 在D界面直接返回到A,会将D,C,B给直接销毁掉)
//	 * @param taskClass 回退到该 Activity/Fragment.class
//	 */
//	void backTask(Class taskClass);

    /**
     * 获取当前栈大小
     * @return
     */
    abstract int getTaskSize();

    /**
     * @return 列表
     */
    abstract LinkedList<T> getTaskList();

    interface ACTION {
        String UP = "UP"; // 上浮
        String IN = "IN"; // 入栈
        String OUT = "OUT"; // 出栈
        String CLEAN = "CLEAN"; // 清栈
    }

    void printTaskInfo(String tag, String className, String msg) {
        String taskTop = "null";
        int taskSize = 0;
        if (taskList != null) {
            taskSize = taskList.size();
            if (taskSize > 0) {
                taskTop = taskList.getFirst().getClass().getSimpleName();
            }
        }
        Logger.d(tag, String.format("%s:%s   TOP:%s   TASK SIZE:%s", msg, className, taskTop, taskSize));
    }

}
