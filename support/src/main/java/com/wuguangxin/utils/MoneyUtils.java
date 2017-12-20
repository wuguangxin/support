package com.wuguangxin.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 金额格式化工具类
 *
 * <p>Created by wuguangxin on 14/6/2 </p>
 */
public class MoneyUtils{
	/** 是否清除后面的0，比如1.90，清除后是1.9 */
	private static boolean isClearZero = false;

	/**
	 * 是否清除后面的0，比如1.90，清除后是1.9
	 */
	public static void setIsClearZero(boolean isClearZero) {
		MoneyUtils.isClearZero = isClearZero;
	}

	/**
	 * 格式化金额 保留两位小数点，四舍五入，如 1,234.56
	 * @param moneyString String类型数据
	 * @return 金额字符串
	 */
	public static String format(String moneyString){
		if (TextUtils.isEmpty(moneyString) || moneyString.split("\\.").length > 2) {
			return clearZero("0.00");
		}
		return clearZero(format(new BigDecimal(moneyString).doubleValue()));
	}

	/**
	 * 格式化金额 保留两位小数点，四舍五入，如 1,234.56
	 * @param number 金额
	 * @return 金额字符串
	 */
	public static String format(Number number){
		return clearZero(format(number, ""));
	}

	/**
	 * 格式化金额 保留两位小数点，四舍五入，如 1,234.56
	 * @param number 金额
	 * @param unit 附加文字，比如"元"
	 * @return 金额字符串
	 */
	public static String format(Number number, String unit){
		if (number == null) {
			number = 0;
		}
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.applyPattern("##,###.00");
		String formatMoney = decimalFormat.format(number.doubleValue());
		if (formatMoney.startsWith(".")) {
			formatMoney = "0" + formatMoney;
		}
		if (TextUtils.isEmpty(unit)) {
			return clearZero(formatMoney);
		} else {
			return String.format("%s%s", clearZero(formatMoney), unit);
		}
	}

	/**
	 * 格式化数值（如 千分位123,456.78）
	 * @param number 数值
	 * @param bit 保留的小数位数
	 * @param roundingMode 舍入模式 RoundingMode
	 * @return 金额字符串
	 */
	public static String format(Number number, int bit, RoundingMode roundingMode){
		if (number == null) {
			number = 0.0d;
		}
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.applyPattern(getReg(bit));
		if(roundingMode != null){
			decimalFormat.setRoundingMode(roundingMode);
		}
		String formatMoney = decimalFormat.format(number);
		if (formatMoney.startsWith(".")) {
			formatMoney = "0" + formatMoney;
		}
		return clearZero(formatMoney);
	}

	/**
	 * 格式化金额，只保留两位小数点，四舍五入，不用千分位格式化（如123456.78）
	 * @param moneyString String类型数据
	 * @return 金额字符串
	 */
	public static String format2bit(String moneyString){
		if (TextUtils.isEmpty(moneyString) || moneyString.split("\\.").length > 2) {
			return clearZero("0.00");
		}
		return clearZero(format2bit(new BigDecimal(moneyString).doubleValue()));
	}

	/**
	 * 格式化金额，只保留两位小数点，四舍五入，不用千分位格式化（如123456.78）
	 * @param moneyLong long类型
	 * @return 金额字符串
	 */
	public static String format2bit(long moneyLong){
		if (moneyLong == 0) {
			return clearZero("0.00");
		}
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.applyPattern("#####.00");
		String moneyString = decimalFormat.format(moneyLong);
		if (moneyString.startsWith(".")) {
			moneyString = "0" + moneyString;
		}
		return clearZero(moneyString);
	}

	/**
	 * 格式化金额，只保留两位小数点，四舍五入，不用千分位格式化（如123456.78）
	 * @param moneyDouble double类型
	 * @return 金额字符串
	 */
	public static String format2bit(double moneyDouble){
		if (moneyDouble == 0) {
			return clearZero("0.00");
		}
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.applyPattern("#####.00");
		String moneyString = decimalFormat.format(moneyDouble);
		if (moneyString.startsWith(".")) {
			moneyString = "0" + moneyString;
		}
		return clearZero(moneyString);
	}

	/**
	 * 格式化金额，只保留两位小数点，不用千分位格式化（如123456.78）
	 * @param moneyBigDecimal BigDecimal类型
	 * @return 金额字符串
	 */
	public static String format2bit(BigDecimal moneyBigDecimal){
		if (moneyBigDecimal == null || moneyBigDecimal.doubleValue() == 0) {
			return clearZero("0.00");
		}
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.applyPattern("#####.00");
		String formatMoney = decimalFormat.format(moneyBigDecimal.doubleValue());
		if (formatMoney.startsWith(".")) {
			formatMoney = "0" + formatMoney;
		}
		return clearZero(formatMoney);
	}

