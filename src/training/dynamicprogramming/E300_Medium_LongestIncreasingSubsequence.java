package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 300. 最长递增子序列: https://leetcode-cn.com/problems/longest-increasing-subsequence/
 *
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 * <p>
 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
 * <p>
 * 你可以设计时间复杂度为 O(n^2) 的解决方案吗？
 * 你能将算法的时间复杂度降低到 O(n log(n)) 吗?
 * <p>
 * 例 1：
 * Input：nums = [10,9,2,5,3,7,101,18]
 * Output：4
 * Explanation：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
 * <p>
 * 例 2：
 * 输入：nums = [0,1,0,3,2,3]
 * 输出：4
 * <p>
 * 例 3：
 * 输入：nums = [7,7,7,7,7,7,7]
 * 输出：1
 * <p>
 * 约束：
 * - 1 <= nums.length <= 2500
 * - -10**4 <= nums[i] <= 10**4
 */
public class E300_Medium_LongestIncreasingSubsequence {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{10, 9, 2, 5, 3, 7, 101, 18}), 4);
        assertEquals(method.applyAsInt(new int[]{0, 1, 0, 3, 2, 3}), 4);
        assertEquals(method.applyAsInt(new int[]{7, 7, 7, 7, 7, 7, 7}), 1);
    }

    /**
     * 动态规划解法：O(n^2)
     *
     * LeetCode 耗时：60 ms - 42.48%
     *          内存消耗：38.5 MB - 77.17%
     */
    public int lengthOfLIS(int[] nums) {
        // dp[i] 表示以 nums[i] 「结尾」的最长递增子序列的长度
        int[] dp = new int[nums.length];

        for (int i = 0; i < nums.length; i++) {
            dp[i] = 1;
            // 将 nums[i] 接到前面每个子序列试试看，从中选出最长的那个
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j])
                    dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }

        int max = dp[0];
        for (int i = 1; i < dp.length; i++)
            max = Math.max(dp[i], max);

        return max;
    }

    @Test
    public void testLengthOfLIS() {
        test(this::lengthOfLIS);
    }


    /**
     * 耐心排序：二分查找，O(n log(n))
     *
     * 详解参见：
     * https://leetcode-cn.com/problems/longest-increasing-subsequence/solution/dong-tai-gui-hua-er-fen-cha-zhao-tan-xin-suan-fa-p/
     *
     * LeetCode 耗时：2 ms - 99.04%
     *          内存消耗：38.9 MB - 35.55%
     */
    public int binarySearchMethod(int[] nums) {
        // 每堆牌最上面的牌
        int[] top = new int[nums.length];
        // 排队初始化为 0
        int piles = 0;

        for (int i = 0; i < nums.length; i++) {
            // 二分查找搜索匹配的最左边牌堆
            int lo = 0, hi = piles;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (nums[i] > top[mid])
                    lo = mid + 1;
                else
                    hi = mid;
            }
            // 没找到合适的牌堆，新建一堆
            if (lo == piles)
                piles++;
            // 将牌放到牌堆上面
            top[lo] = nums[i];
        }

        // 牌堆数就是 LIS 长度
        return piles;
    }

    @Test
    public void testBinarySearchMethod() {
        test(this::binarySearchMethod);
    }
}
