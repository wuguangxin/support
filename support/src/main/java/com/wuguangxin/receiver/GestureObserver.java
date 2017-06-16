package com.wuguangxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 手势密码必须的姿态观察者
 *
 * <p>Created by wuguangxin on 15/3/31 </p>
 */
public class GestureObserver {
    private OnGestureStatusListener onGestureStatusListener;
    private InnerReceiver mReceiver;
    private Context mContext;
    private boolean isRegister;

    public GestureObserver(Context context, OnGestureStatusListener onGestureStatusListener) {
        this.mContext = context;
        this.onGestureStatusListener = onGestureStatusListener;
        this.mReceiver = new InnerReceiver();
    }

    /**
     * 注册监听
     */
    public void register() {
        if (!isRegister) {
            isRegister = true;
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
        if (isRegister && mReceiver != null) {
            isRegister = false;
            mContext.getApplicationContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    /**
     * 广播接收者
     */
    public class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (onGestureStatusListener != null && intent != null) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) { // 屏幕开
                    onGestureStatusListener.onScreenON();
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {  // 屏幕关
                    onGestureStatusListener.onScreenOFF();
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {  // 按home键
                    String reason = intent.getStringExtra("reason");
                    if (reason != null) {
                        if (reason.equals("recentapps")) {
                            onGestureStatusListener.onHomePressedLong(); // 长按Home按键 reason="recentapps"
                        } else {
                            onGestureStatusListener.onHomePressed(); // 按Home按键 reason="homekey"
                        }
                    }
                }
            }
        }
    }

    /**
     * 监听回调接口
     *
     * @author wuguangxin
     */
    public interface OnGestureStatusListener {
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