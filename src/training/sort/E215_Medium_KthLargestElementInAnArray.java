package training.sort;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 215. 数组中的第K个最大元素: https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
 *
 * 在未排序的数组中找到第 k 个最大的元素。请注意，你需要找的是数组排序后的第 k 个最大的元素，
 * 而不是第 k 个不同的元素。
 *
 * 例 1：
 * 输入: [3,2,1,5,6,4] 和 k = 2
 * 输出: 5
 *
 * 例 2：
 * 输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
 * 输出: 4
 *
 * 约束：
 * - 1 ≤ k ≤ 数组的长度
 */
public class E215_Medium_KthLargestElementInAnArray {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(5, method.applyAsInt(new int[]{3,2,1,5,6,4}, 2));
        assertEquals(4, method.applyAsInt(new int[]{3,2,3,1,2,4,5,5,6}, 4));
        assertEquals(2, method.applyAsInt(new int[]{-1,2,0}, 1));
        assertEquals(1, method.applyAsInt(new int[]{1}, 1));
        assertEquals(9, method.applyAsInt(new int[]{9,3}, 1));
        assertEquals(3, method.applyAsInt(new int[]{9,3}, 2));
    }

    /**
     * 类似于快速排序，快速选择算法。
     *
     * LeetCode 耗时：1 ms - 99.25%
     *          内存消耗：38.9 MB - 33.16%
     */
    public int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        int lo = 0, hi = nums.length - 1;
        for (;;) {
            // 注意快速选择的边界条件
            if (lo == hi)
                return nums[lo];

            int mid = (lo + hi) >>> 1;
            if (nums[lo] > nums[mid])
                exch(nums, lo, mid);
            if (nums[mid] > nums[hi])
                exch(nums, mid, hi);
            if (nums[lo] > nums[mid])
                exch(nums, lo, mid);
            exch(nums, lo, mid);

            int pivot = nums[lo];
            int i = lo, j = hi;
            for (;;) {
                while (nums[++i] < pivot);
                while (nums[--j] > pivot);

                if (i >= j)
                    break;
                exch(nums, i, j);
            }
            exch(nums, lo, j);

            if (j == n - k)
                return nums[j];
            else if (j < n - k)
                lo = j + 1;
            else
                hi = j - 1;
        }
    }

    private void exch(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    @Test
    public void testFindKthLargest() {
        test(this::findKthLargest);
    }
}
