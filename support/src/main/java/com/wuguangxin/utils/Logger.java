package com.wuguangxin.utils;

import android.content.Context;
import android.util.Log;

/**
 * 日志打印工具类
 *
 * <p>Created by wuguangxin on 14/10/11 </p>
 */
public class Logger{
	private final static String TAG = "Logger";
	private static boolean mDebug = false;

	public static boolean isDebug() {
		return mDebug;
	}

	public static void setDebug(boolean debug) {
		mDebug = debug;
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
		tag = String.format("wgx %s", tag);
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
	}
}
