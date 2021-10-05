package com.wuguangxin.simple.ui

import com.wuguangxin.simple.retrofit.ApiService
import com.wuguangxin.simple.retrofit.RetrofitUtils
import com.wuguangxin.ui.XinBaseActivity

abstract class BaseActivity : XinBaseActivity() {

    fun getService(): ApiService {
        return RetrofitUtils.getService()
    }

    fun <T> getService(clazz: Class<T>): T {
        return RetrofitUtils.getService(clazz)
    }

    fun <T> getService(baseUrl: String?, clazz: Class<T>): T {
        return RetrofitUtils.getService(baseUrl, clazz)
    }

    fun println(s: String) = this.printLogI(s)
}