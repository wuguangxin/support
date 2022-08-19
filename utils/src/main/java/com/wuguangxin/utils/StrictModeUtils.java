package com.wuguangxin.utils;

import android.os.Build;
import android.os.StrictMode;

/**
 * Android 的【严格模式】工具类
 * Created by wuguangxin on 2017/7/14.
 */
public class StrictModeUtils {
    // 优化（https://blog.csdn.net/wq892373445/article/details/103819687）

    /**
     * 开启检测
     */
    public static void start(boolean debug) {
        if (!debug) {
            return;
        }

        /*
        ThreadPolicy线程策略检测：
            线程策略检测的内容有
            detectCustomSlowCalls() 开启自定义的耗时调用
            detectDiskReads()       开启磁盘读取操作
            detectDiskWrites()      开启磁盘写入操作
            detectNetwork()         开启网络操作

        ThreadPolicy 详解：
            setClassInstanceLimit() // 设置某个类的同时处于内存中的实例上限，可以协助检查内存泄露
            detectLeakedRegistrationObjects() // 用来检查 BroadcastReceiver 或者ServiceConnection 注册类对象是否被正确释放
            detectLeakedClosableObjects() // 用于资源没有正确关闭时提醒
            detectLeakedSqlLiteObjects() // 检查 SQLiteCursor 或者 其他 SQLite对象是否被正确关闭
            detectActivityLeaks() // 用户检查 Activity 的内存泄露情况
            detectDiskReads() // 检查磁盘读取
            detectDiskWrites() // 检查磁盘写入
            noteSlowCall 针对执行比较耗时的检查
            penaltyDeath() // 当触发违规条件时，直接Crash掉当前应用程序。
            penaltyDialog() // 触发违规时，显示对违规信息对话框。
            penaltyFlashScreen() // 会造成屏幕闪烁，不过一般的设备可能没有这个功能。
            penaltyDeathOnNetwork() // 当触发网络违规时，Crash掉当前应用程序。
            penaltyDropBox() // 将违规信息记录到 dropbox 系统日志目录中（/data/system/dropbox），你可以通过如下命令进行插件：
        */
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectNetwork()            // 开启网络操作（没问题）
                .detectDiskReads()          // 检查磁盘读取（有问题）
                .detectDiskWrites()         // 检查磁盘写入（没问题）
                .detectCustomSlowCalls()    // API 11，开启自定义的耗时调用，使用StrictMode.noteSlowCode
////                .detectResourceMismatches() // API23
////                .detectUnbufferedIo()       // API26
                .detectAll()                // 这是对以上的总开关

                .penaltyDeathOnNetwork()    // 当触发网络违规时，Crash掉当前应用程序。
                .penaltyFlashScreen()       // 会造成屏幕闪烁，不过一般的设备可能没有这个功能。
                .penaltyLog()               // 在Logcat 中打印违规异常信息
                .penaltyDeath()             // 当触发违规条件时，直接Crash掉当前应用程序。
                .penaltyDialog()            // 触发违规时，弹出违规提示对话框
                .penaltyDropBox()           // 将违规信息记录到 dropbox 系统日志目录中（/data/system/dropbox），你可以通过如下命令进行插件：
                .build());

        /*
        VmPolicy虚拟机策略检测：
            detectActivityLeaks() 开启Activity泄露
            detectLeakedClosableObjects() 开启未关闭的Closable对象泄露
            detectLeakedSqlLiteObjects()  开启泄露的SQLite对象
            setClassInstanceLimit()   开启检测实例数量
        */
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .setClassInstanceLimit()           // 设置某个类的同时处于内存中的实例上限，可以协助检查内存泄露
                .detectLeakedRegistrationObjects() // API16 用来检查 BroadcastReceiver 或者ServiceConnection 注册类对象是否被正确释放
                .detectLeakedClosableObjects()  // 用于资源没有正确关闭时提醒(API等级11)
                .detectLeakedSqlLiteObjects()   // 检查 SQLiteCursor 或者 其他 SQLite对象是否被正确关闭
                .detectActivityLeaks()          // 用户检查 Activity 的内存泄露情况
                // 违规检查
                .penaltyLog()                   //在Logcat 中打印违规异常信息
                .penaltyDeath()                 // 当触发违规条件时，直接Crash掉当前应用程序。
                .penaltyDropBox()               // 将违规信息记录到 dropbox 系统日志目录中（/data/system/dropbox），你可以通过如下命令进行插件：
                .build());
    }


    /**
     * android 7、8 系统解决拍照的 provider 配置问题。
     * 太特么简单粗暴了，provider配置什么的都可以去死了？？？？？
     */
    public static void fileProvider() {
        if (Build.VERSION.SDK_INT >= 18) {
            // VmPolicy虚拟机策略检测（应该检测VM进程中哪些操作（在任何线程上）的策略，以及如果发生此类操作时的惩罚。）
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            // 设置虚拟机策略
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

}
