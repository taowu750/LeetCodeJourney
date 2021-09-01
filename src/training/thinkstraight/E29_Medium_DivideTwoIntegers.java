package training.thinkstraight;

import org.junit.jupiter.api.Test;

import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 29. 两数相除: https://leetcode-cn.com/problems/divide-two-integers/
 *
 * 给定两个整数，被除数 dividend 和除数 divisor。将两数相除，要求不使用乘法、除法和 mod 运算符。
 * 返回被除数 dividend 除以除数 divisor 得到的商。
 *
 * 整数除法的结果应当截去（truncate）其小数部分，例如：truncate(8.345) = 8 以及 truncate(-2.7335) = -2
 *
 * 例 1：
 * 输入: dividend = 10, divisor = 3
 * 输出: 3
 * 解释: 10/3 = truncate(3.33333..) = truncate(3) = 3
 *
 * 例 2：
 * 输入: dividend = 7, divisor = -3
 * 输出: -2
 * 解释: 7/-3 = truncate(-2.33333..) = -2
 *
 * 约束：
 * - 被除数和除数均为 32 位有符号整数。
 * - 除数不为 0。
 * - 假设我们的环境只能存储 32 位有符号整数，其数值范围是 [−2^31, 2^31 − 1]。本题中，如果除法结果溢出，
 *   则返回 2^31 − 1。
 */
public class E29_Medium_DivideTwoIntegers {

    static void test(IntBinaryOperator method) {
        assertEquals(3, method.applyAsInt(10, 3));
        assertEquals(-2, method.applyAsInt(7, -3));
        assertEquals(Integer.MAX_VALUE, method.applyAsInt(Integer.MIN_VALUE, -1));
        assertEquals(0, method.applyAsInt(-1, 3));
        assertEquals(1, method.applyAsInt(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertEquals(-1, method.applyAsInt(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(0, method.applyAsInt(Integer.MAX_VALUE, Integer.MIN_VALUE));
        assertEquals(2, method.applyAsInt(Integer.MIN_VALUE, Integer.MIN_VALUE / 2));
        assertEquals(Integer.MIN_VALUE / 2, method.applyAsInt(Integer.MIN_VALUE, 2));
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：35.7 MB - 17.66%
     */
    public int divide(int dividend, int divisor) {
        // 都为最小值的特殊情况
        if (dividend == Integer.MIN_VALUE && divisor == Integer.MIN_VALUE) {
            return 1;
        }
        // 否则，divisor 为最小值，结果肯定为 0
        else if (divisor == Integer.MIN_VALUE) {
            return 0;
        }
        // dividend 为最小值，divisor 为 -1，这是唯一一种会溢出的情况
        else if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }

        // 如果 dividend 不为最小值（最小值的绝对值还是最小值），且绝对值小于 divisor 的绝对值，返回 0
        if (dividend != Integer.MIN_VALUE && Math.abs(dividend) < Math.abs(divisor)) {
            return 0;
        }

        int sign = (dividend > 0 && divisor > 0) || (dividend < 0 && divisor < 0) ? 1 : 0;
        // 统一转成负数，因为负数比正数多 1 个
        if (dividend > 0) {
            dividend = -dividend;
        }
        if (divisor > 0) {
            divisor = -divisor;
        }

        // dividend 的一半，divisor 小于它，那么商就只能为 1
        int limit = (dividend + 1) >> 1;
        int quotient = 0;
        do {
            // 不断倍增法
            int div = divisor, q = 1;
            while (div >= limit) {
                div <<= 1;
                q *= 2;
            }
            // 更新 dividend、limit
            dividend -= div;
            limit = (dividend + 1) >> 1;
            quotient += q;
        } while (dividend <= divisor);

        return sign == 1 ? quotient : -quotient;
    }

    @Test
    public void testDivide() {
        test(this::divide);
    }
}
