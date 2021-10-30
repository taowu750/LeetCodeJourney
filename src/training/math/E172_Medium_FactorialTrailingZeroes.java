package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 172. 阶乘后的零: https://leetcode-cn.com/problems/factorial-trailing-zeroes/
 *
 * 给定一个整数 n，返回 n! 结果尾数中零的数量。
 *
 * 你算法的时间复杂度应为 O(log n) 。
 *
 * 例 1：
 * 输入: 3
 * 输出: 0
 * 解释: 3! = 6, 尾数中没有零。
 *
 * 例 2：
 * 输入: 5
 * 输出: 1
 * 解释: 5! = 120, 尾数中有 1 个零.
 */
public class E172_Medium_FactorialTrailingZeroes {

    public static void test(IntUnaryOperator method) {
        assertEquals(0, method.applyAsInt(3));
        assertEquals(1, method.applyAsInt(5));
        assertEquals(6, method.applyAsInt(25));
        assertEquals(8, method.applyAsInt(37));
    }

    /**
     * 一个事实：有多少个 5 末尾就有多少个 0。因为乘法满足交换律，而阶乘中其他偶数和 5 相乘会等于 0。
     * 并且因为 25 = 5 * 5，所以它算两个 5，其他的数类似。
     *
     * 它的时间复杂度是底数为 5 的对数级，也就是O(logN)
     *
     * LeetCode 耗时：1 ms - 93.86%
     *          内存消耗：35.3 MB - 67.66%
     */
    public int trailingZeroes(int n) {
        int result = 0;
        while (n > 1) {
            result += n / 5;
            n /= 5;
        }

        return result;
    }

    @Test
    public void testTrailingZeroes() {
        test(this::trailingZeroes);
    }
}
