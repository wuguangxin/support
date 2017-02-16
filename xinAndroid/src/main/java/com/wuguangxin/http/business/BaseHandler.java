package com.wuguangxin.http.business;

import android.os.Handler;

import java.io.File;

/**
 * Http请求结果处理的的一些回调接口
 *
 * @author: wuguangxin
 * @date: 2015/3/31 11:37
 */
public abstract class BaseHandler extends Handler {
	/**
	 * 请求开始
	 * @param key 请求参数的MD5值，该值可用作缓存的key
	 */
	public void onStart(String key){}
	
	/**
	 * 计算文件长度
	 * @param count 大小
	 */
	public void onCountFileSize(int count){}

	/**
	 * 得到数据字节大小
	 * @param length 大小
	 */
	public void onCountByteLength(int length){}
	
	/**
	 * 下载进度
	 * @param progress 下载进度
	 * @param speed 下载速率(speed/秒)
	 */
	public void onProgress(long progress, long speed){}

	/**
	 * 成功
	 * @param strResponse 返回的字符串
	 */
	public void onSuccess(String strResponse){}
	
	/**
	 * 下载成功
	 * @param file 下载的文件
	 */
	public void onSuccess(File file){}
	
	/**
	 * 失败
	 * @param msg 错误信息
	 */
	public void onFailure(String msg){}
	
	/**
	 * 取消
	 */
	public void onCancel(){}

	/**
	 * 完成
	 */
	public void onFinish(){}

	/**
	 * 模拟服务器返回的字符串
	 * <p/>
	 * 开发时方便测试数据使用。在开发测试初期，可能服务器接口暂时无法联调，可以调用此方法模拟服务器返回的数据，<br>
	 * 在调接口时，将先判断该方法的返回值，如果该值不为空，将使用该值作为返回值，并且不会做网络请求操作。<br>
	 */
	public String getTestData(){
		return null;
	}

	/**
	 * 子类必须实现加密数据算法
	 * @param text
	 * @return
     */
	public abstract String encode(String text);

	/**
	 * 子类必须实现解密数据算法
	 * @param text
	 * @return
     */
	public abstract String decode(String text);

	/**
	 * 由子类实现MD5加密方法
	 * @param text
	 * @return
	 */
	public abstract String md5(String text);
}
