package training.thinkstraight;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 剑指 Offer 64. 求1+2+…+n: https://leetcode-cn.com/problems/qiu-12n-lcof/
 *
 * 求 1+2+...+n ，要求不能使用乘除法、for、while、if、else、switch、case等关键字及条件判断语句（A?B:C）。
 *
 * 例 1：
 * 输入: n = 3
 * 输出: 6
 *
 * 例 2：
 * 输入: n = 9
 * 输出: 45
 *
 * 约束：
 * - 1 <= n <= 10000
 */
public class Offer64_Medium_ArithmeticSequence {

    static void test(IntUnaryOperator method) {
        assertEquals(6, method.applyAsInt(3));
        assertEquals(45, method.applyAsInt(9));
    }

    /**
     * 利用逻辑操作符的短路特性来做。
     *
     * LeetCode 耗时：1 ms - 61.14%
     *          内存消耗：35.6 MB - 67.41%
     */
    public int sumNums(int n) {
        boolean flag = n > 0 && (n += sumNums(n - 1)) > 0;
        return n;
    }

    @Test
    public void testSumNums() {
        test(this::sumNums);
    }
}
