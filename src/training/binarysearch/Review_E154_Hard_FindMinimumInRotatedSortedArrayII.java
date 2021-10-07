package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 154. 寻找旋转排序数组中的最小值 II: https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array-ii/
 *
 * 假设以升序排序的数组在事先未知的某个元素上旋转。例如[0,1,2,4,5,6,7]可能会变成[4,5,6,7,0,1,2]。
 * 找到最小的元素。注意数组可能包含重复值。
 * <p>
 * 例 1：
 * Input: [1,3,5]
 * Output: 1
 * <p>
 * 例 2：
 * Input: [2,2,2,0,1]
 * Output: 0
 */
public class Review_E154_Hard_FindMinimumInRotatedSortedArrayII {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{1, 3, 5}), 1);

        assertEquals(method.applyAsInt(new int[]{2, 2, 2, 0, 1}), 0);

        assertEquals(method.applyAsInt(new int[]{2, 2, 2, 2, 2}), 2);

        assertEquals(method.applyAsInt(new int[]{2, 2, 2, 0, 2}), 0);

        assertEquals(method.applyAsInt(new int[]{2, 2, 3, 4, 4, 5, 6, 6, 1, 2}), 1);

        assertEquals(method.applyAsInt(new int[]{1, 3, 3}), 1);

        assertEquals(method.applyAsInt(new int[]{1, 10, 10, 10, 10}), 1);

        assertEquals(method.applyAsInt(new int[]{2, 2, 2, 2, 2, 3, 0, 2, 2}), 0);
    }

    /**
     * LeetCode 耗时：2ms - 0%
     */
    public int findMin(int[] nums) {
        return findMin(nums, 0, nums.length - 1);
    }

    public int findMin(int[] nums, int lo, int hi) {
        int leftMin = Integer.MAX_VALUE, rightMin = Integer.MAX_VALUE;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] > nums[hi])
                lo = mid + 1;
            else if (nums[mid] < nums[hi])
                hi = mid;
            else {
                leftMin = findMin(nums, lo, mid);
                rightMin = findMin(nums, mid + 1, hi);
                break;
            }
        }

        return Math.min(Math.min(nums[lo], leftMin), rightMin);
    }

    @Test
    public void testFindMin() {
        test(this::findMin);
    }


    /**
     * 参见 {@link Review_E153_Medium_FindMinimumInRotatedSortedArray#invariantMethod(int[])}。
     *
     * LeetCode 耗时：0ms - 100%
     */
    public int betterMethod(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] > nums[hi])
                lo = mid + 1;
            else if (nums[mid] < nums[hi])
                hi = mid;
            else {
                // 对于 1 1 1 1 1 1 1 1 2 1 1 或类似的情况，最小值下标应该是 9，
                // 但是如果没有下面的 if，只有 hi--，得出的结果会是 0。
                // 为了避免这种情况，添加以下判断
                if (nums[hi - 1] > nums[hi]) {
                    lo = hi;
                    break;
                }
                // 当 num[mid] == num[hi] 时，我们不能确定最小值在 mid 的左侧还是右侧的位置，
                // 因此，让上限减小 1。
                hi--;
            }
        }

        return nums[lo];
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
