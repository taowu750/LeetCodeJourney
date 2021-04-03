package training.dynamicprogramming;

import org.junit.jupiter.api.Test;
import training.greedy.E122_Easy_BestTimeToBuyAndSellStockII;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个整数数组 prices，其中第 i 个元素代表了第 i 天的股票价格；非负整数 fee 代表了交易股票的手续费用。
 * 你可以无限次地完成交易，但是你每笔交易都需要付手续费。如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
 * 返回获得利润的最大值。
 *
 * 注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费。
 *
 * 此题和 {@link E122_Easy_BestTimeToBuyAndSellStockII} 类似，只不过有了手续费用约束。
 *
 * 例 1：
 * 输入: prices = [1, 3, 2, 8, 4, 9], fee = 2
 * 输出: 8
 * 解释: 能够达到的最大利润:
 * 在此处买入 prices[0] = 1
 * 在此处卖出 prices[3] = 8
 * 在此处买入 prices[4] = 4
 * 在此处卖出 prices[5] = 9
 * 总利润: ((8 - 1) - 2) + ((9 - 4) - 2) = 8.
 *
 * 约束：
 * - 0 < prices.length <= 50000.
 * - 0 < prices[i] < 50000.
 * - 0 <= fee < 50000.
 */
public class E714_Medium_BestTimeToBuyAndSellStockWithTransactionFee {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(method.applyAsInt(new int[]{1, 3, 2, 8, 4, 9}, 2), 8);
        assertEquals(method.applyAsInt(new int[]{9,8,7,1,2}, 3), 0);
    }

    /**
     * 解题框架和 {@link E122_Easy_BestTimeToBuyAndSellStockII} 类似。
     *
     * 最后一个测试用例超出时间限制。
     */
    public int maxProfit(int[] prices, int fee) {
        return maxProfit(prices, fee, 0, new HashMap<>((int) (prices.length / 0.75)));
    }

    private int maxProfit(int[] prices, int fee, int lo, Map<Integer, Integer> memory) {
        if (lo >= prices.length - 1)
            return 0;
        int res = memory.getOrDefault(lo, -1);
        if (res != -1)
            return res;
        // 注意置 0，因为减去手续费后费用可能为负
        res = 0;

        int curMin = prices[lo];
        for (int sell = lo + 1; sell < prices.length; sell++) {
            curMin = Math.min(curMin, prices[sell]);
            res = Math.max(res, prices[sell] - curMin - fee + maxProfit(prices, fee, sell + 1, memory));
        }
        memory.put(lo, res);

        return res;
    }

    @Test
    public void testMaxProfit() {
        test(this::maxProfit);
    }
}
