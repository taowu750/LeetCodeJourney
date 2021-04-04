package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都「围成一圈」，
 * 这意味着第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警 。
 * <p>
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你「在不触动警报装置的情况下」，能够偷窃到的最高金额。
 * <p>
 * 此题类似于 {@link E198_Medium_HouseRobber}。
 * <p>
 * 例 1：
 * 输入：nums = [2,3,2]
 * 输出：3
 * 解释：你不能先偷窃 1 号房屋（金额 = 2），然后偷窃 3 号房屋（金额 = 2）, 因为他们是相邻的。
 * <p>
 * 例 2：
 * 输入：nums = [1,2,3,1]
 * 输出：4
 * 解释：你可以先偷窃 1 号房屋（金额 = 1），然后偷窃 3 号房屋（金额 = 3）。
 *      偷窃到的最高金额 = 1 + 3 = 4 。
 * <p>
 * 例 3：
 * 输入：nums = [0]
 * 输出：0
 * <p>
 * 约束：
 * - 1 <= nums.length <= 100
 * - 0 <= nums[i] <= 1000
 */
public class E213_Medium_HouseRobberII {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{2, 3, 2}), 3);
        assertEquals(method.applyAsInt(new int[]{1, 2, 3, 1}), 4);
        assertEquals(method.applyAsInt(new int[]{0}), 0);
        assertEquals(method.applyAsInt(new int[]{4, 7, 5, 4, 3, 2, 6}), 17);
        assertEquals(method.applyAsInt(new int[]{1, 2, 3, 4, 5, 1, 2, 3, 4, 5}), 16);
    }

    /**
     * 参见 https://mp.weixin.qq.com/s/z44hk0MW14_mAQd7988mfw
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.9 MB - 50.06%
     */
    public int rob(int[] nums) {
        if (nums.length == 0)
            return 0;
        if (nums.length == 1)
            return nums[0];

        // 我们选 [0, n-2] 和 [1, n-1] 中的最大值，这样就不会同时选中首尾了
        return Math.max(rob(nums, 0, nums.length - 2),
                rob(nums, 1, nums.length - 1));
    }

    private int rob(int[] nums, int lo, int hi) {
        // 这里我们倒着来
        // 记录 dp[i+1] 和 dp[i+2]
        int dp_i_1 = 0, dp_i_2 = 0;
        // 记录 dp[i]
        int dp_i = 0;
        for (int i = hi; i >= lo; i--) {
            dp_i = Math.max(dp_i_1, nums[i] + dp_i_2);
            dp_i_2 = dp_i_1;
            dp_i_1 = dp_i;
        }
        return dp_i;
    }

    @Test
    public void testRob() {
        test(this::rob);
    }
}
