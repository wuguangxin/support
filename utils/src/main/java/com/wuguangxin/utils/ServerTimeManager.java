package com.wuguangxin.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 模拟运行服务器时间（获取服务器时间戳，本地开启定时器，间隔一秒，时间戳增加一秒）。
 *
 * Created by wuguangxin on 17/4/6.
 */
public class ServerTimeManager {
    private final long SPACE_TIME = 1000; // 间隔执行时间
    private static ServerTimeManager instance;
    private long currentTime;
    private Timer mTime;

    private ServerTimeManager() {}

    public static ServerTimeManager getInstance(){
        if(instance == null){
            synchronized (ServerTimeManager.class) {
                if(instance == null){
                    instance = new ServerTimeManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前本地维持的服务器时间
     * @return
     */
    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * 启动定时器，初始化当前时间，应在获取到系统时间时，调该方法传入
     * @param startTime 服务器的时间戳
     */
    public void start(long startTime) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        this.currentTime = startTime;
        String currentTimeStr = String.valueOf(currentTime);
        String millisStr = currentTimeStr.substring(currentTimeStr.length() - 3, currentTimeStr.length());
        long millis = Long.parseLong(millisStr);
        if (mTime != null) {
            mTime.cancel();
        }
        mTime = new Timer();
        mTime.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime += SPACE_TIME;
            }
        }, 1000-millis, SPACE_TIME);
    }

    /**
     * 关闭本地维持的系统时间，在回调 onDestroy() 时应该调用此方法关闭定时器
     */
    public void cancel() {
        if(mTime != null){
            mTime.cancel();
            mTime = null;
        }
    }
}