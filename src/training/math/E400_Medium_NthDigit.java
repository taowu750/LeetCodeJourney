package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 400. 第 N 位数字: https://leetcode-cn.com/problems/nth-digit/
 *
 * 在无限的整数序列 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...中找到第 n 位数字。
 * 注意：n 是正数且在 32 位整数范围内（n < 2**31）。
 *
 * 例 1：
 * 输入：3
 * 输出：3
 *
 * 例 2：
 * 输入：11
 * 输出：0
 * 解释：第 11 位数字在序列 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... 里是 0 ，它是 10 的一部分。
 */
public class E400_Medium_NthDigit {

    static void test(IntUnaryOperator method) {
        assertEquals(3, method.applyAsInt(3));
        assertEquals(0, method.applyAsInt(11));
        assertEquals(1, method.applyAsInt(28));
        assertEquals(5, method.applyAsInt(100));
        assertEquals(6, method.applyAsInt(123));
        assertEquals(1, method.applyAsInt(1000000000));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35 MB - 89.41%
     */
    public int findNthDigit(int n) {
        if (n < 10) {
            return n;
        }

        // 当前数字的位数
        int digit = 1;
        // 当前位数所有数字的长度，注意要用 long 防止数字溢出
        long digitAllSize;
        while (n >= (digitAllSize = 9 * (long) Math.pow(10, digit - 1) * digit)) {
            n -= digitAllSize;
            digit++;
        }
        // 如果 n 刚好为 0，说明到了最后当前位数最后一个数字，结尾必定是 9
        if (n == 0) {
            return 9;
        }

        // 计算在当前位数的数字位置
        int quotient = n / digit, remainder = n % digit;
        int base;
        // 根据余数分情况处理
        if (remainder > 0) {
            base = (int) Math.pow(10, digit - 1) + quotient;
            for (int i = 0; i < digit - remainder; i++) {
                base /= 10;
            }
        } else {
            base = (int) Math.pow(10, digit - 1) + quotient - 1;
        }

        return base % 10;
    }

    @Test
    public void testFindNthDigit() {
        test(this::findNthDigit);
    }
}
