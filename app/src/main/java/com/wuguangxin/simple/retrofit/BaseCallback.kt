package com.wuguangxin.simple.retrofit

import com.wuguangxin.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseCallback<T : Result<*>?> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val code = response.code()
        val result = response.body()
        if (code == 200) {
            onSuccess(result)
        } else {
            onError(code, response.message())
        }
        onComplete()
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onError(-1, t.message)
        onComplete()
    }

    /**
     * 当成功时回调
     */
    fun onSuccess(result: T?) {}

    /**
     * 当错误时回调
     */
    fun onError(code: Int, msg: String?) {
        Logger.e(TAG, "请求失败：errorCode=$code errorMsg=$msg")
    }

    /**
     * 当请求完成时回调
     */
    fun onComplete() {
        Logger.i(TAG, "请求完成")
    }

    companion object {
        private val TAG = BaseCallback::class.java.simpleName
    }
}