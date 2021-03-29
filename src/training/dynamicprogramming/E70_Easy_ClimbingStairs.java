package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 你在爬楼梯。它需要 n 步才能到达顶端。每次你可以爬 1 或 2 步。你能用多少种不同的方法爬到山顶？
 *
 * 例 1：
 * Input: n = 2
 * Output: 2
 * Explanation:
 * 1. 1 step + 1 step
 * 2. 2 steps
 *
 * 例 2：
 * Input: n = 3
 * Output: 3
 * Explanation:
 * 1. 1 step + 1 step + 1 step
 * 2. 1 step + 2 steps
 * 3. 2 steps + 1 step
 *
 * 约束：
 * - 1 <= n <= 45
 */
public class E70_Easy_ClimbingStairs {

    static void test(IntUnaryOperator method) {
        assertEquals(method.applyAsInt(1), 1);
        assertEquals(method.applyAsInt(2), 2);
        assertEquals(method.applyAsInt(3), 3);
        assertEquals(method.applyAsInt(4), 5);
    }

    public int climbStairs(int n) {
        // dp[i] 表示爬 n 阶楼梯的方法数。通过子问题分解易知 dp[i] = dp[i - 1] + dp[i - 2]
        int[] dp = new int[n + 1];
        dp[0] = dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[n];
    }

    @Test
    public void testClimbStairs() {
        test(this::climbStairs);
    }


    /**
     * 状态压缩。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35 MB - 90.68%
     */
    public int compressMethod(int n) {
        int i = 1, j = 1;

        for (int k = 2; k <= n; k++) {
            int next = i + j;
            i = j;
            j = next;
        }

        return j;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
