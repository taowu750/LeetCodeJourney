package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 80. 删除有序数组中的重复项 II: https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array-ii/
 * <p>
 * 给你一个有序数组 nums ，请你「原地」删除重复出现的元素，使每个元素「最多出现两次」，返回删除后数组的新长度。
 * <p>
 * 不要使用额外的数组空间，你必须在「原地」修改输入数组并在使用 O(1) 额外空间的条件下完成。
 * <p>
 * 例 1：
 * 输入：nums = [1,1,1,2,2,3]
 * 输出：5, nums = [1,1,2,2,3]
 * 解释：函数应返回新长度 length = 5, 并且原数组的前五个元素被修改为 1, 1, 2, 2, 3 。
 * 不需要考虑数组中超出新长度后面的元素。
 * <p>
 * 例 2：
 * 输入：nums = [0,0,1,1,1,1,2,3,3]
 * 输出：7, nums = [0,0,1,1,2,3,3]
 * 解释：函数应返回新长度 length = 7, 并且原数组的前五个元素被修改为 0, 0, 1, 1, 2, 3, 3 。
 * 不需要考虑数组中超出新长度后面的元素。
 * <p>
 * 说明：
 * - 1 <= nums.length <= 3 * 10^4
 * - -10^4 <= nums[i] <= 10^4
 * - nums 已按升序排列
 */
public class E80_Medium_RemoveDuplicatesFromSortedArrayII {

    public static void test(ToIntFunction<int[]> method) {
        int[] nums = new int[]{1, 1, 1, 2, 2, 3};
        assertArrayEquals(new int[]{1, 1, 2, 2, 3}, Arrays.copyOf(nums, method.applyAsInt(nums)));
        nums = new int[]{0, 0, 1, 1, 1, 1, 2, 3, 3};
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 3, 3}, Arrays.copyOf(nums, method.applyAsInt(nums)));
    }

    /**
     * 类似于 {@link E26_Easy_RemoveDuplicatesFromSortedArray}
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.4 MB - 73.90%
     */
    public int removeDuplicates(int[] nums) {
        final int n = nums.length;
        if (n < 3) {
            return n;
        }

        int j = 1;
        for (int i = 1, cnt = 1; i < n; i++) {
            if (nums[i] != nums[i - 1]) {
                nums[j++] = nums[i];
                cnt = 1;
            } else if (++cnt <= 2) {
                nums[j++] = nums[i];
            }
        }

        return j;
    }

    @Test
    public void testRemoveDuplicates() {
        test(this::removeDuplicates);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array-ii/solution/shan-chu-pai-xu-shu-zu-zhong-de-zhong-fu-yec2/
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.4 MB - 73.90%
     */
    public int slowFastPointerMethod(int[] nums) {
        if (nums.length <= 2) {
            return nums.length;
        }

        int slow = 2;
        for (int fast = 2; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow - 2]) {
                nums[slow++] = nums[fast];
            }
        }

        return slow;
    }

    @Test
    public void testSlowFastPointerMethod() {
        test(this::slowFastPointerMethod);
    }
}
