package training.number_code;

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

    public static void test(IntBinaryOperator method) {
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
        // 唯一一种会溢出的方法
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        // 注意 abs(MIN_VALUE) 仍然为 MIN_VALUE，所以要特殊处理
        if (dividend != Integer.MIN_VALUE && divisor == Integer.MIN_VALUE) {
            return 0;
        }
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

        // 倍增法
        int ans = 0;
        do {
            int div = divisor, factor = 1;
            do {
                dividend -= div;
                ans += factor;
                // 防止 div 溢出，也可以用 long
                if (div + div < div) {
                    div += div;
                    factor += factor;
                }
            } while (div >= dividend);
        } while (divisor >= dividend);

        return sign * ans;
    }

    @Test
    public void testDivide() {
        test(this::divide);
    }
}
