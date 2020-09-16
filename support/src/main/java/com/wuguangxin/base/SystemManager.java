package com.wuguangxin.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import com.wuguangxin.support.BuildConfig;
import com.wuguangxin.utils.AndroidUtils;
import com.wuguangxin.utils.Logger;

/**
 * 系统管信息理器
 * Created by wuguangxin on 17/5/15.
 */
public class SystemManager {
    private static SystemInfo systemInfo;

    public static SystemInfo getSystemInfo() {
        return systemInfo;
    }

    /**
     * 初始化手机信息
     */
    public static void init(Context context) {
        if (context == null) return;
        systemInfo = new SystemInfo();
        systemInfo.appName = context.getApplicationInfo().name;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            systemInfo.packageName = packageInfo.packageName;
            systemInfo.build = packageInfo.versionCode;
            systemInfo.version = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        systemInfo.screenWidth = displayMetrics.widthPixels;
        systemInfo.screenHeight = displayMetrics.heightPixels;
        systemInfo.screenDensity = displayMetrics.density;          // 屏幕密度
        systemInfo.screenDensityDPI = displayMetrics.densityDpi;
        systemInfo.sdk = android.os.Build.VERSION.SDK_INT;
        systemInfo.model = android.os.Build.MODEL;                  // 需要权限：READ_PHONE_STATE
        systemInfo.release = android.os.Build.VERSION.RELEASE;      // 需要权限：READ_PHONE_STATE
        systemInfo.deviceId = AndroidUtils.getDeviceId(context);    // 需要权限：READ_PHONE_STATE
        systemInfo.deviceType = (AndroidUtils.isMobilePhone(context) ? "真机" : "模拟器");
        systemInfo.mac = AndroidUtils.getMac();
        systemInfo.print();
    }

    public static class SystemInfo {
        /** APP包名 */
        public String packageName;
        /** APP名 */
        public String appName;
        /** 版本号 versionCode */
        public int build;
        /** 版本名 versionName */
        public String version;
        /** 屏幕宽 */
        public int screenWidth;
        /** 屏幕高 */
        public int screenHeight;
        /** 屏幕密度 */
        public double screenDensity;
        /** 屏幕密度DPI */
        public double screenDensityDPI;
        /** 设备型号 */
        public String model;
        /** 系统版本 */
        public String release;
        /** SDK版本 */
        public int sdk;
        /** 设备类型 */
        public String deviceType;
        /** 设备MAC */
        public String mac;
        /** 设备 ID */
        public String deviceId;

        /**
         * 打印手机信息
         */
        public void print() {
            StringBuilder sb = new StringBuilder(".\n")
                    .append("╔════════════════════设备信息═══════════════════╗\n")
                    .append("║ 应用包名：\t").append(packageName).append("\n")
                    .append("║ 应用名称：\t").append(appName).append("\n")
                    .append("║   Debug：\t").append(BuildConfig.DEBUG).append("\n")
                    .append("║   编译号：\t").append(build).append("\n")
                    .append("║   版本名：\t").append(version).append("\n")
                    .append("║ 屏幕宽度：\t").append(screenWidth).append("\n")
                    .append("║ 屏幕高度：\t").append(screenHeight).append("\n")
                    .append("║ 手机型号：\t").append(model).append("\n")  // 需要权限：READ_PHONE_STATE
                    .append("║ 系统版本：\t").append(release).append("\n") // 需要权限：READ_PHONE_STATE
                    .append("║     SDK：\t").append(sdk).append("\n")
                    .append("║ 设备类型：\t").append(deviceType).append("\n")
                    .append("║ 设备MAC：\t").append(mac).append("\n")
                    .append("║  设备ID：\t").append(deviceId).append("\n") // 需要权限：READ_PHONE_STATE
                    .append("║ 屏幕密度（每英寸像素点）：\t").append(screenDensity).append("\n")
                    .append("║ 密度DPI（设备独立像素）：\t").append(screenDensityDPI).append("\n")
                    .append("╚══════════════════════════════════════════════╝\n\n").append("\n");
            Logger.i("SystemManager", sb.toString());
        }
    }
}
