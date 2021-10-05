package com.wuguangxin.simple.retrofit

import com.wuguangxin.simple.App
import com.wuguangxin.utils.AndroidUtils
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
        val obj = JSONObject()
        return obj.apply {
            obj.put("platform", "android")
            obj.put("sessionId", UUID.randomUUID().toString())
            obj.put("deviceId", AndroidUtils.getDeviceId(App.getContext()))
            obj.put("versionCode", AndroidUtils.getVersionCode(App.getContext()))
            obj.put("versionName", AndroidUtils.getVersionName(App.getContext()))
        }.toString()
    }
}