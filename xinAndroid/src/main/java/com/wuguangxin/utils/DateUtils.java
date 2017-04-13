package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.net.URL;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间工具类
 *
 * @author wuguangxin
 * @date: 2015-3-31 下午3:27:11
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils{
	private static final String DEFAULT_URL = "http://www.baidu.com"; // 默认时间来源
	private static final SimpleDateFormat formatLongDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
	private static final long nd = 1000 * 60 * 60 * 24;		//一天的毫秒数
	private static final long nh = 1000 * 60 * 60;			//一小时的毫秒数
	private static final long nm = 1000 * 60;				//一分钟的毫秒数
	private static final long ns = 1000;					//一秒钟的毫秒数
	private static StringBuilder timeDiffBuilder;

	/**
	 * 获取系统当前长日期
	 * @return 如 2015-01-01 10:20:30
	 */
	public static Date getDate(){
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 根据时间戳转换为长日期
	 * @param timestamp 时间戳
	 * @return 如 2015-01-01 10:20:30
	 */
	public static Date getDate(long timestamp){
		return new Date(timestamp);
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * @param dataString 如2017-01-01
	 * @return
	 */
	public static Date getDateShort(String dataString) {
		ParsePosition pos = new ParsePosition(0);
		return formatDate.parse(dataString, pos);
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * @param dataString 如2017-01-01 10:20:30
	 * @return
	 */
	public static Date getDateLong(String dataString) {
		ParsePosition pos = new ParsePosition(0);
		return formatLongDate.parse(dataString, pos);
	}

	/**
	 * 将短时间格式字符串转换为时间戳
	 * @param dataString yyyy-MM-dd 如 2017-01-01
	 * @return
	 */
	public static long formatTimestamp(String dataString) {
		try {
			Date d = formatDate.parse(dataString);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 将长时间格式字符串转换为时间戳
	 * @param dataString yyyy-MM-dd HH:mm:ss 如 2017-01-01 10:20:30
	 * @return
	 */
	public static long formatTimestampLong(String dataString) {
		try {
			Date d = formatLongDate.parse(dataString);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}


	/**
	 * 格式化为长日期
	 * @param timestamp String类型时间戳
	 * @return 如 2014-01-01 15:20:58
	 */
	public static String formatLongDate(String timestamp){
		if (TextUtils.isEmpty(timestamp)) {
			return null;
		}
		return formatLongDate.format(new Date(Long.parseLong(timestamp)));
	}

	/**
	 * 格式化为长日期加时间_不带秒
	 * @param timestamp long类型时间戳
	 * @return 如 2014-01-01 15:20
	 */
	public static String format_yyyyMMddHHmm(long timestamp){
		if (timestamp == -1) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timestamp));
	}

	/**
	 * 格式化时间戳为短日期格式，以"-"分割
	 * @param timestamp String类型时间戳
	 * @return 如 2014-01-01
	 */
	public static String formatDateShortLine(String timestamp){
		if (TextUtils.isEmpty(timestamp)) {
			return null;
		}
		return formatDate.format(new Date(Long.parseLong(timestamp)));
	}

	/**
	 * 格式化时间戳为短日期格式，以"-"分割
	 * @param timestamp long类型时间戳
	 * @return 如 2014-01-01
	 */
	public static String formatDateShortLine(long timestamp){
		if (timestamp <= 0) {
			return null;
		}
		return formatDate.format(new Date(timestamp));
	}

	/**
	 * 格式化时间戳为长日期格式
	 * @param timestamp long类型时间戳
	 * @return 如 2014-01-01 12:30:50
	 */
	public static String formatDateLong(long timestamp){
		if (timestamp <= 0) {
			return null;
		}
		return formatLongDate.format(new Date(timestamp));
	}

	/**
	 * 格式化时间戳为长日期格式
	 * @param date Date类型时间
	 * @return 如 2014-01-01 12:30:50
	 */
	public static String formatDateLong(Date date){
		if (date == null) {
			return null;
		}
		return formatLongDate.format(date);
	}

	/**
	 * 格式化为短日期，如 14.05.13 把20140101/2014-01-01这样的日期字符串格式化为14.01.01
	 */
	public static String formatShortDate(String dateString){
		if (TextUtils.isEmpty(dateString)) {
			return null;
		}
		String shortDateString = dateString.replaceAll("-", ".").replaceAll("/", ".").replaceFirst("20", "");
		return shortDateString;
	}

	/**
	 * 格式化日期
	 * @param timestamp 时间戳
	 * @param format 格式化样式 yyyy-MM-dd HH:mm:ss
     * @return
     */
	public static String format(long timestamp, String format){
		if (timestamp <= 0) {
			return null;
		}
		if (!TextUtils.isEmpty(format)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(timestamp);
		}
		return formatDateShortLine(timestamp);
	}

	/**
	 * 获取当前系统长日期字符串，不带分隔符
	 * @return 如 20140101102055
	 */
	public static String getLongDateString(){
		return getLongDate().replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "");
	}

	/**
	 * 获取当前系统完整日期
	 * @return 如 2014-01-01 10:20:55
	 */
	public static String getLongDate(){
		return formatLongDate.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 获取当前系统短日期，如 2014-01-01
	 */
	public static String getShortDate(){
		return formatDate.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 获取当前系统时间，如 10:20:55
	 */
	public static String getTime(){
		return formatTime.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 获取网络长日期
	 * @return 如 2015-01-10 20:20:10
	 */
	public static String getDateFromNet(){
		return formatDateLong(getTimestampFromNet(null));
	}

	/**
	 * 获取网络时间戳（默认时间源 http://www.beijing-time.org）
	 * @return
	 */
	public static long getTimestampFromNet(){
		return getTimestampFromNet(null);
	}

	/**
	 * String类型长日期转换为long类型时间戳(有BUG，未解决)
	 * @param dateString String类型长日期
	 * @return
	 */
	public static long getTimestamp(String dateString){
		if (TextUtils.isEmpty(dateString)) {
			return -1;
		}
		try {
			return formatLongDate.parse(dateString).getTime();
		} catch (ParseException e) {
			return -1;
		}
	}

	/**
	 * 指定服务器地址获取网络时间戳
	 * @param webUrl 任何一个网站域名或网络IP，如 http://www.ephwealth.com
	 * @return
	 */
	public static long getTimestampFromNet(String webUrl){
		if (TextUtils.isEmpty(webUrl)) {
			webUrl = DEFAULT_URL;
		}
		long bjTimestamp = 0;
		try {
			java.net.URL url = new URL(webUrl);//取得资源对象
			java.net.URLConnection conn = url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(2000);
			conn.connect();
			bjTimestamp = conn.getDate(); //取得网站日期时间
		} catch (Exception e) {
			Logger.e("无法获取北京时间，将使用手机当前时间！！！！");
		}
		return bjTimestamp;
	}

	/**
	 * 获取两个日期的间隔天数（精确到天，会把传入的两个时间戳去掉时分秒）
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 */
	public static int dateDiff(long startTime, long endTime) {
		String endStr = formatDateShortLine(endTime); // 把时分秒抹去
		String startStr = formatDateShortLine(startTime); // 把时分秒抹去
		long dayDiff = formatTimestamp(endStr) - formatTimestamp(startStr); // 时间差
		return (int)(dayDiff / 86400000L); // 除一天的毫秒数
	}

	/**
	 * 获取两个日期的间隔(天时分秒)
	 * @param startTime 开始时间 时间戳
	 * @param endTime 结束时间 时间戳（用当前时间）
	 * @param isFormat 如果为true, 返回 00天11时22分33秒，如果为false，则为00:11:22:33
	 * @return 返回如: 01天22时33分51秒 或 01:22:33:51
	 */
	public static String dateDiff(long startTime, long endTime, boolean isFormat){
		long diff = new java.sql.Date(endTime).getTime() - new java.sql.Date(startTime).getTime();
		return dateDiff(diff, isFormat);
	}

	/**
	 * 将指定毫秒值格式化为(00天00时00分00秒)
	 * @param countTime 总时间 时间戳
	 * @param isFormat 如果为true, 返回 00天11时22分33秒，如果为false，则为00:11:22:33
	 * @return 返回如: 01天22时33分51秒 或 01:22:33:51
	 */
	public static String dateDiff(long countTime, boolean isFormat){
		StringBuilder dateBuilder = new StringBuilder();
		long diff = new java.sql.Date(countTime).getTime();
		long day = diff / nd;						//天
		long hour = diff % nd / nh;					//时
		long min = diff % nd % nh / nm;				//分
		long sec = diff % nd % nh % nm / ns;		//秒
		if (day + hour + min + sec < 0) {
			return null;
		}
		String dS = day < 10 ? "0" + day : "" + day;
		String hS = hour < 10 ? "0" + hour : "" + hour;
		String mS = min < 10 ? "0" + min : "" + min;
		String sS = sec < 10 ? "0" + sec : "" + sec;
		if (isFormat) {
			dateBuilder.append(dS).append("天");
			dateBuilder.append(hS).append("时");
			dateBuilder.append(mS).append("分");
			dateBuilder.append(sS).append("秒");
		} else {
			dateBuilder.append(dS).append(":");
			dateBuilder.append(hS).append(":");
			dateBuilder.append(mS).append(":");
			dateBuilder.append(sS).append(":");
		}
		return dateBuilder.toString();
	}

	/**
	 * 格式化指定时间毫秒数为(时分秒)格式
	 * @param countTime 总时间 时间戳
	 * @param isFormat 如果为true, 返回 11时22分33秒，如果为false，则为11:22:33
	 * @param isFormat 是否是格式化为（00时00分00秒）
	 * @return 返回如: 22时33分51秒 或 22:33:51
	 */
	public static String formatHHmmss(long countTime, boolean isFormat){
		StringBuilder dateBuilder = new StringBuilder();
		long diff = new java.sql.Date(countTime).getTime();
		long hour = diff % nd / nh + diff / nd * 24;//时
		long min = diff % nd % nh / nm;				//分
		long sec = diff % nd % nh % nm / ns;		//秒
		if (hour + min + sec < 0) {
			return null;
		}
		String hS = hour < 10 ? "0" + hour : "" + hour;
		String mS = min < 10 ? "0" + min : "" + min;
		String sS = sec < 10 ? "0" + sec : "" + sec;
		if (isFormat) {
			dateBuilder.append(hS).append("时");
			dateBuilder.append(mS).append("分");
			dateBuilder.append(sS).append("秒");
		} else {
			dateBuilder.append(hS).append(":");
			dateBuilder.append(mS).append(":");
			dateBuilder.append(sS).append(":");
		}
		return dateBuilder.toString();
	}


	/**
	 * 格式化指定时间毫秒数为(时分)格式
	 * @param countTime 总时间 时间戳
	 * @param isFormat 如果为true, 返回 11时22分33秒，如果为false，则为11:22
	 * @param isFormat 是否是格式化为（00时00分）
	 * @return 返回如: 22时33分 或 22:33
	 */
	public static String formatHHmm(long countTime, boolean isFormat){
		StringBuilder dateBuilder = new StringBuilder();
		long diff = new java.sql.Date(countTime).getTime();
		long hour = diff % nd / nh + diff / nd * 24;//时
		long min = diff % nd % nh / nm;				//分
		if (hour + min < 0) {
			return null;
		}
		String hS = hour < 10 ? "0" + hour : "" + hour;
		String mS = min < 10 ? "0" + min : "" + min;
		if (isFormat) {
			dateBuilder.append(hS).append("时");
			dateBuilder.append(mS).append("分");
		} else {
			dateBuilder.append(hS).append(":");
			dateBuilder.append(mS).append(":");
		}
		return dateBuilder.toString();
	}

	/**
	 * 获取两个时间的间隔(时:分)
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 返回如33分51秒: 33:51 
	 */
	public static String timeDiff(long startTime, long endTime){
		long diff = new java.sql.Date(endTime).getTime() - new java.sql.Date(startTime).getTime();
		long min = diff % nd % nh / nm;				//分
		long sec = diff % nd % nh % nm / ns;		//秒
		if (min + sec < 0) {
			return "00:00";
		}
		timeDiffBuilder = new StringBuilder();
		if(min < 10){
			timeDiffBuilder.append(0);
		}
		timeDiffBuilder.append(min);
		timeDiffBuilder.append(":");
		if(sec < 10){
			timeDiffBuilder.append(0);
		}
		timeDiffBuilder.append(sec);
		return timeDiffBuilder.toString();
	}

	/**
	 * 获取当前手机日期的月份（用0补位）
	 * @return 如 1月获取01
	 */
	public static String getCurrentMonth(){
		String month = getShortDate().substring(5, 7);
		return month;
	}

	/**
	 * 获取当前手机日期的月份（不补位）
	 * @return 如1月获取1
	 */
	public static int getCurrentMonthInt(){
		return getMonthInt(getShortDate().substring(5, 7));
	}

	/**
	 * 根据2位String月份获取int类型月份（）
	 * @param month
	 * @return 如02获取2
	 */
	public static int getMonthInt(String month){
		int monthInt = 0;
		if (!TextUtils.isEmpty(month)) {
			try {
				monthInt = Integer.parseInt(month);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return monthInt;
	}

	/**
	 * 把当前系统日期按指定样式进行格式化 <br>
	 * 年 yyyy <br>
	 * 月 MM <br>
	 * 日 dd <br>
	 * 时 HH <br>
	 * 分 mm <br>
	 * 秒 ss <br>
	 * @param formatString 格式化样式，如"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String format(String formatString){
		return format(formatString, new Date(System.currentTimeMillis()));
	}

	/**
	 * 把指定的日期格式化为指定格式 <br>
	 * 年 yyyy <br>
	 * 月 MM <br>
	 * 日 dd <br>
	 * 时 HH <br>
	 * 分 mm <br>
	 * 秒 ss <br>
	 * @param formatString 格式化样式
	 * @param date 日期
	 * @return
	 */
	public static String format(String formatString, Date date){
		if (date == null || TextUtils.isEmpty(formatString)) {
			return null;
		}
		return new SimpleDateFormat(formatString).format(date);
	}

	/**
	 * 根据长日期字符串获取Date
	 * @param dateString 如 2015-05-15 10:10:20
	 * @return 返回Date对象
	 */
	public static Date getDateFromString(String dateString){
		if(!TextUtils.isEmpty(dateString)){
			try {
				return formatLongDate.parse(dateString);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 根据字符串日期获年份
	 * @param date 长日期 如2015-01-01 10:20:30 返回2015
	 * @return
	 */
	public static int getYear(String date){
		if(!TextUtils.isEmpty(date)) {
			date = date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""); // 20150102102030
			if(!TextUtils.isEmpty(date) && date.length() == 14) {
				return Integer.parseInt(date.subSequence(0, 4).toString());
			}
		}
		return 0;
	}

	/**
	 * 根据字符串日期获取当前第几月（已经-1）
	 * @param date 长日期 如2015-01-02 10:20:30 返回 1
	 * @return
	 */
	public static int getMonth(String date){
		if(!TextUtils.isEmpty(date)) {
			date = date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""); // 20150102102030
			if(!TextUtils.isEmpty(date) && date.length() == 14) {
				return Integer.parseInt(date.subSequence(4, 6).toString()) - 1;
			}
		}
		return 0;
	}

	/**
	 * 根据字符串日期获取当前月的第几日
	 * @param date 长日期 如2015-01-02 10:20:30 返回 2
	 * @return
	 */
	public static int getDay(String date){
		if(!TextUtils.isEmpty(date)) {
			date = date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""); // 20150102102030
			if(!TextUtils.isEmpty(date) && date.length() == 14) {
				return Integer.parseInt(date.subSequence(6, 8).toString());
			}
		}
		return 0;
	}

	/**
	 * 根据字符串日期获取当前小时
	 * @param date 长日期 如2015-01-02 10:20:30 返回 10
	 * @return
	 */
	public static int getHours(String date){
		if(!TextUtils.isEmpty(date)) {
			date = date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""); // 20150102102030
			if(!TextUtils.isEmpty(date) && date.length() == 14) {
				return Integer.parseInt(date.subSequence(8, 10).toString());
			}
		}
		return 0;
	}

	/**
	 * 根据字符串日期获取当前分钟
	 * @param date 长日期 如2015-01-02 10:20:30 返回 20
	 * @return
	 */
	public static int getMinute(String date){
		if(!TextUtils.isEmpty(date)) {
			date = date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""); // 20150102102030
			if(!TextUtils.isEmpty(date) && date.length() == 14) {
				return Integer.parseInt(date.subSequence(10, 12).toString());
			}
		}
		return 0;
	}

	/**
	 * 根据字符串日期获取当前秒
	 * @param date 长日期 如2015-01-02 10:20:30 返回 30
	 * @return
	 */
	public static int getSecond(String date){
		if(!TextUtils.isEmpty(date)) {
			date = date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""); // 20150102102030
			if(!TextUtils.isEmpty(date) && date.length() == 14) {
				return Integer.parseInt(date.subSequence(12, 14).toString());
			}
		}
		return 0;
	}

	/**
	 * 获取当前秒（2位，不足补0）
	 * @return
	 */
	public static String getSecond(){
//	    System.out.println("========= 年：" + String.format("%ty%n", date)); // 当前年份（2位，不足补0）
//	    System.out.println("========= 月：" + String.format("%tm%n", date) ); // 当前月份（2位，不足补0）
//	    System.out.println("========= 日：" + String.format("%td%n", date) ); // 当前日（2位，不足补0）
//	    System.out.println("========= 时：" + String.format("%tk%n", date) ); // 当前时（2位，不足补0）
//	    System.out.println("========= 分：" + String.format("%tM%n", date) ); // 当前分（2位，不足补0）
//	    System.out.println("========= 秒：" + String.format("%tS%n", date) ); // 当前秒（2位，不足补0）
//	    System.out.println("========毫秒：" + String.format("%tL%n", date) ); // 当前毫秒（3位，不足补0）
		return String.format("%tS%n", new Date());
	}
}
