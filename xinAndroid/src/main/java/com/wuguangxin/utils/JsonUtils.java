package com.wuguangxin.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonUtils {
	
	
	/**
	 * 字符转到json对象
	 * @param inReader
	 * @return
	 */
	public static JSONObject parseStr2Json(String inReader){
		JSONTokener jsonParser = new JSONTokener(inReader);
		JSONObject jobject;
		try {
			jobject = (JSONObject) jsonParser.nextValue();
			return jobject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 把标准json字符串转换为json对象JSONObject
	 * @param stringJson
	 * @return
	 */
	public static JSONObject toJson(String stringJson){
		try {
			return new JSONObject(stringJson);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
