package com.wuguangxin.base

import android.content.Context
import android.os.Build
import com.wuguangxin.support.BuildConfig
import com.wuguangxin.utils.AndroidUtils
import com.wuguangxin.utils.Logger
import com.wuguangxin.utils.NetworkUtils
import java.lang.Exception
import java.lang.StringBuilder

/**
 * 系统管信息理器
 * Created by wuguangxin on 17/5/15.
 */
object SystemManager {
    private val systemInfo = SystemInfo()

    fun getSystemInfo(): SystemInfo = systemInfo

    /**
     * 初始化手机信息
     */
    fun init(context: Context) {
        systemInfo.appName = context.applicationInfo.name
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            systemInfo.packageName = packageInfo.packageName
            systemInfo.build = packageInfo.versionCode
            systemInfo.version = packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val displayMetrics = context.resources.displayMetrics
        systemInfo.screenWidth = displayMetrics.widthPixels
        systemInfo.screenHeight = displayMetrics.heightPixels
        systemInfo.screenDensity = displayMetrics.density.toDouble() // 屏幕密度
        systemInfo.screenDensityDPI = displayMetrics.densityDpi.toDouble()
        systemInfo.sdk = Build.VERSION.SDK_INT
        systemInfo.model = Build.MODEL // 需要权限：READ_PHONE_STATE
        systemInfo.release = Build.VERSION.RELEASE // 需要权限：READ_PHONE_STATE
        systemInfo.deviceId = "----" //AndroidUtils.getDeviceId(context) // 需要权限：READ_PHONE_STATE
        systemInfo.deviceType = "----" //(AndroidUtils.isMobilePhone(context) ? "真机" : "模拟器");
        systemInfo.mac = AndroidUtils.getMac()
        systemInfo.print()
    }

    class SystemInfo {
        /** APP包名  */
        var packageName: String? = null

        /** APP名  */
        var appName: String? = null

        /** 版本号 versionCode  */
        var build = 0

        /** 版本名 versionName  */
        var version: String? = null

        /** 屏幕宽  */
        var screenWidth = 0

        /** 屏幕高  */
        var screenHeight = 0

        /** 屏幕密度  */
        var screenDensity = 0.0

        /** 屏幕密度DPI  */
        var screenDensityDPI = 0.0

        /** 设备型号  */
        var model: String? = null

        /** 系统版本  */
        var release: String? = null

        /** SDK版本  */
        var sdk = 0

        /** 设备类型  */
        var deviceType: String? = null

        /** 设备MAC  */
        var mac: String? = null

        /** 设备 ID  */
        var deviceId: String? = null

        /**
         * 打印手机信息
         */
        fun print() {
            val sb = StringBuilder(".\n")
                .append("╔════════════════════设备信息═══════════════════╗\n")
                .append("║ 应用包名：\t").append(packageName).append("\n")
                .append("║ 应用名称：\t").append(appName).append("\n")
                .append("║   Debug：\t").append(BuildConfig.DEBUG).append("\n")
                .append("║   编译号：\t").append(build).append("\n")
                .append("║   版本名：\t").append(version).append("\n")
                .append("║ 屏幕宽度：\t").append(screenWidth).append("\n")
                .append("║ 屏幕高度：\t").append(screenHeight).append("\n")
                .append("║ 手机型号：\t").append(model).append("\n") // 需要权限：READ_PHONE_STATE
                .append("║ 系统版本：\t").append(release).append("\n") // 需要权限：READ_PHONE_STATE
                .append("║     SDK：\t").append(sdk).append("\n")
                .append("║ 设备类型：\t").append(deviceType).append("\n")
                .append("║ 设备MAC：\t").append(mac).append("\n")
                .append("║  设备ID：\t").append(deviceId).append("\n") // 需要权限：READ_PHONE_STATE
                .append("║ 屏幕密度（每英寸像素点）：\t").append(screenDensity).append("\n")
                .append("║ 密度DPI（设备独立像素）：\t").append(screenDensityDPI).append("\n")
                .append("╚══════════════════════════════════════════════╝\n\n").append("\n")
            Logger.i("SystemManager", sb.toString())
        }
    }
}