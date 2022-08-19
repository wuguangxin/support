package com.wuguangxin.base

import androidx.fragment.app.Fragment
import java.util.*

/**
 * 全局管理Fragment任务栈
 */
object FragmentTask: Task<Fragment?>() {

    private val TAG = FragmentTask::class.simpleName

    @Synchronized
    override fun inTask(task: Fragment?) {
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
    override fun outTask(task: Fragment?) {
        task?.let {
            if (taskList.remove(it)) {
                printTaskInfo(TAG, it::class.simpleName, Task.OUT)
            }
        }
    }

    @Synchronized
    override fun clearTask() {
        for (i in 0 until taskList.size) {
            taskList.removeLast()?.let {
                printTaskInfo(TAG, it::class.simpleName, Task.CLEAN)
                try { it.onDestroy() } catch (e: Exception) { }
            }
        }
    }
}