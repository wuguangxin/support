package com.wuguangxin.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 城市查询
 *
 * <p>Created by wuguangxin on 15/1/9 </p>
 */
public class CityDao {
	private MyHelper helper;
	
	public CityDao(Context context) {
		helper = new MyHelper(context);
		helper.openDatabase();
	}
	
	/**
	 * 获取省份信息集合
	 * @return 返回 名称 编码 格式集合
	 */
	public Map<String, String> getProvinceMap() {
		SQLiteDatabase db = helper.getDatabase();
		if(db != null){
			// 应该使用query函数
			String sql = "SELECT * FROM city a WHERE SUBSTR(a.item_code,3,4) == '0000'"; // 所有后四位为“0000”的
			Cursor cursor = db.rawQuery(sql, null);
			return getNameCodeMap(db, cursor); 
		}
		return null;
	}
	
	/**
	 * 获取省份名称列表
	 * @return 列表
	 */
	public List<String> getProvinceNameList() {
		SQLiteDatabase db = helper.getDatabase();
		if(db != null){
			// 应该使用query函数
			String sql = "SELECT * FROM city a WHERE SUBSTR(a.item_code,3,4) == '0000'"; // 所有后四位为“0000”的
			Cursor cursor = db.rawQuery(sql, null);
			return getNameList(db, cursor);
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 根据省份编码获取城市信息集合
	 * @param provinceCode 省份编码
	 * @return 返回 名称 编码 格式集合
	 */
	public Map<String, String> getCityMap(String provinceCode) {
		SQLiteDatabase db = helper.getDatabase();
		if(db != null){
			String shengCode = provinceCode.substring(0, 2);
			String sql = "SELECT * FROM city a WHERE SUBSTR(a.item_code,1,2) = '"+shengCode+"' AND a.item_code != '"+provinceCode+"' AND SUBSTR(a.item_code,5,2) = '00'";
			Cursor cursor = db.rawQuery(sql, null);
			return getNameCodeMap(db, cursor); 
		}
		return null;
	}
	
	/**
	 * 根据省份编码获取城市名称列表
	 * @param provinceCode 省份编码
	 * @return List
	 */
	public List<String> getCityNameList(String provinceCode) {
		SQLiteDatabase db = helper.getDatabase();
		if(db != null){
			String shengCode = provinceCode.substring(0, 2);
			String sql = "SELECT * FROM city a WHERE SUBSTR(a.item_code,1,2) = '"+shengCode+"' AND a.item_code != '"+provinceCode+"' AND SUBSTR(a.item_code,5,2) = '00'";
			Cursor cursor = db.rawQuery(sql, null);
			return getNameList(db, cursor);
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 根据城市简码获取区县列表
	 * @param cityCode 城市编码
	 * @return 返回 名称 编码 格式集合
	 */
	public Map<String, String> getCountyList(final String cityCode) {
		SQLiteDatabase db = helper.getDatabase();
		if(db != null){
			String cityCode2 = cityCode.substring(2, 4);
			String sql = "SELECT * FROM city a WHERE SUBSTR(a.item_code,3,2) = '"+cityCode2+"' order by id DESC";
			Cursor cursor = db.rawQuery(sql, null);
			return getNameCodeMap(db, cursor); 
		}
		return null;
	}

	/**
	 * 获取到名称-简码的键值对集合
	 * @param db
	 * @param cursor
	 * @return
	 */
	private Map<String, String> getNameCodeMap(SQLiteDatabase db, Cursor cursor){
		Map<String, String> map = new LinkedHashMap<String, String>(); 
		if(cursor != null){
			for (int i = 0; i < cursor.getCount(); i++) {
				if (cursor.moveToNext()) {
					map.put(cursor.getString(1), cursor.getString(2));
				}
			}
			cursor.close();
		}
//		db.close();
		return map;
	}
	
	/**
	 * 获取到简码和名称的键值对集合
	 * @param db
	 * @param cursor
	 * @return
	 */
	private List<String> getNameList(SQLiteDatabase db, Cursor cursor){
		List<String> list = new LinkedList<String>();
		if(cursor != null){
			for (int i = 0; i < cursor.getCount(); i++) {
				if (cursor.moveToNext()) {
					list.add(cursor.getString(1));
				}
			}
			cursor.close();
		}
//		db.close();
		return list;
	}
}
