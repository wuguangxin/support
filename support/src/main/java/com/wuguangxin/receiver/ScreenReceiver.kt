package com.wuguangxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 屏幕开关广播接收器辅助类
 * <p>Created by wuguangxin on 14/5/12 </p>
 */
public class ScreenReceiver {
    private Receiver mReceiver;
    private Context mContext;
    private boolean registered;

    public ScreenReceiver(Context context, Callback callback) {
        this.mContext = context;
        mReceiver = new Receiver(callback);
    }


    /**
     * 注册屏幕状态监听
     * Intent.ACTION_SCREEN_OFF 屏幕灭屏广播；
     * Intent.ACTION_SCREEN_ON 屏幕亮屏广播；
     * Intent.ACTION_USER_PRESENT 屏幕解锁广播；
     * Intent.ACTION_CLOSE_SYSTEM_DIALOGS 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播；
     * example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
     * 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
     */
    public void register() {
        if (!registered) {
            registered = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            mContext.getApplicationContext().registerReceiver(mReceiver, filter);
        }
    }

    /**
     * 注销屏幕状态监听
     */
    public void unregister() {
        if (registered && mReceiver != null) {
            registered = false;
            mContext.getApplicationContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    /**
     * 屏幕开关广播接收者
     */
    public static class Receiver extends BroadcastReceiver {
        Callback callback;

        public Receiver(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (callback != null && intent != null) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) { // 屏幕开
                    callback.onScreenON();
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {  // 屏幕关
                    callback.onScreenOFF();
                }
            }
        }
    }

    /**
     * 屏幕状态监听器
     */
    public interface Callback {
        /** 打开 */
        void onScreenON();

        /** 关闭 */
        void onScreenOFF();
    }
}