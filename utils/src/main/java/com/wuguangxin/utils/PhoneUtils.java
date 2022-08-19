package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 手机号码工具类
 *
 * <p>Created by wuguangxin on 14/6/2 </p>
 */
public class PhoneUtils {
    private static String REG_PHONE = "^(1[3-9])\\d{9}";

    /**
     * 设置手机号码正则表达式
     *
     * @param reg
     */
    public static void setregphone(String reg) {
        PhoneUtils.REG_PHONE = reg;
    }

    /**
     * 判断是否是手机号码（基本判断，不是很准确）。
     *
     * @param number 手机号码
     * @return 参数为null和不合法时返回false，否则返回true
     */
    public static boolean isPhoneNumber(String number) {
        if (!TextUtils.isEmpty(number) && number.matches(REG_PHONE)) {
            return true;
        }
        return false;
    }

    /**
     * 将手机号码中间4位用*代替（如186 **** 1234），如果不是手机号码格式，返回原数据
     *
     * @param phoneNumber 手机号码()
     * @return 字符串
     */
    public static String formatHide4(String phoneNumber) {
        if (!isPhoneNumber(phoneNumber)) {
            return phoneNumber;
        }
        phoneNumber = phoneNumber.replaceAll(" ", "").replaceAll("-", "");
        return String.format("%s****%s", phoneNumber.substring(0, 3), phoneNumber.substring(7));
    }

    /**
     * 将手机号码中间4位用*代替（如186 **** 1234），并在*号两端加空格，适合于文本变化监听器调用
     *
     * @param phoneNumber 手机号码
     * @return 字符串
     */
    public static String formatHide4Space(String phoneNumber) {
        if (!isPhoneNumber(phoneNumber)) {
            return phoneNumber;
        }
        phoneNumber = phoneNumber.replaceAll(" ", "").replaceAll("-", "");
        return String.format("%s **** %s", phoneNumber.substring(0, 3), phoneNumber.substring(7));
    }

    /**
     * 给手机号码加空格（如 186 0000 1111）
     *
     * @param phoneNumber 手机号
     * @return 字符串
     */
    public static String formatSpace(String phoneNumber) {
        if (!isPhoneNumber(phoneNumber)) {
            return "";
        }
        phoneNumber = phoneNumber.replaceAll(" ", "").replaceAll("-", "");
        return String.format("%s %s %s", phoneNumber.substring(0, 3), phoneNumber.substring(3, 7), phoneNumber.substring(7));
    }

    /**
     * 获取本机号码，如果是双卡手机，获取的将是卡槽1的号码（需要权限）
     *
     * @param context 上下文
     * @return 手机号
     */
    public static String getThisPhoneNumber(Context context) {
        if (context == null) {
            return null;
        }
        TelephonyManager manager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        String phoneString = manager.getLine1Number();
        if (phoneString != null) {
            return phoneString.replace("+86", "");
        }
        return null;
    }

    /**
     * 直接拨打电话
     *
     * @param context 上下文
     * @param tel     号码字符串
     */
    @SuppressLint("MissingPermission")
    public static void call(Context context, String tel) {
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel)));
    }

    /**
     * 打开拨号界面
     *
     * @param context 上下文
     * @param tel     号码字符串
     */
    public static void callView(Context context, String tel) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel)));
    }
}
