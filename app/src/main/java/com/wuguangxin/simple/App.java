package com.wuguangxin.simple;

import android.app.Application;
import android.content.Context;

import com.wuguangxin.base.AppTask;
import com.wuguangxin.utils.Logger;
import com.wuguangxin.utils.MoneyUtils;

/**
 * Application
 *
 * Created by wuguangxin on 17/4/14
 */
public class App extends Application {
	public static final String TAG = "App";
	/**
	 * Application上下文
	 */
	public static Context mContext;

	@Override
	public void onCreate(){
		super.onCreate();
		mContext = this;
		// 基本信息
		MoneyUtils.setIsClearZero(true);
		Logger.setDebug(Constants.DEBUG);
		Logger.setTagPrefix("wgx_");				// 设置日志Tag前缀，便于过滤
	}

	/**
	 * 退出程序
	 * @param context
	 */
	public static void exitApp(Context context){
		Logger.i(TAG, "退出程序");
		AppTask.clearTask();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
