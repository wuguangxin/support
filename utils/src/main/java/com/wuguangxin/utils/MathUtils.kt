package com.wuguangxin.utils

import java.math.BigDecimal

/**
 * 算术工具类（加减乘除）
 *
 * Created by wuguangxin on 17/5/15.
 */
object MathUtils {

    /**
     * 加
     * @param x 值1
     * @param y 值2
     * @return
     */
//    @JvmStatic fun add(x: Any?, y: Any?): BigDecimal = toBigDecimal(x).add(toBigDecimal(y))

    @JvmStatic fun add(vararg values: Any?): BigDecimal {
        return when {
            values.isEmpty() -> BigDecimal.ZERO
            values.size == 1 -> toBigDecimal(values[0])
            else -> {
                var result = BigDecimal.ZERO
                for (v in values) {
                    result = result.add(toBigDecimal(v))
                }
                result
            }
        }
    }

//    fun add(vararg v: BigDecimal?): BigDecimal? {
//        var result = BigDecimal.ZERO
//        for (bigDecimal in v) {
//            result = result.add(bigDecimal)
//        }
//        return result
//    }

    /**
     * 减
     * @param x 值1
     * @param y 值2
     * @return
     */
    @JvmStatic fun sub(x: Any?, y: Any?): BigDecimal = toBigDecimal(x).subtract(toBigDecimal(y))

    /**
     * 乘
     * @param x 值1
     * @param y 值2
     * @return
     */
    @JvmStatic fun mul(x: Any?, y: Any?): BigDecimal = toBigDecimal(x).multiply(toBigDecimal(y))

    /**
     * 除
     * @param x 值1
     * @param y 值2
     * @return
     */
    @JvmStatic fun div(x: Any?, y: Any?): BigDecimal = div(x, y, -1)

    /**
     * 除
     * @param x 值1
     * @param y 值2
     * @param scale 保留小数位数
     * @return
     */
    @JvmStatic fun div(x: Any?, y: Any?, scale: Int): BigDecimal = div(x, y, scale, -1)

    /**
     * 除
     * @param x 值1
     * @param y 值2
     * @param scale 保留小数位数 （-1表示不设置scale）
     * @param roundingMode 对结果的舍入模式 （-1表示不设置roundingMode，由系统默认）
     * @return
     */
    @JvmStatic fun div(x: Any?, y: Any?, scale: Int, roundingMode: Int): BigDecimal {
        val v1 = toBigDecimal(x)
        val v2 = toBigDecimal(y)
        if (v1.compareTo(BigDecimal.ZERO) == 0 || v2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO
        }
        return if (scale >= 0 && roundingMode >= 0) {
            v1.divide(v2, scale, roundingMode)
        } else {
            v1.divide(v2)
        }
    }

    /**
     * 把 value 转换成数值对象 BigDecimal
     */
    @JvmStatic
    fun toBigDecimal(value: Any?): BigDecimal = when {
        value == null  -> BigDecimal.ZERO
        value.toString().isEmpty() -> BigDecimal.ZERO
        value is BigDecimal -> value as BigDecimal
        value is Number -> BigDecimal(value.toString())
        value is String -> BigDecimal(value.toString())
        else -> throw TypeCastException("不能将value[Any]转为[BigDecimal]")
    }

    /**
     * 求余
     * @param dividend 被除数
     * @param divisor 除数
     * @return
     */
    @JvmStatic fun divAndRemainder(dividend: Any?, divisor: Any?): BigDecimal {
        val x = toBigDecimal(dividend);
        val y = toBigDecimal(divisor);
        if (y.compareTo(BigDecimal.ZERO) == 0 || y.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO
        }
        val results = x.divideAndRemainder(y)
        return if (results == null) BigDecimal.ZERO else results[1]
    }
}