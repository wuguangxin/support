package com.wuguangxin.simple.constans

import android.Manifest
import com.wuguangxin.simple.bean.UserBean
import com.wuguangxin.utils.MD5

/**
 * 常量
 */
object Constants {
    var DEBUG = true

    /**
     * 6.0 动态请求的权限-存储空间
     */
    val PERMISSION_EXTERNAL_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,  // 读存储卡
        Manifest.permission.WRITE_EXTERNAL_STORAGE  // 写存储卡
    )

    /**
     * 权限名称对照表
     */
    fun getPermissionDesc(permission: String): String? {
        return when (permission) {
            Manifest.permission.CAMERA -> "相机"
            Manifest.permission.READ_PHONE_STATE -> "手机状态"
            Manifest.permission.ACCESS_WIFI_STATE -> "网络状态"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "存储空间"
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> "存储空间"
            else -> null
        }
    }

    val userList by lazy {
        mutableListOf(
            UserBean("18688888888", MD5.encode("123456"), "刘德华", "男", 18),
            UserBean("18699999999", MD5.encode("123456"), "张三丰", "男", 20)
        )
    }
}