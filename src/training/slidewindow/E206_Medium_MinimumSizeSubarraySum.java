package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 209. 长度最小的子数组: https://leetcode-cn.com/problems/minimum-size-subarray-sum/
 *
 * 给定一个含有 n 个正整数的数组和一个正整数 target 。
 *
 * 找出该数组中满足其和 ≥ target 的长度最小的连续子数组 [numsl, numsl+1, ..., numsr-1, numsr] ，
 * 并返回其长度。如果不存在符合条件的子数组，返回 0 。
 *
 * 如果你已经实现 O(n) 时间复杂度的解法, 请尝试设计一个 O(n log(n)) 时间复杂度的解法。
 *
 * 例 1：
 * 输入：target = 7, nums = [2,3,1,2,4,3]
 * 输出：2
 * 解释：子数组 [4,3] 是该条件下的长度最小的子数组。
 *
 * 例 2：
 * 输入：target = 4, nums = [1,4,4]
 * 输出：1
 *
 * 例 3：
 * 输入：target = 11, nums = [1,1,1,1,1,1,1,1]
 * 输出：0
 *
 * 说明：
 * - 1 <= target <= 10^9
 * - 1 <= nums.length <= 10^5
 * - 1 <= nums[i] <= 10^5
 */
public class E206_Medium_MinimumSizeSubarraySum {

    public static void test(ToIntBiFunction<Integer, int[]> method) {
        assertEquals(2, method.applyAsInt(7, new int[]{2,3,1,2,4,3}));
        assertEquals(1, method.applyAsInt(4, new int[]{1,4,4}));
        assertEquals(0, method.applyAsInt(11, new int[]{1,1,1,1,1,1,1,1}));
        assertEquals(2, method.applyAsInt(5, new int[]{2,3,1,1,1,1,1}));
    }

    /**
     * 滑动窗口法。
     *
     * LeetCode 耗时：1 ms - 99.99%
     *          内存消耗：38.6 MB - 5.31%
     */
    public int minSubArrayLen(int target, int[] nums) {
        int sum = 0, left = 0, right = 0, minLen = -1;
        while (right < nums.length) {
            sum += nums[right++];
            if (sum >= target) {
                do {
                    if (minLen == -1 || minLen > right - left) {
                        minLen = right - left;
                    }
                    // 注意当左边界收缩时也需要检查 minLen
                    sum -= nums[left];
                    left++;
                } while (left < right && sum >= target);
            }
        }

        return minLen == -1 ? 0 : minLen;
    }

    @Test
    public void testMinSubArrayLen() {
        test(this::minSubArrayLen);
    }


    /**
     * O(n logn) 方法，前缀和+二分查找。
     *
     * LeetCode 耗时：3 ms - 24.48%
     *          内存消耗：38.3 MB - 71.24%
     */
    public int prefixMethod(int target, int[] nums) {
        int result = 0;
        int[] prefix = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
            if (prefix[i + 1] >= target) {
                int idx = Arrays.binarySearch(prefix, 0, i + 2, prefix[i + 1] - target);
                if (idx < 0) {
                    idx = -idx - 2;
                }
                if (result == 0 || result > i + 1 - idx) {
                    result = i + 1 - idx;
                }
            }
        }

        return result;
    }

    @Test
    public void testPrefixMethod() {
        test(this::prefixMethod);
    }
}
