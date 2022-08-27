package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 540. 有序数组中的单一元素: https://leetcode-cn.com/problems/single-element-in-a-sorted-array/
 *
 * 给定一个只包含整数的有序数组，每个元素都会出现两次，唯有一个数只会出现一次，找出这个数。
 *
 * 采用的方案可以在 O(log n) 时间复杂度和 O(1) 空间复杂度中运行吗？
 *
 * 例 1：
 * 输入: nums = [1,1,2,3,3,4,4,8,8]
 * 输出: 2
 *
 * 例 2：
 * 输入: nums =  [3,3,7,7,10,11,11]
 * 输出: 10
 *
 * 说明：
 * - 1 <= nums.length <= 10^5
 * - 0 <= nums[i] <= 10^5
 */
public class E540_Medium_SingleElementInSortedArray {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(2, method.applyAsInt(new int[]{1,1,2,3,3,4,4,8,8}));
        assertEquals(10, method.applyAsInt(new int[]{3,3,7,7,10,11,11}));
        assertEquals(5, method.applyAsInt(new int[]{5}));
    }

    /**
     * 通过判断左半边序列长度的奇偶性，可以使用二分法解决问题
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：43.6 MB - 5.01%
     */
    public int singleNonDuplicate(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = (l + r) / 2;
            // 如果 mid 是奇数，那么 [0,mid] 的子数组长度是偶数，并且 mid-1 一定存在
            // 所以检查 nums[mid] 和 nums[mid-1] 是否相等确定结果在哪一半边
            if ((mid & 1) == 1) {
                if (nums[mid] == nums[mid - 1]) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            } else {
                // 如果 mid 是奇数，那么 [0,mid] 的子数组长度是奇数，并且 mid+1 一定存在（l != r）
                // 所以检查 nums[mid] 和 nums[mid+1] 是否相等确定结果在哪一半边
                if (nums[mid] == nums[mid + 1]) {
                    l = mid + 2;
                } else {
                    r = mid;
                }
            }
        }

        return nums[l];
    }

    @Test
    public void testSingleNonDuplicate() {
        test(this::singleNonDuplicate);
    }


    /**
     * 更加简洁的方法，只检查偶数下标。将两种情况化简到一种情况
     */
    public int conciseMethod(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (mid % 2 == 1) {
                mid--;
            }
            if (nums[mid] == nums[mid + 1]) {
                lo = mid + 2;
            } else {
                hi = mid - 1;
            }
        }

        return nums[lo];
    }

    @Test
    public void testConciseMethod() {
        test(this::conciseMethod);
    }
}
