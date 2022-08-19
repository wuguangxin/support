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
public class IDCardUtils {
    /**
     * 中国公民身份证号码最小长度。
     */
    private static final int CHINA_ID_15 = 15;
    /**
     * 中国公民身份证号码最大长度。
     */
    private static final int CHINA_ID_18 = 18;
    /**
     * 最低年限
     */
    private static final int MIN = 1930;

    /**
     * 每位加权因子
     */
    private static int[] getWeight() {
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        return weight;
    }

    /**
     * 台湾身份首字母对应数字
     */
    private static Map<String, Integer> getTWFirstCode() {
        final Map<String, Integer> twFirstCode = new HashMap<>();
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
        return twFirstCode;
    }

    private static Map<String, String> getCityCodes() {
        final Map<String, String> cityCodes = new HashMap<>();
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
        return cityCodes;
    }

    /**
     * 将15位身份证号码转换为18位
     *
     * @param idCard 15位身份编码
     * @return 18位身份编码
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertIDCard15To18(String idCard) {
        String idCard18;
        if (idCard.length() != CHINA_ID_15) {
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
            if (birthDate != null) {
                cal.setTime(birthDate);
            }
            // 获取出生年(完全表现形式,如：2010)
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            // 转换字符数组
            char[] cArr = idCard18.toCharArray();
            if (cArr != null) {
                int[] iCard = convertCharToInt(cArr);
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
     * 检查是否是纯中文（长度2-6）
     *
     * @param name
     */
    public static boolean checkNameCN(String name) {
        String reg = "([\u4E00-\u9FA5]{2,6})";
        return Pattern.matches(reg, name);
    }

    /**
     * 检查是否是纯英文（长度3-10）
     *
     * @param name
     */
    public static boolean checkNameEN(String name) {
        String reg = "([a-zA-Z]{3,10})";
        return Pattern.matches(reg, name);
    }

    /**
     * 验证18位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard18(String idCard) {
        if (TextUtils.isEmpty(idCard)) return false;
        if (idCard.length() == CHINA_ID_18) {
            String code17 = idCard.substring(0, 17); // 前17位
            if (isNum(code17)) { // 如果前17位为数字
                char[] cArr = code17.toCharArray(); // 把前17位转为数组
                return cArr != null;
            }
        }
        return false;
    }

    /**
     * 验证18位身份证号码
     *
     * @param idCard 身份证号
     * @return Boolean
     */
    public static boolean checkIDCard18(String idCard) {
        if (TextUtils.isEmpty(idCard)) return false;
        if (idCard.length() == 18) {
            int sum = 0;
            int[] intArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            for (int i = 0; i < intArr.length; i++) {
                // 将这17位数字和系数相乘的结果相加。
                sum += Character.digit(idCard.charAt(i), 10) * intArr[i];
            }
            int mod = sum % 11;
            int[] intArr3 = {1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};
            String matchDigit = "";
            for (int i = 0; i < 11; i++) {
                if (i == mod) {
                    matchDigit = String.valueOf(intArr3[i]);
                    if (intArr3[i] > 57) {
                        matchDigit = String.valueOf((char) intArr3[i]);
                    }
                }
            }
            return matchDigit.equals(idCard.substring(idCard.length() - 1).toUpperCase());
        }
        return false;
    }

    /**
     * 验证15位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard15(String idCard) {
        if (idCard.length() != CHINA_ID_15) {
            return false;
        }
        if (isNum(idCard)) {
            String proCode = idCard.substring(0, 2);
            if (getCityCodes().get(proCode) == null) {
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
            if (birthDate != null) {
                cal.setTime(birthDate);
            }
            return valiDate(cal.get(Calendar.YEAR),
                    Integer.parseInt(birthCode.substring(2, 4)),
                    Integer.parseInt(birthCode.substring(4, 6)));
        }
        return false;
    }

    /**
     * 验证10位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 身份证信息数组:
     * <br>[0] - 台湾、澳门、香港。
     * <br>[1] - 性别(男M,女F,未知N)。
     * <br>[2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null。
     */
    public static String[] validateIdCard10(String idCard) {
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
    public static boolean validateTWCard(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return false;
        }
        String start = idCard.substring(0, 1);
        String mid = idCard.substring(1, 9);
        String end = idCard.substring(9, 10);
        int iStart = getTWFirstCode().get(start);
        int sum = iStart / 10 + (iStart % 10) * 9;
        char[] chars = mid.toCharArray();
        int flag = 8;
        for (char c : chars) {
            sum = sum + Integer.parseInt(c + "") * flag;
            flag--;
        }
        return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.parseInt(end);
    }

