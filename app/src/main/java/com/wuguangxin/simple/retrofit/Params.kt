package com.wuguangxin.simple.retrofit

import android.net.Uri
import kotlin.jvm.JvmOverloads
import android.text.TextUtils
import org.json.JSONObject
import java.util.HashMap

/**
 * 封装请求参数的HashMap便捷类
 * Created by wuguangxin on 14/10/13
 */
class Params: HashMap<String, Any?>() {

    /**
     * 获取 String value
     */
    fun optString(key: String?, defValue: String): Any {
        if (TextUtils.isEmpty(key)) {
            return defValue
        }
        return get(key) ?: defValue
    }
    /**
     * 获取int value
     */
    fun optInt(key: String?, defValue: Int): Int {
        if (TextUtils.isEmpty(key)) {
            return defValue
        }
        val obj = get(key)?: "0"
        return obj.toString().toInt()
    }

    /**
     * 获取URL参数串（默认保留最后的&）
     */
    val urlParams: String
        get() {
            if (isEmpty()) return ""
            val builder = Uri.Builder()
            for (key in keys) {
                if (key.isNotEmpty()) {
                    builder.appendQueryParameter(key, get(key).toString())
                }
            }
            return builder.toString()
        }

    /**
     * 转换为JSONString
     */
    fun toJSONString(): String {
        return toJSONObject().toString()
    }

    /**
     * 转换为JSONObject
     */
    fun toJSONObject(): JSONObject {
        return JSONObject(this)
    }

    companion object {
        private const val serialVersionUID = 3728066264475707716L

        /**
         * 为了方便，客户端使用OBJECT作为Params的Value的泛型，再给第三方传时，再转换成 Map<String></String>, String>
         */
        fun valueToString(params: Params?): HashMap<String?, String> {
            val maps = HashMap<String?, String>()
            if (params != null) {
                for (key in params.keys) {
                    maps[key] = params[key].toString()
                }
            }
            return maps
        }
    }
}