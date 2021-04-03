package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 *
 * 你只能选择「某一天」买入这只股票，并选择在「未来的某一个不同的日子」卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0。
 *
 * 例 1：
 * 输入：[7,1,5,3,6,4]
 * 输出：5
 * 解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5。
 *      注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
 *
 * 例 2：
 * 输入：prices = [7,6,4,3,1]
 * 输出：0
 * 解释：在这种情况下, 没有交易完成, 所以最大利润为 0。
 *
 * 约束：
 * - 1 <= prices.length <= 10**5
 * - 0 <= prices[i] <= 10**4
 */
public class E121_Easy_BestTimeToBuyAndSellStock {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{7,1,5,3,6,4}), 5);
        assertEquals(method.applyAsInt(new int[]{7,6,4,3,1}), 0);
        assertEquals(method.applyAsInt(new int[]{7,3,5,1,6,4}), 5);
        assertEquals(method.applyAsInt(new int[]{1,2,3,0,2}), 2);
    }

    /**
     * LeetCode 耗时：2 ms - 91.99%
     *          内存消耗：52.4 MB - 18.20%
     */
    public int maxProfit(int[] prices) {
        int maxPrice = prices[prices.length - 1], maxProfit = 0;
        // 从右往左，记录遇到的最大值和最好利润
        for (int i = prices.length - 2; i >= 0; i--) {
            if (prices[i] < maxPrice)
                maxProfit = Math.max(maxPrice - prices[i], maxProfit);
            else
                maxPrice = prices[i];
        }

        return maxProfit;
    }

    @Test
    public void testMaxProfit() {
        test(this::maxProfit);
    }


    /**
     * 参见 {@link E188_Hard_BestTimeToBuyAndSellStockIV#compressMethod(int, int[])}。
     *
     * LeetCode 耗时：1 ms - 99.71%
     *          内存消耗：51.4 MB - 54.85%
     */
    public int compressMethod(int[] prices) {
        final int days = prices.length;
        int noHold = 0, hold = Integer.MIN_VALUE;

        for (int i = 0; i < days; i++) {
            noHold = Math.max(noHold, hold + prices[i]);
            // 注意，k == 1，所以已经不需要任何 k 了，或者说 dp[j-1][0] 恒等于 0，因此这里直接就是 -prices[i]
            hold = Math.max(hold, -prices[i]);
        }

        return noHold;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
