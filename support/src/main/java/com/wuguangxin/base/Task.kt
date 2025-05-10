package com.wuguangxin.base

import com.wuguangxin.utils.Logger
import java.util.*

abstract class Task<T> {

    val taskList = LinkedList<T>()

    /**
     * 获取栈顶Activity
     * @return 栈顶Activity
     */
    val topTask: T? get() = if (taskSize == 0) null else taskList.first

    /**
     * 获取当前栈大小
     * @return
     */
    val taskSize: Int get() = taskList.size

    /**
     * 入栈
     * @param task 入栈的 Activity/Fragment
     */
    abstract fun inTask(task: T)

    /**
     * 出栈
     * @param task 出栈的 Activity/Fragment
     */
    abstract fun outTask(task: T)

    /**
     * 清栈，销毁所有 Activity/Fragment
     */
    abstract fun clearTask()

    /**
     * 回退栈(如从A-B-C-D, 在D界面直接返回到A,现在需要把D,C,B给Finish掉,这时的回退次数num为3)
     * @param count 回退几次
     */
    //abstract fun backTask(count: Int)

    /**
     * 回退栈(如从A-B-C-D, 在D界面直接返回到A,会将D,C,B给直接销毁掉)
     * @param taskClass 回退到该 Activity/Fragment.class
     */
    //abstract fun backTask(taskClass: Class<T>);

    fun printTaskInfo(tag: String?, className: String?, msg: String?) {
        var taskTop = "null"
        if (taskList.size > 0) {
            taskList.first?.let {
                taskTop = it::class.java.simpleName
            }
        }
        Logger.d(tag, "$msg:$className   TOP:$taskTop   TASK SIZE:$taskSize")
    }

    companion object {
        const val UP = "UP" // 上浮
        const val IN = "IN" // 入栈
        const val OUT = "OUT" // 出栈
        const val CLEAN = "CLEAN" // 清栈
    }
}