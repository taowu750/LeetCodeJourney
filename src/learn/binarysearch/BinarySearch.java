package learn.binarysearch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个排序的（按升序排列）n个元素的整数数组 nums 和一个 target，
 * 编写一个函数在 nums 中搜索 target。如果 target 存在，则返回其索引，否则返回-1。
 *
 * 例 1：
 * Input: nums = [-1,0,3,5,9,12], target = 9
 * Output: 4
 *
 * 例 2：
 * Input: nums = [-1,0,3,5,9,12], target = 2
 * Output: -1
 *
 * 约束：
 * - nums 中的所有值都是唯一的
 * - n 的范围为 [1, 10000]
 * - nums 中的值的范围为 [-9999, 9999]
 */
public class BinarySearch {

    interface SearchMethod {
        int search(int[] nums, int target);
    }

    static void test(SearchMethod method) {
        assertEquals(method.search(new int[]{-1,0,3,5,9,12}, 9), 4);

        assertEquals(method.search(new int[]{-1,0,3,5,9,12}, 2), -1);

        assertEquals(method.search(new int[]{-1,0,3,5,9,12}, -1), 0);

        assertEquals(method.search(new int[]{-1,0,3,5,9,12}, 12), 5);

        assertEquals(method.search(new int[]{1}, 1), 0);

        assertEquals(method.search(new int[]{1}, -1), -1);
    }

    /**
     * 循环不变式：
     * - 如果 target 存在初始数组 [0, n-1]，那么它一定在 [lo, hi] 中。
     *
     * 初始化：
     * - 第一轮循环开始之前，处理的数组就是原始数组，这时显然成立。
     *
     * 保持：
     * - 对于 array[mid]==target，查找到了 target 对应的下标，直接返回。
     * - 对于 array[mid] < target，array[lo, ..., mid] 均小于 target，
     *   target 只可能存在于 array[mid+1, ..., hi] 中。
     * - 对于 array[mid] > target，array[mid, ..., hi] 均大于 target，
     *   target 只可能存在于 array[lo, ..., mid-1] 中。
     * - 在前两种情况中，数组长度每次至少减少 1（实际减少的长度分别是 mid-lo+1 和 hi-mid+1），
     *   直到由1（lo==hi）变为0（lo>hi），不会发生死循环。
     *
     * 终止：
     * - 结束时，lo > hi，待处理数组为空，表示 target 不存在于所有步骤的待处理数组，
     *   再结合每一步排除的部分数组中也不可能有 target，因此 target 不存在于原数组。
     */
    public int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] < target)
                lo = mid + 1;
            else if (nums[mid] > target)
                hi = mid - 1;
            else
                return mid;
        }

        return -1;
    }

    @Test
    public void testSearch() {
        test(this::search);
    }
}
