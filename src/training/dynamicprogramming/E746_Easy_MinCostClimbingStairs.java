package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 746. 使用最小花费爬楼梯: https://leetcode-cn.com/problems/min-cost-climbing-stairs/
 *
 * 数组的每个下标作为一个阶梯，第 i 个阶梯对应着一个非负数的体力花费值 cost[i]（下标从 0 开始）。
 * 每当你爬上一个阶梯你都要花费对应的体力值，一旦支付了相应的体力值，你就可以选择向上爬一个阶梯或者爬两个阶梯。
 * 请你找出达到楼层顶部的最低花费。在开始时，你可以选择从下标为 0 或 1 的元素作为初始阶梯。
 *
 * 例 1：
 * 输入：cost = [10, 15, 20]
 * 输出：15
 * 解释：最低花费是从 cost[1] 开始，然后走两步即可到阶梯顶，一共花费 15 。
 *
 * 例 2：
 * 输入：cost = [1, 100, 1, 1, 1, 100, 1, 1, 100, 1]
 * 输出：6
 * 解释：最低花费方式是从 cost[0] 开始，逐个经过那些 1 ，跳过 cost[3] ，一共花费 6 。
 *
 * 约束：
 * - cost 的长度范围是 [2, 1000]。
 * - cost[i] 将会是一个整型数据，范围为 [0, 999] 。
 */
public class E746_Easy_MinCostClimbingStairs {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(15, method.applyAsInt(new int[]{10, 15, 20}));
        assertEquals(6, method.applyAsInt(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
        assertEquals(2, method.applyAsInt(new int[]{5, 2}));
    }

    public int minCostClimbingStairs(int[] cost) {
        Map<Integer, Integer> cache = new HashMap<>((int) (cost.length / 0.75) + 1);
        return dp(cost, cost.length, cache);
    }

    private int dp(int[] cost, int n, Map<Integer, Integer> cache) {
        // dp(n) = min(dp(n-1), dp(n-2))
        // 下面的递归调用式已经加上 cost[n] 了，这里就不重复调用了
        if (n < 2) {
            return 0;
        }

        int ret = cache.getOrDefault(n, -1);
        if (ret != -1) {
            return ret;
        }

        ret = Math.min(dp(cost, n - 1, cache) + cost[n - 1], dp(cost, n - 2, cache) + cost[n - 2]);
        cache.put(n, ret);

        return ret;
    }

    @Test
    public void testMinCostClimbingStairs() {
        test(this::minCostClimbingStairs);
    }


    /**
     * LeetCode 耗时：0 ms - 100%
     *          内存消耗：39 MB - 56%
     */
    public int dpMethod(int[] cost) {
        // dp(n) = min(dp(n-1), dp(n-2))
        int dp1 = 0, dp2 = 0;
        for (int i = 2; i <= cost.length; i++) {
            int dpn = Math.min(dp1 + cost[i - 1], dp2 + cost[i - 2]);
            dp2 = dp1;
            dp1 = dpn;
        }

        return dp1;
    }
}
