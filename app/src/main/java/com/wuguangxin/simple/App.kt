package com.wuguangxin.simple

import android.content.Context
import com.wuguangxin.base.SystemManager.init
import com.wuguangxin.base.BaseApplication
import com.tencent.mmkv.MMKV
import com.wuguangxin.simple.utils.SmartRefreshLayoutUtils
import com.wuguangxin.utils.Logger

/**
 * Application
 *
 * Created by wuguangxin on 17/4/14
 */
class App : BaseApplication() {

    override fun getContent(): Context? = contextRef.get()

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        Logger.setDebug(BuildConfig.DEBUG)
        Logger.setTagPrefix("wgx/") // 设置日志Tag前缀，便于过滤
        init(this)
        SmartRefreshLayoutUtils.init()
    }

}