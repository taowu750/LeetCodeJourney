package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1493. 删掉一个元素以后全为 1 的最长子数组: https://leetcode-cn.com/problems/longest-subarray-of-1s-after-deleting-one-element/
 *
 * 给你一个二进制数组 nums，你需要从中删掉一个元素。
 * 请你在删掉元素的结果数组中，返回最长的且只包含 1 的非空子数组的长度。
 * 如果不存在这样的子数组，请返回 0 。
 *
 * 例 1：
 * 输入：nums = [1,1,0,1]
 * 输出：3
 * 解释：删掉位置 2 的数后，[1,1,1] 包含 3 个 1 。
 *
 * 例 2：
 * 输入：nums = [0,1,1,1,0,1,1,0,1]
 * 输出：5
 * 解释：删掉位置 4 的数字后，[0,1,1,1,1,1,0,1] 的最长全 1 子数组为 [1,1,1,1,1] 。
 *
 * 例 3：
 * 输入：nums = [1,1,1]
 * 输出：2
 * 解释：你必须要删除一个元素。
 *
 * 例 4：
 * 输入：nums = [1,1,0,0,1,1,1,0,1]
 * 输出：4
 *
 * 例 5：
 * 输入：nums = [0,0,0]
 * 输出：0
 *
 * 说明：
 * - 1 <= nums.length <= 10^5
 * - nums[i] 要么是 0 要么是 1 。
 */
public class E1493_Medium_LongestSubarrayOf1AfterDeletingOneElement {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(3, method.applyAsInt(new int[]{1,1,0,1}));
        assertEquals(5, method.applyAsInt(new int[]{0,1,1,1,0,1,1,0,1}));
        assertEquals(2, method.applyAsInt(new int[]{1,1,1}));
        assertEquals(4, method.applyAsInt(new int[]{1,1,0,0,1,1,1,0,1}));
        assertEquals(0, method.applyAsInt(new int[]{0,0,0}));
        assertEquals(0, method.applyAsInt(new int[]{1}));
    }

    /**
     * 解法灵感来自 {@link training.pointer.E42_Hard_TrappingRainWater} 和 {@link training.stack.E84_Hard_LargestRectangleInHistogram}。
     *
     * 我们从每个元素出发，计算去掉它后所能得到的连续 1 的序列长度，明显可以想出用前缀后缀做。
     *
     * LeetCode 耗时：3 ms - 30.35%
     *          内存消耗：48.2 MB - 28.25%
     */
    public int longestSubarray(int[] nums) {
        final int n = nums.length;
        // left[i] 表示 i 左边连续 1 的数量
        int[] left = new int[n];
        for (int i = 1; i < n; i++) {
            if (nums[i - 1] == 1) {
                left[i] = left[i - 1] + 1;
            }
        }

        int result = 0;
        for (int i = n - 2, right = 0; i >= 0; i--) {
            if (nums[i + 1] == 1) {
                right += 1;
            } else {
                right = 0;
            }
            result = Math.max(result, left[i] + right);
        }

        return result;
    }

    @Test
    public void testLongestSubarray() {
        test(this::longestSubarray);
    }


    /**
     * 我们可以维持一个滑动窗口，里面最多包含一个 0。
     *
     * LeetCode 耗时：2 ms - 87.72%
     *          内存消耗：49.8 MB - 8.60%
     */
    public int slideWindowMethod(int[] nums) {
        // 滑动窗口中 0 的下标
        int zeroIdx = -1;
        int left = 0, right = 0, result = 0;
        while (right < nums.length) {
            if (nums[right++] == 0) {
                // 如果之前有 0 了，那么需要缩小滑动窗口的左边界
                if (zeroIdx != -1) {
                    // 注意此时的 right-1 是 0，所有 left 不能包含之前的 0
                    left = zeroIdx + 1;
                }
                zeroIdx = right - 1;
            }
            if (zeroIdx == -1) {
                result = Math.max(result, right - left);
            } else {
                result = Math.max(result, right - left - 1);
            }
        }

        // 如果全为 1，还是需要去掉一个元素
        return zeroIdx == -1 ? result - 1 : result;
    }

    @Test
    public void testSlideWindowMethod() {
        test(this::slideWindowMethod);
    }
}
