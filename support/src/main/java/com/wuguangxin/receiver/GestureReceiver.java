package com.wuguangxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 手势密码必须的姿态观察者
 * <p>Created by wuguangxin on 15/3/31 </p>
 */
public class GestureReceiver {
    private Receiver mReceiver;
    private Context mContext;
    private boolean registered; // 是否注册过

    public GestureReceiver(Context context, Callback callback) {
        this.mContext = context;
        mReceiver = new Receiver(callback);
    }

    /**
     * 注册监听
     */
    public void register() {
        if (!registered) {
            registered = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            mContext.getApplicationContext().registerReceiver(mReceiver, filter);
        }
    }

    /**
     * 注销监听
     */
    public void unregister() {
        if (registered && mReceiver != null) {
            registered = false;
            mContext.getApplicationContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    /**
     * 广播接收者
     */
    public static class Receiver extends BroadcastReceiver {
        Callback callback;

        public Receiver(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (callback == null || intent == null) {
                return;
            }
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // 屏幕开
                callback.onScreenON();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 屏幕关
                callback.onScreenOFF();
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                // 按home键
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if ("recentapps".equals(reason)) {
                        // 长按Home按键 reason="recentapps"
                        callback.onHomePressedLong();
                    } else {
                        // 按Home按键 reason="homekey"
                        callback.onHomePressed();
                    }
                }
            }
        }
    }

    /**
     * 监听回调接口
     */
    public interface Callback {

        /** Home键被按下（轻按） */
        void onHomePressed();

        /** Home键被按下（长按） */
        void onHomePressedLong();

        /** 屏幕亮 */
        void onScreenON();

        /** 屏幕暗 */
        void onScreenOFF();
    }
}