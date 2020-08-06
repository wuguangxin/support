package com.wuguangxin.base;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录密码验证工具
 * Created by wuguangxin on 2018/4/17.
 */
public class LoginPasswordUtils {
    /**
     * 判断字符串是否是数字
     *
     * @param string
     * @return
     */
    public static boolean isNumber(String string) {

        if (TextUtils.isEmpty(string)) {
            return false;
        }
        String reg = "[0-9]*";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * 判断字符串中是否包含数字
     *
     * @param string 字符串
     * @return
     */
    public static boolean isContainsNumber(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        String reg = ".*\\d+.*";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * 判断字符串是否是字母
     *
     * @param string
     * @return
     */
    public static boolean isAbc(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        String reg = "[a-zA-Z]*";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * 判断字符串中是否包含字母
     *
     * @param string 字符串
     * @return
     */
    public static boolean isContainsAbc(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        String reg = ".*[a-zA-Z]+.*";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * 验证密码（8~20位密码，字母、数字、符号，至少包含2种）
     *
     * @param password 密码
     * @return
     */
    public static boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        int num = 0;

        boolean isContainsNumber = isContainsNumber(password);
        boolean isContainsAbc = isContainsAbc(password);
        boolean isContainsSign = !isNumber(password) && !isAbc(password);

        if (isContainsNumber) num++;
        if (isContainsAbc) num++;
        if (isContainsSign) num++;

        return num >= 2;
    }

    /**
     * 验证密码。只能包含数字、大小写字母或者符号 实现：匹配是否有中文，如果有，则表示密码格式不正确
     *
     * @param context  上下文
     * @param password 要匹配的密码
     * @return 匹配是否通过
     */
    public static boolean matchPassword(Context context, String password) {
        String supportSymbol = "_+-@#$%"; // 支持的符号
        return password.matches("^[0-9a-zA-Z" + supportSymbol + "]{6,20}$");
    }
}
