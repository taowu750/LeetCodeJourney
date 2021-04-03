package training.dynamicprogramming;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。
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
}
