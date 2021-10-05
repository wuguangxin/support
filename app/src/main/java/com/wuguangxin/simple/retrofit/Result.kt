package com.wuguangxin.simple.retrofit

/**
 * Created by wuguangxin on 2021/9/8.
 */
class Result<T> {
    var resultcode: String? = null
    var error_code: Int? = null
    var reason: String? = null
    var result: T? = null
}