package com.wuguangxin.simple.retrofit

import com.wuguangxin.base.SystemManager
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.util.*

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newBuilder = chain.request().newBuilder();
        val request = newBuilder.addHeader("Common-Headers", getHeaderJson()).build()
        return chain.proceed(request)
    }

    private fun getHeaderJson(): String {
        val info = SystemManager.getSystemInfo()
        return JSONObject()
            .put("platform", "android")
            .put("sessionId", UUID.randomUUID().toString())
            .put("deviceId", info.deviceId)
            .put("versionCode", info.build)
            .put("versionName", info.version)
            .toString()
    }
}