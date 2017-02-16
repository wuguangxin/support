package com.wuguangxin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL编码解码工具类
 *
 * @author wuguangxin
 * @date: 2014-8-20 下午1:17:12
 */
public class UrlUtils{
	private static final String TAG = "UrlUtils";
	private static final String UTF_8 = "utf-8";

	/**
	 * URL编码（默认UTF-8编码格式）
	 * @param data
	 * @return
	 */
	public static String encoder(String data){
		if(data == null){
			return null;
		}
		return encoder(data, UTF_8);
	};

	/**
	 * URL编码
	 * @param data 数据
	 * @param charsetName 编码格式
	 * @return
	 */
	public static String encoder(String data, String charsetName){
		if(data == null){
			return null;
		}
		try {
			String encodePayPassword = URLEncoder.encode(data, charsetName);
			return encodePayPassword;
		} catch (UnsupportedEncodingException e) {
			Logger.i(TAG, "URL编码异常：" + e.toString());
			return null;
		}
	};
	
	/**
	 * URL解密（默认UTF-8编码格式）
	 * @param data 数据
	 * @return
	 */
	public static String decoder(String data){
		if(data == null){
			return null;
		}
		return decoder(data, UTF_8);
	};
	
	/**
	 * URL解码
	 * @param data 数据
	 * @param charsetName 编码格式
	 * @return
	 */
	public static String decoder(String data, String charsetName){
		if(data == null){
			return null;
		}
		if(charsetName == null){
			charsetName = UTF_8;
		}
		try {
			String decodeData = URLDecoder.decode(data, charsetName);
			return decodeData;
		} catch (UnsupportedEncodingException e) {
			Logger.i(TAG, "URL解码异常：" + e.toString());
			return null;
		}
	};
}
