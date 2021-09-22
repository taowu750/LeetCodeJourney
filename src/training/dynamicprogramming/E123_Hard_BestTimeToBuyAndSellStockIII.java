package training.dynamicprogramming;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 123. 买卖股票的最佳时机 III: https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/
 *
 * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成「两笔」交易。
 * 此题和 {@link E122_Medium_BestTimeToBuyAndSellStockII} 类似，只不过限制了交易次数为 2 次。
 *
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 *
 * 例 1：
 * 输入：prices = [3,3,5,0,0,3,1,4]
 * 输出：6
 * 解释：在第 4 天（股票价格 = 0）的时候买入，在第 6 天（股票价格 = 3）的时候卖出，这笔交易所能获得利润 = 3-0 = 3。
 *      随后，在第 7 天（股票价格 = 1）的时候买入，在第 8 天 （股票价格 = 4）的时候卖出，这笔交易所能获得利润 = 4-1 = 3。
 *
 * 例 2：
 * 输入：prices = [1,2,3,4,5]
 * 输出：4
 * 解释：在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。  
 *      注意你不能在第 1 天和第 2 天接连购买股票，之后再将它们卖出。  
 *      因为这样属于同时参与了多笔交易，你必须在再次购买前出售掉之前的股票。
 *
 * 例 3：
 * 输入：prices = [7,6,4,3,1]
 * 输出：0
 * 解释：在这个情况下, 没有交易完成, 所以最大利润为 0。
 *
 * 例 4：
 * 输入：prices = [1]
 * 输出：0
 *
 * 约束：
 * - 1 <= prices.length <= 10**5
 * - 0 <= prices[i] <= 10**5
 */
public class E123_Hard_BestTimeToBuyAndSellStockIII {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{3,3,5,0,0,3,1,4}), 6);
        assertEquals(method.applyAsInt(new int[]{1,2,3,4,5}), 4);
        assertEquals(method.applyAsInt(new int[]{7,6,4,3,1}), 0);
        assertEquals(method.applyAsInt(new int[]{1}), 0);
    }

    /**
     * 套用和 {@link E122_Medium_BestTimeToBuyAndSellStockII} 一样的框架。
     *
     * 在最后一个测试样例超时。
     */
    public int maxProfit(int[] prices) {
        return maxProfit(prices, 0, 2, new HashMap<>((int) (prices.length / 0.75) * 2));
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
     * 参见 {@link E188_Hard_BestTimeToBuyAndSellStockIV#compressMethod(int, int[])}。
     *
     * LeetCode 耗时：5 ms - 81.07%
     *          内存消耗：54.3 MB - 58.33%
     */
    public int compressMethod(int[] prices) {
        final int days = prices.length;
        // 压缩行
        final int[][] dp = new int[2 + 1][2];
        for (int i = 0; i <= 2; i++) {
            dp[i][1] = Integer.MIN_VALUE;
        }

        for (int i = 1; i <= days; i++) {
            // 注意从右到左，因为后一状态依赖前面的状态
            for (int j = 2; j >= 1; j--) {
                dp[j][0] = Math.max(dp[j][0], dp[j][1] + prices[i - 1]);
                dp[j][1] = Math.max(dp[j][1], dp[j - 1][0] - prices[i - 1]);
            }
        }

        return dp[2][0];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}