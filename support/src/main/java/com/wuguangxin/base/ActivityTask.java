package com.wuguangxin.base;

import android.app.Activity;

import java.util.LinkedList;

/**
 * 全局管理Activity任务栈
 */
public class ActivityTask extends Task<Activity> {

    private static final String TAG = ActivityTask.class.getSimpleName();
    private static ActivityTask instance;

    private ActivityTask(){}

    public static ActivityTask getInstance() {
        if (instance == null) {
            synchronized (ActivityTask.class) {
                if (instance == null) {
                    instance = new ActivityTask();
                }
            }
        }
        return instance;
    }

    @Override
    public void inTask(Activity activity) {
        if (activity == null) return;
        String className = activity.getClass().getSimpleName();
        if (taskList.contains(activity)) {
            taskList.remove(activity);
            taskList.addFirst(activity);
            printTaskInfo(TAG, className, ACTION.UP);  // 移到顶部
        } else {
            taskList.addFirst(activity);
            printTaskInfo(TAG, className, ACTION.IN);
        }
    }

    @Override
    public void outTask(Activity activity) {
        if (activity != null && taskList != null) {
            if (taskList.remove(activity)) {
                printTaskInfo(TAG, activity.getClass().getSimpleName(), ACTION.OUT);
                activity.finish();
            }
        }
    }

    @Override
    public void clearTask() {
        if (taskList != null && !taskList.isEmpty()) {
            Activity taskTop;
            String className;
            int len = taskList.size();
            for (int i = 0; i < len; i++) {
                taskTop = taskList.removeLast();
                if (taskTop != null) {
                    className = taskTop.getClass().getSimpleName();
                    printTaskInfo(TAG, className, ACTION.CLEAN);
                    taskTop.finish();
                }
            }
        }
    }

    /**
     * 获取栈顶Activity
     *
     * @return 栈顶Activity
     */
    public Activity getTopTask() {
        if (taskList != null && !taskList.isEmpty()) {
            return taskList.getFirst();
        }
        return null;
    }

    @Override
    public int getTaskSize() {
        return taskList != null ? taskList.size() : 0;
    }

    @Override
    public LinkedList<Activity> getTaskList() {
        return taskList;
    }
}
