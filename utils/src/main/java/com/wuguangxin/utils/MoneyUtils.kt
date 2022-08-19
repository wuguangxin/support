package com.wuguangxin.utils

import com.wuguangxin.utils.ext.toBigDecimal
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * 金额格式化工具类
 *
 * Created by wuguangxin on 14/6/2
 */
object MoneyUtils {
    /**
     * BigDecimal bd = new BigDecimal(123456789);
     *
     * System.out.println(formatString(",###,###", bd)); 	  //out: 123,456,789
     * System.out.println(formatString("##,####,###", bd));  //out: 123,456,789
     * System.out.println(formatString("######,###", bd));	  //out: 123,456,789
     * System.out.println(formatString("#,##,###,###", bd)); //out: 123,456,789
     * System.out.println(formatString(",###,###.00", bd));  //out: 123,456,789.00
     * System.out.println(formatString(",###,##0.00", bd));  //out: 123,456,789.00
     * BigDecimal bd1 = new BigDecimal(0);
     * System.out.println(formatString(",###,###", bd1)); 	  //out: 0
     * System.out.println(formatString(",###,###.00", bd1)); //out: .00
     * System.out.println(formatString(",###,##0.00", bd1)); //out: 0.00
     */
    private const val REG = "######.######"

    /**
     * 格式化金额（小数点后的0会去掉，如1.0，返回1，1.010，返回1.01）
     * @param value 字符串金额
     * @return
     */
    @JvmStatic
    fun format(value: String): String {
        return value.toBigDecimal().toString()
    }

    /**
     * 格式化金额
     * @param value 金额
     * @param suffix 后缀，比如"元"
     * @return
     */
    @JvmStatic
    fun format(value: String, suffix: String): String {
        return format(value.toBigDecimal(), suffix)
    }

    /**
     * 格式化金额
     *
     * @param value 金额
     * @param suffix 后缀，比如"元"
     * @return
     */
    @JvmStatic
    fun format(value: Number, suffix: String): String {
        return DecimalFormat(REG).format(value) + suffix
    }

    /**
     * 格式化金额
     *
     * @param value 数值
     * @param suffix 后缀，比如"元"
     * @param def 如果格式化后的金额为0，则返回该值
     * @return
     */
    @JvmStatic
    fun format(value: Number, suffix: String, def: String): String {
        return if (value == 0) def else DecimalFormat(REG).format(value) + suffix
    }

    /**
     * 格式化金额
     *
     * @param value 字符串数值
     * @param unit 附加文字，比如"元"
     * @param def 如果格式化后的金额为0，则返回该值
     * @return
     */
    @JvmStatic
    fun format(value: String, unit: String, def: String): String {
        val bd = value.toBigDecimal()
        return if (bd == BigDecimal.ZERO) def else DecimalFormat(REG).format(bd) + unit
    }

    /**
     * 去小数.00 或 .0（比如1.90，清除后是1.9）
     *
     * @param value 数值字符串
     * @return
     */
    @JvmStatic
    fun clearZero(value: String?): String = if (value == null || value.isEmpty()) "0" else
        DecimalFormat("#########.#########").format(BigDecimal(value))

    /**
     * 将数字型货币转换为中文型货币
     *
     * @param number 数字
     * @return 金额字符串
     */
    @JvmStatic
    fun formatCN(number: Number): String {
        if (number.toDouble() == 0.0) {
            return "零元整"
        }
        val digit = charArrayOf('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖')
        val unit = "仟佰拾兆仟佰拾亿仟佰拾万仟佰拾元角分"
        // 注意 如果是0.01，这里会处理为 .01，千万别格式化为 0.01，即reg不能写为 "#.00"
        var s = DecimalFormat("#.00").format(number)
        s = s.replace("\\.".toRegex(), "")
        val l = unit.length
        var sb = StringBuffer(unit)
        for (i in s.length - 1 downTo 0) {
            sb = sb.insert(l - s.length + i, digit[s[i].toInt() - 0x30])
        }
        s = sb.substring(l - s.length, l + s.length)
        s = s.replace("零[拾佰仟]".toRegex(), "零").replace("零{2,}".toRegex(), "零")
            .replace("零([兆万元])".toRegex(), "$1").replace("零[角分]".toRegex(), "")
        if (s.endsWith("角")) {
            s += "零分"
        }
        if (!s.contains("角") && !s.contains("分") && s.contains("元")) {
            s += "整"
        }
        if (s.contains("分") && !s.contains("整") && !s.contains("角")) {
            s = s.replace("元", "元零")
        }
        return s
    }

    /**
     * 判断是否为0
     * @param value
     * @return
     */
    private fun isZero(v: Number?=0): Boolean {
        return v == 0
    }


    /**
     * 计算收益
     * @param money 本金
     * @param income 利率
     * @param duration 期限
     * @param ratio 系数，如果是按年算，则为当年总天数，按月算，则为12（个月）
     * @return
     */
    @JvmStatic
    fun getProfit(
        money: BigDecimal ?= BigDecimal.ZERO,
        income: BigDecimal ?= BigDecimal.ZERO,
        duration: Int,
        ratio: Int): BigDecimal? {

        return if (money?.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal.ZERO
        } else {
            money?.multiply(income)
                ?.multiply(BigDecimal.valueOf(duration.toLong()))
                ?.divide(BigDecimal.valueOf(ratio * 100L), 2, BigDecimal.ROUND_DOWN)
        }
    }
}