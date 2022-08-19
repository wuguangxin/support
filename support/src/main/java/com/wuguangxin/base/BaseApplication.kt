package com.wuguangxin.base

import android.app.Application
import android.content.Context
import android.os.Process.killProcess
import android.os.Process.myPid
import androidx.multidex.MultiDex
import java.lang.ref.SoftReference
import kotlin.system.exitProcess

/**
 * 基础 Application
 */
abstract class BaseApplication : Application() {

    open fun getContent(): Context? = contextRef.get()

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        contextRef = SoftReference(this)
    }

    companion object {
        lateinit var contextRef: SoftReference<Context>

        fun getContent(): Context? = contextRef.get()

        /**
         * 退出程序，并杀死当前进程
         */
        fun exitApp() {
            ActivityTask.clearTask()
            exitProcess(0)
            killProcess(myPid())
        }
    }
}