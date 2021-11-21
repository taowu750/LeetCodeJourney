package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 628. 三个数的最大乘积: https://leetcode-cn.com/problems/maximum-product-of-three-numbers/
 *
 * 给你一个整型数组 nums ，在数组中找出由三个数组成的最大乘积，并输出这个乘积。
 *
 * 例 1：
 * 输入：nums = [1,2,3]
 * 输出：6
 *
 * 例 2：
 * 输入：nums = [1,2,3,4]
 * 输出：24
 *
 * 例 3：
 * 输入：nums = [-1,-2,-3]
 * 输出：-6
 *
 * 说明：
 * - 3 <= nums.length <= 10^4
 * - -1000 <= nums[i] <= 1000
 */
public class E628_Easy_MaximumProductOfThreeNumbers {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(6, method.applyAsInt(new int[]{1,2,3}));
        assertEquals(24, method.applyAsInt(new int[]{1,2,3,4}));
        assertEquals(-6, method.applyAsInt(new int[]{-1,-2,-3}));
        assertEquals(-6, method.applyAsInt(new int[]{-1,-2,-3,-4}));
    }

    /**
     * LeetCode 耗时：10 ms - 74.24%
     *          内存消耗：39.8 MB - 74.19%
     */
    public int maximumProduct(int[] nums) {
        Arrays.sort(nums);

        int n = nums.length;
        /*
        int lastNeg = bs1(nums, 0) - 1;
        int firstPos = bs2(nums, 0);
        // 如果没有正数或负数
        if (firstPos >= n || lastNeg < 0) {
            return nums[n - 1] * nums[n - 2] * nums[n - 3];
        } else {
            // 如果正数的数量大于等于 3
            if (n - firstPos >= 3) {
                // 最大的三个正数乘积
                int result = nums[n - 1] * nums[n - 2] * nums[n - 3];
                if (lastNeg >= 1) {
                    // 最小的两个负数和最大的一个正数乘积
                    result = Math.max(result, nums[0] * nums[1] * nums[n - 1]);
                }
                return result;
            } else if (lastNeg >= 1) {  // 如果负数的数量大于等于 2
                // 最小的两个负数和最大的一个正数乘积
                return nums[0] * nums[1] * nums[n - 1];
            } else if (lastNeg < firstPos - 1) {  // 如果有 0
                return 0;
            } else {  // 只有两个负数、一个正数
                return nums[0] * nums[1] * nums[2];
            }
        }*/

        return Math.max(nums[0] * nums[1] * nums[n - 1], nums[n - 3] * nums[n - 2] * nums[n - 1]);
    }

    /**
     * 寻找第一个大于等于 target 的下标
     */
    private int bs1(int[] nums, int target) {
        int lo = 0, hi = nums.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    /**
     * 寻找第一个大于 target 的下标
     */
    private int bs2(int[] nums, int target) {
        int lo = 0, hi = nums.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    @Test
    public void testMaximumProduct() {
        test(this::maximumProduct);
    }


    /**
     * LeetCode 耗时：2 ms - 99.47%
     *          内存消耗：40 MB - 8.54%
     */
    public int linearMethod(int[] nums) {
        // 找到最大的三个数和最小的两个数
        int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE, max3 = Integer.MIN_VALUE;
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;

        for (int num : nums) {
            if (num > max3) {
                max1 = max2;
                max2 = max3;
                max3 = num;
            } else if (num > max2) {
                max1 = max2;
                max2 = num;
            } else if (num > max1) {
                max1 = num;
            }

            if (num < min1) {
                min2 = min1;
                min1 = num;
            } else if (num < min2) {
                min2 = num;
            }
        }

        return Math.max(max1 * max2 * max3, max3 * min1 * min2);
    }

    @Test
    public void testLinearMethod() {
        test(this::linearMethod);
    }
}
