package com.wuguangxin.base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * 基础 Application
 */
public abstract class BaseApplication extends Application {
    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * 退出程序，并杀死当前进程
     */
    public void exitApp() {
        ActivityTask.getInstance().clearTask();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
