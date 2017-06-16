package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字格式化工具类
 *
 * <p>Created by wuguangxin on 14/6/2 </p>
 */
public class NumberUtils {
    private static DecimalFormat format = new DecimalFormat();

    /**
     * 去小数后面的.00 或 .0
     * @param number
     * @return
     */
    public static String replaceZero(Number number) {
        if(number == null) {
            return "0";
        }
        String s = number.toString();
        if(TextUtils.isEmpty(s)) {
            return "0";
        }
        if(s.startsWith(".")) s = "0" + s;
        if(s.endsWith(".00")) s = s.replace(".00", "");
        if(s.endsWith(".0")) s = s.replace(".0", "");
        return s;
    }

    /**
     * 格式化数字（不千分位，如 1234567.89）
     * @param number Number类型
     * @param bit 小数位数（注意将会四舍五入）
     * @return
     */
    public static String format(Number number, int bit) {
        return getDecimalFormat(false, bit).format(number);
    }

    /**
     * 格式化数字（不千分位，如 1234567.89）
     * @param number
     * @param bit 小数位数（注意将会四舍五入）
     * @return
     */
    public static String format(BigDecimal number, int bit) {
        return getDecimalFormat(false, bit).format(number);
    }

    /**
     * 格式化收益率(不带%符号)
     * @param number 数值 (0-100)
     * @param bit 小数位数（注意将会四舍五入）
     * @return
     */
    public static String formatIncome(Number number, int bit) {
        return formatIncomeSign(number, bit).replace("%", "");
    }

    /**
     * 格式化收益率(带%符号)
     *
     * @param number 数值 (0-100)
     * @param bit 小数位数（注意将会四舍五入）
     * 1. CEILING 		如果BigDecimal是正的，则做ROUND_UP操作；如果为负，则做ROUND_DOWN操作。
     * 2. DOWN 		    从不在舍弃(即截断)的小数之前增加数字。
     * 3. FLOOR 		如果BigDecimal为正，则作UP；如果为负，则作DOWN。
     * 4. HALF_DOWN 	若舍弃部分大于0.5，则作UP；否则，作DOWN。
     * 5. HALF_EVEN 	如果舍弃部分左边的数字为奇数，则作HALF_UP；如果它为偶数，则作   HALF_DOWN   。
     * 6. HALF_UP 		若舍弃部分大于0.5，则作UP；否则，作DOWN。
     * 7. UNNECESSARY 	该“伪舍入模式”实际是指明所要求的操作必须是精确的，，因此不需要舍入操作。
     * 8. UP 			总是在非0舍弃小数(即截断)之前增加数字。
     * @return
     */
    public static String formatIncomeSign(Number number, int bit) {
        if (bit < 0) bit = 0;
        if (number == null) number = 0;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(bit); // 小数保留到几位  显示：47.00%
        return nf.format(number.floatValue() / 100.0f).replace(".00", "").replace(".0", "");
    }

    private static DecimalFormat getDecimalFormat(boolean isSpace, int bit) {
        format.applyPattern(getReg(isSpace, bit));
        return format;
    }

    /*
    * BigDecimal bd = new BigDecimal(123456789);
    * System.out.println(format(",###,###", bd)); 		// 123,456,789
    * System.out.println(format("##,####,###", bd));    // 123,456,789
    * System.out.println(format("######,###", bd));     // 123,456,789
    * System.out.println(format("#,##,###,###", bd));   // 123,456,789
    * System.out.println(format(",###,###.00", bd));    // 123,456,789.00
    * System.out.println(format(",###,##0.00", bd));    // 123,456,789.00
    * BigDecimal bd1 = new BigDecimal(0);
    * System.out.println(format(",###,###", bd1));      // 0
    * System.out.println(format(",###,###.00", bd1));   // .00
    * System.out.println(format(",###,##0.00", bd1));   // 0.00
     */
    private static String getReg(boolean isSpace, int bit) {
        switch (bit) {
        case 0:
            return isSpace ? ",###,##0" : ",#####0";
        case 1:
            return isSpace ? ",###,##0.0" : "#####0.0";
        case 2:
            return isSpace ? ",###,##0.00" : "#####0.00";
        case 3:
            return isSpace ? ",###,##0.000" : "#####0.000";
        case 4:
            return isSpace ? ",###,##0.0000" : "#####0.0000";
        default:
            return isSpace ? ",###,##0.00" : "#####0.00";
        }
    }

