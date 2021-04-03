package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个整数数组，其中第 i 个元素代表了第 i 天的股票价格 。​
 * 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
 * - 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * - 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
 *
 * 此题和 {@link E122_Easy_BestTimeToBuyAndSellStockII} 类似，只不过多了冷冻期约束。
 *
 * 例 1：
 * 输入: [1,2,3,0,2]
 * 输出: 3
 * 解释: 对应的交易状态为: [买入, 卖出, 冷冻期, 买入, 卖出]
 */
public class E309_Medium_BestTimeToBuyAndSellStockWithCooldown {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{1,2,3,0,2}), 3);
        assertEquals(method.applyAsInt(new int[]{1,2,4}), 3);
    }

    /**
     * 题解框架和 {@link E122_Easy_BestTimeToBuyAndSellStockII} 类似。
     *
     * LeetCode 耗时：204 ms - 20.55%
     *          内存消耗：39.2 MB - 5.19%
     */
    public int maxProfit(int[] prices) {
        return maxProfit(prices, 0, new HashMap<>((int) (prices.length / 0.75)));
    }

    private int maxProfit(int[] prices, int lo, Map<Integer, Integer> memory) {
        if (lo >= prices.length - 1)
            return 0;
        int res = memory.getOrDefault(lo, -1);
        if (res != -1)
            return res;

        int curMin = prices[lo];
        for (int sell = lo + 1; sell < prices.length; sell++) {
            curMin = Math.min(curMin, prices[sell]);
            res = Math.max(res, prices[sell] - curMin + maxProfit(prices, sell + 2, memory));
        }
        memory.put(lo, res);

        return res;
    }

    @Test
    public void testMaxProfit() {
        test(this::maxProfit);
    }


    /**
     * 参见 {@link E188_Hard_BestTimeToBuyAndSellStockIV#compressMethod(int, int[])}。
     *
     * LeetCode 耗时：1 ms - 99.48%
     *          内存消耗：36.3 MB - 86.23%
     */
    public int compressMethod(int[] prices) {
        final int days = prices.length;
        // 使用 lastNoHold 表示上上次没有持有的状态
        int noHold = 0, hold = Integer.MIN_VALUE, lastNoHold = 0;
        for (int i = 0; i < days; i++) {
            // k 无穷，则 k 和 k - 1 是一样的。
            // 注意先保存 noHold，防止值被改变
            int tmp = noHold;
            noHold = Math.max(noHold, hold + prices[i]);
            // 等一天：dp[i][1] = max(dp[i-1][1], dp[i-2][0] - prices[i])
            // 第 i 天选择买入时，要从 i - 2 的状态转移（因为只有卖出才能买入，而卖出后需要等一天）
            hold = Math.max(hold, lastNoHold - prices[i]);
            lastNoHold = tmp;
        }

        return noHold;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
