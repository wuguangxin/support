package com.wuguangxin.simple;

import android.app.Application;
import android.content.Context;

import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshInitializer;
import com.tencent.mmkv.MMKV;
import com.wuguangxin.base.ActivityTask;
import com.wuguangxin.simple.constans.Constants;
import com.wuguangxin.utils.Logger;
import com.wuguangxin.utils.mmkv.Config;

import androidx.annotation.NonNull;

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
		MMKV.initialize(this);
		Logger.setDebug(Constants.DEBUG);
		Logger.setTagPrefix("wgx_");				// 设置日志Tag前缀，便于过滤
	}

	/**
	 * 退出程序
	 * @param context
	 */
	public static void exitApp(Context context){
		Logger.i(TAG, "退出程序");
		ActivityTask.getInstance().clearTask();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	// 全局初始化刷新样式（使用static代码段可以防止内存泄漏）
	static {
		//设置全局默认配置（优先级最低，会被其他设置覆盖）
		SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
			@Override
			public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
				//开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
				layout.setReboundDuration(700);
//                layout.setReboundInterpolator(new LinearInterpolator());
//                layout.setFooterHeight(100);
				layout.setDisableContentWhenLoading(false);
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
			}
		});

		//全局设置默认的 Header
		SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
			@Override
			public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
				//开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
				layout.setEnableHeaderTranslationContent(true);
				return new ClassicsHeader(context);
			}
		});
	}
}
