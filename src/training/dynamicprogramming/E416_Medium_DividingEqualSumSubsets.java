package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个「只包含正整数」的「非空」数组。是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
 *
 * 约束：
 * - 每个数组中的元素不会超过 100
 * - 数组的大小不会超过 200
 *
 * 例 1：
 * Input: [1, 5, 11, 5]
 * Output: true
 * Explanation: 数组可以分割成 [1, 5, 5] 和 [11].
 *
 * 例 2：
 * Input: [1, 2, 3, 5]
 * Output: false
 * Explanation: 数组不能分割成两个元素和相等的子集.
 */
public class E416_Medium_DividingEqualSumSubsets {

    static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{1, 5, 11, 5}));
        assertFalse(method.test(new int[]{1, 2, 3, 5}));
        assertFalse(method.test(new int[]{1, 2, 5}));
    }

    /**
     * 算法详解参见 README。
     */
    public boolean canPartition(int[] nums) {
        if (nums.length < 2)
            return false;
        if (nums.length == 2)
            return nums[0] == nums[1];

        int sum = 0;
        for (int num : nums)
            sum += num;
        if ((sum & 1) != 0)
            return false;

        // 如果能够能够装满背包（物品总容量的一半），则表示有解
        int n = nums.length, bagCap = sum >>> 1;
        // dp[i][w] 表示尝试将前 i 个数字装入容量为 w 的背包的所能达到的最大值
        int[][] dp = new int[n + 1][bagCap + 1];
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= bagCap; w++) {
                // 背包容量小于数字的值，只能舍弃，沿用上一次的结果
                if (w < nums[i - 1])
                    dp[i][w] = dp[i - 1][w];
                else
                    // 尝试将数字装入背包，比较和不装的值哪个大
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - nums[i - 1]] + nums[i - 1]);
            }
        }

        // 装满了背包表示有解
        return dp[n][bagCap] == bagCap;
    }

    @Test
    public void testCanPartition() {
        test(this::canPartition);
    }


    /**
     * 状态压缩。
     *
     * LeetCode 耗时：25 ms - 78.58%
     *          内存消耗：37.8 MB - 84.77%
     */
    public boolean compressMethod(int[] nums) {
        int sum = 0;
        for (int num : nums)
            sum += num;
        if ((sum & 1) != 0)
            return false;

        int n = nums.length, bagCap = sum >>> 1;
        // 压缩行
        int[] dp = new int[bagCap + 1];
        for (int i = 0; i < n; i++) {
            // 注意要倒着进行，因为右边的状态依赖于左边的状态
            for (int w = bagCap; w >= 1; w--) {
                if (w >= nums[i])
                    dp[w] = Math.max(dp[w], dp[w - nums[i]] + nums[i]);
            }
        }

        return dp[bagCap] == bagCap;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }


    public boolean betterMethod(int[] nums) {
        int sum = 0;
        for (int num : nums)
            sum += num;
        if ((sum & 1) != 0)
            return false;

        int n = nums.length, bagCap = sum >>> 1;
        boolean[] dp = new boolean[bagCap + 1];
        dp[0] = true;
        for (int i = 0; i < n; i++) {
            for (int w = bagCap; w >= 1; w--) {
                if (w >= nums[i])
                    dp[w] = dp[w] || dp[w - nums[i]];
            }
        }

        return dp[bagCap];
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
