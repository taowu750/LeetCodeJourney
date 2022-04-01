package training.backtracking;

import org.junit.jupiter.api.Test;
import training.binarysearch.E1723_Hard_FindMinimumTimeToFinishAllJobs;

import java.util.Arrays;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 698. 划分为k个相等的子集：https://leetcode-cn.com/problems/partition-to-k-equal-sum-subsets/
 *
 * 给定一个整数数组 nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。
 *
 * 例 1：
 * 输入： nums = [4, 3, 2, 3, 5, 2, 1], k = 4
 * 输出： True
 * 说明： 有可能将其分成 4 个子集（5），（1,4），（2,3），（2,3）等于总和。
 *
 * 约束：
 * - 1 <= k <= len(nums) <= 16
 * - 0 < nums[i] < 10000
 */
public class E698_Medium_PartitionKEqualSumSubsets {

    public static void test(BiPredicate<int[], Integer> method) {
        assertTrue(method.test(new int[]{4, 3, 2, 3, 5, 2, 1}, 4));
        assertFalse(method.test(new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3}, 8));
    }

    /**
     * 回溯算法。和 {@link E1723_Hard_FindMinimumTimeToFinishAllJobs} 的回溯算法一样。
     *
     * LeetCode 耗时：1 ms - 99.81%
     *          内存消耗：35.9 MB - 52.87%
     */
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0, max = 0;
        for (int num : nums) {
            sum += num;
            if (max < num)
                max = num;
        }
        int subSum = sum / k;
        if (sum % k != 0 || max > subSum)
            return false;

        // 从大的数字先放起，再放小的数字
        Arrays.sort(nums);
        int[] sub = new int[k];
        return dfs(nums, nums.length - 1, sub, subSum);
    }

    public boolean dfs(int[] nums, int i, int[] sub, int subSum) {
        if (i < 0) {
            /*
            这里不需要下面的检查，因为只要遍历完了数组，说明放置了所有的数字；
            并且所有 s[i] <= subSum (i ∈ [0, n))，则每个 s[i] 一定等于 subSum
             */
            /*for (int s : sub) {
                if (s != subSum)
                    return false;
            }*/
            return true;
        }

        int cur = nums[i];
        for (int j = 0; j < sub.length; j++) {
            if (sub[j] + cur <= subSum) {
                sub[j] += cur;
                if (dfs(nums, i - 1, sub, subSum))
                    return true;
                sub[j] -= cur;
            }
            // 这里的剪枝操作非常关键，不然会导致超时
            if (sub[j] == 0 || sub[j] + cur == subSum)
                break;
        }

        return false;
    }

    @Test
    public void testCanPartitionKSubsets() {
        test(this::canPartitionKSubsets);
    }
}
