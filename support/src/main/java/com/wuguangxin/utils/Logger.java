package com.wuguangxin.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * 日志打印工具类
 *
 * <p>Created by wuguangxin on 14/10/11 </p>
 */
public class Logger{
	private final static String TAG = "Logger";
	private static String TAG_PREFIX = "";
	private static boolean mDebug = false;

	/**
	 * 是否是DEBUG
	 * @return 是否是DEBUG
	 */
	public static boolean isDebug() {
		return mDebug;
	}

	/**
	 * 设置Logger的debug模式，true则打印日志，false则不打印。
	 * @param debug debug
	 */
	public static void setDebug(boolean debug) {
		mDebug = debug;
	}

	/**
	 * 设置Tag的前缀，可以给整个项目加个前缀，便于过滤项目以外的日志。
	 * @param tagPrefix 前缀
	 */
	public static void setTagPrefix(String tagPrefix) {
		TAG_PREFIX = tagPrefix;
	}

	public static void d(String debugMsg){
		printLog(TAG, debugMsg, Log.DEBUG);
	}
	public static void i(String infoMsg){
		printLog(TAG, infoMsg, Log.INFO);
	}
	public static void e(String errorMsg){
		printLog(TAG, errorMsg, Log.ERROR);
	}
	public static void w(String warnMsg){
		printLog(TAG, warnMsg, Log.WARN);
	}
	public static void v(String verboseMsg){
		printLog(TAG, verboseMsg, Log.VERBOSE);
	}

	// *****************************************************************************
	public static void i(Context context, String infoMsg){
		printLog(context.getClass().getSimpleName(), infoMsg, Log.INFO);
	}
	public static void d(Context context, String debugMsg){
		printLog(context.getClass().getSimpleName(), debugMsg, Log.DEBUG);
	}
	public static void e(Context context, String errorMsg){
		printLog(context.getClass().getSimpleName(), errorMsg, Log.ERROR);
	}
	public static void w(Context context, String warnMsg){
		printLog(context.getClass().getSimpleName(), warnMsg, Log.WARN);
	}
	public static void v(Context context, String verboseMsg){
		printLog(context.getClass().getSimpleName(), verboseMsg, Log.VERBOSE);
	}
	
	// *****************************************************************************
	public static void i(String tag, String infoMsg){
		printLog(tag, infoMsg, Log.INFO);
	}
	public static void d(String tag, String debugMsg){
		printLog(tag, debugMsg, Log.DEBUG);
	}
	public static void e(String tag, String errorMsg){
		printLog(tag, errorMsg, Log.ERROR);
	}
	public static void w(String tag, String warnMsg){
		printLog(tag, warnMsg, Log.WARN);
	}
	public static void v(String tag, String verboseMsg){
		printLog(tag, verboseMsg, Log.VERBOSE);
	}

	private static void printLog(String tag, String msg, int type){
		if (!mDebug) {
			return;
		}
		tag = String.format("%s%s", TAG_PREFIX, tag);
		switch (type){
		case Log.VERBOSE:
			Log.v(tag, msg);
			break;
		case Log.DEBUG:
			Log.d(tag, msg);
			break;
		case Log.INFO:
			Log.i(tag, msg);
			break;
		case Log.WARN:
			Log.w(tag, msg);
			break;
		case Log.ERROR:
			Log.e(tag, msg);
			break;
		}

		if (logCacheList == null) {
			logCacheList = new LogCacheList();
		}
		String time = formatStringLong(System.currentTimeMillis(), "MM-dd HH:mm:ss.SSS");
		LogBean logBean = new LogBean(time + " " + tag+":", msg);
		logCacheList.addLast(logBean);

		if (onLogChangeListener != null) {
			onLogChangeListener.onChanged(logBean, logCacheList);
		}
	}

	private static final SimpleDateFormat FORMAT_DATE_LONG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

	private static LogCacheList logCacheList = new LogCacheList();

	public static LogCacheList getLogCacheList() {
		return logCacheList;
	}

	private static OnLogChangeListener onLogChangeListener;

	public static void setOnLogChangeListener(OnLogChangeListener onLogChangeListener) {
		Logger.onLogChangeListener = onLogChangeListener;
	}

	public interface OnLogChangeListener {
		/**
		 * 日志变化时回调
		 * @param newLogBean 新增加的日志
		 * @param logList 最新的日志列表
		 */
		void onChanged(LogBean newLogBean, LogCacheList logList);
	}

	public static class LogBean {
		public String tag;
		public String msg;

		public LogBean(String tag, String msg) {
			this.tag = tag;
			this.msg = msg;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("LogBean{");
			sb.append("tag='").append(tag).append('\'');
			sb.append(", msg='").append(msg).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}

	/**
	 * 日志缓存列表。默认缓存list的size为1024*10字节（10Kb）
	 * Created by wuguangxin on 2019/1/10.
	 */
	public static class LogCacheList extends LinkedList<LogBean> {
		private int maxSize = 1024*10;

		public int getMaxSize() {
			return maxSize;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}

		@Override
		public boolean add(LogBean logBean) {
			checkSize();
			return super.add(logBean);
		}

		@Override
		public void addFirst(LogBean logBean) {
			checkSize();
			super.addFirst(logBean);
		}

		@Override
		public void addLast(LogBean logBean) {
			checkSize();
			super.addLast(logBean);
		}

		public void addFirst(String tag, String log) {
			addFirst(new LogBean(tag, log));
		}

		public void addLast(String tag, String log) {
			addLast(new LogBean(tag, log));
		}

		private void checkSize() {
			int size = size();
			if (maxSize > 0 && size > maxSize) {
				System.out.println("缓存数量："+ size + " | " + maxSize);
				System.out.println("缓存数量过大，正在释放部分数据");
				super.removeFirst();
			}
		}
	}

}
