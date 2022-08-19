package com.wuguangxin.simple.retrofit

import com.wuguangxin.utils.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Created by wuguangxin on 2021/9/8.
 */
object OkHttpUtils {
    private val client: OkHttpClient = build()

    fun getClient(): OkHttpClient = client

    private fun build(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .callTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    // 日志拦截
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Logger.e(OkHttpUtils::class.simpleName, message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}