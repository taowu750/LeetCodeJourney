package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定不同面额的硬币和一个总金额。写出函数来计算可以凑成总金额的硬币组合数。假设每一种面额的硬币有无限个。
 *
 * 例 1：
 * Input: amount = 5, coins = [1, 2, 5]
 * Output: 4
 * Explanation: 有四种方式可以凑成总金额
 * 5=5
 * 5=2+2+1
 * 5=2+1+1+1
 * 5=1+1+1+1+1
 *
 * 例 2：
 * Input: amount = 3, coins = [2]
 * Output: 0
 * Explanation: 只用面额 2 的硬币不能凑成总金额 3。
 *
 * 例 3：
 * Input: amount = 10, coins = [10]
 * Output: 1
 *
 * 约束：
 * - 0 <= amount (总金额) <= 5000
 * - 1 <= coin (硬币面额) <= 5000
 * - 硬币种类不超过 500 种
 * - 结果符合 32 位符号整数
 */
public class E518_Medium_CoinChangeII {

    static void test(ToIntBiFunction<Integer, int[]> method) {
        assertEquals(method.applyAsInt(5, new int[]{1,2,5}), 4);
        assertEquals(method.applyAsInt(3, new int[]{2}), 0);
        assertEquals(method.applyAsInt(10, new int[]{10}), 1);
        assertEquals(method.applyAsInt(0, new int[]{10}), 1);
    }

    /**
     * 参见 README
     */
    public int change(int amount, int[] coins) {
        if (amount == 0)
            return 1;

        final int n = coins.length;
        // dp[i][a] 表示使用前 i 个硬币凑出 a 的方法次数
        final int[][] dp = new int[n + 1][amount + 1];
        // amount 等于 0，则可以不使用硬币就凑出，因此有一种方法
        for (int i = 0; i <= n; i++)
            dp[i][0] = 1;

        for (int i = 1; i <= n; i++) {
            for (int a = 1; a <= amount; a++) {
                // 继承上一次的结果
                dp[i][a] += dp[i - 1][a];
                // 硬币面额小于容量，尝试将其添加进去。详解参见 README
                if (coins[i - 1] <= a)
                    dp[i][a] += dp[i][a - coins[i - 1]];

            }
        }

        return dp[n][amount];
    }

    @Test
    public void testChange() {
        test(this::change);
    }


    /**
     * LeetCode 耗时：4 ms - 57.63%
     *          内存消耗：35.8 MB - 79.98%
     */
    public int compressMethod(int amount, int[] coins) {
        if (amount == 0)
            return 1;

        final int n = coins.length;
        // 压缩行
        final int[] dp = new int[amount + 1];
        dp[0] = 1;

        for (int i = 1; i <= n; i++) {
            // 这次我们从左到右遍历。因为正序会重复选择物品，相当于物品数量无限，倒序不会重复拿物品，相当于每个物品只有一个。
            for (int a = 1; a <= amount; a++) {
                if (coins[i - 1] <= a)
                    dp[a] += dp[a - coins[i - 1]];
            }
        }

        return dp[amount];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
