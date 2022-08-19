package com.wuguangxin.base

import android.app.Activity

/**
 * 全局管理Activity任务栈
 */
object ActivityTask : Task<Activity?>() {

    private val TAG = ActivityTask::class.simpleName

    @Synchronized
    override fun inTask(task: Activity?) {
        task?.let {
            if (taskList.contains(it)) {
                taskList.remove(it)
                taskList.addFirst(it)
                printTaskInfo(TAG, it::class.simpleName, Task.UP) // 移到顶部
            } else {
                taskList.addFirst(it)
                printTaskInfo(TAG, it::class.simpleName, Task.IN)
            }
        }
    }

    @Synchronized
    override fun outTask(task: Activity?) {
        task?.let {
            if (taskList.remove(it)) {
                printTaskInfo(TAG, it::class.simpleName, Task.OUT)
                it.finish()
            }
        }
    }

    @Synchronized
    override fun clearTask() {
        for (i in 0 until taskList.size) {
            taskList.removeLast()?.let {
                printTaskInfo(TAG, it::class.simpleName, Task.CLEAN)
                it.finish()
            }
        }
    }
}