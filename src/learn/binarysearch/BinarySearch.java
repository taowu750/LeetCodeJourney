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
