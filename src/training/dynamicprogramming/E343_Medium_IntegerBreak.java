package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 343. 整数拆分: https://leetcode-cn.com/problems/integer-break/
 *
 * 给定一个正整数 n，将其拆分为「至少」两个正整数的和，并使这些整数的乘积最大化。
 * 返回你可以获得的最大乘积。
 *
 * 例 1：
 * 输入: 2
 * 输出: 1
 * 解释: 2 = 1 + 1, 1 × 1 = 1。
 *
 * 例 2：
 * 输入: 10
 * 输出: 36
 * 解释: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36。
 *
 * 约束：
 * - 你可以假设 n 不小于 2 且不大于 58。
 */
public class E343_Medium_IntegerBreak {

    static void test(IntUnaryOperator method) {
        assertEquals(1, method.applyAsInt(2));
        assertEquals(36, method.applyAsInt(10));
        assertEquals(729, method.applyAsInt(18));
    }

    /**
     * 有些类似于完全背包问题。
     *
     * 对于的正整数 n，当 n ≥ 2 时，可以拆分成至少两个正整数的和。令 k 是拆分出的第一个正整数，
     * 则剩下的部分是 n-k，n-k 可以不继续拆分，或者继续拆分成至少两个正整数的和。
     *
     * 当 i ≥ 2 时，假设对正整数 i 拆分出的第一个正整数是 j（1≤ j < i），则有以下两种方案：
     * - 将 i 拆分成 j 和 i-j 的和，且 i-j 不再拆分成多个正整数，此时的乘积是 j×(i−j)；
     * - 将 i 拆分成 j 和 i−j 的和，且 i−j 继续拆分成多个正整数，此时的乘积是 j×dp[i−j]。
     *
     * 参见：
     * https://leetcode-cn.com/problems/integer-break/solution/zheng-shu-chai-fen-by-leetcode-solution/
     *
     * LeetCode 耗时：2 ms - 12.74%
     *          内存消耗：35.3 MB - 23.42%
     */
    public int integerBreak(int n) {
        // dp[i][j] 表示前 i 个数中和等于 j 的最大乘积。
        // 其中 i < j，且前 i 个数每个数可以取任意个
        int[][] dp = new int[n][n + 1];

        for (int j = 2; j <= n; j++) {
            dp[1][j] = 1;
        }
        for (int i = 2; i < n; i++) {
            for (int j = 1; j <= i; j++) {
                dp[i][j] = 1;
            }
        }

        for (int i = 2; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                dp[i][j] = Math.max(dp[i - 1][j],
                        Math.max(dp[i][j - i] * i,
                                i * (j - i)));
            }
        }

        return dp[n - 1][n];
    }

    @Test
    public void testIntegerBreak() {
        test(this::integerBreak);
    }


    /**
     * LeetCode 耗时：2 ms - 12.74%
     *          内存消耗：35.3 MB - 23.42%
     */
    public int compressMethod(int n) {
        int[] dp = new int[n + 1];

        for (int j = 2; j <= n; j++) {
            dp[j] = 1;
        }

        for (int i = 2; i < n; i++) {
            for (int k = 1; k <= i; k++) {
                dp[k] = 1;
            }
            for (int j = i + 1; j <= n; j++) {
                dp[j] = Math.max(dp[j], Math.max(dp[j - i] * i, i * (j - i)));
            }
        }

        return dp[n];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }


    /**
     * 数学方法，参见：
     * https://leetcode-cn.com/problems/integer-break/solution/343-zheng-shu-chai-fen-tan-xin-by-jyd/
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：34.9 MB - 93.47%
     */
    public int mathMethod(int n) {
        if (n <= 3) {
            return n - 1;
        }

        int quotient = n / 3, remainder = n % 3;
        if (remainder == 0) {
            return (int) Math.pow(3, quotient);
        } else if (remainder == 1) {
            return (int) (Math.pow(3, quotient - 1) * 4);
        } else {
            return (int) (Math.pow(3, quotient) * 2);
        }
    }

    @Test
    public void testMathMethod() {
        test(this::mathMethod);
    }
}
