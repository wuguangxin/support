package com.wuguangxin.base;

import android.app.Application;
import android.content.Context;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.wuguangxin.support.BuildConfig;
import com.wuguangxin.utils.FileUtils;
import com.wuguangxin.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * 基础 Application
 */
public abstract class BaseApplication extends Application {
    private static WeakReference<Context> mContext;

    public static Context getContext() {
        return mContext.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = new WeakReference<>(this);
        Logger.setDebug(BuildConfig.DEBUG);
//        DES.setKey("@REDPACKETROB@.@");
//        MmkvUtils.init(this, "@MMKV@COM.#");
        FileUtils.init(this);
        initRefreshLayout();
    }

    /**
     * 设置SmartRefreshLayout的刷新header
     */
    public void initRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            return new TwoLevelHeader(context);
//            return new FalsifyHeader(context);
//            return new ClassicsHeader(context); // 文字 正在刷新
//            return new BezierRadarHeader(context).setEnableHorizontalDrag(false));
        });
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
