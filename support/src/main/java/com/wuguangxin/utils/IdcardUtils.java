package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 身份证工具类 注：公民身份证号码按照 GB11643—1999《公民身份证号码》国家标准编制，
 * 由18位数字组成： 前6位为行政区划分代码， 第7位至14位为出生日期码， 第15位至17位为顺序码， 第18位为校验码。
 */
@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class IdcardUtils{
	/** 中国公民身份证号码最小长度。 */
	private static final int CHINA_ID_MIN_LENGTH = 15;
	/** 中国公民身份证号码最大长度。 */
	private static final int CHINA_ID_MAX_LENGTH = 18;
	/** 省、直辖市代码表 */
	@SuppressWarnings("unused")
	private static final String cityCode[] = {
	    "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"
	};
	/** 每位加权因子 */
	private static final int weight[] = {
	    7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
	};
	/** 第18位校检码 */
	private static final String verifyCode[] = {
	    "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"
	};
	/** 最低年限 */
	private static final int MIN = 1930;
	private static Map<String, String> cityCodes = new HashMap<String, String>();
	/** 台湾身份首字母对应数字 */
	private static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();
	/** 香港身份首字母对应数字 */
	private static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();
	static {
		cityCodes.put("11", "北京");
		cityCodes.put("12", "天津");
		cityCodes.put("13", "河北");
		cityCodes.put("14", "山西");
		cityCodes.put("15", "内蒙古");
		cityCodes.put("21", "辽宁");
		cityCodes.put("22", "吉林");
		cityCodes.put("23", "黑龙江");
		cityCodes.put("31", "上海");
		cityCodes.put("32", "江苏");
		cityCodes.put("33", "浙江");
		cityCodes.put("34", "安徽");
		cityCodes.put("35", "福建");
		cityCodes.put("36", "江西");
		cityCodes.put("37", "山东");
		cityCodes.put("41", "河南");
		cityCodes.put("42", "湖北");
		cityCodes.put("43", "湖南");
		cityCodes.put("44", "广东");
		cityCodes.put("45", "广西");
		cityCodes.put("46", "海南");
		cityCodes.put("50", "重庆");
		cityCodes.put("51", "四川");
		cityCodes.put("52", "贵州");
		cityCodes.put("53", "云南");
		cityCodes.put("54", "西藏");
		cityCodes.put("61", "陕西");
		cityCodes.put("62", "甘肃");
		cityCodes.put("63", "青海");
		cityCodes.put("64", "宁夏");
		cityCodes.put("65", "新疆");
		cityCodes.put("71", "台湾");
		cityCodes.put("81", "香港");
		cityCodes.put("82", "澳门");
		cityCodes.put("91", "国外");
		twFirstCode.put("A", 10);
		twFirstCode.put("B", 11);
		twFirstCode.put("C", 12);
		twFirstCode.put("D", 13);
		twFirstCode.put("E", 14);
		twFirstCode.put("F", 15);
		twFirstCode.put("G", 16);
		twFirstCode.put("H", 17);
		twFirstCode.put("J", 18);
		twFirstCode.put("K", 19);
		twFirstCode.put("L", 20);
		twFirstCode.put("M", 21);
		twFirstCode.put("N", 22);
		twFirstCode.put("P", 23);
		twFirstCode.put("Q", 24);
		twFirstCode.put("R", 25);
		twFirstCode.put("S", 26);
		twFirstCode.put("T", 27);
		twFirstCode.put("U", 28);
		twFirstCode.put("V", 29);
		twFirstCode.put("X", 30);
		twFirstCode.put("Y", 31);
		twFirstCode.put("W", 32);
		twFirstCode.put("Z", 33);
		twFirstCode.put("I", 34);
		twFirstCode.put("O", 35);
		hkFirstCode.put("A", 1);
		hkFirstCode.put("B", 2);
		hkFirstCode.put("C", 3);
		hkFirstCode.put("R", 18);
		hkFirstCode.put("U", 21);
		hkFirstCode.put("Z", 26);
		hkFirstCode.put("X", 24);
		hkFirstCode.put("W", 23);
		hkFirstCode.put("O", 15);
		hkFirstCode.put("N", 14);
	}

	/**
	 * 将15位身份证号码转换为18位
	 * 
	 * @param idCard 15位身份编码
	 * @return 18位身份编码
	 */
	@SuppressLint("SimpleDateFormat")
	public static String conver15CardTo18(String idCard){
		String idCard18 = "";
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return null;
		}
		if (isNum(idCard)) {
			// 获取出生年月日
			String birthday = idCard.substring(6, 12);
			Date birthDate = null;
			try {
				birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			if (birthDate != null)
				cal.setTime(birthDate);
			// 获取出生年(完全表现形式,如：2010)
			String sYear = String.valueOf(cal.get(Calendar.YEAR));
			idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
			// 转换字符数组
			char[] cArr = idCard18.toCharArray();
			if (cArr != null) {
				int[] iCard = converCharToInt(cArr);
				int iSum17 = getPowerSum(iCard);
				// 获取校验位
				String sVal = getCheckCode18(String.valueOf(iSum17));
				if (sVal.length() > 0) {
					idCard18 += sVal;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
		return idCard18;
	}

	/**
	 * 验证18位身份编码是否合法
	 * 
	 * @param idCard 身份编码
	 * @return 是否合法
	 */
	public static boolean validateIdCard18(String idCard){
//		Utils.printLogi(TAG, "身份证为：" + idCard);
		boolean bTrue = false;
		if (idCard.length() == CHINA_ID_MAX_LENGTH) {
			// 前17位
			String code17 = idCard.substring(0, 17);
//			Utils.printLogi(TAG, "前17位：" + code17);
			// 第18位
//			String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
//			Utils.printLogi(TAG, "第18位：" + code18);
			if (isNum(code17)) { // 如果前17位为数字
				char[] cArr = code17.toCharArray(); // 把前17位转为数组
				if (cArr != null) {
//					int[] iCard = converCharToInt(cArr);
					// int iSum17 = getPowerSum(iCard); //
					// 将身份证的每位和对应位的加权因子相乘之后，再得到和值
					// String val = getCheckCode18(iSum17); // 获取校验位
//					String val = getCheckCode18(code17);
//					Utils.printLogi(TAG, "校验位 = " + val);
					bTrue = true;
					// 比较校验位
					// if (val.length() > 0) {
					// if (val.equalsIgnoreCase(code18)) {
					// bTrue = true;
					// }
					// }
				}
			}
		}
		return bTrue;
	}

	/**
	 *
	 * @param name
     */
	public static boolean checkNameCN(String name) {
		String reg = "([\u4E00-\u9FA5]{2,6})";
		return Pattern.matches(reg, name);
	}

	/**
	 *
	 * @param name
	 */
	public static boolean checkNameEN(String name) {
		String reg = "([a-zA-Z]{3,10})";
		return Pattern.matches(reg, name);
	}

	/**
	 * 验证身份证是否合法
	 * @param idCard
	 */
	public static boolean checkIdCard18(String idCard){
		if (idCard == null || idCard == "") return false;
		String card = idCard.trim();
		return validateIdCard18W(card);
	}

	/**
	 * 验证18位身份证号码
	 * @param cardNo
	 * @return Boolean
	 */
	public static boolean validateIdCard18W(String cardNo){
		if(cardNo.length() == 18){
		 int sum = 0;  
		int[] intArr = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };  
		   for (int i = 0; i < intArr.length; i++) {  
	            // 2.将这17位数字和系数相乘的结果相加。  
	            sum += Character.digit(cardNo.charAt(i), 10) * intArr[i];  
	        }  
		   Logger.i("sum = "+sum);
		   int mod = sum % 11;  
		   Logger.i("mod = "+mod);
		   int[] intArr2 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };  
	       int[] intArr3 = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 }; 
	       String matchDigit = "";  
	       for (int i = 0; i < intArr2.length; i++) {  
	            int j = intArr2[i];  
	            if (j == mod) {  
	                matchDigit = String.valueOf(intArr3[i]);  
	                if (intArr3[i] > 57) {  
	                    matchDigit = String.valueOf((char) intArr3[i]);  
	                }  
	            }  
	        } 
	       if (matchDigit.equals(cardNo.substring(cardNo.length() - 1).toUpperCase())) {  
	            Logger.i("ID Card Verify Sucsess!");
	            return true;
	        } else {  
	            Logger.i("ID Card Verify Faild!");
	            return false;
	        }  
	        
	        
		}
		return false;
	}
	
	/**
	 * 验证15位身份编码是否合法
	 * 
	 * @param idCard 身份编码
	 * @return 是否合法
	 */
	public static boolean validateIdCard15(String idCard){
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return false;
		}
		if (isNum(idCard)) {
			String proCode = idCard.substring(0, 2);
			if (cityCodes.get(proCode) == null) {
				return false;
			}
			String birthCode = idCard.substring(6, 12);
			Date birthDate = null;
			try {
				birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			if (birthDate != null)
				cal.setTime(birthDate);
			if (!valiDate(cal.get(Calendar.YEAR), Integer.valueOf(birthCode.substring(2, 4)), Integer.valueOf(birthCode.substring(4, 6)))) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 验证10位身份编码是否合法
	 * 
	 * @param idCard 身份编码
	 * @return 身份证信息数组 <p> [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null </p>
	 */
	public static String[] validateIdCard10(String idCard){
		String[] info = new String[3];
		String card = idCard.replaceAll("[\\(|\\)]", "");
		if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
			return null;
		}
		if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
			info[0] = "台湾";
			String char2 = idCard.substring(1, 2);
			if (char2.equals("1")) {
				info[1] = "M";
			} else if (char2.equals("2")) {
				info[1] = "F";
			} else {
				info[1] = "N";
				info[2] = "false";
				return info;
			}
			info[2] = validateTWCard(idCard) ? "true" : "false";
		} else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
			info[0] = "澳门";
			info[1] = "N";
			// TODO
		} else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
			info[0] = "香港";
			info[1] = "N";
			info[2] = validateHKCard(idCard) ? "true" : "false";
		} else {
			return null;
		}
		return info;
	}

	/**
	 * 验证台湾身份证号码
	 * 
	 * @param idCard 身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean validateTWCard(String idCard){
		String start = idCard.substring(0, 1);
		String mid = idCard.substring(1, 9);
		String end = idCard.substring(9, 10);
		Integer iStart = twFirstCode.get(start);
		Integer sum = iStart / 10 + (iStart % 10) * 9;
		char[] chars = mid.toCharArray();
		Integer iflag = 8;
		for (char c: chars) {
			sum = sum + Integer.valueOf(c + "") * iflag;
			iflag--;
		}
		return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end) ? true : false;
	}

	/**
	 * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查) <p> 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35 最后一位校验码为0-9的数字加上字符"A"，"A"代表10 </p> <p> 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效 </p>
	 * 
	 * @param idCard 身份证号码
	 * @return 验证码是否符合
	 */
	@SuppressLint("DefaultLocale")
	public static boolean validateHKCard(String idCard){
		String card = idCard.replaceAll("[\\(|\\)]", "");
		Integer sum = 0;
		if (card.length() == 9) {
			sum = (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 9 + (Integer.valueOf(card.substring(1, 2).toUpperCase().toCharArray()[0]) - 55) * 8;
			card = card.substring(1, 9);
		} else {
			sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 8;
		}
		String mid = card.substring(1, 7);
		String end = card.substring(7, 8);
		char[] chars = mid.toCharArray();
		Integer iflag = 7;
		for (char c: chars) {
			sum = sum + Integer.valueOf(c + "") * iflag;
			iflag--;
		}
		if (end.toUpperCase().equals("A")) {
			sum = sum + 10;
		} else {
			sum = sum + Integer.valueOf(end);
		}
		return (sum % 11 == 0) ? true : false;
	}

	/**
	 * 将字符数组转换成数字数组
	 * 
	 * @param ca 字符数组
	 * @return 数字数组
	 */
	public static int[] converCharToInt(char[] ca){
		int len = ca.length;
		int[] iArr = new int[len];
		try {
			for (int i = 0; i < len; i++) {
				iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return iArr;
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 * 
	 * @param iArr
	 * @return 身份证编码。
	 */
	public static int getPowerSum(int[] iArr){
		int iSum = 0;
		if (weight.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				for (int j = 0; j < weight.length; j++) {
					if (i == j) {
						iSum = iSum + iArr[i] * weight[j];
					}
				}
			}
		}
		return iSum;
	}

	/**
	 * 将power和值与11取模获得余数进行校验码判断
	 * 
	 * @param id17
	 * @return 校验位
	 */
	public static String getCheckCode18(String id17){
		// **************************************************
//		Utils.printLogi(TAG, "id17 = " + id17);
		int sum = 0;
		int mode = 0;
		for (int i = 0; i < id17.length(); i++) {
			sum = sum + Integer.parseInt(String.valueOf(id17.charAt(i))) * weight[i];
		}
//		Utils.printLogi(TAG, "sum = " + sum);
		mode = sum % 11;
//		Utils.printLogi(TAG, "mode = " + mode);
		return verifyCode[mode];
		// **************************************************
		/*
		 * String sCode = ""; switch (iSum % 11) { case 10: sCode = "2"; break; case 9: sCode = "3"; break; case 8: sCode = "4"; break; case 7: sCode = "5"; break; case 6: sCode = "6"; break; case 5: sCode = "7"; break; case 4: sCode = "8"; break; case 3: sCode = "9"; break; case 2: sCode = "X"; break; case 1: sCode = "0"; break; case 0: sCode = "1"; break; } return sCode;
		 */
	}

	/**
	 * 根据身份编号获取年龄
	 * 
	 * @param idCard 身份编号
	 * @return 年龄
	 */
	public static int getAgeByIdCard(String idCard){
		int iAge = 0;
		if (idCard.length() == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		String year = idCard.substring(6, 10);
		Calendar cal = Calendar.getInstance();
		int iCurrYear = cal.get(Calendar.YEAR);
		iAge = iCurrYear - Integer.valueOf(year);
		return iAge;
	}

	/**
	 * 根据身份编号获取生日
	 * 
	 * @param idCard 身份编号
	 * @return 生日(yyyyMMdd)
	 */
	public static String getBirthByIdCard(String idCard){
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return idCard.substring(6, 14);
	}

	/**
	 * 根据身份编号获取生日年
	 * 
	 * @param idCard 身份编号
	 * @return 生日(yyyy)
	 */
	public static Short getYearByIdCard(String idCard){
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(6, 10));
	}

	/**
	 * 根据身份编号获取生日月
	 * 
	 * @param idCard 身份编号
	 * @return 生日(MM)
	 */
	public static Short getMonthByIdCard(String idCard){
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(10, 12));
	}

	/**
	 * 根据身份编号获取生日天
	 * 
	 * @param idCard 身份编号
	 * @return 生日(dd)
	 */
	public static Short getDateByIdCard(String idCard){
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(12, 14));
	}

	/**
	 * 根据身份编号获取性别
	 * 
	 * @param idCard 身份编号
	 * @return 性别(M-男，F-女，N-未知)
	 */
	public static String getGenderByIdCard(String idCard){
		String sGender = "N";
		if (idCard.length() == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		String sCardNum = idCard.substring(16, 17);
		if (Integer.parseInt(sCardNum) % 2 != 0) {
			sGender = "男";
		} else {
			sGender = "女";
		}
		return sGender;
	}

	/**
	 * 根据身份编号获取户籍省份
	 * 
	 * @param idCard 身份编码
	 * @return 省级编码。
	 */
	public static String getProvinceByIdCard(String idCard){
		int len = idCard.length();
		String sProvince = null;
		String sProvinNum = "";
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			sProvinNum = idCard.substring(0, 2);
		}
		sProvince = cityCodes.get(sProvinNum);
		return sProvince;
	}

	/**
	 * 数字验证
	 * 
	 * @param val
	 * @return 提取的数字。
	 */
	public static boolean isNum(String val){
		return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
	}

	/**
	 * 验证小于当前日期 是否有效
	 * 
	 * @param iYear 待验证日期(年)
	 * @param iMonth 待验证日期(月 1-12)
	 * @param iDate 待验证日期(日)
	 * @return 是否有效
	 */
	public static boolean valiDate(int iYear, int iMonth, int iDate){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int datePerMonth;
		if (iYear < MIN || iYear >= year) {
			return false;
		}
		if (iMonth < 1 || iMonth > 12) {
			return false;
		}
		switch (iMonth) {
			case 4:
			case 6:
			case 9:
			case 11:
				datePerMonth = 30;
				break;
			case 2:
				boolean dm = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0)) && (iYear > MIN && iYear < year);
				datePerMonth = dm ? 29 : 28;
				break;
			default:
				datePerMonth = 31;
		}
		return (iDate >= 1) && (iDate <= datePerMonth);
	}
	
	/**
	 * 隐藏身份证中间的生日 (110105 19900101 3756)
	 * @param idCardNumber
	 * @return 如 110105 19900101 3756 返回 110105 ******** 3756
	 */
	public static String formatHint(String idCardNumber){
		if(TextUtils.isEmpty(idCardNumber)){
			return idCardNumber;
		}
		if(idCardNumber.length() == 15){
			return String.format("%s ****** %s", idCardNumber.substring(0, 6), idCardNumber.substring(12, idCardNumber.length()));
		} else if (idCardNumber.length() == 18){
			return String.format("%s ******** %s", idCardNumber.substring(0, 6), idCardNumber.substring(14, idCardNumber.length()));
		} else {
			return idCardNumber;
		}
	}
}
