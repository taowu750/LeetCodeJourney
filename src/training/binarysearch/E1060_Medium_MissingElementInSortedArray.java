package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1060. 有序数组中的缺失元素: https://leetcode-cn.com/problems/missing-element-in-sorted-array/
 *
 * 现有一个按「升序」排列的整数数组 nums ，其中每个数字都「互不相同」。
 * 给你一个整数 k ，请你找出并返回从数组最左边开始的第 k 个缺失数字。
 *
 * 你可以设计一个对数时间复杂度（即，O(log(n))）的解决方案吗？
 *
 * 例 1：
 * 输入：nums = [4,7,9,10], k = 1
 * 输出：5
 * 解释：第一个缺失数字为 5 。
 *
 * 例 2：
 * 输入：nums = [4,7,9,10], k = 3
 * 输出：8
 * 解释：缺失数字有 [5,6,8,...]，因此第三个缺失数字为 8 。
 *
 * 例 3：
 * 输入：nums = [1,2,4], k = 3
 * 输出：6
 * 解释：缺失数字有 [3,5,6,7,...]，因此第三个缺失数字为 6 。
 *
 * 说明：
 * - 1 <= nums.length <= 5 * 10^4
 * - 1 <= nums[i] <= 10^7
 * - nums 按「升序」排列，其中所有元素「互不相同」。
 * - 1 <= k <= 10^8
 */
public class E1060_Medium_MissingElementInSortedArray {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(5, method.applyAsInt(new int[]{4,7,9,10}, 1));
        assertEquals(8, method.applyAsInt(new int[]{4,7,9,10}, 3));
        assertEquals(6, method.applyAsInt(new int[]{1,2,4}, 3));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：44.5 MB - 54.58%
     */
    public int missingElement(int[] nums, int k) {
        /*
        设 missing[i] 表示 nums[0]~nums[i] 中缺失的数字数量，有
            missing[i] = nums[i] - nums[0] - i

        当遇到 missing[i-1] < k && missing[i] == k 时，则找到了最终的结果

        思考这类问题时，不妨先想想对每个元素会得出什么值，以及为了得到最后的结果应该定位到哪里
         */

        int lo = 0, hi = nums.length - 1;
        // 如果 k 比 nums 中的空位数量还大，那么就从最大值往后数
        if (missing(nums, hi) < k) {
            return nums[hi] + k - missing(nums, hi);
        }

        // 否则第 k 个空位肯定在数组中
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int missing = missing(nums, mid);
            if (missing >= k) {
                hi = mid - 1;
                if (missing == k && missing(nums, mid - 1) < k) {
                    break;
                }
            } else {
                lo = mid + 1;
            }
        }

        return nums[hi] + k - missing(nums, hi);
    }

    private int missing(int[] nums, int idx) {
        return nums[idx] - nums[0] - idx;
    }

    @Test
    public void testMissingElement() {
        test(this::missingElement);
    }
}
