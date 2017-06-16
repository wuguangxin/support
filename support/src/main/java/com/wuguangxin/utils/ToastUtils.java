package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.wuguangxin.R;

/**
 * Toast工具类
 *
 * <p>Created by wuguangxin on 14/11/28 </p>
 */
public class ToastUtils{
	private static Toast mErrorToast;
	private static Toast mWarnToast;
	private static Toast mFlowToast;
	private static TextView mErrorToastTextView;
	private static TextView mWarnToastTextView;
	private static TextView mFlowToastTextView;

	/**
	 * 显示普通Toast信息-长时间（居中）
	 * @param context Activity of context
	 * @param msg 吐司信息
	 */
	public static void showToast(Context context, String msg){
		warnToast(context, msg, true);
	}

	/**
	 * 显示普通Toast信息-短时间（居中）
	 * @param context Activity of context
	 * @param msg 吐司信息
	 */
	public static void showToast(Context context, String msg, boolean isLong){
		warnToast(context, msg, isLong);
	}

	/**
	 * 警告吐司-长时间（居中）
	 * @param context
	 * @param msg
	 */
	@SuppressLint("InflateParams")
	public static void warnToast(Context context, String msg){
		warnToast(context, msg, true);
	}

	/**
	 * 显示流量吐司-短时间（顶部）
	 * @param context
	 * @param msg
	 */
	@SuppressLint("InflateParams")
	public static void flowToast(Context context, String msg){
		flowToast(context, msg, false);
	}

	/**
	 * 警告吐司-短时间（居中）
	 * @param context
	 * @param msg
	 */
	@SuppressLint("InflateParams")
	public static void warnToast(Context context, String msg, boolean isLong){
		if (context != null) {
			if (mWarnToast == null || mWarnToastTextView == null) {
				mWarnToast = Toast.makeText(context, null, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
				mWarnToast.setGravity(Gravity.CENTER, 0, 0);
				if(mWarnToastTextView == null){
					mWarnToastTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.xin_toast_warn_layout, null);
				}
				mWarnToast.setView(mWarnToastTextView);
			}
			mWarnToastTextView.setText(msg);
			mWarnToast.show();
			printToast(context, msg);
		}
	}

	/**
	 * 错误吐司（顶部）
	 * @param context 上下文
	 * @param msg 吐司信息
	 */ 
	@SuppressLint("InflateParams")
	public static void errorToast(Context context, String msg){
		if (context != null) {
			if (mErrorToast == null) {
				mErrorToast = Toast.makeText(context, null, Toast.LENGTH_LONG);
				mErrorToast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
				if(mErrorToastTextView == null){
					mErrorToastTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.xin_toast_error_layout, null);
				}
				mErrorToast.setView(mErrorToastTextView);
			}
			mErrorToastTextView.setText(msg);
			mErrorToast.show();
			printToast(context, msg);
		}
	}


	/**
	 * 显示流量吐司-短时间（顶部）
	 * @param context
	 * @param msg
	 */
	@SuppressLint("InflateParams")
	public static void flowToast(Context context, String msg, boolean isLong){
		if (context != null) {
			if (mFlowToast == null || mFlowToastTextView == null) {
				mFlowToast = Toast.makeText(context, null, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
				mFlowToast.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
				if(mFlowToastTextView == null){
					mFlowToastTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.xin_toast_flow_layout, null);
				}
				mFlowToast.setView(mFlowToastTextView);
			}
			mFlowToastTextView.setText(msg);
			mFlowToast.show();
			printToast(context, msg);
		}
	}

	private static void printToast(Context context, String msg) {
		Logger.i(context, msg);
	}
}
