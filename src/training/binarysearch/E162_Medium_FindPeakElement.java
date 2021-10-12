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

    static void test(ToIntFunction<int[]> method) {
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
     * 设 lo = 0, hi = nums.length - 1。我们使用如下的不等式：
     *      nums[lo - 1] < num[lo] && nums[hi] > nums[hi + 1]
     *
     * [lo, hi] 将向中间缩小。[lo, hi] 之间的情况分为以下 3 类：
     * - nums[lo] > nums[lo + 1]。根据 nums[lo - 1] < nums[lo] 不等式，lo 是峰值。
     * - [lo, hi] 是个递增序列。那么根据 nums[hi] > nums[hi + 1] 不等式，hi 是峰值。
     * - [lo, hi] 先增加一段然后降低，那么它开始降低之前的点就是一个峰值，例如 2 5 6 3（6 是峰值）
     *
     * 如果上述不等式成立，则 [left，right] 之间至少存在一个峰值。现在我们需要显示两件事：
     * - 初始状态满足不等式。因为 nums[-1] = nums[n] = -∞
     * - 在循环的每个步骤中，不等式都会重新建立。mid = (lo + hi)/2，考虑循环中的代码，
     *   有以下 2 种情况：
     *   - nums[mid] < nums[mid + 1]。这样 [mid + 1，hi] 又满足不等式
     *   - nums[mid] > nums [mid + 1]。同样，[lo，mid] 满足不变式
     *
     * 结果，不等式被重新建立，并且在退出循环时也将保持不变。最后，我们有一个长度为 2 的间隔，
     * 即 hi = lo + 1。如果 nums[lo] > nums[hi]，我们得到 lo 是峰值；否则，hi 是峰值。
     */
    public int invariantMethod(int[] nums) {
        if (nums.length == 1)
            return 0;

        int lo = 0, hi = nums.length - 1;
        while (hi - lo > 1) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] < nums[mid + 1])
                lo = mid + 1;
            else
                hi = mid;
        }

        return (lo == nums.length - 1 || nums[lo] > nums[lo + 1]) ? lo : hi;
    }

    @Test
    public void testInvariantMethod() {
        test(this::invariantMethod);
    }
}
