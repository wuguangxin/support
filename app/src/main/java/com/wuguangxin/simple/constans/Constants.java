package com.wuguangxin.simple.constans;

import android.Manifest;

/**
 * 常量
 */
public class Constants {
	public static boolean DEBUG	= true;


	/**
	 * 6.0动态请求的权限-拍照需要的权限
	 */
	public static final String[] PERMISSION_CAMERA = {
			Manifest.permission.CAMERA,                    // 相机
			Manifest.permission.READ_EXTERNAL_STORAGE,     // 读存储卡
			Manifest.permission.WRITE_EXTERNAL_STORAGE,    // 写存储卡
	};

	/**
	 * 6.0 动态请求的权限-存储空间
	 */
	public static final String[] PERMISSION_EXTERNAL_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,    // 读存储卡
			Manifest.permission.WRITE_EXTERNAL_STORAGE    // 写存储卡
	};

	// 权限名称对照表
	public static String getPermissionDesc(String permission) {
		if (Manifest.permission.CAMERA.equals(permission)) return "相机";
		if (Manifest.permission.READ_PHONE_STATE.equals(permission)) return "手机状态";
		if (Manifest.permission.ACCESS_WIFI_STATE.equals(permission)) return "网络状态";
		if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) return "存储空间";
		if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) return "存储空间";
		return null;
	}
}
