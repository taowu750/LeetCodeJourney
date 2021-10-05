package training.binarysearch;

import training.binarysearch.E704_Easy_BinarySearch.SearchMethod;
import org.junit.jupiter.api.Test;
import util.ArrayUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 33. 搜索旋转排序数组：https://leetcode-cn.com/problems/search-in-rotated-sorted-array/
 *
 * 给定一个整数数组 nums 以升序排序（具有不同的值）。
 * <p>
 * 在传递给函数之前，nums 以未知的枢轴索引 k（0 <= k < nums.length）旋转，
 * 使得结果数组为 [nums[k],nums[k + 1],...,nums[n-1],nums[0],nums [1],...,nums [k-1]]
 * （0索引）。例如，[0,1,2,4,5,6,7]可能会在枢轴索引3处旋转并变为[4,5,6,7,0,1,2]。
 * <p>
 * 给定旋转后的数组 nums 和一个整数 target，如果 target 在 nums 中，则返回 target 的索引；
 * 否则返回 -1。
 * <p>
 * 你能否保证 O(log n) 的时间复杂度？
 * <p>
 * 例 1：
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 * <p>
 * 例 2：
 * Input: nums = [4,5,6,7,0,1,2], target = 3
 * Output: -1
 * <p>
 * 例 3：
 * Input: nums = [1], target = 0
 * Output: -1
 * <p>
 * 约束：
 * - 1 <= nums.length <= 5000
 * - -10**4 <= nums[i] <= 10**4
 * - nums 中的整数都是唯一的
 * - nums 保证在某些枢轴上旋转
 * - -10**4 <= target <= 10**4
 */
public class E33_Medium_SearchInRotatedSortedArray {

    static void test(SearchMethod method) {
        assertEquals(method.search(new int[]{4,5,6,7,0,1,2}, 0), 4);

        assertEquals(method.search(new int[]{4,5,6,7,0,1,2}, 3), -1);

        assertEquals(method.search(new int[]{1}, 0), -1);

        int[] arr = new int[10001];
        Arrays.parallelSetAll(arr, i -> i);
        int[] pivots = new int[]{0, 50, 67, 733, 9876};
        Random random = new Random();
        for (int pivot : pivots) {
            int[] tmp = ArrayUtil.rotateArray(arr.clone(), pivot);

            for (int i = 0; i < arr.length / 100; i++) {
                int target = random.nextInt(arr.length);
                int idx = target - pivot >= 0 ? target - pivot :
                        arr.length + target - pivot;
                assertEquals(method.search(tmp, target), idx);
            }

            for (int i = -1; i >= -100; i--) {
                assertEquals(method.search(tmp, i), -1);
            }

            for (int i = arr.length; i <= arr.length + 100; i++) {
                assertEquals(method.search(tmp, i), -1);
            }
        }
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public int search(int[] nums, int target) {
        // 先确定最大值的位置
        int lo = 0, hi = nums.length - 1, k = -1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            // 如果数组被旋转了，最大值会大于右边的数
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                k = mid;
                break;
            }
            // 如果数组被旋转了，旋转后左边的数都大于右边的数
            else if (nums[mid] > nums[nums.length - 1]) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        lo = 0;
        hi = nums.length - 1;
        // 如果数组被旋转了，则可以缩减寻找范围
        if (k != -1) {
            if (target >= nums[0]) {
                hi = k;
            } else {
                lo = k + 1;
            }
        }

        // 二分查找
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return -1;
    }

    @Test
    public void testSearch() {
        test(this::search);
    }
}
