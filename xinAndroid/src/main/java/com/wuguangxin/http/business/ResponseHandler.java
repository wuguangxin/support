package com.wuguangxin.http.business;

import android.os.Message;

import com.wuguangxin.http.HttpError;
import com.wuguangxin.http.HttpState;
import com.wuguangxin.utils.MD5;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.io.File;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 请求服务器时回调类
 * 
 * @author wuguangxin
 * @date: 2014-12-29 下午6:27:12
 */
public class ResponseHandler extends BaseHandler{
	private boolean cancelled;
	
	@Override
	final public void handleMessage(Message msg){
		switch (msg.what) {
			case HttpState.START:
				onStart(md5((String)msg.obj));
				break;
			case HttpState.COUNT:
				try {
					onCountFileSize((Integer) msg.obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case HttpState.PROGRESS:
				try {
					long[] data = (long[]) msg.obj;
					if(data != null && data.length == 2){
						long size = data[0];
						long cur = data[1];
						if(cur < 1){
							cur = 1;
						}
						onProgress(size, size / cur * 1000l);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case HttpState.SUCCESS:
				if (msg.obj != null) {
					if (msg.obj instanceof File) {
						onSuccess((File) msg.obj);
					} else {
						String str = (String) msg.obj;
						onSuccess(str); // 可根据实际情况做处理
						onCountByteLength(str != null ? str.getBytes().length : 0);
					}
				} else {
					onFailure(HttpError.DATA_PARSER_ERROR);
					onCountByteLength(0);
				}
				break;
			case HttpState.CANCEL:
				onCancel();
				break;
			case HttpState.FAILURE:
				handlerFailure(msg);
				break;
			case HttpState.FINISH:
				onFinish();
				break;
		}
	}

	private void handlerFailure(Message msg){
		String errMsg = HttpError.UNKNOWN_HOST_EXCEPTION;
		Object obj = msg.obj;
		if (obj instanceof String) {
			errMsg = (String) obj;
		} else if (obj instanceof UnknownHostException) {
			errMsg = HttpError.UNKNOWN_HOST_EXCEPTION;
		} else if (obj instanceof HttpHostConnectException) {
			errMsg = HttpError.HTTPHOST_CONNECT_EXCEPTION;
		} else if (obj instanceof ConnectTimeoutException) {
			errMsg = HttpError.CONNECT_TIMEOUT_EXCEPTION;
		} else if (obj instanceof SocketException) {
			errMsg = HttpError.CONNECT_TIMEOUT_EXCEPTION;
		}
		onFailure(errMsg);
	}
	
	/**
	 * 是否取消网络请求
	 * @return
	 */
	public boolean isCancelled(){
		return cancelled;
	}

	/**
	 * 设置取消网络请求
	 * @param cancelled
	 */
	public void setCancelled(boolean cancelled){
		this.cancelled = cancelled;
	}

	/**
	 * 加密数据算法
	 * @param text
	 * @return
	 */
	public String encode(String text){
		return text;
	}

	/**
	 * 实现解密数据算法
	 * @param text
	 * @return
	 */
	public String decode(String text){
		return text;
	}

	/**
	 * MD5加密
	 * @param text
	 * @return
	 */
	public String md5(String text){
		return MD5.encode(text);
	}
}
