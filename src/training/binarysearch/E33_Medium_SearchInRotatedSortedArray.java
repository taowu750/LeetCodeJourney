package training.binarysearch;

import training.binarysearch.E704_Easy_BinarySearch.SearchMethod;
import org.junit.jupiter.api.Test;
import util.ArrayUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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
        int len = nums.length;
        int end = len - 1, lo = 0, hi = len - 1;
        // 先找到最大值位置。旋转数组前面部分所有值大于后面部分所有值。
        if (nums[0] > nums[len - 1]) {
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (mid - 1 >= 0 && nums[mid] < nums[mid - 1]) {
                    end = mid - 1;
                    break;
                } else if (mid + 1 < len && nums[mid] > nums[mid + 1]) {
                    end = mid;
                    break;
                } else if (nums[mid] < nums[len - 1]) {
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            }
        }
        int start = (end + 1) % len;
        if (target >= nums[start] && target <= nums[end]) {
            lo = 1;
            hi = 0;
            // 确定值在哪一部分
            if (target >= nums[0]) {
                lo = 0;
                hi = end;
            } else if (target <= nums[len - 1]) {
                lo = end + 1;
                hi = len - 1;
            }

            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (nums[mid] < target)
                    lo = mid + 1;
                else if (nums[mid] > target)
                    hi = mid - 1;
                else
                    return mid;
            }
        }

        return -1;
    }

    @Test
    public void testSearch() {
        test(this::search);
    }
}
