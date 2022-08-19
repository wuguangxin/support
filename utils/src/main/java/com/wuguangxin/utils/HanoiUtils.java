package com.wuguangxin.utils;

/**
 * 汉诺塔问题
 * Created by wuguangxin on 2020-05-28.
 */
public class HanoiUtils {
    public StringBuffer text = new StringBuffer(); // 打印日志文本
    public int countSetup; // 总步骤数

    /**
     * 递归解决汉诺塔问题
     * @param n 共有n个盘子
     * @param A 开始柱子
     * @param B 中间柱子
     * @param C 目标柱子
     */
    public void hanoi(int n, char A, char B, char C) {
        if (n == 1) {
            // 只有一个盘子
            String text1 = "第" + ++countSetup + "步：把第1个盘子从" + A + "移到" + C;
            text.append(text1).append("msg =");
            System.out.println("text1 = " + text1);
        } else {
            // 无论多少个盘子，都认为只有两个，最底下的一个盘子，和他上面的所有盘子。
            // 移动上面所有的盘子到中间位子
            hanoi(n - 1, A, C, B);

            // 移动下面的盘子
            String text2 = "第" + ++countSetup + "步：把第" + n + "个盘子从" + A + "移到" + C;
            text.append(text2).append("\n");
            System.out.println("text2 = " + text2);

            // 把上面的所有盘子从中间位置移到目标位子
            hanoi(n - 1, B, A, C);
        }
    }
}
