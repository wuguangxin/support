package com.wuguangxin.simple.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.widget.Toast
import android.content.ComponentName

import android.content.pm.ResolveInfo




/**
 * Created by wuguangxin on 2021/9/7.
 */
object AppUtils {

    /**
     *
     * @param flag 查询类型 PackageManager.MATCH_SYSTEM_ONLY 参考：https://blog.csdn.net/cowbin2012/article/details/112448866
     */
    @SuppressLint("QueryPermissionsNeeded")
    @JvmStatic
    fun getInstalledPackages(context: Context, flag: Int?=0): MutableList<PackageInfo> {
        try {
            val packageManager = context.packageManager
            return packageManager.getInstalledPackages(flag?:0)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }

    @JvmStatic
    fun isSystem(context: Context, pi: PackageInfo): Boolean {
        val ai = pi.applicationInfo ?: return false
        return ApplicationInfo.FLAG_SYSTEM and ai.flags != 0
    }

    @JvmStatic
    fun getAppIcon(context: Context, pi: PackageInfo): Drawable? {
        val ai = pi.applicationInfo ?: return null
        return ai.loadIcon(context.packageManager)
    }

    @JvmStatic
    fun getAppName(context: Context, pi: PackageInfo): String? {
        val ai = pi.applicationInfo ?: return pi.packageName
        return ai.loadLabel(context.packageManager).toString()
    }


    /**
     * 启动应用程序
     *
     * @param context context
     * @param packInfo 包信息
     */
    fun startApplication(context: Context, packInfo: PackageInfo) {
        // 开启这个应用程序的第一个activity. 默认情况下 第一个activity就是具有启动能力的activity.
        val pm = context.packageManager
        try {
            // 懒加载
            val activities = packInfo.activities
            if (activities != null && activities.isNotEmpty()) {
                val className = activities[0].name
                val intent = Intent()
                intent.setClassName(packInfo.packageName, className)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "该应用程序没有界面", Toast.LENGTH_LONG).show()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Toast.makeText(context, "无法启动应用", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 分享应用程序,启动系统的分享界面
     *
     * @param context context
     */
    fun shareApplication(context: Context, packName: String) {
        // <action android:name="android.intent.action.SEND" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:mimeType="text/plain" />
        val intent = Intent()
        intent.addCategory("android.intent.category.DEFAULT")
        intent.action = "android.intent.action.SEND"
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件，下载地址为:https://play.google.com/store/apps/details?id=$packName")
        context.startActivity(intent)
    }


    fun startApp(context: Context, packageName: String) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                context.startActivity(intent)
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        startAppWithPackageName(context, packageName);
    }

    fun startAppWithPackageName(context: Context, packageName: String) {
        try {
            // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = context.applicationContext.packageManager.getPackageInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            if (packageInfo == null) {
                return
            }
            val resolveIntent = Intent(Intent.ACTION_MAIN, null)
            //resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);这个不能添加，否则和上面方法是一样的。
            resolveIntent.setPackage(packageInfo.packageName)

            // 通过getPackageManager()的queryIntentActivities方法遍历
            val resolveInfoList: List<ResolveInfo> = context.packageManager.queryIntentActivities(resolveIntent, 0)
            val resolveInfo = resolveInfoList.iterator().next()
            // packagename = 参数packname
            val pName = resolveInfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            val className = resolveInfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            val cn = ComponentName(pName, className)
            intent.component = cn
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}