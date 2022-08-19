package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.net.URL;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具类
 * <p>Created by wuguangxin on 15-3-31 </p>
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {
    private static final String DEFAULT_URL = "https://www.baidu.com"; // 默认时间来源
    public static final SimpleDateFormat FORMAT_DATE_LONG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat FORMAT_DATE_SHORT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat FORMAT_DATE = FORMAT_DATE_SHORT;
    public static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");
    public static final long ND = 1000 * 60 * 60 * 24;        //一天的毫秒数
    public static final long NH = 1000 * 60 * 60;            //一小时的毫秒数
    public static final long NM = 1000 * 60;                //一分钟的毫秒数
    public static final long NS = 1000;                    //一秒钟的毫秒数
    private static StringBuilder timeDiffBuilder;

    /* *****************************************************************************************
     *          获取 长日期 Date
     *******************************************************************************************/
    /**
     * 获取系统当前长日期
     * @return Date
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 获取系统当前短日期，如 2015-01-01。
     * @return 短日期字符串
     */
    public static Date getDateShort() {
        return formatDateShort(new Date());
    }

    /**
     * 获取当前系统长日期字符串，如 2014-01-01 00:00:00。
     * @return 长日期字符串
     */
    public static String getStringLong() {
        return FORMAT_DATE_LONG.format(new Date());
    }

    /**
     * 获取当前系统短日期字符串，如 2014-01-01。
     * @return 短日期字符串
     */
    public static String getStringShort() {
        return FORMAT_DATE_SHORT.format(new Date());
    }

    /**
     * 获取当前系统时间，如 10:10:30。
     * @return 长日期字符串
     */
    public static String getStringTime() {
        return FORMAT_TIME.format(new Date());
    }

    /**
     * 获取网络长日期字符串，如 2014-01-01 00:00:00。
     * @return 长日期字符串
     */
    public static String getStringLongFromNet() {
        return DateUtils.formatStringLong(getTimestampFromNet(null));
    }

    /* *****************************************************************************************
     *          格式化为 长日期 Date
     *******************************************************************************************/
    /**
     * 根据时间戳转换为长日期Date。
     * @param timestamp 时间戳
     * @return Date
     */
    public static Date formatDateLong(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 格式化为长日期对象Date。
     * @param dataString 如2017-01-01 10:20:30
     * @return Date
     */
    public static Date formatDateLong(String dataString) {
        ParsePosition pos = new ParsePosition(0);
        return FORMAT_DATE_LONG.parse(dataString, pos);
    }

    /* *****************************************************************************************
     *          格式化为 长日期 String
     *******************************************************************************************/

    /**
     * 格式化为长日期字符串，如 2014-01-01 00:00:00。
     * @param timestamp String类型时间戳
     * @return 长日期字符串
     */
    public static String formatStringLong(String timestamp) {
        if (TextUtils.isEmpty(timestamp)) {
            return null;
        }
        return FORMAT_DATE_LONG.format(new Date(Long.parseLong(timestamp)));
    }

    /**
     * 格式化为长日期字符串，如 2014-01-01 00:00:00。
     * @param timestamp long类型时间戳
     * @return 长日期字符串
     */
    public static String formatStringLong(long timestamp) {
        if (timestamp <= 0) return null;
        return FORMAT_DATE_LONG.format(new Date(timestamp));
    }

    /**
     * 格式化为长日期字符串，如 2014-01-01 00:00:00。
     * @param date Date类型时间
     * @return 长日期字符串
     */
    public static String formatStringLong(Date date) {
        if (date == null) return null;
        return FORMAT_DATE_LONG.format(date);
    }

    /**
     * 格式化为长日期字符串，如 2014-01-01 00:00:00。
     * @param timestamp 时间戳
     * @param format 格式化样式 yyyy-MM-dd HH:mm:ss
     * @return 长日期字符串
     */
    public static String formatStringLong(long timestamp, String format) {
        if (timestamp <= 0) {
            return null;
        }
        if (!TextUtils.isEmpty(format)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(timestamp);
        }
        return FORMAT_DATE_LONG.format(new Date(timestamp));
    }

    /**
     * 格式化为长日期加时间，不带秒。如 2014-01-01 15:20。
     * @param timestamp long类型时间戳
     * @return 返回年月日时分
     */
    public static String format_yyyyMMddHHmm(long timestamp) {
        if (timestamp <= 0) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(timestamp));
    }

    /* *****************************************************************************************
     *          格式化 短日期 Date
     *******************************************************************************************/

    /**
     * 把一个时间戳日期去掉时分秒并转换为Date
     *
     * @param timeMillis 时间戳
     * @return Date，如果日期格式不传入不正确，则返回null。
     */
    public static Date formatDateShort(long timeMillis) {
        if (timeMillis == 0) {
            return null;
        }
        return formatDateShort(DateUtils.formatStringLong(new Date(timeMillis)));
    }

    /**
     * 把一个字符串日期去掉时分秒并转换为Date
     *
     * @param dateString 字符串日期 如：2017-01-01 或 2017-01-01 12:12:12,符合日期格式就行
     * @return Date，如果日期格式不传入不正确，则返回null。
     */
    public static Date formatDateShort(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化为短日期Date，就是去掉时分秒
     *
     * @param date 日期对象
     * @return Date对象，如果日期格式不传入不正确，则返回null。
     */
    public static Date formatDateShort(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            return format.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把一个时间戳，按 指定格式（pattern）进行格式化，并返回 Date
     *
     * @param timeMillis 时间戳
     * @param pattern 参考：yyyy-MM-dd HH:mm:ss
     * @return Date，发生异常会返回null。
     */
    public static Date formatDate(long timeMillis, String pattern) {
        try {
            String dateString = new SimpleDateFormat(pattern).format(new Date(timeMillis));
            return formatDate(dateString, pattern);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把一个字符串日期（dateString） 按 自定义的格式化（pattern）进行格式化，并返回 Date
     *
     * @param dateString 字符串日期 如：2017-01-01 或 2017-01-01 12:12:12,符合日期格式就行
     * @param pattern 参考：yyyy-MM-dd HH:mm:ss
     * @return Date，发生异常返回null。
     */
    public static Date formatDate(String dateString, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* *****************************************************************************************
     *         格式化 短日期 String
     *******************************************************************************************/

    /**
     * 格式化时间戳为短日期格式，以"-"分割
     * @param date 日期对象
     * @return 如 2014-01-01
     */
    public static String formatStringShort(Date date) {
        if (date == null) return null;
        return FORMAT_DATE_SHORT.format(date);
    }

    /**
     * 格式化时间戳为短日期格式，以"-"分割
     * @param timestamp String类型时间戳
     * @return 如 2014-01-01
     */
    public static String formatStringShort(String timestamp) {
        if (TextUtils.isEmpty(timestamp)) {
            return null;
        }
        return FORMAT_DATE.format(new Date(Long.parseLong(timestamp)));
    }

    /**
     * 格式化时间戳为短日期格式，以"-"分割
     * @param timestamp long类型时间戳
     * @return 如 2014-01-01
     */
    public static String formatStringShort(long timestamp) {
        if (timestamp <= 0) return null;
        return FORMAT_DATE.format(new Date(timestamp));
    }

    /* *****************************************************************************************
     *          格式化为 自定义 当前手机时间字符串
     *******************************************************************************************/

    /**
     * 把当前系统日期按指定样式进行格式化 <br>
     * 年 yyyy <br>
     * 月 MM <br>
     * 日 dd <br>
     * 时 HH <br>
     * 分 mm <br>
     * 秒 ss <br>
     *
     * @param format 格式化样式，如"yyyy-MM-dd HH:mm:ss"
     * @return 格式化的字符串
     */
    public static String formatString(String format) {
        return formatString(format, new Date());
    }

    /**
     * 把指定的日期格式化为指定格式 <br>
     * 年 yyyy <br>
     * 月 MM <br>
     * 日 dd <br>
     * 时 HH <br>
     * 分 mm <br>
     * 秒 ss <br>
     *
     * @param format 格式化样式
     * @param date 日期
     * @return 格式化的字符串
     */
    public static String formatString(String format, Date date) {
        if (date == null || TextUtils.isEmpty(format)) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    /* *****************************************************************************************
     *          返回时间戳long专区
     *******************************************************************************************/


    /**
     * String类型长日期转换为long类型时间戳(有BUG，未解决)
     * @param dateString String类型长日期
     * @return long类型时间戳
     */
    public static long formatLongTimestamp(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return 0;
        }
        try {
            return FORMAT_DATE_LONG.parse(dateString).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 获取网络时间戳（默认时间源 http://www.beijing-time.org）
     * @return 时间戳
     */
    public static long getTimestampFromNet() {
        return getTimestampFromNet(null);
    }

    /**
     * 指定服务器地址获取网络时间戳
     * @param webUrl 任何一个网站域名或网络IP，如 http://www.baidu.com
     * @return 网络时间戳
     */
    public static long getTimestampFromNet(String webUrl) {
        if (TextUtils.isEmpty(webUrl)) {
            webUrl = DEFAULT_URL;
        }
        long bjTimestamp = 0;
        try {
            URL url = new URL(webUrl);//取得资源对象
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


    /* *****************************************************************************************
     *          返回时间相差几天
     *******************************************************************************************/

    /**
     * 比较两个日期相差的天数，精确到天，时分秒去掉再比较。
     * @param date1
     * @param date2
     * @return
     */
    public static int dateDiff(Date date1, Date date2) {
        try {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            int day1 = cal1.get(Calendar.DAY_OF_YEAR);
            int day2 = cal2.get(Calendar.DAY_OF_YEAR);

            int year1 = cal1.get(Calendar.YEAR);
            int year2 = cal2.get(Calendar.YEAR);
            if (year1 != year2) {
                // 同一年
                int timeDistance = 0;
                for (int i = year1; i < year2; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                        //闰年
                        timeDistance += 366;
                    } else {
                        //不是闰年
                        timeDistance += 365;
                    }
                }
                return timeDistance + (day2 - day1);
            } else {
                //不同年
                return day2 - day1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取两个日期的间隔天数（精确到天，会把传入的两个时间戳去掉时分秒）
     *
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return 间隔天数
     */
    public static int dateDiff(long startTime, long endTime) {
        try {
            Date startDate = FORMAT_DATE_SHORT.parse(FORMAT_DATE_SHORT.format(new Date(startTime)));
            Date endDate = FORMAT_DATE_SHORT.parse(FORMAT_DATE_SHORT.format(new Date(endTime)));
            return dateDiff(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取两个日期的间隔(天时分秒)
     *
     * @param startTime 开始时间 时间戳
     * @param endTime 结束时间 时间戳（用当前时间）
     * @param isFormat 如果为true, 返回 00天11时22分33秒，如果为false，则为00:11:22:33
     * @return 返回如: 01天22时33分51秒 或 01:22:33:51
     */
    public static String dateDiff(long startTime, long endTime, boolean isFormat) {
        long diff = new java.sql.Date(endTime).getTime() - new java.sql.Date(startTime).getTime();
        return dateDiff(diff, isFormat);
    }

    /**
     * 将指定毫秒值格式化为(00天00时00分00秒)
     *
     * @param countTime 总时间 时间戳
     * @param isFormat 如果为true, 返回 00天11时22分33秒，如果为false，则为00:11:22:33
     * @return 返回如: 01天22时33分51秒 或 01:22:33:51
     */
    public static String dateDiff(long countTime, boolean isFormat) {
        StringBuilder dateBuilder = new StringBuilder();
        long diff = new java.sql.Date(countTime).getTime();
        long day = diff / ND;                        //天
        long hour = diff % ND / NH;                    //时
        long min = diff % ND % NH / NM;                //分
        long sec = diff % ND % NH % NM / NS;        //秒
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
     * 格式化指定时间毫秒数为 时、分、秒 三个数据格式，默认格式为 00时00分00秒。
     *
     * @param countTime 总时间戳（毫秒值）
     * @param format 格式化类型,，例如：%s时%s分%s秒，则返回结果例如：00时00分00秒
     * @return 返回如: 00时00分00秒
     */
    public static String formatHHmmss(long countTime, String format) {
        StringBuilder dateBuilder = new StringBuilder();
        long diff = new java.sql.Date(countTime).getTime();
        long hour = diff % ND / NH + diff / ND * 24;//时
        long min = diff % ND % NH / NM;                //分
        long sec = diff % ND % NH % NM / NS;        //秒
        if (hour + min + sec < 0) {
            return null;
        }
        String hS = hour < 10 ? "0" + hour : "" + hour;
        String mS = min < 10 ? "0" + min : "" + min;
        String sS = sec < 10 ? "0" + sec : "" + sec;
        if (TextUtils.isEmpty(format)) {
            dateBuilder.append(hS).append("时");
            dateBuilder.append(mS).append("分");
            dateBuilder.append(sS).append("秒");
            return dateBuilder.toString();
        } else {
            return String.format(format, hS, mS, sS);
        }
    }

    /**
     * 格式化指定时间毫秒数为(时分秒)格式
     *
     * @param countTime 总时间 时间戳
     * @param isFormat 如果为true, 返回 11时22分33秒，如果为false，则为11:22:33
     * @return 返回如: 22时33分51秒 或 22:33:51
     */
    public static String formatHHmmss(long countTime, boolean isFormat) {
        StringBuilder dateBuilder = new StringBuilder();
        long diff = new java.sql.Date(countTime).getTime();
        long hour = diff % ND / NH + diff / ND * 24;//时
        long min = diff % ND % NH / NM;                //分
        long sec = diff % ND % NH % NM / NS;        //秒
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
            dateBuilder.append(sS);
        }
        return dateBuilder.toString();
    }


    /**
     * 格式化指定时间毫秒数为(时分)格式
     *
     * @param countTime 总时间 时间戳
     * @param isFormat 如果为true, 返回 11时22分，如果为false，则为11:22
     * @return 返回如: 22时33分 或 22:33
     */
    public static String formatHHmm(long countTime, boolean isFormat) {
        StringBuilder dateBuilder = new StringBuilder();
        // 因为不显示秒，避免显示00时00分(后面我59秒之后不显示)，所以分+1，秒去掉。
        Date date = new Date(countTime+60*1000);
        date.setSeconds(0);
        long diff = date.getTime();
        long hour = diff % ND / NH + diff / ND * 24;//时
        long min = diff % ND % NH / NM;             //分
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
            dateBuilder.append(mS);
        }
        return dateBuilder.toString();
    }

    /**
     * 格式化指定时间毫秒数为 时、分 两个值格式，默认格式为 00时00分。
     *
     * @param countTime 总时间戳（毫秒值）
     * @param format 格式化类型,，例如：%s时%s分，则返回结果例如：00时00分
     * @return 返回如: 00时00分
     */
    public static String formatHHmm(long countTime, String format) {
        StringBuilder dateBuilder = new StringBuilder();
        // 因为不显示秒，避免显示00时00分(后面我59秒之后不显示)，所以分+1，秒去掉。
        Date date = new Date(countTime+60*1000);
        date.setSeconds(0);
        long diff = date.getTime();
        long hour = diff % ND / NH + diff / ND * 24;//时
        long min = diff % ND % NH / NM;             //分
        if (hour + min < 0) {
            return null;
        }
        String hS = hour < 10 ? "0" + hour : "" + hour;
        String mS = min < 10 ? "0" + min : "" + min;
        if (TextUtils.isEmpty(format)) {
            dateBuilder.append(hS).append("时");
            dateBuilder.append(mS).append("分");
            return dateBuilder.toString();
        } else {
            return String.format(format, hS, mS);
        }
    }

    /**
     * 获取两个时间的间隔(时:分)
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 返回如33分51秒: 33:51
     */
    public static String timeDiff(long startTime, long endTime) {
        long diff = new java.sql.Date(endTime).getTime() - new java.sql.Date(startTime).getTime();
        long min = diff % ND % NH / NM;                //分
        long sec = diff % ND % NH % NM / NS;        //秒
        if (min + sec < 0) {
            return "00:00";
        }
        timeDiffBuilder = new StringBuilder();
        if (min < 10) {
            timeDiffBuilder.append(0);
        }
        timeDiffBuilder.append(min);
        timeDiffBuilder.append(":");
        if (sec < 10) {
            timeDiffBuilder.append(0);
        }
        timeDiffBuilder.append(sec);
        return timeDiffBuilder.toString();
    }


    /**
     * 将字符串日期转换为Date
     *
     * @param dataString 必须符合以下几种格式：
     * 1：yyyy-MM
     * 2：yyyy-MM-dd
     * 3：yyyy-MM-dd HH:mm
     * 4：yyyy-MM-dd HH:mm:ss
     * 5：yyyy/MM/dd HH:mm:ss
     * 6：yyyy.MM.dd HH:mm:ss
     * 7：yyyy年MM月dd日HH时mm分ss秒。
     * 如果传入的时间格式不正确，则返回0。
     * @return Date
     */
    public static Date formatDate(String dataString) {
        if (TextUtils.isEmpty(dataString)) {
            return null;
        }
        try {
            if (dataString.contains(".")) dataString = dataString.replaceAll("\\.", "-");
            if (dataString.contains("/")) dataString = dataString.replaceAll("/", "-");
            if (dataString.contains("年")) dataString = dataString.replaceAll("年", "-");
            if (dataString.contains("月")) dataString = dataString.replaceAll("月", "-");
            if (dataString.contains("日")) dataString = dataString.replaceAll("日", " ");
            if (dataString.contains("时")) dataString = dataString.replaceAll("时", ":");
            if (dataString.contains("分")) dataString = dataString.replaceAll("分", ":");
            if (dataString.contains("秒")) dataString = dataString.replaceAll("秒", "");
            dataString = dataString.trim(); // 去除首尾空格
            SimpleDateFormat format;
            int length = dataString.length();
            switch (length) {
            case 7:
                format = new SimpleDateFormat("yyyy-MM");
                break;
            case 10:
                format = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 16:
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;
            default:
            case 19:
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            }
            Date date = format.parse(dataString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* *****************************************************************************************
     *          返回日期的值
     *******************************************************************************************/

    /**
     * 获取当前手机日期的月份（用0补位）
     *
     * @return 如 1月获取01
     */
    public static String getCurrentMonth() {
        String month = getStringShort().substring(5, 7);
        return month;
    }

    /**
     * 获取当前手机日期的月份（不补位）
     *
     * @return 如1月获取1
     */
    public static int getCurrentMonthInt() {
        return getMonthInt(getStringShort().substring(5, 7));
    }

    /**
     * 根据2位String月份获取int类型月份（）
     *
     * @param month 2位String月份
     * @return 如02获取2
     */
    public static int getMonthInt(String month) {
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
     * 根据字符串日期获年份(已加1900)
     *
     * @param dateString 长日期 如2015-01-01 10:20:30 返回2015
     * @return 年份
     */
    public static int getYear(String dateString) {
        Date date = formatDate(dateString);
        return date == null ? 0 : date.getYear() + 1900;
    }

    /**
     * 根据字符串日期获取当前第几月（0-11）
     *
     * @param dateString 长日期 如2015-01-02 10:20:30 返回0
     * @return 月份（0-11）
     */
    public static int getMonth(String dateString) {
        Date date = formatDate(dateString);
        return date == null ? 0 : date.getMonth();
    }

    /**
     * 根据字符串日期获取当前月是第几日
     *
     * @param dateString 长日期 如2015-01-02 10:20:30 返回 2
     * @return 第几日(1-31)
     */
    public static int getDate(String dateString) {
        Date date = formatDate(dateString);
        return date == null ? 0 : date.getDate();
    }

    /**
     * 根据字符串日期获取当前小时
     *
     * @param dateString 长日期 如2015-01-02 10:20:30 返回 10
     * @return 当前小时
     */
    public static int getHours(String dateString) {
        Date date = formatDate(dateString);
        return date == null ? 0 : date.getHours();
    }

    /**
     * 根据字符串日期获取当前分钟
     *
     * @param dateString 长日期 如2015-01-02 10:20:30 返回 20
     * @return 当前分钟
     */
    public static int getMinute(String dateString) {
        Date date = formatDate(dateString);
        return date == null ? 0 : date.getMinutes();
    }

    /**
     * 根据字符串日期获取当前秒
     *
     * @param dateString 长日期 如2015-01-02 10:20:30 返回 30
     * @return 当前秒
     */
    public static int getSecond(String dateString) {
        Date date = formatDate(dateString);
        return date == null ? 0 : date.getSeconds();
    }

    /**
     * 根据字符串日期获取它是周几
     *
     * @param dateString 长日期 如2015-01-02 10:20:30
     * @return 返回这个日期是周几（0-6：周日-周六）
     */
    public static int getWeek(String dateString) {
        Date date = formatDate(dateString);
        return date == null ? 0 : date.getDay();
    }

    /**
     * 获取当前秒（2位，不足补0）
     *
     * @return 当前秒（2位，不足补0）
     */
    public static String addZero() {
//	    System.out.println("========= 年：" + String.formatString("%ty%n", date)); // 当前年份（2位，不足补0）
//	    System.out.println("========= 月：" + String.formatString("%tm%n", date) ); // 当前月份（2位，不足补0）
//	    System.out.println("========= 日：" + String.formatString("%td%n", date) ); // 当前日（2位，不足补0）
//	    System.out.println("========= 时：" + String.formatString("%tk%n", date) ); // 当前时（2位，不足补0）
//	    System.out.println("========= 分：" + String.formatString("%tM%n", date) ); // 当前分（2位，不足补0）
//	    System.out.println("========= 秒：" + String.formatString("%tS%n", date) ); // 当前秒（2位，不足补0）
//	    System.out.println("========毫秒：" + String.formatString("%tL%n", date) ); // 当前毫秒（3位，不足补0）
        return String.format("%tS%n", new Date());
    }

    /**
     * 根据给定时间戳，获取该年总天数
     * @param timestamp 时间戳
     */
    public static int getDaysOfYear(long timestamp) {
        Date date = DateUtils.formatDateLong(timestamp);
        int year = date.getYear() + 1900;//要判断的年份，比如2008
        int days;//某年(year)的天数
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {//闰年的判断规则
            days = 366;
        } else {
            days = 365;
        }
        return days;
    }
}
