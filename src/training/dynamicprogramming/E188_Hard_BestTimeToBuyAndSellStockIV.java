package training.dynamicprogramming;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 188. 买卖股票的最佳时机 IV: https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/
 *
 * 给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
 *
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 *
 * 此题和 {@link E123_Hard_BestTimeToBuyAndSellStockIII} 类似。
 *
 * 例 1：
 * 输入：k = 2, prices = [2,4,1]
 * 输出：2
 * 解释：在第 1 天 (股票价格 = 2) 的时候买入，在第 2 天 (股票价格 = 4) 的时候卖出，这笔交易所能获得利润 = 4-2 = 2 。
 *
 * 例 2：
 * 输入：k = 2, prices = [3,2,6,5,0,3]
 * 输出：7
 * 解释：在第 2 天 (股票价格 = 2) 的时候买入，在第 3 天 (股票价格 = 6) 的时候卖出, 这笔交易所能获得利润 = 6-2 = 4 。
 *      随后，在第 5 天 (股票价格 = 0) 的时候买入，在第 6 天 (股票价格 = 3) 的时候卖出, 这笔交易所能获得利润 = 3-0 = 3 。
 *
 * 约束：
 * - 0 <= k <= 100
 * - 0 <= prices.length <= 1000
 * - 0 <= prices[i] <= 1000
 */
public class E188_Hard_BestTimeToBuyAndSellStockIV {

    static void test(ToIntBiFunction<Integer, int[]> method) {
        assertEquals(method.applyAsInt(2, new int[]{2,4,1}), 2);
        assertEquals(method.applyAsInt(2, new int[]{3,2,6,5,0,3}), 7);
    }

    /**
     * LeetCode 耗时：2366 ms - 6.33%
     *          内存消耗：47.9 MB - 5.12%
     */
    public int maxProfit(int k, int[] prices) {
        return maxProfit(prices, 0, k, new HashMap<>((int) (prices.length / 0.75) * k));
    }

    public int maxProfit(int[] prices, int lo, int k, Map<Pair<Integer, Integer>, Integer> memory) {
        if (lo >= prices.length - 1 || k == 0)
            return 0;
        Pair<Integer, Integer> cur = new Pair<>(lo, k);
        int res = memory.getOrDefault(cur, -1);
        if (res != -1)
            return res;

        int curMin = prices[lo];
        for (int sell = lo + 1; sell < prices.length; sell++) {
            curMin = Math.min(curMin, prices[sell]);
            res = Math.max(res, prices[sell] - curMin + maxProfit(prices, sell + 1, k - 1, memory));
        }
        memory.put(cur, res);

        return res;
    }

    @Test
    public void testMaxProfit() {
        test(this::maxProfit);
    }


    /**
     * 参见 README 8.7 节。
     *
     * LeetCode 耗时：9 ms - 38.56%
     *          内存消耗：40.9 MB - 20.82%
     */
    public int dpMethod(int k, int[] prices) {
        final int days = prices.length;
        // 三个状态：天数、当天允许交易的最大次数、当前的持有状态。存的是最大利润
        final int[][][] dp = new int[days + 1][k + 1][2];

        // base case
        for (int i = 0; i <= k; i++) {
            dp[0][i][1] = Integer.MIN_VALUE;
        }
        for (int i = 0; i <= days; i++) {
            dp[i][0][1] = Integer.MIN_VALUE;
        }

        for (int i = 1; i <= days; i++) {
            for (int j = 1; j <= k; j++) {
                // 从（昨天没有持有，今天不操作，还是没有持有）和（昨天持有，今天卖出了，所以今天没有持有了）中选择最大的
                dp[i][j][0] = Math.max(dp[i - 1][j][0], dp[i - 1][j][1] + prices[i - 1]);
                // 从（昨天持有，今天不操作，继续持有）和（昨天没有持有，今天买了，所以今天持有了）中选择最大的
                dp[i][j][1] = Math.max(dp[i - 1][j][1], dp[i - 1][j - 1][0] - prices[i - 1]);
            }
        }

        return dp[days][k][0];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * 状态压缩。
     *
     * LeetCode 耗时：5 ms - 85.32%
     *          内存消耗：36.3 MB - 65.68%
     */
    public int compressMethod(int k, int[] prices) {
        final int days = prices.length;
        // 压缩行
        final int[][] dp = new int[k + 1][2];
        for (int i = 0; i <= k; i++) {
            dp[i][1] = Integer.MIN_VALUE;
        }

        for (int i = 1; i <= days; i++) {
            // 注意从右到左，因为后一状态依赖前面的状态
            for (int j = k; j >= 1; j--) {
                dp[j][0] = Math.max(dp[j][0], dp[j][1] + prices[i - 1]);
                dp[j][1] = Math.max(dp[j][1], dp[j - 1][0] - prices[i - 1]);
            }
        }

        return dp[k][0];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
