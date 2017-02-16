package com.wuguangxin.listener;

import com.wuguangxin.utils.Logger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 创建此监听器，便捷的打开目标界面
 * @author wuguangxin
 * @date: 2014-9-22 上午11:57:50
 */
public class OpenActivityListener  implements OnClickListener{
	private Class<?> clazz;
	private Context context;
	private Intent intent;
	private Bundle bundle;

	public OpenActivityListener(Context context, Class<?> clazz){
		super();
		this.context = context;
		this.clazz = clazz;
	}
	
	public OpenActivityListener(Context context, Bundle bundle, Class<?> clazz){
		super();
		this.context = context;
		this.bundle = bundle;
		this.clazz = clazz;
	}
	
	@Override
	public void onClick(View v){
		Logger.i(context, "打开：" + clazz.getName());
		if(context != null && clazz != null){
			intent = new Intent(context, clazz);
			if(bundle != null){
				intent.putExtra("bundle", bundle);
				intent.putExtras(bundle);
			}
			context.startActivity(intent);
		}
	}
}
