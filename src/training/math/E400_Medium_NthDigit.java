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

    public static void test(IntUnaryOperator method) {
        assertEquals(3, method.applyAsInt(3));
        assertEquals(0, method.applyAsInt(11));
        assertEquals(1, method.applyAsInt(28));
        assertEquals(5, method.applyAsInt(100));
        assertEquals(6, method.applyAsInt(123));
        assertEquals(1, method.applyAsInt(1000000000));
    }

    /**
     * 1 位数字有 9 个，2 位数字有 90 个，3 位数字有 900 个，...
     * 根据这个可以确定 n 在哪位数字范围中。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35 MB - 89.41%
     */
    public int findNthDigit(int n) {
        if (n <= 9) {
            return n;
        }

        // 当前数字的位数
        int digit = 1;
        // weight 表示当前位数数字的个数
        long weight = 9;
        // 计算 n 在哪个位数范围内，并且减去之前所有位数数字的长度
        do {
            n -= weight * digit;
            weight *= 10;
            digit++;
        } while (n > weight * digit);

        // base 表示 n 所在的数字
        int base = (int) (n / digit + weight / 9 - 1), remainder = n % digit;
        // 如果余数等于 0，表示 base 的末尾恰好就是第 n 位数字
        if (remainder == 0) {
            return base % 10;
        }
        // 余数不等于 0，则 n 在 base + 1 中，并且是它的第 remainder 位数字
        for (base++, remainder = digit - remainder; remainder > 0; remainder--) {
            base /= 10;
        }

        return base % 10;
    }

    @Test
    public void testFindNthDigit() {
        test(this::findNthDigit);
    }
}
