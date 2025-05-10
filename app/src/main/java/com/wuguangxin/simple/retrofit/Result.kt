package com.wuguangxin.simple.retrofit

/**
 * Created by wuguangxin on 2021/9/8.
 */
class Result<T> {
    var code: Int = 0
    var msg: String? = null
    var data: T? = null
}