package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素
 * 就是相邻的房屋装有相互连通的防盗系统，「如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警」。
 *
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你「不触动警报装置的情况下」，
 * 一夜之内能够偷窃到的最高金额。
 *
 * 例 1：
 * 输入：[1,2,3,1]
 * 输出：4
 * 解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
 *      偷窃到的最高金额 = 1 + 3 = 4 。
 *
 * 例 2：
 * 输入：[2,7,9,3,1]
 * 输出：12
 * 解释：偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
 *      偷窃到的最高金额 = 2 + 9 + 1 = 12 。
 *
 * 约束：
 * - 0 <= nums.length <= 100
 * - 0 <= nums[i] <= 400
 */
public class E198_Medium_HouseRobber {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{1,2,3,1}), 4);
        assertEquals(method.applyAsInt(new int[]{2,7,9,3,1}), 12);
    }

    /**
     * 类似于 {@link E300_Medium_LongestIncreasingSubsequence}。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.1 MB - 6.09%
     */
    public int rob(int[] nums) {
        if (nums.length == 0)
            return 0;

        final int n = nums.length;
        // dp[i] 表示偷窃了 nums[i] 所能获取的最大收益。和最长递增子序列的动规解法类似
        final int[] dp = new int[n];

        int max = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = nums[i];
            // 偷窃了 nums[i]，就只能选择前面隔一家或隔两家，从中挑选最大值
            dp[i] += Math.max(i - 2 >= 0 ? dp[i - 2] : 0, i - 3 >= 0 ? dp[i - 3] : 0);
            max = Math.max(dp[i], max);
        }

        return max;
    }

    @Test
    public void testRob() {
        test(this::rob);
    }


    /**
     * 状态压缩。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.9 MB - 45.44%
     */
    public int compressMethod(int[] nums) {
        if (nums.length == 0)
            return 0;

        int lastRob = 0, llastRob = 0, lllastRob = 0, max = 0;
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            cur += Math.max(i - 2 >= 0 ? llastRob : 0, i - 3 >= 0 ? lllastRob : 0);
            lllastRob = llastRob;
            llastRob = lastRob;
            lastRob = cur;
            max = Math.max(cur, max);
        }

        return max;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
