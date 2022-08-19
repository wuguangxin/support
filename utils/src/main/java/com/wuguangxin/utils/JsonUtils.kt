package com.wuguangxin.utils

import com.google.gson.GsonBuilder
import org.json.JSONObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type

/**
 * Json解析工具类，异常已处理
 *
 * Created by wuguangxin on 2022/1/12.
 */
object JsonUtils {

    @JvmStatic
    fun getGson(): Gson = mGson

    private val mGson by lazy {
        createGson()
    }

    private fun createGson(): Gson {
        return GsonBuilder().serializeNulls().disableHtmlEscaping().create()
    }

    /**
     * 解析为json字符串
     * @param any list/map/javaBean
     * @return
     */
    @JvmStatic
    fun <T> toJson(any: T?): String? {
        try {
            return getGson().toJson(any)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun <T> toBean(json: String?, clazz: Class<T>): T? {
        try {
            return getGson().fromJson(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun <T> fromJson(json: String?): T? {
        return fromJson(json, null)
    }

    @JvmStatic
    fun <T> fromJson(json: String?, type: Type?): T? {
        try {
            val t = type?: object: TypeToken<T>() {}.type
            val result = getGson().fromJson<T>(json, t)
            if (result != null) {
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

//    inline fun <reified T> fromJsonInline(json: String?): T? {
//        return fromJsonInline(json, null as T?)
//    }
//
//    inline fun <reified T> fromJsonInline(json: String?, def: T?): T? {
//        return fromJsonInline(json, null, def)
//    }
//
//    /**
//     * json字符串 ==> List/Map等
//     * @param json json数组字符串
//     * @param type 泛型Type：参考 Type type = new TypeToken<List></List><Object>>(){}.getType();
//     * @param defValue 默认值
//     * @return
//     */
//    inline fun <reified T> fromJsonInline(json: String?, type: Type?, defValue: T?): T? {
//        try {
//            val t = type?: object: TypeToken<T>() {}.type
//            val result = getGson().fromJson<T>(json, t)
//            if (result != null) {
//                return result
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return defValue
//    }
}