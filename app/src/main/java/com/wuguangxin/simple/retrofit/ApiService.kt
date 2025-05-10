package com.wuguangxin.simple.retrofit

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by wuguangxin on 2021/9/7.
 */
interface ApiService {
    companion object {
        const val baseUrl = "http://apis.juhe.cn/"
//        const val baseUrl = "https://api.github.com/"
//        const val baseUrl = "https://earthquake.usgs.gov/"
    }

    @GET("fdsnws/event/1/query")
    fun query(@QueryMap params: HashMap<String?, Any?>): Call<JSONObject>

    @GET("/")
    fun gitHubApi(): Result<String>

}