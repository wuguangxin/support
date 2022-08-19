package com.wuguangxin.simple.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by wuguangxin on 2021/9/8.
 */
enum class RetrofitUtils {
    INSTANCE;

    private var retrofit: Retrofit? = null

    init {
        retrofit = buildRetrofit(null)
    }

    private fun buildRetrofit(baseUrl: String?): Retrofit {
        val url = baseUrl ?: ApiService.baseUrl
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder().apply {
            baseUrl(url)
            client(OkHttpUtils.getClient())
            addConverterFactory(GsonConverterFactory.create(gson))
        }.build()
    }

    companion object {
        fun getService(): ApiService {
            return getService(null)
        }

        fun getService(baseUrl: String?): ApiService {
            return getService(baseUrl, ApiService::class.java)
        }

        fun <T> getService(clazz: Class<T>): T {
            return getService(null, clazz)
        }

        fun <T> getService(baseUrl: String?, clazz: Class<T>): T {
            return INSTANCE.buildRetrofit(baseUrl).create(clazz)
        }
    }
}