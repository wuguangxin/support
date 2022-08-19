package com.wuguangxin.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * URL工具类
 *
 * <p>Created by wuguangxin on 14/8/20 </p>
 */
public class UrlUtils {
	private static final String TAG = "UrlUtils";
	private static final String UTF_8 = "utf-8";

	/**
	 * URL编码（默认UTF-8编码格式）
	 * @param data 数据
	 * @return 密文
	 */
	public static String encoder(String data){
		if(data == null){
			return null;
		}
		return encoder(data, UTF_8);
	}

	/**
	 * URL编码
	 * @param data 数据
	 * @param charsetName 编码格式
	 * @return 密文
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
	}
	
	/**
	 * URL解密（默认UTF-8编码格式）
	 * @param data 数据
	 * @return 明文
	 */
	public static String decoder(String data){
		if(data == null){
			return null;
		}
		return decoder(data, UTF_8);
	}
	
	/**
	 * URL解码
	 * @param data 数据
	 * @param charsetName 编码格式
	 * @return 明文
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
	}

	/**
	 * 从url中解析参数
	 * @param url 带参数的地址
	 * @return 返回一个 JSONObject，如果发生异常，则返回一个空的 Map
	 */
	public static Map parserParamsByUrl(String url) {
		Map map = new HashMap<>();
		if(TextUtils.isEmpty(url)){
			return map;
		}

		if(!url.contains("?")){
			return map;
		}

		int start = url.indexOf("?") + 1;
		int end = url.length();

		// 不带参数
		if(start == end){
			return map;
		}

		// 得到参数串
		String paramStr = url.substring(start, end);
		if(TextUtils.isEmpty(paramStr)){
			return map;
		}

		if(!paramStr.contains("&")){
			map = addToJSONObject(map, paramStr);
		} else {
			String[] kvArr = paramStr.split("&");
			for (String aKvArr : kvArr) {
				map = addToJSONObject(map, aKvArr);
			}
		}
		return map;
	}

	/**
	 * 把键值对字符串（如 name=张三 ）解析成键、值，添加到JSONObject中并返回
	 * @param map 存储键值对的JSONObject对象
	 * @param kvStr 如 "name=张三"
	 * @return
	 */
	private static Map addToJSONObject(Map map, String kvStr){
		if(map == null){
			map = new HashMap<>();
		}
		if(kvStr.contains("=")){
			try {
				String[] kvArr = kvStr.split("=");
				if(kvArr.length > 0){
					String key = kvArr[0];
					if(!TextUtils.isEmpty(key)){
						String value = kvArr.length >= 2 ? kvArr[1] : null;
						map.put(key, value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 从URL地址上获取文件名（如果存在的话）
	 * @param url URL字符串地址
	 * @return
	 */
	public static String getFileName(String url) {
		if (TextUtils.isEmpty(url)) return null;

		if (url.contains("/")) {
			String fileName = url.substring(url.lastIndexOf("/") + 1);
			if (!TextUtils.isEmpty(fileName)) {
				return fileName;
			}
		}
		return null;
	}

	/**
	 * 从File上获取文件名（如果存在的话）
	 * @param file 有效存在的文件
	 * @return
	 */
	public static String getFileName(File file) {
		if (file == null || !file.exists() || !file.isFile()) return null;
		return getFileName(file.getPath());
	}

	/**
	 * 讲参数集合转换成url参数串
	 * @param map 参数集合
	 * @return
	 */
	public static String getQueryParameter(Map<String, Object> map) {
		if (map != null && !map.isEmpty()) {
			Uri.Builder builder = new Uri.Builder();
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				builder.appendQueryParameter(key, (String) map.get(key));
			}
			return builder.toString();
		}
		return "";
	}

	/**
	 * 讲参数集合转换成url参数串
	 * @param map 参数集合
	 * @return
	 */
	public static String getJosnString(Map<String, Object> map) {
		if (map != null && !map.isEmpty()) {
			return new Gson().toJson(map);
		}
		return "";
	}

	/**
	 * 获取参数
	 * @param url 地址
	 * @param key 要获取哪个key的值
	 * @return
	 */
	public static String getQueryParameter(String url, String key) {
		if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
			Uri uri = Uri.parse(url);
			return  uri.getQueryParameter(key);
		}
		return url;
	}

	/**
	 * 拼接参数
	 * @param url 地址
	 * @param key
	 * @param value
	 * @return
	 */
	public static String appendQueryParameter(String url, String key, String value) {
		if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
			Uri uri = Uri.parse(url);
			Uri.Builder builder = uri.buildUpon();
			Uri.Builder result = builder.appendQueryParameter(key, value);
			return result.toString();
		}
		return url;
	}

	/**
	 * 讲参数集合拼接到地址后面
	 * @param url url地址
	 * @param map 参数集合
	 * @return
	 */
	public static String appendQueryParameter(String url, Map<String, Object> map) {
		if (TextUtils.isEmpty(url)) return null;
		if (map == null || map.isEmpty()) return url;

		Uri uri = Uri.parse(url);
		Uri.Builder builder = uri.buildUpon();

		Set<String> keySet = map.keySet();
		Object value;
		for (String key : keySet) {
			if (!TextUtils.isEmpty(key)) {
				value = map.get(key);
				builder.appendQueryParameter(key,  value == null ? null : String.valueOf(value));
			}
		}
		return builder.toString();
	}

	/**
	 * 修改参数
	 * @param url url地址
	 * @param key 要修改的key
	 * @param value 要修改的value
	 * @return
	 */
	public static String replace(String url, String key, String value) {
		if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
			url = url.replaceAll("(" + key + "=[^&]*)", key + "=" + value);
		}
		return url;
	}
}
