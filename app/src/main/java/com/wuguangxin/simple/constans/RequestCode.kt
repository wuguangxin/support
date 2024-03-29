package com.wuguangxin.simple.constans

/**
 * requestCode分类表。
 *
 * Created by wuguangxin on 2016/09/20
 */
object RequestCode {
    const val NONE = 0 // 默认code，别占用
    const val REQUEST_PERMISSIONS = 100 // 权限请求
    const val REQUEST_LOGIN = 110 // 登录

    // 手势
    const val REQUEST_GESTURE_OPEN = 200 // 手势密码-开启
    const val REQUEST_GESTURE_CHANGE = 201 // 手势密码-修改
    const val REQUEST_GESTURE_CHECK = 202 // 手势密码-验证
    const val REQUEST_GESTURE_CLOSE = 203 // 手势密码-关闭
    const val REQUEST_GESTURE_RELOGIN = 210 // 手势密码-密码登录
    const val REQUEST_GESTURE_VERIFY_PASSWORD = 211 // 手势密码-验证登录密码

    // 指纹
    const val REQUEST_FINGERPRINT_UNLOCK = 300 // 指纹-解锁
    const val REQUEST_FINGERPRINT_OPEN = 301 // 指纹-开启
    const val REQUEST_FINGERPRINT_CLOSE = 302 // 指纹-关闭
    const val REQUEST_FINGERPRINT_CHECK = 303 // 指纹-验证
    const val REQUEST_FINGERPRINT_LOGIN = 304 // 指纹-密码登录
}