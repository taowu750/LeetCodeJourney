package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 162. 寻找峰值: https://leetcode-cn.com/problems/find-peak-element/
 *
 * 峰值元素是严格大于其相邻元素的元素。
 * <p>
 * 给定一个整数数组 nums，找到一个峰值元素，然后返回其索引。如果数组包含多个峰，
 * 则返回任意一个峰的索引。
 * <p>
 * 你可以假定 nums[-1] = nums[n] = -∞。
 * <p>
 * 你能找到 O(log n) 时间复杂度的实现吗？
 * <p>
 * 例 1：
 * Input: nums = [1,2,3,1]
 * Output: 2
 * <p>
 * 例 2：
 * Input: nums = [1,2,1,3,5,6,4]
 * Output: 5
 * Explanation: 可以返回 1 或 5
 * <p>
 * 约束：
 * - 1 <= nums.length <= 1000
 * - -2**31 <= nums[i] <= 2**31 - 1
 * - 所有相邻元素不相等，即 nums[i] != nums[i + 1]
 */
public class E162_Medium_FindPeakElement {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{1, 2, 3, 1}), 2);

        assertTrue(new HashSet<>(Arrays.asList(1, 5)).contains(
                method.applyAsInt(new int[]{1,2,1,3,5,6,4})));
    }

    /**
     * 使用递归进行二分搜索。
     *
     * LeetCode 耗时：0ms - 100%
     */
    public int findPeakElement(int[] nums) {
        return findPeakElement(nums, 0, nums.length - 1);
    }

    private int findPeakElement(int[] nums, int lo, int hi) {
        if (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if ((mid - 1 < 0 || nums[mid - 1] < nums[mid])
                    && (mid + 1 >= nums.length || nums[mid + 1] < nums[mid]))
                return mid;
            int result = findPeakElement(nums, lo, mid - 1);
            if (result != -1)
                return result;
            return findPeakElement(nums, mid + 1, hi);
        }

        return -1;
    }

    @Test
    public void testFindPeakElement() {
        test(this::findPeakElement);
    }


    /**
     * 参见 https://leetcode.cn/problems/find-peak-element/solution/gong-shui-san-xie-noxiang-xin-ke-xue-xi-qva7v/
     */
    public int betterMethod(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] < nums[mid + 1])
                lo = mid + 1;
            else
                hi = mid;
        }

        return lo;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
