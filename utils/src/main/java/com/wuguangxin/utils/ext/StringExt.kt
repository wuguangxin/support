package com.wuguangxin.utils.ext

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 *
 *
 * Created by wuguangxin on 2021/10/30.
 */

/**
 * 将符合数值格式的对象格式化为 BigDecimal 对象
 * @return
 */
fun Any?.toBigDecimal(): BigDecimal {
    return try {
        when (this) {
            is Number -> BigDecimal(toString())
            is String -> BigDecimal(this.replace(",".toRegex(), ""))
            else -> BigDecimal.ZERO
        }
    } catch (e: Exception) {
        BigDecimal.ZERO
    }
}

fun BigDecimal.format(maximumFractionDigits: Int): BigDecimal {
    return setScale(maximumFractionDigits, RoundingMode.HALF_UP)
}