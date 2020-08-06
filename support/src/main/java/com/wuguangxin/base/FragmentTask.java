package com.wuguangxin.base;

import java.util.LinkedList;

import androidx.fragment.app.Fragment;

/**
 * 全局管理Fragment任务栈
 */
public class FragmentTask extends Task<Fragment> {

    private static final String TAG = FragmentTask.class.getSimpleName();
    private static FragmentTask instance;

    private FragmentTask(){}

    public static FragmentTask getInstance() {
        if (instance == null) {
            synchronized (FragmentTask.class) {
                if (instance == null) {
                    instance = new FragmentTask();
                }
            }
        }
        return instance;
    }

    @Override
    public void inTask(Fragment fragment) {
        if (fragment == null) return;
        String className = fragment.toString();
        if (taskList.contains(fragment)) {
            taskList.remove(fragment);
            taskList.addFirst(fragment);
            printTaskInfo(TAG, className, ACTION.UP);  // 移到顶部
        } else {
            taskList.addFirst(fragment);
            printTaskInfo(TAG, className, ACTION.IN);
        }
    }

    @Override
    public void outTask(Fragment fragment) {
        if (fragment != null && taskList != null) {
            if (taskList.remove(fragment)) {
                printTaskInfo(TAG, fragment.getClass().getSimpleName(), ACTION.OUT);
            }
        }
    }

    @Override
    public void clearTask() {
        if (taskList != null && !taskList.isEmpty()) {
            Fragment taskTop;
            String className;
            int len = taskList.size();
            for (int i = 0; i < len; i++) {
                taskTop = taskList.removeLast();
                if (taskTop != null) {
                    className = taskTop.getClass().getSimpleName();
                    printTaskInfo(TAG, className, ACTION.CLEAN);
                }
            }
        }
    }

    /**
     * 获取栈顶Fragment
     *
     * @return 栈顶Fragment
     */
    public Fragment getTopTask() {
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
    public LinkedList<Fragment> getTaskList() {
        return taskList;
    }


}
