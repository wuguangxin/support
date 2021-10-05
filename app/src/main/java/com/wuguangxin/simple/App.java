package com.wuguangxin.simple;

import com.tencent.mmkv.MMKV;
import com.wuguangxin.base.BaseApplication;
import com.wuguangxin.base.SystemManager;
import com.wuguangxin.simple.utils.SmartRefreshLayoutUtils;
import com.wuguangxin.utils.Logger;

/**
 * Application
 *
 * Created by wuguangxin on 17/4/14
 */
public class App extends BaseApplication {

	@Override
	public void onCreate(){
		super.onCreate();
		MMKV.initialize(this);
		Logger.setDebug(BuildConfig.DEBUG);
		Logger.setTagPrefix("wgx/"); // 设置日志Tag前缀，便于过滤
		SystemManager.init(this);
	}

	// 全局初始化刷新样式（使用static代码段可以防止内存泄漏）
	static {
		SmartRefreshLayoutUtils.init();
	}
}
