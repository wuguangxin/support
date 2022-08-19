package com.wuguangxin.utils;

/**
 * 斐波那契数列。前两个数固定为1，第三位及以后为前两位之和：
 * <br>(1 1 2 3 5 8 13 21)
 * <p>Created by wuguangxin on 2020-05-28.
 */
public class Fibonacci {

    /**
     * 获取第n项斐波那契数列
     * @param n 第n项
     * @return
     */
    public static int fibonacci(int n) {
        if (n == 1 || n == 2) {
            return 1;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
}