    /**
     * 验证香港身份证号码（存在Bug，部份特殊身份证无法检查）。
     * <p> 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58，
     * 前2位英文字符A-Z分别对应数字10-35，最后一位校验码为0-9的数字加上字符"A"，"A"代表10。
     *
     * <p> 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效。
     *
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    @SuppressLint("DefaultLocale")
    public static boolean validateHKCard(String idCard) {
        String card = idCard.replaceAll("[\\(|\\)]", "");
        int sum;
        int startNum = card.substring(0, 1).toUpperCase().toCharArray()[0];
        if (card.length() == 9) {
            sum = (startNum - 55) * 9 + ((int) card.substring(1, 2).toUpperCase().toCharArray()[0] - 55) * 8;
            card = card.substring(1, 9);
        } else {
            sum = 522 + (startNum - 55) * 8;
        }
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        int flag = 7;
        for (char c : chars) {
            sum = sum + Integer.parseInt(c + "") * flag;
            flag--;
        }
        if (end.equalsIgnoreCase("A")) {
            sum = sum + 10;
        } else {
            sum = sum + Integer.parseInt(end);
        }
        return sum % 11 == 0;
    }

    /**
     * 将字符数组转换成数字数组
     *
     * @param ca 字符数组
     * @return 数字数组
     */
    private static int[] convertCharToInt(char[] ca) {
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
     * @param arr
     * @return 身份证编码。
     */
    private static int getPowerSum(int[] arr) {
        int iSum = 0;
        int[] weight = getWeight();
        if (weight.length == arr.length) {
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < weight.length; j++) {
                    if (i == j) {
                        iSum = iSum + arr[i] * weight[j];
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
    private static String getCheckCode18(String id17) {
        // 中国二代身份证第18位校检码
        String[] verifyCode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int sum = 0;
        int[] weight = getWeight();
        for (int i = 0; i < id17.length(); i++) {
            sum = sum + Integer.parseInt(String.valueOf(id17.charAt(i))) * weight[i];
        }
        int mode = sum % 11;
        return verifyCode[mode];
    }

    /**
     * 根据身份编号获取年龄
     *
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        String format18 = format18(idCard);
        if (format18 == null) {
            return 0;
        }
        int thisYear = Integer.parseInt(format18.substring(6, 10));
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        return currYear - thisYear;
    }

    /**
     * 根据身份证号获取出生年月日
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        String format18 = format18(idCard);
        if (format18 == null) {
            return null;
        }
        return TextUtils.substring(format18, 6, 14);
    }

    /**
     * 根据身份证号获取出生年
     *
     * @param idCard 身份证号
     * @return 出生年份(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
        String format18 = format18(idCard);
        if (format18 == null) {
            return 0;
        }
        return Short.valueOf(TextUtils.substring(format18, 6, 10));
    }

    /**
     * 根据身份证号获取出生月
     *
     * @param idCard 身份证号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
        String format18 = format18(idCard);
        if (format18 == null) {
            return 0;
        }
        return Short.valueOf(TextUtils.substring(format18, 10, 12));
    }

    /**
     * 根据身份证号获取出生日.
     *
     * @param idCard 身份证号
     * @return 生日(dd)
     */
    public static Short getDayByIdCard(String idCard) {
        String format18 = format18(idCard);
        if (format18 == null) {
            return 0;
        }
        return Short.valueOf(TextUtils.substring(format18, 12, 14));
    }

    /**
     * 根据身份证号获取性别.
     *
     * @param idCard 身份证号
     * @return 性别（男、女）
     */
    public static String getGenderByIdCard(String idCard) {
        String format18 = format18(idCard);
        if (format18 == null) {
            return null;
        }
        String num17 = TextUtils.substring(format18, 16, 17);
        boolean result = Integer.parseInt(num17) % 2 != 0;
        return result ? "男" : "女";
    }

    /**
     * 根据身份编号获取户籍省份
     *
     * @param idCard 身份编码
     * @return 省级编码。
     */
    public static String getProvinceByIdCard(String idCard) {
        String format18 = format18(idCard);
        if (format18 == null) {
            return null;
        }
        return getCityCodes().get(format18.substring(0, 2));
    }

    /**
     * 数字验证
     *
     * @param val
     * @return 提取的数字。
     */
    public static boolean isNum(String val) {
        return val != null && !"".equals(val) && val.matches("^[0-9]*$");
    }

    /**
     * 验证小于当前日期 是否有效
     *
     * @param iYear  待验证日期(年)
     * @param iMonth 待验证日期(月 1-12)
     * @param iDate  待验证日期(日)
     * @return 是否有效
     */
    public static boolean valiDate(int iYear, int iMonth, int iDate) {
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
     *
     * @param idCard 身份证号码（15位或18位）
     * @return 如 110105 19900101 8888 返回 110105 ******** 8888
     */
    public static String formatHint(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return idCard;
        }
        if (idCard.length() == 15) {
            return String.format("%s ****** %s", idCard.substring(0, 6), idCard.substring(12));
        } else if (idCard.length() == 18) {
            return String.format("%s ******** %s", idCard.substring(0, 6), idCard.substring(14));
        } else {
            return idCard;
        }
    }

    /**
     * 转换为18位身份证号码
     *
     * @param idCard 身份证号码
     * @return 返回18位身份证号码，如果不规范，返回null
     */
    private static String format18(String idCard) {
        if (!TextUtils.isEmpty(idCard)) {
            int length = idCard.length();
            if (length == CHINA_ID_15) {
                return convertIDCard15To18(idCard);
            } else if (length == CHINA_ID_18) {
                if (checkIDCard18(idCard)) {
                    return idCard;
                }
            }
        }
        return null;
    }
}
