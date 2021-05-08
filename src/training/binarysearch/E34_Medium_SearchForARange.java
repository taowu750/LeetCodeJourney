package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个以升序排列的整数 nums 数组，请找到给定 target 的开始和结束位置。
 * 如果在数组中未找到 target，则返回 [-1，-1]。
 * 你能写出 O(log n) 时间复杂度的实现吗？
 * <p>
 * 例 1：
 * Input: nums = [5,7,7,8,8,10], target = 8
 * Output: [3,4]
 * <p>
 * 例 2：
 * Input: nums = [5,7,7,8,8,10], target = 6
 * Output: [-1,-1]
 * <p>
 * 例 3：
 * Input: nums = [], target = 0
 * Output: [-1,-1]
 * <p>
 * 约束：
 * - 0 <= nums.length <= 10**5
 * - -10**9 <= nums[i] <= 10**9
 * - nums 是非降序数组
 * - -10**9 <= target <= 10**9
 */
public class E34_Medium_SearchForARange {

    static void test(BiFunction<int[], Integer, int[]> method) {
        assertArrayEquals(method.apply(new int[]{5, 7, 7, 8, 8, 10}, 8), new int[]{3, 4});

        assertArrayEquals(method.apply(new int[]{5, 7, 7, 8, 8, 10}, 7), new int[]{1, 2});

        assertArrayEquals(method.apply(new int[]{5, 7, 7, 8, 8, 10}, 6), new int[]{-1, -1});

        assertArrayEquals(method.apply(new int[]{}, 0), new int[]{-1, -1});

        assertArrayEquals(method.apply(new int[]{1, 2, 3, 4, 5, 6, 7}, 5), new int[]{4, 4});
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0)
            return new int[]{-1, -1};

        int left = -1, right = -1;
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] < target)
                lo = mid + 1;
            else
                hi = mid;
        }
        if (nums[lo] == target)
            left = lo;

        // 利用之前已经找到的左边界
        if (left != -1) {
            lo = left;
            hi = nums.length - 1;
            while (hi - lo > 1) {
                int mid = (lo + hi) >>> 1;
                if (nums[mid] <= target)
                    lo = mid;
                else
                    hi = mid - 1;
            }
            if (nums[hi] == target)
                right = hi;
            else if (nums[lo] == target)
                right = lo;
        }

        return new int[]{left, right};
    }

    @Test
    public void testSearchRange() {
        test(this::searchRange);
    }
}
