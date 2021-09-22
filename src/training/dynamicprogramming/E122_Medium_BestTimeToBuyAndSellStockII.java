package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 122. 买卖股票的最佳时机 II: https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/
 *
 * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
 * 此题类似于 {@link E121_Easy_BestTimeToBuyAndSellStock}，不过可以进行多次买卖。
 * <p>
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p>
 * 例 1：
 * 输入: [7,1,5,3,6,4]
 * 输出: 7
 * 解释: 在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4。
 *      随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 这笔交易所能获得利润 = 6-3 = 3。
 * <p>
 * 例 2：
 * 输入: [1,2,3,4,5]
 * 输出: 4
 * 解释: 在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。
 *      注意你不能在第 1 天和第 2 天接连购买股票，之后再将它们卖出。因为这样属于同时参与了多笔交易，你必须在再次购买前出售掉之前的股票。
 * <p>
 * 例 3：
 * 输入: [7,6,4,3,1]
 * 输出: 0
 * 解释: 在这种情况下, 没有交易完成, 所以最大利润为 0。
 * <p>
 * 约束：
 * - 1 <= prices.length <= 3 * 10**4
 * - 0 <= prices[i] <= 10**4
 */
public class E122_Medium_BestTimeToBuyAndSellStockII {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{7, 1, 5, 3, 6, 4}), 7);
        assertEquals(method.applyAsInt(new int[]{1, 2, 3, 4, 5}), 4);
        assertEquals(method.applyAsInt(new int[]{7, 6, 4, 3, 1}), 0);
        assertEquals(method.applyAsInt(new int[]{6, 1, 3, 2, 4, 7}), 7);
    }

    /**
     * 超时。
     */
    public int maxProfit(int[] prices) {
        return maxProfit(prices, prices.length - 2, 0, prices[prices.length - 1]);
    }

    // 从右往左扫描数组。计算在第 i 天时，根据后面的最大股票价格，返回所能获得的最大收益。
    private int maxProfit(int[] prices, int i, int profit, int maxPrice) {
        // 到达末尾时，返回挣得的收益
        if (i < 0)
            return profit;

        int p;
        // 如果当前股票价格大于等于后面的最大值，则无法交易
        if (prices[i] >= maxPrice)
            // 更新最大值为 prices[i]
            p = maxProfit(prices, i - 1, profit, prices[i]);
        else
            // 否则，有两种选择，看看哪种更好
            p = Math.max(
                    // 1. 现在就交易，消耗掉了最大值，因此传递 -1
                    maxProfit(prices, i - 1, profit + maxPrice - prices[i], -1),
                    // 2. 先不交易，将最大值传递下去
                    maxProfit(prices, i - 1, profit, maxPrice));
        return p;
    }

    @Test
    public void testMaxProfit() {
        test(this::maxProfit);
    }


    /**
     * 穷举法，用以说明问题框架。
     */
    public int exhaust(int[] prices) {
        return recur(prices, 0, prices.length - 1);
    }

    private int recur(int[] prices, int lo, int hi) {
        int res = 0;
        for (int buy = lo; buy <= hi - 1; buy++) {
            for (int sell = buy + 1; sell <= hi; sell++) {
                if (prices[sell] > prices[buy])
                    res = Math.max(res, prices[sell] - prices[buy] + recur(prices, sell + 1, hi));
                else
                    res = Math.max(res, recur(prices, sell + 1, hi));
            }
        }

        return res;
    }

    @Test
    public void testExhaust() {
        test(this::exhaust);
    }


    /**
     * 在穷举的基础上消除循环，增加备忘录。
     */
    public int betterDpMethod(int[] prices) {
        return betterDpMethod(prices, 0, new HashMap<>((int) (prices.length / 0.75)));
    }

    private int betterDpMethod(int[] prices, int lo, Map<Integer, Integer> memory) {
        if (lo >= prices.length - 1)
            return 0;
        int res = memory.getOrDefault(lo, -1);
        if (res != -1)
            return res;

        int curMin = prices[lo];
        for (int sell = lo + 1; sell <= prices.length - 1; sell++) {
            curMin = Math.min(curMin, prices[sell]);
            res = Math.max(res, prices[sell] - curMin + betterDpMethod(prices, sell + 1, memory));
        }
        memory.put(lo, res);

        return res;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterDpMethod);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/solution/mai-mai-gu-piao-de-zui-jia-shi-ji-ii-by-leetcode-s/
     *
     * LeetCode 耗时：1ms - 99.60%
     *          内存消耗：38.4 MB - 41.70%
     */
    public int greedyMethod(int[] prices) {
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1])
                maxProfit += prices[i] - prices[i - 1];
        }

        return maxProfit;
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }


    /**
     * 参见 {@link E188_Hard_BestTimeToBuyAndSellStockIV#compressMethod(int, int[])}。
     *
     * LeetCode 耗时：1ms - 99.60%
     *          内存消耗：37.9 MB - 97.18%
     */
    public int compressMethod(int[] prices) {
        final int days = prices.length;
        int noHold = 0, hold = Integer.MIN_VALUE;

        for (int i = 0; i < days; i++) {
            // k 无穷，则 k 和 k - 1 是一样的。
            // 注意先保存 noHold，防止值被改变
            int tmp = noHold;
            noHold = Math.max(noHold, hold + prices[i]);
            hold = Math.max(hold, tmp - prices[i]);
        }

        return noHold;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
