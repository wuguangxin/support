package com.wuguangxin.http;

/**
 * Http异步处理状态
 *
 * @author wuguangxin
 * @date: 2014-12-31 下午3:07:46
 */
public class HttpState{
	/**
	 * 请求开始
	 */
	public static final int START = 100;
	
	/**
	 * 文件大小
	 */
	public static final int COUNT = 110;
	
	/**
	 * 下载进度
	 */
	public static final int PROGRESS = 111;
	
	/**
	 * 请求成功
	 */
	public static final int SUCCESS = 200;
	
	/**
	 * 请求取消
	 */
	public static final int CANCEL = 300;
	
	/**
	 * 请求失败
	 */
	public static final int FAILURE = 400;
	
	/**
	 * 请求完成
	 */
	public static final int FINISH = 900;
	
	/**
	 * Http请求类型
	 *
	 * @author wuguangxin
	 * @date: 2014-12-31 下午6:30:55
	 */
	static enum HttpType{
		/**
		 * Http请求类型为GET
		 */
		GET, 
		
		/**
		 * Http请求类型为POST
		 */
		POST
	}
}
