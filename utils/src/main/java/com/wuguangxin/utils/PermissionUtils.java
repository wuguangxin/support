package com.wuguangxin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 权限管理工具类
 * Created by wuguangxin on 2018/8/10.
 */
public class PermissionUtils {

    /**
     * 检查权限是否获取
     *
     * @param context 上下文
     * @param permission 权限
     * @return 是否已获取
     */
    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断是否已经获取该权限
     *
     * @param context 上下文
     * @param permissions 权限集
     * @return 如果其中某个权限为获取，返回false，否则返回true
     */
    public static boolean checkPermission(Context context, String[] permissions) {
        ObjectUtils.requireNonNull(context, "'context' cannot be null");
        if (permissions == null) return true;
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getPackageManager();
        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != packageManager.checkPermission(permission, packageName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测未授权的权限并返回
     *
     * @param context
     * @param permissions 权限集
     * @return
     */
    public static String[] checkUnAcceptPermission(Context context, String... permissions) {
        ObjectUtils.requireNonNull(context, "'context' cannot be null");
        ArrayList<String> list = new ArrayList<>();
        if (permissions != null) {
            for (String permission : permissions) {
                if (!checkPermission(context, permission)) {
                    list.add(permission);
                }
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[0]);
    }

    /**
     * 请求权限。（直接搬用系统的： {@link ActivityCompat#requestPermissions(Activity, String[], int)}）
     *
     * @param activity Activity
     * @param permissions 权限集
     * @param requestCode 请求码
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 请求权限。（直接搬用系统的： {@link ActivityCompat#requestPermissions(Activity, String[], int)}）
     *
     * @param activity Activity
     * @param requestCode 请求码
     * @param permissions 权限集
     */
    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