    /**
     * 获取编号
     * @param number 当前编号
     * @param bit 编号位数
     * @return 如id=2，bit=5，则返回00002
     */
    public static String getNumberId(int number, int bit) {
        String reg = "%0" +bit+ "d%n";
        return String.format(reg, number);
    }

    // #########################################################
    // #################  百分比  #################################

    /**
     * 格式化为百分比。默认保留2位小数，默认舍入模式为 RoundingMode.HALF_UP
     *
     * @param num (0-1)
     * @return
     */
    public static String formatPercent(Number num) {
        return formatPercent(num, 2, RoundingMode.HALF_UP);
    }

    /**
     * 格式化为百分比。默认保留2位小数
     *
     * @param num 数值  (0-1)
     * @param roundingMode 舍入模式
     * @return
     */
    public static String formatPercent(Number num, RoundingMode roundingMode) {
        return formatPercent(num, 2, roundingMode);
    }

    /**
     * 格式化为百分比。默认舍入模式为 RoundingMode.HALF_UP
     *
     * @param num 数值 (0-1)
     * @param maximumFractionDigits 小数位数
     * @return
     */
    public static String formatPercent(Number num, int maximumFractionDigits) {
        return formatPercent(num, maximumFractionDigits, RoundingMode.HALF_UP);
    }

    /**
     * 格式化为百分比
     *
     * @param num 数值  (0-1)
     * @param maximumFractionDigits 小数位数
     * @param roundingMode 舍入模式
     * @return
     */
    @SuppressLint("NewApi")
    public static String formatPercent(Number num, int maximumFractionDigits, RoundingMode roundingMode) {
        return format(num, maximumFractionDigits, roundingMode, NumberFormat.getPercentInstance());
    }

    // #########################################################
    // ########################   货币格式  ########################

    /**
     * 格式化为货币格式。默认保留2位小数，默认舍入模式为 RoundingMode.HALF_UP
     *
     * @param num
     * @return
     */
    public static String formatCurrency(Number num) {
        return formatPercent(num, 2, RoundingMode.HALF_UP);
    }

    /**
     * 格式化为货币格式。默认保留2位小数
     *
     * @param num 数值
     * @param roundingMode 舍入模式
     * @return
     */
    public static String formatCurrency(Number num, RoundingMode roundingMode) {
        return formatPercent(num, 2, roundingMode);
    }

    /**
     * 格式化为货币格式。默认舍入模式为 RoundingMode.HALF_UP
     *
     * @param num 数值
     * @param maximumFractionDigits 小数位数
     * @return
     */
    public static String formatCurrency(Number num, int maximumFractionDigits) {
        return formatPercent(num, maximumFractionDigits, RoundingMode.HALF_UP);
    }

    /**
     * 格式化为货币格式
     *
     * @param num 数值
     * @param maximumFractionDigits 小数位数
     * @param roundingMode 舍入模式
     * @return
     */
    @SuppressLint("NewApi")
    public static String formatCurrency(Number num, int maximumFractionDigits, RoundingMode roundingMode) {
        return format(num, maximumFractionDigits, roundingMode, NumberFormat.getCurrencyInstance());
    }

    /**
     * 格式化
     *
     * @param num 数值
     * @param maximumFractionDigits 小数位数
     * @param roundingMode 舍入模式 （看 http://wenku.baidu.com/link?url=wqskKRDCuep020rIJcb04qbIIP59FtoS51LIvSDQK6Vah0-CfIRW0BEs2NyIgrGjYY6GUcc8v2Uz4MoCnWsbc0SC73gMyETxzwmeyRmcGAW）
     * @param numberFormat 格式化类型
     * @return
     */
    @SuppressLint("NewApi")
    public static String format(Number num, int maximumFractionDigits, RoundingMode roundingMode, NumberFormat numberFormat) {
        if (numberFormat != null && num != null) {
            numberFormat.setMaximumFractionDigits(maximumFractionDigits);
            if (roundingMode != null) {
                numberFormat.setRoundingMode(roundingMode);
            }
            return numberFormat.format(num);
        }
        return null;
    }
}