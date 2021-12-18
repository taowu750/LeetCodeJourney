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
     * 细节是魔鬼。
     * <p>
     * 此题的难点在于：
     * - 如何正确地将后面的元素放到正确的位置
     * - 将元素覆盖后，不能影响元素数量的判断
     * <p>
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.4 MB - 73.90%
     */
    public int removeDuplicates(int[] nums) {
        int newIdx = 1;
        for (int i = 1, cnt = 1; i < nums.length; i++) {
            // 先计算重复元素个数
            if (nums[i] == nums[i - 1]) {
                cnt++;
            } else {
                cnt = 1;
            }
            /*
            再覆盖元素。
            - 计算数量之后，再覆盖元素才不会影响元素数量的判断
            - 要在移动 newIdx 之前覆盖元素
             */
            nums[newIdx] = nums[i];
            // 最后当重复元素个数小于等于 2 时，才增大 newIdx
            if (cnt <= 2) {
                newIdx++;
            }
        }

        return newIdx;
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