	/**
	 * 格式化金额，只保留两位小数点，不用千分位格式化（如123456.78）
	 * @param moneyDouble double类型
	 * @return 金额字符串
	 */
	public static String format1bit(double moneyDouble){
		if (moneyDouble == 0) {
			return clearZero("0.0");
		}
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.applyPattern("#####.0");
		String moneyString = decimalFormat.format(moneyDouble);
		if (moneyString.startsWith(".")) {
			moneyString = "0" + moneyString;
		}
		return clearZero(moneyString);
	}

	/**
	 * 格式化金额，千分位格式化（如123,456.78...）
	 * @param money double类型
	 * @param bit 保留的小数位数
	 * @return 金额字符串
	 */
	public static String format(BigDecimal money, int bit){
		return clearZero(getDecimalFormat(bit).format(money));
	}

	/**
	 * 格式化金额，千分位格式化（如123,456.78...）
	 * @param money double类型
	 * @param bit 保留的小数位数
	 * @return 金额字符串
	 */
	public static String format(double money, int bit){
		return clearZero(getDecimalFormat(bit).format(money));
	}

	/**
	 * 格式化金额，千分位格式化（如123,456.78...）
	 * @param money double类型
	 * @param bit 保留的小数位数
	 * @return 金额字符串
	 */
	public static String format(long money, int bit){
		return clearZero(getDecimalFormat(bit).format(money));
	}

	/**
	 * 去小数.00 或 .0（比如1.90，清除后是1.9）
	 * @param numberStr 数值字符串
	 * @return
	 */
	public static String clearZero(String numberStr) {
		if (!isClearZero) {
			return numberStr;
		}
		if(numberStr == null) {
			return "0";
		}
		if(numberStr.indexOf(".") > 0){
			numberStr = numberStr.replaceAll("0+?$", "");//去掉后面无用的零
			numberStr = numberStr.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
		}
		return numberStr;
	}

	/**
	 * 获取DecimalFormat 实例，并设置保留的小数位数
	 * @param bit 保留的小数位数
	 * @return DecimalFormat
	 */
	private static DecimalFormat getDecimalFormat(int bit){
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.applyPattern(getReg(bit));
		return decimalFormat;
	}

	/**
	 * 将数字型货币转换为中文型货币
	 * @param number 数字
	 * @return 金额字符串
	 */
	public static String formatCN(Number number){
		if(number.doubleValue() == 0){
			return "零元整";
		}
		char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
		String unit = "仟佰拾兆仟佰拾亿仟佰拾万仟佰拾元角分";
		// 注意 如果是0.01，这里会处理为 .01，千万别格式化为 0.01，即reg不能写为 "#.00"
		String s = new DecimalFormat("#.00").format(number);
		s = s.replaceAll("\\.", "");
		int l = unit.length();
		StringBuffer sb = new StringBuffer(unit);
		for (int i = s.length() - 1; i >= 0; i--){
			sb = sb.insert(l - s.length() + i, digit[(s.charAt(i) - 0x30)]);
		}
		s = sb.substring(l - s.length(), l + s.length());
		s = s.replaceAll("零[拾佰仟]", "零").replaceAll("零{2,}", "零").replaceAll("零([兆万元])", "$1").replaceAll("零[角分]", "");
		if (s.endsWith("角")){
			s += "零分";
		}
		if (!s.contains("角") && !s.contains("分") && s.contains("元")){
			s += "整";
		}
		if (s.contains("分") && !s.contains("整") && !s.contains("角")){
			s = s.replace("元", "元零");
		}
		return s;
	}

	/*
	BigDecimal bd = new BigDecimal(123456789);
	System.out.println(formatString(",###,###", bd)); 	//out: 123,456,789
	System.out.println(formatString("##,####,###", bd));	//out: 123,456,789
	System.out.println(formatString("######,###", bd));	//out: 123,456,789
	System.out.println(formatString("#,##,###,###", bd)); //out: 123,456,789
	System.out.println(formatString(",###,###.00", bd));	//out: 123,456,789.00
	System.out.println(formatString(",###,##0.00", bd));	//out: 123,456,789.00
	BigDecimal bd1 = new BigDecimal(0);
	System.out.println(formatString(",###,###", bd1)); 	//out: 0
	System.out.println(formatString(",###,###.00", bd1)); //out: .00
	System.out.println(formatString(",###,##0.00", bd1)); //out: 0.00
	*/

	/**
	 * @param bit 小数位数
	 * @return 通配符
	 */
	private static String getReg(int bit){
		switch (bit) {
		case 0: return ",###,##0";
		case 1: return ",###,##0.0";
		case 2: return ",###,##0.00";
		case 3: return ",###,##0.000";
		case 4: return ",###,##0.0000";
		default: return ",###,##0.00";
		}
	}
}