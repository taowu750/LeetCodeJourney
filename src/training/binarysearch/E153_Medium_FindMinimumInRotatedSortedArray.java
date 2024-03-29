package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 153. 寻找旋转排序数组中的最小值：https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array/
 *
 * 假设以升序排序的长度为 n 的数组在 1 到 n 之间向右旋转。例如，数组 nums = [0,1,2,4,5,6,7]可能变为：
 * - 旋转 4 次：[4,5,6,7,0,1,2]。
 * - 旋转 7 次：[0,1,2,4,5,6,7]。
 * <p>
 * 给定排序后的旋转数组 num，返回此数组的最小元素。
 * <p>
 * 例 1：
 * Input: nums = [3,4,5,1,2]
 * Output: 1
 * <p>
 * 例 2：
 * Input: nums = [4,5,6,7,0,1,2]
 * Output: 0
 * <p>
 * 例 3：
 * Input: nums = [11,13,15,17]
 * Output: 11
 * <p>
 * 约束：
 * - n == nums.length
 * - 1 <= n <= 5000
 * - -5000 <= nums[i] <= 5000
 * - nums 中的整数是唯一的
 * - 旋转次数在 [1, n] 范围内
 */
public class E153_Medium_FindMinimumInRotatedSortedArray {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{3,4,5,1,2}), 1);

        assertEquals(method.applyAsInt(new int[]{4,5,6,7,0,1,2}), 0);

        assertEquals(method.applyAsInt(new int[]{11,13,15,17}), 11);

        assertEquals(method.applyAsInt(new int[]{2,3,4,5,1}), 1);
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public int findMin(int[] nums) {
        int maxIdx = -1, lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                maxIdx = mid;
                break;
            } else if (nums[mid] > nums[nums.length - 1]) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return maxIdx == -1 ? nums[0] : nums[maxIdx + 1];
    }

    @Test
    public void testFindMin() {
        test(this::findMin);
    }


    /**
     * 循环不变式方法，简洁优雅。
     *
     * 有以下循环不变式：
     * - low < high
     * - mid != high && nums[mid] != nums[high]
     * - 最小值在 [low, high] 中
     *
     * 每次迭代后，不是 "high" 减少就是 "low" 增加，所以区间 [low, high] 总是会缩小。在循环中：
     * - 如果 nums[mid] < nums[high]，我们将知道最小值应在左侧，因此我们移动 high。
     *   我们总是可以使 high = mid，而不必担心循环不会结束。因为我们知道 high 永远不会与 mid 相同，
     *   因此 high = mid 将确保间隔在缩小。
     * - 否则，最小值应在右侧，因此我们移动 low。由于 nums[mid] > nums[high]，nums[mid] 不会是最小值，
     *   因此我们可以安全地设置 low = mid + 1，这也可以确保间隔在缩小。
     *
     * 注意，上面的收缩方式可以同时应对旋转次数大于 0 和旋转次数等于 0 这两种情况。
     */
    public int invariantMethod(int[] nums) {
        int low = 0, high = nums.length - 1;
        while (low < high) {
            int mid = (low + high) >>> 1;
            // 最小值在左半部分
            if (nums[mid] < nums[high])
                high = mid;
            // 最小值在右半部分
            else
                low = mid + 1;
        }

        return nums[low];
    }

    @Test
    public void testInvariantMethod() {
        test(this::invariantMethod);
    }
}
