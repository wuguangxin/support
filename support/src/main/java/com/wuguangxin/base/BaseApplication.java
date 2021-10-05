package com.wuguangxin.base;

import android.app.Application;
import android.content.Context;

import java.lang.ref.SoftReference;

import androidx.multidex.MultiDex;

/**
 * 基础 Application
 */
public abstract class BaseApplication extends Application {

    private static SoftReference<Context> mContextRef;

    public static Context getContext() {
        return mContextRef.get();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContextRef = new SoftReference<>(this);
    }

    /**
     * 退出程序，并杀死当前进程
     */
    public static void exitApp() {
        ActivityTask.getInstance().clearTask();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
